/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Matthew Lu
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int number = k/8;
            int nybble = k - (number * 8);
            int big = _data[number];
            big = big >>> nybble*4;
            big = big & 0b1111;
            if(big >= 8)
                return  big - 16;
            else
                return big;
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            int number = k/8;
            int nybble = k - (number * 8);
            _data[number] = _data[number] & ~(0b1111 << nybble*4);
            if (val < 0) {
                val += 16;
            }
            val = val & 0b1111;
            val = val << (nybble * 4);
            _data[number] = _data[number] | val;
        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
