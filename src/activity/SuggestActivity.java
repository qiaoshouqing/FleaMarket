package activity;

import java.io.IOException;

import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;
import util.Encrype;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SuggestActivity extends Activity implements OnClickListener{
	
	private String url = "http://glimpse.sinaapp.com/fsuggest_ok_android.php";
	private EditText message;
	private TextView back;
	private TextView post;
	private IsNet isNet;
	private SharedPreferences preferences;
	private String uid;
	private boolean postFlag = true;
	private Context ctx = SuggestActivity.this;
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 0x123)
			{
				postFlag = true;
				String result = (String) msg.obj;
				
				if(result.contains("发表成功"))
				{
					Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
					
					finish();
				}
				else
				{
					Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.suggestactivity);
		
		preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		String encryptuid = preferences.getString("uid", null);
		
		try {
			uid = Encrype.decrypt("joyce", encryptuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		message = (EditText) findViewById(R.id.message);
		back = (TextView) findViewById(R.id.back);
		post = (TextView) findViewById(R.id.post);
		back.setOnClickListener(this);
		post.setOnClickListener(this);	
		
		isNet = new IsNet(getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id)
		{
		case R.id.back:
			try {
				Runtime runtime = Runtime.getRuntime();
				runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.post:
			if(!postFlag)
			{
				Toast.makeText(getApplicationContext(), "正在上传。。。", Toast.LENGTH_SHORT).show();
				return;
			}
			String myMessage = message.getText().toString();
			if(myMessage != null && myMessage != "")
			{
				String value = "{\"uid\":\"" + uid + "\",\"content\":\""
						+ myMessage + "\"}";
				
				if(isNet.IsConnect())
				{
					ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
					postFlag = false;
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "请填写完整", Toast.LENGTH_SHORT).show();
			}
		}	
	}
}