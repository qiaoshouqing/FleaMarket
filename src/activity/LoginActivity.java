package activity;

import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;


//import com.baidu.android.pushservice.PushConstants;
//import com.baidu.android.pushservice.PushManager;


import util.Encrype;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements /*PlatformActionListener, */OnClickListener{
	
	private String url = "http://glimpse.sinaapp.com/flogin_ok_android.php";
	private EditText phone;
	private EditText password;
	private Button post;
	private Button register;
	private String myPhone;
	private String myPassword;
	private IsNet isNet;
	private Context context = LoginActivity.this;
	
	
	Handler handler = new Handler()
	{
		private String decryptingCode;

		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 0x123)
			{
				String result = (String) msg.obj;
				
				//Log.i("resultlogin", result);
				
				if(result.contains("登陆成功"))
				{
					Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
					
					String suid[] = result.split("#");
					String uid = suid[0];
					String uname = suid[1];
					String encryptuid = null;
					try {
						encryptuid = Encrype.encrypt("joyce",uid);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					SharedPreferences preferences;
					SharedPreferences.Editor editor;
					preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
					editor = preferences.edit();
					editor.putString("uid", encryptuid);
					editor.putString("uname", uname);
					editor.commit();
					
					finish();
					
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences preferences = getSharedPreferences("glimpse", MODE_PRIVATE);
		String encryptuid = preferences.getString("uid", null);
        if(encryptuid != null)
        {
        	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			
			finish();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.loginactivity);
		
		phone = (EditText) findViewById(R.id.phone);
		password = (EditText) findViewById(R.id.password);
		post = (Button) findViewById(R.id.post);
		register = (Button) findViewById(R.id.register);
		
		isNet = new IsNet(getApplicationContext());
		
		post.setOnClickListener(this);
		
		register.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id)
		{
		case R.id.post:
			myPhone = phone.getText().toString();
			myPassword = password.getText().toString();
			
			String value = "{\"phone\":\"" + myPhone + "\",\"password\":\""
					+ myPassword + "\"}";
			
			if(isNet.IsConnect())
			{
				ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
			}
			break;
		case R.id.register:
			Intent intent1 = new Intent(LoginActivity.this, IdentifyPhoneActivity.class);
			startActivity(intent1);
			break;
			
		}
		
	}
}