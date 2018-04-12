package com.deepsyntax.tictactoegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class MainGameActivity extends AppCompatActivity implements OnItemClickListener, OnClickListener, GameControl.ComInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private boolean isSinglePlayer;
    private GridView gameBoard;
    private GameBoardAdapter boardAdapter;
    private TextView player1NameTv, player2NameTv, player1ScoreTv, player2ScoreTv;
    private String player1Name, player2Name;
    private GameControl gameControl;
    private boolean isSmallBoard = true;
    private Button restartGameBtn, endGameBtn;
    private CircularImageView player1Image;
    private CircularImageView player2Image;
    private boolean isPlayerTurn;
    private int numsOfBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        initializeViews();

        Bundle extras = getIntent().getExtras();
        isSinglePlayer = extras.getBoolean("SINGLE_PLAYER", true);

        ArrayList<String> nameList = extras.getStringArrayList("PLAYER_NAMES");
        player1Name = isSinglePlayer ? "You" : nameList.get(0);
        player2Name = isSinglePlayer ? "Computer" : nameList.get(1);
        displayPlayerInfo();

        int numsRound = extras.getInt("NUMBER_OF_ROUNDS", 0);
        numsOfBoxes = extras.getInt("NUMBER_OF_GRIDS", 9);
        int player1Symbol = extras.getInt("PLAYER_SYMBOL", R.drawable.x);
        int player2Symbol = player1Symbol == R.drawable.x ? R.drawable.o : R.drawable.x;
        Game game = new Game(this);
        game.setPlayersSymbol(player1Symbol, player2Symbol);

        boardAdapter = new GameBoardAdapter(this, Board.generateBoxes(numsOfBoxes));
        if (numsOfBoxes > 9) {
            gameBoard.setNumColumns(5);
            boardAdapter.setBoxArea(65, 65);
        }

        gameBoard.setAdapter(boardAdapter);
        gameControl = new GameControl(this, gameBoard, boardAdapter);
        gameControl.setPlayersProps(player1Name, player2Name, player1ScoreTv, player2ScoreTv, player1Image, player2Image);
        gameControl.setNumberOfRounds(numsRound);
        gameControl.resetScore();
        gameControl.showActivePlayer(player1Image);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int position, long id) {
        if (isSinglePlayer && gameControl.isComPlaying()) return;
        if (!gameControl.play(position, v)) return;
        if (isSinglePlayer && gameControl.getPlayedPosition().size() < 9)
            gameControl.computerPlay(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_game_btn:
                endGame();
                break;
            case R.id.restart_game_btn:
                gameControl.restartGame();
                gameControl.resetScore();
                break;
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
        gameBoard = findViewById(R.id.game_board);


        gameBoard.setOnItemClickListener(this);
        restartGameBtn.setOnClickListener(this);
        endGameBtn.setOnClickListener(this);
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
        enableBtns(isViewing);
    }

    @Override
    public void onEndGame() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO: Implement this method
        super.onDestroy();
        new Board(this).resetBoardSize(gameBoard);
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
                Board board = new Board(this);
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

}

	
