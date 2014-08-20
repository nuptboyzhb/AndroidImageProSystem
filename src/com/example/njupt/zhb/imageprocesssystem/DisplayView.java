package com.example.njupt.zhb.imageprocesssystem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
public class DisplayView extends View {    
	public Bitmap bitmap1=null;
	public Bitmap bitmap2=null;
	public int TopInterval=10;
	public Rect BMPRect1,BMPRect2;
	public DisplayView(Context context){
		super(context);
	}
	/*一定要重写这个构造方法*/
    public DisplayView(Context context,AttributeSet attrs) {
    	super(context,attrs);
    }
    /*重写onDraw()*/   
    @Override   
    protected void onDraw(Canvas canvas)    
    {       
         super.onDraw(canvas);
         Paint paint=new Paint();    
         /*去锯齿*/   
         paint.setAntiAlias(true);    
         /*设置paint的颜色*/   
         paint.setColor(Color.RED);    
         /*设置paint的 style 为STROKE：空心*/   
         paint.setStyle(Paint.Style.STROKE);    
         /*设置paint的外框宽度*/   
         paint.setStrokeWidth(1); 
         ////////////////////////////////////////////////
         int width=getWidth();
         int height=getHeight()-TopInterval;
         int myInterval=(int)(width-height*2)/3;
         int x1=myInterval,y1=TopInterval,x2=myInterval+height,y2=height;
         int x3=myInterval*2+height,y3=TopInterval,x4=(myInterval+height)*2,y4=y2;
         BMPRect1=new Rect(x1,y1,x2,y2);
         BMPRect2=new Rect(x3,y3,x4,y4);
         ///////////////////////////////////////////////
         /*画位图1*/
         if (bitmap1!=null) {
        	 canvas.drawBitmap(bitmap1, null, BMPRect1, null);
		}else{
			paint.setTextSize(18);    
            canvas.drawText("源图像区域", x1, height/2, paint);  
		}
         /*画位图2*/
         if(bitmap2!=null){
             canvas.drawBitmap(bitmap2, null, BMPRect2, null);
         }else{
 			 paint.setTextSize(18);    
             canvas.drawText("处理后的图像区域",x3, height/2, paint);
         }
    }  
    
} 