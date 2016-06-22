package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


@SuppressLint("NewApi")
public class FileUtiles {
	
	private Context ctx;

	public FileUtiles(Context ctx) {
		this.ctx = ctx;
	}

	
	public String getAbsolutePath() {
		File root = ctx.getExternalFilesDir(null);
		
		if (root != null)
			return root.getAbsolutePath();
		return null;
	}

	
	public boolean isBitmap(String name) {
		File root = ctx.getExternalFilesDir(null);
		
		File file = new File(root, name);
		return file.exists();
	}

	
	public void saveBitmap(String name, Bitmap bitmap) {
		if (bitmap == null)
			return;
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.i("tip", "sdcard卡不可用");
			return;
		}
		
		String BitPath = getAbsolutePath() + "/" + name;
		
		try {
			FileOutputStream fos = new FileOutputStream(BitPath);
			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
