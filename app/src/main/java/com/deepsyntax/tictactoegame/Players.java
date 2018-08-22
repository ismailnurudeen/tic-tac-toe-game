package com.deepsyntax.tictactoegame;

import android.graphics.Bitmap;

public class Players
{
	private static Bitmap mPlayer1Image=null;
	private static Bitmap mPlayer2Image=null;

	public static void setPlayer1Image(Bitmap img){
		mPlayer1Image = img;
	}
	public static void setPlayer2Image(Bitmap img){
		mPlayer2Image = img;
	}
	public static Bitmap getPlayer1Image(){
		return mPlayer1Image;
	}
	public static Bitmap getPlayer2Image(){
		return mPlayer2Image ;
	}
	public static void resetDefaults(){
		mPlayer1Image = null;
		mPlayer2Image = null;
	}
}
