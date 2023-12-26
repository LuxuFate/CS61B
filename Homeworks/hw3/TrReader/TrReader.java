import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Matthew Lu
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

   /**The Reader. */
    private Reader rdr;
    /**The From. */
    private String _from;
    /**The To. */
    private String _to;

    /** TrReader's Constructor.
     * @param str The Reader
     * @param from String from
     * @param to String to*/
    public TrReader(Reader str, String from, String to) {
        this.rdr = str;
        this._from = from;
        this._to = to;
    }

    @Override
    public void close() throws IOException {
        this.rdr.close();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = this.rdr.read(cbuf, off, len);
        for (int i = off; i < off + len; i++) {
            if (this._from.indexOf(cbuf[i]) != -1) {
                cbuf[i] = this._to.charAt(this._from.indexOf(cbuf[i]));
            }
        }
        return Math.min(read, len);
    }

    /*
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
}
