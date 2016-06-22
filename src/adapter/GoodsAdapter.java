package adapter;

import java.util.List;

import com.example.fleamarket.R;

import util.AsyncImageLoader;
import entity.GoodsEntity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsAdapter extends BaseAdapter{
	
	private List<GoodsEntity> listItems;
	private LayoutInflater inflater;
	private AsyncImageLoader asyncImageLoader;
	private Bitmap photobitmap;
	private String imageUrl;
	
	public GoodsAdapter(Activity activity,List<GoodsEntity> listItems){
		
		asyncImageLoader = new AsyncImageLoader(activity);
		inflater = activity.getLayoutInflater();
		this.listItems = listItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.goodsitem, null);
			TextView uname = (TextView) convertView.findViewById(R.id.uname);
			TextView gcreated_at = (TextView) convertView.findViewById(R.id.gcreated_at);
			TextView gname = (TextView) convertView.findViewById(R.id.gname);
			//TextView gis_sold = (TextView) convertView.findViewById(R.id.gis_sold);
			ImageView gimage = (ImageView) convertView.findViewById(R.id.gimage);
			TextView gprice = (TextView) convertView.findViewById(R.id.gprice);
			TextView ccount = (TextView) convertView.findViewById(R.id.ccount);
			
			holder = new ViewHolder();
			holder.uname = uname;
			holder.gcreated_at = gcreated_at;
			holder.gname = gname;
			//holder.gis_sold = gis_sold;
			holder.gimage = gimage;
			holder.gprice = gprice;
			holder.ccount = ccount;
			
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		GoodsEntity pe = listItems.get(position);
		imageUrl = pe.getGimage();
		
		holder.uname.setText(pe.getUname());
		holder.gcreated_at.setText(pe.getGcreated_at());
		holder.gname.setText(pe.getGname());
		//holder.gis_sold.setText(pe.getGis_sold());
		holder.gimage.setImageResource(R.drawable.background);
		holder.gimage.setTag(imageUrl);
		holder.gprice.setText(pe.getGprice()+"￥");
		holder.ccount.setText("交流："+pe.getCcount());
		
		photobitmap = asyncImageLoader.loadImage(holder.gimage, imageUrl);
		if(photobitmap != null)
		{
			holder.gimage.setImageBitmap(photobitmap);
		}
		
		return convertView;
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
	
	class ViewHolder {
		
		TextView uname;
		TextView gcreated_at;
		TextView gname;
		//TextView gis_sold;
		ImageView gimage;
		TextView gprice;
		TextView ccount;
	}
}
