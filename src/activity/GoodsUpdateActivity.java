package activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;
import util.AsyncImageLoader;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsUpdateActivity extends Activity implements OnClickListener {

	private String url = "http://glimpse.sinaapp.com/fgoods_update_android_haoba.php";
	private Uri uri;
	@SuppressLint("SdCardPath")
	String imgPath = "/sdcard/img.jpg";
	private Bitmap bitmap;
	private TextView post;
	private IsNet isNet;
	private boolean postFlag = true;
	private String[] sortArray = {"杂物" ,"书籍", "车子", "衣物"};
	private String[] is_soldArray = {"未售出", "已售出"};

	private EditText gname;
	private EditText gprice;
	private EditText gdescribe;
	private Spinner gsort;
	private Spinner gis_sold;
	private ImageView gimage;

	private String myGname;
	private String myGprice;
	private String myGdescribe;
	private String myGsort;
	private String myGis_sold;
	private String myGimg;
	
	private Context ctx = GoodsUpdateActivity.this;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			postFlag = true;
			if (result.contains("更新成功")) {
				Toast.makeText(ctx, "更新成功",
						Toast.LENGTH_SHORT).show();

				finish();
				Intent intent = new Intent();
				intent.setClass(ctx, BooksActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
				
			} else {
				Toast.makeText(ctx, result,
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	private TextView title_bar_name;

	private int gid;

	private TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goodsupdateactivity);

		isNet = new IsNet(getApplicationContext());

		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("修改");
		post = (TextView) findViewById(R.id.refresh_post);
		post.setText("修改");
		post.setOnClickListener(this);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
		
		gname = (EditText) findViewById(R.id.gname);
		gprice = (EditText) findViewById(R.id.gprice);
		gdescribe = (EditText) findViewById(R.id.gdescribe);
		gsort = (Spinner) findViewById(R.id.gsort);
		gis_sold = (Spinner) findViewById(R.id.gis_sold);
		gimage = (ImageView) findViewById(R.id.gimage);
		gimage.setOnClickListener(this);
		gsort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				myGsort = sortArray[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		gis_sold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				myGis_sold = is_soldArray[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader(ctx);
		
		Intent intent = getIntent();
		gid = intent.getIntExtra("gid", 0);
		String name = intent.getStringExtra("gname");
		String is_sold = intent.getStringExtra("gis_sold");
		String imageUrl = intent.getStringExtra("gimage");
		String describe = intent.getStringExtra("gdescribe");
		String price = intent.getStringExtra("gprice");
		String sort = intent.getStringExtra("gsort");
		isNet = new IsNet(ctx);
		
		
		gname.setText(name);
		int position = 0;
		if(is_sold.equals("未售出"))
		{
			position = 0;
		}
		else
		{
			position = 1;
		}
		myGis_sold = is_sold;
		gis_sold.setSelection(position);
		gimage.setImageResource(R.drawable.background);
		gdescribe.setText(describe);
		gprice.setText(price);
		if(sort.equals("杂物"))
		{
			position = 0;
		}
		else if(sort.equals("书籍"))
		{
			position = 1;
		}
		else if(sort.equals("车子"))
		{
			position = 2;
		}
		else
		{
			position = 3;
		}
		myGsort = sort;
		gsort.setSelection(position);
		myGimg = imageUrl;
		Bitmap bitmap = asyncImageLoader.loadImage(gimage, imageUrl);
		if(bitmap != null)
		{
			gimage.setImageBitmap(bitmap);
		}

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.gimage:
			String[] items = { "图库", "拍照" };
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							switch (which) {
							case 0:
								Intent intent = new Intent();

								intent.setType("image/*");

								intent.setAction(Intent.ACTION_GET_CONTENT);

								startActivityForResult(intent, 11);
								break;
							case 1:
								File vFile = new File(imgPath);
								if (!vFile.exists()) {
									File vDirPath = vFile.getParentFile();
									vDirPath.mkdirs();
								}
								Uri uri = Uri.fromFile(vFile);
								Intent intent1 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								startActivityForResult(intent1, 22);
								break;
							default:
								break;
							}

						}

					});

			builder.create().show();
			break;
		case R.id.refresh_post:

			if (!postFlag) {
				Toast.makeText(getApplicationContext(), "正在上传。。。",
						Toast.LENGTH_SHORT).show();
				break;
			}
			myGname = gname.getText().toString();
			myGprice = gprice.getText().toString();
			myGdescribe = gdescribe.getText().toString();
			if (myGname != null && myGprice != null && myGdescribe != null
					&& myGsort != null && myGis_sold != null && myGimg != null) {
				String imgname = System.currentTimeMillis() + ".jpeg";

				String value = "{\"gid\":\"" + gid + "\",\"gname\":\""
						+ myGname + "\",\"gprice\":\"" + myGprice
						+ "\",\"gdescribe\":\"" + myGdescribe
						+ "\",\"gis_sold\":\"" + myGis_sold + "\",\"gsort\":\"" + myGsort + "\",\"gimagename\":\""
						+ imgname + "\"}";

				if (isNet.IsConnect()) {
					ThreadPoolUtils.execute(new HttpPostThread(handler, url,
							value, myGimg));
					postFlag = false;
				}
			} else {
				Toast.makeText(getApplicationContext(), "请填写完整",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.back:
			try {
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}

	}

	/****************** ���ͼƬ��ת���� ****************************/
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * 
	 * @param path
	 *            ͼƬ���·��
	 * @return degree��ת�ĽǶ�
	 */
	@SuppressLint("NewApi")
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			// ��ȡͼƬ���Ե�
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 11) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				Log.e("uri", uri.toString());
				ContentResolver cr = this.getContentResolver();
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					options.inPurgeable = true;
					options.inInputShareable = true;

					if (null != bitmap && bitmap.isRecycled() == false) {
						bitmap.recycle();
						bitmap = null;
					}

					if (uri != null) {
						bitmap = BitmapFactory.decodeFile(uri.getPath(),
								options);
					}
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					/* ��Bitmap�趨��ImageView */
					gimage.setImageBitmap(bitmap);

					myGimg = createbase64(bitmap);
				} catch (FileNotFoundException e) {
					Log.e("Exception", e.getMessage(), e);
				}
			}

		} else if (requestCode == 22) {
			File fial = new File(imgPath);
			uri = Uri.fromFile(fial);
			//
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			int degree = readPictureDegree(fial.getAbsolutePath());

			if (null != bitmap && bitmap.isRecycled() == false) {
				bitmap.recycle();
				bitmap = null;
			}

			if (uri != null) {
				bitmap = BitmapFactory.decodeFile(uri.getPath(), options);

				bitmap = rotaingImageView(degree,
						BitmapFactory.decodeFile(uri.getPath(), options));
			}
			gimage.setImageBitmap(bitmap);
			myGimg = createbase64(bitmap);

		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	static String createbase64(Bitmap bitmap) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 40, bos);// ����100��ʾ��ѹ��
		byte[] bytes = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

}
