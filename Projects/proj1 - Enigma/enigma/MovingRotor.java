package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Matthew Lu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;
    }

    @Override
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (this.permutation().alphabet().toChar(this.setting())
                    == ((_notches.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        super.set(permutation().wrap(this.setting() + 1));
    }

    /** My notches. */
    private String _notches;

    /** Gets my notches.
     * @return returns _notches */
    String notches() {
        return _notches;
    }

}
