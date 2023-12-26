package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/** Command Class.
 * @author Matthew Lu */
public class Commands {

    /** All Commands.*/
    public static final List<String> ALLCOMMANDS = Arrays.asList("init", "add",
            "commit", "rm", "log", "global-log",
            "find", "status", "checkout", "branch",
            "rm-branch", "reset", "merge", "add-remote",
            "rm-remote", "push", "fetch", "pull");

    /** Magic Number.*/
    public static final int MAGICNUMBER = 40;

    /** Init Command.*/
    public static void init() {
        if (Main.GITLET_FOLDER.exists()) {
            String str = "Gitlet version-control system "
                    + "already exists in the current directory.";
            Main.exitwithMessage(str);
        } else {
            Main.REPO.mkdir();
        }
        Main.GITLET_FOLDER.mkdir();

        Commit initial = new Commit("initial commit",
                null, new Date(0), new HashMap<String, String>());
        initial.setHash(Utils.sha1(Utils.serialize(initial)));
        File commits = new File(".gitlet/commits");
        commits.mkdir();
        File first = new File(".gitlet/commits/" + initial.getHash());
        Utils.writeObject(first, initial);

        File branches = new File(".gitlet/branches");
        branches.mkdir();
        File master = new File(".gitlet/branches/master");
        Utils.writeContents(master, initial.getHash());

        File head = new File(".gitlet/HEAD");
        Utils.writeContents(head, "branches/master");

        File stage = new File(".gitlet/stage");
        stage.mkdir();
        File stageForRemoval = new File(".gitlet/stage/stageForRemoval");
        File stageForAddition = new File(".gitlet/stage/stageForAddition");
        stageForAddition.mkdir();
        stageForRemoval.mkdir();

        File blobs = new File(".gitlet/blobs");
        blobs.mkdir();
    }

    /** Add Command.
     * @param filename Name of File */
    public static void add(String filename) {
        File add = Utils.join(Main.REPO, filename);
        if (!add.exists()) {
            Main.exitwithMessage("File does not exist.");
        }
        File inStage = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition/" + filename);
        Commit current = getCurrentCommit();
        if (current.getMapping().containsKey(filename)) {
            Blob check = new Blob(add);
            if (current.getMapping().get(filename).equals(check.getHash())) {
                File inStageRem = Utils.join(Main.GITLET_FOLDER,
                        "stage/stageForRemoval/" + filename);
                if (inStage.exists()) {
                    inStage.delete();
                }
                if (inStageRem.exists()) {
                    inStageRem.delete();
                }
                return;
            }
        }
        Utils.writeObject(inStage, add);
    }

    /** Commit Command.
     * @param message Message of Commit */
    public static void commit(String message) {
        if (message.equals("")) {
            Main.exitwithMessage("Please enter a commit message.");
        }
        File stage = Utils.join(Main.GITLET_FOLDER, "stage/stageForAddition");
        List<String> stagelist = Utils.plainFilenamesIn(stage);
        File removal = Utils.join(Main.GITLET_FOLDER, "stage/stageForRemoval");
        List<String> removelist = Utils.plainFilenamesIn(removal);

        if (stagelist.isEmpty() && removelist.isEmpty()) {
            Main.exitwithMessage("No changes added to the commit.");
        }
        Commit current = getCurrentCommit();
        Commit newCommit = new Commit(message, current.getHash(),
                new Date(), new HashMap<String, String>());
        HashSet<String> allNames = new HashSet<String>(stagelist);
        allNames.addAll(current.getMapping().keySet());
        for (String str: allNames) {
            if (stagelist.contains(str)) {
                File fromStage = Utils.join(stage, str);
                Blob newBlob =
                        new Blob(Utils.readObject(fromStage, File.class));
                File blobFolder = Utils.join(Main.GITLET_FOLDER,
                        "blobs/" + newBlob.getHash());
                Utils.writeObject(blobFolder, newBlob);
                newCommit.getMapping().put(str, newBlob.getHash());
                Utils.join(stage, str).delete();
            } else if (removelist.contains(str)) {
                File removeThis = Utils.join(removal, str);
                removeThis.delete();
            } else {
                newCommit.getMapping().put(str, current.getMapping().get(str));
            }
        }
        newCommit.setHash(Utils.sha1(Utils.serialize(newCommit)));
        File where = new File(".gitlet/commits/" + newCommit.getHash());
        Utils.writeObject(where, newCommit);
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File currentBranch = Utils.join(Main.GITLET_FOLDER, path);
        Utils.writeContents(currentBranch, newCommit.getHash());
    }

    /** Remove Command.
     * @param filename Name of File */
    public static void remove(String filename) {
        File fileInStage = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition/" + filename);
        Commit currentCommit = getCurrentCommit();
        if (!currentCommit.getMapping().containsKey(filename)
                && !fileInStage.exists()) {
            Main.exitwithMessage("No reason to remove the file.");
        }
        if (fileInStage.exists()) {
            fileInStage.delete();
        }
        if (currentCommit.getMapping().containsKey(filename)) {
            File removal = Utils.join(Main.GITLET_FOLDER,
                    "stage/stageForRemoval/" + filename);
            Utils.writeContents(removal, filename);
            File file = Utils.join(Main.REPO, filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /** Log Command.*/
    public static void log() {
        Commit current = getCurrentCommit();
        while (current.getParent() != null) {
            logMessage(current);
            File findNext = Utils.join(Main.GITLET_FOLDER,
                    "commits/" + current.getParent());
            current = Utils.readObject(findNext, Commit.class);
        }
        logMessage(current);
    }

    /** Global-Log Command.*/
    public static void globalLog() {
        File commits = Utils.join(Main.GITLET_FOLDER, "commits");
        File[] list = commits.listFiles();
        for (File commit: list) {
            Commit current = Utils.readObject(commit, Commit.class);
            logMessage(current);
        }
    }

    /** Prints out Log Message.
     * @param current Current commit */
    public static void logMessage(Commit current) {
        System.out.println("===");
        System.out.println("commit " + current.getHash());
        DateFormat changeformat =
                new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        String timestamp = changeformat.format(current.getTimestamp());
        System.out.println("Date: " + timestamp);
        System.out.println(current.getMessage() + "\n");
    }

    /** Gets Current HEAD commit.
     * @return Returns current commit*/
    public static Commit getCurrentCommit() {
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File current = Utils.join(Main.GITLET_FOLDER, path);
        String hash = Utils.readContentsAsString(current);
        File getFromFolder = Utils.join(Main.GITLET_FOLDER, "commits/" + hash);
        return Utils.readObject(getFromFolder, Commit.class);
    }

    /** Checkout Helper.
     * @param check Commit to checkout
     * @param filename Name of File */
    public static void checkoutFile(Commit check, String filename) {
        if (!check.getMapping().containsKey(filename)) {
            Main.exitwithMessage("File does not exist in that commit.");
        }
        String blobID = check.getMapping().get(filename);
        File blobFile = Utils.join(Main.GITLET_FOLDER, "blobs/" + blobID);
        File putInCWD = Utils.join(Main.REPO, filename);
        Blob converted = Utils.readObject(blobFile, Blob.class);
        String str = new String(converted.getContents());
        Utils.writeContents(putInCWD, str);
    }

    /** Checkout Command 1.
     * @param filename Name of File */
    public static void checkout1(String filename) {
        Commit head = getCurrentCommit();
        checkoutFile(head, filename);
    }

    /** Checkout Command 2.
     * @param commitID ID of commit
     * @param filename Name of File */
    public static void checkout2(String commitID, String filename) {
        if (commitID.length() < MAGICNUMBER) {
            File commits = Utils.join(Main.GITLET_FOLDER, "commits");
            List<String> allCommitIDs = Utils.plainFilenamesIn(commits);
            for (String id: allCommitIDs) {
                if (id.substring(0, commitID.length()).equals(commitID)) {
                    commitID = id;
                    break;
                }
            }
        }
        File commit = Utils.join(Main.GITLET_FOLDER, "commits/" + commitID);
        if (!commit.exists()) {
            Main.exitwithMessage("No commit with that id exists.");
        }
        Commit check = Utils.readObject(commit, Commit.class);
        checkoutFile(check, filename);
    }

    /** Checkout Command 3.
     * @param branchname Name of branch */
    public static void checkout3(String branchname) {
        File branch = Utils.join(Main.GITLET_FOLDER, "branches/" + branchname);
        if (!branch.exists()) {
            Main.exitwithMessage("No such branch exists.");
        }
        String hashOfBranch = Utils.readContentsAsString(branch);
        File headOfBranch = Utils.join(Main.GITLET_FOLDER,
                "commits/" + hashOfBranch);
        Commit currentCommit = getCurrentCommit();
        Commit checkedOut = Utils.readObject(headOfBranch, Commit.class);
        Set<String> currentSet = currentCommit.getMapping().keySet();
        Set<String> checkedOutSet = checkedOut.getMapping().keySet();

        for (String filename: checkedOutSet) {
            File check = Utils.join(Main.REPO, filename);
            if (!currentSet.contains(filename) && check.exists()) {
                String str = "There is an untracked file in the way; "
                        + "delete it, or add and commit it first.";
                Main.exitwithMessage(str);
            }
        }
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File currentBranch = Utils.join(Main.GITLET_FOLDER, path);
        if (branch.equals(currentBranch)) {
            Main.exitwithMessage("No need to checkout the current branch.");
        }
        for (String filename: checkedOutSet) {
            checkoutFile(checkedOut, filename);
        }
        for (String name: currentSet) {
            if (!checkedOutSet.contains(name)) {
                File remove = Utils.join(Main.REPO, name);
                remove.delete();
            }
        }
        clearStage();
        File headPlace = Utils.join(Main.GITLET_FOLDER, "HEAD");
        Utils.writeContents(headPlace, "branches/" + branchname);
    }

    /** Clears the Staging Area.*/
    public static void clearStage() {
        File stageAdd = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition");
        File stageRem = Utils.join(Main.GITLET_FOLDER, "stage/stageForRemoval");
        File[] addList = stageAdd.listFiles();
        File[] remList = stageRem.listFiles();
        for (File add: addList) {
            add.delete();
        }
        for (File rem: remList) {
            rem.delete();
        }
    }

    /** Find Command.
     * @param message Message to find */
    public static void find(String message) {
        File commits = Utils.join(Main.GITLET_FOLDER, "commits");
        File[] commitArray = commits.listFiles();
        boolean found = false;
        for (File commitFile: commitArray) {
            Commit commit = Utils.readObject(commitFile, Commit.class);
            if (commit.getMessage().equals(message)) {
                System.out.println(commit.getHash());
                found = true;
            }
        }
        if (!found) {
            Main.exitwithMessage("Found no commit with that message.");
        }
    }

    /** Branch Status.*/
    public static void branchStatus() {
        File branches = Utils.join(Main.GITLET_FOLDER, "branches");
        List<String> branchList = Utils.plainFilenamesIn(branches);
        for (int i = 0; i < branchList.size(); i++) {
            File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
            String path = Utils.readContentsAsString(head);
            File currentHead = Utils.join(Main.GITLET_FOLDER, path);
            String branchname = branchList.get(i);
            File branch = Utils.join(branches, branchname);
            if (currentHead.equals(branch)) {
                branchList.set(i, "*" + branchname);
            }
        }
        statusDisplay("Branches", branchList);
    }

    /** Status Command.*/
    public static void status() {
        branchStatus();
        File additions = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition");
        List<String> additionList = Utils.plainFilenamesIn(additions);
        statusDisplay("Staged Files", additionList);
        File removals = Utils.join(Main.GITLET_FOLDER, "stage/stageForRemoval");
        List<String> removalsList = Utils.plainFilenamesIn(removals);
        statusDisplay("Removed Files", removalsList);
        List<String> modList = new ArrayList<String>();
        Commit currentCommit = getCurrentCommit();
        Set<String> tracked = currentCommit.getMapping().keySet();
        File stageAdd = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition");
        List<String> addList = Utils.plainFilenamesIn(stageAdd);
        File stageRem = Utils.join(Main.GITLET_FOLDER, "stage/stageForRemoval");
        List<String> remList = Utils.plainFilenamesIn(stageRem);
        for (String filename: tracked) {
            File inCWD = Utils.join(Main.REPO, filename);
            if (!inCWD.exists() && !remList.contains(filename)) {
                modList.add(filename + " (deleted)");
            } else if (inCWD.exists()) {
                String blobID = currentCommit.getMapping().get(filename);
                File blob = Utils.join(Main.GITLET_FOLDER, "blobs/" + blobID);
                Blob byteArray = Utils.readObject(blob, Blob.class);
                String fileToString = new String(byteArray.getContents());
                if (!fileToString.equals(Utils.readContentsAsString(inCWD))
                        && !addList.contains(filename)
                        && !remList.contains(filename)) {
                    modList.add(filename + " (modified)");
                }
            }
        }
        for (String filename: addList) {
            File inCWD = Utils.join(Main.REPO, filename);
            if (!inCWD.exists()) {
                modList.add(filename + " (deleted)");
            } else {
                File stageFile = Utils.join(stageAdd, filename);
                File original = Utils.readObject(stageFile, File.class);
                String content = Utils.readContentsAsString(original);
                if (!content.equals(Utils.readContentsAsString(inCWD))) {
                    modList.add(filename + " (modified)");
                }
            }
        }
        statusDisplay("Modifications Not Staged For Commit", modList);
        List<String> untracked = new ArrayList<String>();
        File[] repo = Main.REPO.listFiles();
        for (File f: repo) {
            if (!f.isDirectory() && !addList.contains(f.getName())
                    && !tracked.contains(f.getName())) {
                untracked.add(f.getName());
            }
            if (remList.contains(f.getName())) {
                untracked.add(f.getName());
            }
        }
        statusDisplay("Untracked Files", untracked);
    }

    /** Displays Section of Status.
     * @param title Title of Folder
     * @param contents Contents of Folder*/
    public static void statusDisplay(String title, List<String> contents) {
        System.out.println("=== " + title + " ===");
        for (Object str: contents) {
            System.out.println(str);
        }
        System.out.println();
    }

    /** Branch Command.
     * @param name Name of Branch */
    public static void branch(String name) {
        Commit currentCommit = getCurrentCommit();
        File check = Utils.join(Main.GITLET_FOLDER, "branches/" + name);
        if (check.exists()) {
            Main.exitwithMessage("A branch with that name already exists.");
        }
        File newBranch = new File(".gitlet/branches/" + name);
        Utils.writeContents(newBranch, currentCommit.getHash());
    }

    /** Remote Branch Command.
     * @param name Name of Branch*/
    public static void rmBranch(String name) {
        File branch = Utils.join(Main.GITLET_FOLDER, "branches/" + name);
        if (!branch.exists()) {
            Main.exitwithMessage("A branch with that name does not exist.");
        }
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File currentHead = Utils.join(Main.GITLET_FOLDER, path);
        if (currentHead.equals(branch)) {
            Main.exitwithMessage("Cannot remove the current branch.");
        }
        branch.delete();
    }

    /** Reset Command.
     * @param commitID The commit*/
    public static void reset(String commitID) {
        File commit = Utils.join(Main.GITLET_FOLDER, "commits/" + commitID);
        if (!commit.exists()) {
            Main.exitwithMessage("No commit with that id exists.");
        }
        Commit reset = Utils.readObject(commit, Commit.class);
        Commit currentCommit  = getCurrentCommit();
        Set<String> currentSet = currentCommit.getMapping().keySet();
        Set<String> resetSet = reset.getMapping().keySet();
        for (String filename: resetSet) {
            File check = Utils.join(Main.REPO, filename);
            if (!currentSet.contains(filename) && check.exists()) {
                String str = "There is an untracked file in the "
                        + "way; delete it, or add and commit it first.";
                Main.exitwithMessage(str);
            }
        }
        for (String filename: resetSet) {
            checkoutFile(reset, filename);
        }
        for (String name: currentSet) {
            if (!resetSet.contains(name)) {
                File remove = Utils.join(Main.REPO, name);
                remove.delete();
            }
        }
        clearStage();
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File currentBranch = Utils.join(Main.GITLET_FOLDER, path);
        Utils.writeContents(currentBranch, reset.getHash());
    }

    /** Merge Command.
     * @param branchname Name of Branch
     * @param pull Pull Option*/
    public static void merge(String branchname, boolean pull) {
        Commit currentCommit  = getCurrentCommit();
        Set<String> currentSet = currentCommit.getMapping().keySet();
        File branch = Utils.join(Main.GITLET_FOLDER, "branches/" + branchname);
        if (!branch.exists()) {
            Main.exitwithMessage("A branch with that name does not exist.");
        }
        File stageAdd = Utils.join(Main.GITLET_FOLDER,
                "stage/stageForAddition");
        File stageRem = Utils.join(Main.GITLET_FOLDER, "stage/stageForRemoval");
        List<String> addList = Utils.plainFilenamesIn(stageAdd);
        String commitID = Utils.readContentsAsString(branch);
        Commit otherCommit = getCommit(commitID);
        Set<String> otherFiles = otherCommit.getMapping().keySet();
        for (String filename: otherFiles) {
            File check = Utils.join(Main.REPO, filename);
            if (!currentSet.contains(filename) && check.exists()
                    && !addList.contains(filename)) {
                String str = "There is an untracked file in the way; "
                        + "delete it, or add and commit it first.";
                Main.exitwithMessage(str);
            }
        }
        File[] addArray = stageAdd.listFiles();
        File[] remArray = stageRem.listFiles();
        if (!(addArray.length == 0) || !(remArray.length == 0)) {
            Main.exitwithMessage("You have uncommitted changes.");
        }
        File head = Utils.join(Main.GITLET_FOLDER, "HEAD");
        String path = Utils.readContentsAsString(head);
        File currentHead = Utils.join(Main.GITLET_FOLDER, path);
        if (currentHead.equals(branch)) {
            Main.exitwithMessage("Cannot merge a branch with itself.");
        }
        String hash = Utils.readContentsAsString(branch);
        Commit branchCommit = getCommit(hash);
        List<String> currentAncestor = getAncestry(currentCommit);
        List<String> branchAncestor = getAncestry(branchCommit);
        currentAncestor.retainAll(branchAncestor);
        String split = currentAncestor.get(0);
        if (split.equals(branchCommit.getHash())) {
            String str = "Given branch is an ancestor of the current branch.";
            Main.exitwithMessage(str);
        } else if (split.equals(currentCommit.getHash())) {
            checkout3(branchname);
            Main.exitwithMessage("Current branch fast-forwarded.");
        }
        boolean conflicted;
        conflicted = mergeHelper(currentSet, branchCommit,
                currentCommit, getCommit(split));
        if (pull && branchname.contains("%")) {
            branchname = branchname.replace("%", "/");
        }
        commitSecondParent("Merged " + branchname
                + " into " + currentHead.getName()
                + ".", branchCommit.getHash());
        if (conflicted) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Helper for merge.
     * @param currentSet CurrentSet
     * @param branchCommit Branch Commit
     * @param currentCommit Current Commit
     * @param splitPoint Split ID
     * @return if conflicted or not*/
    public static boolean mergeHelper(Set<String> currentSet,
                                      Commit branchCommit, Commit currentCommit,
                                      Commit splitPoint) {
        HashMap<String, String> splitMap = splitPoint.getMapping();
        HashMap<String, String> branchMap = branchCommit.getMapping();
        HashMap<String, String> currentMap = currentCommit.getMapping();
        Set<String> splitSet = splitMap.keySet(); boolean conflicted = false;
        Set<String> branchSet = branchMap.keySet();
        for (String filename: branchSet) {
            if (splitSet.contains(filename)) {
                if (splitMap.get(filename).equals(currentMap.get(filename))
                        && branchSet.contains(filename)
                        && !splitMap.get(filename).equals(
                        branchMap.get(filename))) {
                    checkout2(branchCommit.getHash(), filename);
                    add(filename);
                }
            } else if (!currentSet.contains(filename)) {
                checkout2(branchCommit.getHash(), filename);
                add(filename);
            } else if (!branchMap.get(filename).equals(splitMap.get(filename))
                    && !branchMap.get(filename).equals(
                    currentMap.get(filename))) {
                conflicted = true;
                makeConflictFile(currentCommit, branchCommit, filename);
            }
        }
        for (String filename: currentSet) {
            if (splitSet.contains(filename)) {
                if (currentMap.get(filename).equals(splitMap.get(filename))
                        && !branchSet.contains(filename)) {
                    remove(filename);
                } else if (!splitMap.get(filename).equals(
                        branchMap.get(filename))
                        && !currentMap.get(filename).equals(
                        splitMap.get(filename))
                        && !currentMap.get(filename).equals(
                        branchMap.get(filename))) {
                    conflicted = true;
                    makeConflictFile(currentCommit, branchCommit, filename);
                }
            }
        }
        for (String filename: splitSet) {
            if (!splitMap.get(filename).equals(currentMap.get(filename))
                    && !splitMap.get(filename).equals(
                    branchMap.get(filename))) {
                if (currentMap.get(filename) != null
                        && !currentMap.get(filename).equals(
                        branchMap.get(filename))) {
                    conflicted = true;
                    makeConflictFile(currentCommit, branchCommit, filename);
                } else if (branchMap.get(filename) != null
                        && !branchMap.get(filename).equals(
                        currentMap.get(filename))) {
                    conflicted = true;
                    makeConflictFile(currentCommit, branchCommit, filename);
                }
            }
        }
        return conflicted;
    }

    /** Commits the second Parent.
     * @param message The message
     * @param secondParent The second Parent*/
    public static void commitSecondParent(String message, String secondParent) {
        commit(message);
        Commit current = getCurrentCommit();
        current.setSecondParent(secondParent);
        File where = new File(".gitlet/commits/" + current.getHash());
        Utils.writeObject(where, current);
    }

    /** Creates the conflicted File.
     * @param current Current Commit
     * @param branch Branch Commit
     * @param filename Conflicted File*/
    public static void makeConflictFile(Commit current,
                                        Commit branch, String filename) {
        String currentContent = "";
        String branchContent = "";
        if (current.getMapping().get(filename) != null) {
            String blobID = current.getMapping().get(filename);
            File blobFile = Utils.join(Main.GITLET_FOLDER, "blobs/" + blobID);
            Blob converted = Utils.readObject(blobFile, Blob.class);
            currentContent = new String(converted.getContents());
        }
        if (branch.getMapping().get(filename) != null) {
            String blobID = branch.getMapping().get(filename);
            File blobFile = Utils.join(Main.GITLET_FOLDER, "blobs/" + blobID);
            Blob converted = Utils.readObject(blobFile, Blob.class);
            branchContent = new String(converted.getContents());
        }
        String content = "<<<<<<< HEAD\n"
                + currentContent + "=======\n" + branchContent + ">>>>>>>\n";
        File conflict = new File(filename);
        Utils.writeContents(conflict, content);
        add(filename);
    }

    /** Gets the history of the commit.
     * @param commit The Commit
     * @return History of the commit */
    public static List<String> getAncestry(Commit commit) {
        List<String> lineage = new ArrayList<String>();
        lineage.add(commit.getHash());
        while (commit.getParent() != null) {
            if (commit.getSecondParent() != null) {
                lineage.add(commit.getSecondParent());
            }
            lineage.add(commit.getParent());
            commit = getCommit(commit.getParent());
        }
        return lineage;
    }

    /** Gets the Commit with the given ID.
     * @param commitID ID of the Commit
     * @return Commit of the ID*/
    public static Commit getCommit(String commitID) {
        File commitFile = Utils.join(Main.GITLET_FOLDER, "commits/" + commitID);
        return Utils.readObject(commitFile, Commit.class);
    }

    /** Add Remote Command.
     * @param name The remote name
     * @param path The path to remote */
    public static void addRemote(String name, String path) {
        if (!Main.REMOTES.exists()) {
            Main.REMOTES.mkdir();
        }
        if (Utils.plainFilenamesIn(Main.REMOTES).contains(name)) {
            Main.exitwithMessage("A remote with that name already exists.");
        } else {
            File newRemote = Utils.join(Main.REMOTES, name);
            Utils.writeContents(newRemote, path);
        }
    }

    /** Remove Remote Command.
     * @param name The remote name */
    public static void removeRemote(String name) {
        if (!Utils.plainFilenamesIn(Main.REMOTES).contains(name)) {
            Main.exitwithMessage("A remote with that name does not exist.");
        } else {
            File remote = Utils.join(Main.REMOTES, name);
            remote.delete();
        }
    }

    /** Push Command.
     * @param remoteName The remote Directory
     * @param branchName The branch name */
    public static void push(String remoteName, String branchName) {
        Commit currentHead = getCurrentCommit();
        File remoteFile = Utils.join(Main.REMOTES, remoteName);
        String path = Utils.readContentsAsString(remoteFile);
        File remoteDir = Utils.join(path);
        if (!remoteDir.exists()) {
            Main.exitwithMessage("Remote directory not found.");
        }
        File branchFile = Utils.join(remoteDir, "branches/" + branchName);
        if (!branchFile.exists()) {
            branchRemote(remoteDir, branchName);
        }
        String commitID = Utils.readContentsAsString(branchFile);
        File commitFile = Utils.join(remoteDir, "commits/" + commitID);
        Commit branchHead = Utils.readObject(commitFile, Commit.class);
        List<String> history = pushHistory(currentHead);
        if (!history.contains(branchHead.getHash())) {
            String str = "Please pull down remote changes before pushing.";
            Main.exitwithMessage(str);
        }
        for (String id: history) {
            if (id.equals(branchHead.getHash())) {
                history = history.subList(0, history.indexOf(id));
            }
        }
        int counter = 0;
        String lastHash = "";
        for (String id: history) {
            Commit thisCommit = getCommit(id);
            for (String filename: thisCommit.getMapping().keySet()) {
                File blobFile = Utils.join(Main.GITLET_FOLDER,
                        "blobs/" + thisCommit.getMapping().get(filename));
                Blob blob = Utils.readObject(blobFile, Blob.class);
                File remoteBlob = Utils.join(remoteDir,
                        "blobs/" + blob.getHash());
                if (!remoteBlob.exists()) {
                    Utils.writeObject(remoteBlob, blob);
                }
            }
            File remoteCommitFile = Utils.join(remoteDir, "commits/" + id);
            Utils.writeObject(remoteCommitFile, thisCommit);
            counter++;
            if (counter == history.size()) {
                lastHash = id;
            }
        }
        Utils.writeContents(branchFile, lastHash);
    }

    /** History Command for Push.
     * @param commit The commit
     * @return The history of the commit */
    public static List<String> pushHistory(Commit commit) {
        List<String> lineage = new ArrayList<String>();
        lineage.add(commit.getHash());
        while (commit.getParent() != null) {
            lineage.add(commit.getParent());
            commit = getCommit(commit.getParent());
        }
        return lineage;
    }

    /** Fetch Command.
     * @param remoteName The remote Directory
     * @param branchName The branch name */
    public static void fetch(String remoteName, String branchName) {
        File remoteFile = Utils.join(Main.REMOTES, remoteName);
        String path = Utils.readContentsAsString(remoteFile);
        File remoteDir = Utils.join(path);
        if (!remoteDir.exists()) {
            Main.exitwithMessage("Remote directory not found.");
        }
        File branchFile = Utils.join(remoteDir, "branches/" + branchName);
        if (!branchFile.exists()) {
            Main.exitwithMessage("That remote does not have that branch.");
        }
        File remoteBranch = Utils.join(Main.GITLET_FOLDER,
                "branches/" + remoteName + "%" + branchName);
        if (!remoteBranch.exists()) {
            branch(remoteName + "%" + branchName);
        }
        String remoteBranchHeadID = Utils.readContentsAsString(branchFile);
        File commitFile = Utils.join(remoteDir,
                "commits/" + remoteBranchHeadID);
        Commit remoteBranchHead = Utils.readObject(commitFile, Commit.class);
        List<String> history = pushHistory(remoteBranchHead);

        for (String id: history) {
            File currentFile = Utils.join(remoteDir, "commits/" + id);
            Commit thisCommit = Utils.readObject(currentFile, Commit.class);
            for (String filename: thisCommit.getMapping().keySet()) {
                File blobFile = Utils.join(remoteDir,
                        "blobs/" + thisCommit.getMapping().get(filename));
                Blob blob = Utils.readObject(blobFile, Blob.class);
                File where = Utils.join(Main.GITLET_FOLDER,
                        "blobs/" + blob.getHash());
                if (!where.exists()) {
                    Utils.writeObject(where, blob);
                }
            }
            File commits = Utils.join(Main.GITLET_FOLDER,
                    "commits/" + thisCommit.getHash());
            if (!commits.exists()) {
                Utils.writeObject(commits, thisCommit);
            }
        }
        Utils.writeContents(remoteBranch, remoteBranchHead.getHash());
    }

    /** Pull Command.
     * @param remoteName The remote Directory
     * @param branchName The branch name */
    public static void pull(String remoteName, String branchName) {
        fetch(remoteName, branchName);
        merge(remoteName + "%" + branchName, true);
    }

    /** Checkout for Remote.
     * @param branchname The branch name */
    public static void checkoutRemote(String branchname) {
        String edit = branchname.replace("/", "%");
        checkout3(edit);
    }

    /** Branch for Remote.
     * @param remoteDir The remote Directory
     * @param name The branch name */
    public static void branchRemote(File remoteDir, String name) {
        File headCommit = Utils.join(remoteDir, "HEAD");
        String path = Utils.readContentsAsString(headCommit);
        File current = Utils.join(remoteDir, path);
        String hash = Utils.readContentsAsString(current);
        File getFromFolder = Utils.join(remoteDir, "commits/" + hash);
        Commit currentCommit = Utils.readObject(getFromFolder, Commit.class);
        File check = Utils.join(remoteDir, "branches/" + name);
        if (check.exists()) {
            Main.exitwithMessage("A branch with that name already exists.");
        }
        File newBranch = new File(".gitlet/branches/" + name);
        Utils.writeContents(newBranch, currentCommit.getHash());
    }
}
