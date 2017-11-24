/*
package hr.eazework.selfcare.communication;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import hr.eazework.com.R;
import hr.eazework.com.ui.util.MLogger;
import hr.eazework.mframe.communication.ImageLoaderListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageLoader {
	// BaseAdapter mAdapter = null;
	public static HashMap<String, SoftReference<Bitmap>> Cache = new HashMap<String, SoftReference<Bitmap>>();
	public static Bitmap bannerImage;
	private Activity mActivity;
	public int imageLoadingCounter;

	public ImageLoader(Activity mActivity) {
		this.mActivity = mActivity;
	}

	private class WorkerThread implements Runnable {
		String url = null;
		String key = "";
		ImageView imageView;
		Bitmap bm;

		public WorkerThread(String url, String key, ImageView imageView) {
			this.url = url;
			this.key = key;
			this.imageView = imageView;
		}

		@Override
		public void run() {

			bm = getDrawble(this.url);
			if (bm == null) {
				bm = readDrawableFromNetwork(this.url,mActivity);
				Cache.put(key, new SoftReference<Bitmap>(bm));
			}
			mActivity.runOnUiThread(new Runnable() {
				@SuppressLint("NewApi")
				public void run() {
					imageLoadingCounter++;
					if (bm != null) {
						Drawable d = new BitmapDrawable(mActivity
								.getResources(), bm);
						int version = android.os.Build.VERSION.SDK_INT;
						if (version >= 16)
							imageView.setImageBitmap(bm);
						else
							imageView.setImageBitmap(bm);
						imageView.setVisibility(View.VISIBLE);
						imageView.invalidate();
						if (imageView.getTag() != null)
							((ImageLoaderListener) imageView
									.getTag()).onBitmapDownloaded(
									bm, imageView);
					} else {
						MLogger.debug(getClass().getName(),"Downloaded bitmap is null");
					}
				}
			});

		}
	}

	public Bitmap getDrawble(String url) {
		SoftReference<Bitmap> mReference = Cache.get(url);
		if (mReference != null) {
			imageLoadingCounter++;
			return mReference.get();
		} else {
			return null;
		}

	}

	*/
/* method to get Image if already or to start new task to download *//*

	public void loadImage(String url, ImageView imageView) {
		try {
			WorkerThread workerThread = new WorkerThread(url, url, imageView);
			Thread t = new Thread(workerThread);
			t.start();
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}

	}// end of method

	private static Bitmap readDrawableFromNetwork(String url, Context context) {
		final Bitmap[] bm = {null};
		Target loadtarget = null;


			if (loadtarget == null) loadtarget = new Target() {
				@Override
				public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
					// do something with the Bitmap
					bm[0] = bitmap;
				}

				@Override
				public void onBitmapFailed(Drawable errorDrawable) {

				}

				@Override
				public void onPrepareLoad(Drawable placeHolderDrawable) {

				}

			};

			Picasso.with(context).load(url).into(loadtarget);


		*/
/*try {
			MLogger.debug("image url::::::::", "image url:::::::::" + url);
			HttpGet httpRequest = null;
			httpRequest = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream instream = bufHttpEntity.getContent();
			bm = BitmapFactory.decodeStream(instream);
			// URL Url = new URL(url);
			// InputStream is = (InputStream) Url.getContent();
			return bm;
		} catch (MalformedURLException e) {
			MLogger.error("ImageLoader", e.getMessage());
			bm = null;
		} catch (OutOfMemoryError e) {
			MLogger.error("ImageLoader", e.getMessage());
			bm = null;
		} catch (IOException e) {
			MLogger.error("ImageLoader", e.getMessage());
			bm = null;
		} catch (Exception e) {
			MLogger.error("ImageLoader", e.getMessage());
			bm = null;
		}*//*

		return bm[0];
	}// end of method
}
*/
