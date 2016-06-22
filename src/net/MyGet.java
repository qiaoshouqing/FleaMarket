package net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 我的get请求方式工具类
 * */
public class MyGet {

	public String doGet(String url) throws ClientProtocolException,IOException{
		String result = null;// 我们的网络交互返回值
		try
		{
			HttpGet myGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000);
			httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					30 * 1000);
			HttpResponse httpResponse = httpClient.execute(myGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				return result;
			}
		}catch(Exception e)
		{
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
		return null;
	}

}
