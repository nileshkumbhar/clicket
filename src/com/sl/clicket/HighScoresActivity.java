package com.sl.clicket;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.sl.clicket.dao.DatabaseHandler;
import com.sl.clicket.entity.HighScore;

public class HighScoresActivity extends Activity {
	private DatabaseHandler db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHandler(this);

        Log.d("READING: ", "Reading all high scores");
        List<HighScore> highScoreList = db.getAllHighScores();
        
	    setContentView(R.layout.highscores);

	    findViewById(R.id.highScoreTextParent).setBackgroundColor(MainActivity.THEME.getBackgroundColor());
    	TextView highScoreText = (TextView)findViewById(R.id.highScoreText);
    	
    	StringBuffer scoreBuffer = new StringBuffer();
    	if(highScoreList != null && !highScoreList.isEmpty()){
	    	for(HighScore highScore : highScoreList){
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
    	highScoreText.setTextSize(18);
    	highScoreText.setText(Html.fromHtml(scoreBuffer.toString()));
	}
}