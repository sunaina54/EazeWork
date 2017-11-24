package hr.eazework.com.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import hr.calender.caldroid.CaldroidFragment;
import hr.calender.caldroid.CaldroidListener;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.SearchOnbehalfActivity;
import hr.eazework.com.model.EmpLeaveModel;
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.GetCorpEmpParamResultResponse;
import hr.eazework.com.model.LeaveTypeModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class CreateNewLeaveFragment extends BaseFragment implements OnCheckedChangeListener {
    public static final String TAG = "CreateNewLeaveFragment";
    private CaldroidFragment dialogCaldroidFragment;
    private LeaveTypeModel leaveTypeModel;
    private Calendar startDate;
    private Calendar toDate;
    private EditText etRemark;
    private float availableLeaves;
    private ArrayList<String> mRhList;
    protected String selectedRs;
    private boolean isRhSelected = false;
    private boolean isSubmitClicked = true;
    private Preferences preferences;
    private RelativeLayout searchLayout;
    private TextView empNameTV;
    public static int LEAVE_EMP=1;
    private EmployItem employItem;
    private String empId;
    private String defaultFromDateLabel="From Date",defaultToDateLable="To Date",value="--/--/----",defaultLeaveLable="Select Leave";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
       /* LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        if (loginUserModel != null && loginUserModel.getUserModel() != null) {
            MainActivity.isAnimationLoaded = false;
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getEmpLeavesData(loginUserModel.getUserModel().getEmpId()), CommunicationConstant.API_EMP_LEAVES,
                    true);

            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getEmpLeaveBalancesData(loginUserModel.getUserModel().getEmpId()), CommunicationConstant.API_EMP_RH_LEAVES,
                    false);

            CommunicationManager.getInstance().sendPostRequest(
                    this,
                    AppRequestJSONString.getEmpLeaveBalancesData(loginUserModel.getUserModel().getEmpId()),
                    CommunicationConstant.API_GET_EMP_LEAVE_BALANCES, false);


        }*/

        super.onCreate(savedInstanceState);
    }

    private void setupLeave(){
        if (employItem!=null) {
            MainActivity.isAnimationLoaded = false;
            String  empCode=String.valueOf(employItem.getEmpID());
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getEmpLeavesData(empCode), CommunicationConstant.API_EMP_LEAVES,
                    true);


            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getEmpLeaveBalancesData(empCode), CommunicationConstant.API_EMP_RH_LEAVES,
                    false);

            CommunicationManager.getInstance().sendPostRequest(
                    this,
                    AppRequestJSONString.getEmpLeaveBalancesData(empCode),
                    CommunicationConstant.API_GET_EMP_LEAVE_BALANCES, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.startDate=null;
        this.toDate=null;
        this.leaveTypeModel=null;
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.ll_create_new_leave, container, false);

        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(getActivity(), preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        etRemark = (EditText) rootView.findViewById(R.id.et_remark);
        rootView.findViewById(R.id.tv_select_leave_type).setOnClickListener(this);
        rootView.findViewById(R.id.tv_select_rest_leaves).setOnClickListener(this);
        rootView.findViewById(R.id.ll_from_date).setOnClickListener(this);
        rootView.findViewById(R.id.ll_to_date).setOnClickListener(this);
        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
        rootView.findViewById(R.id.btn_save_as_draft).setOnClickListener(this);
        empNameTV=(TextView)rootView.findViewById(R.id.empNameTV);



        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setOnCheckedChangeListener(this);
        ((RelativeLayout)rootView.findViewById(R.id.searchLayout)).setOnClickListener(this);



        if (ModelManager.getInstance().getLeaveTypeModel() == null) {
            showHideProgressView(true);
        } else {
            showHideProgressView(false);
        }

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Create Leave");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveTypeModel != null) {
                    if (isSubmitClicked) {
                        isSubmitClicked = false;
                        doSubmitOperation();
                    }
                } else {
                    new AlertCustomDialog(getContext(), "Please select leave type");
                }

            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.LEAVE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                    }
                }
            }
        });
        employItem=new EmployItem();
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();

        employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
        employItem.setName(loginUserModel.getUserModel().getUserName());
        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());

        setupLeave();
        updateEmploy();
        etRemark.setText("");
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        etRemark.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LEAVE_EMP){
            if(data!=null){
                EmployItem item=(EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_EMP);
                if(item!=null){
                    empNameTV.setText(item.getName());
                    empId=String.valueOf(item.getEmpID());
                    employItem=item;
                }
                leaveTypeModel=null;
               /* EmpLeaveModel leaveModel = ModelManager.getInstance()
                        .getEmpLeaveModel() != null ? ModelManager
                        .getInstance().getEmpLeaveModel()
                        .getEmpLeaveById(leaveTypeModel.getLeaveId())
                        : null;
               */
               /* ModelManager.getInstance().setLeaveTypeModel(null);
                if (ModelManager.getInstance().getLeaveTypeModel() == null) {*/
                showHideProgressView(true);
               /* } else {
                    showHideProgressView(false);
                }*/
                setupLeave();

                updateLeaveAvailablity(null);
                updateLeaveDayType(leaveTypeModel);
                updateLeaveSelectionType(leaveTypeModel);
                updateFromAndToDate(leaveTypeModel);

            }
        }
    }

    private void updateEmploy(){
        empNameTV.setText(employItem.getName() + " (" + employItem.getEmpCode() + ")");
    }
    private void doSubmitOperation() {
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        String empCode=String.valueOf(employItem.getEmpID());

        String remark = etRemark.getText().toString();
        if (isRhSelected) {
            if(selectedRs==null || selectedRs.equalsIgnoreCase("")){
                isSubmitClicked = true;
                Toast.makeText(getActivity(), "Please select leave date", Toast.LENGTH_LONG).show();
                return;
            }
            showHideProgressView(true);
            setActiveInActive(false);
            if(loginUserModel != null && loginUserModel != null) {
                MainActivity.isAnimationLoaded = false;
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), selectedRs,
                                selectedRs, "1", remark),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);

            }
            return;
        }
        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
        if(isCompensatory){
            if (this.startDate == null) {
                Toast.makeText(getActivity(), "Please select Date Worked", Toast.LENGTH_LONG).show();
                isSubmitClicked = true;
                return;
            }
            if (this.toDate == null) {
                Toast.makeText(getActivity(), "Please select Compensatory Off Date", Toast.LENGTH_LONG).show();
                isSubmitClicked = true;
                return;
            }
            showHideProgressView(true);
            setActiveInActive(false);
            if(loginUserModel != null && loginUserModel.getUserModel() != null) {
                String startDate = String.format("%1$td/%1$tm/%1$tY", this.startDate);
                String endDate = String.format("%1$td/%1$tm/%1$tY", toDate);
                MainActivity.isAnimationLoaded = false;
                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), startDate, endDate,
                                "" + getOneDayData(), remark),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
            }
            return;
        }
        if (this.startDate == null) {
            Toast.makeText(getActivity(), "Please select from date.", Toast.LENGTH_LONG).show();
            isSubmitClicked = true;
            return;
        }
        if (this.toDate == null) {
            Toast.makeText(getActivity(), "Please select to date.", Toast.LENGTH_LONG).show();
            isSubmitClicked = true;
            return;
        }
        if (getTotalDay(this.startDate, this.toDate) <= 0) {
            Toast.makeText(getActivity(), "Please select to date later than from date.", Toast.LENGTH_LONG).show();
            isSubmitClicked = true;
            return;
        }
        // if (getTotalDay(startDate, toDate) <= availableLeaves) {
        showHideProgressView(true);
        String startDate = String.format("%1$td/%1$tm/%1$tY", this.startDate);
        String endDate = String.format("%1$td/%1$tm/%1$tY", this.toDate);
        if (getTotalDay(this.startDate, toDate) == 1) {
            setActiveInActive(false);
            if (getOneDayData() != 1) {
                if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                    MainActivity.isAnimationLoaded = false;
                    CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.getSaveLeaveRequestData(
                                    empCode,
                                    leaveTypeModel.getLeaveId(), startDate, endDate,
                                    "" + getOneDayData(), remark),
                            CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                }
            } else {
                if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                    MainActivity.isAnimationLoaded = false;
                    CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.GetLeaveReqTotalDays(empCode,
                                    leaveTypeModel.getLeaveId(),
                                    startDate,
                                    endDate),
                            CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY,
                            true);
                }
            }
        } else {

            if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                MainActivity.isAnimationLoaded = false;
                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.GetLeaveReqTotalDays(empCode,
                                leaveTypeModel.getLeaveId(),
                                startDate,
                                endDate),
                        CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY, true);
            }
        }

    }

    protected void updateFromAndToDate(LeaveTypeModel leaveTypeModel) {
        if (leaveTypeModel != null) {
            boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");

            ((TextView) rootView.findViewById(R.id.tv_from_date_top)).setText(isCompensatory ? "Date Worked" : "From Date");
            ((TextView) rootView.findViewById(R.id.tv_to_date_top)).setText(isCompensatory ? "Compensatory Off Date" : "To Date");

            ((TextView) rootView.findViewById(R.id.tv_from_day)).setText(isCompensatory ? "" : "From Date");
            ((TextView) rootView.findViewById(R.id.tv_to_day)).setText(isCompensatory ? "" : "To Date");

            if (isCompensatory) {
                rootView.findViewById(R.id.ll_avail_leaves).setVisibility(View.GONE);
                updateCompasatory(isCompensatory);
            }
        }
    }


    protected void updateLeaveSelectionType(LeaveTypeModel leaveTypeModel) {
        if (leaveTypeModel != null) {
            rootView.findViewById(R.id.tv_remark).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.et_remark).setVisibility(View.VISIBLE);
            if (leaveTypeModel.getLeaveType().equalsIgnoreCase("R")) {
                isRhSelected = true;
                if (mRhList != null && mRhList.size() > 0) {
                    rootView.findViewById(R.id.rl_main_container).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.rl_error_text).setVisibility(View.GONE);
                } else {
                    rootView.findViewById(R.id.tv_remark).setVisibility(View.GONE);
                    rootView.findViewById(R.id.et_remark).setVisibility(View.GONE);
                    rootView.findViewById(R.id.btn_submit).setVisibility(View.GONE);
                    rootView.findViewById(R.id.rl_main_container).setVisibility(View.GONE);
                    rootView.findViewById(R.id.rl_error_text).setVisibility(View.VISIBLE);
                }
                rootView.findViewById(R.id.ll_select_date_container).setVisibility(View.GONE);
                ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
            } else {
                isRhSelected = false;
                selectedRs=null;
                if(!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
                } else {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.VISIBLE);
                }

                rootView.findViewById(R.id.rl_main_container).setVisibility(View.GONE);
                rootView.findViewById(R.id.ll_select_date_container).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.rl_error_text).setVisibility(View.GONE);
            }

        }
    }


    protected void updateLeaveAvailablity(EmpLeaveModel leaveModel) {
        if (leaveModel != null) {
            rootView.findViewById(R.id.ll_avail_leaves).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.ll_consume_leaves).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.ll_avail_leaves_count)).setText("" + leaveModel.getmBalance());
            ((TextView) rootView.findViewById(R.id.ll_consume_leaves_count)).setText("" + leaveModel.getmConsumed());
            try {
                availableLeaves = Float.parseFloat(leaveModel.getmBalance());
            } catch (NumberFormatException e) {
                availableLeaves = 0;
            }
        } else {
            availableLeaves = 0;
            ((TextView) rootView.findViewById(R.id.tv_from_date_top)).setText("From Date");
            ((TextView) rootView.findViewById(R.id.tv_to_date_top)).setText("To Date");

            rootView.findViewById(R.id.ll_avail_leaves).setVisibility(View.GONE);
            rootView.findViewById(R.id.ll_consume_leaves).setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.tv_from_day))
                    .setText(defaultFromDateLabel);
            // String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
            ((TextView) rootView.findViewById(R.id.tv_from_date)).setText(value);
            ((TextView) rootView.findViewById(R.id.tv_to_day))
                    .setText(defaultToDateLable);
            // String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
            ((TextView) rootView.findViewById(R.id.tv_to_date)).setText(value);
            ((TextView) rootView.findViewById(R.id.tv_select_leave_type)).setText(defaultLeaveLable);
            rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.GONE);
        }
    }

    protected  void updateCompasatory(boolean isCompasatory){
        if (isCompasatory) {
            rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.VISIBLE);
            if (leaveTypeModel.isDayP25()) {
                rootView.findViewById(R.id.rb_25_day).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.rb_25_day).setVisibility(View.GONE);
            }
            if (leaveTypeModel.isDayP50()) {
                rootView.findViewById(R.id.rb_half_day).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.rb_half_day).setVisibility(View.GONE);
            }
            if (leaveTypeModel.isDayP75()) {
                rootView.findViewById(R.id.rb_75_day).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.rb_75_day).setVisibility(View.GONE);
            }

            if(!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
                ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
            } else {
                ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.VISIBLE);
            }


        } else {
            rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.GONE);
        }


        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(true);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
    }
    protected void updateLeaveDayType(LeaveTypeModel leaveTypeModel) {

        if (leaveTypeModel != null) {
            if (startDate != null && toDate != null && (getTotalDay(startDate, toDate) == 1)) {

                rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.VISIBLE);
                if (leaveTypeModel.isDayP25()) {
                    rootView.findViewById(R.id.rb_25_day).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.rb_25_day).setVisibility(View.GONE);
                }
                if (leaveTypeModel.isDayP50()) {
                    rootView.findViewById(R.id.rb_half_day).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.rb_half_day).setVisibility(View.GONE);
                }
                if (leaveTypeModel.isDayP75()) {
                    rootView.findViewById(R.id.rb_75_day).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.rb_75_day).setVisibility(View.GONE);
                }

                if(!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
                } else {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.VISIBLE);
                }


            } else {
                rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.GONE);
            }
        }

        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(true);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
    }

    @SuppressLint("NewApi")
    private void setActiveInActive(boolean isActive) {
        rootView.findViewById(R.id.btn_submit).setClickable(isActive);
        if (Utility.isNotLowerVersion(VERSION_CODES.HONEYCOMB))
            rootView.findViewById(R.id.btn_submit).setActivated(isActive);
    }



    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        showHideProgressView(false);
        String responseData = response.getResponseData();
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        JSONObject jsonObject;
        if (response.isSuccess()) {
            switch (response.getRequestData().getReqApiId()) {
                case CommunicationConstant.API_EMP_RH_LEAVES:
                    JSONObject rhobject;
                    try {
                        rhobject = new JSONObject(response.getResponseData());
                        JSONObject rhjsonObject = rhobject.optJSONObject("GetEmpRHLeavesResult");
                        JSONArray array = rhjsonObject.optJSONArray("RHLeaves");
                        mRhList = new ArrayList<String>();
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                mRhList.add(array.optString(i, ""));
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }
                    break;
                case CommunicationConstant.API_SAVE_LEAVE_REQUEST:
                    setActiveInActive(true);
                    try {
                        JSONObject object = new JSONObject(response.getResponseData());
                        jsonObject = object.optJSONObject("SaveLeaveReqResult");
                        if (jsonObject.optInt("ErrorCode", -1) != 0) {
                            String errorMessage = jsonObject.optString("ErrorMessage", "");
                            new AlertCustomDialog(getActivity(), errorMessage);
                            isSubmitClicked = true;

                        } else {

                            Utility.displayMessage(getContext(), "Leave submitted");
                            isSubmitClicked = true;
                            etRemark.setText("");
                            MenuItemModel itemModel = ModelManager.getInstance().getMenuItemModel();
                            if (itemModel != null) {
                                MenuItemModel model = itemModel.getItemModel(MenuItemModel.LEAVE_KEY);
                                if (model != null && model.isAccess()) {
                                    mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                                } else {
                                    mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                                }
                            }


                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }
                    break;
                case CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY:
                    try {
                        String empCode=String.valueOf(employItem.getEmpID());
                        jsonObject = (new JSONObject(response.getResponseData())).optJSONObject("GetLeaveReqTotalDaysResult");
                        double d = jsonObject.optDouble("TotalDays", 0);
                        //      if (d > 0) {
                        showHideProgressView(true);
                           /* LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                            if(loginUserModel != null && loginUserModel.getUserModel() != null) {*/
                                /*UserModel userModel = loginUserModel.getUserModel();
                                String empId = userModel.getEmpId();*/

                        String fromDateFormatted = String.format("%1$td/%1$tm/%1$tY", startDate);
                        String toDateFormatted = String.format("%1$td/%1$tm/%1$tY", toDate);

                        String leaveId = leaveTypeModel.getLeaveId();

                        EditText etRemark = (EditText) rootView.findViewById(R.id.et_remark);
                        String remark = etRemark.getText().toString();
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getSaveLeaveRequestData(
                                        empCode,
                                        leaveId, fromDateFormatted, toDateFormatted, "" + d,
                                        remark),
                                CommunicationConstant.API_SAVE_LEAVE_REQUEST,
                                true);

                        //}
                        /*} else {
                            isSubmitClicked=true;
                            String messageString = jsonObject.optString("ErrorMessage", "");
                            new AlertCustomDialog(getActivity(), messageString.equalsIgnoreCase("") ? "Please select some valid dates." : messageString);
                        }*/
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }
                    break;
                case CommunicationConstant.API_EMP_LEAVES:
                    try {
                        jsonObject = new JSONObject(response.getResponseData());
                        LeaveTypeModel leaveTypeModel = new LeaveTypeModel(jsonObject.getJSONObject("GetEmpLeavesResult").optJSONArray("Leaves"));
                        ModelManager.getInstance().setLeaveTypeModel(leaveTypeModel);


                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.GetCorpEmpParam(), CommunicationConstant.API_GET_CORPEMP_PARAM,
                                true);


                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }
                    break;
                case CommunicationConstant.API_GET_EMP_LEAVE_BALANCES:
                    try {
                        if (responseData != null) {
                            JSONObject jsonObj = new JSONObject(responseData);
                            String getEmpLeaveBalancesResult = jsonObj.optJSONObject("GetEmpLeaveBalancesResult").toString();
                            ModelManager.getInstance().setEmpLeaveModel(getEmpLeaveBalancesResult);
                        }
                    } catch (JSONException e) {
                        Crashlytics.logException(e);
                        Log.e("Leave",e.getMessage(),e);
                    }

                    break;
/////

                case CommunicationConstant.API_GET_CORPEMP_PARAM:
                    try {
                        ((RelativeLayout)rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
                        if (responseData != null) {
                            GetCorpEmpParamResultResponse corpEmpParamResultResponse = GetCorpEmpParamResultResponse.create(responseData);
                            if (corpEmpParamResultResponse != null && corpEmpParamResultResponse.getGetCorpEmpParamResult() != null &&  corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode()!=null &&  corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode().equalsIgnoreCase("0") ){

                                if(corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList() != null &&
                                    corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().size() > 0) {
                                if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue() != null) {

                                    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam().equalsIgnoreCase("LeaveOnBehalfOfYN") && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue().equalsIgnoreCase("Y")) {
                                        ((RelativeLayout)rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            }else {

                            }
                        }
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        Log.e("Leave",e.getMessage(),e);
                    }

                    break;
                default:
                    break;
            }
        } else {
            setActiveInActive(true);
        }
        super.validateResponse(response);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_as_draft:
                new AlertCustomDialog(getActivity(), "This feature is under development.");
                break;
            case R.id.searchLayout:
                Intent theIntent=new Intent(getActivity(), SearchOnbehalfActivity.class);
                startActivityForResult(theIntent,LEAVE_EMP);

                break;
            case R.id.btn_submit:
                // if (availableLeaves > 0) {
                String empCode=String.valueOf(employItem.getEmpID());
                LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                EditText etRemark = (EditText) rootView.findViewById(R.id.et_remark);
                String remark = etRemark.getText().toString();
                if (isRhSelected) {
                    showHideProgressView(true);
                    setActiveInActive(false);

                    if(loginUserModel != null && loginUserModel.getUserModel() != null) {
                        MainActivity.isAnimationLoaded = false;
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.getSaveLeaveRequestData(empCode,
                                        leaveTypeModel.getLeaveId(), selectedRs,
                                        selectedRs, "1", remark),
                                CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                    }
                    return;
                }
                if (startDate == null) {
                    Utility.displayMessage(getContext(),"Please select from date.");
                    return;
                }
                if (toDate == null) {
                    Utility.displayMessage(getContext(),"Please select to date.");
                    return;
                }
                if (getTotalDay(startDate, toDate) < 0) {
                    Utility.displayMessage(getContext(),"Please select to date later than from date.");
                    return;
                }
                showHideProgressView(true);
                String startDate = String.format("%1$td/%1$tm/%1$tY", this.startDate);
                String endDate = String.format("%1$td/%1$tm/%1$tY", toDate);
                if (getTotalDay(this.startDate, toDate) == 1) {
                    setActiveInActive(false);
                    if (getOneDayData() != 1) {
                        if(loginUserModel != null && loginUserModel.getUserModel() != null) {
                            MainActivity.isAnimationLoaded = false;
                            CommunicationManager.getInstance().sendPostRequest(
                                    this,
                                    AppRequestJSONString.getSaveLeaveRequestData(
                                            empCode, leaveTypeModel
                                                    .getLeaveId(), startDate, endDate,
                                            "" + getOneDayData(), remark),
                                    CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                        }
                    } else {
                        if(loginUserModel != null && loginUserModel.getUserModel() != null) {
                            MainActivity.isAnimationLoaded = false;
                            CommunicationManager.getInstance().sendPostRequest(
                                    this,
                                    AppRequestJSONString.GetLeaveReqTotalDays(
                                            empCode,
                                            leaveTypeModel.getLeaveId(),
                                            startDate,
                                            endDate),
                                    CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY,
                                    true);
                        }
                    }
                } else {
                    if(loginUserModel != null && loginUserModel.getUserModel() != null) {
                        MainActivity.isAnimationLoaded = false;
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.GetLeaveReqTotalDays(loginUserModel
                                                .getUserModel().getEmpId(),
                                        leaveTypeModel.getLeaveId(),
                                        startDate,
                                        endDate),
                                CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY, true);
                    }
                }
            /*
             * } else { Toast.makeText( getActivity(), "" + availableLeaves +
			 * " Days leave available only.", Toast.LENGTH_LONG).show(); }
			 *
			 * *} else { Toast.makeText(getActivity(), "No leave available.",
			 * Toast.LENGTH_LONG).show(); }
			 */

                break;
            case R.id.tv_select_rest_leaves:

                if (mRhList != null && mRhList.size() > 0) {
                    final CustomBuilder restrictedLeaves = new CustomBuilder(getContext(), "Select Date", true);
                    restrictedLeaves.setSingleChoiceItems(mRhList, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            selectedRs = (String) selectedObject;
                            ((TextView) rootView.findViewById(R.id.tv_select_rest_leaves)).setText(selectedRs);
                            restrictedLeaves.dismiss();
                        }
                    });
                    restrictedLeaves.show();
                } else {

                }

                break;
            case R.id.tv_select_leave_type:
                LeaveTypeModel leaveType = ModelManager.getInstance().getLeaveTypeModel();
                if (leaveType != null) {
                    ArrayList<LeaveTypeModel> leaveTypeList = leaveType.getLeaveTypeList();

                    CustomBuilder leaveTypeDialog = new CustomBuilder(getContext(), "Select Leave Type", true);
                    leaveTypeDialog.setSingleChoiceItems(leaveTypeList, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {

                            leaveTypeModel = (LeaveTypeModel) selectedObject;

                            ((TextView) rootView.findViewById(R.id.tv_select_leave_type)).setText(leaveTypeModel.getLeaveName());

                            EmpLeaveModel leaveModel = ModelManager.getInstance()
                                    .getEmpLeaveModel() != null ? ModelManager
                                    .getInstance().getEmpLeaveModel()
                                    .getEmpLeaveById(leaveTypeModel.getLeaveId())
                                    : null;
                            updateLeaveAvailablity(leaveModel);
                            updateLeaveDayType(leaveTypeModel);
                            updateLeaveSelectionType(leaveTypeModel);
                            updateFromAndToDate(leaveTypeModel);
                            builder.dismiss();

                        }
                    });
                    leaveTypeDialog.show();
                }


                break;
            case R.id.ll_from_date:

                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Calendar calendarCurrent = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        // if (calendar.compareTo(calendarCurrent) >= 0) {
                        CreateNewLeaveFragment.this.startDate = calendar;
                        ((TextView) rootView.findViewById(R.id.tv_from_day))
                                .setText(String.format("%1$tA", calendar));
                        String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
                        ((TextView) rootView.findViewById(R.id.tv_from_date)).setText(formatedData);
                        dialogCaldroidFragment.dismiss();
                        if(leaveTypeModel==null)
                            return;

                        updateLeaveDayType(leaveTypeModel);
                        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");

                        if(isCompensatory) {
                            updateCompasatory(isCompensatory);
                        }

                    }
                });
                // If activity is recovered from rotation
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                Bundle state = dialogCaldroidFragment.getArguments();
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getChildFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }
                dialogCaldroidFragment.show(getChildFragmentManager(), dialogTag);
                break;
            case R.id.ll_to_date:

                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        toDate = calendar;
                        ((TextView) rootView.findViewById(R.id.tv_to_day)).setText(String.format("%1$tA", calendar));
                        String formatTodate = String.format("%1$td/%1$tm/%1$tY", calendar);
                        ((TextView) rootView.findViewById(R.id.tv_to_date)).setText(formatTodate);
                        dialogCaldroidFragment.dismiss();
                        if(leaveTypeModel==null)
                            return;

                        updateLeaveDayType(leaveTypeModel);
                        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
                        if(isCompensatory) {
                            updateCompasatory(isCompensatory);
                        }
					/*
					 * } else { Toast.makeText(getActivity(),
					 * "Please select valid date.", Toast.LENGTH_LONG) .show();
					 * }
					 */
                    }
                });
                // If activity is recovered from rotation
                final String dialogTag2 = "CALDROID_DIALOG_FRAGMENT";
                Bundle state2 = dialogCaldroidFragment.getArguments();
                if (state2 != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getChildFragmentManager(), state2,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag2);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }
                dialogCaldroidFragment.show(getChildFragmentManager(), dialogTag2);
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    private double getOneDayData() {
        if (((CheckBox) rootView.findViewById(R.id.rb_25_day)).isChecked()) {
            return 0.25;
        } else if (((CheckBox) rootView.findViewById(R.id.rb_half_day)).isChecked()) {
            return 0.5;
        } else if (((CheckBox) rootView.findViewById(R.id.rb_75_day)).isChecked()) {
            return 0.75;
        } else {
            return 1;
        }
    }

    @SuppressLint("NewApi")
    private int getTotalDay(Calendar startDate2, Calendar toDate2) {
        if (startDate2 == null || toDate2 == null)
            return 0;
        long diff = toDate2.getTime().getTime() - startDate2.getTime().getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_25_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
                    break;
                case R.id.rb_half_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
                    break;
                case R.id.rb_75_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
                    break;
                case R.id.rb_full_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
                    break;
                default:
                    break;
            }
        }
    }
}
