package theleatherguy.epiandroid.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;

import com.squareup.picasso.Transformation;

import theleatherguy.epiandroid.R;

public class RoundedAvatar implements Transformation
{
	@Override
	public Bitmap transform(Bitmap source)
	{
		if (source == null || source.isRecycled())
		{
			return null;
		}

		final int borderwidth = 0;
		final int bordercolor = 0x32A0D2;
		final int width = source.getWidth() + borderwidth;
		final int height = source.getHeight() + borderwidth;

		Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		Canvas canvas = new Canvas(canvasBitmap);
		float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
		canvas.drawCircle(width / 2, height / 2, radius, paint);

		//border code
		paint.setShader(null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(bordercolor);
		paint.setStrokeWidth(borderwidth);
		canvas.drawCircle(width / 2, height / 2, radius - borderwidth / 2, paint);
		//--------------------------------------

		if (canvasBitmap != source)
		{
			source.recycle();
		}

		return canvasBitmap;
	}

	@Override
	public String key()
	{
		return "circle";
	}
}