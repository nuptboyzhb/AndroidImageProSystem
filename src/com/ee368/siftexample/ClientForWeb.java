package com.ee368.siftexample;
/*
 *@author: ZhengHaibo  
 *web:     blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2012-9-3  Nanjing njupt
 *Reference:
 *EE368 Digital Image Processing
 *Android Tutorial #3: Server-Client Communication
 *Author: Derek Pang (dcypang@stanford.edu), David Chen (dmchen@stanford.edu)
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
public class ClientForWeb{
	private static final String TAG = "SIFTExampleActivity";
	/** PLEASE PUT YOUR SERVER URL **/
	public final static String INPUT_IMG_FILENAME = "/temp.jpg"; //name for storing image captured by camera view
	public ClientForWeb(Activity pActivity){
		Log.e("msg","ClientForWeb ok!");
	}
    //store the image as a jpeg image
    public  boolean compressByteImage(Bitmap myImage,
  			int quality) {
    	Log.e("msg","begin compressByteImage......");
    	File sdCard = Environment.getExternalStorageDirectory();
  		FileOutputStream fileOutputStream = null;
  		try {  			 			
  			BitmapFactory.Options options=new BitmapFactory.Options();
  			options.inSampleSize = 1;  	//no downsampling		 			
  			fileOutputStream = new FileOutputStream(
  					sdCard.toString() +INPUT_IMG_FILENAME);							
 
  			BufferedOutputStream bos = new BufferedOutputStream(
  					fileOutputStream);
  			
  			//compress image to jpeg
  			myImage.compress(CompressFormat.JPEG, quality, bos);

  			bos.flush();
  			bos.close();  			
  			fileOutputStream.close();  			

  		} catch (FileNotFoundException e) {
   			Log.e(TAG, "FileNotFoundException");
  			e.printStackTrace();
  		} catch (IOException e) {
  			Log.e(TAG, "IOException");
  			e.printStackTrace();
  		}
  		return true;
  	}	
}
