package com.deepsyntax.tictactoegame;


import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Board {
    final private int xboxSymbol = R.drawable.x;
    final private int oboxSymbol = R.drawable.o;
    private String xWinColor = "#03A9F4";
    private String oWinColor = "#FFEB3B";
    private String tieColor = "#4CAF50";
    private int winnerColor;
    private Context mContext;
    private ImageView mBox;
    private int[] mWinPattern;
    private int mSymbol;
    private int[] mPlayedMoves;
    public final int DEFAULT_BOX_HEIGHT_3x3 = 80;
    public final int DEFAULT_BOX_WIDTH_3x3 = 80;
    public final int DEFAULT_BOX_HEIGHT_5x5 = 50;
    public final int DEFAULT_BOX_WIDTH_5x5 = 50;
    private static int boxHeight = 80;
    private static int boxWidth = 80;
    public static int numOfColumns = 3;

    public int boardArea = -1;

    private ArrayList numsOfBoxes;

    public Board(Context context) {
        mContext = context;
    }

    public Board(Context context, int symbol, int[] winPattern, int[] playedMoves) {
        mContext = context;
        mPlayedMoves = playedMoves;
        mWinPattern = winPattern;
        mSymbol = symbol;
    }

    public Board(Context context, int symbol, int[] playedMoves) {
        mContext = context;
        mPlayedMoves = playedMoves;
        mSymbol = symbol;
    }

    static public ArrayList generateBoxes(int nums) {
        ArrayList<Integer> boxes = new ArrayList<>();
        for (int i = 0; i < nums; i++) {
            boxes.add(i);
        }
        return boxes;
    }

    public void displayPattern(View v, int position) {
        reDisplayBoard(v, position);

        if (mSymbol == oboxSymbol)
            winnerColor = Color.parseColor(oWinColor);
        else
            winnerColor = Color.parseColor(xWinColor);


        if (mWinPattern[0] != -1) {
            for (int i : mWinPattern) {
                if (position == i) {
                    mBox.setBackgroundColor(winnerColor);
                }
            }
        } else {
            mBox.setBackgroundColor(Color.parseColor(tieColor));
        }
    }

    public void reDisplayBoard(View v, int position) {
        mBox = v.findViewById(R.id.game_box);
        for (int i = 0; i < mPlayedMoves.length; i++) {
            if (mPlayedMoves[i] != -1) {
                if (i == position) {
                    mBox.setImageResource(mPlayedMoves[i]);
                }
            }
        }
    }

    public void drawBoardLines(View v, int position) {
        if (!(numsOfBoxes.size() > 9)) {
            if (position < 6) {
                v.setPadding(0, 0, 0, 3);
            }
            if (!(position == 2 || position == 5 || position == 8)) {
                v.setPadding(0, 0, 3, 0);
                if (position < 6) v.setPadding(0, 0, 3, 3);
            }
        } else {
            if (position < 20) {
                v.setPadding(0, 0, 0, 3);
            }
            if (!(position == 4 || position == 9 || position == 14 || position == 19 || position == 24)) {
                v.setPadding(0, 0, 3, 0);
                if (position < 20) v.setPadding(0, 0, 3, 3);
            }
        }
    }

    public void resizeBoard(GridView board, int height, int width) {
        boardArea = pixelsToDips(width * board.getNumColumns());
        board.getLayoutParams().width = boardArea;
        board.getLayoutParams().height = boardArea;

        setBoxArea(height, width);
        GameBoardAdapter adapter = new GameBoardAdapter(mContext, numsOfBoxes, GameControl.currentPlayer, GameControl.game.getAllMoves());
        board.setAdapter(adapter);
    }

    public void resetBoardSize(GridView board) {
        int grids = (int) Math.pow(board.getNumColumns(), 2);
        setNumsBoxes(generateBoxes(grids));
        if (!(grids > 9)) {
            resizeBoard(board, DEFAULT_BOX_HEIGHT_3x3, DEFAULT_BOX_WIDTH_3x3);
        } else {
            resizeBoard(board, DEFAULT_BOX_HEIGHT_5x5, DEFAULT_BOX_WIDTH_5x5);
        }
    }

    public void setBox(ImageView box) {
        mBox = box;
    }

    public void setNumsBoxes(ArrayList nums) {
        numsOfBoxes = nums;
    }

    public static int getBoxHeight() {
        return boxHeight;
    }

    public static int getBoxWidth() {
        return boxWidth;
    }

    public void setBoxArea(int height, int width) {
        boxHeight = height;
        boxWidth = width;
    }

    public int pixelsToDips(int px) {
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mContext.getResources().getDisplayMetrics());
        return dimensionInDp;
    }

    public void setNumsOfColumns(int num) {
        numOfColumns = num;
    }

    public static interface BoardResizeListener {
        public void onBoardResized(GridView board, int height, int width, int area);
    }
}
