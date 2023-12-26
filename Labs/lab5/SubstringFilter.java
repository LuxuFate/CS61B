/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        this.colName = colName;
        this.subStr = subStr;
        this.table = input;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(table.colNameToIndex(colName)).contains(subStr);
    }

    private String colName;
    private String subStr;
    private Table table;
}
