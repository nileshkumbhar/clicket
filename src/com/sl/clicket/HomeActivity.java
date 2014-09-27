package com.sl.clicket;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.sl.clicket.dao.DatabaseHandler;
import com.sl.clicket.domain.Theme;
import com.sl.clicket.entity.HighScore;

public class HomeActivity extends Activity {

	/** Called when the activity is first created. */
	
	private Dialog popupMessage;
	
	private Dialog highScoreDialog;
	
	private DatabaseHandler db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		int SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
		
	    setContentView(R.layout.home);
	    
	    
	    
	    TableLayout tableLayout = (TableLayout)findViewById(R.id.linearLayout2);
	    tableLayout.setBackgroundColor(MainActivity.THEME.getBackgroundColor());
	    
	    ImageView logoIcon = (ImageView)findViewById(R.id.logo_clicket);
	    logoIcon.setMinimumHeight(SCREEN_WIDTH*4/5);
	    logoIcon.setMinimumWidth(SCREEN_WIDTH*4/5);
	    logoIcon.setPadding(0, SCREEN_WIDTH/4, 0, 0); 
	    
	    logoIcon.setMaxHeight(SCREEN_WIDTH);
	    logoIcon.setMaxWidth(SCREEN_WIDTH); 
	    
	    resetImageButton(R.id.btn_high_scores, SCREEN_WIDTH, R.drawable.btn_high_scores_f);
	    resetImageButton(R.id.btn_play, SCREEN_WIDTH, R.drawable.btn_play_f);
	    resetImageButton(R.id.btn_help, SCREEN_WIDTH, R.drawable.btn_help_f);
	    resetImageButton(R.id.btn_theme, SCREEN_WIDTH, R.drawable.btn_theme_f);
	    
	    if(MainActivity.THEME.getBackgroundColor() == Color.GRAY){
			((Button)findViewById(R.id.btn_theme)).setText("FULL MOON");
		} else {
			((Button)findViewById(R.id.btn_theme)).setText("NO MOON");
		}
	}

	@Override
    public void onBackPressed() {

	};
    
	private void resetImageButton(int btnId, int SCREEN_WIDTH, final int onFocusBtnDrawableId) {
		Button imageBtn = (Button)findViewById(btnId);
		
		imageBtn.setMaxWidth((int) ((SCREEN_WIDTH/2)-10));
		imageBtn.setMaxHeight((int) (SCREEN_WIDTH/6));
		imageBtn.setMinimumWidth((int) ((SCREEN_WIDTH/2)-10));
		imageBtn.setMinimumHeight((int) (SCREEN_WIDTH/6));
		
		imageBtn.setTypeface(Typeface.DEFAULT_BOLD);
		imageBtn.setTextSize(SCREEN_WIDTH/40);
		
		final Drawable imageBtnBackground = imageBtn.getBackground();
		imageBtn.setOnTouchListener(new View.OnTouchListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(onFocusBtnDrawableId));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(imageBtnBackground);
			    }
			    return false;
			}
		});
	}

	public void play(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	
	public void showInstructions(View view) {
		popupMessage = new Dialog(this);
    	popupMessage.setTitle("INSTRUCTIONS");
    	
    	popupMessage.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	popupMessage.getWindow().setBackgroundDrawable(getResources().getDrawable(MainActivity.THEME.getAlertBackgroundId()));
    	
    	ScrollView helpScrollView = new ScrollView(this);
    	helpScrollView.addView(createInstructionDialogText());
    	
    	popupMessage.setContentView(helpScrollView);

    	popupMessage.show();
	}
	
	
	
	public void showHighScores(View view) {
		int SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
		
		highScoreDialog = new Dialog(this);
		highScoreDialog.setTitle("HIGH SCORES");
		
		highScoreDialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	highScoreDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(MainActivity.THEME.getAlertBackgroundId()));
    	
    	ScrollView highScoreScrollView = new ScrollView(this);
    	highScoreScrollView.addView(createHighScoreDialogText());
    	highScoreScrollView.setMinimumWidth(SCREEN_WIDTH*3/4);
    	
    	highScoreDialog.setContentView(highScoreScrollView);
    	
    	highScoreDialog.show();
	}
	
	public void changeTheme(View view) {
		if(MainActivity.THEME.getBackgroundColor() == Color.GRAY){
			MainActivity.THEME = new Theme(Color.WHITE, R.drawable.btn_unfocused_fm, R.drawable.btn_focused_fm, R.drawable.alert_bg_fm); 
			((Button)findViewById(R.id.btn_theme)).setText("NO MOON");
		} else {
			MainActivity.THEME = new Theme(Color.GRAY, R.drawable.btn_unfocused_nm, R.drawable.btn_focused_nm, R.drawable.alert_bg_nm);
			((Button)findViewById(R.id.btn_theme)).setText("FULL MOON");
		}
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	public static View showAd(int layoutId, int adId, LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);
        AdView mAdView = (AdView) v.findViewById(adId);
        mAdView.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {

			}
			
			@Override
			public void onPresentScreen(Ad arg0) {

			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {

			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {

			}
			
			@Override
			public void onDismissScreen(Ad arg0) {

			}
		});

        AdRequest adRequest = new AdRequest();
        mAdView.loadAd(adRequest);
        return v;
   }
	
	private LinearLayout createInstructionDialogText() {
		LinearLayout layoutOfPopup = new LinearLayout(this);
		layoutOfPopup.setLayoutParams(new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		StringBuffer instructions = new StringBuffer();
		instructions
				.append("&#8226; This is a 15 square puzzle game.<BR /><BR />");
		instructions
				.append("&#8226; Buttons are shuffled across panel.<BR /><BR />");
		instructions
				.append("&#8226; Click the button to move it to the next empty space.<BR /><BR />");
		instructions
				.append("&#8226; Rearrange all buttons in a sequence before health is finished. <BR /><BR />");
		instructions
				.append("&#8226; Try to stay away from RED buttons. They will eat some of your health. <BR /><BR />");
		instructions
				.append("&#8226; GREEN buttons are your friends. They will give you some extra health.<BR/>");

		TextView popupText = new TextView(this);
		popupText.setTextSize(18);
		popupText.setText(Html.fromHtml(instructions.toString()));

		final Button closeButton = new Button(this);
		closeButton.setText("OK");
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
			}
		});

		linearLayout.addView(popupText);
		linearLayout.addView(closeButton);

		layoutOfPopup.addView(linearLayout);

		return layoutOfPopup;
	}

	private LinearLayout createHighScoreDialogText() {
		LinearLayout scoresLayout = new LinearLayout(this);
		scoresLayout.setOrientation(LinearLayout.VERTICAL);

		db = new DatabaseHandler(this);
		List<HighScore> highScores = db.getAllHighScores();
		
		TextView scoreTextView = new TextView(this);
		StringBuffer scoreBuffer = new StringBuffer();
		if (highScores != null && !highScores.isEmpty()) {
			for (HighScore highScore : highScores) {
				if (highScore.getLevel() < 10) {
					scoreBuffer
							.append("LEVEL "
									+ highScore.getLevel()
									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+ highScore.getScore() + "<BR/>");
				} else {
					scoreBuffer.append("LEVEL " + highScore.getLevel()
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+ highScore.getScore() + "<BR/>");
				}
			}
		} else {
			scoreBuffer
					.append("No high score is recorded yet. Solve the puzzle and create the new one!");
		}
		scoreBuffer.append("<BR/>");
		scoreTextView.setTextSize(18);
		scoreTextView.setText(Html.fromHtml(scoreBuffer.toString()));
		
		final Button closeButton = new Button(this);
		closeButton.setText("OK");
		
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				highScoreDialog.dismiss();
			}
		});

		scoresLayout.addView(scoreTextView);
		scoresLayout.addView(closeButton);
		
		return scoresLayout;
	}
}