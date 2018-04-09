package com.deepsyntax.tictactoegame;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class GameBoardAdapter extends ArrayAdapter{
    private int[] mWinPattern;
    private int[] mPlayedMoves;
    private Board mBoard;
    private Context mContext;
    private static int boxHeight=100;
    private static int boxWidth=100;

    public GameBoardAdapter(Context ctx, ArrayList boxes){
        super(ctx, 0, boxes);
        mContext=ctx;
        mBoard=new Board(ctx);
        mBoard.setNumsBoxes(boxes);
    }
    public GameBoardAdapter(Context ctx, ArrayList boxes, int symbol, int[] winPattern, int[] playedMoves){
        super(ctx, 0, boxes);
        mContext=ctx;
        mWinPattern = winPattern;
        mPlayedMoves = playedMoves;
        mBoard = new Board(ctx, symbol, winPattern, playedMoves);
        mBoard.setNumsBoxes(boxes);
    }
    public GameBoardAdapter(Context ctx, ArrayList boxes, int symbol, int[] playedMoves){
        super(ctx, 0, boxes);
        mContext=ctx;
        mPlayedMoves = playedMoves;
        mBoard = new Board(ctx, symbol, playedMoves);
        mBoard.setNumsBoxes(boxes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_box, parent, false);
        if (mWinPattern != null){
            mBoard.displayPattern(convertView, position);
        }
        else if (mPlayedMoves != null){
            mBoard.reDisplayBoard(convertView, position);
        }

        ImageView box = convertView.findViewById(R.id.game_box);
        mBoard.setBox(box);
        box.getLayoutParams().height =mBoard.pixelsToDips(boxHeight);
        box.getLayoutParams().width =mBoard.pixelsToDips(boxWidth);
        mBoard.drawBoardLines(convertView, position);

        return convertView;
    }
    public void setBoxArea(int height,int width){
        boxHeight=height;
        boxWidth=width;
    }
}
