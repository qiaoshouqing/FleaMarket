package activity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.IsNet;
import net.MyPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fleamarket.R;

import entity.CommentsEntity;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;
import util.AsyncImageLoader;
import util.Encrype;
import adapter.CommentsAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsShowActivity extends Activity implements OnClickListener{
	
	private String url = "http://glimpse.sinaapp.com/fgoodsshow_android.php";
	private String url1 = "http://glimpse.sinaapp.com/fcomments_android.php";
	private String url2 = "http://glimpse.sinaapp.com/fcomments_ok_android.php";
	private String url3 = "http://glimpse.sinaapp.com/fgoods_delete_android_haoba.php";
	private EditText comment;
	private ListView listView;
	private List<CommentsEntity> listItems;
	private CommentsAdapter commentsAdapter;
	private Context ctx = GoodsShowActivity.this;
	private IsNet isNet;
	private SharedPreferences preferences;
	private int uid;
	private String myComment;
	
	private TextView title_bar_name;
	private String name = null;
	private String is_sold = null;
	private String imageUrl = null;
	private String describe = null;
	private String price = null;
	private String sort = null;
	private String name1 = null;
	private String phone = null;
	private String created_at = null;
	private int gid = 0;
	private Button dopost;
	private Date date = new Date();
	private DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private int reply_uid = 0;
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 0x123)
			{
				String result = (String) msg.obj;
				//Log.i("result", result);
				if(result.contains("gdescribe"))
				{
					if(result.contains("<html>"))
					{
						Toast.makeText(ctx, "请检查网络", Toast.LENGTH_SHORT).show();
					}
					else
					{
						try
						{
							JSONArray jArray = new JSONArray(result);
							for(int i = 0;i < jArray.length();i++)
							{
								JSONObject json_data = jArray.getJSONObject(i);
								
								
								gid = json_data.getInt("gid");
								uid = json_data.getInt("uid");
								name1 = json_data.getString("uname");
								phone = json_data.getString("uphone");
								name = json_data.getString("gname");
								price = json_data.getString("gprice");
								describe = json_data.getString("gdescribe");
								is_sold = json_data.getString("gis_sold");
								sort = json_data.getString("gsort");
								imageUrl = json_data.getString("gimage");
								created_at = json_data.getString("gcreated_at");
								
								reply_uid = uid;
								Log.i("reply_uid", reply_uid+"");
								
								gname.setText(name);
								gis_sold.setText(is_sold);
								gimage.setImageResource(R.drawable.background);
								gdescribe.setText(describe);
								gprice.setText(price+"￥");
								//gsort.setText("分类："+sort);
								uname.setText(name1);
								uphone.setText("直接拨打："+phone);
								gcreated_at.setText(created_at);
								
								Bitmap bitmap = asyncImageLoader.loadImage(gimage, imageUrl);
								if(bitmap != null)
								{
									gimage.setImageBitmap(bitmap);
								}
								
							}
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				else if(result.contains("发表成功"))
				{
					Toast.makeText(GoodsShowActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
				}
				else if(result.contains("删除成功"))
				{
					Toast.makeText(GoodsShowActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ctx, MyPostActivity.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Toast.makeText(GoodsShowActivity.this, result, Toast.LENGTH_SHORT).show();
				}
				
			}
			else if(msg.what == 0x789)
			{
				listView.setAdapter(commentsAdapter);
			}
		}
	};
	private TextView gname;
	private TextView gis_sold;
	private ImageView gimage;
	private TextView gdescribe;
	private TextView gprice;
	private TextView gsort;
	private TextView uname;
	private TextView uphone;
	private TextView gcreated_at;
	private AsyncImageLoader asyncImageLoader;
	private String dbuname;
	private String dbuid;
	private TextView back;
	private ImageView more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.goodsshowactivity);
		
		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("展示");
		more = (ImageView) findViewById(R.id.more);
		more.setVisibility(View.GONE);
		more.setOnClickListener(this);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
		
		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
        dbuname =preferences.getString("uname", "用户");
        String encryptuid = preferences.getString("uid", null);
		try {
			dbuid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gname = (TextView) findViewById(R.id.gname);
		gis_sold = (TextView) findViewById(R.id.gis_sold);
		gimage = (ImageView) findViewById(R.id.gimage);
		gdescribe = (TextView) findViewById(R.id.gdescribe);
		gprice = (TextView) findViewById(R.id.gprice);
		//gsort = (TextView) findViewById(R.id.gsort);
		uname = (TextView) findViewById(R.id.uname);
		uphone = (TextView) findViewById(R.id.uphone);
		gcreated_at = (TextView) findViewById(R.id.gcreated_at);
		uphone.setOnClickListener(this);
		
		comment = (EditText) findViewById(R.id.EditText);
		dopost = (Button) findViewById(R.id.Button);
		listView = (ListView) findViewById(R.id.ListView_comments);
		listItems = new ArrayList<CommentsEntity>();
		dopost.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				TextView reply = (TextView) view.findViewById(R.id.reply);
				CommentsEntity ce = (CommentsEntity) listView.getItemAtPosition(position);
				final String uname = ce.getUname();
				reply_uid = ce.getUid();
				reply.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						comment.setText("回复@" + uname + ":");
						comment.setFocusable(true);
						InputMethodManager imm = (InputMethodManager)GoodsShowActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				});
				
			}
		});
		
		
		asyncImageLoader = new AsyncImageLoader(GoodsShowActivity.this);
		
		Intent intent = getIntent();
		gid = intent.getIntExtra("gid", 0);
		uid = intent.getIntExtra("uid", 0);
		name = intent.getStringExtra("gname");
		is_sold = intent.getStringExtra("gis_sold");
		imageUrl = intent.getStringExtra("gimage");
		describe = intent.getStringExtra("gdescribe");
		price = intent.getStringExtra("gprice");
		sort = intent.getStringExtra("gsort");
		name1 = intent.getStringExtra("uname");
		phone = intent.getStringExtra("uphone");
		created_at = intent.getStringExtra("gcreated_at");
		
		isNet = new IsNet(ctx);
		reply_uid = uid;
		Log.i("reply_uid", reply_uid+"");
		
		if(dbuid.equals(uid+""))
		{
			more.setVisibility(View.VISIBLE);
		}
		else if(uid != 0 && name != null)
		{
			gname.setText(name);
			gis_sold.setText(is_sold);
			gimage.setImageResource(R.drawable.background);
			gdescribe.setText(describe);
			gprice.setText(price+"￥");
			//gsort.setText("分类："+sort);
			uname.setText(name1);
			uphone.setText("直接拨打："+phone);
			gcreated_at.setText(created_at);
			Bitmap bitmap = asyncImageLoader.loadImage(gimage, imageUrl);
			if(bitmap != null)
			{
				gimage.setImageBitmap(bitmap);
			}
		}
		
		if(isNet.IsConnect())
		{
			String value = "{\"gid\":\"" + gid + "\"}";
			//Log.i("gid", gid+"");
			ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
			new Thread()
			{
				public void run() 
				{
					checkServer(gid);
					
					handler.sendEmptyMessage(0x789);
					
				}

			}.start();
		}
	}
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch(id)
		{
		case R.id.uphone:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:"+ phone));
			startActivity(intent);
			break;
		case R.id.more:
			String[] items = { "编辑", "删除" };
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							switch (which) {
							case 0:
								Intent intent = new Intent(ctx, GoodsUpdateActivity.class);
								intent.putExtra("gid", gid);
								intent.putExtra("gname", name);
								intent.putExtra("gis_sold", is_sold);
								intent.putExtra("gimage", imageUrl);
								intent.putExtra("gdescribe", describe);
								intent.putExtra("gprice", price);
								//intent.putExtra("gsort", sort);
								startActivity(intent);
								break;
							case 1:
								if(isNet.IsConnect())
								{
									String value = "{\"gid\":\"" + gid + "\"}";
									ThreadPoolUtils.execute(new HttpPostThread(handler, url3, value));
									Log.i("deletegid", gid+"");
								}
								
								break;
							default:
								break;
							}

						}

					});

			builder.create().show();
			break;
		case R.id.Button:
			myComment = comment.getText().toString();
			
			String value = "{\"uid\":\"" + dbuid + "\",\"gid\":\"" + gid + "\",\"reply_uid\":\"" + reply_uid + "\",\"ccontent\":\""
					+ myComment + "\"}";
			
			if(isNet.IsConnect())
			{
				ThreadPoolUtils.execute(new HttpPostThread(handler, url2, value));
				
				String created_at = format.format(date);
				updateDisplay(new CommentsEntity(0, 0, gid, dbuname, myComment, created_at));
			}
			
			comment.setText("");
			
			try {
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
			} catch (IOException e) {
				e.printStackTrace();
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
	
	// 更新界面显示内容
    private void updateDisplay(CommentsEntity commentsEntity) {
    	
    	//Log.i("updateDisplay","123");
    	listItems.add(0 ,commentsEntity);
    	commentsAdapter.notifyDataSetChanged();

    }
	
	private void checkServer(int gid)
	{
		
		String value = "{\"gid\":\"" + gid + "\"}";
		String result1 = MyPost.getRequest(url1, value, null);
		//Log.i("message123", result1);
		
		try {
			JSONArray jArray = new JSONArray(result1);
			
			listItems = new ArrayList<CommentsEntity>();
			if(result1 == null)
			{
				return;
			}
			
			for(int i = 0;i < jArray.length();i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
				
				int cid = json_data.getInt("cid");
				int uid = json_data.getInt("uid");
				int gid1 = json_data.getInt("gid");
				String uname = json_data.getString("uname");
				String ccontent = json_data.getString("ccontent");
				String ccreated_at = json_data.getString("ccreated_at");
				listItems.add(new CommentsEntity(cid, uid, gid1, uname, ccontent, ccreated_at));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		//Log.i("message123", result1);
		
		
		commentsAdapter = new CommentsAdapter((Activity)ctx, listItems);
	}

}
