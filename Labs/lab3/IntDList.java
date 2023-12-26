/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int counter = 0;
        DNode f = this._front;
        while (f != null){
            counter++;
            f = f._next;
        }
        return counter;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        DNode front = this._front;
        DNode back = this._back;
        if (this.size() == 1){
            return this._front._val;
        }
        else if (i >= 0){
            for (int j = i; j != 0; j--){
                front = front._next;
            }
            return front._val;
        }
        else {
            for (int j = i; j != -1; j++){
                back = back._prev;
            }
            return back._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if (this._front == null & this._back == null){
            this._front = this._back = new DNode(null, d, null);
        }
        else {
            DNode n =  new DNode(null, d, null);
            n._next = this._front;
            this._front._prev = n;
            this._front = n;
        }

    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if (this._front == null & this._back == null){
            this._front = this._back = new DNode(null, d, null);
        }
        else {
            DNode n =  new DNode(null, d, null);
            n._prev = this._back;
            this._back._next = n;
            this._back = n;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        DNode front = this._front;
        DNode back = this._back;
        DNode n = new DNode(null, d, null);
        if (this._front == null & this._back == null){
            this._front = n;
            this._back = n;
        }
        else if (index == this.size() || index == -1){
            this.insertBack(d);
        }
        else if (Math.abs(index+1) == this.size()){
            this.insertFront(d);
        }
        else if (index > 0){
            for (int j = index; j != 1; j--){
                front = front._next;
            }
            n._prev = front;
            n._next = front._next;
            front._next._prev = n;
            front._next = n;
        }
        else if (index < -1){
            for (int j = index; j != -1; j++) {
                back = back._prev;
            }
            n._next = back._next;
            n._prev = back;
            back._next._prev = n;
            back._next = n;
        }
        else {
            n._next = front;
            this._front._prev = n;
            this._front = n;

        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int save = this._front._val;
        if (this.size() == 1){
            this._front = this._back = null;
            return save;
        }
        else {
            this._front = this._front._next;
            this._front._prev = null;
            return save;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int save = this._back._val;
        if (this.size() == 1){
            this._front = this._back = null;
            return save;
        }
        else {
            this._back = this._back._prev;
            this._back._next = null;
            return save;
        }
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        int save = this.get(index);
        DNode front = this._front;
        DNode back = this._back;
        if (this.size() == 1){
            this._front = this._back = null;
        }
        else if (index+1 == this.size() || index == -1){
            this.deleteBack();
        }
        else if ((Math.abs(index+1) == this.size() & index < 0) || index == 0|| index == -this.size()){
            this.deleteFront();
        }
        else if (index > 0){
            for (int j = index; j != 1; j--){
                front = front._next;
            }
            front._next._next._prev = front;
            front._next = front._next._next;
        }
        else if (index < -1){
            for (int j = index; j != -2; j++) {
                back = back._prev;
            }
            back._prev._prev._next = back;
            back._prev = back._prev._prev;
        }
        return save;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (this.size() == 0){
            return "[]";
        }
        else{
            DNode front = this._front;
            String str = "[";
            while (front != null){
                str += Integer.toString(front._val) + ", ";
                front = front._next;
            }
            return str.substring(0,str.length()-2) + "]";
        }
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
