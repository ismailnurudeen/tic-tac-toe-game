package com.deepsyntax.tictactoegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class MainGameActivity extends AppCompatActivity implements OnItemClickListener, OnClickListener, GameControl.ComInterface, SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences mainSettingsPrefs;
    private SharedPreferences gamePlayPrefs;
    private SharedPreferences.Editor editor;
    public static final String MAIN_SETTINGS_PREF = "pref_general";
    public static final String IN_GAME_PREF = "pref_gameplay";
    public static String SOUND_PREF_KEY = "click_sound";
    public static String DIFFICULTY_PREF_KEY = "game_difficulty";
    public static String THEME_PREF_KEY = "game_theme";
    public static String BOARD_TYPE_PREF_KEY = "board_type";

    private boolean isSinglePlayer;
    private GridView gameBoard;
    private GameBoardAdapter boardAdapter;
    private TextView player1NameTv, player2NameTv, player1ScoreTv, player2ScoreTv;
    private String player1Name, player2Name;
    private GameControl gameControl;
    private boolean isSmallBoard = true;
    private ImageView restartGameBtn, endGameBtn, zoomInBtn, zoomOutBtn, settingsBtn;
    private CircularImageView player1Image;
    private CircularImageView player2Image;
    private boolean isPlayerTurn;
    private int numsOfBoxes;
    private Board board;
    private boolean soundPref;
    int gameDifficultyPref, gameThemePref, boardTypePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_main);
        mainSettingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mainSettingsPrefs.registerOnSharedPreferenceChangeListener(this);
        initializeViews();

        Bundle extras = getIntent().getExtras();
        isSinglePlayer = extras.getBoolean("SINGLE_PLAYER", true);

        ArrayList<String> nameList = extras.getStringArrayList("PLAYER_NAMES");
        player1Name = isSinglePlayer ? "You" : nameList.get(0);
        player2Name = isSinglePlayer ? "Computer" : nameList.get(1);

        int numsRound = extras.getInt("NUMBER_OF_ROUNDS", 0);
        numsOfBoxes = extras.getInt("NUMBER_OF_GRIDS", 9);
        int player1Symbol = extras.getInt("PLAYER_SYMBOL", R.drawable.x);
        int player2Symbol = player1Symbol == R.drawable.x ? R.drawable.o : R.drawable.x;

        board = new Board(this);
        board.setNumsOfColumns(3);
        board.setBoxArea(80, 80);
        gameBoard.getLayoutParams().width = board.pixelsToDips(80 * 3);
        boardAdapter = new GameBoardAdapter(this, Board.generateBoxes(numsOfBoxes));
        if (numsOfBoxes > 9) {
            gameBoard.setNumColumns(5);
            board.setNumsOfColumns(5);
            board.setBoxArea(50, 50);
            gameBoard.getLayoutParams().width = board.pixelsToDips(50 * 5);
        }
        Game game = new Game(this);
        game.setPlayersSymbol(player1Symbol, player2Symbol);

        gameBoard.setAdapter(boardAdapter);
        gameControl = new GameControl(this, gameBoard, boardAdapter);
        gameControl.setPlayersProps(player1Name, player2Name, player1ScoreTv, player2ScoreTv, player1Image, player2Image);
        gameControl.setNumberOfRounds(numsRound);
        gameControl.resetScore();
        gameControl.showActivePlayer(player1Image);

        initCurrentSettings(mainSettingsPrefs);
        displayPlayerInfo();
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int position, long id) {
        if (isSinglePlayer && gameControl.isComPlaying()) return;
        if (!gameControl.play(position, v)) return;
        if (isSinglePlayer && gameControl.getPlayedPosition().size() < numsOfBoxes)
            gameControl.computerPlay(v);
    }

    @Override
    public void onClick(View v) {
        Board board = new Board(this);
        board.setNumsBoxes(Board.generateBoxes(numsOfBoxes));
        int size = numsOfBoxes > 9 ? 60 : 100;
        gameBoard.setOnTouchListener(null);
        switch (v.getId()) {
            case R.id.end_game_btn:
                endGame();
                break;
            case R.id.restart_game_btn:
                gameControl.restartGame();
                gameControl.resetScore();
                break;
            case R.id.zoom_in_btn:
                board.resizeBoard(gameBoard, size, size);
                break;
            case R.id.zoom_out_btn:
                board.resetBoardSize(gameBoard);
                break;
            case R.id.main_game_settings_btn:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT);
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(SettingsActivity.SETTINGS_TO_SHOW, 1);
                startActivity(settingsIntent);
        }
    }

    private void initializeViews() {
//        Manually binding views using View ID
        player1NameTv = findViewById(R.id.player1_label);
        player2NameTv = findViewById(R.id.player2_label);
        player1ScoreTv = findViewById(R.id.player1_score);
        player2ScoreTv = findViewById(R.id.player2_score);
        player1Image = findViewById(R.id.player1_img);
        player2Image = findViewById(R.id.player2_img);
        restartGameBtn = findViewById(R.id.restart_game_btn);
        endGameBtn = findViewById(R.id.end_game_btn);
        zoomInBtn = findViewById(R.id.zoom_in_btn);
        zoomOutBtn = findViewById(R.id.zoom_out_btn);
        settingsBtn = findViewById(R.id.main_game_settings_btn);
        gameBoard = findViewById(R.id.game_board);


        gameBoard.setOnItemClickListener(this);
        restartGameBtn.setOnClickListener(this);
        endGameBtn.setOnClickListener(this);
        zoomInBtn.setOnClickListener(this);
        zoomOutBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    private void initCurrentSettings(SharedPreferences prefs) {
        soundPref = prefs.getBoolean(SOUND_PREF_KEY, true);
        boardTypePref = prefs.getInt(BOARD_TYPE_PREF_KEY, 0);
        gameThemePref = prefs.getInt(THEME_PREF_KEY, 0);
        gameDifficultyPref = prefs.getInt(DIFFICULTY_PREF_KEY, 1);
        gameControl.enableSound(soundPref);
        gameControl.setDifficultyLevel(gameDifficultyPref);
    }

    private void displayPlayerInfo() {
        player1NameTv.setText(player1Name);
        player2NameTv.setText(player2Name);
    }

    public void enableBtns(boolean state) {
        endGameBtn.setEnabled(state);
        restartGameBtn.setEnabled(state);
    }

    public void endGame() {
        gameControl.showGameOverDialog();
    }

    @Override
    public void onViewBoard(boolean isViewing) {

    }

    @Override
    public void onEndGame() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO: Implement this method
        super.onDestroy();
        board.setBoxArea(80, 80);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resize_board:
                board = new Board(this);
                board.setNumsBoxes(Board.generateBoxes(numsOfBoxes));
                int size = numsOfBoxes > 9 ? 50 : 80;
                if (isSmallBoard) {
                    board.resizeBoard(gameBoard, size, size);
                    isSmallBoard = !isSmallBoard;
                } else {
                    board.resetBoardSize(gameBoard);
                    isSmallBoard = !isSmallBoard;
                }
                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(SettingsActivity.SETTINGS_TO_SHOW, "MAINGAMEACTIVITY");
                startActivity(settingsIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    int exitCount = 0;

    @Override
    public void onBackPressed() {
        if (exitCount < 1) {
            Toast.makeText(this, "press back again to exit app", Toast.LENGTH_SHORT).show();
            exitCount++;
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        initCurrentSettings(sharedPreferences);
    }
}

	
