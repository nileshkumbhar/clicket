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
		/*TextView messageTextView = (TextView)findViewById(102);
		gridLayout.removeView(messageTextView);
		messageTextView.setText("You lost!");
		setContentView(messageTextView);*/
		showMessageScreen("", "You lost!", this);
	}
	
	public void stop(){
		pointsTextView.setText(""+300000);
	}
	
	private void showMessageScreen(String result, String message, CountDownTimer timer){
			/*timer.cancel();
			((TextView)findViewById(99)).setText(message);
		    //Thread.sleep(5000);
		    LinearLayout linearLayout = new LinearLayout(mainActivity);
		   
		   TextView messageTextView = new TextView(mainActivity);
		    messageTextView.setTextSize(23);
		    messageTextView.setText(message);
		  
	
			Button nextLevelBtn = new Button(mainActivity);
			nextLevelBtn.setText("NEXT LEVEL >>");
			nextLevelBtn.setOnClickListener(new View.OnClickListener(){
				
				@Override
				public void onClick(View view){
					MainActivity.GAME_LEVEL = MainActivity.GAME_LEVEL + 1;
					startActivity(new Intent(mainActivity, MainActivity.class));
				}
			});
			nextLevelBtn.setVisibility(View.VISIBLE);
		    linearLayout.addView(nextLevelBtn);
			linearLayout.addView(messageTextView);*/
			
		    //setContentView(linearLayout);
	}

}
