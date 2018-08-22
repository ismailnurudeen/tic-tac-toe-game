package com.deepsyntax.tictactoegame;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayersInfoFragment extends Fragment implements OnClickListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText nameEditText;
    ArrayList<String> playersName = new ArrayList<>();
    ArrayList<Byte> playersImage = new ArrayList<>();
    int counter = 0;
    private static ImageView playerImagePreview;
    private Button uploadImageBtn;
    private TextView currentPlayerSettingsLabel;
    private View mainView;
    private Animation slideIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getActionBar() != null)
            getActivity().getActionBar().setTitle(this.getTag());

        prefs = getActivity().getSharedPreferences("USER_INFO", getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in);
        slideIn.setDuration(300);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        Button nextBtn = view.findViewById(R.id.fragment_playerinfo_next_btn);
        nameEditText = view.findViewById(R.id.fragment_player_name_et);
        playerImagePreview = view.findViewById(R.id.fragmentplayerImageView);
        uploadImageBtn = view.findViewById(R.id.fragment_player_upload_image_btn);
        currentPlayerSettingsLabel = view.findViewById(R.id.initial_settings_current_player);
        nextBtn.setOnClickListener(this);
        uploadImageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentsInterface fragInterface = (FragmentsInterface) getActivity();
        switch (v.getId()) {
            case R.id.fragment_playerinfo_next_btn:
                if (nameEditText.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), "Name must not be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String playerName = nameEditText.getText().toString();
                playersName.add(playerName);
                resetFields();
                counter++;
                if (counter == 2) fragInterface.OnPlayerSettingsComplete(playersName);
                break;
            case R.id.fragment_player_upload_image_btn:
                fragInterface.OnUploadImage(counter);
                break;
        }
    }

    private void resetFields() {
        mainView.setAnimation(slideIn);
        nameEditText.setText("");
        nameEditText.setHint("(Players 2 name)");
        currentPlayerSettingsLabel.setText("Players Two");
        playerImagePreview.setImageResource(R.drawable.no_image);
    }

    public static void showSelectedImage(Bitmap imgBitmap) {
        // TODO: Implement this method
        playerImagePreview.setImageBitmap(imgBitmap);
        playerImagePreview.setVisibility(View.VISIBLE);
    }
}
