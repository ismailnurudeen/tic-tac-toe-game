package com.deepsyntax.tictactoegame;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;

public class PlayersInfoFragment extends Fragment implements OnClickListener{

	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	EditText nameEditText;
	ArrayList<String> playersName=new ArrayList<>();
	ArrayList<Byte> playersImage=new ArrayList<>();
	int counter=0;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getActivity().getActionBar()!= null) getActivity().getActionBar().setTitle(this.getTag());

		prefs = getActivity().getSharedPreferences("USER_INFO", getActivity().MODE_PRIVATE);
		editor = prefs.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_players_info, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.fragment_playerinfo_next_btn)
			.setOnClickListener(this);
		nameEditText = view.findViewById(R.id.fragment_player_name_et);
		//Toast.makeText(getContext(), getTag(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v){
		FragmentsInterface fragInterface=(FragmentsInterface) getActivity();
		//int state=getTag().equals(PlayersInfoActivity.PLAYER_1_TAG) ? 0:1;
		if (nameEditText.getText().toString().length() < 1)return;

		String playerName=nameEditText.getText().toString();
		playersName.add(playerName);
		resetFields();
		counter++;
		
		if (counter == 2) fragInterface.OnPlayerSettingsComplete(playersName);
	}
	private void resetFields(){
		nameEditText.setText("");
		nameEditText.setHint("Player 2 Name");
	}
}
