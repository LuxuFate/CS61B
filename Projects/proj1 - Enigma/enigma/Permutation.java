package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Matthew Lu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.replace(")", "");
        cycles = cycles.replace("(", "");
        _cycles = cycles.split(" +");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycle = cycle.replace(")", "");
        cycle = cycle.replace("(", "");
        cycle = cycle.trim();
        String[] added = new String[_cycles.length + 1];
        for (int i = 0; i < _cycles.length; i++) {
            added[i] = _cycles[i];
        }
        added[_cycles.length + 1] = cycle;
        this._cycles = added;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        Character input = _alphabet.toChar(wrap(p));
        Character output = this.permute(input);
        return _alphabet.toInt(output);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        Character input = _alphabet.toChar(wrap(c));
        Character output = this.invert(input);
        return _alphabet.toInt(output);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (int c = 0; c < _cycles.length; c++) {
            for (int i = 0; i < _cycles[c].length(); i++) {
                if (_cycles[c].charAt(i) == p) {
                    return _cycles[c].charAt((i + 1) % _cycles[c].length());
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (int cy = 0; cy < _cycles.length; cy++) {
            for (int i = 0; i < _cycles[cy].length(); i++) {
                if (_cycles[cy].charAt(i) == c) {
                    if (i == 0) {
                        return _cycles[cy].charAt(_cycles[cy].length() - 1);
                    } else {
                        return _cycles[cy].charAt((i - 1)
                                % _cycles[cy].length());
                    }
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int total = 0;
        for (int i = 0; i < _cycles.length; i++) {
            total += _cycles[i].length();
            if (_cycles[i].length() == 1) {
                return false;
            }
        }
        if (total == _alphabet.size()) {
            return true;
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String[] _cycles;

    /** Getter for Cycles of this permutation.
     * @return returns the cycle as a string*/
    String getCycleString() {
        String cycleStr = "";
        for (int i = 0; i < _cycles.length; i++) {
            cycleStr += " " + "(" + _cycles[i] + ")" + " ";
        }
        return cycleStr;
    }
}
