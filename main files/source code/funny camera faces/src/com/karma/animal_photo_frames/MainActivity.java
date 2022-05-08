package com.karma.animal_photo_frames;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	ImageButton mCamera, mEditor, mGallery, share, mRateIt;
	Global global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.activity_main);

		global = ((Global) getApplicationContext());

		mCamera = (ImageButton) findViewById(R.id.camera_ibtn);
		mEditor = (ImageButton) findViewById(R.id.editor_ibtn);
		mGallery = (ImageButton) findViewById(R.id.gallery_ibtn);
		share = (ImageButton) findViewById(R.id.share_ibtn);
		mRateIt = (ImageButton) findViewById(R.id.rate_it_ibtn);

		mCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File f = new File(android.os.Environment
						.getExternalStorageDirectory(), "temp.jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				startActivityForResult(intent, 11);

			}
		});

		mEditor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 0);

			}
		});

//		mGallery.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				
//				Intent i = new Intent(MainActivity.this, FolderViewActivity.class);
//				startActivity(i);
//			
//
//			}
//		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

//				String shareBody=""
				String shareBody =  "http://play.google.com/store/apps/details?id=com.karma.animal_photo_frames";
			    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			        sharingIntent.setType("text/plain");
			        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Animal Photo Frames : - ");
			        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			        startActivity(Intent.createChooser(sharingIntent, shareBody));
			}
		});

		mRateIt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final String appName = getPackageName();
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
				}
				
			
		
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0:

			if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				global.setPath(picturePath);

				global.setBitmap_1(BitmapFactory.decodeFile(picturePath));

				Intent i = new Intent(MainActivity.this, SelectFrameActivity.class);
				startActivity(i);
				finish();
				
			}
			break;

		case 11:

			if (requestCode == 11 && resultCode == RESULT_OK) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}

				global.setPath(f.getAbsolutePath());
				global.setBitmap_1(BitmapFactory.decodeFile(f.getAbsolutePath()));

				Intent i = new Intent(MainActivity.this, SelectFrameActivity.class);
				startActivity(i);
				finish();
				
			}
			break;
		}

		

	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
