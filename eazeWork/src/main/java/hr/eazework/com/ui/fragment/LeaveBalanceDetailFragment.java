package hr.eazework.com.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.BackdatedAttendanceActivity;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.TimeModificationActivity;
import hr.eazework.com.model.AttendanceItem;
import hr.eazework.com.model.EmpLeaveModel;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveBalanceModel;
import hr.eazework.com.model.LeaveModel;
import hr.eazework.com.model.LeaveReqsItem;
import hr.eazework.com.model.LeaveSummaryResponseModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.model.WithdrawWFHResponse;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppConfig;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.StringUtils;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class LeaveBalanceDetailFragment extends BaseFragment {

    public static final String TAG = "LeaveBalanceDetailFragment";
    public static final long daysForFilter = 15552000000L;
    private Preferences preferences;
    private Context context;
    private LeaveAdapter leaveAdapter;
    private RecyclerView expenseApprovalRecyclerView;
    private LinearLayout searchParentLayout, errorLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        super.onCreate(savedInstanceState);
    }

    public void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        Calendar calendar = Calendar.getInstance();
        Calendar beginYear = Calendar.getInstance();
        beginYear.setTimeInMillis(beginYear.getTimeInMillis() - daysForFilter);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + daysForFilter);

        String beginYearDate = String.format("%1$td/%1$tm/%1$tY", beginYear);
        String calendarDate = String.format("%1$td/%1$tm/%1$tY", calendar);

        CommunicationManager.getInstance().sendPostRequest(
                this,
                AppRequestJSONString.getEmpLeaveRequestsData(beginYearDate, calendarDate, "L"),
                CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_PENDING,
                false);

      /*  CommunicationManager.getInstance().sendPostRequest(
                this,
                AppRequestJSONString.getEmpLeaveRequestsData(beginYearDate, calendarDate, true, false),
                CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_PENDING,
                false);
        CommunicationManager.getInstance().sendPostRequest(
                this,
                AppRequestJSONString.getEmpLeaveRequestsData(
                        beginYearDate,
                        calendarDate, false,
                        false),
                CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_APPROOVED,
                false);
        if (AppConfig.IS_CONSUMED_ENABLE_IN_LEAVE_HOME) {
            CommunicationManager.getInstance().sendPostRequest(
                    this,
                    AppRequestJSONString.getEmpLeaveRequestsData(
                            beginYearDate,
                            calendarDate,
                            false, true),
                    CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_CONSUMED,
                    false);
        }*/
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        if (loginUserModel != null) {
            UserModel userModel = loginUserModel.getUserModel();
            if (userModel != null) {
                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getEmpLeaveBalancesData(userModel
                                .getEmpId()),
                        CommunicationConstant.API_GET_EMP_LEAVE_BALANCES, false);

                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getLeaveBalanceData(userModel
                                .getEmpId(), "ALL"),
                        CommunicationConstant.API_EMP_LEAVE_BALANCE, true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = new Preferences(getContext());
        context = getContext();
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.leave_balance_detail_root_container, container, false);
        MainActivity.updataProfileData(getActivity(), rootView);

        int bgColorCode = Utility.getBgColorCode(getContext(), preferences);
        int textColorCode = Utility.getTextColorCode(preferences);

        ((LinearLayout) rootView.findViewById(R.id.leave_header)).setBackgroundColor(bgColorCode);
        ((TextView) rootView.findViewById(R.id.tv_apply_current_leave)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_leave_count)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_leave_type)).setTextColor(textColorCode);
        ((TextView) rootView.findViewById(R.id.tv_apply_current_leave)).setBackgroundColor(bgColorCode);
        expenseApprovalRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseApprovalRecyclerView);
        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorLinearLayout);
        errorLinearLayout.setVisibility(View.GONE);

        View leaveContainer = rootView.findViewById(R.id.ll_pending_leave_container);
        //populateLeaves(leaveContainer, true, getPendingLeave());
        //populateLeaves(rootView.findViewById(R.id.ll_avail_leave_container), false, getAvailLeave());

       /* if (AppConfig.IS_CONSUMED_ENABLE_IN_LEAVE_HOME) {

            rootView.findViewById(R.id.tv_consumed_leaves).setVisibility(View.VISIBLE);
            View consumedLeaveContainer = rootView.findViewById(R.id.ll_consumed_leave_container);
            consumedLeaveContainer.setVisibility(View.VISIBLE);
            populateLeaves(consumedLeaveContainer, false, getConsumedLeave());
        }*/
        EmpLeaveModel leaveModel = ModelManager.getInstance().getEmpLeaveModel();
        if (leaveModel != null)
            ((TextView) rootView.findViewById(R.id.tv_apply_current_leave))
                    .setText(leaveModel.getVisibleFormatedMessage());
        LeaveBalanceModel leaveBalanceModel = ModelManager.getInstance().getLeaveBalanceModel();
        double leaveBalance = leaveBalanceModel != null ? leaveBalanceModel.getmAvailable() : 0;
        ((TextView) rootView.findViewById(R.id.tv_leave_count)).setText(String.valueOf(leaveBalance));
        ((TextView) rootView.findViewById(R.id.tv_leave_type)).setText(getString(R.string.leave_balance));
        MainActivity.animateToVisible(rootView.findViewById(R.id.ll_leave_details), -1);
        MainActivity.animateToVisible(leaveContainer, -1);
        MainActivity.animateToVisible(rootView.findViewById(R.id.ll_avail_leave_container), -1);
        rootView.findViewById(R.id.tv_apply_leave).setOnClickListener(this);

        rootView.findViewById(R.id.tv_apply_leave).setVisibility(View.GONE);

        if (ModelManager.getInstance().getPendingLeaveModel() == null) {
            showHideProgressView(true);
        } else {
            showHideProgressView(false);
        }
        requestAPI();
        return rootView;// super.onCreateView(inflater, container,
        // savedInstanceState);
    }

    private ArrayList<LeaveModel> getAvailLeave() {
        ArrayList<LeaveModel> arrayList = new ArrayList<LeaveModel>();
        LeaveModel leaveModel = ModelManager.getInstance()
                .getApprovedLeaveModel();
        if (leaveModel != null) {
            arrayList = leaveModel.getmLeaveLIst();
        }
        return arrayList;
    }

    private ArrayList<LeaveModel> getConsumedLeave() {
        ArrayList<LeaveModel> arrayList = new ArrayList<LeaveModel>();
        LeaveModel leaveModel = ModelManager.getInstance()
                .getConsumedLeaveModel();
        if (leaveModel != null) {
            arrayList = leaveModel.getmLeaveLIst();
        }
        return arrayList;
    }

    private ArrayList<LeaveModel> getPendingLeave() {
        ArrayList<LeaveModel> arrayList = new ArrayList<LeaveModel>();
        LeaveModel leaveModel = ModelManager.getInstance()
                .getPendingLeaveModel();
        if (leaveModel != null) {
            arrayList = leaveModel.getmLeaveLIst();
        }
        return arrayList;
    }
/*
    private void populateLeaves(View view, ArrayList<LeaveModel> leaveModels) {
        if (view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            layout.removeAllViews();
            if (leaveModels != null && leaveModels.size() > 0) {
                for (LeaveModel leaveModel : leaveModels) {
                    View lView = inflater.inflate(R.layout.leave_detail_item,
                            layout, false);
                    ((TextView) lView.findViewById(R.id.tv_leave_type))
                            .setText(leaveModel.getmLeaveName());
                    ((TextView) lView.findViewById(R.id.tv_leave_remark))
                            .setText(String.format(
                                    getString(R.string.leave_remark_format),
                                    leaveModel.getmLeaveMSG()));
                    ((TextView) lView.findViewById(R.id.tv_leave_from_to))
                            .setText(String.format(
                                    getString(R.string.leave_from_to_format),
                                    leaveModel.getmLeaveFrom(),
                                    leaveModel.getmLeaveTo()));
                    TextView leaveTotalDaysView = (TextView) lView.findViewById(R.id.tv_leave_total_days);
                    leaveTotalDaysView.setVisibility(View.VISIBLE);
                    int count = 0;
                    count = (int) (Float.parseFloat(leaveModel.getmLeaveDays()));

                    leaveTotalDaysView.setText(String.format(getResources().getQuantityString(
                            R.plurals.leave_total_days_format,
                            count), leaveModel.getmLeaveDays()));
                    TextView leaveEmployeeName = (TextView) lView.findViewById(R.id.tv_leave_employee_name);

                    leaveEmployeeName.setVisibility(View.VISIBLE);
                    leaveEmployeeName.setText(String.format(getString(R.string.leave_req_id_format), leaveModel.getmRequestCode()));

                    TextView statusLeaveTV = (TextView) lView.findViewById(R.id.statusLeaveTV);
                    statusLeaveTV.setText(leaveModel.getStatusDesc());
                    Button viewBTN = (Button) lView.findViewById(R.id.viewBTN);
                    view.setVisibility(View.GONE);
                    if (leaveModel.getButtons() != null) {
                        viewBTN.setVisibility(View.VISIBLE);
                        viewBTN.setText("View");
                        if (leaveModel.getStatusDesc().equalsIgnoreCase("Draft")) {
                            viewBTN.setText("Edit");
                        }


                    }

                    *//*if (isPendingLeaves) {
                        ((TextView) lView.findViewById(R.id.tv_leave_type)).setTextColor(Utility.getColor(getContext(), R.color.accent));
                    }*//*
                    // View withDrawView = lView.findViewById(R.id.tv_withdraw);
                 *//*   if (leaveModel.isWithdrawen()) {
                        withDrawView.setVisibility(View.VISIBLE);
                    } else {
                        withDrawView.setVisibility(View.GONE);
                    }*//*
                    //   withDrawView.setTag(leaveModel);
                    //    withDrawView.setOnClickListener(LeaveBalanceDetailFragment.this);
                    layout.addView(lView);
                }
            }
            else {
                View lView = inflater.inflate(R.layout.leave_detail_item, layout, false);
                //((TextView) lView.findViewById(R.id.tv_leave_type)).setText("No Leaves.");
                ((TextView) lView.findViewById(R.id.tv_leave_remark)).setVisibility(View.GONE);
                ((TextView) lView.findViewById(R.id.tv_leave_from_to)).setVisibility(View.GONE);
                layout.addView(lView);
            }
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply_leave:
                mUserActionListener.performUserAction(IAction.CREATE_NEW_LEAVE, v, null);
                break;
            case R.id.tv_withdraw:
                final LeaveModel acceptModel = (LeaveModel) v.getTag();
                new AlertCustomDialog(getActivity(),
                        "Are you sure you want to withdraw this request?",
                        getString(R.string.dlg_cancel), getString(R.string.dlg_ok),
                        new AlertCustomDialog.AlertClickListener() {

                            @Override
                            public void onPositiveBtnListener() {
                                MainActivity.isAnimationLoaded = false;
                                showHideProgressView(true);
                             /*   CommunicationManager.getInstance().sendPostRequest(
                                        LeaveBalanceDetailFragment.this,
                                        AppRequestJSONString.getUpdatePendingStatusData("W", acceptModel.getmRequestId(), acceptModel.getmStatus(), -1),
                                        CommunicationConstant.API_GET_UPDATE_PENDING_APPROVAL_STATUS,
                                        false);*/
                                //sendWithdrawRequestData(acceptModel);

                            }

                            @Override
                            public void onNegativeBtnListener() {
                            }
                        });
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        showHideProgressView(false);
        String responseData = response.getResponseData();
        if (response.isSuccess() && !isSessionValid(responseData)) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        int reqApiId = response.getRequestData().getReqApiId();
        switch (reqApiId) {
          /*  case CommunicationConstant.API_GET_UPDATE_PENDING_APPROVAL_STATUS:
                JSONObject json;
                try {
                    json = new JSONObject(responseData);
                    JSONObject mainJSONObject = json
                            .optJSONObject("UpdateEmpPendingReqResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        Utility.displayMessage(getContext(), "Leave withdrawn");
                        requestAPI();

                    }

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }
                break;*/
            case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_PENDING:
                    if (responseData != null) {
                     //   ModelManager.getInstance().setPendingLeaveModel((new JSONObject(responseData)).optJSONObject("GetEmpLeaveRequestsResult").toString());
                       // populateLeaves(rootView.findViewById(R.id.ll_pending_leave_container), true, getPendingLeave());
                        String str1 = response.getResponseData();
                        Log.d("TAG", "Advance Response : " + str1);
                        expenseApprovalRecyclerView.setVisibility(View.GONE);
                        errorLinearLayout.setVisibility(View.VISIBLE);
                        LeaveSummaryResponseModel leaveSummaryResponseModel = LeaveSummaryResponseModel.create(str1);
                        if (leaveSummaryResponseModel != null && leaveSummaryResponseModel.getGetEmpLeaveRequestsResult() != null
                                && leaveSummaryResponseModel.getGetEmpLeaveRequestsResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                                && leaveSummaryResponseModel.getGetEmpLeaveRequestsResult().getLeaveReqs().size() > 0) {
                            if (leaveSummaryResponseModel.getGetEmpLeaveRequestsResult().getLeaveReqs().get(0) != null) {
                                expenseApprovalRecyclerView.setVisibility(View.VISIBLE);
                                errorLinearLayout.setVisibility(View.GONE);
                                refresh(leaveSummaryResponseModel.getGetEmpLeaveRequestsResult().getLeaveReqs());
                            }
                        }

                    }
                break;
            case CommunicationConstant.API_EMP_LEAVE_BALANCE:
                try {
                    JSONObject jsonLeaveBalanceResult = (new JSONObject(responseData)).getJSONObject("GetEmpLeaveBalanceResult");
                    String getEmpLeaveBalanceResult = jsonLeaveBalanceResult.toString();
                    ModelManager.getInstance().setLeaveBalanceModel(getEmpLeaveBalanceResult);
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }
                View viewById = rootView.findViewById(R.id.tv_leave_count);
                LeaveBalanceModel leaveBalanceModel = ModelManager.getInstance().getLeaveBalanceModel();
                ((TextView) viewById).setText("" + (leaveBalanceModel != null ? leaveBalanceModel.getmAvailable() : 0));
                break;
       /*     case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_APPROOVED:
                try {
                    if (responseData != null) {
                        String getEmpLeaveRequestsResult = (new JSONObject(responseData)).optJSONObject("GetEmpLeaveRequestsResult").toString();
                        ModelManager.getInstance().setApprovedLeaveModel(getEmpLeaveRequestsResult);
                        populateLeaves(rootView.findViewById(R.id.ll_avail_leave_container), false, getAvailLeave());
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }
                break;

            case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_CONSUMED:

                try {
                    if (responseData != null) {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String getEmpLeaveRequestsResult = jsonObject.optJSONObject("GetEmpLeaveRequestsResult").toString();
                        ModelManager.getInstance().setConsumedLeaveModel(getEmpLeaveRequestsResult);
                        populateLeaves(rootView.findViewById(R.id.ll_consumed_leave_container), false, getConsumedLeave());
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }
                break;*/

            case CommunicationConstant.API_GET_EMP_LEAVE_BALANCES:
                try {
                    if (responseData != null) {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String getEmpLeaveBalancesResult = jsonObject.optJSONObject("GetEmpLeaveBalancesResult").toString();
                        ModelManager.getInstance().setEmpLeaveModel(getEmpLeaveBalancesResult);
                    }
                    EmpLeaveModel leaveModel = ModelManager.getInstance().getEmpLeaveModel();
                    View applyCurrentLeave = rootView.findViewById(R.id.tv_apply_current_leave);
                    if (leaveModel != null) {
                        ((TextView) applyCurrentLeave).setText(leaveModel.getVisibleFormatedMessage());
                    } else {
                        ((TextView) applyCurrentLeave).setText("");
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }

                break;
           /* case CommunicationConstant.API_WITHDRAW_LEAVE_REQUEST:
                String strResponse = response.getResponseData();
                Log.d("TAG", "Leave Withdraw Response : " + strResponse);
                WithdrawWFHResponse withdrawWFHResponse = WithdrawWFHResponse.create(strResponse);
                if (withdrawWFHResponse != null && withdrawWFHResponse.getWithdrawLeaveRequestResult() != null
                        && withdrawWFHResponse.getWithdrawLeaveRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, withdrawWFHResponse.getWithdrawLeaveRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), withdrawWFHResponse.getWithdrawLeaveRequestResult().getErrorMessage());
                }
                break;*/
            default:
                break;
        }
        super.validateResponse(response);
    }

   /* private void sendWithdrawRequestData(LeaveModel item) {
        GetWFHRequestDetail requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(item.getmRequestId());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_WITHDRAW_LEAVE_REQUEST, true);
    }*/

    private void refresh(ArrayList<LeaveReqsItem> items) {
        if (items != null && items.size() > 0 && items.get(0) != null) {
            leaveAdapter = new LeaveAdapter(items);
            expenseApprovalRecyclerView.setAdapter(leaveAdapter);
            expenseApprovalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            leaveAdapter.notifyDataSetChanged();
        }
    }

    private class LeaveAdapter extends
            RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {
        private ArrayList<LeaveReqsItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView requestIdLeaveTV, fromDateLeaveTV, toDateLeaveTV, totalDaysTV, leaveRemarksTV,statusLeaveTV,leaveTypeTV;
            Button viewBTN;
            ImageView menuIV;


            public MyViewHolder(View v) {
                super(v);

                requestIdLeaveTV = (TextView) v.findViewById(R.id.requestIdLeaveTV);
                fromDateLeaveTV = (TextView) v.findViewById(R.id.fromDateLeaveTV);
                toDateLeaveTV = (TextView) v.findViewById(R.id.toDateLeaveTV);
                totalDaysTV = (TextView) v.findViewById(R.id.totalDaysTV);
                leaveRemarksTV = (TextView) v.findViewById(R.id.leaveRemarksTV);
                statusLeaveTV = (TextView) v.findViewById(R.id.statusLeaveTV);
                leaveTypeTV= (TextView) v.findViewById(R.id.leaveTypeTV);

                viewBTN = (Button) v.findViewById(R.id.viewBTN);


            }
        }

        public void addAll(List<LeaveReqsItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public LeaveAdapter(List<LeaveReqsItem> data) {
            this.dataSet = (ArrayList<LeaveReqsItem>) data;

        }

        @Override
        public LeaveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leave_summary_item, parent, false);
            LeaveAdapter.MyViewHolder myViewHolder = new LeaveAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final LeaveAdapter.MyViewHolder holder, final int listPosition) {

            final LeaveReqsItem item = dataSet.get(listPosition);
            holder.leaveTypeTV.setText(item.getLeaveName());
            holder.requestIdLeaveTV.setText(item.getReqCode());
            holder.fromDateLeaveTV.setText(item.getStartDate());
            holder.toDateLeaveTV.setText(item.getEndDate());
            holder.leaveRemarksTV.setText(item.getRemarks());
            holder.totalDaysTV.setText(item.getTotalDays());
            holder.statusLeaveTV.setText(item.getStatusDesc());
            holder.viewBTN.setText(item.getButtons()[0]);

            holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getStatusDesc()!=null && item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        CreateNewLeaveFragment requestFragment = new CreateNewLeaveFragment();
                        requestFragment.setLeaveReqsItem(dataSet.get(listPosition));
                        Fragment fragment=requestFragment;

                        mUserActionListener.performUserActionFragment(IAction.CREATE_NEW_LEAVE,fragment,null);
                        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/
                    }

                    if(item.getStatusDesc()!=null && !item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        ViewLeaveFragment viewLeaveFragment = new ViewLeaveFragment();
                        viewLeaveFragment.setLeaveReqsItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, viewLeaveFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });




            }


        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }
}
