import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Matthew Lu
 */
class ECHashStringSet implements StringSet {

    private int _length;
    private ArrayList<LinkedList> _buckets;
    private int _elements;
    private double _max;
    private double _min;

    public ECHashStringSet() {
        _elements = 0;
        _max = 5;
        _min = 0.2;
        _buckets = new ArrayList<LinkedList>((int)(1/_min));
        for (int i = 0; i < (int)(1/_min); i++) {
            _buckets.add(null);
        }
        _length = (int)(1/_min);
    }

    @Override
    public void put(String s) {
        if (((double) _elements / (double) _length) > _max) {
            resize();
        }
        int index = (s.hashCode() & 0x7fffffff) % _length;
        if (_buckets.get(index) == null) {
            _buckets.set(index, new LinkedList<String>());
        }
        _buckets.get(index).add(s);
        _elements++;
    }

    @Override
    public boolean contains(String s) {
        int index = (s.hashCode() & 0x7fffffff) % _length;
        if (_buckets.get(index) == null) {
            return false;
        } else {
            for (LinkedList l: _buckets) {
                if (l != null && l.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList list = new ArrayList();
        if (_elements != 0) {
            for (LinkedList l: _buckets) {
                for (int i = 0; i < l.size(); i++) {
                    list.add(l.get(i));
                }
            }
        }
        return list;
    }

    public void resize() {
        ArrayList<LinkedList> old = _buckets;
        _buckets = new ArrayList<LinkedList>(old.size() * 2);
        _length = _length * 2;
        _elements = 0;
        for (int i = 0; i < _length; i++) {
            _buckets.add(null);
        }
        for (LinkedList l: old) {
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {
                    this.put((String) l.get(j));
                }
            }
        }
    }

    /** Getter for length. **/
    public int length() {
        return _length;
    }

    /** Getter for elements. **/
    public int elements() {
        return _elements;
    }
}
