package hr.eazework.com.ui.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.application.MyApplication;
import hr.eazework.com.model.Chips;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.ExpenseItemListModel;
import hr.eazework.com.model.LineItemColumnsItem;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MappedEmployee;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.fragment.CameraActivity;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.UserActionListner;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ChipViewRecyclerAdapter;

public class Utility {
   static ArrayList<Long> list = new ArrayList<>();
    private static final String TAG = Utility.class.getName();
    public static int deviceHeight;
    static BroadcastReceiver onComplete=null;

    public static double maxLimit=1;
    public static String sizeMsg ="File size should not be greater than "+maxLimit +" MB";

    public static int calcBase64SizeInKBytes(String base64String) {
       /* Double result = -1.0;
        if(base64String!=null) {
            Integer padding = 0;
            if(base64String.endsWith("==")) {
                padding = 2;
            }
            else {
                if (base64String.endsWith("=")) padding = 1;
            }
            result = (Math.ceil(base64String.length() / 4) * 3 ) - padding;
        }
        return result / 1000;*/
       int result = (base64String.length()*3/4)/(1048576);
       return result;
    }

    public static boolean isNotLowerVersion(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }

    public static boolean isInclusiveAndAboveMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isNetwork = false;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected()) {
                isNetwork = true;
            }
        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
            Crashlytics.logException(e);
        }

        return isNetwork;
    }

    public static boolean isLocationEnabled(final Context context) {
        LocationManager lm = null;
        try {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
            Crashlytics.logException(e);
        }
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Crashlytics.log(1, MainActivity.TAG, e.getMessage());
            Crashlytics.logException(e);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Crashlytics.log(1, MainActivity.TAG, e.getMessage());
            Crashlytics.logException(e);
        }

        if (!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Animator[] getAnimators(View view, boolean isIn) {
        if (isIn) {
            return new Animator[]{ /*ObjectAnimator.ofFloat(view,
                    "translationY", Utilities.deviceHeight - view.getY(), 0),*/
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                    ObjectAnimator.ofFloat(view, "scaleX", 0, 1),
                    ObjectAnimator.ofFloat(view, "scaleY", 0, 1),
             /*ObjectAnimator.ofFloat(view, "translationX",
             view.getRootView().getWidth(), 0)*/
            };
        } else {
            return new Animator[]{ /*ObjectAnimator.ofFloat(
                    view,
					"translationY",
					view.getY()>view.getHeight()?-view.getY():-view.getHeight() , 0),*/
                    ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 0),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 0),
			/* ObjectAnimator.ofFloat(view, "translationX",
			 0, -view.getRootView().getWidth())*/
            };
        }
    }

    public static Date getDateFromString(String dateString, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            return format.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Calendar.getInstance(Locale.ENGLISH).getTime();
        }
    }

    public static void navigateToTeamOrHome(UserActionListner mUserActionListner) {
        MenuItemModel model = ModelManager.getInstance().getMenuItemModel().getItemModel(MenuItemModel.TEAM_KEY);
        if (model != null && model.isAccess()) {
            TeamMember.setmCurrentEmpId("");
            mUserActionListner.performUserAction(IAction.TEAM_MEMBER_LIST, null, null);
        } else {
            mUserActionListner.performUserAction(IAction.HOME_VIEW, null, null);
        }
    }

    public static void displayMessage(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openCamera(Activity activity, Fragment fragment, int cameraMode, String purpose) {
        Intent i = new Intent(activity, CameraActivity.class);
        i.putExtra("camera_key", cameraMode);
        i.putExtra("purpose", purpose);
        fragment.startActivityForResult(i, AppsConstant.REQ_CAMERA);
    }

    public static void openCamera(Activity activity, Fragment fragment, int cameraMode, String purpose,String screenName) {
        Intent i = new Intent(activity, CameraActivity.class);
        i.putExtra("camera_key", cameraMode);
        i.putExtra("purpose", purpose);
        i.putExtra("screen",screenName);
        fragment.startActivityForResult(i, AppsConstant.REQ_CAMERA);
    }

    public static String LogUserDetails() {
        EmployeeProfileModel profileModel = ModelManager.getInstance().getEmployeeProfileModel();
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();

        String empProfileDetails = "";
        String empIdDetails = "";

        if (profileModel != null) {
            empProfileDetails = "Employee Profile Details : " + profileModel.toString();
        }
        if (loginUserModel != null && loginUserModel.getUserModel() != null) {
            empIdDetails = "Logged in User Details : " + loginUserModel.getUserModel().toString();
        }

        return empProfileDetails + " " + empIdDetails;
    }


    public static void addElementToView(Activity activity, LinearLayout empFieldLayout, String label, String value) {
        View emailView = LayoutInflater.from(activity).inflate(R.layout.profile_item_view, null, false);
        ((TextView) emailView.findViewById(R.id.tv_item_title)).setText(label);
        ((TextView) emailView.findViewById(R.id.tv_item_value)).setText(value);
        empFieldLayout.addView(emailView);
    }

    public static LinearLayout addElementToView(Activity activity, LinearLayout empFieldLayout, String label, String value,String extra) {
        View emailView = LayoutInflater.from(activity).inflate(R.layout.profile_item_view, null, false);
        ((TextView) emailView.findViewById(R.id.tv_item_title)).setText(label);
        ((TextView) emailView.findViewById(R.id.tv_item_value)).setText(value);
        empFieldLayout.addView(emailView);
        return empFieldLayout;
    }

    public static void requestToEnableGPS(final Context context, Preferences preferences) {
        preferences.remove(Preferences.USERLATITUDE);
        preferences.remove(Preferences.USERLONGITUDE);
        preferences.commit();
        new AlertCustomDialog(context, "Location is not enabled",
                "Open Settings",
                context.getString(R.string.dlg_cancel),
                context.getString(R.string.dlg_confirm), new AlertCustomDialog.AlertClickListener() {

            @Override
            public void onPositiveBtnListener() {
                Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
            }

            @Override
            public void onNegativeBtnListener() {
                // TODO Auto-generated method stub

            }
        });
    }

    public static void showNetworkNotAvailableDialog(Context context) {
        new AlertCustomDialog(context, context.getString(R.string.msg_internet_connection));
    }

    public static int getColor(Context context, int code) {
        if (isInclusiveAndAboveMarshmallow()) {
            return context.getColor(code);
        } else {
            return context.getResources().getColor(code);
        }
    }

    public static int getTextColorCode(Preferences preferences) {
        String textColor = preferences.getString(Preferences.HEADER_TEXT_COLOR, null);
        int color = Color.WHITE;

        try {
            color = !TextUtils.isEmpty(textColor) ? Color.parseColor(textColor) : Color.WHITE;
        } catch (Exception e) {
            Crashlytics.logException(e);
            color = Color.WHITE;
        } finally {
            return color;
        }
    }

    public static int getBgColorCode(Context context, Preferences preferences) {
        String bgColor = preferences.getString(Preferences.HEADER_BG_COLOR, null);
        int color = Utility.getColor(context, R.color.primary);
        try {
            color = !TextUtils.isEmpty(bgColor) ? Color.parseColor(bgColor) : Utility.getColor(context, R.color.primary);
        } catch (Exception e) {
            Crashlytics.logException(e);
            color = Utility.getColor(context, R.color.primary);
        } finally {
            return color;
        }
    }

    public static void setCorpBackground(Context context, View rootView) {
        Preferences preferences = new Preferences(context);
        int bgColorCode = Utility.getBgColorCode(context, preferences);
        int textColorCode = Utility.getTextColorCode(preferences);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.profile_header);
        ((TextView) rootView.findViewById(R.id.tv_profile_name)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_department)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_role)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_employee)).setTextColor(textColorCode);
        layout.setBackgroundColor(bgColorCode);
    }

    public static void saveEmpConfig(Preferences preferences) {
        EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();

        if (employeeProfileModel != null) {
            String geoFenceYN = employeeProfileModel.getmGeofencingYN();
            String selfieYN = employeeProfileModel.getmSelfieYN();
            String workLocationYN = employeeProfileModel.getmWorkLocationYN();

            if (!TextUtils.isEmpty(geoFenceYN) && geoFenceYN.equalsIgnoreCase("Y")) {
                preferences.saveBoolean(Preferences.ISONPREMISE, true);
            } else {
                preferences.saveBoolean(Preferences.ISONPREMISE, false);
            }

            if (!TextUtils.isEmpty(selfieYN) && selfieYN.equalsIgnoreCase("Y")) {
                preferences.saveBoolean(Preferences.SELFIEYN, true);
            } else {
                preferences.saveBoolean(Preferences.SELFIEYN, false);
            }

            if (!TextUtils.isEmpty(workLocationYN)) {
                preferences.saveString(Preferences.WORKLOCATION_YN, workLocationYN);
            }

            if (!TextUtils.isEmpty(workLocationYN) && workLocationYN.equalsIgnoreCase("Y")) {

                String workLocation = employeeProfileModel.getmWorkLocation();
                if (!TextUtils.isEmpty(workLocation)) {
                    preferences.saveString(Preferences.SITENAME, workLocation);
                } else {
                    preferences.saveString(Preferences.SITENAME, " ");
                }
            } else {

                String officeLocation = employeeProfileModel.getmOfficeLocation();
                if (!TextUtils.isEmpty(officeLocation)) {
                    preferences.saveString(Preferences.SITENAME, officeLocation);
                } else {
                    preferences.saveString(Preferences.SITENAME, " ");
                }
            }

            if (!TextUtils.isEmpty(employeeProfileModel.getmLatitude())) {
                preferences.saveString(Preferences.LATITUDE, employeeProfileModel.getmLatitude());
            } else {
                preferences.saveString(Preferences.LATITUDE, "");
            }

            if (!TextUtils.isEmpty(employeeProfileModel.getmLongitude())) {
                preferences.saveString(Preferences.LONGITUDE, employeeProfileModel.getmLongitude());
            } else {
                preferences.saveString(Preferences.LONGITUDE, "");
            }
            preferences.saveString(Preferences.HEADER_BG_COLOR, employeeProfileModel.getmHeaderBGColor());
            preferences.saveString(Preferences.HEADER_TEXT_COLOR, employeeProfileModel.getmHeaderTextColor());

            if (!TextUtils.isEmpty(employeeProfileModel.getmRadius())) {
                preferences.saveString(Preferences.SITE_RADIUS, employeeProfileModel.getmRadius());
            } else {
                preferences.saveString(Preferences.SITE_RADIUS, "0");
            }
            preferences.commit();
        }
    }

    public static String policyStatus(String policyId,String limitedTo, String input,String amount) {
        String status = "";
        double policyAmount = 0;
        if (policyId != null && !policyId.equalsIgnoreCase("")) {
            if ((limitedTo != null && !limitedTo.equalsIgnoreCase("")) &&
                    (input != null && !input.equalsIgnoreCase("")) && (amount != null && !amount.equalsIgnoreCase(""))) {

                policyAmount = Double.parseDouble(limitedTo) * Double.parseDouble(input);
                if (Double.parseDouble(amount) > policyAmount) {
                    status = "Exceeds Policy";
                    return status;
                } else {
                    status = "As Per Policy";
                    return status;
                }
            } else {
                status = "As Per Policy";
                return status;
            }
        } else {
            status = "No Policy";
            return status;
        }

    }
    public static void downloadPdf(String url,Uri uri, final String title, final Context context,final Activity activity) {
        if (!checkSelfPermission(context)) {
            Log.e("Permission error","You have permission");
            return;

        }
       DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri=null;
        if(url!=null) {
            Download_Uri = Uri.parse(url);
        }else{
            Download_Uri=uri;
        }

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        request.setDescription("Downloading " + title);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/FileEazeWork/" + title);


      Long  refid = downloadManager.enqueue(request);

       onComplete = new BroadcastReceiver() {

            public void onReceive(Context ctxt, Intent intent) {

                // get the refid from the download manager
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

// remove it from our list
                list.remove(referenceId);

// if list is empty means all downloads completed
                if (list.isEmpty())
                {

// show a notification
                    Log.e("INSIDE", "" + referenceId);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText("Download completed-(Download/FileEazeWork/"+title);


                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(455, mBuilder.build());
                 //   activity.unregisterReceiver(onComplete);

                }

            }
        };
        activity.
                registerReceiver(onComplete,
                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static boolean checkSelfPermission(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }else{
            return true;
        }

    }

    public static String converBitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void openPolicyStatusPopUp(LineItemsModel itemsModel,Context context,Preferences preferences){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.policy_popup_layout);
        int textColor = Utility.getTextColorCode(preferences);
        TextView unitTV,limitTV,policyAmountTV,claimAmountTV,statusTV;

        TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
        tv_header_text.setTextColor(textColor);
        tv_header_text.setText("Policy details for -" + itemsModel.getHeadDesc());
        int bgColor = Utility.getBgColorCode(context, preferences);
        FrameLayout fl_actionBarContainer = (FrameLayout) dialog.findViewById(R.id.fl_actionBarContainer);
        fl_actionBarContainer.setBackgroundColor(bgColor);

        unitTV=(TextView) dialog.findViewById(R.id.unitTV);
        limitTV=(TextView) dialog.findViewById(R.id.limitTV);
        policyAmountTV=(TextView) dialog.findViewById(R.id.policyAmountTV);
        claimAmountTV=(TextView) dialog.findViewById(R.id.claimAmountTV);
        statusTV=(TextView) dialog.findViewById(R.id.statusTV);


        unitTV.setText(itemsModel.getUnit());
        limitTV.setText(itemsModel.getPolicyLimitValue());
        if((itemsModel.getPolicyLimitValue()!=null && !itemsModel.getPolicyLimitValue().equalsIgnoreCase(""))
                && itemsModel.getInputUnit()!=null && !itemsModel.getInputUnit().equalsIgnoreCase("")){
            double policyAmount = Double.parseDouble(itemsModel.getPolicyLimitValue()) * Double.parseDouble(itemsModel.getInputUnit());
            policyAmountTV.setText(policyAmount+"");
        }else {
            policyAmountTV.setText(itemsModel.getClaimAmt());
        }

        claimAmountTV.setText(itemsModel.getClaimAmt());
        statusTV.setText(policyStatus(itemsModel.getPolicyID(),itemsModel.getPolicyLimitValue(),itemsModel.getInputUnit(),itemsModel.getClaimAmt()));

        (dialog).findViewById(R.id.ibRight).setVisibility(View.GONE);
        (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void setLineItemLabel(LinearLayout layout,LineItemsModel lineItem,ArrayList<LineItemColumnsItem> lineItemColumns){

    }
    public static void refreshLineItem(TextView currency,ArrayList<LineItemsModel> list){
        boolean isAllDeleted=true;
        for(LineItemsModel itemsModel : list){
            if(!itemsModel.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)){
                isAllDeleted=false;
                break;
            }
        }
        currency.setEnabled(true);
        if(!isAllDeleted){
           currency.setEnabled(false);
        }
    }

    public static void formatAmount(TextView textView,double amount){
        textView.setText( String.format("%.2f", amount ) );

    }

    public static ArrayList<ExpenseItemListModel> prepareFilterList(List<ExpenseItemListModel> list){
            // Set set1 = new LinkedHashSet(list);
            Set set = new TreeSet(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                   return  ((ExpenseItemListModel) o1).getReqStatus()-((ExpenseItemListModel) o2).getReqStatus();

                }
            });
            set.addAll(list);
            ArrayList<ExpenseItemListModel> newList = new ArrayList(set);
            return newList;
    }




}


