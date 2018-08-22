package com.deepsyntax.tictactoegame;

/**
 * Created by El Nuru on 22/04/2018.
 * Provides custom themes for the game
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Themes {
    private int[] minimalTheme;
    private int[] matrixTheme;
    private int[] girlieTheme;
    private int[] clawsTheme;
    private int[][] allThemes;
    private Context mContext;

    private Game game;
    private static int player1Symbol = R.drawable.x;

    Themes(Context ctx) {
        mContext = ctx;
        minimalTheme = new int[]{R.drawable.x, R.drawable.o, Color.WHITE, Color.parseColor("#D32F2F"), R.drawable.color_wood};
        clawsTheme = new int[]{R.drawable.claws_x, R.drawable.claws_o, R.drawable.claws_bg, mContext.getResources().getColor(R.color.colorAccent), R.drawable.dark_texture_background};
        girlieTheme = new int[]{R.drawable.girlie_x, R.drawable.girlie_o, Color.WHITE, Color.parseColor("#FF4081"), R.drawable.girlie_bg_1};
        matrixTheme = new int[]{R.drawable.matrix_x, R.drawable.matrix_o, Color.parseColor("#BDBDBD"), Color.parseColor("#43A047"), R.drawable.matrix_bg};
        allThemes = new int[][]{minimalTheme, clawsTheme, girlieTheme, matrixTheme};
        game = new Game(ctx);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setNewTheme(FrameLayout lines, ImageView boardBackground, int themeIndex) {
        switch (themeIndex) {
            case 0:
                setGameSymbol(minimalTheme[0], minimalTheme[1]);
                boardBackground.setBackgroundColor((minimalTheme[2]));
                lines.setBackgroundColor(minimalTheme[3]);
                MainGameActivity.MAIN_GAME_BG.setBackgroundResource(minimalTheme[4]);
                MainGameActivity.MAIN_SCORE_BOARD.setBackgroundResource(R.drawable.minimal_score_board);
                MainGameActivity.APP_WINDOW.setStatusBarColor(mContext.getResources().getColor(R.color.minimalStatusBarColor));
                break;
            case 1:
                setGameSymbol(clawsTheme[0], clawsTheme[1]);
                boardBackground.setBackgroundResource((clawsTheme[2]));
                lines.setBackgroundColor(clawsTheme[3]);
                MainGameActivity.MAIN_GAME_BG.setBackgroundResource(clawsTheme[4]);
                MainGameActivity.MAIN_SCORE_BOARD.setBackgroundResource(R.drawable.ic_launcher_background);
                MainGameActivity.APP_WINDOW.setStatusBarColor(mContext.getResources().getColor(R.color.clawsStatusBarColor));
                break;
            case 2:
                setGameSymbol(girlieTheme[0], girlieTheme[1]);
                boardBackground.setBackgroundColor(girlieTheme[2]);
                lines.setBackgroundColor(girlieTheme[3]);
                MainGameActivity.MAIN_SCORE_BOARD.setBackgroundResource(R.drawable.girlie_score_board);
                MainGameActivity.MAIN_GAME_BG.setBackgroundResource(girlieTheme[4]);
                MainGameActivity.APP_WINDOW.setStatusBarColor(mContext.getResources().getColor(R.color.girlieStatusBarColor));
                break;
            case 3:
                setGameSymbol(matrixTheme[0], matrixTheme[1]);
                boardBackground.setBackgroundColor(matrixTheme[2]);
                lines.setBackgroundColor(matrixTheme[3]);
                MainGameActivity.MAIN_SCORE_BOARD.setBackgroundResource(R.drawable.matrix_score_board);
                MainGameActivity.MAIN_GAME_BG.setBackgroundResource(matrixTheme[4]);
                MainGameActivity.APP_WINDOW.setStatusBarColor(mContext.getResources().getColor(R.color.matrixStatusBarColor));
                break;
        }
    }

    void setInitialSettingsIcons(ImageView xIcon, ImageView oIcon, int index) {
        xIcon.setImageResource(allThemes[index][0]);
        oIcon.setImageResource(allThemes[index][1]);
        new Game(mContext).setPlayersSymbol(allThemes[index][0], allThemes[index][1]);
    }

    int[] getCurrentThemeSymbols(int i) {
        return new int[]{allThemes[i][0], allThemes[i][1]};
    }

    public void setPlayer1Symbol(int symbol) {
        player1Symbol = symbol;
    }

    public void setGameSymbol(int symbol1, int symbol2) {
        if (player1Symbol == R.drawable.x) {
            game.setPlayersSymbol(symbol1, symbol2);
        } else {
            game.setPlayersSymbol(symbol2, symbol1);
        }
    }
}
