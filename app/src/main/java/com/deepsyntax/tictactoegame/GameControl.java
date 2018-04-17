package com.deepsyntax.tictactoegame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GameControl implements OnClickListener {
    private Context mContext;
    private GameBoardAdapter boardAdapter;
    public static Game game;
    public static int currentPlayer = -1;
    private int currentRound = 1;
    private AlertDialog gameOverDialog;
    private int gameStatus;
    private boolean gameOver;
    private boolean hasXplayed;
    private boolean comPlaying;
    private ArrayList<Integer> playedPosition = new ArrayList<>();
    private GridView gameBoard;
    private int numsRound;

    private CircularImageView player1ImageView, player2ImageView;
    private TextView player1ScoreTv, player2ScoreTv;
    private int player1Score, player2Score;
    private String player1Name, player2Name;

    private ArrayList boxes = Board.generateBoxes(9);
    private MediaPlayer mp;
    ComInterface comInterface;
    private AlertDialog.Builder builder;

    private boolean quickView;
    private int position;

    public GameControl(Context ctx) {
        this.mContext = ctx;
        comInterface = (GameControl.ComInterface) mContext;
    }

    public GameControl(Context ctx, GridView board, GameBoardAdapter adapter) {
        this.mContext = ctx;
        boardAdapter = adapter;
        gameBoard = board;
        boxes = Board.generateBoxes((int) Math.pow(Board.numOfColumns, 2));
        game = new Game(ctx, Board.numOfColumns);
        comInterface = (GameControl.ComInterface) mContext;
        Log.v("GAME CONTROL", Board.numOfColumns + "");
    }

    public void instancaite() {
        game = new Game(mContext, 1);
    }

    public void playSound(int soundID) {
        if (mp != null) mp.release();
        mp = MediaPlayer.create(mContext, soundID);
        mp.start();
    }

    public boolean play(int position, View v) {
        if (playedPosition.contains(position) || gameOver)
            return false;
        playSound(R.raw.tink);
        playedPosition.add(position);
        displaySymbol(v, position);
        checkGameStatus(position);
        changeTurn();
        return true;
    }


    public void displaySymbol(View v, int position) {
        currentPlayer = hasXplayed ? game.getOPlayerSymbol() : game.getXPlayerSymbol();
        if (v != null) {
            FrameLayout frame = (FrameLayout) v;
            ImageView gameBox = frame.findViewById(R.id.game_box);
            gameBox.setImageResource(currentPlayer);
        } else {
            game.addMove(position, currentPlayer);
//            boxes = Board.generateBoxes((int) Math.pow(gameBoard.getNumColumns(), 2));
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
        }
    }

    public void checkGameStatus(int position) {
        gameStatus = game.isGameOver(playedPosition, position, currentPlayer);

        if (gameStatus == 0) {
            showWinDialog("DRAW", "Perfect play!\tits a draw!");
            boxes = Board.generateBoxes((int) Math.pow(gameBoard.getNumColumns(), 2));
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getWinPattern(), game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
            gameOver = true;
        } else if (gameStatus == -1) {
            return;
        } else {
            String winner = gameStatus == game.getXPlayerSymbol() ? player1Name + " wins" : player2Name + " wins";
            boardAdapter = new GameBoardAdapter(mContext, boxes, currentPlayer, game.getWinPattern(), game.getAllMoves());
            gameBoard.setAdapter(boardAdapter);
            gameOver = true;
            addScore();
            showWinDialog("WINNER!", winner);
        }
    }

    @SuppressLint("WrongConstant")
    public void computerPlay(View v) {
        if (Board.numOfColumns > 3) {
            position = game.machinePlayer();
                    //game.minimax(game.getAllMoves(), game.getOPlayerSymbol());
        } else {
            position = game.myAi();
        }
        Toast.makeText(mContext, "Thinking...", 10).show();

        comPlaying = true;
        Handler delay = new Handler(Looper.getMainLooper());
        delay.postDelayed(new Runnable() {

            @Override
            public void run() {
                play(position, null);
                comPlaying = false;
            }

        }, 950L);
    }

    public void changeTurn() {
        hasXplayed = !hasXplayed;
        player1ImageView.setBorderColor(Color.DKGRAY);
        player2ImageView.setBorderColor(Color.DKGRAY);

        if (hasXplayed) {
            showActivePlayer(player2ImageView);
        } else {
            showActivePlayer(player1ImageView);
        }
    }

    public void showActivePlayer(CircularImageView imageView) {
        int activeColor = mContext.getResources().getColor(R.color.colorAccent);
        imageView.addShadow();
        imageView.setBorderColor(activeColor);
    }

    public void restartGame() {
        playedPosition.clear();
        currentPlayer = -1;
        hasXplayed = false;
        gameOver = false;
        game.newGame();
        boxes = Board.generateBoxes((int) Math.pow(gameBoard.getNumColumns(), 2));
        boardAdapter = new GameBoardAdapter(mContext, boxes);
        gameBoard.setAdapter(boardAdapter);

        player1ImageView.setBorderColor(Color.DKGRAY);
        player2ImageView.setBorderColor(Color.DKGRAY);
        showActivePlayer(player1ImageView);
    }

    public void showSweetWinDialog(String status, String msg) {
        final SweetAlertDialog sweetDialog = new SweetAlertDialog(mContext, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweetDialog.setCustomImage(currentPlayer);

        builder = new AlertDialog.Builder(mContext);

        if (numsRound > 0 && !(currentRound > numsRound)) {
            sweetDialog.setTitleText("Round " + currentRound + "/" + numsRound);
            builder.setTitle("Round " + currentRound + "/" + numsRound);
            currentRound++;

            if (currentRound > numsRound) {
                sweetDialog.setConfirmText("Game Over")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetDialog.dismiss();
                                showGameOverDialog();
                            }
                        });
            } else {
                sweetDialog.setConfirmText("Continue")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                restartGame();
                                sweetDialog.dismiss();
                            }
                        });
                sweetDialog.setCancelText("View Board")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetDialog.dismiss();
                                hideSweetDialog(sweetDialog);
                            }
                        });
            }
        } else {
            sweetDialog.setTitleText(status);
            sweetDialog.setConfirmText("Continue")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            restartGame();
                            sweetDialog.dismiss();
                        }
                    });
            sweetDialog.setCancelText("View Board")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetDialog.dismiss();
                            hideSweetDialog(sweetDialog);
                        }
                    });
        }
        sweetDialog.setContentText(msg)
                .setCancelable(false);
        sweetDialog.show();
    }

    public void hideSweetDialog(final SweetAlertDialog dialog) {
        comInterface.onViewBoard(false);
        gameBoard.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                dialog.show();
                comInterface.onViewBoard(true);
                gameBoard.setOnTouchListener(null);
                return false;
            }
        });
    }

    public void showWinDialog(String status, String msg) {
        //Snackbar.make(gameBoard, "Round " + currentRound + "/" + numsRound + " " + msg, Snackbar.LENGTH_INDEFINITE).setAction("Continue", new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        }).show();

        builder = new AlertDialog.Builder(mContext);

        if (numsRound > 0 && !(currentRound > numsRound)) {
            builder.setTitle("Round " + currentRound + "/" + numsRound);
            currentRound++;

            if (currentRound > numsRound) {
                builder.setPositiveButton("Game Over", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2) {
                        dialog.dismiss();
                        showGameOverDialog();

                    }
                });
                builder.setNegativeButton("View Board", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2) {
                        dialog.dismiss();
                        delayDialog(builder);
                    }
                });
            } else {
                builder.setNegativeButton("View Board", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2) {
                        dialog.dismiss();
                        delayDialog(builder);
                    }
                });
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2) {
                        restartGame();
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("End Game", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int p2) {
                        restartGame();
                        dialog.dismiss();
                        showGameOverDialog();
                    }
                });
            }
        } else {
            builder.setTitle(status);
            builder.setNegativeButton("View Board", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2) {
                    dialog.dismiss();
                    delayDialog(builder);
                }
            });
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2) {
                    restartGame();
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("End Game", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int p2) {
                    restartGame();
                    dialog.dismiss();
                    showGameOverDialog();
                }
            });
        }

        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.create().show();
    }

    public void delayDialog(final AlertDialog.Builder builder) {
        comInterface.onViewBoard(false);
        gameBoard.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                builder.create();
                builder.show();
                comInterface.onViewBoard(true);
                gameBoard.setOnTouchListener(null);
                quickView = true;
                return false;
            }
        });
    }

    public void showGameOverDialog() {
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
    public void onClick(View v) {
        switch (v.getId()) {
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

    public void addScore() {
        if (currentPlayer == game.getXPlayerSymbol()) {
            player1Score++;
            player1ScoreTv.setText(String.valueOf(player1Score));
        } else {
            player2Score++;
            player2ScoreTv.setText(String.valueOf(player2Score));
        }
    }

    public void resetScore() {
        player1Score = 0;
        player2Score = 0;
        player1ScoreTv.setText(String.valueOf(player1Score));
        player2ScoreTv.setText(String.valueOf(player1Score));
    }

    public void setNumberOfRounds(int rounds) {
        this.numsRound = rounds;
    }

    public ArrayList<Integer> getPlayedPosition() {
        return playedPosition;
    }

    public boolean isComPlaying() {
        return comPlaying;
    }

    public void setPlayersProps(String player1name, String player2name, TextView player1scoreTv, TextView player2scoreTv,
                                CircularImageView player1Img, CircularImageView player2Img) {
        this.player1Name = player1name;
        this.player2Name = player2name;
        this.player1ScoreTv = player1scoreTv;
        this.player2ScoreTv = player2scoreTv;
        this.player1ImageView = player1Img;
        this.player2ImageView = player2Img;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public interface ComInterface {
        void onViewBoard(boolean isViewing);

        void onEndGame();
    }
}
