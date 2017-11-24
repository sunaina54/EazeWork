package hr.eazework.mframe.communication;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface ImageLoaderListener {
	public void onBitmapDownloaded(Bitmap bitmap, ImageView imageview);
}
