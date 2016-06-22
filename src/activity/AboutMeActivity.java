package activity;

import java.io.IOException;

import com.example.fleamarket.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AboutMeActivity extends Activity implements OnClickListener{
	
	private TextView title;
	private TextView refresh_post;
	private TextView content;
	//private Context ctx = AboutMeActivity.this;
	private TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutmeactivity);
		
		title = (TextView) findViewById(R.id.title_bar_name);
		back = (TextView) findViewById(R.id.back);
		refresh_post = (TextView) findViewById(R.id.refresh_post);
		back.setOnClickListener(this);
		
		title.setText("关于");
		refresh_post.setText("");
		
		content = (TextView) findViewById(R.id.content);
		content.setText(R.string.mycontent);
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
		}
	}

}
