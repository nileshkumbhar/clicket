package com.sl.clicket;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.sl.clicket.dao.DatabaseHandler;
import com.sl.clicket.domain.Theme;
import com.sl.clicket.entity.HighScore;
import com.sl.clicket.util.CustomTimer;

@SuppressLint("NewApi")
public class MainActivity extends Activity
{
    private static final long MAX_HEALTH = 1000000;
	private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;
	public static int GAME_LEVEL = 1;
	
	public static Theme THEME = new Theme(Color.GRAY, R.drawable.btn_unfocused_nm, R.drawable.btn_focused_nm, R.drawable.alert_bg_nm);
	
	
	private static boolean newGame = false;
	private MainActivity mainActivity = this;
	public static long score = 0;
	
	private static List<HighScore> highScores;
	
	private DatabaseHandler db;
	
	CustomTimer timer;
	
	LinearLayout layoutOfPopup;
	Dialog popupMessage;
	Dialog highScoreDialog;
    Dialog resultPopup;
    Button insidePopupButton;
    TextView popupText;
    SoundPool sounds;
    int clockSound;
    int bellHappy;
    int clapping;
    
    private int[] gameStatus = new int[16];
    private InterstitialAd interstitial;

    
    @Override
    public void onBackPressed() {
    	//doNothing
    };
    
	@Override
	public void onContentChanged() {
		//doNothing
	};
	
    /** Called when the activity is first created. */
    @SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(this);

        highScores = db.getAllHighScores();
        
        init();
        popupInit();
        
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        setContentView(R.layout.main);
        interstitial = new InterstitialAd(this, "ca-app-pub-6324055048371015/6705059886");
        

        AdRequest adRequest = new AdRequest();
        interstitial.loadAd(adRequest);
        
        interstitial.setAdListener(new AdListener() {
          

			@Override
			public void onDismissScreen(Ad arg0) {
				MainActivity.GAME_LEVEL = MainActivity.GAME_LEVEL + 1;
				startActivity(new Intent(mainActivity, MainActivity.class));
			}
	
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode errorCode) {

			}
	
			@Override
			public void onLeaveApplication(Ad arg0) {

			}
	
			@Override
			public void onPresentScreen(Ad arg0) {

			}
	
			@Override
			public void onReceiveAd(Ad arg0) {

			}
        });
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  
        clockSound = sounds.load(this, R.raw.clock_tick_loop, 1);
        bellHappy = sounds.load(this, R.raw.bell_happy, 1);
        clapping = sounds.load(this, R.raw.clapping, 1);

        SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
        
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setBackgroundColor(THEME.getBackgroundColor());
		for(int id = 11; id <=18; id++){
			gridLayout.addView(createTextView(id));
		}
		
		Button timerTitleButton = createTopButton(102);
		timerTitleButton.setText("HEALTH");
		timerTitleButton.setTextSize(16);
		timerTitleButton.setTextColor(Color.RED);
		gridLayout.addView(timerTitleButton);
		
		Button timerButton = createTopButton(101);
		timerButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"));
		timerButton.setTextSize(18);
		timerButton.setTextColor(Color.RED);
		gridLayout.addView(timerButton);
		
		Button scoreTitleButton = createTopButton(104);
		scoreTitleButton.setTextSize(16);
		scoreTitleButton.setTextColor(Color.GREEN);
		scoreTitleButton.setText("LEVEL");
		
		gridLayout.addView(scoreTitleButton);
		
		Button levelButton = createTopButton(103);
		levelButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"));
		levelButton.setTextSize(22);
		levelButton.setText(""+MainActivity.GAME_LEVEL);
		levelButton.setTextColor(Color.GREEN);
		gridLayout.addView(levelButton);
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
        popupText.setTextSize(18);
        if(timer == null){
        	timer = new CustomTimer((Button)findViewById(101), levelButton, resultPopup, score, interstitial);
        }
		
		for(int i = 1; i<=16; i++){
			gridLayout.addView(createButton(""+i, timer));
	    }
		
		final Context context = this;
		Button homeButton = createPurpleButton("HOME");
		homeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HomeActivity.class);
				startActivity(intent);
			}
		});
		gridLayout.addView(homeButton);
		
		Button resetButton = createPurpleButton("RESET");
		
		resetButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				newGame = true;
				restartGame(timer);
			}
		}); 
		gridLayout.addView(resetButton);
		
		final Button instructionsButton = createPurpleButton("HELP");
		instructionsButton.setId(9999);
		
		instructionsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMessage.show();//showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
				timer.getTimer().cancel();
			}
		});
		gridLayout.addView(instructionsButton);
		
		Button highScoreButton = createPurpleButton("CHAMPS");
		highScoreButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				highScoreDialog.show();//showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
				timer.getTimer().cancel();
			}
		});
		gridLayout.addView(highScoreButton);
		
	    timer.createNewTimer(MAX_HEALTH/GAME_LEVEL);
	
		newGame = true;
		restartGame(timer);
		
    }
    
    public void init() {
        popupText = new TextView(this);
        layoutOfPopup = new LinearLayout(this);
        layoutOfPopup.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        StringBuffer instructions = new StringBuffer();
        instructions.append("&#8226; This is a 15 square puzzle game.<BR /><BR />");
        instructions.append("&#8226; Buttons are shuffled across panel.<BR /><BR />");
        instructions.append("&#8226; Click the button to move it to the next empty space.<BR /><BR />");
        instructions.append("&#8226; Rearrange all the buttons in a sequence before health is finished. <BR /><BR />");
        instructions.append("&#8226; Try to stay away from RED buttons. They will eat some of your health. <BR /><BR />");
        instructions.append("&#8226; GREEN buttons are your friends. They will give you some extra health.<BR/>");
        
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
    }
    
    
    
    public void popupInit() {
    	popupMessage = new Dialog(this);
    	popupMessage.setTitle("INSTRUCTIONS");
    	
    	ScrollView helpScrollView = new ScrollView(this);
    	helpScrollView.addView(layoutOfPopup);
    	
    	popupMessage.setContentView(helpScrollView);

    	popupMessage.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	popupMessage.getWindow().setBackgroundDrawable(getResources().getDrawable(MainActivity.THEME.getAlertBackgroundId()));

    	popupMessage.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(timer != null){
					timer.addTime(0L);
				}
			}
		});
    	
    	resultPopup = new Dialog(this);
    	resultPopup.setTitle("RESULT");
    	resultPopup.setCanceledOnTouchOutside(false);
    	resultPopup.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	resultPopup.getWindow().setBackgroundDrawable(getResources().getDrawable(MainActivity.THEME.getAlertBackgroundId()));
    	
    	highScoreDialog = new Dialog(this);
    	highScoreDialog.setCanceledOnTouchOutside(true);
    	highScoreDialog.setTitle("HIGH SCORES");
    	
    	ScrollView scrollView = new ScrollView(this);
    	
    	LinearLayout scoresLayout = new LinearLayout(this);
    	scoresLayout.setOrientation(LinearLayout.VERTICAL);
    	List<HighScore> highScores = db.getAllHighScores();
    	TextView scoreTextView = new TextView(this);
    	StringBuffer scoreBuffer = new StringBuffer();
    	if(highScores != null && !highScores.isEmpty()){
	    	for(HighScore highScore : highScores){
	    		if(highScore.getLevel() < 10){
	    			scoreBuffer.append("LEVEL "+highScore.getLevel()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+highScore.getScore()+"<BR/>");
	    		} else {
	    			scoreBuffer.append("LEVEL "+highScore.getLevel()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+highScore.getScore()+"<BR/>");
	    		}
	    	}
	    	
    	}else{
    		scoreBuffer.append("No high score is recorded yet. Solve the puzzle and create the new one!");
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
    	
    	scrollView.addView(scoresLayout);
    	scrollView.setMinimumWidth(SCREEN_WIDTH*3/4);
      
    	highScoreDialog.setContentView(scrollView);

    	highScoreDialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	highScoreDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(MainActivity.THEME.getAlertBackgroundId()));
    	highScoreDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(timer != null){
					timer.addTime(0L);
				}
			}
		});
    	
    }
    
    public void displayInterstitialOrMainActivity() {
        if (interstitial.isReady()) {
          interstitial.show();
        }else{
        	MainActivity.GAME_LEVEL = MainActivity.GAME_LEVEL + 1;
			startActivity(new Intent(mainActivity, MainActivity.class));
        }
    }

    
    @SuppressLint("NewApi")
	private Button createPurpleButton(String label){
    	Button button = new Button(this);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/50);
		button.setTypeface(Typeface.SANS_SERIF);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
//		button.setTextSize(14);
		button.setText(label);
		button.setBackground(getResources().getDrawable(R.drawable.btn17));

		button.setOnTouchListener(new View.OnTouchListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn18));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn17));
			    }
			    return false;
			}
		});
		return button;
    }
	
	private void showMessageScreen(String result, String message, CountDownTimer timer){
		try{
			timer.cancel();
		    LinearLayout linearLayout = new LinearLayout(mainActivity);
		    linearLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		    linearLayout.setOrientation(LinearLayout.VERTICAL);
		   
		    TextView messageTextView = new TextView(mainActivity);
		    messageTextView.setTextSize(18);
		    messageTextView.setText(Html.fromHtml(message+" <BR/>"));
		    
			Button nextLevelBtn = createDialogInsideButton("NEXT LEVEL >>");
			
			linearLayout.addView(messageTextView);
			linearLayout.addView(nextLevelBtn);
			resultPopup.setContentView(linearLayout);
			resultPopup.show(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Button createDialogInsideButton(String label) {
		final Button nextLevelBtn = new Button(this);//
		nextLevelBtn.setTextSize(14);
		nextLevelBtn.setText(label);

		nextLevelBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				displayInterstitialOrMainActivity();
			}
		});
		nextLevelBtn.setVisibility(View.VISIBLE);
	    	
		return nextLevelBtn;
	}
	
	private TextView createTextView(int id){
		TextView textView = new TextView(mainActivity);
		textView.setId(100+id);
		textView.setTextSize(23);
		return textView;
	} 
	
	private Button createTopButton(int id){
		Button button = new Button(this);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setTypeface(Typeface.SANS_SERIF);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/8);
		button.setMaxWidth(SCREEN_WIDTH/4);
		button.setMaxHeight(SCREEN_WIDTH/8);
		button.setMinWidth(SCREEN_WIDTH/4);
		button.setMinHeight(SCREEN_WIDTH/8);
		button.setId(id);

		button.setBackground(getResources().getDrawable(R.drawable.btn_top5));
		
		return button;
	} 

	@SuppressLint("NewApi") private Button createButton(String label, final CustomTimer timer){
		Button button = new Button(this);
		button.setText(label);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
		button.setBackground(getResources().getDrawable(THEME.getUnfocusedButtonId()));
		
		button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"), 48);
		button.setTextColor(THEME.getBackgroundColor());
		
		button.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BitmapDrawable background = (BitmapDrawable) v.getBackground();
				Button scoreView = ((Button)findViewById(101));
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(THEME.getFocusedButtonId()));
			    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
			    	v.setBackground(getResources().getDrawable(THEME.getUnfocusedButtonId()));
			    }

				if(background.getBitmap().equals(((BitmapDrawable)getResources().getDrawable(R.drawable.btn_green)).getBitmap())){
			    	Long score = Long.parseLong(""+((TextView)findViewById(101)).getText()); 
			    	if((score+1000)*10/GAME_LEVEL > 99999){
			    		timer.createNewTimer(MAX_HEALTH/GAME_LEVEL);
			    	}else {
			    		timer.createNewTimer((score+1000)*10/GAME_LEVEL);
			    	}
			    }else if(background.getBitmap().equals(((BitmapDrawable)getResources().getDrawable(R.drawable.btn_red)).getBitmap())){
			    	Long score = Long.parseLong(""+((TextView)findViewById(101)).getText()); 
			    	timer.getTimer().cancel();
			    	timer.createNewTimer((score-1000)*10/GAME_LEVEL);
			    } 
			    return false;
			}
		});
			
		button.setId(Integer.parseInt(label));
		button.setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View view, DragEvent event) {
				processOnClick(timer, view);
				return true;
			}
		});
		
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				processOnClick(timer, view);
			}
		}); 
		if(label.equals("16")){
			makeInvisible(button);
		}
		return button;
	}
	
	private void processOnClick(final CustomTimer timer, View view) {
		sounds.play(bellHappy, 1.0f, 1.0f, 0, 0, 1.5f);
		Button invisibleButton = findAdjusantInvisibleButton(view.getId());
		
		if(invisibleButton != null){
			invisibleButton.setText(((Button)view).getText());
		    invisibleButton.setVisibility(Button.VISIBLE);
		    makeInvisible((Button)view);
		}
		if(!newGame && isResolved()){
			sounds.play(clapping, 1.0f, 1.0f, 0, 0, 1.5f);
			long currentLevelScore = Long.parseLong(""+((TextView)findViewById(101)).getText())+(1000*(GAME_LEVEL-1));
			score = score + currentLevelScore;
			
			StringBuffer message = new StringBuffer();
			HighScore highScore = db.getHighScoreByLevel(GAME_LEVEL);
			if(highScore != null && currentLevelScore > highScore.getScore()){
				highScore.setScore(currentLevelScore);
				db.updateHighScore(highScore);
				message.append("                                  ");
				message.append("Congratulations! <BR/>New high score for level "+GAME_LEVEL + " - " + currentLevelScore);
			}else if (highScore == null){
				highScore = new HighScore();
				highScore.setLevel(GAME_LEVEL);
				highScore.setScore(currentLevelScore);
				db.add(highScore);
				message.append("                                  ");
				message.append("Congratulations! <BR/>New high score for level "+GAME_LEVEL+ " - " + currentLevelScore);
			}else{
				message.append("Level "+ GAME_LEVEL + " completed.");
			}
			message.append("<BR/>Score "+currentLevelScore);
			showMessageScreen("", message.toString(), timer.getTimer());//. Total score - "+((TextView)findViewById(102)).getText());
		}else {
			String scoreText = ((TextView)findViewById(101)).getText().toString();
			if(scoreText != null && !scoreText.trim().equals("")){
				long currentLevelScore = Long.parseLong(""+((TextView)findViewById(101)).getText())+(10000*(GAME_LEVEL-1));
				if(currentLevelScore < 10000){
			        int clockSoundStream = sounds.play(clockSound, 1.0f, 1.0f, 0,-1, 1.5f);  
				}
			}
		}
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
			Button currButton = (Button)findViewById(i);
			if(currButton != null && currButton.getVisibility() == Button.VISIBLE){
				currButton.setTextColor(THEME.getBackgroundColor());
				currButton.setBackground(getResources().getDrawable(THEME.getUnfocusedButtonId()));
			}
		}
		
	    int id = button.getId();
        if((id-1) % 4 != 0 && id > 1){
			highlight(id - 1);
		}
		if(id % 4 != 0 && id < 16){
			highlight(id + 1);
		}
		if(id > 4){
			highlight(id - 4);
		}
		if(id < 13){
			highlight(id + 4);
		}
		
	}
	
	private void highlight(int id){
		if(MainActivity.THEME.getBackgroundColor() == Color.WHITE){
			((Button)findViewById(id)).setTextColor(Color.rgb(255, 140, 26));
		}else{
			((Button)findViewById(id)).setTextColor(Color.rgb(255, 183, 111));
		}
		
		int shuffle = (int)(Math.random()*100);
		if(GAME_LEVEL > 4 && shuffle < GAME_LEVEL-1 ){
			((Button)findViewById(id)).setBackground(getResources().getDrawable(R.drawable.btn_red));
		}else if(GAME_LEVEL > 2 && shuffle <= 2*GAME_LEVEL && shuffle < 100){
			((Button)findViewById(id)).setBackground(getResources().getDrawable(R.drawable.btn_green));
		}
	}
	
	private void restartGame(CustomTimer timer){
		timer.getTimer().cancel();
		TextView textView = (TextView)findViewById(99);
		if(textView != null && textView.getText() != null && !textView.getText().toString().trim().equals("")){
			textView.setText("");
		}
		
		rearrangeButtonsToOriginalPositions();
		for(int i = 0; i<999; i++){
			int btnId = (int)(Math.random()*16)+1;
			Button button = findAdjusantInvisibleButton(btnId);
			if(16-btnId <= 2*GAME_LEVEL && button != null){
				((Button)findViewById(btnId)).performClick();
			}
		}
		if(isResolved() || isSimilarToPriviousGame()){
			restartGame(timer);
		}
		populateGameStatus();
		newGame = false;
		
		timer.createNewTimer(MAX_HEALTH/GAME_LEVEL);
		timer.getTimer().start();
	}
	
	private void rearrangeButtonsToOriginalPositions() {
		for(int btnId = 1; btnId <= 16; btnId++){
			Button button = ((Button)findViewById(btnId));
			button.setText(""+btnId);
			button.setVisibility(View.VISIBLE);
		}
		((Button)findViewById(16)).setVisibility(View.INVISIBLE);
	}

	private void populateGameStatus(){
		for(int i=1; i<=16 ; i++){
			gameStatus[i-1] = Integer.parseInt(((Button)findViewById(i)).getText().toString().trim());
		}
	}
	
	private boolean isSimilarToPriviousGame(){
		int [] currentGameStatus = new int[16];
		for(int i=1; i<=16 ; i++){
			currentGameStatus[i-1] = Integer.parseInt(((Button)findViewById(i)).getText().toString().trim());
		}
		return currentGameStatus.equals(gameStatus);
	}
	
}
