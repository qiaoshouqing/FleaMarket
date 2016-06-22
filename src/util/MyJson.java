package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import entity.CommentsEntity;
import entity.GoodsEntity;

public class MyJson {
/*
 * json解析类
 * @author: joyce
 */


	

	public List<GoodsEntity> getGoodsList(String result)
	{
		List<GoodsEntity> listItems = null;
		try
		{
			JSONArray jArray = new JSONArray(result);
			listItems = new ArrayList<GoodsEntity>();
			for(int i = 0;i < jArray.length();i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
				
				
				int gid = json_data.getInt("gid");
				int uid = json_data.getInt("uid");
				String uname = json_data.getString("uname");
				String uphone = json_data.getString("uphone");
				String gname = json_data.getString("gname");
				String gprice = json_data.getString("gprice");
				String gdescribe = json_data.getString("gdescribe");
				String gis_sold = json_data.getString("gis_sold");
				String gsort = json_data.getString("gsort");
				String gimage = json_data.getString("gimage");
				String gcreated_at = json_data.getString("gcreated_at");
				String ccount = json_data.getString("ccount");
				
				listItems.add(new GoodsEntity(gid, uid, uname, uphone, gname, gprice, gdescribe, gis_sold, gsort, gimage, gcreated_at, ccount));
				
                
			}
	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listItems;
	}
	
	public List<CommentsEntity> getCommentsList(String result)
	{
		List<CommentsEntity> listItems = null;
		try {
			JSONArray jArray = new JSONArray(result);
			listItems = new ArrayList<CommentsEntity>();
			for(int i = 0;i < jArray.length();i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
				
				int cid = json_data.getInt("cid");
				int uid = json_data.getInt("uid");
				int gid = json_data.getInt("gid");
				String uname = json_data.getString("uname");
				String ccontent = json_data.getString("ccontent");
				String ccreated_at = json_data.getString("ccreated_at");
				listItems.add(new CommentsEntity(cid, uid, gid, uname, ccontent, ccreated_at));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		 return listItems;
	 }
	
}
