package hr.eazework.com.application;

import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.ui.util.Utility;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
	public static final String TAG = MyApplication.class.getName();
	private static Context context;
	private static MyApplication application;
	private static String mDeviceId;
	// only lazy initializations here!

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		MyApplication.context = getApplicationContext();
		Utility.deviceHeight=context.getResources().getDisplayMetrics().heightPixels;
		mDeviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		application = this;
	}

	public static String getDeviceId(){
		return mDeviceId;
	}
	public static MyApplication getApplication() {
		return application;
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

	void showToast() {
	}/*
	public static void showHideActionBarView(boolean isShow){
		Intent intent=new Intent(
				BaseActivityReceiver.SHOW_HIDE_ACTION_BAR_ACTION);
		intent.putExtra(BaseActivityReceiver.IS_ACTIVE, isShow);
		application.sendBroadcast(intent);
	}
	public static void activeDeactiveSlider(boolean isActive){
		Intent intent=new Intent(
				BaseActivityReceiver.ACTIVE_DEACTIVE_SLIDER_ACTION);
		intent.putExtra(BaseActivityReceiver.IS_ACTIVE, isActive);
		application.sendBroadcast(intent);
	}
	public static void enableDisableBack(boolean isActive){
		Intent intent=new Intent(
				BaseActivityReceiver.ENABLE_DISABLE_BACK);
		intent.putExtra(BaseActivityReceiver.IS_ACTIVE, isActive);
		application.sendBroadcast(intent);
	}*/


}
