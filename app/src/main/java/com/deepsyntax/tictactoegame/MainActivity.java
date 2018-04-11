package com.deepsyntax.tictactoegame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentsInterface {
    private MainMenuFragment mainMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if(getSupportActionBar()!=null)getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mainMenuFragment = new MainMenuFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_menu_fragment, mainMenuFragment, "Main Menu")
                .commit();
    }

    @Override
    public void OnPlayerSettingsComplete(ArrayList<String> playerNames) {

    }

    public void handleClicks(View v) {
        switch (v.getId()) {
            case R.id.single_player_btn:
                InitialSettingsFragment initialSettings = new InitialSettingsFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_menu_fragment, initialSettings, "InitialSettings")
                        .commit();
                break;
            case R.id.multi_player_btn:
                Intent startGameSettings = new Intent(MainActivity.this, PlayersInfoActivity.class);
                startActivity(startGameSettings);
                break;
        }
    }

    @Override
    public void OnGameSettingsComplete(ArrayList<String> playerNames, ArrayList<byte[]> playerImage, int playerSymbol, int rounds) {
        Intent startGame = new Intent(MainActivity.this, MainGameActivity.class);
        startGame.putExtra("SINGLE_PLAYER", true);
        startGame.putExtra("Player_Symbol", playerSymbol);
        startActivity(startGame);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_menu_fragment, mainMenuFragment, "Main Menu")
                            .commit();
    }

    @Override
    public void OnUploadImage() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static class MainMenuFragment extends Fragment {
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
