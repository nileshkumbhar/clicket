package com.sl.clicket.domain; 

import android.os.CountDownTimer;
import android.widget.TextView;

public class ClicketCountDownTimer extends CountDownTimer{
	private TextView pointsTextView;
	
	private int gameLevel;
	
	public TextView getPointsTextView() {
		return pointsTextView;
	}

	public void setPointsTextView(TextView pointsTextView) {
		this.pointsTextView = pointsTextView;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}

	public ClicketCountDownTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onTick(long remainingTime){
		pointsTextView.setText("-"+(remainingTime*gameLevel/(100)));
	}

	@Override
	public void onFinish(){
		showMessageScreen("", "You lost!", this);
	}
	
	public void stop(){
		pointsTextView.setText(""+300000);
	}
	
	private void showMessageScreen(String result, String message, CountDownTimer timer){

	}

}
