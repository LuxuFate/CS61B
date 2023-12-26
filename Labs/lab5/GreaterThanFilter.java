/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        this.colName = colName;
        this.ref = ref;
        this.table = input;
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(table.colNameToIndex(colName)).compareTo(ref) > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String colName;
    private String ref;
    private Table table;
}
