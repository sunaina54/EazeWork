package hr.eazework.com.ui.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.SalaryMonthModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class PaySlipDownloadFragment extends BaseFragment {
    public static final String TAG = "PaySlipDownloadFragment";
    private TextView tvSelectMonth;
    private SalaryMonthModel selectedMonthModel = null;
    private ArrayList<SalaryMonthModel> salaryMonthList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_download_pay_slip_container, container, false);
        tvSelectMonth = (TextView) rootView.findViewById(R.id.tv_select_month);
        tvSelectMonth.setOnClickListener(this);
        updateSpinnerData();
        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
        return rootView;
    }

    private void updateSpinnerData() {
        SalaryMonthModel salaryData = ModelManager.getInstance().getSalaryMonthModel();
        if (salaryData != null) {
            salaryMonthList = salaryData.getMonths();
            tvSelectMonth.setText(salaryMonthList.get(0).getmMontTitle());
            selectedMonthModel = salaryMonthList.get(0);
        } else {

            CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getSallarySlipMonthData(), CommunicationConstant.API_SALARY_SLIP_MONTH, true);
        }

    }


    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        showHideProgressView(true);
        String responseData = response.getResponseData();
        if (response.isSuccess() && !isSessionValid(responseData)) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_VALIDATE_PASSWORD:
                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONObject mainJSONObject = json.optJSONObject("ValidatePasswordResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        new AlertCustomDialog(getActivity(), mainJSONObject.optString("ErrorMessage", "Error:"));
                    } else {
                        if (selectedMonthModel != null) {
                            mUserActionListener.performUserAction(IAction.VIEW_PAY_SLIP, null, selectedMonthModel);
                        } else {
                            Utility.displayMessage(getContext(),"Please select month");
                        }
                    }

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                }
                break;
            case CommunicationConstant.API_SALARY_SLIP_MONTH:
                try {
                    String salaryMonthResult = ((new JSONObject(responseData)).optJSONObject("SalaryMonthResult")).toString();
                    ModelManager.getInstance().setSalaryMonthModel(salaryMonthResult);
                    updateSpinnerData();
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                }
                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                TextView textView = (TextView) rootView.findViewById(R.id.et_password);
                String password = textView.getText().toString();
                if (password.equalsIgnoreCase("")) {
                    textView.setError(getString(R.string.enter_password));
                    return;
                } else {
                    MainActivity.isAnimationLoaded = false;
                    showHideProgressView(true);
                    CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getValidateLoginData(true, password), CommunicationConstant.API_VALIDATE_PASSWORD, true);
                }
                break;
            case R.id.tv_select_month:
                final CustomBuilder monthSelector = new CustomBuilder(getContext(),"Select Month",true);
                monthSelector.setSingleChoiceItems(salaryMonthList, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        selectedMonthModel  = (SalaryMonthModel) selectedObject;
                        tvSelectMonth.setText(selectedMonthModel.getmMontTitle());
                        monthSelector.dismiss();
                    }
                });
                monthSelector.show();
                break;
            default:
                break;
        }
        super.onClick(v);
    }
}
