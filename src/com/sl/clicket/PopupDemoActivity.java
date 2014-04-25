package com.sl.clicket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;



public class PopupDemoActivity extends Activity {
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button popupButton, insidePopupButton;
    TextView popupText;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        popupInit();
    }
 
    public void init() {
        popupButton = (Button) findViewById(9999);
        popupText = new TextView(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        insidePopupButton.setText("OK");
        popupText.setText("This is Popup Window.press OK to dismiss         it.");
        popupText.setPadding(0, 0, 0, 20);
        layoutOfPopup.setOrientation(1);
        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(insidePopupButton);
    }
 
    public void popupInit() {
    	popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
        
    	/*popupButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				try { 
					// We need to get the instance of the LayoutInflater 
					LayoutInflater inflater = (LayoutInflater) PopupDemoActivity.this 
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
					View layout = inflater.inflate(R.layout.popupdemo,(ViewGroup)
	
					findViewById(R.id.popup_element)); 
					PopupWindow pwindo = new PopupWindow(layout, 350, 350, true); 
					pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
	
	//				btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup); 
	//				btnClosePopup.setOnClickListener(cancel_button_click_listener);
	
					
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
			
//				popupMessage.showAsDropDown(popupButton, 0, 0);
			}
    	});*/
        
    	insidePopupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMessage.dismiss();
			}
		});
    }
 
 /*   @Override
    public void onClick(View v) {
 
        if (v.getId() == 999) {
            popupMessage.showAsDropDown(popupButton, 0, 0);
        }
 
        else {
            popupMessage.dismiss();
        }
    }*/
}


