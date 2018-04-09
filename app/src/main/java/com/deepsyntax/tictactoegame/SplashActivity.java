package com.deepsyntax.tictactoegame;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import android.view.animation.*;

public class SplashActivity extends AppCompatActivity
{
	private final long SPLASH_DURATION=3500L;
	private TextView ticTv,tacTv,toeTv,letsPlayTv;
	private Animation firstAnim,secondAnim,thirdAnim,fourthAnim;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (getSupportActionBar() != null) getSupportActionBar().hide();

		ticTv = findViewById(R.id.splashTextView_tic);
		tacTv = findViewById(R.id.splashTextView_tac);
		toeTv = findViewById(R.id.splashTextView_toe);
		letsPlayTv = findViewById(R.id.splashTextView_lets_play);

		firstAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
		secondAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim);
		thirdAnim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
		showSplashAnimation();
		
		Handler delay=new Handler(Looper.getMainLooper());
		delay.postDelayed(new Runnable(){
				@Override
				public void run(){
						startActivity(new Intent(SplashActivity.this, MainActivity.class));
						finish();
					}

			},SPLASH_DURATION);
	}
	private void showSplashAnimation(){
		firstAnim.setDuration(2000L);
		secondAnim.setDuration(1500L);
		thirdAnim.setDuration(1500L);

		ticTv.setAnimation(secondAnim);
		tacTv.setAnimation(secondAnim);
		toeTv.setAnimation(secondAnim);

		letsPlayTv.setAnimation(thirdAnim);
		//letsPlayTv.setAnimation(firstAnim);
	}
}
