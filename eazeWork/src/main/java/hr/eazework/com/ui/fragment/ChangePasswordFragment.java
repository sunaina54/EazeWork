package hr.eazework.com.ui.fragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.ui.util.SharedPreference;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

public class ChangePasswordFragment extends BaseFragment {

	public static final String TAG = "ChangePasswordFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
		rootView=inflater.inflate(R.layout.change_password_dialog_root_container, container, false);
		rootView.findViewById(R.id.btn_change_pass).setOnClickListener(this);
		return rootView;
	}
	private boolean validate() {
		boolean validate = true;
		TextView currentPass = (TextView) rootView.findViewById(R.id.et_current_password);
		TextView newPass = (TextView) rootView.findViewById(R.id.et_new_password);
		TextView confirmPass = (TextView) rootView.findViewById(R.id.et_confirm_password);
		if (!confirmPass.getText().toString().equals(newPass.getText().toString())) {
			confirmPass.setError(getString(R.string.confirm_missmatch_password));
			validate = false;
		}
		if (confirmPass.getText().toString().equals("")) {
			confirmPass.setError(getString(R.string.confirm_password));
			validate = false;
		}
		if (newPass.getText().toString().equals("")) {
			newPass.setError(getString(R.string.new_password));
			validate = false;
		}
		if (currentPass.getText().toString().equals("")) {
			currentPass.setError(getString(R.string.current_password));
			validate = false;
		}
		return validate;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_pass:
			if(validate()){
				TextView currentPass = (TextView) rootView.findViewById(R.id.et_current_password);
				TextView newPass = (TextView) rootView.findViewById(R.id.et_new_password);
				showHideProgressView(true);
                String changePasswordInput = AppRequestJSONString.getChangePasswordData(currentPass.getText().toString(), newPass.getText().toString());
                CommunicationManager.getInstance().sendPostRequest(this, changePasswordInput, CommunicationConstant.API_CHANGE_PASSWORD, true);
			}
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	@Override
	public void validateResponse(ResponseData response) {
		showHideProgressView(false);
		switch (response.getRequestData().getReqApiId()) {
		case CommunicationConstant.API_CHANGE_PASSWORD:
			try {
				JSONObject jsonObject=new JSONObject(response.getResponseData());
				JSONObject mainResponseJSObject=jsonObject.optJSONObject("ChangePasswordResult");
				if(mainResponseJSObject!=null&&mainResponseJSObject.optInt("ErrorCode", -1)==0){
					String errorMessage=mainResponseJSObject.optString("ErrorMessage", "");
					SharedPreference.setSessionId(mainResponseJSObject.optString("SessionID", ""));
                    ((MainActivity)getActivity()).displayMessage("Password changed");
                    getActivity().onBackPressed();
				} else {
					String errorMessage=mainResponseJSObject.optString("ErrorMessage", "");
					new AlertCustomDialog(getActivity(), ""+errorMessage);
				}
			} catch (Exception e) {
			}
			break;

		default:
			break;
		}
		super.validateResponse(response);
	}
}
