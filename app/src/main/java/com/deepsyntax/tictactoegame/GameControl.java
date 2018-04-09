package com.deepsyntax.tictactoegame;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import android.app.AlertDialog.*;

public class GameControl implements OnClickListener{
    private Context mContext;
    private GameBoardAdapter boardAdapter;
    public static Game game;
    private int currentRound = 1;
    private AlertDialog gameOverDialog;
    private int gameStatus;
    private boolean gameOver;
    private boolean hasXplayed;
    private ArrayList<Integer> playedPosition = new ArrayList<>();
    public static int currentPlayer = -1;
    private boolean comPlaying;
    private GridView gameBoard;
    private int numsRound;

    private TextView player1ScoreTv, player2ScoreTv;
    private int player1Score, player2Score;
    private String player1Name,player2Name;

    private ArrayList boxes;
    private MediaPlayer mp;
    ComInterface comInterface;
    private AlertDialog.Builder builder;

    private boolean quickView;

    public GameControl(Context ctx){
        this.mContext = ctx;
        comInterface = (GameControl.ComInterface) mContext;
    }
    public GameControl(Context ctx, GridView board, GameBoardAdapter adapter){
        this.mContext = ctx;
        boardAdapter = adapter;
        gameBoard = board;
        game = new Game(ctx);
        boxes = Board.generateBoxes(9);
        comInterface = (GameControl.ComInterface) mContext;
    }

    public void playSound(int soundID){
        if (mp != null)mp = null;
        mp = MediaPlayer.create(mContext, soundID);
        mp.start();
    }

    public boolean play(int position, View v){
        if (playedPosition.contains(position) || gameOver)
            return false;

        playSound(R.raw.tink);
        playedPosition.add(position);
        displaySymbol(v, position);
        checkGameStatus(position);
        hasXplayed = !hasXplayed;

        return true;
    }


    public void displaySymbol(View v, int position){
        currentPlayer = hasXplayed ? game.getOPlayerSymbol() : game.getXPlayerSymbol();
        if (v != null){
            FrameLayout frame = (FrameLayout) v;
            ImageView gameBox = frame.findViewById(R.id.game_box);
            gameBox.setImageResource(currentPlayer);
        }
        else{
            game.addMove(position, currentPlayer);
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
        }
    }
    public void checkGameStatus(int position){
        gameStatus = game.isGameOver(playedPosition, position, currentPlayer);

        if (gameStatus == 0){
            showWinDialog("DRAW", "Perfect play!\tits a draw!");
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getWinPattern(), game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
            gameOver = true;
        }
        else if (gameStatus == -1){
            return;
        }
        else{
            String winner = gameStatus == game.getXPlayerSymbol() ? player1Name + " wins" : player2Name + " wins";
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getWinPattern(), game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
            gameOver = true;
            addScore();
            showWinDialog("WINNER!", winner);
        }
    }
    public void computerPlay(View v){
        final int position= game.minimax(game.getAllMoves(), game.getOPlayerSymbol());
        if (!gameOver) Toast.makeText(mContext, "Thinking...", 50).show();

        Log.v("MAIN-GAME-ACTIVITY", game.minimax(game.getAllMoves(), game.getOPlayerSymbol()) + "");
        comPlaying = true;
        Handler delay = new Handler(Looper.getMainLooper());
        delay.postDelayed(new Runnable() {

            @Override
            public void run(){
                play(position, null);
                comPlaying = false;
            }

        }, 950L);
    }

    public void restartGame(){
        playedPosition.clear();
        currentPlayer = -1;
        hasXplayed = false;
        gameOver = false;
        game.newGame();
        boardAdapter = new GameBoardAdapter(mContext, boxes);
        gameBoard.setAdapter(boardAdapter);
    }


    public void showWinDialog(String status, String msg){
        builder = new AlertDialog.Builder(mContext);

        if (numsRound > 0 && !(currentRound > numsRound)){
            builder.setTitle("Round " + currentRound + "/" + numsRound);
            currentRound++;

            if (currentRound > numsRound){
                builder.setPositiveButton("Game Over", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2){
                        dialog.dismiss();
                        showGameOverDialog();

                    }
                });
                builder.setNeutralButton("View Board", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2){
                        dialog.dismiss();
                        delayDialog(builder);
                    }
                });
            }
            else{
                builder.setNeutralButton("View Board", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2){
                        dialog.dismiss();
                        delayDialog(builder);
                    }
                });
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2){
                        restartGame();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("End Game", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2){
                        restartGame();
                        dialog.dismiss();
                        comInterface.onEndGame();
                    }
                });
            }
        }
        else{
            builder.setTitle(status);
            builder.setNeutralButton("View Board", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2){
                    dialog.dismiss();
                    delayDialog(builder);
                }
            });
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2){
                    restartGame();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("End Game", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2){
                    restartGame();
                    dialog.dismiss();
                    comInterface.onEndGame();
                }
            });
        }

        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.create().show();
    }

    public void delayDialog(final AlertDialog.Builder builder){
        comInterface.onViewBoard(false);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run(){
                gameBoard.setOnTouchListener(new OnTouchListener(){

                    @Override
                    public boolean onTouch(View v, MotionEvent e){
                        builder.create();
                        builder.show();
                        comInterface.onViewBoard(true);
                        gameBoard.setOnTouchListener(null);
                        quickView = true;
                        return false;
                    }
                });
            }
        }, 500L);

    }

    public void showGameOverDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("GAME OVER");
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.gameover_dialog, null);
        TextView player1DialogName = dialogView.findViewById(R.id.dialog_player1_label);
        TextView player2DialogName = dialogView.findViewById(R.id.dialog_player2_label);
        TextView player1DialogScore = dialogView.findViewById(R.id.dialog_player1_score);
        TextView player2DialogScore = dialogView.findViewById(R.id.dialog_player2_score);
        Button rematchBtn = dialogView.findViewById(R.id.gameover_dialog_rematch);
        Button endGameBtn = dialogView.findViewById(R.id.gameover_dialog_end_game);
        rematchBtn.setOnClickListener(this);
        endGameBtn.setOnClickListener(this);

        player1DialogName.setText(player1Name);
        player2DialogName.setText(player2Name);
        player1DialogScore.setText(String.valueOf(player1Score));
        player2DialogScore.setText(String.valueOf(player2Score));

        builder.setView(dialogView);
        builder.setCancelable(false);
        gameOverDialog = builder.create();
        gameOverDialog.show();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.gameover_dialog_rematch:
                restartGame();
                resetScore();
                currentRound = 1;
                gameOverDialog.dismiss();
                break;
            case R.id.gameover_dialog_end_game:
                comInterface.onEndGame();
                break;
            case R.id.game_board:
                builder.create();
                builder.show();
                comInterface.onViewBoard(true);
                break;
        }
    }
    public void addScore(){
        if (currentPlayer == game.getXPlayerSymbol()){
            player1Score++;
            player1ScoreTv.setText(String.valueOf(player1Score));
        }
        else{
            player2Score++;
            player2ScoreTv.setText(String.valueOf(player2Score));
        }
    }

    public void resetScore(){
        player1Score = 0;
        player2Score = 0;
        player1ScoreTv.setText(String.valueOf(player1Score));
        player2ScoreTv.setText(String.valueOf(player1Score));
    }

    public void setNumberOfRounds(int rounds){
        this.numsRound = rounds;
    }
    public ArrayList<Integer> getPlayedPosition(){
        return playedPosition;
    }
    public boolean isComPlaying(){
        return comPlaying;
    }

    public void setPlayersProps(String player1name, String player2name, TextView player1scoreTv, TextView player2scoreTv){
        this.player1Name = player1name;
        this.player2Name = player2name;
        this.player1ScoreTv = player1scoreTv;
        this.player2ScoreTv = player2scoreTv;
    }

    public interface ComInterface{
        void onViewBoard(boolean isViewing);
        void onEndGame();
    }
}
