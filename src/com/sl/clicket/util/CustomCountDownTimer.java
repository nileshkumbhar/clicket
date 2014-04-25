package com.sl.clicket.util;

import com.sl.clicket.MainActivity;

import android.os.CountDownTimer;
import android.widget.Button;

public class CustomCountDownTimer extends CountDownTimer{
	private long currentTime;
	private Button timerButton;
	
	public CustomCountDownTimer(long currentTime, long countDownInterval, Button timerButton) {
		super(currentTime, countDownInterval);
		this.timerButton = timerButton;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}


	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}


	public Button getTimerButton() {
		return timerButton;
	}


	public void setTimerButton(Button timerButton) {
		this.timerButton = timerButton;
	}


	@Override
	public void onTick(long remainingTime){
		/*if(getCurrentTime() > 0){
			remainingTime = getCurrentTime();
			setCurrentTime(getCurrentTime()-(remainingTime-getCurrentTime()));
		}*/
		
		timerButton.setText(""+remainingTime*MainActivity.GAME_LEVEL/(100));
		
	}

	@Override
	public void onFinish(){
		/*TextView messageTextView = (TextView)findViewById(102);
		gridLayout.removeView(messageTextView);
		messageTextView.setText("You lost!");
		setContentView(messageTextView);*/
//		showMessageScreen("", "You lost!", this);
	}
	
}
