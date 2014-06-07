package com.sl.clicket;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    showAd(R.layout.home, R.id.top_ad, LayoutInflater.from(this), (LinearLayout)findViewById(R.id.linearLayout2), savedInstanceState);
	    showAd(R.layout.home, R.id.bottom_ad, LayoutInflater.from(this), (LinearLayout)findViewById(R.id.linearLayout2), savedInstanceState);
	    setContentView(R.layout.home);
	    
	}

	public void play(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	
	
	public static View showAd(int layoutId, int adId, LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);
//        final TextView mAdStatus = (TextView) v.findViewById(R.id.status);
        AdView mAdView = (AdView) v.findViewById(adId);
        mAdView.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub
//				mAdStatus.setText("onReceiveAd");
			}
			
			@Override
			public void onPresentScreen(Ad arg0) {
				// TODO Auto-generated method stub
//				mAdStatus.setText("onPresentScreen");
			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {
				// TODO Auto-generated method stub
//				 mAdStatus.setText("onLeaveApplication");
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
//				 mAdStatus.setText("error_receive_ad");

			}
			
			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub
//				mAdStatus.setText("onDismissScreen");
			}
		});

        AdRequest adRequest = new AdRequest();
//        adRequest.addKeyword("sporting goods");
//        adRequest.setTesting(true);
        mAdView.loadAd(adRequest);
        return v;
   }
	
}
