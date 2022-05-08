package com.karma.animal_photo_frames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SelectedImageActivity extends Activity implements OnTouchListener {

	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	Matrix matrix1 = new Matrix();
	Matrix savedMatrix1 = new Matrix();

	String mImagename;

	File file;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	float[] lastEvent = null;
	float d = 0f;
	float newRot = 0f;
	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	ImageView iv_new;
	ImageView iv_frame;
//	ImageButton mRightRotation;
//	ImageButton mLeftRotation;
	ImageButton mSelectFrame;
	ImageButton home;
	ImageButton mOk;
	ImageButton mEffect;
	ImageButton mShare;
	LinearLayout main_layout;
	RelativeLayout ll1;
	InterstitialAd interstitialAds;
	AdRequest adRequest;
	Global global;
	Bitmap m_bitmap1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.activity_selected_image);
		

//		AdView adView = (AdView)this.findViewById(R.id.adView);
//		AdRequest adRequest1 = new AdRequest.Builder()
//		    .build();
//		adView.loadAd(adRequest1);
		
		interstitialAds = new InterstitialAd(this);
		interstitialAds.setAdUnitId(getResources().getString(R.string.admob_intersitials));
	     adRequest = new AdRequest.Builder().build();
	     interstitialAds.loadAd(adRequest);
	     interstitialAds.show();
	     main_layout=(LinearLayout)findViewById(R.id.main_layout);
		global = ((Global) getApplicationContext());

		iv_new = (ImageView) findViewById(R.id.iv_1);
		iv_frame = (ImageView) findViewById(R.id.main_img);
		home=(ImageButton) findViewById(R.id.home);
		
		global.setIsFrameselectButton(false);
		mSelectFrame = (ImageButton) findViewById(R.id.selectframe_ibtn);
//		mLeftRotation = (ImageButton) findViewById(R.id.leftRotation_ibtn);
//		mRightRotation = (ImageButton) findViewById(R.id.rightRotation_ibtn);
		mOk = (ImageButton) findViewById(R.id.ok_ibtn);
		mEffect = (ImageButton) findViewById(R.id.effect_ibtn);

		mShare = (ImageButton) findViewById(R.id.share_ibtn);
		ll1 = (RelativeLayout) findViewById(R.id.ll1);

		Intent i = getIntent();
		int imgid = i.getIntExtra("img_id", 0);

		iv_frame = (ImageView) findViewById(R.id.main_img);

		iv_frame.setBackgroundResource(imgid);

		// m_bitmap1 = BitmapFactory.decodeFile(global.getPath());

		m_bitmap1 = global.getBitmap_1();
		Matrix mat = new Matrix();

		Bitmap bMapRotate = Bitmap.createBitmap(m_bitmap1, 0, 0,
				m_bitmap1.getWidth(), m_bitmap1.getHeight(), mat, true);
		iv_new.setImageBitmap(bMapRotate);
		iv_new.setOnTouchListener(this);

		mSelectFrame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 interstitialAds.show();
				global.setIsFrameselectButton(true);
				
				onBackPressed();
			}
		});
		
 


		

		/*mLeftRotation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 interstitialAds.show();
				matrix.postRotate(-12, iv_new.getMeasuredWidth() / 2,
						iv_new.getMeasuredHeight() / 2);
				iv_new.setImageMatrix(matrix);

			}
		});

		mRightRotation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				matrix.postRotate(12, iv_new.getMeasuredWidth() / 2,
						iv_new.getMeasuredHeight() / 2);
				iv_new.setImageMatrix(matrix);
			}
		});*/

		mOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 interstitialAds.show();
				File filename = captureImage();
				global.setFile(filename);
/*
				Intent i = new Intent(SelectedImageActivity.this, FolderViewActivity.class);
				startActivity(i);
*/			

				 interstitialAds.show();
				Intent i = new Intent(SelectedImageActivity.this, FullScreenViewActivity.class);
				SelectedImageActivity.this.startActivity(i);
				
				finish();
				
			}
		});

		mShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 interstitialAds.show();
				File filename = captureImage();

				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				Uri screenshotUri = Uri.fromFile(filename);
				sharingIntent.setType("image/*");
				sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
				startActivity(Intent.createChooser(sharingIntent,
						"Share image using"));

			}
		});

		
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		mEffect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 interstitialAds.show();
				LaunchInstaFiverr(Uri.parse(new File(global.getPath())
						.toString()));

			}
		});

	}
	/** Determine the degree between the first two fingers */
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		Log.d("Rotation ~~~~~~~~~~~~~~~~~",
				delta_x + " ## " + delta_y + " ## " + radians + " ## " + Math.toDegrees(radians));
		return (float) Math.toDegrees(radians);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {


		ImageView view = (ImageView) v;

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			lastEvent = null;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			savedMatrix.set(matrix);
			midPoint(mid, event);
			mode = ZOOM;

			lastEvent = new float[4];
			lastEvent[0] = event.getX(0);
			lastEvent[1] = event.getX(1);
			lastEvent[2] = event.getY(0);
			lastEvent[3] = event.getY(1);
			d = rotation(event);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			lastEvent = null;

			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM && event.getPointerCount() == 2) {
				float newDist = spacing(event);

				matrix.set(savedMatrix);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
				if (lastEvent != null) {
					newRot = rotation(event);

					float r = newRot - d;
					matrix.postRotate(r, iv_new.getMeasuredWidth() / 2, iv_new.getMeasuredHeight() / 2);
				}
			}
			break;
		}

		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!global.getIsFrameselectButton()){
			 interstitialAds.show();
		Intent i = new Intent(SelectedImageActivity.this, MainActivity.class);
		startActivity(i);
		
		finish();
		}
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// output image path
				Uri mImageUri = data.getData();

				// Appdata.uri = mImageUri;
				// new File(mImageUri.getPath());

				try {

					m_bitmap1 = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), mImageUri);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// m_bitmap1 = BitmapFactory.decodeFile(mImageUri);
				Matrix mat = new Matrix();

				Bitmap bMapRotate = Bitmap.createBitmap(m_bitmap1, 0, 0,
						m_bitmap1.getWidth(), m_bitmap1.getHeight(), mat, true);
				global.setBitmap_1(bMapRotate);

				iv_new.setImageBitmap(bMapRotate);

				break;
			}

		}

	}

	public void LaunchInstaFiverr(Uri uri) {

		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(uri);
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
				"670de73f38b6ff0a");
		startActivityForResult(newIntent, 1);
	}

	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		ImageView view = (ImageView) v;

		if (view == iv_new) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);

				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					// ...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);

				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			view.setImageMatrix(matrix);

		}

		return true;
	}*/

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private File captureImage() {
		// TODO Auto-generated method stub
		OutputStream output;

		Calendar cal = Calendar.getInstance();

		Bitmap bitmap = Bitmap.createBitmap(ll1.getWidth(), ll1.getHeight(),
				Config.ARGB_8888);

		bitmap = ThumbnailUtils.extractThumbnail(bitmap, ll1.getWidth(),
				ll1.getHeight());

		Canvas b = new Canvas(bitmap);
		ll1.draw(b);

		
		global.setBm1(bitmap);
		
		// Find the SD Card path
		File filepath = Environment.getExternalStorageDirectory();

		// Create a new folder in SD Card
		File dir = new File(filepath.getAbsolutePath() + "/Funny face Cams/");
		dir.mkdirs();

		mImagename = "image" + cal.getTimeInMillis() + ".png";

		// Create a name for the saved image
		file = new File(dir, mImagename);

		// Show a toast message on successful save
		Toast.makeText(SelectedImageActivity.this, "Image Saved to SD Card",
				Toast.LENGTH_SHORT).show();
		
		ContentValues values = new ContentValues();
	    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // setar isso
	    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		        // to notify change         
		     this.getContentResolver().notifyChange(
		        Uri.parse("file://" + dir.getPath()),null);
		
		
		try {

			output = new FileOutputStream(file);
			// Compress into png format image from 0% - 100%
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			output.flush();
			output.close();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;

	}

}
