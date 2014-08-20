package com.example.njupt.zhb.imageprocesssystem;
import Jama.Matrix;
import android.graphics.Bitmap;
/*
作者：郑海波 2012-08-12 Email:zhb931706659@126.com
*/
public class ImageProcess {
	
	public ImageProcess() {
		// TODO Auto-generated constructor stub
	}
	private Matrix getDataR(int[] pix,int width,int height){
    	Matrix dataR=new Matrix(width,height,0.0);
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = ((pix[index] >> 16) & 0xff);
      			dataR.set(x, y, r);
    			index++;
    		} // x
    	} // y
    	return dataR;
	}
	private Matrix getDataG(int[] pix,int width,int height){
    	Matrix dataG=new Matrix(width,height,0.0);
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int g = ((pix[index] >> 8) & 0xff);
      			dataG.set(x, y, g);
    			index++;
    		} // x
    	} // y
    	return dataG;
	}
	private Matrix getDataB(int[] pix,int width,int height){
    	Matrix dataB=new Matrix(width,height,0.0);
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int b =(pix[index] & 0xff);
      			dataB.set(x, y, b);
    			index++;
    		} // x
    	} // y
    	return dataB;
	}
	private Matrix getDataGray(int[] pix,int width,int height){
    	Matrix dataGray=new Matrix(width,height,0.0);
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = ((pix[index] >> 16) & 0xff);
    			int g = ((pix[index] >> 8) & 0xff);
    			int b = (pix[index] & 0xff);
    			int gray=(int)(0.3*r+0.59*g+0.11*b);
      			dataGray.set(x, y, gray);
      			index++;
    		} // x
    	} // y
    	return dataGray;
	}
	private Bitmap makeToBitmap(Matrix dataR,Matrix dataG,Matrix dataB,int width,int height) {
		
		int[] pix = new int[width * height];
		int index=0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			pix[index] = 0xff000000 | ((int)dataR.get(x, y) << 16) | ((int)dataG.get(x, y) << 8) | (int)dataB.get(x, y);
    			index++;
    		} // x
    	} // y
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	pix = null;
    	return bitmap;
	}
	public Bitmap brighten(int brightenOffset,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = (pix[index] >> 16) & 0xff;
    			int g = (pix[index] >> 8) & 0xff;
    			int b = pix[index] & 0xff;
    			r = Math.max(0, Math.min(255, r + brightenOffset));
    			g = Math.max(0, Math.min(255, g + brightenOffset));
    			b = Math.max(0, Math.min(255, b + brightenOffset));
    			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
    }
    
    // filterWidth and filterHeight must be odd numbers
    public Bitmap averageFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int filterHalfWidth = filterWidth/2;
    	int filterHalfHeight = filterHeight/2;
    	int filterArea = filterWidth * filterHeight;
    	for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
    	{
    		for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
    		{
    			// Accumulate values in neighborhood
    			int accumR = 0, accumG = 0, accumB = 0;
    			for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
    			{
    				for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
    				{
    					int index = (y+dy)*width + (x+dx);
    					accumR += (pixOld[index] >> 16) & 0xff;
    					accumG += (pixOld[index] >> 8) & 0xff;
    					accumB += pixOld[index] & 0xff;
    				} // dx
    			} // dy
    			
    			// Normalize
    			accumR /= filterArea;
    			accumG /= filterArea;
    			accumB /= filterArea;
    			int index = y*width + x;
    			pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
    }
    
    // filterWidth and filterHeight must be odd numbers
    public Bitmap medianFilter(int filterWidth, int filterHeight,Bitmap myBitmap)
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pixNew = new int[width * height];
    	int[] pixOld = new int[width * height];
    	myBitmap.getPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap.getPixels(pixOld, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int filterHalfWidth = filterWidth/2;
    	int filterHalfHeight = filterHeight/2;
    	int filterArea = filterWidth * filterHeight;
    	for (int y = filterHalfHeight; y < height-filterHalfHeight; y++)
    	{
    		for (int x = filterHalfWidth; x < width-filterHalfWidth; x++)
    		{
    			// Accumulate values in neighborhood
    			int accumR = 0, accumG = 0, accumB = 0;
    			for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++)
    			{
    				for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++)
    				{
    					int index = (y+dy)*width + (x+dx);
    					accumR += (pixOld[index] >> 16) & 0xff;
    					accumG += (pixOld[index] >> 8) & 0xff;
    					accumB += pixOld[index] & 0xff;
    				} // dx
    			} // dy
    			
    			// Normalize
    			accumR /= filterArea;
    			accumG /= filterArea;
    			accumB /= filterArea;
    			int index = y*width + x;
    			pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
    	myBitmap = null;
    	pixOld = null;
    	pixNew = null;
    	return bitmap;
    }
    public Bitmap ColorToGray(Bitmap myBitmap){
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = (pix[index] >> 16) & 0xff;
    			int g = (pix[index] >> 8) & 0xff;
    			int b = pix[index] & 0xff;
    			int gray=(int)(0.3*r+0.59*g+0.11*b);
    			r=gray;
    			g=gray;
    			b=gray;
      			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
    }
    public Bitmap ColorToBinary(Bitmap myBitmap){
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = (pix[index] >> 16) & 0xff;
    			int g = (pix[index] >> 8) & 0xff;
    			int b = pix[index] & 0xff;
    			int gray=(int)(0.3*r+0.59*g+0.11*b);
    			if (gray>=128) {
					gray=255;
				}else{
					gray=0;
				}
    			r=gray;
    			g=gray;
    			b=gray;
      			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
    }
    public Bitmap BitReverse(Bitmap myBitmap){
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = 255-((pix[index] >> 16) & 0xff);
    			int g = 255-((pix[index] >> 8) & 0xff);
    			int b = 255-(pix[index] & 0xff);
      			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
    }
    /*
     *旋转90度
     *2012-08-29
     */
	public Bitmap rotate90(Bitmap myBitmap) {
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	//Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	Matrix dataRT=new Matrix(height,width,0.0);
    	Matrix dataGT=new Matrix(height,width,0.0);
    	Matrix dataBT=new Matrix(height,width,0.0);
    	dataRT=dataR.transpose();
    	dataGT=dataG.transpose();
    	dataBT=dataB.transpose();
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataRT, dataGT, dataBT, height, width);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	/*
	 *直方图均衡化 
	 */
	public Bitmap histEqualize(Bitmap myBitmap){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	//Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataR=eachEqualize(dataR,width,height);
    	dataG=eachEqualize(dataG,width,height);
    	dataB=eachEqualize(dataB,width,height);
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachEqualize(Matrix temp,int width,int height){
		// 灰度映射表
		int	bMap[]=new int[256];
		// 灰度映射表
		int lCount[]=new int[256];
		// 重置计数为0
		int i,j;
		for (i = 0; i < 256; i ++){
			// 清零
			lCount[i] = 0;
		}
		// 计算各个灰度值的计数 - 参考灰度直方图的绘制代码 (对话框类中)
		for (i = 0; i < width; i ++){
			for (j = 0; j < height; j ++){
					lCount[(int)temp.get(i, j)]++;  // 计数加1
			}
		}
		// 计算灰度映射表
		for (i = 0; i < 256; i++){
			// 初始为0
			int Temp = 0;
			for (j = 0; j <= i ; j++){
				Temp += lCount[j];
			}
			// 计算对应的新灰度值
			bMap[i] = (int) (Temp * 255 / height / width);
		}
		// 每行
		for (i = 0; i < width; i++){
			// 每列
			for (j = 0; j < height; j++){
					temp.set(i, j, bMap[(int)temp.get(i,j)]);
			}
		}
		return temp; 
	}
	/*
	 * Robert算子梯度
	 * 
	 */
	public Bitmap RobertGradient(Bitmap myBitmap){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	//Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataR=eachRobertGradient(dataR,width,height);
    	dataG=eachRobertGradient(dataG,width,height);
    	dataB=eachRobertGradient(dataB,width,height);
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachRobertGradient(Matrix tempM,int width,int height){
		int i,j;
		for(i=0;i<width-1;i++){
			for(j=0;j<height-1;j++){
				int temp=Math.abs((int)tempM.get(i, j)-(int)tempM.get(i,j+1))
						 +Math.abs((int)tempM.get(i+1,j)-(int)tempM.get(i,j+1));
				tempM.set(i, j, temp);
			}
		}
		return tempM;
	}
	/*
	 *Sobel算子锐化 
	 */
	public Bitmap SobelGradient(Bitmap myBitmap){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataGray=eachSobelGradient(dataGray, width, height);
    	dataR=dataGray.copy();
    	dataG=dataGray.copy();
    	dataB=dataGray.copy();
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachSobelGradient(Matrix tempM,int width,int height){
		int i,j;
		Matrix resultMatrix=tempM.copy();
		for(i=1;i<width-1;i++){
			for(j=1;j<height-1;j++){
				int temp1=Math.abs(((int)tempM.get(i+1, j-1)+2*(int)tempM.get(i+1, j)+(int)tempM.get(i+1,j+1))
						 -(((int)tempM.get(i-1,j-1)+2*(int)tempM.get(i-1,j)+(int)tempM.get(i-1,j-1))));
				int temp2=Math.abs(((int)tempM.get(i-1, j+1)+2*(int)tempM.get(i, j+1)+(int)tempM.get(i+1,j+1))
						 -(((int)tempM.get(i-1,j-1)+2*(int)tempM.get(i,j-1)+(int)tempM.get(i+1,j-1))));
				int temp=temp1+temp2;
				resultMatrix.set(i, j, temp);
			}
		}
		return resultMatrix;
	}
	/*
	 *Laplace 锐化 
	 */
	public Bitmap LaplaceGradient(Bitmap myBitmap){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataGray=eachLaplaceGradient(dataGray,width,height);
    	dataR=dataGray.copy();
    	dataG=dataGray.copy();
    	dataB=dataGray.copy();
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachLaplaceGradient(Matrix tempM,int width,int height){
		int i,j;
		Matrix resultMatrix=tempM.copy();
		for(i=1;i<width-1;i++){
			for(j=1;j<height-1;j++){
				int temp=Math.abs(5*(int)tempM.get(i, j)-(int)tempM.get(i+1,j)
						-(int)tempM.get(i-1,j)-(int)tempM.get(i,j+1)-(int)tempM.get(i,j-1));
				resultMatrix.set(i, j, temp);
			}
		}
		return resultMatrix;
	}
	public Bitmap Histograms(Bitmap myBitmap){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataGray=eachHistograms(dataGray,width,height);
    	dataR=dataGray.copy();
    	dataG=dataGray.copy();
    	dataB=dataGray.copy();
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachHistograms(Matrix data,int width,int height){
		Matrix dataBT=new Matrix(width,height,255);
		int map[]=new int[256];
		for (int i = 0; i < map.length; i++) {
			map[i]=0;
		}
		for(int i=0;i<width;i++){
			for (int j = 0; j < height; j++) {
				map[(int)data.get(i, j)]++;
			}
		}
		int max_map=0;
		for (int i = 0; i < map.length; i++) {
			if(map[i]>max_map){
				max_map=map[i];
			}
		}
		for (int i = 0; i < map.length; i++) {
			map[i]=(int)(map[i]*(height-10)/max_map);
		}
		for(int j=0;j<256;j++){
			for(int i=height-1;i>=height-1-map[j];i--){
				dataBT.set(j,i,0);
			}
		}
		return dataBT;
	}
	/*
	 *Sobel算子锐化 
	 */
	public Bitmap SobelGradientBlue(Bitmap myBitmap,int Threshold){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataGray=eachSobelGradientBlue(dataGray, width, height);
    	for(int i=0;i<width;i++){
    		for (int j = 0; j < height; j++) {
				if (dataGray.get(i, j)>Threshold) {
					dataB.set(i, j, 255);
					dataR.set(i, j, 0);
					dataG.set(i, j, 0);
				}else {
					dataB.set(i, j, 255);
					dataR.set(i, j, 255);
					dataG.set(i, j, 255);
				}
			}
    	}
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachSobelGradientBlue(Matrix tempM,int width,int height){
		int i,j;
		Matrix resultMatrix=tempM.copy();
		for(i=1;i<width-1;i++){
			for(j=1;j<height-1;j++){
				int temp1=Math.abs(((int)tempM.get(i+1, j-1)+2*(int)tempM.get(i+1, j)+(int)tempM.get(i+1,j+1))
						 -(((int)tempM.get(i-1,j-1)+2*(int)tempM.get(i-1,j)+(int)tempM.get(i-1,j-1))));
				int temp2=Math.abs(((int)tempM.get(i-1, j+1)+2*(int)tempM.get(i, j+1)+(int)tempM.get(i+1,j+1))
						 -(((int)tempM.get(i-1,j-1)+2*(int)tempM.get(i,j-1)+(int)tempM.get(i+1,j-1))));
				int temp=temp1+temp2;
				resultMatrix.set(i, j, temp);
			}
		}
		return resultMatrix;
	}
	public Bitmap LaplaceGradientBlue(Bitmap myBitmap,int Threshold){
		// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	Matrix dataR=getDataR(pix, width, height);
    	Matrix dataG=getDataG(pix, width, height);
    	Matrix dataB=getDataB(pix, width, height);
    	Matrix dataGray=getDataGray(pix, width, height);
    	/////////////////////////////////////////////////////////
    	dataGray=eachLaplaceGradientBlue(dataGray, width, height);
    	for(int i=0;i<width;i++){
    		for (int j = 0; j < height; j++) {
				if (dataGray.get(i, j)>Threshold) {
					dataB.set(i, j, 255);
					dataR.set(i, j, 0);
					dataG.set(i, j, 0);
				}else {
					dataB.set(i, j, 255);
					dataR.set(i, j, 255);
					dataG.set(i, j, 255);
				}
			}
    	}
    	///////////////////////////////////////////////////////////////
    	// Change bitmap to use new array
    	Bitmap bitmap=makeToBitmap(dataR, dataG, dataB, width, height);	
    	myBitmap = null;
    	pix = null;
    	return bitmap;
	}
	private Matrix eachLaplaceGradientBlue(Matrix tempM,int width,int height){
		int i,j;
		Matrix resultMatrix=tempM.copy();
		for(i=1;i<width-1;i++){
			for(j=1;j<height-1;j++){
				int temp=Math.abs(4*(int)tempM.get(i, j)-(int)tempM.get(i+1,j)
						-(int)tempM.get(i-1,j)-(int)tempM.get(i,j+1)-(int)tempM.get(i,j-1));
				resultMatrix.set(i, j, temp);
			}
		}
		return resultMatrix;
	}
}
