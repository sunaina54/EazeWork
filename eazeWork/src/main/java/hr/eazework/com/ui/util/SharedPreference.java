package hr.eazework.com.ui.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import hr.eazework.com.application.MyApplication;

public class SharedPreference {

	private static final String SESSION_ID = "login";
	private static final String CORP_URL = "CORP_URL";

	public static String getSessionId() {
		SharedPreferences prefernces = MyApplication.getAppContext()
				.getSharedPreferences(SESSION_ID, Context.MODE_PRIVATE);
		return prefernces.getString(SESSION_ID, "");

	}

	public static void setSessionId(String sessionId) {
		SharedPreferences prefernces = MyApplication.getAppContext()
				.getSharedPreferences(SESSION_ID, Context.MODE_PRIVATE);
		Editor edit = prefernces.edit();
		edit.putString(SESSION_ID, sessionId);
		edit.commit();
	}
	public static String getCorpUrl() {
		SharedPreferences prefernces = MyApplication.getAppContext()
				.getSharedPreferences(CORP_URL, Context.MODE_PRIVATE);
		return prefernces.getString(CORP_URL, "");

	}

	public static void setCorpUrl(String corpUrl) {
		SharedPreferences prefernces = MyApplication.getAppContext()
				.getSharedPreferences(CORP_URL, Context.MODE_PRIVATE);
		Editor edit = prefernces.edit();
		edit.putString(CORP_URL, corpUrl);
		edit.commit();
	}

	public static void saveSharedPreferenceData(String projectPrefName,String contentPrefName,String content,Context context){
		try {
			SharedPreferences prefs = context.getSharedPreferences(projectPrefName,
					Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
			SharedPreferences.Editor prefEditor = prefs.edit();
			prefEditor.putString(contentPrefName, content);
			Log.i("", "User Prefrence : Data saved successfully");
			prefEditor.commit();
			prefEditor.clear();
		} catch (Exception e) {
			Log.i("", "Exception : " + e.toString());
		}
	}
	public static String getSharedPreferenceData(String projectPrefName,String contentPrefName,Context context){
		try {
			SharedPreferences prefs = context.getSharedPreferences(projectPrefName,
					Context.MODE_PRIVATE);
			String str = prefs.getString(contentPrefName, null);
			return str;
		} catch (Exception e) {

		}
		return null;
	}
}
