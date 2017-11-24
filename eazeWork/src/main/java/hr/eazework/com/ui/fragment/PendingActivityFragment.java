package hr.eazework.com.ui.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;


public class PendingActivityFragment extends BaseFragment {

    public static final String TAG = "PendingActivityFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pending_activity_container, container, false);
        rootView.findViewById(R.id.tv_apply_leave).setOnClickListener(this);
        populateLeaves();
        if (Utility.isNetworkAvailable(getContext())) {
            MainActivity.isAnimationLoaded = false;
            String jsonSallarySlipMonthData = AppRequestJSONString.getSallarySlipMonthData();
            CommunicationManager.getInstance().sendPostRequest(this, jsonSallarySlipMonthData, CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQUESTS, false);
            showHideProgressView(true);
        } else {
            new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
        }
        return rootView;
    }

    private void populateLeaves() {
        ((TextView) rootView.findViewById(R.id.tv_leave_count)).setText("" + ModelManager.getInstance().getUserTotalPendingRequests());
        View view = rootView.findViewById(R.id.ll_pending_item_container);
        EmployeeLeaveModel employeeLeaveModel = ModelManager.getInstance().getEmployeePendingLeaveModel();
        ArrayList<EmployeeLeaveModel> leaveModels = null;
        if (employeeLeaveModel != null) {
            leaveModels = employeeLeaveModel.getmPendingLeaveList();
        }
        if (leaveModels == null)
            leaveModels = new ArrayList<>();
        if (view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            layout.removeAllViews();
            if (leaveModels != null && leaveModels.size() > 0) {
                for (EmployeeLeaveModel leaveModel : leaveModels) {
                    View seperator = new View(getActivity());
                    View lView = inflater.inflate(R.layout.leave_detail_item, layout, false);
                    TextView employeeName = (TextView) lView.findViewById(R.id.tv_leave_employee_name);
                    employeeName.setVisibility(View.VISIBLE);
                    employeeName.setText(String
                                    .format(getString(R.string.leave_employee_name_format),
                                            leaveModel.getmForEmpName()));
                    ((TextView) lView.findViewById(R.id.tv_leave_type))
                            .setText(leaveModel.getmReqTypeDesc());
                    ((TextView) lView.findViewById(R.id.tv_leave_remark))
                            .setText(String.format(
                                    getString(R.string.leave_remark_format),
                                    leaveModel.getmRemark()));
                    ((TextView) lView.findViewById(R.id.tv_leave_from_to))
                            .setText("Details : " + leaveModel.getmDetails());
                    ((TextView) lView.findViewById(R.id.tv_leave_type))
                            .setTextColor(getResources().getColor(
                                    R.color.accent));
                    View acceptView = lView.findViewById(R.id.tv_accept);
                    View rejectView = lView.findViewById(R.id.tv_reject);

                    acceptView.setVisibility(View.VISIBLE);
                    rejectView.setVisibility(View.VISIBLE);

                    acceptView.setTag(leaveModel);
                    rejectView.setTag(leaveModel);

                    acceptView.setOnClickListener(this);
                    rejectView.setOnClickListener(this);

                    seperator.setBackgroundResource(R.color.transparent);
                    seperator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
                    layout.addView(lView);
                    layout.addView(seperator);
                }
            } else {
                View lView = inflater.inflate(R.layout.leave_detail_item,
                        layout, false);
                ((TextView) lView.findViewById(R.id.tv_leave_type))
                        .setText("No Leaves.");
                ((TextView) lView.findViewById(R.id.tv_leave_remark))
                        .setVisibility(View.GONE);
                ((TextView) lView.findViewById(R.id.tv_leave_from_to))
                        .setVisibility(View.GONE);
                /*
				 * if (isPendingLeaves) { ((TextView)
				 * lView.findViewById(R.id.tv_leave_type))
				 * .setTextColor(getResources().getColor( R.color.accent)); }
				 */
                layout.addView(lView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply_leave:
                mUserActionListener.performUserAction(IAction.CREATE_NEW_LEAVE, v, null);
                break;
            case R.id.tv_accept:
                updateLeaveStatus(v,"approve","Leave approved","A");
                break;
            case R.id.tv_reject:
                updateLeaveStatus(v,"reject","Leave rejected","R");
                break;

            default:
                break;
        }
        super.onClick(v);
    }

    public void updateLeaveStatus(View v, String status,final String toastMessage,final String updateFlag) {
        final EmployeeLeaveModel acceptModel = (EmployeeLeaveModel) v.getTag();
        if (Utility.isNetworkAvailable(getContext())) {
            new AlertCustomDialog(getActivity(), "Are you sure you want to "+status+" this request?", getString(R.string.dlg_cancel), getString(R.string.dlg_ok), new AlertCustomDialog.AlertClickListener() {

                @Override
                public void onPositiveBtnListener() {
                    showHideProgressView(true);
                    MainActivity.isAnimationLoaded = false;
                    CommunicationManager.getInstance().sendPostRequest(
                                    new IBaseResponse() {
                                        @Override
                                        public void validateResponse(ResponseData response) {
                                            showHideProgressView(false);
                                            MainActivity.isAnimationLoaded = true;
                                            try {

                                                JSONObject json = new JSONObject(response.getResponseData());
                                                JSONObject mainJSONObject = json.optJSONObject("UpdateEmpPendingReqResult");
                                                if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                    String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                    new AlertCustomDialog(getActivity(), errorMessage);
                                                    refreshList();
                                                } else {
                                                    ((MainActivity)getActivity()).displayMessage(toastMessage);
                                                    refreshList();
                                                }

                                            } catch (JSONException e) {
                                                Log.e(TAG,e.getMessage(),e);
                                                Crashlytics.logException(e);
                                            }
                                        }
                                    },
                                    AppRequestJSONString
                                            .getUpdatePendingStatusData(
                                                    updateFlag,
                                                    acceptModel.getmReqID(),
                                                    acceptModel.getmStatus(),
                                                    acceptModel
                                                            .getmApprovalLevel()),
                                    CommunicationConstant.API_GET_UPDATE_PENDING_APPROVAL_STATUS,
                                    false);

                }

                @Override
                public void onNegativeBtnListener() {
                    // Nothing here

                }
            });


        } else {
            new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
        }
    }

    private void refreshList() {
        MainActivity.isAnimationLoaded = false;
        showHideProgressView(true);
        CommunicationManager.getInstance().sendPostRequest(PendingActivityFragment.this,
                AppRequestJSONString.getSimpleRequestSessionDataStr(),
                CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQUESTS,
                false);
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        showHideProgressView(false);
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        if (response.isSuccess()) {
            switch (response.getRequestData().getReqApiId()) {
                case CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQUESTS:
                    try {
                        JSONObject json = new JSONObject(response.getResponseData());
                        JSONObject mainJSONObject = json.optJSONObject("GetEmpPendingApprovalReqsResult");
                        EmployeeLeaveModel employeeLeaveModel = new EmployeeLeaveModel(mainJSONObject.optJSONArray("reqTypeDetails"));
                        ModelManager.getInstance().setEmployeePendingLeaveModel(employeeLeaveModel);
                        ModelManager.getInstance().setUserTotalPendingRequests(employeeLeaveModel.getmPendingLeaveList().size());

                    } catch (JSONException e) {
                        Log.e(TAG,e.getMessage(),e);
                        Crashlytics.logException(e);
                    }
                    populateLeaves();
                    break;
                default:
                    break;
            }
        } else {
            new AlertCustomDialog(getActivity(), "Failed");
        }
        super.validateResponse(response);
    }
}
