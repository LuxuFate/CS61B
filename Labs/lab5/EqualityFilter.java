/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this.colName = colName;
        this.match = match;
        this.table = input;

    }

    @Override
    protected boolean keep() {
        return _next.getValue(table.colNameToIndex(colName)).equals(match);
    }

    private String colName;
    private String match;
    private Table table;

}
