package com.deepsyntax.tictactoegame;


import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class Board{
	final private int xboxSymbol = R.drawable.x;
	final private int oboxSymbol = R.drawable.o;
	private String xWinColor="#03A9F4";
	private String oWinColor="#FFEB3B";
	private String tieColor="#4CAF50";
	private int winnerColor;
	private Context mContext;
	private ImageView mBox;
	private int[] mWinPattern;
	private int mSymbol;
	private int[] mPlayedMoves;
	public final int DEFAULT_BOX_HEIGHT =100;
	public final int DEFAULT_BOX_WIDTH=100;
	public int boardArea=-1;

	private ArrayList numsOfBoxes;

	public Board(Context context){
		mContext = context;
	}
	public Board(Context context, int symbol, int[] winPattern, int[] playedMoves){
		mContext = context;
		mPlayedMoves = playedMoves;
		mWinPattern = winPattern;
		mSymbol = symbol;
	}
	public Board(Context context, int symbol, int[] playedMoves){
		mContext = context;
		mPlayedMoves = playedMoves;
		mSymbol = symbol;
	}

	static public ArrayList generateBoxes(int nums){
		ArrayList<Integer> boxes = new ArrayList<>();
		for (int i = 0; i < nums; i++){
			boxes.add(i);
		}
		return boxes;
	}

	public void displayPattern(View v, int position){
		reDisplayBoard(v, position);

		if (mSymbol == oboxSymbol)
			winnerColor = Color.parseColor(oWinColor);
		else
			winnerColor = Color.parseColor(xWinColor);


		if (mWinPattern[0] != -1){
			for (int i:mWinPattern){
				if (position == i){
					mBox.setBackgroundColor(winnerColor);
				}
			}
		}
		else{
			mBox.setBackgroundColor(Color.parseColor(tieColor));
		}
	}
	public void reDisplayBoard(View v, int position){
		mBox = (ImageView) v.findViewById(R.id.game_box);
		for (int i=0;i < mPlayedMoves.length;i++){
			if (mPlayedMoves[i] != -1){
				if (i == position){
					mBox.setImageResource(mPlayedMoves[i]);
				}
			}
		}
	}
	public void drawBoardLines(View v, int position){
		if (position < 6){
			v.setPadding(0, 0, 0, 3);
		}
		if (!(position == 2 || position == 5 || position == 8)){
			v.setPadding(0, 0, 3, 0);
			if (position < 6)v.setPadding(0, 0, 3, 3);
		}
	}
	public void resizeBoard(GridView board, int height, int width){
		boardArea=pixelsToDips(width * 3);

		GameBoardAdapter adapter=new GameBoardAdapter(mContext, numsOfBoxes,GameControl.currentPlayer,GameControl.game.getAllMoves());
		adapter.setBoxArea(height, width);
		board.getLayoutParams().width = boardArea;
		board.setAdapter(adapter);
	}

	public void resetBoardSize(GridView board){
		setNumsBoxes(generateBoxes(9));
		resizeBoard(board,DEFAULT_BOX_HEIGHT,DEFAULT_BOX_WIDTH);
	}

	public void setBox(ImageView box){
		mBox = box;
	}

	public void setNumsBoxes(ArrayList nums){
		numsOfBoxes = nums;
	}

	public int pixelsToDips(int px){
		int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mContext.getResources().getDisplayMetrics());
		return dimensionInDp;
	}


	public static interface BoardResizeListener{
		public void onBoardResized(GridView board,int height,int width,int area);
	}
}
