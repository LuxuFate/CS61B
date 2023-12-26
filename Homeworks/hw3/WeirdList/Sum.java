
/** Sum Class.
 * @author Matthew Lu*/
public class Sum implements IntUnaryFunction {
    /** The Total.*/
    private int total = 0;

    /** Sum Constructor.
     * @param start The Initial value.*/
    public Sum(int start) {
        this.total = start;
    }

    @Override
    public int apply(int x) {
        this.total += x;
        return x;
    }

    /** Returns Total. */
    public int getsum() {
        return this.total;
    }

}
