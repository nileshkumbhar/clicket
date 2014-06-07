package com.sl.clicket;

import java.util.List;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.sl.clicket.dao.DatabaseHandler;
import com.sl.clicket.entity.HighScore;
import com.sl.clicket.util.Calculator;
import com.sl.clicket.util.CustomCountDownTimer;
import com.sl.clicket.util.CustomTimer;

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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.TextSize;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity
{
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;
	public static int GAME_LEVEL = 1;
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
    
    private int[] gameStatus = new int[16];
    private InterstitialAd interstitial;

    
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

        Log.d("READING: ", "Reading all high scores");
        highScores = db.getAllHighScores();
        
        init();
        popupInit();
        
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        HomeActivity.showAd(R.layout.main, R.id.top_main_ad, LayoutInflater.from(this), (LinearLayout)findViewById(R.id.tableRow1), savedInstanceState);

        setContentView(R.layout.main);
        
     // Create the interstitial.
        interstitial = new InterstitialAd(this, "a15361716b05982");

        // Create ad request.
        AdRequest adRequest = new AdRequest();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        
     // Set the AdListener.
        interstitial.setAdListener(new AdListener() {
          

			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode errorCode) {
				String message = String.format("onAdFailedToLoad (%s)", errorCode.values());
	            Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show();
			}
	
			@Override
			public void onLeaveApplication(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onPresentScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onReceiveAd(Ad arg0) {
	            Toast.makeText(mainActivity, "onAdLoaded", Toast.LENGTH_SHORT).show();
			}
        });
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  
        clockSound = sounds.load(this, R.raw.clock_tick_loop, 1);
        bellHappy = sounds.load(this, R.raw.bell_happy, 1);

        /*MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clock_tick_loop);
        mediaPlayer.setLooping(true);
        mediaPlayer.
        mediaPlayer.start();*/
        
        SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
		
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
		gridLayout.setBackgroundColor(Color.BLACK);
//		gridLayout.setBackground(getResources().getDrawable(R.drawable.alert_bg));
		for(int id = 11; id <=14; id++){
			gridLayout.addView(createTextView(id));
		}
		
		//gridLayout.setsc
		/*for(int id = 1; id <=4; id++){
			gridLayout.addView(createTextView(100+id));
		}*/
		
		Button timerTitleButton = createTopButton(102, R.drawable.level);
//		levelButton.setTextColor(Color.BLACK);
		timerTitleButton.setText("TIMER");
//		timerTitleButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BRUSHSCI.TTF"));
		timerTitleButton.setTextSize(18);
		timerTitleButton.setTextColor(Color.RED);
		gridLayout.addView(timerTitleButton);
		
		Button timerButton = createTopButton(101, R.drawable.stop_watch2);
		timerButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"));
		timerButton.setTextSize(18);
		timerButton.setTextColor(Color.RED);
		gridLayout.addView(timerButton);
		
		/*Button levelButton = createTopButton(102, R.drawable.level);
//		levelButton.setTextColor(Color.BLACK);
		levelButton.setText(""+GAME_LEVEL);
		levelButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/m23.TTF"), 24);
		levelButton.setTextColor(Color.BLUE);
		gridLayout.addView(levelButton);*/
		
		Button scoreTitleButton = createTopButton(104, R.drawable.score);
//		scoreTitleButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BRUSHSCI.TTF"));
		scoreTitleButton.setTextSize(16);
		scoreTitleButton.setTextColor(Color.GREEN);
		scoreTitleButton.setText("SCORE");
		
		gridLayout.addView(scoreTitleButton);

		
		Button scoreButton = createTopButton(103, R.drawable.stop_watch2);
		scoreButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"));
		scoreButton.setTextSize(14);
		scoreButton.setText(""+score);
		scoreButton.setTextColor(Color.GREEN);
		gridLayout.addView(scoreButton);
		
		/*TextView textView = new TextView(this);
		textView.setId(99);
		textView.setText("1");
		gridLayout.addView(textView);*/
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
//        popupText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BRUSHSCI.TTF"));
        popupText.setTextSize(18);
//        linearLayout.setPadding(50, 50, 50, 50);
//        linearLayout.addView(popupText);
//        resultPopup.setContentView(linearLayout);
        if(timer == null){
        	timer = new CustomTimer((Button)findViewById(101), scoreButton, resultPopup, score);
        }
		/*@Override
		public void onFinish(){
			TextView messageTextView = (TextView)findViewById(102);
			gridLayout.removeView(messageTextView);
			messageTextView.setText("You lost!");
			setContentView(messageTextView);
			showMessageScreen("", "You lost!", this);
		}
	};*/
		
//		((TextView)findViewById(101)).setText("POINTS");
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
				restartGame(timer.getTimer());
			}
		}); 
		//LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
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
		
		
		/*TextView textView = new TextView(this);
		//textView.setText("1");
		textView.setId(99);
		gridLayout.addView(textView);*/
		
		Button highScoreButton = createPurpleButton("CHAMPS");
		highScoreButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				highScoreDialog.show();//showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
				timer.getTimer().cancel();
			}
		});
		gridLayout.addView(highScoreButton);
		
		/*final TextView timerView = new TextView(this);
		gridLayout.addView(timerView);*/
		
		
		
	    timer.createNewTimer(600000/GAME_LEVEL);

	
		newGame = true;
		restartGame(timer.getTimer());
		
    }
    
    public void init() {
        popupText = new TextView(this);
        layoutOfPopup = new LinearLayout(this);
        layoutOfPopup.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        layoutOfPopup.setBackgroundColor(Color.CYAN);
//        insidePopupButton.setText("OK");
        StringBuffer instructions = new StringBuffer();
        instructions.append("&#8226; Buttons are shuffled across the panel.<BR /><BR />");
        instructions.append("&#8226; Click the button to move it to adjacent empty space.<BR /><BR />");
        instructions.append("&#8226; Rearrange all the buttons in a sequence before timer is up. <BR /><BR />");
        instructions.append("&#8226; RED buttons will eat some of your time. <BR /><BR />");
        instructions.append("&#8226; GREEN buttons will give you some extra time to solve the puzzle.<BR/>");
        
        popupText.setTextSize(18);
        popupText.setText(Html.fromHtml(instructions.toString()));

        
        
        final Button closeButton = new Button(this);
//        imageButton.setBackground(getResources().getDrawable(R.drawable.btn_red));
        /*imageButton.setText("OK");
        imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
			}
		});*/
        
//        closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
    	closeButton.setText("OK");
    	/*closeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_pressed));
				return false;
			}
    	});*/
    	
    	closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
//				closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
			}
		});
        
//        popupText.setPadding(50, 50, 0, 0);
//        linearLayout.setPadding(50, 50, 50, 50);

//        insidePopupButton.setWidth(layoutOfPopup.getWidth()-100);
        linearLayout.addView(popupText);
        linearLayout.addView(closeButton);
//        layoutOfPopup.addView(insidePopupButton);
        
        layoutOfPopup.addView(linearLayout);
    }
    
    
    
    public void popupInit() {
    	/*popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
    	popupMessage.setContentView(layoutOfPopup);
        popupMessage.setWidth(SCREEN_WIDTH-50);
        popupMessage.setHeight(SCREEN_HEIGHT-300);*/
    	popupMessage = new Dialog(this);
    	popupMessage.setTitle("INSTRUCTIONS");
    	
    	ScrollView helpScrollView = new ScrollView(this);
    	helpScrollView.addView(layoutOfPopup);
    	
    	
//    	helpScrollView.setPadding(50, 0, 50, 0);
    	popupMessage.setContentView(helpScrollView);
//    	
    	popupMessage.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	popupMessage.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alert_bg));
//    	
        popupMessage.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(timer != null){
					timer.addTime(0L);
				}
			}
		});
//        popupMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        
        
    	/*insidePopupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
			}
		});*/
    	
    	/*resultPopup = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
    	resultPopup.setContentView(layoutOfPopup);
    	resultPopup.setWidth(SCREEN_WIDTH-50);
    	resultPopup.setHeight(SCREEN_HEIGHT-300);
    	resultPopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.clicket_popup_bg));*/
    	
    	resultPopup = new Dialog(this);
    	resultPopup.setTitle("RESULT");
    	resultPopup.setCanceledOnTouchOutside(false);
    	resultPopup.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	resultPopup.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alert_bg));
//    	resultPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//    	resultPopup.setContentView(layoutOfPopup);
//    	resultPopup.setContentView(layoutOfPopup);
    	
    	/*insidePopupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
				resultPopup.dismiss();
			}
		});*/
    	
    	highScoreDialog = new Dialog(this);
    	highScoreDialog.setCanceledOnTouchOutside(false);
    	highScoreDialog.setTitle("HIGH SCORES");
    	
    	ScrollView scrollView = new ScrollView(this);
    	
    	LinearLayout scoresLayout = new LinearLayout(this);
    	scoresLayout.setOrientation(LinearLayout.VERTICAL);
    	List<HighScore> highScores = db.getAllHighScores();
    	TextView scoreTextView = new TextView(this);
    	StringBuffer scoreBuffer = new StringBuffer();
    	if(highScores != null && !highScores.isEmpty()){
//    		scoreBuffer.append("LEVEL     SCORE<BR/>");
	    	for(HighScore highScore : highScores){
	    		scoreBuffer.append("LEVEL "+highScore.getLevel()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+highScore.getScore()+"<BR/>");
	    	}
    	}else{
    		scoreBuffer.append("No high score is recorded yet. Solve the puzzle and create the new one!");
    	}
    	scoreBuffer.append("<BR/>");
//    	scoreTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BRUSHSCI.TTF"));
    	scoreTextView.setTextSize(18);
    	scoreTextView.setText(Html.fromHtml(scoreBuffer.toString()));
    	
    	final Button closeButton = new Button(this);
//    	closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
    	closeButton.setText("OK");
    	/*closeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_pressed));
				return false;
			}
    	});*/
    	
    	closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				highScoreDialog.dismiss();
//				closeButton.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
			}
		});
      
//      popupText.setPadding(50, 50, 0, 0);
//    	scoresLayout.setPadding(50, 50, 50, 50);

//      insidePopupButton.setWidth(layoutOfPopup.getWidth()-100);
    	
      scoresLayout.addView(scoreTextView);
      scoresLayout.addView(closeButton);
    	
      scrollView.addView(scoresLayout);
      
//      scrollView.setPadding(50, 0, 50, 0);
    	highScoreDialog.setContentView(scrollView);
//    	
    	highScoreDialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//    	highScoreDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.clicket_popup_bg));
    	highScoreDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alert_bg));
    	highScoreDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(timer != null){
					timer.addTime(0L);
				}
			}
		});
    	
    }
    
    public void displayInterstitial() {
        if (interstitial.isReady()) {
          interstitial.show();
        }
    }

    
    @SuppressLint("NewApi")
	private Button createPurpleButton(String label){
    	Button button = new Button(this);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
		button.setTextSize(14);
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
//			((TextView)findViewById(99)).setText(message);
		    //Thread.sleep(5000);
		    LinearLayout linearLayout = new LinearLayout(mainActivity);
		    linearLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		    linearLayout.setOrientation(LinearLayout.VERTICAL);
		   
		    TextView messageTextView = new TextView(mainActivity);
//		    messageTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BRUSHSCI.TTF"));
		    messageTextView.setTextSize(18);
		    messageTextView.setText(Html.fromHtml(message+" <BR/>"));
	
			Button nextLevelBtn = createDialogInsideButton("NEXT LEVEL >>");
		    
			linearLayout.addView(messageTextView);
			linearLayout.addView(nextLevelBtn);
//			linearLayout.setPadding(50, 50, 50, 50);
			resultPopup.setContentView(linearLayout);
			resultPopup.show();
//		    setContentView(linearLayout);
		}catch(Exception e){
			//LogPrinter logPrinter = new LogPrinter(1, "");
			//logPrinter.println(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

	private Button createDialogInsideButton(String label) {
		final Button nextLevelBtn = new Button(this);//
//		nextLevelBtn.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		nextLevelBtn.setTextSize(14);
		nextLevelBtn.setText(label);
//		nextLevelBtn.setBackground(getResources().getDrawable(R.drawable.btn17));

		/*nextLevelBtn.setOnTouchListener(new View.OnTouchListener() {
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
		});*/
		
		nextLevelBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
//				nextLevelBtn.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
				displayInterstitial();
				MainActivity.GAME_LEVEL = MainActivity.GAME_LEVEL + 1;
				startActivity(new Intent(mainActivity, MainActivity.class));
			}
		});
		nextLevelBtn.setVisibility(View.VISIBLE);
		
		/*nextLevelBtn.setBackground(getResources().getDrawable(R.drawable.button_bg_normal));
		nextLevelBtn.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					nextLevelBtn.setBackground(getResources().getDrawable(R.drawable.button_bg_pressed));
					return false;
				}
	    	});*/
	    	
		return nextLevelBtn;
	}
	
	private TextView createTextView(int id){
		TextView textView = new TextView(mainActivity);
		textView.setId(100+id);
		textView.setTextSize(23);
//		textView.setHeight(AdSize.BANNER.getHeight());
//		textView.setBackgroundColor(Color.RED);
		return textView;
	} 
	
	private Button createTopButton(int id, int resourceId){
		Button button = new Button(this);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
		button.setId(id);
//		button.setTextColor(Color.RED);
//		button.getBackground().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
		
//		button.setBackground(getResources().getDrawable(resourceId));
		
		button.setBackgroundColor(Color.BLACK);
		
		/*button.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(R.drawable.alarm_btn2));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(getResources().getDrawable(R.drawable.alarm_btn2));
			    }
			    return false;
			}
		});*/
		
		/*if(id==101){
			button.setVisibility(View.VISIBLE);
		}else{
			button.setVisibility(View.INVISIBLE);
		}*/
		return button;
	} 

	@SuppressLint("NewApi") private Button createButton(String label, final CustomTimer timer){
		Button button = new Button(this);
		button.setText(label);
		button.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		button.setTextSize(SCREEN_WIDTH/20);
		button.setWidth(SCREEN_WIDTH/4);
		button.setHeight(SCREEN_WIDTH/4);
//		button.getBackground().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
		button.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
		
		button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Clockopia.ttf"), 48);
		
		button.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BitmapDrawable background = (BitmapDrawable) v.getBackground();
				Button scoreView = ((Button)findViewById(101));
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_focused));
			    } else if (event.getAction() == MotionEvent.ACTION_UP) {
			    	v.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
			    }

				if(background.getBitmap().equals(((BitmapDrawable)getResources().getDrawable(R.drawable.btn_green)).getBitmap())){
//			    	TextView scoreView  = (TextView)findViewById(101);
			    	Long score = Long.parseLong(""+((TextView)findViewById(101)).getText()); 
//			    	timer.setCurrentTime((score+200)*100/GAME_LEVEL);
//			    	timer.onTick((score-200)*100/GAME_LEVEL);
			    	timer.createNewTimer((score+1000)*10/GAME_LEVEL);
			    }else if(background.getBitmap().equals(((BitmapDrawable)getResources().getDrawable(R.drawable.btn_red)).getBitmap())){
//			    	TextView scoreView  = (TextView)findViewById(101);
			    	Long score = Long.parseLong(""+((TextView)findViewById(101)).getText()); 
//			    	scoreView.setText(""+(score-200));
//			    	timer.setCurrentTime((score-200)*100/GAME_LEVEL);
//			    	timer.onTick((score-200)*100/GAME_LEVEL);
			    	timer.getTimer().cancel();
			    	timer.createNewTimer((score-1000)*10/GAME_LEVEL);
			    }
			    return false;
			}
		});
			
		//button.setTextColor(Color.RED);
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
			//button.setVisibility(button.INVISIBLE);
		}
		return button;
	}
	
	private void processOnClick(final CustomTimer timer, View view) {
		//((TextView)findViewById(99)).setText(view.getId()+"-"+((Button)view).getText());

	    //((TextView)findViewById(99)).setText(""+newGame);
		sounds.play(bellHappy, 1.0f, 1.0f, 0, 0, 1.5f);
		Button invisibleButton = findAdjusantInvisibleButton(view.getId());
		
		if(invisibleButton != null){
			invisibleButton.setText(((Button)view).getText());
			/*try{
				invisibleButton.setText(Calculator.createExpression(Integer.parseInt(((Button)view).getText().toString())));
			}catch(Throwable e){
				e.printStackTrace();
			}*/
		    invisibleButton.setVisibility(Button.VISIBLE);
		    //((Button)view).setVisibility(Button.INVISIBLE);
		    
//		    ((Button)view).setText(""+Calculator.evaluateExpression(((Button)view).getText().toString()));
		    makeInvisible((Button)view);
		}
		//((TextView)findViewById(99)).setText(""+isResolved());
		if(!newGame && isResolved()){
			//((TextView)findViewById(99)).setText(((TextView)findViewById(102)).getText());
			//Intent intent = new Intent(this, DisplayMessageActivity.class);
			//setContentView(createMessageTextView());
			long currentLevelScore = Long.parseLong(""+((TextView)findViewById(101)).getText())+(10000*(GAME_LEVEL-1));
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
			message.append("<BR/>Total score "+score);
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
			//((Button)findViewById(99)).setText(i);
			Button currButton = (Button)findViewById(i);
			if(currButton != null && currButton.getVisibility() == Button.VISIBLE){
				currButton.setTextColor(Color.GRAY);
				currButton.setBackground(getResources().getDrawable(R.drawable.btn_unfocused));
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
		int shuffle = (int)(Math.random()*100);
		System.out.println(shuffle+" ========================= "+GAME_LEVEL);
		if(GAME_LEVEL > 12 && shuffle < GAME_LEVEL-1 ){
			((Button)findViewById(id)).setBackground(getResources().getDrawable(R.drawable.btn_red));
		}else if(GAME_LEVEL > 8 && shuffle <= 2*GAME_LEVEL && shuffle < 100){
			((Button)findViewById(id)).setBackground(getResources().getDrawable(R.drawable.btn_green));
		}/*else if(shuffle == 4){
			System.out.println("===========  GOLDEN  ===============");
			((Button)findViewById(id)).setBackground(getResources().getDrawable(R.drawable.btn_golden_focused));
		}*/
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
			if(16-btnId <= 2*GAME_LEVEL && button != null){
				((Button)findViewById(btnId)).performClick();
			}
		    //findViewById(btnId).performClick();
			//((TextView) findViewById(99)).append("-"+btnId);
			//Thread.sleep(1000);
			//((Button) findViewById(16)).setText(btnId);
		}
		if(isResolved() || isSimilarToPriviousGame()){
			restartGame(timer);
		}
		populateGameStatus();
		newGame = false;
		timer.start();
	}
	
	private void populateGameStatus(){
		for(int i=1; i<=16 ; i++){
			gameStatus[i-1] = Integer.parseInt(((Button)findViewById(i)).getText().toString().trim());
		}
		System.out.println("gameStatus ===> "+gameStatus);
	}
	
	private boolean isSimilarToPriviousGame(){
		int [] currentGameStatus = new int[16];
		for(int i=1; i<=16 ; i++){
			currentGameStatus[i-1] = Integer.parseInt(((Button)findViewById(i)).getText().toString().trim());
		}
		return currentGameStatus.equals(gameStatus);
	}
	
}
