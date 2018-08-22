package com.deepsyntax.tictactoegame;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.regex.Pattern;


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
    public static ConstraintLayout MAIN_GAME_BG;
    public static ConstraintLayout MAIN_SCORE_BOARD;

    private boolean isSinglePlayer;
    private GridView gameBoard;
    private GameBoardAdapter boardAdapter;
    private TextView player1NameTv, player2NameTv, player1ScoreTv, player2ScoreTv;
    private String player1Name, player2Name;
    private GameControl gameControl;
    private boolean isSmallBoard = true;
    private ImageView restartGameBtn, endGameBtn, zoomInBtn, zoomOutBtn, settingsBtn;
    private CircularImageView player1ImageView, player2ImageView;
    private boolean isPlayerTurn;
    private int numsOfBoxes;
    private Board board;
    private boolean soundPref;
    int gameDifficultyPref, gameThemePref, boardTypePref;
    private ImageView helpBtn;
    private Game game;
    private AlertDialog gameOverDialog;
    private int symbol;
    private int player1Symbol, player2Symbol;
    public static Window APP_WINDOW;
    private Themes themes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        APP_WINDOW = getWindow();
        APP_WINDOW.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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
        int numsGrid = extras.getInt("NUMBER_OF_GRIDS", -1);
        numsOfBoxes = numsGrid != -1 ? numsGrid : Integer.parseInt(mainSettingsPrefs.getString(BOARD_TYPE_PREF_KEY, "9"));

        symbol = extras.getInt("PLAYER_SYMBOL", R.drawable.x);
        themes = new Themes(this);

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
        game = new Game(this);
        gameBoard.setAdapter(boardAdapter);
        gameControl = new GameControl(this, gameBoard, boardAdapter);
        gameControl.setPlayersProps(player1Name, player2Name, player1ScoreTv, player2ScoreTv, player1ImageView, player2ImageView);
        gameControl.setNumberOfRounds(numsRound);
        gameControl.resetScore();
        gameControl.showActivePlayer(player1ImageView);
        gameControl.isSinglePlayer(isSinglePlayer);

        initCurrentSettings(mainSettingsPrefs, true);

        int themeIndex = extras.getInt("THEME_CHOICE", -1);
        if (themeIndex != -1) {
            themes.setPlayer1Symbol(symbol);
            boardAdapter.setThemeIndex(themeIndex);
            gameBoard.setAdapter(boardAdapter);
            player1Symbol = symbol == R.drawable.x ? game.getXPlayerSymbol() : game.getOPlayerSymbol();
            player2Symbol = symbol == R.drawable.x ? game.getOPlayerSymbol() : game.getXPlayerSymbol();
            displayPlayerInfo();
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ;

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
            case R.id.main_game_help_btn:
                View dialogView = LayoutInflater.from(this).inflate(R.layout.dailog_help, null);
                builder.setView(dialogView);
                builder.setIcon(R.drawable.ic_help_outline_black_24dp);
                builder.setCancelable(true);
                dialogView.findViewById(R.id.help_dailog_ok).setOnClickListener(this);
                TextView wikipediaLink = dialogView.findViewById(R.id.learn_more_tictactoe);
                Pattern pattern = Pattern.compile("\\bAndroid+\\b");
                Linkify.addLinks(wikipediaLink, pattern, "http://en.wikipedia.org/tic-tac-toe");
                gameOverDialog = builder.create();
                gameOverDialog.show();
                break;
            case R.id.help_dailog_ok:
                gameOverDialog.dismiss();
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
        MAIN_SCORE_BOARD = findViewById(R.id.main_score_board);
        MAIN_GAME_BG = findViewById(R.id.main_game_bg);
        player1NameTv = findViewById(R.id.player1_label);
        player2NameTv = findViewById(R.id.player2_label);
        player1ScoreTv = findViewById(R.id.player1_score);
        player2ScoreTv = findViewById(R.id.player2_score);
        player1ImageView = findViewById(R.id.player1_img);
        player2ImageView = findViewById(R.id.player2_img);
        restartGameBtn = findViewById(R.id.restart_game_btn);
        endGameBtn = findViewById(R.id.end_game_btn);
        zoomInBtn = findViewById(R.id.zoom_in_btn);
        zoomOutBtn = findViewById(R.id.zoom_out_btn);
        settingsBtn = findViewById(R.id.main_game_settings_btn);
        helpBtn = findViewById(R.id.main_game_help_btn);
        gameBoard = findViewById(R.id.game_board);


        gameBoard.setOnItemClickListener(this);
        restartGameBtn.setOnClickListener(this);
        endGameBtn.setOnClickListener(this);
        zoomInBtn.setOnClickListener(this);
        zoomOutBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
        game = new Game(this);
    }

    private void initCurrentSettings(SharedPreferences prefs, boolean themeChanged) {
        soundPref = prefs.getBoolean(SOUND_PREF_KEY, true);
        gameThemePref = Integer.parseInt(prefs.getString(THEME_PREF_KEY, "0"));
        gameDifficultyPref = Integer.parseInt(prefs.getString(DIFFICULTY_PREF_KEY, "1"));

        gameControl.enableSound(soundPref);
        gameControl.setDifficultyLevel(gameDifficultyPref);
        if (themeChanged) {
            boardAdapter.setThemeIndex(gameThemePref);
            gameBoard.setAdapter(boardAdapter);
            int[] themeSymbols = themes.getCurrentThemeSymbols(gameThemePref);
            player1Symbol = symbol == R.drawable.x ? themeSymbols[0] : themeSymbols[1];
            player2Symbol = symbol == R.drawable.x ? themeSymbols[1] : themeSymbols[0];
            displayPlayerInfo();
        }
    }

    private void displayPlayerInfo() {
        player1NameTv.setText(player1Name);
        player2NameTv.setText(player2Name);

        if (Players.getPlayer1Image() != null) {
            player1ImageView.setImageBitmap(Players.getPlayer1Image());
        } else {
            player1ImageView.setImageResource(player1Symbol);
        }
        if (Players.getPlayer2Image() != null) {
            player2ImageView.setImageBitmap(Players.getPlayer2Image());
        } else {
            player2ImageView.setImageResource(player2Symbol);
        }
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO: Implement this method
        super.onDestroy();
        board.setBoxArea(80, 80);
        Players.resetDefaults();
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
            Toast.makeText(this, "press back again to exit current game", Toast.LENGTH_SHORT).show();
            exitCount++;
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(THEME_PREF_KEY)) {
            gameControl.restartGame();
            initCurrentSettings(sharedPreferences, true);
        } else {
            initCurrentSettings(sharedPreferences, false);
            boardAdapter = new GameBoardAdapter(this, Board.generateBoxes(numsOfBoxes), GameControl.currentPlayer, GameControl.game.getAllMoves());
            boardAdapter.redisplay(true);
        }

    }
}

	
