package thread;

import net.MyPost;
import android.os.Handler;
import android.os.Message;

public class HttpPostThread implements Runnable{
	
	private Handler handler;
	private String url = null;
	private String value;
	private String img = "";
	Message msg = new Message();
	
	public HttpPostThread(Handler handler, String url, String value)
	{
		this.handler = handler;
		this.url = url;
		this.value = value;
	}
	
	public HttpPostThread(Handler handler, String url, String value, String img)
	{
		this.handler = handler;
		this.url = url;
		this.value = value;
		this.img = img;
	}
	

	@Override
	public void run() {
		
		String result = null;
		if(img.equals(""))
		{
			result = MyPost.getRequest(url, value, null);
		}
		else
		{
			result = MyPost.getRequest(url, value, img);
		}
		
		msg.what = 0x123;
		msg.arg1 = 1;
		msg.obj = result;
		handler.sendMessage(msg);
	}

}
