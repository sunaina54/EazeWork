package hr.eazework.com.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import hr.calender.caldroid.CaldroidFragment;
import hr.calender.caldroid.CaldroidListener;
import hr.eazework.com.FileUtils;
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
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.adapter.DocumentUploadAdapter;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

import static android.app.Activity.RESULT_OK;
import static hr.eazework.com.ui.util.ImageUtil.rotateImage;


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
    private LinearLayout errorLinearLayout;
    private RecyclerView expenseRecyclerView;
    private Context context;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private Bitmap bitmap = null;
    private String purpose = "";
    private static int UPLOAD_DOC_REQUEST = 1;
    private String empId;
    private ImageView plus_create_newIV;
    private String defaultFromDateLabel="From Date",defaultToDateLable="To Date",value="--/--/----",defaultLeaveLable="Select Leave";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
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
        context=getContext();
        uploadFileList=new ArrayList<>();
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


      /*  errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);
        expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(expenseRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_line));
        expenseRecyclerView.addItemDecoration(itemDecoration);

        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);

        plus_create_newIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Take a photo");
                list.add("Gallery");
                final CustomBuilder customBuilder = new CustomBuilder(getContext(), "Upload From", false);
                customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                if (selectedObject.toString().equalsIgnoreCase("Take a photo")) {
                                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext())) {
                                        PermissionUtil.askAllPermissionCamera(CreateNewLeaveFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), CreateNewLeaveFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", TAG);
                                        customBuilder.dismiss();
                                    }
                                } else if (selectedObject.toString().equalsIgnoreCase("Gallery")) {
                                    galleryIntent();
                                    customBuilder.dismiss();
                                }
                            }
                        }
                );
                customBuilder.show();
            }
        });*/


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

            ((TextView) rootView.findViewById(R.id.tv_from_date)).setText(value);
            ((TextView) rootView.findViewById(R.id.tv_to_day))
                    .setText(defaultToDateLable);

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
                        showHideProgressView(true);

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
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Calendar calendarCurrent = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
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



    private void galleryIntent() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, UPLOAD_DOC_REQUEST);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
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
                showHideProgressView(true);
                setupLeave();

                updateLeaveAvailablity(null);
                updateLeaveDayType(leaveTypeModel);
                updateLeaveSelectionType(leaveTypeModel);
                updateFromAndToDate(leaveTypeModel);

            }
        }

       /* final SupportDocsItemModel fileObj = new SupportDocsItemModel();
        if (requestCode == UPLOAD_DOC_REQUEST && resultCode == RESULT_OK) {
            boolean fileShow = true;
            final Uri uri = data.getData();
            String encodeFileToBase64Binary = null;
            if (data != null) {
                String path = data.getStringExtra("path");
                System.out.print(path);
                Uri uploadedFilePath = data.getData();
                String filename = Utility.getFileName(uploadedFilePath,context);
                filename = filename.toLowerCase();
                String fileDesc = Utility.getFileName(uploadedFilePath,context);
                String[] extList = filename.split("\\.");
                System.out.print(extList[1].toString());
                String extension = "." + extList[extList.length - 1];
                encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                Log.d("TAG", "RAR Base 64 :" + encodeFileToBase64Binary);
                *//*List<String> extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getMessage());
                    return;
                }*//*
                fileObj.setDocPathUri(uploadedFilePath);

                if (filename.contains(".pdf")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);

                    } catch (Exception e) {
                        System.out.print(e.toString());
                    }
                } else if (filename.contains(".jpg") || filename.contains(".png") ||
                        filename.contains(".jpeg") || filename.contains(".bmp") || filename.contains(".BMP")) {

                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File mediaFile = null;
                    if (bitmap != null) {
                        encodeFileToBase64Binary = Utility.converBitmapToBase64(bitmap);
                        byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                        if (mediaFile != null) {
                            try {
                                FileOutputStream fos = new FileOutputStream(mediaFile);
                                fos.write(imageBytes);
                                fileObj.setDocFile(filename);
                                fileObj.setName(fileDesc);
                                fos.close();
                            } catch (FileNotFoundException e) {
                                Crashlytics.log(1, getClass().getName(), e.getMessage());
                                Crashlytics.logException(e);
                            } catch (IOException e) {
                                Crashlytics.log(1, getClass().getName(), e.getMessage());
                                Crashlytics.logException(e);
                            }
                        }
                    }
                } else if (filename.contains(".docx") || filename.contains(".doc")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                } else if (filename.contains(".xlsx") || filename.contains(".xls")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                } else if (filename.contains(".txt")) {
                    try {
                        encodeFileToBase64Binary =Utility.fileToBase64Conversion(data.getData(),context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);

                    } catch (Exception e) {

                    }
                } else if (filename.contains(".gif")) {
                    encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                } else if (filename.contains(".rar")) {
                    encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(),context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                } else if (filename.contains(".zip")) {
                    encodeFileToBase64Binary =Utility.fileToBase64Conversion(data.getData(),context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                }


                if (Utility.calcBase64SizeInKBytes(encodeFileToBase64Binary) > Utility.maxLimit) {
                    CustomDialog.alertWithOk(context, Utility.sizeMsg);
                    return;
                }
                if (fileShow) {
                    if (uploadFileList.size() > 0) {
                        for (int i = 1; i <= uploadFileList.size(); i++) {
                            fileObj.setBase64Data(encodeFileToBase64Binary);
                            fileObj.setFlag("N");
                            String seqNo = String.valueOf(i + 1);
                            Log.d("seqNo", "seqNo");
                            uploadFileList.add(fileObj);

                            break;
                        }
                    } else {
                        fileObj.setBase64Data(encodeFileToBase64Binary);
                        fileObj.setFlag("N");
                        uploadFileList.add(fileObj);
                    }
                }
                refreshList();

            }
        }

        if (requestCode == AppsConstant.REQ_CAMERA && resultCode == RESULT_OK) {
            final Intent intent = data;
            String path = intent.getStringExtra("response");
            Uri uri = Uri.fromFile(new File(path));
            if (uri == null) {
                Log.d("uri", "null");
            } else {
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File mediaFile = null;
                if (bitmap != null) {
                    byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                    File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                    if (mediaFile != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(mediaFile);
                            fos.write(imageBytes);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Crashlytics.log(1, getClass().getName(), e.getMessage());
                            Crashlytics.logException(e);
                        } catch (IOException e) {
                            Crashlytics.log(1, getClass().getName(), e.getMessage());
                            Crashlytics.logException(e);
                        }
                    }
                }
            }
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.image_preview_expense);
            final TextView filenameET = (TextView) dialog.findViewById(R.id.filenameET);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_preview);
            imageView.setImageBitmap(bitmap);

            int textColor = Utility.getTextColorCode(preferences);
            TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
            tv_header_text.setTextColor(textColor);
            tv_header_text.setText("Supporting Documents");
            int bgColor = Utility.getBgColorCode(context, preferences);
            FrameLayout fl_actionBarContainer = (FrameLayout) dialog.findViewById(R.id.fl_actionBarContainer);
            fl_actionBarContainer.setBackgroundColor(bgColor);

            (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (filenameET.getText().toString().equalsIgnoreCase("")) {
                        new AlertCustomDialog(context, "Please enter file name");
                    } else {
                        fileObj.setDocFile(filenameET.getText().toString() + ".jpg");
                        fileObj.setName(filenameET.getText().toString() + ".jpg");

                        boolean fileShow1 = true;

                        if (fileShow1) {
                            String encodeFileToBase64Binary = Utility.converBitmapToBase64(bitmap);
                            // Log.d("TAG","IMAGE SIZE : "+ Utility.calcBase64SizeInKBytes(encodeFileToBase64Binary));

                            if (uploadFileList.size() > 0) {
                                for (int i = 1; i <= uploadFileList.size(); i++) {
                                    fileObj.setBase64Data(encodeFileToBase64Binary);
                                    fileObj.setFlag("N");
                                    String seqNo = String.valueOf(i + 1);
                                    Log.d("seqNo", "seqNo");
                                    uploadFileList.add(fileObj);

                                    break;
                                }
                            } else {
                                fileObj.setBase64Data(encodeFileToBase64Binary);
                                fileObj.setFlag("N");

                                uploadFileList.add(fileObj);
                            }
                        }
                        refreshList();
                        dialog.dismiss();
                    }
                }
            });
            (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
*/
    }

   /* private void refreshList() {
        if (uploadFileList != null && uploadFileList.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            expenseRecyclerView.setVisibility(View.VISIBLE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList,context,AppsConstant.ADD,errorLinearLayout);
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            expenseRecyclerView.setVisibility(View.GONE);
        }
    }*/
}
