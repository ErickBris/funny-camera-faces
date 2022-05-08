package com.karma.animal_photo_frames;

import java.io.File;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Global extends Application {

	private String path= "";
	private File file;
	private Bitmap bitmap_1;
	
	private Boolean isFrameselectButton = false;
	
	private Bitmap bm1;

	
	
	public Boolean getIsFrameselectButton() {
		return isFrameselectButton;
	}
	public void setIsFrameselectButton(Boolean isFrameselectButton) {
		this.isFrameselectButton = isFrameselectButton;
	}
	public Bitmap getBm1() {
		return bm1;
	}

	public void setBm1(Bitmap bm1) {
		this.bm1 = bm1;
	}
	
	public Bitmap getBitmap_1() {
		return bitmap_1;
	}
	public void setBitmap_1(Bitmap bitmap_1) {
		this.bitmap_1 = bitmap_1;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	
}
