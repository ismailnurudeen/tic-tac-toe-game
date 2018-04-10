package com.deepsyntax.tictactoegame;

import android.app.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.FileNotFoundException;
import java.util.*;

public class PlayersInfoActivity extends AppCompatActivity implements FragmentsInterface{
	public static String PLAYER_1_TAG="Player One";
	public static String PLAYER_2_TAG="Player Two";
	public static final int UPLOAD_IMAGE_REQUEST=1;
	public static String PREF_NAME="USER_INFO";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	
	FragmentManager manager;
	FragmentTransaction transaction;
	Bundle dataBundle;

	private Intent in;
	private byte[] imageValue;

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
	public void OnGameSettingsComplete(ArrayList<String> playerNames,ArrayList<byte[]> playerImages,int rounds){
		in.putExtra("SINGLE_PLAYER",false);
		in.putExtra("NUMBER_OF_ROUNDS",rounds);
		in.putStringArrayListExtra("PLAYER_NAMES",playerNames);
		//in.putExtra("PLAYER_IMAGES",byteListToArray(playerImages));
		startActivity(in);
		finish();
	}

	@Override
	public void OnUploadImage() {
		uploadImage();
	}

	public void uploadImage(){
		Intent getImageIntent=new Intent(Intent.ACTION_GET_CONTENT);
		getImageIntent.setType("image/*");
		startActivityForResult(getImageIntent.createChooser(getImageIntent, "Select an Image"), UPLOAD_IMAGE_REQUEST);
	}


//    private byte[] byteListToArray(ArrayList<Byte[]> playerImages) {
//	    byte[] imageArray=new byte[playerImages.size()];
//	    for(int i=0;i<playerImages.size();i++) imageArray[i]=playerImages.get(i);
//	    return imageArray;
//    }


//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		// TODO: Implement this method
//		if (resultCode == RESULT_OK){
//			switch (requestCode){
//				case UPLOAD_IMAGE_REQUEST:
//					Uri imageUri=data.getData();
//					try{
//						Bitmap imgBitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//						imageValue = bitmapToByteArray(imgBitmap);
//						showSelectedImage(imgBitmap);
//					}
//					catch (FileNotFoundException e){
//						e.printStackTrace();
//					}
//					break;
//			}
//			super.onActivityResult(requestCode, resultCode, data);
//		}
//
//	}
	
}
