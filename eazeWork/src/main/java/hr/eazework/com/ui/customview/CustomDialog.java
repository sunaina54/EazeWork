package hr.eazework.com.ui.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.lang.ref.WeakReference;

import hr.eazework.com.AddExpenseActivity;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.UserActionListner;


/** class to create the Custom Dialog **/
public class CustomDialog extends Dialog
{
	//initializations
	boolean isCancellable = true;
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 */
	private WeakReference<MainActivity> baseActivity;
	private WeakReference<AddExpenseActivity> expencebaseActivity;
	
	public CustomDialog(Context context, View view)
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
		if(context instanceof AddExpenseActivity)
			this.expencebaseActivity= new WeakReference<AddExpenseActivity >((AddExpenseActivity) context);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 */
	public CustomDialog(Context context, View view, int lpW, int lpH)
	{
		this(context, view, lpW, lpH, true);
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	/**
	 * Constructor 
	 * @param context
	 * @param view
	 * @param lpW
	 * @param lpH
	 * @param isCancellable
	 */
	public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable)
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
		this.isCancellable = isCancellable;
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	
	public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable, int style)
	{
		super(context, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
		this.isCancellable = isCancellable;
		if(context instanceof MainActivity)
		this.baseActivity= new WeakReference<MainActivity >((MainActivity) context);
	}
	
	@Override
	public void onBackPressed()
	{
		if(isCancellable)
			super.onBackPressed();
	}
	@Override
	public void setCanceledOnTouchOutside(boolean cancel) 
	{
		super.setCanceledOnTouchOutside(cancel);
		//
	}
	
	public void showCustomDialog(){
		try {
			if(baseActivity != null && baseActivity.get()!=null && !baseActivity.get().isFinishing()) {
                show();
            }else{
				show();
			}
		} catch (Exception e) {
			Crashlytics.log(1,getClass().getName(),"Error in Custom Dialog");
			Crashlytics.logException(e);
		}
	}

	public static void alertOkWithFinish(final Context mContext, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Alert");
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
						// mContext.startActivity(intent);
						((Activity)mContext).finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alertOkWithFinishFragment(final Context mContext, String msg, final UserActionListner mUserActionListener, final int action,boolean isMaterial) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(mContext,
				isMaterial ? R.style.MyDialogTheme
						: R.style.MyDialogThemeTransparent);

		final Dialog openDialog = new Dialog(ctw);
		openDialog.setCancelable(false);
		openDialog.setContentView(R.layout.material_dialog_layout);
		TextView tv_message = (TextView)openDialog.findViewById(R.id.tv_message);
		TextView tv_title = (TextView) openDialog.findViewById(R.id.tv_title);
		tv_title.setText("Confirmation");
		tv_message.setText(msg);
		TextView tv_cancel = (TextView) openDialog.findViewById(R.id.tv_cancel);
		tv_cancel.setVisibility(View.GONE);
		TextView tv_ok = (TextView) openDialog.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mUserActionListener.performUserAction(action, null, null);
				openDialog.dismiss();
			}
		});
		openDialog.show();
	}



	public static void alertWithOk(Context mContext, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Alert");
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alertOkWithFinishActivity(final Context mContext, String msg, final Activity activity, boolean isMaterial) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(mContext,
				isMaterial ? R.style.MyDialogTheme
						: R.style.MyDialogThemeTransparent);

		final Dialog openDialog = new Dialog(ctw);
		openDialog.setCancelable(false);
		openDialog.setContentView(R.layout.material_dialog_layout);
		TextView tv_message = (TextView)openDialog.findViewById(R.id.tv_message);
		TextView tv_title = (TextView) openDialog.findViewById(R.id.tv_title);
		tv_title.setText("Confirmation");
		tv_message.setText(msg);
		TextView tv_cancel = (TextView) openDialog.findViewById(R.id.tv_cancel);
		tv_cancel.setVisibility(View.GONE);
		TextView tv_ok = (TextView) openDialog.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//	mUserActionListener.performUserAction(action, null, null);
				Intent intent = new Intent();
				activity.setResult(1,intent);
				activity.finish();
				openDialog.dismiss();
			}
		});
		openDialog.show();
	}
}
