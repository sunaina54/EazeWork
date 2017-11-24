package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.SharedPreference;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class SplashFragment extends BaseFragment {
	public static final String TAG = "SplashFragment";
	private Handler handler;
	private boolean isFirstTime = true;
	private Runnable runnable;

    private Preferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		rootView = LayoutInflater.from(getActivity()).inflate(R.layout.splash_fragment_container, container, false);

        preferences = new Preferences(getContext());
        boolean isTestServer = preferences.getBoolean(Preferences.ISTESTSERVER,false);
        boolean isProduction = preferences.getBoolean(Preferences.ISPRODUCTION,true);
        CommunicationConstant.setIsTestServer(isTestServer);
        CommunicationConstant.setIsProduction(isProduction);
        CommunicationConstant.setServerLocation(isTestServer,isProduction);
        rootView.findViewById(R.id.ll_error_layout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showHideProgressView(true);
				rootView.findViewById(R.id.ll_error_layout).setVisibility(View.GONE);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						if (Utility.isNetworkAvailable(getContext())) {
                			rootView.findViewById(R.id.ll_error_layout).setVisibility(View.GONE);
							showHideProgressView(true);
							CommunicationManager.getInstance().sendPostRequest(SplashFragment.this,
									AppRequestJSONString.getValidateLoginData(false, null),
									CommunicationConstant.API_VALIDATE_LOGIN, true);
						} else {
							rootView.findViewById(R.id.ll_error_layout).setVisibility(View.VISIBLE);
						}
					}
				}, 200);
			}
		});
		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				if (isFirstTime) {
					isFirstTime = false;
					MainActivity.animateToVisible(
							rootView.findViewById(R.id.img_icon_logo), 1000);

					handler.postDelayed(runnable, 2000);
				} else {
					moveToNext();
				}
			}
		};
		handler.postDelayed(runnable, 100);

		return rootView;
	}

	protected void moveToNext() {
		if (SharedPreference.getSessionId().equalsIgnoreCase("")) {
			mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
		} else {
			if (Utility.isNetworkAvailable(getContext())) {
				rootView.findViewById(R.id.ll_error_layout).setVisibility(View.GONE);
				showHideProgressView(true);
				CommunicationManager.getInstance().sendPostRequest(SplashFragment.this, AppRequestJSONString.getValidateLoginData(false, null), CommunicationConstant.API_VALIDATE_LOGIN, true);
			} else {
				rootView.findViewById(R.id.ll_error_layout).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void validateResponse(ResponseData response) {
		showHideProgressView(false);
		if(response.isSuccess()&& !isSessionValid(response.getResponseData())){
			mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
			return;
		}
		if (response.isSuccess()) {
			if (response.getRequestData().getReqApiId() == CommunicationConstant.API_VALIDATE_LOGIN) {
				try {
                    String validateLogInResult = ((new JSONObject(response.getResponseData())).getJSONObject("ValidateLogInResult")).toString();
                    ModelManager.getInstance().setLoginUserModel(validateLogInResult);
				} catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG,e.getMessage(),e);
                }
				if (ModelManager.getInstance().getLoginUserModel() != null && ModelManager.getInstance().getLoginUserModel().isLoggedIn()) {
					CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getLogOutData(), CommunicationConstant.API_USER_PROFILE_DETAILS, true);
				} else {
					SharedPreference.setSessionId("");
					mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
				}
			} else if (response.getRequestData().getReqApiId() == CommunicationConstant.API_USER_PROFILE_DETAILS) {
				ModelManager.getInstance().setEmployeeProfileModel(response.getResponseData());
                Utility.saveEmpConfig(new Preferences(getContext()));
                preferences.saveBoolean(AppsConstant.ISFROMSPLASH,true);
                preferences.commit();
				mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
			}
		} else {
			new AlertCustomDialog(getActivity(), "Unable to process your request.",
					new AlertCustomDialog.AlertClickListener() {

						@Override
						public void onPositiveBtnListener() {
							getActivity().finish();
						}

						@Override
						public void onNegativeBtnListener() {
							getActivity().finish();
						}
					});
		}
		super.validateResponse(response);
	}
}
