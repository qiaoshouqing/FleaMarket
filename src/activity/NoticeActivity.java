package activity;

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
import util.SlideOnClickListener;
import view.SlideMenu;
import entity.CommentsEntity;
import adapter.CommentsAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class NoticeActivity extends Activity implements OnClickListener{
	
	private String about_url = "http://glimpse.sinaapp.com/fnotice_android.php";
	private ListView lv;
	private String url = null;
	private int start = 0;
	private View moreView;
	private Button moredata;
	private ProgressBar pb;
	private TextView back;
	private TextView refresh;
	private SharedPreferences preferences;
	private String uid;
	private SlideMenu slideMenu;
	
	private List<CommentsEntity> listItems = new ArrayList<CommentsEntity>();
	private CommentsAdapter commentsAdapter;
	private IsNet isNet;
	private MyJson myJson = new MyJson();
	private FileUtil fileUtil;
	private Context ctx = NoticeActivity.this;
	private String FILE_NAME = "/notice";
	private boolean refreshFlag = false;
	
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(msg.what == 0x123)
			{
				moredata.setVisibility(View.VISIBLE);
				String result = (String) msg.obj;
				Log.i("result", result);
				if(result != null)
				{
					List<CommentsEntity> newListItems = myJson.getCommentsList(result);
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
							
						for (CommentsEntity ce : newListItems) {
							listItems.add(ce);
						}
						
					}
					else
					{
						moredata.setVisibility(View.GONE);
						Toast.makeText(ctx, "没有鸟~", Toast.LENGTH_SHORT).show();
					}
					
				}
				pb.setVisibility(View.GONE);
				commentsAdapter.notifyDataSetChanged();
			}
		};
	};
	private TextView title_bar_name;
	private TextView others;
	private TextView books;
	private TextView bicycles;
	private TextView cloths;
	private TextView search;
	private TextView add;
	private TextView my;
	private TextView share;
	private TextView notice;
	private TextView about;
	private TextView main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.noticeactivity);
		initView();
	}
	
	private void initView() {
		
		refresh = (TextView) findViewById(R.id.refresh_post);
		refresh.setOnClickListener(this);
		
		title_bar_name = (TextView) findViewById(R.id.title_bar_name);
		title_bar_name.setText("通知");
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
		share = (TextView) findViewById(R.id.share);
		notice = (TextView) findViewById(R.id.notice);
		about = (TextView) findViewById(R.id.about);
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
		

		lv = (ListView) findViewById(R.id.ListView);
		moreView = getLayoutInflater().inflate(R.layout.moredata, null);
		moredata = (Button) moreView.findViewById(R.id.button);
		pb = (ProgressBar) moreView.findViewById(R.id.pb);
		moredata.setVisibility(View.GONE);
		moredata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Log.i("tip","正在加载");
				if(isNet.IsConnect())
				{
					url = about_url + "?uid=" + uid +"&start=" + start;
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
				
				CommentsEntity ce = (CommentsEntity) lv.getItemAtPosition(position);
				Intent intent = new Intent(ctx, GoodsShowActivity.class);
				intent.putExtra("gid", ce.getGid());
				//Log.i("ngid", ce.getGid()+"");
				startActivity(intent);
				
			}
		});
		
		commentsAdapter = new CommentsAdapter((Activity)ctx, listItems);
		lv.addFooterView(moreView);
		lv.setAdapter(commentsAdapter);
		
		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		String encryptuid = preferences.getString("uid", null);
		try {
			uid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
			url = about_url + "?uid=" + uid + "&start=" + start;
			ThreadPoolUtils.execute(new HttpGetThread(handler, url));
			refreshFlag = true;
		}
	}
}
