package adapter;

import java.util.List;

import com.example.fleamarket.R;

import entity.CommentsEntity;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter{

	private List<CommentsEntity> listItems;
	LayoutInflater inflater;
	
	
	public CommentsAdapter(Activity activity, List<CommentsEntity> listItems)
	{
		this.listItems = listItems;
		inflater = activity.getLayoutInflater();
		
	}
	@Override
	public int getCount() {
		
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		convertView = inflater.inflate(R.layout.commentitem, null);
		TextView uname = (TextView) convertView.findViewById(R.id.uname);
		TextView ccontent = (TextView) convertView.findViewById(R.id.content);
		TextView ccreated_at = (TextView) convertView.findViewById(R.id.created_at);
		
		
		CommentsEntity be = listItems.get(position);
		uname.setText(be.getUname());
		ccontent.setText(be.getCcontent());
		ccreated_at.setText(be.getCcreated_at());
		
		return convertView;
	}

}
