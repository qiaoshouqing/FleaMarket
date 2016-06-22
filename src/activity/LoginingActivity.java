package activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.fleamarket.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class LoginingActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.loginingactivity);
		
//	      PushManager.startWork(getApplicationContext(),
//	      PushConstants.LOGIN_TYPE_API_KEY,
//	      "9r3f2F6krarhLRCGPCGcOGk1");
		
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				
				Intent intent = new Intent(LoginingActivity.this, LoginActivity.class);
				startActivity(intent);
				
				finish();
			}
		};
		timer.schedule(task, 3 * 1000);
	}

}
