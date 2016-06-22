package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	
	private String FILE_NAME = null;
	private Context ctx;
	
	public FileUtil(Context ctx, String FILE_NAME)
	{
		this.FILE_NAME = FILE_NAME;
		this.ctx = ctx;
		
		File targetFolder = new File(getAbsolutePath());
		if(!targetFolder.exists())
		{
			targetFolder.mkdirs();
		}
	}


	@SuppressLint("NewApi")
	public String getAbsolutePath() {
		File root = ctx.getExternalCacheDir();
		
		if (root != null)
			return root.getAbsolutePath();
		return null;
	}

	public void write(String result) {
		
		try{
			File targetFile = new File(getAbsolutePath() + FILE_NAME + ".bin");
			FileOutputStream fos = new FileOutputStream(targetFile);
			fos.write(result.getBytes());
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public String read() {
		
		
		try
		{
			
			File targetFile = new File(getAbsolutePath() + FILE_NAME + ".bin");
				
			if(!targetFile.exists())
			{
				return null;
			}
				
			FileInputStream fis = new FileInputStream(targetFile);
				
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				
			StringBuilder sb = new StringBuilder("");

			String line = "";
				
			while((line = br.readLine()) != null)
			{
				sb.append(line);
			}
				
			br.close();
			return sb.toString();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		
	}	
}
