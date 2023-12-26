package gitlet;

import java.io.Serializable;
import java.io.File;

/** Blob Class.
 * @author Matthew Lu */
public class Blob implements Serializable {

    /** Contents.*/
    private byte[] contents;
    /** Hash.*/
    private String hash;

    /** Blob Constructor.
     * @param file File being blobbed*/
    public Blob(File file) {
        this.contents = Utils.readContents(file);
        this.hash = Utils.sha1(Utils.serialize(this));
    }

    /** Getter for Hash.
     * @return the Hash*/
    public String getHash() {
        return hash;
    }

    /** Getter for Content.
     * @return the Content*/
    public byte[] getContents() {
        return contents;
    }
}
