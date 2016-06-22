package net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class MyPost {
	
		public static String getRequest(final String url, final String value, final String img)
		{
			String result = "";
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("value", value));
			params.add(new BasicNameValuePair("img", img));
			
			try{
				HttpClient httpClient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				
				HttpResponse response = httpClient.execute(httpPost);
			
				if(response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity httpEntity = response.getEntity();
					
					result = EntityUtils.toString(httpEntity);
					
					return result;
				}
				else
				{
					Log.e("log tag", "服务器返回响应失败");
				}
				
				
			}catch(Exception e)
			{
				Log.e("log_tag", "Error in http connection "+e.toString());
			}
			return null;
			
		}

}
