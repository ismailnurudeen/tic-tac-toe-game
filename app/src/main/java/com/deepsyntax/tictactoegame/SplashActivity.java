package com.deepsyntax.tictactoegame;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import android.view.animation.*;

public class SplashActivity extends AppCompatActivity
{
	private final long SPLASH_DURATION=4000L;
	private TextView ticTv,tacTv,toeTv,letsPlayTv;
	ImageView splashLogo;
	private Animation scaleAnim,slideAnim,bounceAnim;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (getSupportActionBar() != null) getSupportActionBar().hide();

		ticTv = findViewById(R.id.splashTextView_tic);
		tacTv = findViewById(R.id.splashTextView_tac);
		toeTv = findViewById(R.id.splashTextView_toe);
        splashLogo = findViewById(R.id.splash_logo);
		letsPlayTv = findViewById(R.id.splashTextView_lets_play);

		scaleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim);
		slideAnim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
		bounceAnim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
	private void showSplashAnimation(){;
		scaleAnim.setDuration(1500L);
		bounceAnim.setDuration(800);
		slideAnim.setDuration(1500L);


		ticTv.setAnimation(scaleAnim);
		tacTv.setAnimation(scaleAnim);
		toeTv.setAnimation(scaleAnim);
        splashLogo.setAnimation(bounceAnim);
		letsPlayTv.setAnimation(slideAnim);
	}
}
