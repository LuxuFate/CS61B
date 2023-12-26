package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Matthew Lu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigmaM = readConfig();
        String settings = _input.nextLine();
        setUp(enigmaM, settings);
        String result = "";
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            if (next.equals("")) {
                result += "\n";
            } else if (!next.startsWith("*")) {
                result += enigmaM.convert(next) + "\n";
            } else {
                setUp(enigmaM, next);
            }
        }
        printMessageLine(result);
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int numRotors = 0;
            int numPawls = 0;
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();

            String checkAl = _config.nextLine();
            if (checkAl.contains("*") || checkAl.contains("(")
                    || checkAl.contains(")")) {
                throw new EnigmaException("Alphabet in "
                        + "Configuration has the wrong format");
            } else {
                _alphabet = new Alphabet(checkAl);
            }
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of rotors "
                        + "in Configuration has the wrong format");
            } else {
                numRotors = _config.nextInt();
                if (numRotors <= 0) {
                    throw new EnigmaException("Number of rotors "
                            + "must be greater than 0");
                }
            }
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of pawls in "
                        + "Configuration has the wrong format");
            } else {
                numPawls = _config.nextInt();
                if (numPawls <= 0) {
                    throw new EnigmaException("Number of "
                            + "pawls must be greater than 0");
                }
                if (numRotors < numPawls) {
                    throw new EnigmaException("Number of rotors must be "
                            + "greater than or equal to the number of pawls");
                }
            }
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("Configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next().toUpperCase();
            if (name.contains("(") || name.contains(")")) {
                throw new EnigmaException("Rotor name must "
                        + "not have parentheses");
            }
            String type = _config.next().toUpperCase();
            String cycles = "";
            while (_config.hasNext("\\(.+\\)")) {
                String addspace = _config.next();
                for (int i = 0; i < addspace.length() - 1; i++) {
                    if (addspace.charAt(i) == ')') {
                        addspace = addspace.substring(0, i)
                                + " " + addspace.substring(i);
                        i++;
                    }
                }
                cycles += " " + addspace;
            }
            Permutation perm = new Permutation(cycles.trim(), _alphabet);

            if (type.charAt(0) == 'M') {
                String notches = "";
                for (int i = 1; i <= type.length() - 1; i++) {
                    notches += type.charAt(i);
                }
                return new MovingRotor(name, perm, notches);
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else if (type.charAt(0) == 'R') {
                return new Reflector(name, perm);
            } else {
                throw new EnigmaException("Configuration wrong format: "
                        + "Type of Rotor not stated");
            }
        } catch (NoSuchElementException excp) {
            throw error("Bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        if (!settings.startsWith("*")) {
            throw new EnigmaException("Settings in wrong format: "
                    + "'*' must be in first column");
        }
        settings = settings.replace("* ", "");
        String[] setting = settings.split(" +");

        String[] rotors = new String[M.numRotors()];

        for (int i = 0; i < rotors.length; i++) {
            rotors[i] = setting[i];
        }
        M.insertRotors(rotors);

        int ringSet = 1;
        if (M.numRotors() + 1 < setting.length) {
            if (!setting[M.numRotors() + 1].startsWith("(")) {
                String ringSettings = setting[M.numRotors() + 1];
                ringSet = 2;
                M.ringSetted();
                M.setRingSetting(ringSettings);
            }
        }

        String rotorSettings = setting[M.numRotors()];
        M.setRotors(rotorSettings);

        String cycle = "";
        for (int i = M.numRotors() + ringSet; i < setting.length; i++) {
            cycle += (" " + setting[i]);
        }
        M.setPlugboard(new Permutation(cycle.trim(), _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int count = 5;
        String print = "";
        Scanner scan = new Scanner(msg);
        while (scan.hasNextLine()) {
            String next = scan.nextLine();
            for (int i = 0; i < next.length(); i++) {
                count--;
                print += next.charAt(i);
                if (count == 0) {
                    print += " ";
                    count = 5;
                }
            }
            _output.println(print);
            count = 5;
            print = "";
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
