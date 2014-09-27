package com.sl.clicket.util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.InterstitialAd;
import com.sl.clicket.MainActivity;

public class CustomTimer {
	private long remainingTime;
    private CountDownTimer timer;
    private Button timerButton;
    private Button scoreButton;
    private Dialog resultPopup;
    private long score;
    private InterstitialAd interstitial;
    
	public CustomTimer(Button timerButton, Button scoreButton, Dialog resultPopup, long score, InterstitialAd interstitial) {
		this.timerButton = timerButton;
		this.scoreButton = scoreButton;
		this.resultPopup = resultPopup;
		this.score = score;
		this.interstitial = interstitial;
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
	    	   if(timerButton.getText().toString().trim().equalsIgnoreCase("0")){
	    		   onFinish();
	    	   }else{
	    		   remainingTime = millisUntilFinished;
	    		   timerButton.setText(""+remainingTime*MainActivity.GAME_LEVEL/10);
//	           		timerButton.setText(""+remainingTime*MainActivity.GAME_LEVEL);
	    	   }
	       }
	
	       @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	       @Override
	       public void onFinish() {
	    	   timerButton.setText("0");
	    	   MainActivity.GAME_LEVEL = 0;
	    	   TextView popupText = new TextView(resultPopup.getContext());
	           popupText.setTextSize(18);
	    	   popupText.setText(Html.fromHtml("Game finished! <BR/>"));
	    	   final Button playAgainButton = new Button(resultPopup.getContext());
	    	   playAgainButton.setTextSize(14);
	    	   playAgainButton.setText("PLAY AGAIN");

	    	   playAgainButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (interstitial.isReady()) {
					          interstitial.show();
					    }else{
					    	MainActivity.score = 0;
							MainActivity.GAME_LEVEL = 1;
							resultPopup.getContext().startActivity(new Intent(resultPopup.getContext(),
									MainActivity.class));
					    }
					}
				});
	    	   playAgainButton.setVisibility(View.VISIBLE);
	    	   
	    	   LinearLayout linearLayout = new LinearLayout(resultPopup.getContext());
	    	   linearLayout.setOrientation(LinearLayout.VERTICAL);
	    	   linearLayout.addView(popupText);
	    	   linearLayout.addView(playAgainButton);
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