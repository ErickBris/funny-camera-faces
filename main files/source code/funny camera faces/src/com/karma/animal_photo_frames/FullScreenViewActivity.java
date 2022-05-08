package com.karma.animal_photo_frames;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class FullScreenViewActivity extends Activity {
	Bitmap bitmap;
	Global mGlobal;
	InterstitialAd interstitialAds;
	AdRequest adRequest;
	ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fullscreen_image);

		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest1 = new AdRequest.Builder()
		    .build();
		adView.loadAd(adRequest1);
		
		interstitialAds = new InterstitialAd(this);
		interstitialAds.setAdUnitId(getResources().getString(R.string.admob_intersitials));
	     adRequest = new AdRequest.Builder().build();
	     interstitialAds.loadAd(adRequest);
	     interstitialAds.show();
		Intent i = getIntent();

		mGlobal = ((Global) getApplicationContext());
		// Uri targetUri = (Uri)
		// getIntent().getParcelableExtra("UriOfSelectedImage");

		bitmap = mGlobal.getBm1();
		ImageView iv = (ImageView) findViewById(R.id.imgDisplay);

		// Bitmap setimg =
		// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
		// targetUri);
		iv.setImageBitmap(bitmap);

		Button btnShare, btnClose,btnwall;

		btnClose = (Button) findViewById(R.id.btnClose);
		btnShare = (Button) findViewById(R.id.btnShare);
		btnwall= (Button) findViewById(R.id.btnwall);
		btnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String pathofBmp = Images.Media.insertImage(
						getContentResolver(), bitmap, "title", null);
				Uri bmpUri = Uri.parse(pathofBmp);

				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("image/*");
				sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
				startActivity(Intent.createChooser(sharingIntent,
						"Share image using"));
			}
		});
		// close button click event
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				FullScreenViewActivity.this.finish();
			}
		});
		
		btnwall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String off_id="",unm="",pass="";
				
				wall_set task = new wall_set(off_id, unm, pass);
				task.execute();
				
			}
		});

	}

	class wall_set extends AsyncTask<String, String, String> {

		String off_id, unm, pass;

		public wall_set(String office, String user, String pwd) {

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(FullScreenViewActivity.this);
			progress.setMessage("processing...");
			progress.setCancelable(true);
			progress.setIndeterminate(false);
			progress.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				// Toast.makeText(MainActivity.this, "Proccessing...",
				// Toast.LENGTH_LONG).show();
				// br = (BitmapDrawable)i.getDrawable();
				// b=br.getCurrent().;
				
				WallpaperManager wm = WallpaperManager
						.getInstance(FullScreenViewActivity.this);
				wm.setBitmap(bitmap);
				
				runOnUiThread(new  Runnable() {
					public void run() {
						Toast.makeText(FullScreenViewActivity.this,
								"Wallpaper set :)", Toast.LENGTH_SHORT)
								.show();

						Log.i("doinbackground...","exit");
					}
				});
				
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Error in set as wallpaper :(", Toast.LENGTH_SHORT)
						.show();
				Log.e("xml parsing error in login_activity", "" + e);
				// TODO: handle exception
			}

			return null;
		}

		protected void onPostExecute(String result) {
			Log.i("postexec...","enter");
			progress.dismiss();
		};

	}
	
	
	/*
	 * public class FullScreenImageAdapter extends PagerAdapter {
	 * 
	 * private Activity _activity; private ArrayList<String> _imagePaths;
	 * private LayoutInflater inflater;
	 * 
	 * // constructor public FullScreenImageAdapter(Activity activity,
	 * ArrayList<String> imagePaths) { this._activity = activity;
	 * this._imagePaths = imagePaths; }
	 * 
	 * @Override public int getCount() { return this._imagePaths.size(); }
	 * 
	 * @Override public boolean isViewFromObject(View view, Object object) {
	 * return view == ((RelativeLayout) object); }
	 * 
	 * @Override public Object instantiateItem(ViewGroup container, int
	 * position) { final TouchImageView imgDisplay; Button btnClose, btnShare;
	 * 
	 * inflater = (LayoutInflater) _activity
	 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); View viewLayout =
	 * inflater.inflate( R.layout.layout_fullscreen_image, container, false);
	 * 
	 * imgDisplay = (TouchImageView) viewLayout .findViewById(R.id.imgDisplay);
	 * btnClose = (Button) viewLayout.findViewById(R.id.btnClose); btnShare =
	 * (Button) viewLayout.findViewById(R.id.btnShare);
	 * 
	 * BitmapFactory.Options options = new BitmapFactory.Options();
	 * options.inPreferredConfig = Bitmap.Config.ARGB_8888; Bitmap bitmap =
	 * BitmapFactory.decodeFile(_imagePaths.get(position), options);
	 * imgDisplay.setImageBitmap(bitmap);
	 * 
	 * // Share button click event btnShare.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { final Drawable d =
	 * imgDisplay.getDrawable(); final Bitmap bitmap = drawableToBitmap(d);
	 * Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	 * sharingIntent.setType("image/jpeg");
	 * sharingIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(_activity,
	 * bitmap)); startActivity(Intent.createChooser(sharingIntent,
	 * "Share image using")); } }); // close button click event
	 * btnClose.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * _activity.finish(); } });
	 * 
	 * ((ViewPager) container).addView(viewLayout);
	 * 
	 * return viewLayout; }
	 * 
	 * @Override public void destroyItem(ViewGroup container, int position,
	 * Object object) { ((ViewPager) container).removeView((RelativeLayout)
	 * object);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public static Bitmap drawableToBitmap(Drawable drawable) { if (drawable
	 * instanceof BitmapDrawable) { return ((BitmapDrawable)
	 * drawable).getBitmap(); }
	 * 
	 * Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
	 * drawable.getIntrinsicHeight(), Config.ARGB_8888); Canvas canvas = new
	 * Canvas(bitmap); drawable.setBounds(0, 0, canvas.getWidth(),
	 * canvas.getHeight()); drawable.draw(canvas);
	 * 
	 * return bitmap; }
	 * 
	 * public Uri getImageUri(Context inContext, Bitmap inImage) {
	 * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	 * inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes); String path =
	 * Images.Media.insertImage(inContext.getContentResolver(), inImage,
	 * "Title", null); return Uri.parse(path); }
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		show_alert_back("Exit","Are you sure you want to exit Editor ?");
		return super.onKeyDown(keyCode, event);
		
	}
	
	
	
	public void show_alert_back(String title,String msg) {

	  	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	  			FullScreenViewActivity.this);

	  		// set title
	  		alertDialogBuilder.setTitle(title);

	  		// set dialog message
	  		alertDialogBuilder
	  			.setMessage(msg)
	  			.setCancelable(true)
	  			.setIcon(R.drawable.icon_80).setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						
					}
				})
	  			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	  				public void onClick(DialogInterface dialog,int id) {
	  					// if this button is clicked, close
	  					// current activity
	  					finish();
	  					
	  				
	  				}
	  			  });

	  


	  		
	  			// create alert dialog
	  			AlertDialog alertDialog = alertDialogBuilder.create();
	  			alertDialog.setCancelable(false);
	  			// show it
	  			alertDialog.show();
	  			
	  			Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
	  			Button b1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
	  			if(b != null)
	  			        b.setTextColor(Color.BLUE);
	  			if(b1 != null)
	  		        b1.setTextColor(Color.BLUE);
	  }
	
}
