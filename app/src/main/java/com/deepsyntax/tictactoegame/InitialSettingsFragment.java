package com.deepsyntax.tictactoegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InitialSettingsFragment extends Fragment implements OnClickListener {

    private EditText roundEditText;
    private ArrayList<String> playerNames;
    int playerSymbol = R.drawable.x;
    private CheckBox defautSettingsCheck;
    private LinearLayout secondarySettingsLayout;
    private Spinner boardTypeSpinner;

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
        boardTypeSpinner=view.findViewById(R.id.fragment_initial_game_settings_board_type);
        roundEditText = view.findViewById(R.id.initial_game_settings_rounds);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_initial_game_settings_next_btn:
                FragmentsInterface fragInterface = (FragmentsInterface) getActivity();
                String rounds = roundEditText.getText().toString();
                if (rounds.length() < 1) {
                    Toast.makeText(getActivity(), "Indicate Number of rounds", Toast.LENGTH_SHORT).show();
                    return;
                }
                int boardType = boardTypeSpinner.getSelectedItemPosition() == 0 ? 9 : 25;
//                TODO: change this value to the players images
                ArrayList<byte[]> imageList = new ArrayList();

//                Passing settings value to the host activity <@PlayerInfoActivity>
                fragInterface.OnGameSettingsComplete(playerNames, imageList, playerSymbol,boardType,Integer.parseInt(rounds));
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
