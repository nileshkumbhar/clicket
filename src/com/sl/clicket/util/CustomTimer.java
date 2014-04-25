package com.sl.clicket.util;

import com.sl.clicket.MainActivity;
import com.sl.clicket.R;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomTimer {
	private long remainingTime;
    private CountDownTimer timer;
    private Button timerButton;
    private Button scoreButton;
    private Dialog resultPopup;
    private long score;
	
	public CustomTimer(Button timerButton, Button scoreButton, Dialog resultPopup, long score) {
		this.timerButton = timerButton;
		this.scoreButton = scoreButton;
		this.resultPopup = resultPopup;
		this.score = score;
	}

    public void addTime(long addedTimeInMillis) {
      createNewTimer(remainingTime + addedTimeInMillis);
    }

    public void createNewTimer(long timeInMillis) {
	       if(timer != null) {
	         timer.cancel();
	       }
	       timer = new CountDownTimer(timeInMillis, 10) {
	
	       @Override
	       public void onTick(final long millisUntilFinished) {
	           remainingTime = millisUntilFinished;
	           timerButton.setText(""+remainingTime*MainActivity.GAME_LEVEL/10);
//	           timerButton.setText(""+remainingTime*MainActivity.GAME_LEVEL);
	       }
	
	       @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	       @Override
	       public void onFinish() {
	    	   TextView popupText = new TextView(resultPopup.getContext());
	    	   popupText.setTypeface(Typeface.createFromAsset(new ContextWrapper(resultPopup.getContext()).getAssets(), "fonts/BRUSHSCI.TTF"));
	           popupText.setTextSize(24);
	    	   popupText.setText("Game finished! Final Score is " + score);
	    	   final Button playAgainButton = new Button(resultPopup.getContext());
//	    	   playAgainButton.setLayoutParams(new ViewGroup.LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	   playAgainButton.setTextSize(14);
	    	   playAgainButton.setText("PLAY AGAIN");
	    	   playAgainButton.setBackground(resultPopup.getContext().getResources().getDrawable(R.drawable.button_bg_normal));
	    	   playAgainButton.setOnTouchListener(new View.OnTouchListener() {
	   				@Override
	   				public boolean onTouch(View v, MotionEvent event) {
	   					playAgainButton.setBackground(resultPopup.getContext().getResources().getDrawable(R.drawable.button_bg_pressed));
	   					return false;
	   				}
	   	    	});
//	    	   playAgainButton.setBackground(resultPopup.getContext().getResources().getDrawable(
//						R.drawable.btn17));

	    	   /*playAgainButton.setOnTouchListener(new View.OnTouchListener() {
					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							v.setBackground(resultPopup.getContext().getResources().getDrawable(
									R.drawable.btn18));
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							v.setBackground(resultPopup.getContext().getResources().getDrawable(
									R.drawable.btn17));
						}
						return false;
					}
				});*/

	    	   playAgainButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						playAgainButton.setBackground(resultPopup.getContext().getResources().getDrawable(R.drawable.button_bg_normal));
						MainActivity.score = 0;
						MainActivity.GAME_LEVEL = 1;
						resultPopup.getContext().startActivity(new Intent(resultPopup.getContext(),
								MainActivity.class));
					}
				});
	    	   playAgainButton.setVisibility(View.VISIBLE);
	    	   
	    	   LinearLayout linearLayout = new LinearLayout(resultPopup.getContext());
	    	   linearLayout.setOrientation(LinearLayout.VERTICAL);
	    	   linearLayout.addView(popupText);
	    	   linearLayout.addView(playAgainButton);
	    	   linearLayout.setPadding(50, 50, 50, 50);
	    	   resultPopup.setContentView(linearLayout);
	    	   resultPopup.show();
	       }
       }.start();
    }

	public long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public CountDownTimer getTimer() {
		return timer;
	}

	public void setTimer(CountDownTimer timer) {
		this.timer = timer;
	}

	public Button getTimerButton() {
		return timerButton;
	}

	public void setTimerButton(Button timerButton) {
		this.timerButton = timerButton;
	}

}
