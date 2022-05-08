package com.karma.animal_photo_frames;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
public class FolderViewActivity extends Activity {
		File [] mediaFiles;
		File imageDir;
		static GridView gridView;
		FolderViewImageAdapter adapter;
		Intent in;
		ImageButton btncam;
		private Utils utils;
		
		private int columnWidth;
		String name = null;
		ArrayList<Bitmap> bmpArray = new ArrayList<Bitmap>();
		ArrayList<String> fileName = new ArrayList<String>();
		public static final String TAG = "Album3Activity";





public void onCreate(Bundle savedInstanceState) 
{
    imageDir = new File(Environment.getExternalStorageDirectory().toString()+
             "/LoveFrames");
	utils = new Utils(this);

    
    super.onCreate(savedInstanceState);
    if((imageDir.exists()))
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grid_view);
        mediaFiles = imageDir.listFiles();
        //Log.d("Length of images",""+mediaFiles.length);
        for(File file : mediaFiles)
        {
            bmpArray.add(convertToBitmap(file));
            fileName.add(readFileName(file));
            //Log.d(TAG + "bmpArray Size", ""+bmpArray.size());
            //Log.d(TAG, "call to convertToBitmap");
        }//for

        adapter = new FolderViewImageAdapter(this, bmpArray, fileName);
        gridView = (GridView)findViewById(R.id.grid_view);
        
        InitilizeGridLayout();
        
        gridView.setAdapter(adapter);
     /*   sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED
                , Uri.parse(Environment.getExternalStorageDirectory().toString()+"/	Intent i = new Intent(CaptureActivity.this, FolderViewActivity.class);
				startActivity(i);
			")));
*/
/*        gridView.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                long arg3) {
        	
        	
        	
        	
        	
            in = new Intent(getApplicationContext(), FullScreen.class);
            in.putExtra("id", position);
            startActivity(in);
        }
    });*/
    }//if
    else
    {
    	
    	Toast.makeText(FolderViewActivity.this, ""+"File Not Found", Toast.LENGTH_SHORT);
    	
    	return;
       // setContentView(R.layout.no_media);
    }
}//onCreate

static Bitmap bmp = null;

public static Bitmap convertToBitmap(File file) 
{
    URL url = null;
    try 
    {
        url = file.toURL();
    } catch (MalformedURLException e1) 
    {
        //Log.d(TAG, e1.toString());
    }//catch

  //  Bitmap bmp = null;
    try
    {
       bmp = BitmapFactory.decodeStream(url.openStream());
      // bmp.recycle();
    }catch(Exception e)
    {
        //Log.d(TAG, "Exception: "+e.toString());
    }//catch
    return bmp;
}//convertToBitmap




private void InitilizeGridLayout() {
	Resources r = getResources();
	float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			AppConstant.GRID_PADDING, r.getDisplayMetrics());

	columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

	gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
	gridView.setColumnWidth(columnWidth);
	gridView.setStretchMode(GridView.NO_STRETCH);
	gridView.setPadding((int) padding, (int) padding, (int) padding,
			(int) padding);
	gridView.setHorizontalSpacing((int) padding);
	gridView.setVerticalSpacing((int) padding);
}


public String readFileName(File file){
    String name = file.getName();
    return name;
}//readFileName
}//class