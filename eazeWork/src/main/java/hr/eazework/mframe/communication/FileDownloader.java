package hr.eazework.mframe.communication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;



import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import hr.eazework.com.R;

public class FileDownloader extends AsyncTask<String, Integer, Boolean> {

	private FileDownloadListener downloadListener;
	private Context context;
	private File mainfile;
	private String mAppName;

	public FileDownloader(Context context, FileDownloadListener downloadListener) {
		this.context = context;
		this.downloadListener = downloadListener;
		mAppName=context.getString(R.string.app_name);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String url = params[0];
		String fileName = params[1];
		boolean downloadStatus = downLoadFileFromNetwork(url, fileName);
		return downloadStatus;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		try {
		if (result) {
			if (downloadListener != null) {
				downloadListener.onFileDownloadComplete(result, Uri.fromFile(mainfile));
			} else {
				Toast.makeText(context, "File downloaded successfully",
						Toast.LENGTH_LONG).show();
			}
		} else {
			if (downloadListener != null) {
				downloadListener.onFileDownloadComplete(result, Uri.fromFile(mainfile));
			} else {
				Toast.makeText(context, "File downloaded failed",
						Toast.LENGTH_LONG).show();
			}
		}
		} catch(Exception exception){
			
		}
	}

	private boolean downLoadFileFromNetwork(String downloadUrl, String fileName) {
		boolean bm = false;
		if (!createFileOnDirectroy(fileName)) {
			return false;
		}
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fos = new FileOutputStream(mainfile);
			int current = 0;
			while ((current = bis.read()) != -1) {
				fos.write(current);
			}

			fos.flush();
			fos.close();
			bm = true;
		} catch (MalformedURLException e) {
			bm = false;
		} catch (OutOfMemoryError e) {
			bm = false;
		} catch (IOException e) {
			bm = false;
		} catch (Exception e) {
			bm = false;
		}
		return bm;
	}

	/*private boolean downLoadFileFromNetwork(String downloadUrl, String fileName) {
		boolean bm = false;
		if (!createFileOnDirectroy(fileName)) {
			return false;
		}
		try {
			URL url = new URL(downloadUrl);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			FileOutputStream fos = new FileOutputStream(mainfile);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			bm = true;
		} catch (MalformedURLException e) {
			bm = false;
		} catch (OutOfMemoryError e) {
			bm = false;
		} catch (IOException e) {
			bm = false;
		} catch (Exception e) {
			bm = false;
		}
		return bm;
	}*/

	private boolean createFileOnDirectroy(String fileName) {
		boolean isDir = false;
		File directory = null;
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			directory = new File(Environment.getDownloadCacheDirectory()
					+ File.separator + mAppName);
			if (directory.exists()) {
				isDir = true;
			} else {
				if (directory.mkdirs()) {
					isDir = true;
				} else {
					isDir = false;
				}
			}
		} else {
			directory = new File(Environment.getExternalStorageDirectory()
					+ File.separator + mAppName);
			if (directory.exists()) {
				isDir = true;
			} else {
				if (directory.mkdirs()) {
					isDir = true;
				} else {
					isDir = false;
				}
			}
		}

		if (isDir && directory != null) {
			mainfile = new File(directory, fileName);
//			if (vodafile.exists()) {
//				return true;
//			} else {
//				return false;
//			}
			if(mainfile!=null)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
