# Gitlet Design Document

**Name**: Matthew Lu

## Classes and Data Structures

###Commits
This class serves as the basic snapshots of your code at one specific time. 
Each one of them individually can form a tree together where there can be multiple branches of the same tree that can get merged together or proceed independently.
This class will always probably implement Serializable.
Once a commit node has been created and instantiated, it can no longer be deleted. Commit Trees are immutable. 
#####Fields
1. Timestamp: Keeps track of the time stamp of when the commit happened
2. Log Message: Keeps track of the log message, which is the comment that can be helpful for when you need to recognize which commit happened in which order.
3. Cryptographic Hash Function: Acts as a reference for the Commit Object. This produces a 160-bit integer hash from any sequence of bytes, meaning that the probability of having a hash collision is extremely low, being 2 to the power of  -120. Although there is a chance that it could become a potential problem, the chances are very low so we will ignore the possibility.
4. Reference to parent: Linked to the rest of the commit tree. This reference will probably be in the form of a cryptographic hash function. 
5. Blob: These will act as the contents of the commit and when the contents change, the commit object will be pointing to a new blob as their content instead of the old one. 

###Commands
This class processes the commands called by main and contains different methods for different commands.


###Main
This class would be the the main hub for everything, starting the gitlet and getting user input.
This is where the user would input the commands into. 
#####Fields
1. CWD: The Current Working Directory
2. Metadata Folder(.gitlet): Keeps all the files within this directory

###Blobs
This class contains the contents of a certain file.
#####Fields
1. Cryptographic Hash Function: Same as the commit, it will be an individual identification reference. 

## Algorithms
###Main
1. Sets up Persistence: This consists of making a new directory
2. Initializes the files that would stores the commits made by the user. 
3. Processes Commands: This would mean to check the arguments passed within main and determine which Command it should call.
This also involves making sure that the arguments are not illegal. 

###Commands
1. Make sure runtime and memory requirements are in line
2. Account for failure cases and error messages within each command
3. Commands will include:
init, add, commit, rm, log, global-log, find, status, checkout, branch, rm-branch, reset and merge. 

###Commit
1. Update the content of the commit, which instantiates a new blob.
2. This would need to be able to be serialized. 

###Blobs
1. This would need to be able to be serialized. 

## Persistence
To ensure that we save the state of the program across multiple runs, we will save the data in the metadata folder.
This would mean that the each time the user commits, it is serialized and saved into the file. This is done within the .gitlet of program. 
After each command that would change the commit tree, the files need to be updated.
