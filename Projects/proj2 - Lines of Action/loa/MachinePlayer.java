/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import static loa.Piece.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/** An automated Player.
 *  @author Matthew Lu
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;
        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (board.gameOver()) {
            if (board.winner() == board.turn().opposite()) {
                return WINNING_VALUE;
            } else {
                return -WINNING_VALUE;
            }
        }
        if (depth == 0) {
            return judge(board, board.turn().opposite());
        }
        List<Move> poslist = board.legalMoves();
        ArrayList<Integer> vals = new ArrayList<Integer>();
        Move found = null;
        int best = 0;
        for (Move mv: poslist) {
            Board newBoard = new Board(board);
            newBoard.makeMove(mv);
            int save = findMove(newBoard, depth - 1,
                    saveMove, -sense, alpha, beta);
            vals.add(save);
            if (sense == 1) {
                alpha = Math.max(save, alpha);
                if (save > best) {
                    best = save;
                    found = mv;
                }
            } else if (sense == -1) {
                beta = Math.min(save, beta);
                if (best == 0) {
                    best = save;
                    found = mv;
                }
                if (save < best) {
                    best = save;
                    found = mv;
                }
            }
            if (alpha > beta || best == WINNING_VALUE) {
                break;
            }
        }
        if (saveMove) {
            _foundMove = found;
        }
        if (sense > 0) {
            return Collections.min(vals);
        } else {
            return Collections.max(vals);
        }
    }


    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    /** Finds an Int that judges the current board for a Piece.
     * @param board The Board
     * @param turn The Turn
     * @return Judge Value */
    private int judge(Board board, Piece turn) {
        if (board.winner() != null) {
            if (board.winner() == turn) {
                return WINNING_VALUE;
            } else {
                return -WINNING_VALUE;
            }
        } else {
            return (int) (100 * (1 / (avgDistance(board, turn))));
        }
    }

    /** Finds the average distance between all of the pieces of a certain color.
     * @param board The Board
     * @param turn The Turn
     * @return Average distance */
    private double avgDistance(Board board, Piece turn) {
        double sum = 0;
        int count = 0;
        for (Square from : Square.ALL_SQUARES) {
            if (board.get(from) == turn) {
                count++;
                for (Square to : Square.ALL_SQUARES) {
                    if (from != to && board.get(to) == turn) {
                        int x1 = from.col();
                        int y1 = from.row();
                        int x2 = to.col();
                        int y2 = to.row();
                        sum += Math.sqrt(Math.pow((y2 - y1), 2)
                                + Math.pow((x2 - x1), 2));
                    }
                }
            }
        }
        return sum / count;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
