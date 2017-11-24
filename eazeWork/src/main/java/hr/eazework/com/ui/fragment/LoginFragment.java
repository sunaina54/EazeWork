package hr.eazework.com.ui.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CommonValues;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.caching.manager.DataCacheManager;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class LoginFragment extends BaseFragment {

    public static final String TAG = "LoginFragment";
    private TextView et_password;
    private Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = new Preferences(getContext());
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.login_fragment_container, container, false);
        rootView.findViewById(R.id.btn_sign_in).setOnClickListener(this);
        rootView.findViewById(R.id.img_reload).setOnClickListener(this);
        MainActivity.animateToVisible(rootView, -1);
        et_password = (TextView) rootView.findViewById(R.id.et_password);
        ((TextView) rootView.findViewById(R.id.tv_register)).setText(Html.fromHtml("<u>" + getString(R.string.dont_have_account) + "</u>"));
        rootView.findViewById(R.id.tv_register).setOnClickListener(this);
        rootView.findViewById(R.id.icon_eye).setOnTouchListener(
                new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // PRESSED
                                et_password.setTransformationMethod(null);
                                return true; // if you want to handle the touch
                            // event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_OUTSIDE:
                                // RELEASED
                                et_password
                                        .setTransformationMethod(PasswordTransformationMethod
                                                .getInstance());
                                return true; // if you want to handle the touch
                            // event
                        }
                        return false;
                    }

                });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(CommunicationConstant.EAZE_WORK_REQUEST_DEMO_URL));
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                break;
            case R.id.btn_sign_in:

                if (validate()) {
                    TextView url = (TextView) rootView.findViewById(R.id.et_url);
                    TextView uName = (TextView) rootView.findViewById(R.id.et_user_name);
                    String strUrl = url.getText().toString();
                    String sUrl = "";
                    int urlLength = strUrl.length();
                    if (urlLength >= 2 && strUrl.charAt(1) == '*') {
                        if (strUrl.charAt(0) == 't' || strUrl.charAt(0) == 'T') {
                            preferences.saveBoolean(Preferences.ISPRODUCTION, false);
                            preferences.saveBoolean(Preferences.ISTESTSERVER, true);
                            preferences.commit();
                            CommunicationConstant.setIsProduction(false);
                            CommunicationConstant.setIsTestServer(true);
                            int endlength = strUrl.length();
                            sUrl = strUrl.substring(2, endlength);
                            CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                        } else if (strUrl.charAt(0) == 's' || strUrl.charAt(0) == 'S') {
                            preferences.saveBoolean(Preferences.ISTESTSERVER, false);
                            preferences.saveBoolean(Preferences.ISPRODUCTION, false);
                            preferences.commit();
                            CommunicationConstant.setIsProduction(false);
                            CommunicationConstant.setIsTestServer(false);
                            int endlength = strUrl.length();
                            sUrl = strUrl.substring(2, endlength);
                            CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                        }
                    } else {
                        preferences.saveBoolean(Preferences.ISPRODUCTION, true);
                        preferences.saveBoolean(Preferences.ISTESTSERVER, false);
                        preferences.commit();
                        CommunicationConstant.setIsTestServer(false);
                        CommunicationConstant.setIsProduction(true);
                        sUrl = strUrl;
                        CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                    }
                    if (Utility.isNetworkAvailable(getContext())) {
                        showHideProgressView(true);
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.getLoginData(sUrl, uName.getText().toString(), et_password.getText().toString()),
                                CommunicationConstant.API_LOGIN_USER, true);
                    } else {
                        showHideNetworkErrorView(true);
                    }
                }
                break;
            case R.id.img_reload:
                showHideNetworkErrorView(false);
                View view = new View(getActivity());
                view.setId(R.id.btn_sign_in);
                view.performClick();
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    private boolean validate() {
        boolean validate = true;
        TextView url = (TextView) rootView.findViewById(R.id.et_url);
        TextView uName = (TextView) rootView.findViewById(R.id.et_user_name);
        TextView pass = (TextView) rootView.findViewById(R.id.et_password);

        if (pass.getText().toString().equals("")) {
            pass.setError(getString(R.string.enter_password));
            validate = false;
        }
        if (uName.getText().toString().equals("")) {
            uName.setError(getString(R.string.enter_username));
            validate = false;
        }
        if (url.getText().toString().equals("")) {
            url.setError(getString(R.string.enter_url));
            validate = false;
        }
        return validate;
    }

    @Override
    public void validateResponse(ResponseData response) {
        showHideProgressView(false);
        String responseData = response.getResponseData();
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_LOGIN_USER:

                try {
                    ModelManager.getInstance().setLoginUserModel(((new JSONObject(response.getResponseData())).getJSONObject("LogInUserResult")).toString());

                    TextView url = (TextView) rootView.findViewById(R.id.et_url);
                    DataCacheManager.getInstance().setDataCachingNoExpiry(CommonValues.DB_KEY_CORP_URL, url.getText().toString());
                    // SharedPreference.setCorpUrl(url.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                if (loginUserModel != null) {
                    if (loginUserModel.isLoggedIn()) {

                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getSallarySlipMonthData(),
                                CommunicationConstant.API_GET_MENU_DATA, true);


                    } else {
                        if(loginUserModel.getErroreCode() == -9) {
                            new AlertCustomDialog(getActivity(), loginUserModel.getErroreMessage(),"Upgrade",true,new AlertCustomDialog.AlertClickListener() {
                                @Override
                                public void onPositiveBtnListener() {
                                    final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException e) {
                                        Crashlytics.logException(e);
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }

                                @Override
                                public void onNegativeBtnListener() {

                                }
                            });
                        } else {
                            new AlertCustomDialog(getActivity(),loginUserModel.getErroreMessage());
                        }
                    }
                }
                break;
            case CommunicationConstant.API_USER_PROFILE_DETAILS:
                Log.d("TAG","Response Profile"+response.getResponseData());
                ModelManager.getInstance().setEmployeeProfileModel(response.getResponseData());
                ((TextView) rootView.findViewById(R.id.et_password)).setText("");
                ((TextView) rootView.findViewById(R.id.et_url)).setText("");
                ((TextView) rootView.findViewById(R.id.et_user_name)).setText("");
                Utility.saveEmpConfig(new Preferences(getContext()));
                preferences.saveBoolean(AppsConstant.ISFROMLOGIN,true);
                preferences.commit();
                mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                break;
            case CommunicationConstant.API_GET_MENU_DATA:
                try {
                    JSONObject getMenuDataResult = (new JSONObject(responseData)).optJSONObject("GetMenuDataResult");
                    JSONArray menuJsonArray = getMenuDataResult.optJSONArray("menuDataList");
                    ModelManager.getInstance().setMenuItemModel(menuJsonArray);
                    CommunicationManager.getInstance().sendPostRequest(this,
                            AppRequestJSONString.getLogOutData(),
                            CommunicationConstant.API_USER_PROFILE_DETAILS, true);

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                }


                break;
            default:
                break;
        }
        super.validateResponse(response);
    }
}
