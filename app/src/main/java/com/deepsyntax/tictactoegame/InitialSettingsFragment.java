package com.deepsyntax.tictactoegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

public class InitialSettingsFragment extends Fragment implements OnClickListener {

    private ArrayList<String> playerNames;
    int playerSymbol = R.drawable.x;
    private CheckBox defautSettingsCheck;
    private LinearLayout secondarySettingsLayout;
    private Spinner boardTypeSpinner;
    private NumberPicker roundsPicker;
    private Spinner themesSpinner;
    private ImageView symbolPicker_O, symbolPicker_X;
    private Themes themes;

    public InitialSettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_initial_game_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button nextBtn = view.findViewById(R.id.fragment_initial_game_settings_next_btn);
        defautSettingsCheck = view.findViewById(R.id.fragment_initial_game_settings_defaut_settings_check);
        nextBtn.setOnClickListener(this);
        defautSettingsCheck.setOnClickListener(this);
        secondarySettingsLayout = view.findViewById(R.id.secondary_settings_layout);
        boardTypeSpinner = view.findViewById(R.id.fragment_initial_game_settings_board_type);
        themesSpinner = view.findViewById(R.id.initial_game_settings_theme);

        roundsPicker = view.findViewById(R.id.initial_game_settings_rounds);
        roundsPicker.setMaxValue(100);
        roundsPicker.setMinValue(1);
        roundsPicker.setWrapSelectorWheel(false);
        symbolPicker_O = view.findViewById(R.id.initial_o_symbol);
        symbolPicker_X = view.findViewById(R.id.initial_x_symbol);
        themes = new Themes(getContext());
        RadioGroup symbolPicker = view.findViewById(R.id.symbol_picker);
        symbolPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.x_selected) {
                    playerSymbol = R.drawable.x;
                } else {
                    playerSymbol = R.drawable.o;
                }
            }
        });
        themesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                themes.setInitialSettingsIcons(symbolPicker_X, symbolPicker_O, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_initial_game_settings_next_btn:
                FragmentsInterface fragInterface = (FragmentsInterface) getActivity();
                int rounds = roundsPicker.getValue();
                int boardType = boardTypeSpinner.getSelectedItemPosition() == 0 ? 9 : 25;
                int theme = themesSpinner.getSelectedItemPosition();

//                Passing settings value to the host activity <@PlayerInfoActivity>
                if (defautSettingsCheck.isChecked()) {
                    fragInterface.OnGameSettingsComplete(playerNames, -1, R.drawable.x, -1, rounds);
                } else {
                    fragInterface.OnGameSettingsComplete(playerNames, theme, playerSymbol, boardType, rounds);
                }
                break;
            case R.id.fragment_initial_game_settings_defaut_settings_check:
                if (defautSettingsCheck.isChecked()) {
                    secondarySettingsLayout.setVisibility(View.GONE);
                } else {
                    secondarySettingsLayout.setVisibility(View.VISIBLE);
                }
        }
    }

    public void newInstance(ArrayList<String> names) {
        this.playerNames = names;
    }
}
