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
import hr.eazework.com.model.MemberApprovalModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;


public class PendingEmployeeApprovalFragment extends BaseFragment {

    public static final String TAG = "PendingEmployeeApprovalFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.employee_pending_approval, container, false);
        rootView.findViewById(R.id.tv_apply_leave).setOnClickListener(this);
        populateLeaves();
        if (Utility.isNetworkAvailable(getContext())) {
            MainActivity.isAnimationLoaded = false;
            CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getSimpleRequestSessionDataStr(), CommunicationConstant.APT_GET_PENDING_MEMBER_APPROVAL, false);
            showHideProgressView(true);
        } else {
            new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
        }
        return rootView;
    }

    private void populateLeaves() {
        int totalPendingMemmberApprovals = ModelManager.getInstance().getTotalPendingMemmberApprovals();
        ((TextView) rootView.findViewById(R.id.tv_leave_count)).setText("" + totalPendingMemmberApprovals);
        View view = rootView.findViewById(R.id.ll_pending_item_container);
        MemberApprovalModel employeeLeaveModel = ModelManager.getInstance().getMemberApprovalModel();
        ArrayList<MemberApprovalModel> leaveModels = null;
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
                for (MemberApprovalModel leaveModel : leaveModels) {
                    View seperator = new View(getActivity());
                    View lView = inflater.inflate(R.layout.employee_detail_item, layout, false);
                    ((TextView) lView.findViewById(R.id.tv_leave_employee_name)).setVisibility(View.VISIBLE);
                    ((TextView) lView.findViewById(R.id.tv_request_id)).setVisibility(View.VISIBLE);
                    ((TextView) lView.findViewById(R.id.tv_requested_by)).setVisibility(View.VISIBLE);
                    ((TextView) lView.findViewById(R.id.tv_leave_employee_name)).setText(String.format(getString(R.string.leave_employee_name_format), leaveModel.getmName()));

                    ((TextView) lView.findViewById(R.id.tv_leave_from_to)).setText(String.format(getString(R.string.requested_date), leaveModel.getmReqDate()));
                    ((TextView) lView.findViewById(R.id.tv_request_id)).setText(getString(R.string.request_id) + " " + leaveModel.getmReqCode());
                    ((TextView) lView.findViewById(R.id.tv_requested_by)).setText(getString(R.string.requested_by) + " " + leaveModel.getmInitiator());

                    lView.findViewById(R.id.tv_accept).setVisibility(View.VISIBLE);
                    lView.findViewById(R.id.tv_reject).setVisibility(View.VISIBLE);
                    lView.findViewById(R.id.tv_accept).setTag(leaveModel);
                    lView.findViewById(R.id.tv_reject).setTag(leaveModel);
                    lView.findViewById(R.id.tv_accept).setOnClickListener(this);
                    lView.findViewById(R.id.tv_reject).setOnClickListener(this);
                    seperator.setBackgroundResource(R.color.transparent);
                    seperator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
                    layout.addView(lView);
                    layout.addView(seperator);
                }
            } else {
                View lView = inflater.inflate(R.layout.employee_detail_item, layout, false);
                ((TextView) lView.findViewById(R.id.tv_leave_type)).setText("No Employees.");

                ((TextView) lView.findViewById(R.id.tv_leave_from_to)).setVisibility(View.GONE);
                ((TextView) lView.findViewById(R.id.tv_request_id)).setVisibility(View.GONE);
                ((TextView) lView.findViewById(R.id.tv_requested_by)).setVisibility(View.GONE);
                lView.findViewById(R.id.tv_accept).setVisibility(View.GONE);
                lView.findViewById(R.id.tv_reject).setVisibility(View.GONE);

                layout.addView(lView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_accept:
                updateEmployeeStatus(v, "approve", "Employee approved", "A");
                break;
            case R.id.tv_reject:
                updateEmployeeStatus(v, "reject", "Employee rejected", "R");
                break;

            default:
                break;
        }
        super.onClick(v);
    }

    public void updateEmployeeStatus(View v, String status, final String toastMessage, final String updateFlag) {
        final MemberApprovalModel acceptModel = (MemberApprovalModel) v.getTag();
        if (Utility.isNetworkAvailable(getContext())) {
            new AlertCustomDialog(getActivity(), "Are you sure you want to " + status + " this request?", getString(R.string.dlg_cancel), getString(R.string.dlg_ok), new AlertCustomDialog.AlertClickListener() {

                @Override
                public void onPositiveBtnListener() {
                    showHideProgressView(true);
                    MainActivity.isAnimationLoaded = false;
                    CommunicationManager.getInstance()
                            .sendPostRequest(
                                    new IBaseResponse() {
                                        @Override
                                        public void validateResponse(ResponseData response) {
                                            showHideProgressView(false);
                                            MainActivity.isAnimationLoaded = true;
                                            try {

                                                JSONObject json = new JSONObject(response.getResponseData());
                                                JSONObject mainJSONObject = json.optJSONObject("UpdateMemberApprovalRejectionResult");
                                                if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                    String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                    new AlertCustomDialog(getActivity(), errorMessage);
                                                    refreshList();
                                                } else {
                                                    Utility.displayMessage(getContext(),toastMessage);
                                                    refreshList();
                                                }
                                            } catch (JSONException e) {
                                                Log.e("EmployeeApproval", e.getMessage(), e);
                                                Crashlytics.logException(e);
                                            }
                                        }
                                    },
                                    AppRequestJSONString.getUpdatePendingEmployeeStatusData(updateFlag, acceptModel.getmReqID(), acceptModel.getmReqStatus(), acceptModel.getmApprovalLevel()),
                                    CommunicationConstant.API_GET_MEMBER_PENDING_APPROVAL_STATUS,
                                    true);

                }

                @Override
                public void onNegativeBtnListener() {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
        }
    }

    public void refreshList() {
        showHideProgressView(true);
        MainActivity.isAnimationLoaded = false;
        String josnSimpleRequestSessionDataStr = AppRequestJSONString.getSimpleRequestSessionDataStr();
        CommunicationManager.getInstance().sendPostRequest(PendingEmployeeApprovalFragment.this, josnSimpleRequestSessionDataStr, CommunicationConstant.APT_GET_PENDING_MEMBER_APPROVAL, false);
    }

    @Override
    public void validateResponse(ResponseData response) {
        showHideProgressView(false);
        MainActivity.isAnimationLoaded = true;
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        if (response.isSuccess()) {
            switch (response.getRequestData().getReqApiId()) {
                case CommunicationConstant.APT_GET_PENDING_MEMBER_APPROVAL:
                    try {
                        JSONObject json = new JSONObject(response.getResponseData());
                        JSONObject mainJSONObject = json.optJSONObject("GetPendingMemberReqListResult");
                        MemberApprovalModel employeeLeaveModel = new MemberApprovalModel(mainJSONObject.optJSONArray("list"));
                        ModelManager.getInstance().setMemberApprovalModel(employeeLeaveModel);
                        ModelManager.getInstance().setTotalPendingMemmberApprovals(employeeLeaveModel.getmPendingLeaveList().size());

                    } catch (JSONException e) {
                        Log.e("EmployeeApproval",e.getMessage(),e);
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
