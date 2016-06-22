package activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpGetThread;
import thread.ThreadPoolUtils;
import util.Encrype;
import util.FileUtil;
import util.MyJson;
import view.SlideMenu;
import entity.GoodsEntity;
import adapter.GoodsAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


@SuppressLint("HandlerLeak")
public class MyPostActivity extends Activity implements OnClickListener{
	
	private String purl = "http://glimpse.sinaapp.com/fmygoods_android.php";
	private String url = null;
	private int start = 0;
	private View moreView;
	private String uid;
	private SharedPreferences preferences;
	private Button moredata;
	private ProgressBar pb;
	private ListView lv;
	private List<GoodsEntity> listItems = new ArrayList<GoodsEntity>();
	private GoodsAdapter goodsAdapter;
	private MyJson myJson = new MyJson();
	private IsNet isNet;
	private FileUtil fileUtil;
	private SlideMenu slideMenu;
	private Context ctx = MyPostActivity.this;
	
	
	private boolean refreshFlag = false;
	
	private String FILE_NAME = "/mypost";
	
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(msg.what == 0x123)
			{
				moredata.setVisibility(View.VISIBLE);
				String result = (String) msg.obj;
				//Log.i("resultothers", result);
				if(result != null)
				{
					List<GoodsEntity> newListItems = myJson.getGoodsList(result);
					if(newListItems != null)
					{
						//Log.i("tip", "1");
						if(newListItems.size() == 5)
						{
							if(start == 0)
							{
								fileUtil.write(result);
							}
							//Log.i("tip", "2");
							start += 5;
						}
						else
						{
							moredata.setVisibility(View.GONE);
						}
						if(refreshFlag == true)
						{
							listItems.removeAll(listItems);
							refreshFlag = false;
							Toast.makeText(ctx, "刷新完毕", Toast.LENGTH_SHORT).show();
						}
							
						for (GoodsEntity pe : newListItems) {
							listItems.add(pe);
						}
						
					}
					else
					{
						moredata.setVisibility(View.GONE);
						Toast.makeText(ctx, "没有啦~", Toast.LENGTH_SHORT).show();
					}
					
				}
				pb.setVisibility(View.GONE);
				goodsAdapter.notifyDataSetChanged();
			}
		}
	};
	private TextView title_bar_name;
	private TextView back;
	private TextView refresh_post;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mypostactivity);
		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		preferences.getString("uname", "用户名");
		String encryptuid = preferences.getString("uid", null);
		try {
			uid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i("encryptuid", encryptuid);
		//Log.i("uid", uid);
		initView();
	}
		
	private void initView() {
		
		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("我的发布");
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
		refresh_post = (TextView) findViewById(R.id.refresh_post);
		refresh_post.setOnClickListener(this);
		
		lv = (ListView) findViewById(R.id.ListView);
		moreView = getLayoutInflater().inflate(R.layout.moredata, null);
		moredata = (Button) moreView.findViewById(R.id.button);
		pb = (ProgressBar) moreView.findViewById(R.id.pb);
		moredata.setVisibility(View.GONE);
		moredata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(isNet.IsConnect())
				{
					url = purl + "?uid=" + uid  +"&start=" + start;
					ThreadPoolUtils.execute(new HttpGetThread(handler, url));
					moredata.setVisibility(View.GONE);
					pb.setVisibility(View.VISIBLE);
				}
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				GoodsEntity pe = (GoodsEntity) lv.getItemAtPosition(position);
				Intent intent = new Intent(ctx, GoodsShowActivity.class);
				intent.putExtra("gid", pe.getGid());
				intent.putExtra("uid", pe.getUid());
				intent.putExtra("gname", pe.getGname());
				intent.putExtra("gis_sold", pe.getGis_sold());
				intent.putExtra("gimage", pe.getGimage());
				intent.putExtra("gdescribe", pe.getGdescribe());
				intent.putExtra("gprice", pe.getGprice());
				intent.putExtra("gsort", pe.getGsort());
				intent.putExtra("uname", pe.getUname());
				intent.putExtra("uphone", pe.getUphone());
				intent.putExtra("gcreated_at", pe.getGcreated_at());
				startActivity(intent);
			}
		});
		
		
		
		goodsAdapter = new GoodsAdapter((Activity)ctx, listItems);
		lv.addFooterView(moreView);
		lv.setAdapter(goodsAdapter);
		
		
		fileUtil = new FileUtil(ctx, FILE_NAME);
		isNet = new IsNet(ctx);
		String result;
		if((result = fileUtil.read()) != null)
		{
			Message msg = new Message();
			msg.what = 0x123;
			msg.obj = result;
			handler.sendMessage(msg);
			
			Timer timer = new Timer(true);
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					Looper.prepare();
					refresh();
					Looper.loop();
				}
			};
			timer.schedule(task, 3 * 1000);
		}
		else
		{
			refresh();
		}
		
		
	}
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch(id)
		{
		case R.id.title_bar_menu_btn:
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
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
		case R.id.refresh_post:
			refresh();
			break;
		}
	}
	
	public void refresh()
	{
		if(isNet.IsConnect())
		{
			start = 0;
			url = purl + "?uid=" + uid  +"&start=" + start;
			ThreadPoolUtils.execute(new HttpGetThread(handler, url));
			refreshFlag = true;
		}
	}
	
}
