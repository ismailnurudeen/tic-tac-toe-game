package com.deepsyntax.tictactoegame;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static final int[][] winCombo = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    public final static int[] LEADING_DAIGONAL = winCombo[6];
    public final static int[] SUPPORTING_DIAGONAL = winCombo[7];

    public final static int[][] HORIZONTAL_PATTERNS = {winCombo[0], winCombo[1], winCombo[2]};
    public final static int[][] VERTICAL_PATTERNS = {winCombo[3], winCombo[4], winCombo[5]};
    public final static int[] DRAW_PATTERN = {-1};

    private int[] pattern;
    private int moves[] = new int[9];
    private int tempMoves[] = new int[9];
    Context mContext;
    private int xPlayerSymbol;
    private int oPlayerSymbol;

    ArrayList<ArrayList> winComboList = new ArrayList<>();

    private boolean gameWon;


    public Game(Context ctx) {
        mContext = ctx;
        xPlayerSymbol = R.drawable.x;
        oPlayerSymbol = R.drawable.o;
        newGame();
    }

    public void newGame() {
        for (int i = 0; i < moves.length; i++) moves[i] = -1;
    }

    public int isGameOver(ArrayList<Integer> playedPosList, int position, int player) {
        moves[position] = player;
        return whoWon(moves);
    }

    public int getMoves(int[] theMoves, int x, int y) {
        return theMoves[3 * y + x];
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
        for (int i = 0; i < 3; i++) {
            if (getMoves(boardMoves, i, 0) != -1 &&
                    getMoves(boardMoves, i, 0) == getMoves(boardMoves, i, 1) &&
                    getMoves(boardMoves, i, 1) == getMoves(boardMoves, i, 2)) {
                gameWon = true;
                pattern = VERTICAL_PATTERNS[i];
                return getMoves(boardMoves, i, 0);
            }

            if (getMoves(boardMoves, 0, i) != -1 &&
                    getMoves(boardMoves, 0, i) == getMoves(boardMoves, 1, i) &&
                    getMoves(boardMoves, 1, i) == getMoves(boardMoves, 2, i)) {
                gameWon = true;
                pattern = HORIZONTAL_PATTERNS[i];
                return getMoves(boardMoves, 0, i);
            }
        }

        if (getMoves(boardMoves, 0, 0) != -1 &&
                getMoves(boardMoves, 0, 0) == getMoves(boardMoves, 1, 1) &&
                getMoves(boardMoves, 1, 1) == getMoves(boardMoves, 2, 2)) {
            gameWon = true;
            pattern = LEADING_DAIGONAL;
            return getMoves(boardMoves, 0, 0);
        }

        if (getMoves(boardMoves, 2, 0) != -1 &&
                getMoves(boardMoves, 2, 0) == getMoves(boardMoves, 1, 1) &&
                getMoves(boardMoves, 1, 1) == getMoves(boardMoves, 0, 2)) {
            gameWon = true;
            pattern = SUPPORTING_DIAGONAL;
            return getMoves(boardMoves, 2, 0);
        }

        for (int i = 0; i < 9; i++) {
            if (moves[i] == -1)
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


    public void setPlayersSymbols(int xPlayer, int oPlayer) {
        this.xPlayerSymbol = xPlayer;
        this.oPlayerSymbol = oPlayer;
    }

    public void setXPlayerSymbol(int xPlayerSymbol) {
        this.xPlayerSymbol = xPlayerSymbol;
    }

    public int getXPlayerSymbol() {
        return xPlayerSymbol;
    }

    public void setOPlayerSymbol(int oPlayerSymbol) {
        this.oPlayerSymbol = oPlayerSymbol;
    }

    public int getOPlayerSymbol() {
        return oPlayerSymbol;
    }

    public int[] getWinPattern() {
        return this.pattern;
    }

    boolean isTempMovesSet;

    public void setTempMoves() {
        if (isTempMovesSet) return;
        System.arraycopy(getAllMoves(), 0, tempMoves, 0, getAllMoves().length);
        isTempMovesSet = true;
    }

    public int getTempMoves(int x, int y) {
        return tempMoves[3 * y + x];
    }

    public int[] getAllTempMoves() {
        return this.tempMoves;
    }

    public ArrayList<Integer> getTempEmptySpots() {
        ArrayList<Integer> tempEmptySpots = new ArrayList<>();
        for (int i = 0; i < getAllTempMoves().length; i++) {
            if (getAllTempMoves()[i] == -1) {
                tempEmptySpots.add(i);
            }
        }
        return tempEmptySpots;
    }

    //    public int checkWin() {
//        for (int i = 0; i < 3; i++) {
//            if (getTempMoves(i, 0) != -1 &&
//                    getTempMoves(i, 0) == getMoves(i, 1) &&
//                    getTempMoves(i, 1) == getMoves(i, 2)) {
//                return getTempMoves(i, 0);
//            }
//
//            if (getTempMoves(0, i) != -1 &&
//                    getTempMoves(0, i) == getMoves(1, i) &&
//                    getTempMoves(1, i) == getMoves(2, i)) {
//                return getTempMoves(0, i);
//            }
//        }
//
//        if (getTempMoves(0, 0) != -1 &&
//                getTempMoves(0, 0) == getMoves(1, 1) &&
//                getTempMoves(1, 1) == getMoves(2, 2)) {
//            return getTempMoves(0, 0);
//        }
//
//        if (getTempMoves(2, 0) != -1 &&
//                getTempMoves(2, 0) == getMoves(1, 1) &&
//                getTempMoves(1, 1) == getMoves(0, 2)) {
//            return getTempMoves(2, 0);
//        }
//        return 0;
//    }
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

}
