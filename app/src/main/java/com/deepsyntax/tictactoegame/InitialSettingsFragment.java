package com.deepsyntax.tictactoegame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class InitialSettingsFragment extends Fragment implements OnClickListener {

    private EditText roundEditText;
    private ArrayList<String> playerNames;
    int playerSymbol;

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
        view.findViewById(R.id.fragment_initial_game_settings_next_btn)
                .setOnClickListener(this);
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
        FragmentsInterface fragInterface = (FragmentsInterface) getActivity();
        String rounds = roundEditText.getText().toString();
        if (rounds.length() < 1) {
            Toast.makeText(getActivity(), "Indicate Number of rounds", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<byte[]> imageList = new ArrayList();
        fragInterface.OnGameSettingsComplete(playerNames, imageList,playerSymbol,Integer.parseInt(rounds));
    }

    public void newInstance(ArrayList<String> names) {
        this.playerNames = names;
    }
}
