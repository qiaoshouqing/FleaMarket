package util;

import com.example.fleamarket.R;

import activity.BicyclesActivity;
import activity.BooksActivity;
import activity.ClothsActivity;
import activity.MainActivity;
import activity.MyActivity;
import activity.NoticeActivity;
import activity.OthersActivity;
import activity.GoodsAddActivity;
import activity.SearchActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SlideOnClickListener implements OnClickListener{
	
	private Context context;
	private Intent intent = new Intent();
	
	public SlideOnClickListener(Context context) {
		
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch(id)
		{
		case R.id.main:
			intent.setClass(context.getApplicationContext(), MainActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.other:
	        intent.setClass(context.getApplicationContext(), OthersActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.books:
			intent.setClass(context.getApplicationContext(), BooksActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.bicycles:
			intent.setClass(context.getApplicationContext(), BicyclesActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.cloths:
			intent.setClass(context.getApplicationContext(), ClothsActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.search:
			intent.setClass(context.getApplicationContext(), SearchActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.add:
			intent.setClass(context.getApplicationContext(), GoodsAddActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.notice:
			intent.setClass(context.getApplicationContext(), NoticeActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		case R.id.my:
			intent.setClass(context.getApplicationContext(), MyActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.getApplicationContext().startActivity(intent);
			break;
		
		}
		
	}
}
