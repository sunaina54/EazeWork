package hr.eazework.com.ui.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.BackdatedAttendanceActivity;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.TimeModificationActivity;
import hr.eazework.com.model.AttendanceRejectItem;
import hr.eazework.com.model.AttendanceRejectRequestModel;
import hr.eazework.com.model.AttendanceRejectResponseModel;
import hr.eazework.com.model.AttendanceReqDetail;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetODRequestDetail;
import hr.eazework.com.model.GetODRequestDetailResult;
import hr.eazework.com.model.GetTimeModificationRequestDetail;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveDetailResponseModel;
import hr.eazework.com.model.LeaveRejectResponseModel;
import hr.eazework.com.model.LeaveReqDetailModel;
import hr.eazework.com.model.LeaveRequestDetailsModel;
import hr.eazework.com.model.LeaveRequestModel;
import hr.eazework.com.model.LeaveResponseModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.ODRequestDetail;
import hr.eazework.com.model.ODRequestModel;
import hr.eazework.com.model.ODResponseModel;
import hr.eazework.com.model.ODSummaryResponse;
import hr.eazework.com.model.OdReqDetailModel;
import hr.eazework.com.model.PartialDayDataModel;
import hr.eazework.com.model.PartialDayModel;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TimeModificationItem;
import hr.eazework.com.model.TimeModificationRequestModel;
import hr.eazework.com.model.TimeModificationSummaryResponseModel;
import hr.eazework.com.model.TourReasonListModel;
import hr.eazework.com.model.TourReqDetail;
import hr.eazework.com.model.TourRequestModel;
import hr.eazework.com.model.TourResponseModel;
import hr.eazework.com.model.TourSummaryRequestDetail;
import hr.eazework.com.model.TourSummaryResponse;
import hr.eazework.com.model.WFHRejectRequestModel;
import hr.eazework.com.model.WFHRequestDetailItem;
import hr.eazework.com.model.WFHRequestDetailModel;
import hr.eazework.com.model.WFHRequestModel;
import hr.eazework.com.model.WFHResponseModel;
import hr.eazework.com.model.WFHSummaryResponse;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;


public class PendingActivityFragment extends BaseFragment {

    public static final String TAG = "PendingActivityFragment";
    public static final String screenName = "PendingActivityFragment";
    private EmployeeLeaveModel employeeLeaveModel;
    private String comments = "";
    private Context context;
    private GetWFHRequestDetail requestDetail;
    private WFHRequestDetailItem wfhRequest;
    private LeaveRequestDetailsModel leaveRequestDetailsModel;
    private ODRequestDetail odRequestDetailResult;
    private TourSummaryRequestDetail tourSummaryRequestDetail;
    private TimeModificationSummaryResponseModel summaryResponseModel;
    private AttendanceReqDetail reqDetail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
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
        final EmployeeLeaveModel employeeLeaveModel = ModelManager.getInstance().getEmployeePendingLeaveModel();
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
                for (final EmployeeLeaveModel leaveModel : leaveModels) {
                    View seperator = new View(getActivity());
                    View lView = inflater.inflate(R.layout.leave_approval_detail_layout, layout, false);
                    TextView requestIdTV = (TextView) lView.findViewById(R.id.requestIdTV);
                    requestIdTV.setText(leaveModel.getmReqTypeDesc());
                    TextView requestNoTV = (TextView) lView.findViewById(R.id.requestNoTV);
                    requestNoTV.setText(leaveModel.getmReqCode());
                    TextView empNameTV = (TextView) lView.findViewById(R.id.empNameTV);
                    empNameTV.setText(leaveModel.getmForEmpName());
                    TextView detailsTV = (TextView) lView.findViewById(R.id.detailsTV);
                    detailsTV.setText(leaveModel.getmDetails());
                    TextView leaveRemarksTV = (TextView) lView.findViewById(R.id.leaveRemarksTV);
                    leaveRemarksTV.setText(leaveModel.getmRemark());
                    ImageView menuIV = (ImageView) lView.findViewById(R.id.menuIV);
                    menuIV.setVisibility(View.GONE);
                    Button approveBTN = (Button) lView.findViewById(R.id.approveBTN);
                    approveBTN.setVisibility(View.GONE);
                    final ArrayList<String> menuList = new ArrayList<>();
                    if (leaveModel.getmButtons() != null) {
                        for (String str : leaveModel.getmButtons()) {
                            if (str.equalsIgnoreCase("Approve")) {
                                approveBTN.setVisibility(View.VISIBLE);
                            }
                            if (str.equalsIgnoreCase("Edit")) {
                                menuList.add(str);
                            }
                          /*  if(str.equalsIgnoreCase("View")){
                                menuList.add(str);
                            }*/
                            if (str.equalsIgnoreCase("Reject")) {
                                menuList.add(str);

                            }

                        }
                    }

                    if (menuList.size() > 0) {
                        // menu list visible
                        menuIV.setVisibility(View.VISIBLE);
                        menuIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomBuilder builder = new CustomBuilder(getContext(), "Options", true);
                                builder.setSingleChoiceItems(menuList, null, new CustomBuilder.OnClickListener() {
                                    @Override
                                    public void onClick(CustomBuilder builder, Object selectedObject) {
                                        if (selectedObject.toString().equalsIgnoreCase("Reject")) {
                                            /*if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("LR")) {
                                                updateLeaveStatus(null, "reject", "Leave rejected", "R", leaveModel);
                                            } else {
                                                rejectPopup(leaveModel);
                                            }*/
                                            rejectPopup(leaveModel);
                                        } else if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                                            if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("LR")) {
                                                CreateNewLeaveFragment requestFragment = new CreateNewLeaveFragment();
                                                requestFragment.setEmployeeLeaveModel(leaveModel);
                                                requestFragment.setScreenName(screenName);
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);
                                                fragmentTransaction.addToBackStack(PendingActivityFragment.TAG);
                                                fragmentTransaction.commit();
                                            }

                                            if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("WR")) {
                                                CreateNewLeaveFragment requestFragment = new CreateNewLeaveFragment();
                                                requestFragment.setEmployeeLeaveModel(leaveModel);
                                                requestFragment.setScreenName(screenName);
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }


                                        }/*else if(selectedObject.toString().equalsIgnoreCase("View")){
                                            if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("WR")) {
                                                CreateNewLeaveFragment requestFragment = new CreateNewLeaveFragment();
                                                requestFragment.setEmployeeLeaveModel(leaveModel);
                                                requestFragment.setScreenName(screenName);
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }
                                        }*/
                                        builder.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                /*    View lView = inflater.inflate(R.layout.leave_detail_item, layout, false);
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
                    rejectView.setVisibility(View.VISIBLE);*/

                    approveBTN.setTag(leaveModel);
                    // rejectView.setTag(leaveModel);

                    // approveBTN.setOnClickListener(this);
                    // rejectView.setOnClickListener(this);
                    approveBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         /*   if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("LR")) {
                                updateLeaveStatus(v, "approve", "Leave approved", "A", null);
                            } else {*/
                            if (leaveModel.getmReqCode() != null && leaveModel.getmReqCode().startsWith("LR") ||
                                    leaveModel.getmReqCode().startsWith("WR")) {
                                sendViewLeaveRequestSummaryData(leaveModel);
                            }

                            //}
                        }
                    });
                    seperator.setBackgroundResource(R.color.transparent);
                    seperator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
                    layout.addView(lView);
                    layout.addView(seperator);
                }
            } else {
                View lView = inflater.inflate(R.layout.leave_detail_item,
                        layout, false);
                ((TextView) lView.findViewById(R.id.tv_leave_type))
                        .setText(context.getResources().getString(R.string.leave_approval_error_msg));
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
            /*case R.id.approveBTN:
                updateLeaveStatus(v, "approve", "Leave approved", "A", null);
                break;*/
            /*case R.id.tv_reject:
                updateLeaveStatus(v,"reject","Leave rejected","R");
                break;*/

            default:
                break;
        }
        super.onClick(v);
    }

    public void updateLeaveStatus(View v, String status, final String toastMessage, final String updateFlag, EmployeeLeaveModel model) {
        final EmployeeLeaveModel acceptModel;
        if (v != null) {
            acceptModel = (EmployeeLeaveModel) v.getTag();
        } else {
            acceptModel = model;
        }
        if (Utility.isNetworkAvailable(getContext())) {
            new AlertCustomDialog(getActivity(), "Are you sure you want to " + status + " this request?", getString(R.string.dlg_cancel), getString(R.string.dlg_ok), new AlertCustomDialog.AlertClickListener() {

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
                                            ((MainActivity) getActivity()).displayMessage(toastMessage);
                                            refreshList();
                                        }

                                    } catch (JSONException e) {
                                        Log.e(TAG, e.getMessage(), e);
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
                        employeeLeaveModel = new EmployeeLeaveModel(mainJSONObject.optJSONArray("reqTypeDetails"));
                        ModelManager.getInstance().setEmployeePendingLeaveModel(employeeLeaveModel);
                        ModelManager.getInstance().setUserTotalPendingRequests(employeeLeaveModel.getmPendingLeaveList().size());

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }
                    populateLeaves();
                    break;

                case CommunicationConstant.API_REJECT_LEAVE_REQUEST:
                    String leaveResponse = response.getResponseData();
                    Log.d("TAG", "reject response : " + leaveResponse);
                    LeaveRejectResponseModel rejectResponseModel = LeaveRejectResponseModel.create(leaveResponse);
                    if (rejectResponseModel != null && rejectResponseModel.getRejectLeaveRequestResult() != null
                            && rejectResponseModel.getRejectLeaveRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                        CustomDialog.alertOkWithFinishFragment(context, rejectResponseModel.getRejectLeaveRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                    } else {
                        new AlertCustomDialog(getActivity(), rejectResponseModel.getRejectLeaveRequestResult().getErrorMessage());
                    }
                    break;
                case CommunicationConstant.API_GET_LEAVE_REQUEST_DETAIL:
                    String leaveResp = response.getResponseData();
                    Log.d("TAG", "WFH Response : " + leaveResp);
                    LeaveDetailResponseModel leaveDetailResponseModel = LeaveDetailResponseModel.create(leaveResp);
                    if (leaveDetailResponseModel != null && leaveDetailResponseModel.getGetLeaveRequestDetailsResult() != null
                            && leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                            && leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails() != null) {
                        leaveRequestDetailsModel = leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails();
                        sendLeaveApprovalData();
                    }

                    break;
                case CommunicationConstant.API_APPROVE_LEAVE_REQUEST:
                    String leaveApprove = response.getResponseData();
                    Log.d("TAG", "Leave Response : " + leaveApprove);
                    LeaveResponseModel leaveResponseModel = LeaveResponseModel.create(leaveApprove);
                    if (leaveResponseModel != null && leaveResponseModel.getApproveLeaveRequestResult() != null
                            && leaveResponseModel.getApproveLeaveRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)){
                        CustomDialog.alertOkWithFinishFragment(context, leaveResponseModel.getApproveLeaveRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                    }else {
                        new AlertCustomDialog(getActivity(), leaveResponseModel.getApproveLeaveRequestResult().getErrorMessage());

                    }
                    break;
                default:
                    break;
            }
        } else {
            new AlertCustomDialog(getActivity(), "Failed");
        }


        super.validateResponse(response);
    }

    private void rejectPopup(final EmployeeLeaveModel item) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reject_layout);

        final EditText messageTV = (EditText) dialog.findViewById(R.id.messageTV);

        final TextView cancelTV, okTV;
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
                comments = messageTV.getText().toString();
                if (comments.equalsIgnoreCase("")) {
                    new AlertCustomDialog(getContext(), getResources().getString(R.string.enter_remarks));
                    return;
                }

                if (item.getmReqCode() != null && item.getmReqCode().startsWith("LR")) {
                    rejectLeaveRequest(item);
                }

                if (item.getmReqCode() != null && item.getmReqCode().startsWith("WR")) {
                    rejectLeaveRequest(item);
                }


                dialog.dismiss();
            }

        });
        dialog.show();
    }

    private void rejectLeaveRequest(EmployeeLeaveModel item) {
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(item.getmReqID());
        rejectRequestModel.setComments(comments);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_LEAVE_REQUEST, true);

    }

    private void rejectWFHRequest(EmployeeLeaveModel item) {
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(item.getmReqID());
        rejectRequestModel.setComments(comments);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_WFH_REQUEST, true);
    }

    private void rejectAttendanceRequest(EmployeeLeaveModel item) {
        AttendanceRejectRequestModel rejectRequestModel = new AttendanceRejectRequestModel();
        AttendanceRejectItem attendanceRejectItem=new AttendanceRejectItem();
        attendanceRejectItem.setReqID(item.getmReqID());
        attendanceRejectItem.setRemark(comments);
        attendanceRejectItem.setApprovalLevel(item.getmApprovalLevel());
        attendanceRejectItem.setStatus(item.getmStatus());
        rejectRequestModel.setRequest(attendanceRejectItem);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectAttendanceRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_ATTENDANCE_REQUEST, true);
    }












    private void sendViewLeaveRequestSummaryData(EmployeeLeaveModel item) {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(item.getmReqID());
        requestDetail.setAction(AppsConstant.EDIT_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_LEAVE_REQUEST_DETAIL, true);
    }

    private void sendLeaveApprovalData() {
        LeaveReqDetailModel leaveReqDetailModel=new LeaveReqDetailModel();
        leaveReqDetailModel.setReqID(leaveRequestDetailsModel.getReqID()+"");
        leaveReqDetailModel.setForEmpID(leaveRequestDetailsModel.getForEmpID()+"");
        leaveReqDetailModel.setStartDate(leaveRequestDetailsModel.getStartDate());
        leaveReqDetailModel.setEndDate(leaveRequestDetailsModel.getEndDate());
        leaveReqDetailModel.setLeaveID(leaveRequestDetailsModel.getLeaveID());
        leaveReqDetailModel.setRemarks(leaveRequestDetailsModel.getRemark());
        leaveReqDetailModel.setTotalDays(leaveRequestDetailsModel.getTotalDays());
        leaveReqDetailModel.setApprovalLevel(Integer.parseInt(leaveRequestDetailsModel.getApprovalLevel()));
        leaveReqDetailModel.setStatus(leaveRequestDetailsModel.getReqStatus());
        leaveReqDetailModel.setAttachments(leaveRequestDetailsModel.getAttachments());
        LeaveRequestModel leaveRequestModel = new LeaveRequestModel();
        leaveRequestModel.setLeaveReqDetail(leaveReqDetailModel);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.leaveRequest(leaveRequestModel),
                CommunicationConstant.API_APPROVE_LEAVE_REQUEST, true);
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showLog(PendingActivityFragment.class,"ResultCode "+ resultCode+"" + data);

        if (Utility.isNetworkAvailable(getContext())) {
            MainActivity.isAnimationLoaded = false;
            String jsonSallarySlipMonthData = AppRequestJSONString.getSallarySlipMonthData();
            CommunicationManager.getInstance().sendPostRequest(this, jsonSallarySlipMonthData, CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQUESTS, false);
            showHideProgressView(true);
        } else {
            new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
        }

    }*/

}
