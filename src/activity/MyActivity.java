package activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.IsNet;

import org.w3c.dom.Text;

import update.UpdateManager;
import util.Encrype;
import util.FileUtiles;
import util.SlideOnClickListener;
import view.RefreshableView;
import view.SlideMenu;

import com.example.fleamarket.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends Activity implements OnClickListener {

	private TextView title_bar_name;
	private TextView myName;
	private SharedPreferences preferences;
	private String uid;
	private String uname;
	private TextView myPost;
	private TextView share;
	private TextView check;
	private TextView about;
	private Intent intent = new Intent();
	private Context context = MyActivity.this;
	private TextView others;
	private TextView books;
	private TextView bicycles;
	private TextView cloths;
	private TextView search;
	private TextView add;
	private TextView my;
	private TextView notice;
	private SlideMenu slideMenu;
	private IsNet isNet;
	private TextView refresh;
	private TextView main;
	private TextView suggest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myactivity);

		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("我的");
		refresh = (TextView) findViewById(R.id.refresh_post);
		refresh.setText("");
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
		menuImg.setOnClickListener(this);
		
		
		SlideOnClickListener sclick = new SlideOnClickListener(context);
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

		isNet = new IsNet(context);
		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		uname = preferences.getString("uname", "用户名");
		String encryptuid = preferences.getString("uid", null);
		try {
			uid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myName = (TextView) findViewById(R.id.myName);
		myName.setText(uname);

		myPost = (TextView) findViewById(R.id.mypost);
		share = (TextView) findViewById(R.id.share);
		suggest = (TextView) findViewById(R.id.suggest);
		check = (TextView) findViewById(R.id.check);
		about = (TextView) findViewById(R.id.about);
		
		myPost.setOnClickListener(this);
		share.setOnClickListener(this);
		suggest.setOnClickListener(this);
		check.setOnClickListener(this);
		about.setOnClickListener(this);

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
		case R.id.mypost:
			intent.setClass(this, MyPostActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.about:
			intent.setClass(this, AboutMeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.check:
			if(isNet.IsConnect())
			{
				UpdateManager manager = new UpdateManager(context, false);
				// 检查软件更新
				manager.checkUpdate();
			}
			
			break;
		case R.id.share:
			String imgPath = getImgPath("erweima.jpeg");
			shareMsg(
					"ShareActivity",
					"跳蚤市场",
					"[聊大跳蚤市场]安卓端上线啦~你可以在这里发布商品，或者淘到好东西~应用链接：http://glimpse.sinaapp.com/application/FleaMarket.apk",
					imgPath);
			break;
		case R.id.suggest:
			intent.setClass(this, SuggestActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;

		}

	}

	private String getImgPath(String name) {

		File root = context.getExternalFilesDir(null);
		File file = new File(root, name);
		if (file.exists()) {
			return root.getAbsolutePath() + "/" + name;
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.erweima);
			FileUtiles fileUtils = new FileUtiles(context);
			fileUtils.saveBitmap(name, bitmap);
			return root.getAbsolutePath() + "/" + name;
		}
	}

	public void shareMsg(String activityTitle, String msgTitle, String msgText,
			String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/jpg");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
}