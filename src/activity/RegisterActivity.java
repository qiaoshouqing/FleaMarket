package activity;


import com.example.fleamarket.R;

import net.IsNet;
import thread.HttpPostThread;
import thread.ThreadPoolUtils;
import util.Encrype;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private String url = "http://glimpse.sinaapp.com/fregister_ok_android.php";
	private Context context = RegisterActivity.this;
	private EditText uname;
	private EditText upassword;
	private Button post;
	
	
	private String myUname = null;
	private String myUpassword = null;
	private String phone = null;
	
	private IsNet isNet;
	
	
	Handler handler = new Handler()
	{

		public void handleMessage(Message msg) 
		{
			if(msg.what == 0x123)
			{
				String result = (String) msg.obj;
				
				//Log.i("resultlogin", result);
				
				if(result.contains("注册成功"))
				{
					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
					
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
					
					Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	private EditText confirmPassword;
	private String myConfirmPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registeractivity);
		
		isNet = new IsNet(getApplicationContext());
		phone = getIntent().getStringExtra("phone");
		
		uname = (EditText) findViewById(R.id.uname);
		upassword = (EditText) findViewById(R.id.upassword);
		confirmPassword = (EditText) findViewById(R.id.confirmupassword);
		post = (Button) findViewById(R.id.post);
		
		
		post.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.post:
			myUname = uname.getText().toString();
			myUpassword = upassword.getText().toString();
			myConfirmPassword = confirmPassword.getText().toString();
			
			if(myUname != null && myUpassword != null && phone != null)
			{
				if(myUpassword.equals(myConfirmPassword))
				{
					String value = "{\"phone\":\"" + phone + "\",\"password\":\""
							+ myUpassword + "\",\"name\":\""
							+ myUname + "\"}";
					
					if(isNet.IsConnect())
					{
						ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
					}
				}
				else
				{
					Toast.makeText(context, "两次输入的密码不同", Toast.LENGTH_SHORT).show();
				}
				
			}
			else
			{
				Toast.makeText(context, "请填写完整", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		
	}
	

}
