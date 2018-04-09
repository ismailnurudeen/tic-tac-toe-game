package com.deepsyntax.tictactoegame;

import android.app.*;
import android.os.*;
import android.content.*;
import android.support.v7.app.AppCompatActivity;

import java.util.*;

public class PlayersInfoActivity extends AppCompatActivity implements FragmentsInterface{
	public static String PLAYER_1_TAG="Player One";
	public static String PLAYER_2_TAG="Player Two";
	public static String PREF_NAME="USER_INFO";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	
	FragmentManager manager;
	FragmentTransaction transaction;
	Bundle dataBundle;

	private Intent in;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_info);
		dataBundle = new Bundle();
		prefs=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
		editor=prefs.edit();
		
		manager = getFragmentManager();
		transaction = manager.beginTransaction();

		transaction.replace(R.id.activity_players_infoFrameLayout, new PlayersInfoFragment(), PLAYER_1_TAG);
		transaction.commit();
		in=new Intent(this, MainGameActivity.class);
	}

	@Override
	public void OnPlayerSettingsComplete(ArrayList<String> playerNames){
		InitialSettingsFragment initialSettings=new InitialSettingsFragment();
		initialSettings.newInstance(playerNames);
		getFragmentManager()
			.beginTransaction()
			.replace(R.id.activity_players_infoFrameLayout, initialSettings, "InitialSettings")
			.commit();
	}
	@Override
	public void OnGameSettingsComplete(ArrayList<String> playerNames,int rounds){
		in.putExtra("SINGLE_PLAYER",false);
		in.putExtra("NUMBER_OF_ROUNDS",rounds);
		in.putStringArrayListExtra("PLAYER_NAMES",playerNames);
		startActivity(in);
		finish();
	}

	
}
