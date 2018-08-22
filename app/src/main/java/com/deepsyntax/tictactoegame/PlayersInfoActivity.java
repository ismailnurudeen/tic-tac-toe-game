package com.deepsyntax.tictactoegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PlayersInfoActivity extends AppCompatActivity implements FragmentsInterface {
    public static String PLAYER_1_TAG = "Players One";
    public static String PLAYER_2_TAG = "Players Two";
    public static final int UPLOAD_IMAGE_REQUEST = 1;
    public static String PREF_NAME = "USER_INFO";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    FragmentManager manager;
    FragmentTransaction transaction;
    Bundle dataBundle;

    private Intent in;
    private byte[] imageValue;
    private int currentPlayerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_info);
        dataBundle = new Bundle();
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        transaction.replace(R.id.activity_players_infoFrameLayout, new PlayersInfoFragment(), PLAYER_1_TAG);
        transaction.commit();
        in = new Intent(this, MainGameActivity.class);
    }

    @Override
    public void OnPlayerSettingsComplete(ArrayList<String> playerNames) {
        InitialSettingsFragment initialSettings = new InitialSettingsFragment();
        initialSettings.newInstance(playerNames);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .replace(R.id.activity_players_infoFrameLayout, initialSettings, "InitialSettings")
                .commit();
    }

    @Override
    public void OnGameSettingsComplete(ArrayList<String> playerNames, int theme, int playerSymbol, int numBoardGrid, int rounds) {
        in.putExtra("SINGLE_PLAYER", false);
        in.putExtra("NUMBER_OF_ROUNDS", rounds);
        in.putStringArrayListExtra("PLAYER_NAMES", playerNames);
        in.putExtra("PLAYER_SYMBOL", playerSymbol);
        in.putExtra("NUMBER_OF_GRIDS", numBoardGrid);
        in.putExtra("THEME_CHOICE",theme);
        startActivity(in);
        finish();
    }

    @Override
    public void OnUploadImage(int playerIndex) {
        // TODO: Implement this method
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.setType("image/*");
        startActivityForResult(getImageIntent.createChooser(getImageIntent, "Select an Image"), UPLOAD_IMAGE_REQUEST);
        currentPlayerSettings = playerIndex;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: Implement this method
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPLOAD_IMAGE_REQUEST:
                    Uri imageUri = data.getData();
                    try {
                        Bitmap imgBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        if (currentPlayerSettings == 0) {
                            Players.setPlayer1Image(imgBitmap);
                        } else {
                            Players.setPlayer2Image(imgBitmap);
                        }
                        PlayersInfoFragment.showSelectedImage(imgBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
