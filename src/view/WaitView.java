package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class WaitView extends View{
	
	private Matrix matrix = new Matrix();

	public WaitView(Context context) {
		super(context);
		
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		canvas.drawColor(Color.TRANSPARENT);
		//Paint paint = new Paint();
		
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fat_po_f01);
		//int width = bitmap.getWidth();
		//int height = bitmap.getHeight();
		
		
		
		//matrix.setRotate(90);
		//bitmap = Bitmap.createBitmap(bitmap, 0,0, width, height, matrix, true);
		//canvas.drawBitmap(bitmap, 100, 100, paint);
		
		invalidate();
	}

}
