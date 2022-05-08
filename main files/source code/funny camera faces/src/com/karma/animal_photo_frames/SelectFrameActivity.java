package com.karma.animal_photo_frames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SelectFrameActivity extends Activity {

	GridView grid;
	Integer[] Frame_id;
	Integer[] Frame_id_new;

	Adapter_grid adapter;
	InterstitialAd interstitialAds;
	AdRequest adRequest;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		
		Frame_id = new Integer[] { R.drawable.frame_1, R.drawable.frame_2,
				R.drawable.frame_3, R.drawable.frame_4, R.drawable.frame_5,
				R.drawable.frame_6, R.drawable.frame_7, R.drawable.frame_8,
				R.drawable.frame_9, R.drawable.frame_10, R.drawable.frame_11,
				R.drawable.frame_12, R.drawable.frame_13, R.drawable.frame_14,
				R.drawable.frame_15,};

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.activity_select_frame);

		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest1 = new AdRequest.Builder()
		    .build();
		adView.loadAd(adRequest1);
		
		interstitialAds = new InterstitialAd(this);
		interstitialAds.setAdUnitId(getResources().getString(R.string.admob_intersitials));
	     adRequest = new AdRequest.Builder().build();
	     interstitialAds.loadAd(adRequest);
	     interstitialAds.show();
	     
		grid = (GridView) findViewById(R.id.gridView1);
		adapter = new Adapter_grid(getApplicationContext(), Frame_id);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(SelectFrameActivity.this,
						SelectedImageActivity.class);
				
				i.putExtra("img_id", Frame_id[arg2]);
//				 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
//				finish();

			}
		});
	}

}
