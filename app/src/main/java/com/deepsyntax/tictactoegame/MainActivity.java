package com.deepsyntax.tictactoegame;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.content.*;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
	public void handleClicks(View v){
		switch (v.getId()){
			case R.id.single_player_btn:
				Intent startGame=new Intent(MainActivity.this,MainGameActivity.class);
				startGame.putExtra("SINGLE_PLAYER",true);
				startActivity(startGame);
			break;
			case R.id.multi_player_btn:
				Intent startGameSettings=new Intent(MainActivity.this,PlayersInfoActivity.class);
				startActivity(startGameSettings);
				break;
		}
	}
}
