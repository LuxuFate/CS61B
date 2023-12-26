package enigma;

import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Matthew Lu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        this._alphabet = alpha;
        this._numRotors = numRotors;
        this._numPawls = pawls;
        this._allRotors = (ArrayList<Rotor>) allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _myRotors = new ArrayList<Rotor>();
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.size(); j++) {
                if (rotors[i].toUpperCase().equals(_allRotors.get(j).name())) {
                    if (_allRotors.get(j).reflecting()) {
                        Reflector temp = new Reflector(_allRotors.get(j)
                                .name(), _allRotors.get(j).permutation());
                        _myRotors.add(temp);
                    } else if (_allRotors.get(j).rotates()) {
                        MovingRotor notchgetter =
                                (MovingRotor) _allRotors.get(j);
                        MovingRotor temp = new MovingRotor(notchgetter
                                .name(), notchgetter.permutation(),
                                notchgetter.notches());
                        _myRotors.add(temp);
                    } else {
                        FixedRotor temp = new FixedRotor(_allRotors.get(j)
                                .name(), _allRotors.get(j).permutation());
                        _myRotors.add(temp);
                    }
                }
            }
        }
        if (!_myRotors.get(0).reflecting()) {
            throw new EnigmaException("The first rotor must be a Reflector");
        }
        if (_myRotors.size() != _numRotors) {
            throw new EnigmaException("Settings contain "
                    + "the wrong number of arguments");
        }
        int numMove = 0;
        for (int i = 0; i < _myRotors.size(); i++) {
            if (_myRotors.get(i).rotates()) {
                numMove++;
            }
        }
        if (_numPawls != numMove) {
            throw new EnigmaException("Settings wrong arguments: "
                    + "Number of moving rotors are wrong");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Setting must be "
                    + "a string of numRotors()-1 character");
        } else {
            for (int i = 0; i < setting.length(); i++) {
                if (!_alphabet.contains(setting.charAt(i))) {
                    throw new EnigmaException("Setting must "
                            + "be a string in my alphabet");
                }
            }
        }

        for (int i = 1; i <= _numRotors - 1; i++) {
            _myRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugBoard = plugboard;
    }

    /** Set the ringsettings of the rotors.
     * @param ringSetting the ringSetting to set it to*/
    void setRingSetting(String ringSetting) {
        if (ringSetting.length() != _numRotors - 1) {
            throw new EnigmaException("Ring Settingsmust "
                    + "be a string of numRotors()-1 character");
        }
        for (int i = ringSetting.length() - 1; i >= 0; i--) {
            if (!_alphabet.contains(ringSetting.charAt(i))) {
                throw new EnigmaException("Ringsetting "
                        + "must be a string in my alphabet");
            }

            int start = _alphabet.toInt(ringSetting.charAt(i));
            String newAl = "";
            for (int c = start; c < _alphabet.size(); c++) {
                newAl += _alphabet.toChar(c);
            }
            for (int c = 0; c < start; c++) {
                newAl += _alphabet.toChar(c);
            }

            Alphabet rs = new Alphabet(newAl);

            Permutation permWithRS = new Permutation(_myRotors.get(i + 1)
                    .permutation().getCycleString().trim(), rs);
            _myRotors.get(i + 1).setPermutation(permWithRS);
        }
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] shouldAd = new boolean[_myRotors.size()];
        for (int i = _myRotors.size() - 2; i >= 1; i--) {
            if (_myRotors.get(i + 1).atNotch() && _myRotors.get(i).rotates()) {
                shouldAd[i + 1] = true;
                shouldAd[i] = true;
            }
        }
        if (!shouldAd[_myRotors.size() - 1]) {
            shouldAd[_myRotors.size() - 1] = true;
        }
        for (int i = 0; i < shouldAd.length; i++) {
            if (shouldAd[i]) {
                _myRotors.get(i).advance();
            }
        }

        int converted = _plugBoard.permute(c);
        for (int i = _myRotors.size() - 1; i >= 1; i--) {
            if (_ringSetted) {
                converted = _myRotors.get(i).convertForwardwRS(converted);
                Character letter = _myRotors.get(i)
                        .permutation().alphabet().toChar(converted);
                converted = _myRotors.get(i - 1)
                        .permutation().alphabet().toInt(letter);
            } else {
                converted = _myRotors.get(i).convertForward(converted);
            }
        }

        converted = _myRotors.get(0).convertForward(converted);
        if (_ringSetted) {
            Character letter = _myRotors.get(0)
                    .permutation().alphabet().toChar(converted);
            converted = _myRotors.get(1).permutation().alphabet().toInt(letter);
        }
        for (int i = 1; i <= _myRotors.size() - 1; i++) {
            if (_ringSetted) {
                converted = _myRotors.get(i).convertBackwardwRS(converted);
                if (i != _myRotors.size() - 1) {
                    Character letter = _myRotors.get(i)
                            .permutation().alphabet().toChar(converted);
                    converted = _myRotors.get(i + 1)
                            .permutation().alphabet().toInt(letter);
                }
            } else {
                converted = _myRotors.get(i).convertBackward(converted);
            }
        }
        int temp = _plugBoard.permute(converted);
        Character print = _myRotors.get(_myRotors.size() - 1)
                .permutation().alphabet().toChar(temp);
        return temp;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String converted = "";
        msg = msg.replace(" ", "");
        if (_ringSetted) {
            for (int i = 0; i < msg.length(); i++) {
                Alphabet end = _myRotors.get
                        (_myRotors.size() - 1).permutation().alphabet();
                converted += end.toChar(convert(end.toInt((msg.charAt(i)))));
            }
        } else {
            for (int i = 0; i < msg.length(); i++) {
                converted += _alphabet.toChar
                        (convert(_alphabet.toInt((msg.charAt(i)))));
            }
        }
        return converted;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Returns the _alphabet.
     * @return returns _alphabet */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Getter for allRotors.
     * @return returns _allRotors */
    ArrayList allRotors() {
        return _allRotors;
    }

    /** Getter for myRotors.
     * @return returns _myRotors */
    ArrayList myRotors() {
        return _myRotors;
    }

    /** Number of Rotors. */
    private final int _numRotors;

    /** Number of Pawls. */
    private final int _numPawls;

    /** Available Rotors. */
    private ArrayList<Rotor> _allRotors;

    /** The Plugboard. */
    private Permutation _plugBoard;

    /** The machine's Rotors. */
    private ArrayList<Rotor> _myRotors = new ArrayList<>();

    /** If the Machine has ring settings or not. */
    private boolean _ringSetted = false;

    /** Set the Machine to have ring settings. */
    void ringSetted() {
        _ringSetted = true;
    }
}
