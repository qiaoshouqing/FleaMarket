package thread;

import java.io.IOException;

import net.MyGet;
import org.apache.http.client.ClientProtocolException;

import android.os.Handler;
import android.os.Message;

/**
 * 网络Get请求的线�?
 * */
public class HttpGetThread implements Runnable {

	private Handler handler;
	private String url;
	private MyGet myGet = new MyGet();
	Message msg = new Message();

	public HttpGetThread(Handler handler, String endParamerse) {
		this.handler = handler;
		// 拼接访问服务器完整的地址
		//url = Model.HTTPURL + endParamerse;
		this.url = endParamerse;
	}

	@Override
	public void run() {

		try {
			String result = myGet.doGet(url);
			msg.what = 0x123;
			msg.obj = result;
		} catch (ClientProtocolException e) {
			msg.what = 0x404;
		} catch (IOException e) {
			msg.what = 0x100;
		}
		handler.sendMessage(msg);
		
	}
}
