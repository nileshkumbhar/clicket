package com.sl.clicket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private static int SCREEN_WIDTH;
	private static int GAME_LEVEL = 1;
	private static boolean newGame = false;
	private MainActivity mainActivity = this;

	@Override
	public void onContentChanged() {
		//doNothing
	};
	
    /** Called when the activity is first created. */
    @SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState)
	{
    	
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);
        SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
		final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
		gridLayout.setBackgroundColor(Color.BLACK);
		//gridLayout.setsc
		for(int id = 1; id <=4; id++){
			gridLayout.addView(createTextView(id));
		}
		 
		
		/*TextView textView = new TextView(this);
		textView.setId(99);
		textView.setText("1");
		gridLayout.addView(textView);*/
		
		final CountDownTimer timer = new CountDownTimer(300000/GAME_LEVEL, 10){
			@Override
			public void onTick(long remainingTime){
				((TextView)findViewById(102)).setText("-"+(remainingTime*GAME_LEVEL/(100)));
			}

			@Override
			public void onFinish(){
				/*TextView messageTextView = (TextView)findViewById(102);
				gridLayout.removeView(messageTextView);
				messageTextView.setText("You lost!");
				setContentView(messageTextView);*/
				showMessageScreen("", "You lost!", this);
			}
		};
		
		((TextView)findViewById(101)).setText("POINTS");
		for(int i = 1; i<=16; i++){
			gridLayout.addView(createButton(""+i, timer));
	    }
		
		Button button = new Button(this);
		button.setTextSize(14);
		button.setText("RESTART");
		button.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
		button.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_focused));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
			    }
			    return false;
			}
		});
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				newGame = true;
				restartGame(timer);
			}
		}); 
		//LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		gridLayout.addView(button);
		
		TextView textView = new TextView(this);
		//textView.setText("1");
		textView.setId(99);
		gridLayout.addView(textView);
		
		final TextView timerView = new TextView(this);
		gridLayout.addView(timerView);
		
	    timer.start();
	
		newGame = true;
		restartGame(timer);
    }
	
	private void showMessageScreen(String result, String message, CountDownTimer timer){
		try{
			timer.cancel();
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
			linearLayout.addView(messageTextView);
			
		    setContentView(linearLayout);
		}catch(Exception e){
			//LogPrinter logPrinter = new LogPrinter(1, "");
			//logPrinter.println(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	private TextView createTextView(int id){
		TextView textView = new TextView(mainActivity);
		textView.setId(100+id);
		textView.setTextSize(23);
		return textView;
	} 

	@SuppressLint("NewApi") private Button createButton(String label, final CountDownTimer timer){
		Button button = new Button(this);
		button.setText(label);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
//		button.getBackground().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
		button.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
		
		button.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_focused));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
			    }
			    return false;
			}
		});
			
		//button.setTextColor(Color.RED);
		button.setId(Integer.parseInt(label));
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				//((TextView)findViewById(99)).setText(view.getId()+"-"+((Button)view).getText());
		
			    //((TextView)findViewById(99)).setText(""+newGame);
				Button invisibleButton = findAdjusantInvisibleButton(view.getId());
				
				if(invisibleButton != null){
					invisibleButton.setText(((Button)view).getText());
				    invisibleButton.setVisibility(Button.VISIBLE);
				    //((Button)view).setVisibility(Button.INVISIBLE);
				    makeInvisible((Button)view);
				}
				//((TextView)findViewById(99)).setText(""+isResolved());
				if(!newGame && isResolved()){
					//((TextView)findViewById(99)).setText(((TextView)findViewById(102)).getText());
					//Intent intent = new Intent(this, DisplayMessageActivity.class);
					//setContentView(createMessageTextView());
					showMessageScreen("", "Level completed. Your score "+((TextView)findViewById(102)).getText(), timer);//. Total score - "+((TextView)findViewById(102)).getText());
				}
			}
		}); 
		if(label.equals("16")){
			makeInvisible(button);
			//button.setVisibility(button.INVISIBLE);
		}
		return button;
	}
	
	private boolean isResolved(){
		boolean resolved = false;
		int id = 1;
		do{
			Button button = (Button)findViewById(id);
			resolved = button.getVisibility() == Button.VISIBLE && button.getText().equals(""+id);
			id++;
		}while(id < 18 && resolved);
		return id == 17;
	}
	
	private Button findAdjusantInvisibleButton(int id){
		Button button = null;
		if(id % 4 != 0 && isInvisible(id+1)){
			button = (Button) findViewById(id+1);
		}else if((id-1) % 4 != 0 && isInvisible(id-1)){
			button = (Button) findViewById(id-1);
		}else if(isInvisible(id+4)){
			button = (Button) findViewById(id+4);
		}else if(isInvisible(id-4)){
			button = (Button) findViewById(id-4);
		}
		return button;
	}
	
	private boolean isInvisible(int id){
		return findViewById(id) != null && findViewById(id).getVisibility() == Button.INVISIBLE;
	}
	
	private void makeInvisible(Button button){
		button.setVisibility(Button.INVISIBLE);
		 
		for(int i = 1; i < 17; i++) {
			//((Button)findViewById(99)).setText(i);
			Button currButton = (Button)findViewById(i);
			if(currButton != null && currButton.getVisibility() == Button.VISIBLE){
				currButton.setTextColor(Color.GRAY);
			}
		}
			//if(findViewById(16).getVisibility() == Button.VISIBLE){
				//((Button)findViewById(16)).setTextColor(Color.WHITE);
		    //}
			
		
		
	    int id = button.getId();
        if((id-1) % 4 != 0 && id > 1){
			highlight(id - 1);
			//((Button)findViewById(99)).setText(""+(id-1));
		}
		if(id % 4 != 0 && id < 16){
			highlight(id + 1);
			//((Button)findViewById(99)).append(""+(id+1));
		}
		if(id > 4){
			highlight(id - 4);
			//((Button)findViewById(99)).append(""+(id-4));
		}
		if(id < 13){
			highlight(id + 4);
			//((Button)findViewById(99)).append(""+(id+4));
		}
		
	}
	
	private void highlight(int id){
		((Button)findViewById(id)).setTextColor(Color.rgb(255, 183, 111));
	}
	
	private void restartGame(CountDownTimer timer){
		timer.cancel();
		TextView textView = (TextView)findViewById(99);
		if(textView != null && textView.getText() != null && !textView.getText().toString().trim().equals("")){
			textView.setText("");
		}
			
		for(int i = 0; i<999; i++){
			int btnId = (int)(Math.random()*16)+1;
			Button button = findAdjusantInvisibleButton(btnId);
			if(button != null){
				((Button)findViewById(btnId)).performClick();
			}
		    //findViewById(btnId).performClick();
			//((TextView) findViewById(99)).append("-"+btnId);
			//Thread.sleep(1000);
			//((Button) findViewById(16)).setText(btnId);
		}
		newGame = false;
		timer.start();
	}
	
}
