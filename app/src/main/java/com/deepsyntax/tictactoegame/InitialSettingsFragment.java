package com.deepsyntax.tictactoegame;
import android.app.*;
import android.view.*;
import android.os.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;

public class InitialSettingsFragment extends Fragment implements OnClickListener
{

	private EditText roundEditText;
	private ArrayList<String> playerNames;
	
	public InitialSettingsFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view=inflater.inflate(R.layout.fragment_initial_game_settings,container,false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.fragment_initial_game_settings_next_btn)
		.setOnClickListener(this);
		roundEditText=(EditText)view.findViewById(R.id.initial_game_settings_rounds);
		
	}

	@Override
	public void onClick(View v){
		FragmentsInterface fragInterface=(FragmentsInterface) getActivity();
		String rounds=roundEditText.getText().toString();
		fragInterface.OnGameSettingsComplete(playerNames,Integer.parseInt(rounds));
	}

	public void newInstance(ArrayList<String> names){
		this.playerNames=names;
	}
}
