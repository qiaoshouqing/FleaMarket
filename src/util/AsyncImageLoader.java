package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

/**
 * ͼƬ�첽������
 * 
 * @author Leslie.Fang
 * 
 */
@SuppressLint("NewApi")
public class AsyncImageLoader {
	//软引用
	private Map<String, SoftReference<Bitmap>> softCaches = null;
	//文件缓存 DiskLruCache
	private FileUtiles fileUtiles;

	public AsyncImageLoader(Context context) {
		softCaches = new HashMap<String, SoftReference<Bitmap>>();
		fileUtiles = new FileUtiles(context);
	}


	public Bitmap getBitmapFromSoft(String url) {
		if (softCaches.containsKey(url)) {
			//װ
			SoftReference<Bitmap> soft = softCaches.get(url);
			
			Bitmap bit = soft.get();
			if (bit != null)
				return bit;
			
		}
		return null;
		
	}

	/**
	 *
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void putBitmapToSoft(String url, Bitmap bitmap) {
		softCaches.put(url, new SoftReference<Bitmap>(bitmap));
	}
	
	

	/**
	 *
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromDisk(String fileName, String filePath) {
		
		Bitmap bitmap = null;
		
		if(fileUtiles.isBitmap(fileName))
		{
			bitmap = BitmapFactory.decodeFile(filePath);
		}
		return bitmap;
	}

	/**
	 * 
	 * 
	 * @param imageView
	 * @param imageUrl
	 */
	public Bitmap loadImage(ImageView imageView, String imageUrl) {
		
		
		//
		Bitmap bitmap = getBitmapFromSoft(imageUrl);

		if (bitmap != null) {
			Log.i("leslie", "image exists in memory");
			return bitmap;
		}
		
		
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
		String filePath = fileUtiles.getAbsolutePath() + "/" + fileName;
		//
		bitmap = getBitmapFromDisk(fileName, filePath);
		if (bitmap != null) {
			Log.i("leslie", "image exists in file");
			//
			putBitmapToSoft(imageUrl, bitmap);
			return bitmap;
		}

		//
		if (!TextUtils.isEmpty(imageUrl)) {
			new ImageDownloadTask(imageView).execute(imageUrl, fileName);
		}

		return null;
	}

	class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
		private String imageUrl;
		private ImageView imageView;
		private String fileName;

		public ImageDownloadTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				imageUrl = params[0];
				fileName = params[1];
				
				URL url = new URL(imageUrl);
				
				InputStream is = url.openStream();
				
				BitmapFactory.Options op = new BitmapFactory.Options();
				op.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeStream(is, null,
						op);
				
				
				if (bitmap != null) {
					Log.i("qsq", "image from server");
					//将图片放入软引用
					putBitmapToSoft(imageUrl, bitmap);
					//将图片存入文件
					fileUtiles.saveBitmap(fileName, bitmap);
				}
				else
				{
					Log.i("tip", "图片获取失败");
				}
				
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				//
				if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
					imageView.setImageBitmap(result);
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
}