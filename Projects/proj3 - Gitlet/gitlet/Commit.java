package gitlet;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;

/** Commit Class.
 * @author Matthew Lu */
public class Commit implements Serializable {

    /** Message.*/
    private String message;
    /** Timestamp.*/
    private Date timestamp;
    /** Parent.*/
    private String parent;
    /** SecondParent.*/
    private String secondParent;
    /** Hash.*/
    private String hash;
    /** Mapping.*/
    private HashMap<String, String> mapping;

    /** Commit Constructor.
     * @param cmessage The message
     * @param cparent The parent
     * @param  ctimestamp The timestamp
     * @param cmapping The mapping */
    public Commit(String cmessage, String cparent,
                  Date ctimestamp, HashMap<String, String> cmapping) {
        this.message = cmessage;
        this.parent = cparent;
        this.timestamp = ctimestamp;
        this.mapping = cmapping;
    }

    /** SecondParent Setter.
     * @param csecondParent Sets it to this */
    public void setSecondParent(String csecondParent) {
        this.secondParent = csecondParent;
    }

    /** Message Getter.
     * @return Message */
    public String getMessage() {
        return message;
    }

    /** Timestamp Getter.
     * @return Timestamp */
    public Date getTimestamp() {
        return timestamp;
    }

    /** Parent Getter.
     * @return Parent */
    public String getParent() {
        return parent;
    }

    /** SecondParent Getter.
     * @return SecondParent */
    public String getSecondParent() {
        return secondParent;
    }

    /** Hash Getter.
     * @return Hash */
    public String getHash() {
        return hash;
    }

    /** Hash Setter.
     * @param chash the Has to set to */
    public void setHash(String chash) {
        this.hash = chash;
    }

    /** Mapping Getter.
     * @return Mapping */
    public HashMap<String, String> getMapping() {
        return mapping;
    }

}
