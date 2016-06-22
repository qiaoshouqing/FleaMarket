package activity;

import static cn.smssdk.framework.utils.R.getStringRes;

import com.example.fleamarket.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IdentifyPhoneActivity extends Activity implements OnClickListener {

	
	// 填写从短信SDK应用后台注册得到的APPKEY 
	private static String APPKEY = "7ebfc0f5d51c";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "452126bddb06693bdac83aef3ec2ccc6";//
	public String phString;                                              //3684fd4f0e16d68f69645af1c7f8f5bd
	private EditText phoneNum;
	private Button getIdentifyCode;
	private EditText identifyCode;
	private Button identify;
	private TextView tip;
	
	private Context context = IdentifyPhoneActivity.this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.identifyphone);
		
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		getIdentifyCode = (Button) findViewById(R.id.getIdentifyCode);
		identifyCode = (EditText) findViewById(R.id.identifyCode);
		identify = (Button) findViewById(R.id.identify);
		tip = (TextView)findViewById(R.id.tip);
		
		getIdentifyCode.setOnClickListener(this);
		identify.setOnClickListener(this);
		
		
		//System.loadLibrary("OSbase");
		SMSSDK.initSDK(this,APPKEY,APPSECRET);
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eh);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.getIdentifyCode://获取验证码
			if(!TextUtils.isEmpty(phoneNum.getText().toString())){
				SMSSDK.getVerificationCode("86",phoneNum.getText().toString());
				phString=phoneNum.getText().toString();
			}else {
				Toast.makeText(this, "电话不能为空", 1).show();
			}
			
			break;
		case R.id.identify://校验验证码
			if(!TextUtils.isEmpty(identifyCode.getText().toString())){
				SMSSDK.submitVerificationCode("86", phString, identifyCode.getText().toString());
			}else {
				Toast.makeText(this, "验证码不能为空", 1).show();
			}
			break;
		default:
			break;
		}
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			//Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
					tip.setText("提交验证码成功");
					
					Intent intent = new Intent(context, RegisterActivity.class);
					intent.putExtra("phone", phString);
					startActivity(intent);
					finish();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
					tip.setText("验证码已发送");
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(context, "smssdk_network_error");
				Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
				if (resId > 0) {
					Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	

}
