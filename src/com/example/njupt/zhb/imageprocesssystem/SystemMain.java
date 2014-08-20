package com.example.njupt.zhb.imageprocesssystem;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
/*
 作者：郑海波 2012-08-12 Email:zhb931706659@126.com
 */
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.ee368.siftexample.ClientForWeb;

/*
 作者：郑海波 2012-08-12 Email:zhb931706659@126.com
 */
public class SystemMain extends Activity implements OnTouchListener {
	private Button selectImgBtn;
	private Button selectAlgBtn;
	private Button aboutBtn;
	private Button saveBtn;
	private Button shotScrBtn;
	private Button cameraBtn;
	private Button sendBtn;
	private TextView filepathView;
	private TextView aboutView;
	private LinkTextView linkTextView;
	private OnClickListener seleImgBtnListener = null;
	private OnClickListener seleAlgBtnListener = null;
	private OnClickListener aboutBtnListener = null;
	private OnClickListener saveBtnListener = null;
	private OnClickListener shotScrBtnListener = null;
	private OnClickListener cameraBtnListener = null;
	private OnClickListener sendBtnListener = null;
	private static int RESULT_LOAD_IMAGE = 123;
	private static int SystemCapture = 125;
	private String picturePath = null;
	private Bitmap myBitmap;
	private Bitmap displayBitmap;
	private Bitmap preBitmap;
	public Bitmap resultForWebImage;
	private DisplayView myImageView;
	private ImageProcess myImageProcess = new ImageProcess();
	private ClientForWeb mClientForWeb;
	private static final String DYNAMICACTION_Broadcast = "Broadcast.njupt.zhb.selectAlg";
	private static final String DYNAMICACTION_Broadcast_WEB = "Broadcast.njupt.zhb.webselectAlg";
	public String SERVERURL = "http://10.10.145.154/WebForDCT/ImageDCT.php";
	private static final String TEMP_WEB_IMAGE_PATH = "/sdcard/app/tmp/";
	public boolean mCameraReadyFlag = false;
	public boolean IsShowingResult = false;
	private static final String TAG = "SIFTExampleActivity";
	float Touch_x1 = 0;
	float Touch_y1 = 0;
	float Touch_x2 = 0;
	float Touch_y2 = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("ImageProcessing Made by ZhengHaibo");
		setContentView(R.layout.activity_system_main);
		mClientForWeb = new ClientForWeb(this);
		seleImgBtnListener = new View.OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		};
		seleAlgBtnListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SystemMain.this,
						SelectAlgActivity.class);
				startActivity(intent);
			}
		};
		saveBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveBitmapToJpegFile();
			}
		};
		aboutBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SystemMain.this, ActivityAbout.class);
				startActivity(intent);
			}
		};
		shotScrBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				preBitmap = myBitmap;
				myBitmap = ShotScreentoBitmap(null);
				ShowImage(myBitmap, 1);
			}
		};
		cameraBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, SystemCapture);
			}
		};
		sendBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SystemMain.this,
						WebAlgSelectActivity.class);
				startActivity(intent);
				/*
				 * if(myBitmap!=null){ mClientForWeb.compressByteImage(myBitmap,
				 * 75);; ServerTask task = new ServerTask();
				 * Log.e("msg","new ServerTask finished!");
				 * task.execute(Environment
				 * .getExternalStorageDirectory().toString()
				 * +mClientForWeb.INPUT_IMG_FILENAME); }
				 */
			}
		};
		SetControl();
		ShowImage(null, 1);
	}

	private void SetControl() {
		selectAlgBtn = (Button) findViewById(R.id.processBtn);
		selectImgBtn = (Button) findViewById(R.id.SelectBtn);
		saveBtn = (Button) findViewById(R.id.SaveBtn);
		aboutBtn = (Button) findViewById(R.id.AboutBtn);
		shotScrBtn = (Button) findViewById(R.id.ScreenShotBtn);
		cameraBtn = (Button) findViewById(R.id.CameraBtn);
		sendBtn = (Button) findViewById(R.id.SendBtn);
		filepathView = (TextView) findViewById(R.id.ImagePath);
		aboutView = (TextView) findViewById(R.id.AboutTextView);
		myImageView = (DisplayView) findViewById(R.id.imageshow);
		linkTextView = (LinkTextView) findViewById(R.id.linktext);
		selectAlgBtn.setOnClickListener(seleAlgBtnListener);
		selectImgBtn.setOnClickListener(seleImgBtnListener);
		saveBtn.setOnClickListener(saveBtnListener);
		aboutBtn.setOnClickListener(aboutBtnListener);
		shotScrBtn.setOnClickListener(shotScrBtnListener);
		cameraBtn.setOnClickListener(cameraBtnListener);
		sendBtn.setOnClickListener(sendBtnListener);
		myImageView.setOnTouchListener(this);
		linkTextView.setUrl(this, "http://blog.csdn.net/nuptboyzhb/");
		IntentFilter filter_dynamic = new IntentFilter();
		filter_dynamic.addAction(DYNAMICACTION_Broadcast);
		registerReceiver(dynamicReceiver, filter_dynamic);
		IntentFilter filter_dynamic_web = new IntentFilter();
		filter_dynamic_web.addAction(DYNAMICACTION_Broadcast_WEB);
		registerReceiver(dynamicReceiverWeb, filter_dynamic_web);
	}

	// 2 自定义动态广播接收器，内部类,接收选择的本地算法
	private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(DYNAMICACTION_Broadcast)) {
				String seleFlag = intent.getStringExtra("selectFlag");
				int ch = Integer.parseInt(seleFlag);
				switch (ch) {
				case 0:
					ShowImage(myImageProcess.brighten(20, myBitmap), 2);
					break;
				case 1:
					ShowImage(myImageProcess.averageFilter(3, 3, myBitmap), 2);
					break;
				case 2:
					ShowImage(myImageProcess.averageFilter(3, 3, myBitmap), 2);
					break;
				case 3:
					ShowImage(myImageProcess.ColorToGray(myBitmap), 2);
					break;
				case 4:
					ShowImage(myImageProcess.ColorToBinary(myBitmap), 2);
					break;
				case 5:
					ShowImage(myImageProcess.BitReverse(myBitmap), 2);
					break;
				case 6:
					ShowImage(myImageProcess.rotate90(myBitmap), 2);
					break;
				case 7:
					ShowImage(myImageProcess.histEqualize(myBitmap), 2);
					break;
				case 8:
					ShowImage(myImageProcess.RobertGradient(myBitmap), 2);
					break;
				case 9:
					ShowImage(myImageProcess.SobelGradient(myBitmap), 2);
					break;
				case 10:
					ShowImage(myImageProcess.LaplaceGradient(myBitmap), 2);
					break;
				case 11:
					ShowImage(myImageProcess.Histograms(myBitmap), 2);
					break;
				case 12:
					ShowImage(myImageProcess.SobelGradientBlue(myBitmap, 70), 2);
					break;
				case 13:
					ShowImage(myImageProcess.LaplaceGradientBlue(myBitmap, 30),
							2);
					break;
				default:
					Toast.makeText(SystemMain.this, "Wrong!",
							Toast.LENGTH_SHORT).show();
					break;
				}
				Toast.makeText(SystemMain.this, "Processing finished!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	// 2 自定义动态广播接收器，内部类,接收选择的web算法
	private BroadcastReceiver dynamicReceiverWeb = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(DYNAMICACTION_Broadcast_WEB)) {
				String seleFlag = intent.getStringExtra("selectFlag");
				int ch = Integer.parseInt(seleFlag);
				switch (ch) {
				case 0:
					SERVERURL = "http://10.10.145.154/WebImageProcess/ImageDCT.php";
					StartWebProcess();
					break;
				case 1:
					SERVERURL = "http://10.10.145.154/WebImageProcess/ImageWavelet.php";
					StartWebProcess();
					break;
				default:
					Toast.makeText(SystemMain.this, "Wrong!",
							Toast.LENGTH_SHORT).show();
					break;
				}
				Toast.makeText(SystemMain.this, "wait...", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private void StartWebProcess() {
		if (myBitmap != null) {
			mClientForWeb.compressByteImage(myBitmap, 75);
			;
			ServerTask task = new ServerTask();
			Log.e("msg", "new ServerTask finished!");
			task.execute(Environment.getExternalStorageDirectory().toString()
					+ mClientForWeb.INPUT_IMG_FILENAME);
		}
	}

	private Bitmap FilesToBitmap(String filename) {
		Bitmap temp = null;
		if (filename != null) {
			File imageFile = new File(filename);
			if (imageFile.exists()) {
				// Load the image from file
				temp = BitmapFactory.decodeFile(filename);
			}

		}
		return temp;
	}

	public void ShowImage(Bitmap bitmap, int flag) {
		if (bitmap != null) {
			if (flag == 1) {
				myImageView.bitmap1 = null;
				myImageView.bitmap1 = bitmap;
				myBitmap = bitmap;
			} else {
				myImageView.bitmap2 = null;
				myImageView.bitmap2 = bitmap;
			}
		} else {
			if (flag == 1) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.lenna);
				myImageView.bitmap1 = null;
				myImageView.bitmap1 = bitmap;
				myBitmap = bitmap;
				preBitmap = bitmap;
				displayBitmap = bitmap;
				picturePath = null;
			} else {

			}
		}
		myImageView.invalidate();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			filepathView.setText(picturePath);
			// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			preBitmap = myBitmap;
			myBitmap = FilesToBitmap(picturePath);
			ShowImage(myBitmap, 1);
		}
		if (requestCode == SystemCapture && resultCode == RESULT_OK
				&& null != data) {
			preBitmap = myBitmap;
			myBitmap = (Bitmap) data.getExtras().get("data");
			ShowImage(myBitmap, 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_system_main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.imageshow) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Touch_x1 = event.getX();
				Touch_y1 = event.getY();
				// String posString="pos=("+Touch_x1+","+Touch_y1+",)";
				// Toast.makeText(SystemMain.this,posString,Toast.LENGTH_SHORT).show();
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				Touch_x2 = event.getX();
				Touch_y2 = event.getY();
				// String posString="pos=("+Touch_x2+","+Touch_y2+",)";
				// Toast.makeText(SystemMain.this,posString,Toast.LENGTH_SHORT).show();
				float temp_x = Math.abs(Touch_x1 - Touch_x1);
				float temp_y = Math.abs(Touch_y1 - Touch_y2);
				if (temp_x <= 1 && temp_y <= 1) {// 视为没有移动
					LayoutParams para = myImageView.getLayoutParams();
					if (para.height == LayoutParams.FILL_PARENT) {
						para.height = LayoutParams.WRAP_CONTENT;
					} else {
						para.height = LayoutParams.FILL_PARENT;
					}
					myImageView.setLayoutParams(para);
				}
				if (temp_x >= temp_y) {
					if (Touch_x1 >= Touch_x2) {// 向左滑动
						// ShowImage(displayBitmap);
					} else {// 向右滑动
						aboutView.setText("上一幅图像");
						ShowImage(preBitmap, 1);
					}
				} else {
					if (Touch_y1 >= Touch_y2) {// 向上滑动
						aboutView.setText("Lenna图像");
						ShowImage(null, 1);
					} else {// 向下滑动
						aboutView.setText("原图像");
						ShowImage(myBitmap, 1);
					}
				}
			}
		}
		return true;
	}

	public void SaveBitmapToJpegFile() {
		try {
			String outputPath = null;
			if (picturePath == null) {
				File outputDirectory = new File("/sdcard");
				String outputFile = "lennaFromSystem.jpg";
				outputPath = outputDirectory.toString() + "/" + outputFile;
			} else {
				outputPath = picturePath;
			}
			int quality = 75;
			FileOutputStream fileOutStr = new FileOutputStream(outputPath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(
					fileOutStr);
			displayBitmap.compress(CompressFormat.JPEG, quality, bufOutStr);
			bufOutStr.flush();
			bufOutStr.close();
			String tips = "file have save as:" + outputPath;
			Toast.makeText(SystemMain.this, tips, Toast.LENGTH_SHORT).show();
			aboutView.setText(tips);
		} catch (FileNotFoundException exception) {
			Toast.makeText(SystemMain.this, exception.toString(),
					Toast.LENGTH_SHORT).show();
			// Log.e("debug_log", exception.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Bitmap ShotScreentoBitmap(Activity activity) {
		Bitmap resultBitmap = null;
		// View是你需要截图的View
		if (activity == null) {
			activity = (Activity) this;
		}
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		resultBitmap = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		Bitmap b = Bitmap.createBitmap(resultBitmap, 0, statusBarHeight, width,
				height - statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	// *******************************************************************************
	// Push image processing task to server
	// *******************************************************************************
	public class ServerTask extends AsyncTask<String, Integer, Void> {
		public byte[] dataToServer;

		// Task state
		private final int UPLOADING_PHOTO_STATE = 0;
		private final int SERVER_PROC_STATE = 1;
		String serverFileName = null;
		private ProgressDialog dialog;

		// upload photo to server
		HttpURLConnection uploadPhoto(FileInputStream fileInputStream) {

			serverFileName = "test" + (int) Math.round(Math.random() * 1000)
					+ ".jpg";
			// final String serverFileName = "test.jpg";
			final String lineEnd = "\r\n";
			final String twoHyphens = "--";
			final String boundary = "*****";
			Log.e("msg", "begin HttpURLConnection......");
			try {
				URL url = new URL(SERVERURL);
				// Open a HTTP connection to the URL
				final HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// Allow Inputs
				conn.setDoInput(true);
				// Allow Outputs
				conn.setDoOutput(true);
				// Don't use a cached copy.
				conn.setUseCaches(false);

				// Use a post method.
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
						+ serverFileName + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				int bytesAvailable = fileInputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				publishProgress(SERVER_PROC_STATE);
				// close streams
				fileInputStream.close();
				dos.flush();
				Log.e("msg", "upload finished!");
				return conn;
			} catch (MalformedURLException ex) {
				Log.e(TAG, "error: " + ex.getMessage(), ex);
				return null;
			} catch (IOException ioe) {
				Log.e(TAG, "error: " + ioe.getMessage(), ioe);
				return null;
			}
		}

		/*
		 * 参数 ：将要获取的服务器文件名 filename 如果成功，返回真
		 */
		boolean getImageByWeb2(String filename) {
			File img = new File(TEMP_WEB_IMAGE_PATH + filename);
			// Create directories
			new File(TEMP_WEB_IMAGE_PATH).mkdirs();
			// only download new images
			if (!img.exists()) {
				try {
					URL imageUrl = new URL(
							"http://10.10.145.154/WebImageProcess/output/"
									+ filename);
					InputStream in = imageUrl.openStream();
					OutputStream out = new BufferedOutputStream(
							new FileOutputStream(img));

					for (int b; (b = in.read()) != -1;) {
						out.write(b);
					}
					out.close();
					in.close();
				} catch (MalformedURLException e) {
					img = null;
					return false;
				} catch (IOException e) {
					img = null;
					return false;
				}
			}
			return true;
		}

		// get image result from server and display it in result view
		void getResultImage(HttpURLConnection conn) {
			// retrieve the response from server
			InputStream is;
			resultForWebImage = null;
			try {
				is = conn.getInputStream();
				// get result image from server
				resultForWebImage = BitmapFactory.decodeStream(is);
				if (resultForWebImage == null) {
					Log.e("msg", "no image file download!");
				}
				is.close();
				IsShowingResult = true;
				Log.d("msg", "download finished!");
			} catch (IOException e) {
				Log.e(TAG, "getResultImage:" + e.toString());
				e.printStackTrace();
			}

		}

		// Main code for processing image algorithm on the server
		void processImage(String inputImageFilePath) {
			publishProgress(UPLOADING_PHOTO_STATE);
			File inputFile = new File(inputImageFilePath);
			try {

				// create file stream for captured image file
				FileInputStream fileInputStream = new FileInputStream(inputFile);

				// upload photo
				final HttpURLConnection conn = uploadPhoto(fileInputStream);

				// get processed photo from server
				if (conn != null) {
					getResultImage(conn);
					if (resultForWebImage == null) {
						if (getImageByWeb2("processed_" + serverFileName)) {
							resultForWebImage = FilesToBitmap(TEMP_WEB_IMAGE_PATH
									+ "processed_" + serverFileName);
						} else {
							Log.e(TAG, "获取图片又失败！！");
						}
					}
				}
				fileInputStream.close();
			} catch (FileNotFoundException ex) {
				Log.e(TAG, "processImage" + ex.toString());
			} catch (IOException ex) {
				Log.e(TAG, "processImage" + ex.toString());
			}
		}

		public ServerTask() {
			dialog = new ProgressDialog(SystemMain.this);
		}

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Photo captured");
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(String... params) { // background
															// operation
			String uploadFilePath = params[0];
			processImage(uploadFilePath);
			// release camera when previous image is processed
			mCameraReadyFlag = true;
			return null;
		}

		// progress update, display dialogs
		@Override
		protected void onProgressUpdate(Integer... progress) {
			if (progress[0] == UPLOADING_PHOTO_STATE) {
				dialog.setMessage("Uploading");
				dialog.show();
			} else if (progress[0] == SERVER_PROC_STATE) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				dialog.setMessage("Processing");
				dialog.show();
			}
		}

		@Override
		protected void onPostExecute(Void param) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				if (resultForWebImage != null) {
					ShowImage(resultForWebImage, 2);
				} else {

					ShowImage(resultForWebImage, 2);
				}
			}
		}
	}
}
