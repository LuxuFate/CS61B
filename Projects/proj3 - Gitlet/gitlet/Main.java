package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Matthew Lu
 */
public class Main {

    /** Current Working Directory. */
    static final File REPO = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** Remote Login Info (Remote name, Remote path). */
    static final File REMOTES = new File(".remotes");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        checkCommand(args);
        switch (args[0]) {
        case "init":
            validateNumArgs(args, 1);
            Commands.init();
            break;
        case "add":
            validateNumArgs(args, 2);
            Commands.add(args[1]);
            break;
        case "commit":
            validateNumArgs(args, 2);
            Commands.commit(args[1]);
            break;
        case "rm":
            validateNumArgs(args, 2);
            Commands.remove(args[1]);
            break;
        case "log":
            validateNumArgs(args, 1);
            Commands.log();
            break;
        case "global-log":
            validateNumArgs(args, 1);
            Commands.globalLog();
            break;
        case "checkout":
            checkoutOptions(args);
            break;
        case "find":
            validateNumArgs(args, 2);
            Commands.find(args[1]);
            break;
        case "status":
            validateNumArgs(args, 1);
            Commands.status();
            break;
        case "branch":
            validateNumArgs(args, 2);
            Commands.branch(args[1]);
            break;
        case "rm-branch":
            validateNumArgs(args, 2);
            Commands.rmBranch(args[1]);
            break;
        case "reset":
            validateNumArgs(args, 2);
            Commands.reset(args[1]);
            break;
        case "merge":
            validateNumArgs(args, 2);
            Commands.merge(args[1], false);
            break;
        default:
            mainPartTwo(args);
        }
    }
    /** Extension of Main.
     * @param args User input */
    public static void mainPartTwo(String... args) {
        switch (args[0]) {
        case "add-remote":
            validateNumArgs(args, 3);
            Commands.addRemote(args[1], args[2]);
            break;
        case "rm-remote":
            validateNumArgs(args, 2);
            Commands.removeRemote(args[1]);
            break;
        case "push":
            validateNumArgs(args, 3);
            Commands.push(args[1], args[2]);
            break;
        case "fetch":
            validateNumArgs(args, 3);
            Commands.fetch(args[1], args[2]);
            break;
        case "pull":
            validateNumArgs(args, 3);
            Commands.pull(args[1], args[2]);
            break;
        default:
            exitwithMessage("No command with that name exists.");
        }
    }

    /** System exits after printing out error message.
     * @param message Message to print */
    public static void exitwithMessage(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /** Makes sure the number of arguments is correct.
     * @param args User input
     * @param n Correct amount */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitwithMessage("Incorrect operands.");
        }
    }

    /** Helper method for checkout.
     * @param args User input */
    public static void checkoutOptions(String... args) {
        if (args.length > 4 || args.length < 2) {
            exitwithMessage("Incorrect operands.");
        }
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                exitwithMessage("Incorrect operands.");
            }
            Commands.checkout1(args[2]);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                exitwithMessage("Incorrect operands.");
            }
            Commands.checkout2(args[1], args[3]);
        } else {
            if (args[1].contains("/")) {
                Commands.checkoutRemote(args[1]);
            } else {
                Commands.checkout3(args[1]);
            }
        }
    }

    /** Helper method for Main.
     * @param args User input */
    public static void checkCommand(String... args) {
        if (args.length == 0) {
            exitwithMessage("Please enter a command.");
        }
        if (Commands.ALLCOMMANDS.contains(args[0])
                && !args[0].equals("init") && !GITLET_FOLDER.exists()) {
            Main.exitwithMessage("Not in an initialized Gitlet directory.");
        }
    }
}
