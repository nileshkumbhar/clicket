package com.sl.clicket;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

public class InstructionsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.instructions);

	    findViewById(R.id.instructionsTextParent).setBackgroundColor(MainActivity.THEME.getBackgroundColor());
	    StringBuffer instructions = new StringBuffer();
        instructions.append("&#8226; This is a 15 square puzzle game.<BR /><BR />");
        instructions.append("&#8226; Buttons are shuffled across the panel.<BR /><BR />");
        instructions.append("&#8226; Click the button to move it to the next empty space.<BR /><BR />");
        instructions.append("&#8226; Rearrange all the buttons in a sequence before health is finished. <BR /><BR />");
        instructions.append("&#8226; Try to stay away from RED buttons. They will eat some of your health when you click them. <BR /><BR />");
        instructions.append("&#8226; GREEN buttons are your friends. They will give you some extra health when you click them.<BR/>");
        
        ScrollView instructionsText = (ScrollView)findViewById(R.id.instructionsText);
        TextView instructionsTextView = new TextView(this);
        instructionsTextView.setTextSize(18);
        instructionsTextView.setText(Html.fromHtml(instructions.toString()));
        instructionsText.addView(instructionsTextView);
	}
}
