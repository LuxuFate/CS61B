/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this.colName1 = colName1;
        this.colName2 = colName2;
        this.table = input;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(table.colNameToIndex(colName1)).equals(_next.getValue(table.colNameToIndex(colName2)));
    }

    private String colName1;
    private String colName2;
    private Table table;
}
