package com.deepsyntax.tictactoegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentsInterface {
    private MainMenuFragment mainMenuFragment;
    final String MAIN_MENU_TAG = "MainActivity.MAIN_MENU";
    final String INITIAL_SETTINGS_TAG = "MainActivity.INITIAL_SETTINGS";
    private String mCurrentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_menu_fragment, mainMenuFragment, MAIN_MENU_TAG)
                .commit();
        mCurrentTag=MAIN_MENU_TAG;
    }

    @Override
    public void OnPlayerSettingsComplete(ArrayList<String> playerNames) {

    }

    public void handleClicks(View v) {
        switch (v.getId()) {
            case R.id.single_player_btn:
                InitialSettingsFragment initialSettings = new InitialSettingsFragment();
                changeFragment(initialSettings, INITIAL_SETTINGS_TAG);
                break;
            case R.id.multi_player_btn:
                Intent startGameSettings = new Intent(MainActivity.this, PlayersInfoActivity.class);
                startActivity(startGameSettings);
                break;
            case R.id.settings_btn:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(SettingsActivity.SETTINGS_TO_SHOW, "MAINGAMEACTIVITY");
                startActivity(settingsIntent);
                break;
            case R.id.about_btn:
                break;
            case R.id.share_btn:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String msg = "Download this awesome app, MASTER TICTACTOE from the playstore\nI really love the game!";
                shareIntent.setType("application/apk");
                shareIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, "com.deepsyntax.ticactoegame");
                shareIntent.setPackage("cn.xender");
                startActivity(Intent.createChooser(shareIntent, "Share app with..."));
                break;
            case R.id.exit_btn:
                finish();
                break;
        }
    }

    @Override
    public void OnGameSettingsComplete(ArrayList<String> playerNames, ArrayList<byte[]> playerImage, int playerSymbol, int numBoardGrid, int rounds) {
        Intent startGame = new Intent(MainActivity.this, MainGameActivity.class);
        startGame.putExtra("SINGLE_PLAYER", true);
        startGame.putExtra("PLAYER_SYMBOL", playerSymbol);
        startGame.putExtra("NUMBER_OF_GRIDS", numBoardGrid);
        startActivity(startGame);
    }

    @Override
    public void OnUploadImage() {

    }

    int exitCount = 0;

    public void changeFragment(Fragment fragment, String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_menu_fragment);

        if (currentFragment.getClass() == fragment.getClass()) return;

        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .replace(R.id.main_menu_fragment, fragment, tag)
                .commit();
        mCurrentTag = tag;
    }

    @Override
    public void onBackPressed() {
        if (MAIN_MENU_TAG.equals(mCurrentTag)) {
            if (exitCount < 1) {
                Toast.makeText(this, "press back again to exit app", Toast.LENGTH_SHORT).show();
                exitCount++;
                return;
            }
            finish();
        } else {
            changeFragment(mainMenuFragment, MAIN_MENU_TAG);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        changeFragment(mainMenuFragment, MAIN_MENU_TAG);
    }

    public static class MainMenuFragment extends Fragment {
        public MainMenuFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_menu, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

}
