package hr.eazework.com.ui.util;

import android.util.Log;


import hr.eazework.com.R;
import hr.eazework.com.application.MyApplication;

public final class MLogger {

	public static final String APP_TAG = MyApplication.getAppContext().getResources().getString(R.string.app_name);
	private static final boolean logEnabled = AppConfig.IS_UNDER_DEVELOPMENT;

	public static void debug(String tag, String message) {
		  if (logEnabled) {

		   if (tag == null && message != null)
		    Log.d(MyApplication.getAppContext()
		      .getString(R.string.app_name), message);
		   else if (message != null)
		    Log.d(tag, message);
		  }
		 }

		 public static void error(String tag, String message) {
		  if (logEnabled) {
		   if (tag == null && message != null)
		    Log.e("Error :: "
		      + MyApplication.getAppContext().getString(
		        R.string.app_name), message);
		   else if (message != null)
		    Log.e("Error :: " + tag, message);
		  }
		 }
}
