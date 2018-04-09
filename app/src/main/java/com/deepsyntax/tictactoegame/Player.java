package com.deepsyntax.tictactoegame;

import android.content.*;
public class Player
{
	int playerSymbol=R.drawable.o;
	private String mPlayerName;
	private String mPlayerImage;
	private Context mContext;

	public Player(Context context,String playerImage){
		this.mPlayerImage = playerImage;
		this.mContext = context;
	}

	public Player(Context context,String playerName, String playerImage){
		this.mPlayerName = playerName;
		this.mPlayerImage = playerImage;
		this.mContext = context;
	}


	public void setPlayerSymbol(int playerSymbol){
		this.playerSymbol = playerSymbol;
	}

	public int getPlayerSymbol(){
		return playerSymbol;
	}

	public void setPlayerName(String playerName){
		this.mPlayerName = playerName;
	}

	public String getPlayerName(){
		return mPlayerName;
	}

	public void setPlayerImage(String playerImage){
		this.mPlayerImage = playerImage;
	}

	public String getPlayerImage(){
		return mPlayerImage;
	}
}
