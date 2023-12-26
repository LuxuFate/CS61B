package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Matthew Lu
 */
class Alphabet {

    /** An array of characters. */
    private char[] _char;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        this._char = new char[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            this._char[i] = chars.charAt(i);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this._char.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (char c: this._char) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _char[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int index = 0;
        for (int i = 0; i < this._char.length; i++) {
            if (this._char[i] == ch) {
                index = i;
            }
        }
        return index;
    }

}
