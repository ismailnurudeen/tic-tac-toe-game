package com.deepsyntax.tictactoegame;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static final int[][] winCombo_5x5 = {
            //HORIZONTAL WIN COMBO
            {0, 1, 2, 3, 4},
            {5, 6, 7, 8, 9},
            {10, 11, 12, 13, 14},
            {15, 16, 17, 18, 19},
            {20, 21, 22, 23, 24},

            //VERTICAL WIN COMBO
            {0, 5, 10, 15, 20},
            {1, 6, 11, 16, 21},
            {2, 7, 12, 17, 22},
            {3, 8, 13, 18, 23},
            {4, 9, 14, 19, 24},

            //DIAGONAL WIN COMBO
            {0, 6, 12, 18, 24},
            {4, 8, 12, 16, 20}
    };
    private static final int[][] winCombo_3x3 = {
            //HORIZONTAL WIN COMBO
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},

            //VERTICAL WIN COMBO
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},

            //DIAGONAL WIN COMBO
            {0, 4, 8},
            {2, 4, 6}
    };
    public final static int[] LEADING_DAIGONAL_5x5 = winCombo_5x5[10];
    public final static int[] SUPPORTING_DIAGONAL_5x5 = winCombo_5x5[11];

    public final static int[][] HORIZONTAL_PATTERNS_5x5 = {winCombo_5x5[0], winCombo_5x5[1], winCombo_5x5[2], winCombo_5x5[3], winCombo_5x5[4]};
    public final static int[][] VERTICAL_PATTERNS_5x5 = {winCombo_5x5[5], winCombo_5x5[6], winCombo_5x5[7], winCombo_5x5[8], winCombo_5x5[9]};

    public final static int[] LEADING_DAIGONAL_3x3 = winCombo_3x3[6];
    public final static int[] SUPPORTING_DIAGONAL_3x3 = winCombo_3x3[7];

    public final static int[][] HORIZONTAL_PATTERNS_3x3 = {winCombo_3x3[0], winCombo_3x3[1], winCombo_3x3[2]};
    public final static int[][] VERTICAL_PATTERNS_3x3 = {winCombo_3x3[3], winCombo_3x3[4], winCombo_3x3[5]};
    public final static int[] DRAW_PATTERN = {-1};

    private int mBoardLength;

    private int[] pattern;
    private int moves[] = new int[9];
    private boolean isFirstMove;
    Context mContext;
    private static int xPlayerSymbol = R.drawable.x;
    private static int oPlayerSymbol = R.drawable.o;

    ArrayList<ArrayList> winComboList = new ArrayList<>();

    private boolean gameWon;


    public Game(Context ctx) {
        mContext = ctx;
    }

    public Game(Context ctx, int boardLength) {
        mContext = ctx;
        mBoardLength = boardLength;
        moves = new int[(int) Math.pow(boardLength, 2)];
        Log.v("MASTER TICTACTOE", boardLength + "");
        newGame();
    }

    public void newGame() {
        isFirstMove = true;
        for (int i = 0; i < moves.length; i++) moves[i] = -1;
    }

    public int isGameOver(ArrayList<Integer> playedPosList, int position, int player) {
        moves[position] = player;
        return whoWon(moves);
    }

    public int getMoves(int[] myMoves, int x, int y) {
        return myMoves[mBoardLength * y + x];
    }

    public int[] getAllMoves() {
        return this.moves;
    }

    public ArrayList<Integer> getMovesList() {
        ArrayList<Integer> movesList = new ArrayList<>();
        for (int move : getAllMoves()) movesList.add(move);
        return movesList;
    }

    public ArrayList<Integer> getEmptySpots(ArrayList<Integer> allSpots) {
        ArrayList<Integer> emptySpots = new ArrayList<>();
        for (int i = 0; i < getAllMoves().length; i++) {
            if (allSpots.get(i) == -1) {
                emptySpots.add(i);
            }
        }
        return emptySpots;
    }

    public void addMove(int position, int player) {

    }

    public int whoWon(int[] boardMoves) {
        if (!(mBoardLength > 3)) {
            for (int i = 0; i < mBoardLength; i++) {
                if (getMoves(boardMoves, i, 0) != -1 &&
                        getMoves(boardMoves, i, 0) == getMoves(boardMoves, i, 1) &&
                        getMoves(boardMoves, i, 1) == getMoves(boardMoves, i, 2)) {
                    gameWon = true;
                    pattern = VERTICAL_PATTERNS_3x3[i];
                    return getMoves(boardMoves, i, 0);
                }

                if (getMoves(boardMoves, 0, i) != -1 &&
                        getMoves(boardMoves, 0, i) == getMoves(boardMoves, 1, i) &&
                        getMoves(boardMoves, 1, i) == getMoves(boardMoves, 2, i)) {
                    gameWon = true;
                    pattern = HORIZONTAL_PATTERNS_3x3[i];
                    return getMoves(boardMoves, 0, i);
                }
            }
            if (getMoves(boardMoves, 0, 0) != -1 &&
                    getMoves(boardMoves, 0, 0) == getMoves(boardMoves, 1, 1) &&
                    getMoves(boardMoves, 1, 1) == getMoves(boardMoves, 2, 2)) {
                gameWon = true;
                pattern = LEADING_DAIGONAL_3x3;
                return getMoves(boardMoves, 0, 0);
            }

            if (getMoves(boardMoves, 2, 0) != -1 &&
                    getMoves(boardMoves, 2, 0) == getMoves(boardMoves, 1, 1) &&
                    getMoves(boardMoves, 1, 1) == getMoves(boardMoves, 0, 2)) {
                gameWon = true;
                pattern = SUPPORTING_DIAGONAL_3x3;
                return getMoves(boardMoves, 2, 0);
            }

        } else {
            for (int i = 0; i < mBoardLength; i++) {
                //Check 5x5 board win
                if (getMoves(boardMoves, i, 0) != -1 &&
                        getMoves(boardMoves, i, 0) == getMoves(boardMoves, i, 1) &&
                        getMoves(boardMoves, i, 1) == getMoves(boardMoves, i, 2) &&
                        getMoves(boardMoves, i, 2) == getMoves(boardMoves, i, 3) &&
                        getMoves(boardMoves, i, 3) == getMoves(boardMoves, i, 4)) {
                    gameWon = true;
                    pattern = VERTICAL_PATTERNS_5x5[i];
                    return getMoves(boardMoves, i, 0);
                }

                if (getMoves(boardMoves, 0, i) != -1 &&
                        getMoves(boardMoves, 0, i) == getMoves(boardMoves, 1, i) &&
                        getMoves(boardMoves, 1, i) == getMoves(boardMoves, 2, i) &&
                        getMoves(boardMoves, 2, i) == getMoves(boardMoves, 3, i) &&
                        getMoves(boardMoves, 3, i) == getMoves(boardMoves, 4, i)) {
                    gameWon = true;
                    pattern = HORIZONTAL_PATTERNS_5x5[i];
                    return getMoves(boardMoves, 0, i);
                }
            }
            if (getMoves(boardMoves, 0, 0) != -1 &&
                    getMoves(boardMoves, 0, 0) == getMoves(boardMoves, 1, 1) &&
                    getMoves(boardMoves, 1, 1) == getMoves(boardMoves, 2, 2) &&
                    getMoves(boardMoves, 2, 2) == getMoves(boardMoves, 3, 3) &&
                    getMoves(boardMoves, 3, 3) == getMoves(boardMoves, 4, 4)) {
                gameWon = true;
                pattern = LEADING_DAIGONAL_5x5;
                return getMoves(boardMoves, 0, 0);
            }

            if (getMoves(boardMoves, 4, 0) != -1 &&
                    getMoves(boardMoves, 4, 0) == getMoves(boardMoves, 3, 1) &&
                    getMoves(boardMoves, 3, 1) == getMoves(boardMoves, 2, 2) &&
                    getMoves(boardMoves, 2, 2) == getMoves(boardMoves, 1, 3) &&
                    getMoves(boardMoves, 1, 3) == getMoves(boardMoves, 0, 4)) {
                gameWon = true;
                pattern = SUPPORTING_DIAGONAL_5x5;
                return getMoves(boardMoves, 4, 0);
            }
        }
        for (int j = 0; j < mBoardLength * mBoardLength; j++) {
            if (moves[j] == -1)
                return -1;
        }
        pattern = DRAW_PATTERN;
        return 0;
    }


    public int machinePlayer() {
        Random random = new Random();
        int randomSpot = random.nextInt(getEmptySpots(getMovesList()).size());

        return getEmptySpots(getMovesList()).get(randomSpot);
    }


    public void setPlayersSymbol(int xPlayer, int oPlayer) {
        xPlayerSymbol = xPlayer;
        oPlayerSymbol = oPlayer;
    }

    public void setXPlayerSymbol(int xPlayerSymbol) {
        xPlayerSymbol = xPlayerSymbol;
    }

    public void setOPlayerSymbol(int oPlayerSymbol) {
        oPlayerSymbol = oPlayerSymbol;
    }

    public int getXPlayerSymbol() {
        return xPlayerSymbol;
    }

    public int getOPlayerSymbol() {
        return oPlayerSymbol;
    }

    public int[] getWinPattern() {
        return this.pattern;
    }

    public int[] intListToArray(ArrayList<Integer> intList) {
        int[] intArray = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) intArray[i] = intList.get(i);
        return intArray;
    }

    public ArrayList<Integer> intArrayToList(int[] intArray) {
        ArrayList<Integer> intList = new ArrayList<>();
        for (int value : intArray) intList.add(value);
        return intList;
    }

    public int minimax(int[] playedMovesArray, int player) {
        ArrayList<Integer> emptySpots = getEmptySpots(intArrayToList(playedMovesArray));

        if (whoWon(playedMovesArray) == xPlayerSymbol) {
            return -10;
        } else if (whoWon(playedMovesArray) == oPlayerSymbol) {
            return 10;
        } else if (whoWon(playedMovesArray) == 0) {
            return 0;
        }

        ArrayList<Integer> possibleMove = new ArrayList<>();
        ArrayList<Integer> possibleScores = new ArrayList<>();


        for (int i = 0; i < emptySpots.size(); i++) {
            int spotIndex = emptySpots.get(i);
            playedMovesArray[spotIndex] = player;
            int result = -1;
            if (player == oPlayerSymbol) {
                result = minimax(playedMovesArray, xPlayerSymbol);
            } else if (player == xPlayerSymbol) {
                result = minimax(playedMovesArray, oPlayerSymbol);
            }
            playedMovesArray[spotIndex] = -1;
            possibleScores.add(result);
            possibleMove.add(spotIndex);
        }
        int bestMove = -1;
        if (player == oPlayerSymbol) {
            int bestScore = -10000;
            for (int j = 0; j < possibleScores.size(); j++) {
                if (possibleScores.get(j) > bestScore) {
                    bestScore = possibleScores.get(j);
                    bestMove = j;
                }
            }
        } else {
            int bestScore = 10000;
            for (int j = 0; j < possibleScores.size(); j++) {
                if (possibleScores.get(j) < bestScore) {
                    bestScore = possibleScores.get(j);
                    bestMove = j;
                }
            }
        }
        return possibleMove.get(bestMove);
    }

    public int myAi() {
        int[] myMoves = getAllMoves();
        if (isFirstMove) {
            return makeFirstMove(myMoves);
        } else {
            Log.v("O WIN - BLOCKWIN", winOrBlockWin(oPlayerSymbol, winCombo_3x3) + "");
            Log.v("X WIN - BLOCKWIN", winOrBlockWin(xPlayerSymbol, winCombo_3x3) + "");
            if (winOrBlockWin(oPlayerSymbol, winCombo_3x3) != -1) {
                return winOrBlockWin(oPlayerSymbol, winCombo_3x3);
            } else if (winOrBlockWin(xPlayerSymbol, winCombo_3x3) != -1) {
                return winOrBlockWin(xPlayerSymbol, winCombo_3x3);
            } else if ((myMoves[winCombo_3x3[0][0]] != -1 && myMoves[winCombo_3x3[0][0]] == myMoves[winCombo_3x3[2][2]]
                    && myMoves[winCombo_3x3[0][0]] != oPlayerSymbol) ||
                    (myMoves[winCombo_3x3[0][2]] != -1 && myMoves[winCombo_3x3[0][2]] == myMoves[winCombo_3x3[2][0]]
                            && myMoves[winCombo_3x3[0][0]] != oPlayerSymbol)) {
                return emptyEdge();
            }
            /*else{
             return minimax(myMoves, oPlayerSymbol);
			 }*/
            else if (myMoves[winCombo_3x3[0][0]] != -1 && myMoves[winCombo_3x3[2][2]] == -1) {
                return winCombo_3x3[2][2];
            } else if (myMoves[winCombo_3x3[2][2]] != -1 && myMoves[winCombo_3x3[0][0]] == -1) {
                return winCombo_3x3[0][0];
            } else if (myMoves[winCombo_3x3[0][2]] != -1 && myMoves[winCombo_3x3[2][0]] == -1) {
                return winCombo_3x3[2][0];
            } else if (myMoves[winCombo_3x3[2][0]] != -1 && myMoves[winCombo_3x3[0][2]] == -1) {
                return winCombo_3x3[0][2];
            } else if (emptyConner() != -1) {
                return emptyConner();
            } else {
                return emptyEdge();
            }
        }
    }

    private int emptyConner() {
        if (getAllMoves()[winCombo_3x3[0][0]] == -1)
            return winCombo_3x3[0][0];

        if (getAllMoves()[winCombo_3x3[0][2]] == -1)
            return winCombo_3x3[0][2];

        if (getAllMoves()[winCombo_3x3[2][0]] == -1)
            return winCombo_3x3[2][0];

        if (getAllMoves()[winCombo_3x3[2][2]] == -1)
            return winCombo_3x3[2][2];

        return -1;
    }

    private int emptyEdge() {
        if (getAllMoves()[winCombo_3x3[0][1]] == -1)
            return winCombo_3x3[0][1];

        else if (getAllMoves()[winCombo_3x3[1][0]] == -1)
            return winCombo_3x3[1][0];

        else if (getAllMoves()[winCombo_3x3[1][2]] == -1)
            return winCombo_3x3[1][2];

        else if (getAllMoves()[winCombo_3x3[2][1]] == -1)
            return winCombo_3x3[2][1];

        return -1;
    }

    private int makeFirstMove(int[] myMoves) {
        int madeMove = firstPlayedPosition();
        isFirstMove = !isFirstMove;
        if (madeMove == winCombo_3x3[0][0] || madeMove == winCombo_3x3[0][2] ||
                madeMove == winCombo_3x3[2][0] || madeMove == winCombo_3x3[2][2]) {
            return winCombo_3x3[1][1];
        } else if (madeMove == winCombo_3x3[0][1] || madeMove == winCombo_3x3[1][0] ||
                madeMove == winCombo_3x3[1][2] || madeMove == winCombo_3x3[2][1]) {
            return winCombo_3x3[1][1];
        } else {
            return emptyConner();
        }
    }

    public int firstPlayedPosition() {
        for (int i = 0; i < getAllMoves().length; i++) {
            if (getAllMoves()[i] != -1) return i;
        }
        return -1;
    }

    public int winOrBlockWin(int player, int[][] winCombo) {
        for (int[] aWinCombo : winCombo) {
            int bestSpot = -1;
            int status = 0;
            for (int comboIndex : aWinCombo) {
                if (moves[comboIndex] != -1) {
                    if (moves[comboIndex] == player) {
                        status++;
                    } else {
                        status = 0;
                        break;
                    }
                } else {
                    bestSpot = comboIndex;
                }
            }
            if (status > 1) return bestSpot;
        }
        return -1;
    }

    public void isFirstMove(boolean state) {
        isFirstMove = state;
    }
}
