package com.deepsyntax.tictactoegame;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayersInfoFragment extends Fragment implements OnClickListener{

	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	EditText nameEditText;
	ArrayList<String> playersName=new ArrayList<>();
	ArrayList<Byte> playersImage=new ArrayList<>();
	int counter=0;
	private ImageView playerImagePreview;
	private Button uploadImageBtn;


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
		Button nextBtn=view.findViewById(R.id.fragment_playerinfo_next_btn);
		nameEditText = view.findViewById(R.id.fragment_player_name_et);
		playerImagePreview = view.findViewById(R.id.fragmentplayerImageView);
		uploadImageBtn = view.findViewById(R.id.fragment_player_upload_image_btn);
        nextBtn.setOnClickListener(this);
       uploadImageBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
        FragmentsInterface fragInterface = (FragmentsInterface) getActivity();
        switch (v.getId()) {
            case R.id.fragment_playerinfo_next_btn:
                //int state=getTag().equals(PlayersInfoActivity.PLAYER_1_TAG) ? 0:1;
                if (nameEditText.getText().toString().length() < 1) {
					Toast.makeText(getActivity(),"Input a name...",Toast.LENGTH_SHORT).show();
					return;
				}

                String playerName = nameEditText.getText().toString();
                playersName.add(playerName);
                //playersImage.add()
                resetFields();
                counter++;
                if (counter == 2) fragInterface.OnPlayerSettingsComplete(playersName);
                break;
            case R.id.fragment_player_upload_image_btn:
                fragInterface.OnUploadImage();
                break;
        }
    }
	private void resetFields(){
		nameEditText.setText("");
		nameEditText.setHint("Player 2");
	}

    private void showSelectedImage(Bitmap imgBitmap){
        // TODO: Implement this method
        playerImagePreview.setImageBitmap(imgBitmap);
        playerImagePreview.setVisibility(View.VISIBLE);
    }
}
