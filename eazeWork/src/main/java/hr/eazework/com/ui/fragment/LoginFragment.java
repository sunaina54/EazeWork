package hr.eazework.com.ui.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.Executor;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ForgotCredentialsRequestModel;
import hr.eazework.com.model.ForgotCredentialsResponseModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TimeModificationResponseModel;
import hr.eazework.com.ui.customview.CustomDialog;
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
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 234;
    private String email="";
    //creating a GoogleSignInClient object
    private GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;
    private SignInButton btn_gmail_sign_in;
    private Context context;
    private String fcmToken;
    private TextView forgotCredentialTV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        preferences = new Preferences(getContext());
        fcmToken=findToken();
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.login_fragment_container, container, false);
        rootView.findViewById(R.id.btn_sign_in).setOnClickListener(this);
        rootView.findViewById(R.id.img_reload).setOnClickListener(this);
        forgotCredentialTV = (TextView) rootView.findViewById(R.id.forgotCredentialTV);
        forgotCredentialTV.setOnClickListener(this);
        btn_gmail_sign_in = (SignInButton) rootView.findViewById(R.id.btn_gmail_sign_in);
        btn_gmail_sign_in.setOnClickListener(this);
        TextView textView = (TextView) btn_gmail_sign_in.getChildAt(0);
        textView.setText(R.string.continue_with_google);
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

        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        return rootView;
    }

    private String findToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token "+ token);
       // token="value";
        String prefrenceToken=preferences.getString(AppsConstant.FCM_TOKEN,null);
        if(prefrenceToken==null){
            preferences.saveString(AppsConstant.FCM_TOKEN, token);
        }else if(prefrenceToken!=null  && !prefrenceToken.equalsIgnoreCase(token)){
            preferences.saveString(AppsConstant.FCM_TOKEN, token);
        }
        return preferences.getString(AppsConstant.FCM_TOKEN, token);
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
            case R.id.forgotCredentialTV:
                TextView url1 = (TextView) rootView.findViewById(R.id.et_url);
                String strUrl1 = url1.getText().toString();
                String sUrl1 = "";
                int urlLength1 = strUrl1.length();
                if (urlLength1 >= 2 && strUrl1.charAt(1) == '*') {
                    if (strUrl1.charAt(0) == 't' || strUrl1.charAt(0) == 'T') {
                        preferences.saveBoolean(Preferences.ISPRODUCTION, false);
                        preferences.saveBoolean(Preferences.ISTESTSERVER, true);
                        preferences.commit();
                        CommunicationConstant.setIsProduction(false);
                        CommunicationConstant.setIsTestServer(true);
                        int endlength = strUrl1.length();
                        sUrl1 = strUrl1.substring(2, endlength);
                        CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                    } else if (strUrl1.charAt(0) == 's' || strUrl1.charAt(0) == 'S') {
                        preferences.saveBoolean(Preferences.ISTESTSERVER, false);
                        preferences.saveBoolean(Preferences.ISPRODUCTION, false);
                        preferences.commit();
                        CommunicationConstant.setIsProduction(false);
                        CommunicationConstant.setIsTestServer(false);
                        int endlength = strUrl1.length();
                        sUrl1 = strUrl1.substring(2, endlength);
                        CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                    }
                } else {
                    preferences.saveBoolean(Preferences.ISPRODUCTION, true);
                    preferences.saveBoolean(Preferences.ISTESTSERVER, false);
                    preferences.commit();
                    CommunicationConstant.setIsTestServer(false);
                    CommunicationConstant.setIsProduction(true);
                    sUrl1 = strUrl1;
                    CommunicationConstant.setServerLocation(CommunicationConstant.isTestServer(), CommunicationConstant.isProduction());
                }
                if (sUrl1.equalsIgnoreCase("")) {
                    new AlertCustomDialog(getContext(), getResources().getString(R.string.enter_url));
                    return;
                }
                forgotCredentialsPopup();
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
                                AppRequestJSONString.getLoginData(sUrl, uName.getText().toString(), et_password.getText().toString(),fcmToken),
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
            case R.id.btn_gmail_sign_in:
                signIn();
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    private void forgotCredentialsPopup() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reject_layout);

        final EditText messageTV = (EditText) dialog.findViewById(R.id.messageTV);

        final TextView cancelTV, okTV,tv_title;
        messageTV.setLines(1);
        tv_title= (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(context.getResources().getString(R.string.mail_label));
        okTV = (TextView) dialog.findViewById(R.id.okTV);
        cancelTV = (TextView) dialog.findViewById(R.id.cancelTV);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = messageTV.getText().toString();
                if (email.equalsIgnoreCase("")) {
                    new AlertCustomDialog(getContext(), getResources().getString(R.string.enter_mail));
                    return;
                }
                sendForgotCredentialsData(email);
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    private void sendForgotCredentialsData(String email){
        ForgotCredentialsRequestModel item=new ForgotCredentialsRequestModel();
        item.setEmail(email);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.forgotCredentialsRequest(item),
                CommunicationConstant.API_FORGOT_CREDENTIALS_REQUEST, true);
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
                    Log.d("TAG", "Login Response :" + ((new JSONObject(response.getResponseData())).getJSONObject("LogInUserResult")).toString());
                    TextView url = (TextView) rootView.findViewById(R.id.et_url);
                    DataCacheManager.getInstance().setDataCachingNoExpiry(CommonValues.DB_KEY_CORP_URL, url.getText().toString());
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
                        if (loginUserModel.getErroreCode() == -9) {
                            new AlertCustomDialog(getActivity(), loginUserModel.getErroreMessage(), "Upgrade", true, new AlertCustomDialog.AlertClickListener() {
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
                            new AlertCustomDialog(getActivity(), loginUserModel.getErroreMessage());
                        }
                    }
                }
                break;
            case CommunicationConstant.API_USER_PROFILE_DETAILS:
                Log.d("TAG", "Response Profile" + response.getResponseData());
                ModelManager.getInstance().setEmployeeProfileModel(response.getResponseData());
                ((TextView) rootView.findViewById(R.id.et_password)).setText("");
                ((TextView) rootView.findViewById(R.id.et_url)).setText("");
                ((TextView) rootView.findViewById(R.id.et_user_name)).setText("");
                Utility.saveEmpConfig(new Preferences(getContext()));
                preferences.saveBoolean(AppsConstant.ISFROMLOGIN, true);
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

            case CommunicationConstant.API_FORGOT_CREDENTIALS_REQUEST:
                String resp = response.getResponseData();
                Log.d("TAG", "Forgot credentials response : " + resp);
                ForgotCredentialsResponseModel forgotCredentialsResponseModel = ForgotCredentialsResponseModel.create(resp);
                if (forgotCredentialsResponseModel != null && forgotCredentialsResponseModel.getForgetCredentialsResult() != null
                        && forgotCredentialsResponseModel.getForgetCredentialsResult().getErrorCode().equalsIgnoreCase("-5")) {
                    new AlertCustomDialog(context, forgotCredentialsResponseModel.getForgetCredentialsResult().getErrorMessage());
                   // CustomDialog.alertOkWithFinishFragment(context, forgotCredentialsResponseModel.getForgetCredentialsResult().getErrorMessage(), null, IAction.LOGIN_VIEW, true);
                } else {
                    new AlertCustomDialog(context, forgotCredentialsResponseModel.getForgetCredentialsResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        if(mGoogleSignInClient!=null){
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();

        }
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Log.d(TAG,"Gmail token :"+acct.getIdToken());

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,"User name :"+ user.getDisplayName());
                            Log.d(TAG,"email :"+ user.getEmail());
                            Toast.makeText(getContext(), "User Signed In", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
           // finish();
            //startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
