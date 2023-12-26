import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Matthew Lu
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        this._root = putN(s, _root);
    }

    private Node putN(String s, Node root) {
        if (root == null) {
            return new Node(s);
        } else {
            if (root.left == null && root.s.compareTo(s) > 0) {
                root.left = new Node(s);
            } else if (root.right == null && root.s.compareTo(s) < 0) {
                root.right = new Node(s);
            } else {
                if (root.left != null && root.s.compareTo(s) > 0) {
                    root.left = putN(s, root.left);
                } else if (root.right != null && root.s.compareTo(s) < 0) {
                    root.right = putN(s, root.right);
                }
            }
        }
        return root;
    }

    @Override
    public boolean contains(String s) {
        return containsN(s, _root);
    }

    private boolean containsN(String s, Node root) {
        if (root == null) {
            return false;
        } else if (root.s.compareTo(s) == 0) {
            return true;
        } else {
            if (root.s.compareTo(s) < 0) {
                return containsN(s, root.right);
            } else if (root.s.compareTo(s) > 0) {
                return containsN(s, root.left);
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        return asListN(_root);
    }

    private List<String> asListN(Node root) {
        ArrayList list = new ArrayList<String>();
        for (String string: this) {
            list.add(string);
        }
        return list;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }

    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTBoundedIterator(low, high);
    }

    private class BSTBoundedIterator extends BSTIterator {

        private String _low;
        private String _high;
        private Node _start;
        private Stack<Node> _toDo = new Stack<>();

        public BSTBoundedIterator(String low, String high) {
            super(_root);
            _low = low;
            _high = high;
            _start = _root;
        }

        @Override
        public boolean hasNext() {
            if (!_toDo.isEmpty()) {
                return true;
            } else if (_start != null && _start.s.compareTo(_high) < 0) {
                return true;
            } else {
                if (_start!= null && _start.left != null && _start.left.s.compareTo(_high) < 0) {
                    _start = _start.left;
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public String next() {
            while (_start != null && _start.s.compareTo(_low) >= 0){
                _toDo.push(_start);
                _start = _start.left;
            }
            Node get = _toDo.pop();
            _start = get.right;
            return get.s;
        }
    }

    /** Root node of the tree. */
    private Node _root;

    /** Root Getter. */
    public Node root() {
        return _root;
    }

    /** String Getter. */
    public String s(Node n) {
        return n.s;
    }
    /** Left Getter. */
    public Node left(Node n) {
        return n.left;
    }
    /** Right Getter. */
    public Node right(Node n) {
        return n.right;
    }
}
