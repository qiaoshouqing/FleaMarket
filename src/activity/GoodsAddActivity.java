package activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;
import util.Encrype;
import util.SlideOnClickListener;
import view.SlideMenu;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsAddActivity extends Activity implements OnClickListener {

	private String url = "http://glimpse.sinaapp.com/fgoods_ok_android.php";
	private String imgPath = null;
	private Uri uri;
	private Bitmap bitmap;
	private TextView post;
	private IsNet isNet;
	private String uid;
	private boolean postFlag = true;
	private SharedPreferences preferences;
	private String[] sortArray = {"杂物" ,"书籍", "车子", "衣物"};
	private String[] is_soldArray = {"未售出", "已售出"};

	private EditText gname;
	private EditText gprice;
	private EditText gdescribe;
	private Spinner gsort;
	private ImageView gimage;

	private String myGname;
	private String myGprice;
	private String myGdescribe;
	private String myGsort;
	private String myGimg;
	
	private TextView title_bar_name;
	private TextView others;
	private TextView books;
	private TextView bicycles;
	private TextView cloths;
	private TextView search;
	private TextView add;
	private TextView my;
	private SlideMenu slideMenu;
	private Context ctx = GoodsAddActivity.this;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			postFlag = true;
			if (result.contains("发表成功")) {
				Toast.makeText(GoodsAddActivity.this, "发表成功",
						Toast.LENGTH_SHORT).show();
				Toast.makeText(GoodsAddActivity.this, "售出后请改为已售出",
						Toast.LENGTH_SHORT).show();

				finish();
			} else {
				Toast.makeText(GoodsAddActivity.this, result,
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	private TextView share;

	private TextView notice;

	private TextView about;
	private TextView main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goodsaddactivity);
		
		File root = this.getExternalFilesDir(null);
		imgPath = root.getAbsolutePath() + "/imgpath";

		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		String encryptuid = preferences.getString("uid", null);
		
		try {
			uid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i("encryptuid", encryptuid);
		//Log.i("uid", uid);
		
		isNet = new IsNet(getApplicationContext());

		gname = (EditText) findViewById(R.id.gname);
		gprice = (EditText) findViewById(R.id.gprice);
		gdescribe = (EditText) findViewById(R.id.gdescribe);
		gsort = (Spinner) findViewById(R.id.gsort);
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
		
		
		//侧边栏和标题栏
		post = (TextView) findViewById(R.id.refresh_post);
		post.setText("发布");
		post.setOnClickListener(this);
		
		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("添加");
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
		menuImg.setOnClickListener(this);
		
		SlideOnClickListener sclick = new SlideOnClickListener(ctx);
		others = (TextView) findViewById(R.id.other);
		books = (TextView) findViewById(R.id.books);
		bicycles = (TextView) findViewById(R.id.bicycles);
		cloths = (TextView) findViewById(R.id.cloths);
		search = (TextView) findViewById(R.id.search);
		add = (TextView) findViewById(R.id.add);
		my = (TextView) findViewById(R.id.my);
		notice = (TextView) findViewById(R.id.notice);
		main = (TextView) findViewById(R.id.main);
		
		others.setOnClickListener(sclick);
		books.setOnClickListener(sclick);
		bicycles.setOnClickListener(sclick);
		cloths.setOnClickListener(sclick);
		search.setOnClickListener(sclick);
		add.setOnClickListener(sclick);
		my.setOnClickListener(sclick);
		notice.setOnClickListener(sclick);
		main.setOnClickListener(sclick);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
			}
			break;
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
					&& myGsort != null && myGimg != null) {
				String imgname = System.currentTimeMillis() + ".jpeg";

				String value = "{\"uid\":\"" + uid + "\",\"gname\":\""
						+ myGname + "\",\"gprice\":\"" + myGprice
						+ "\",\"gdescribe\":\"" + myGdescribe
						+ "\",\"gsort\":\"" + myGsort + "\",\"gimagename\":\""
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
