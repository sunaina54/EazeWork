package hr.eazework.com.ui.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import hr.calender.caldroid.CaldroidFragment;
import hr.calender.caldroid.CaldroidListener;
import hr.eazework.com.FileUtils;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.SearchOnbehalfActivity;
import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.EmpLeaveModel;
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetCorpEmpParamResultResponse;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveDetailResponseModel;
import hr.eazework.com.model.LeaveRejectResponseModel;
import hr.eazework.com.model.LeaveReqDetailModel;
import hr.eazework.com.model.LeaveReqsItem;
import hr.eazework.com.model.LeaveRequestDetailsModel;
import hr.eazework.com.model.LeaveRequestModel;
import hr.eazework.com.model.LeaveResponseModel;
import hr.eazework.com.model.LeaveTypeModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.model.WFHRejectRequestModel;
import hr.eazework.com.model.WFHRequestDetailItem;
import hr.eazework.com.ui.adapter.DocumentUploadAdapter;
import hr.eazework.com.ui.adapter.RemarksAdapter;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.DateTimeUtil;
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
    private View rootView;
    public static final String TAG = "CreateNewLeaveFragment";
    private String screenName = "CreateNewLeaveFragment";
    private CaldroidFragment dialogCaldroidFragment;
    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private LeaveTypeModel leaveTypeModel;
    private Calendar startDate;
    private boolean isCompensatory;
    private Calendar toDate;
    private String leaveId;
    private EditText etRemark;
    private float availableLeaves;
    private List<String> extensionList;
    private ArrayList<String> mRhList;
    protected String selectedRs = "";
    private boolean isRhSelected = false;
    private boolean isSubmitClicked = true;
    private Preferences preferences;
    private RelativeLayout searchLayout;
    private TextView empNameTV;
    public static int LEAVE_EMP = 1;
    private EmployItem employItem;
    private LinearLayout errorLinearLayout;
    private RecyclerView expenseRecyclerView;
    private Context context;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private Bitmap bitmap = null;
    private String purpose = "";
    private String halfDayFS;
    private static int UPLOAD_DOC_REQUEST = 2;
    private String empId, reqId;
    private Button rejectBTN, saveDraftBTN, approvalBTN, deleteBTN, submitBTN;
    private ImageView plus_create_newIV;
    private String fromButton = "";
    private int approvalLevel, status;
    private String startDate1 = "", endDate1 = "";
    private TextView daysTV;
    private RecyclerView remarksRV;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private LinearLayout remarksDataLl, remarksLinearLayout, halfLl;
    private boolean isDaysCount = false;
    private CheckBox firstHalfCB, secondHalfCB;

    private LeaveReqsItem leaveReqsItem;
    private String defaultFromDateLabel = "From Date", defaultToDateLable = "To Date", value = "--/--/----", defaultLeaveLable = "Select Leave";

    public LeaveReqsItem getLeaveReqsItem() {
        return leaveReqsItem;
    }

    public void setLeaveReqsItem(LeaveReqsItem leaveReqsItem) {
        this.leaveReqsItem = leaveReqsItem;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    private EmployeeLeaveModel employeeLeaveModel;

    public EmployeeLeaveModel getEmployeeLeaveModel() {
        return employeeLeaveModel;
    }

    public void setEmployeeLeaveModel(EmployeeLeaveModel employeeLeaveModel) {
        this.employeeLeaveModel = employeeLeaveModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(screenName!=null && screenName.equalsIgnoreCase(AttendanceApprovalFragment.screenName)){
            this.setShowPlusMenu(false);
            this.setShowEditTeamButtons(false);
        }else{
            this.setShowPlusMenu(false);
            this.setShowEditTeamButtons(true);
        }
        super.onCreate(savedInstanceState);
    }

    private void setupLeave() {
        if (employItem != null) {
            MainActivity.isAnimationLoaded = false;
            String empCode = String.valueOf(employItem.getEmpID());
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
        this.startDate = null;
        this.toDate = null;
        this.leaveTypeModel = null;
        reqId = "0";
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.ll_create_new_leave,null);
        context = getContext();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(getActivity(), preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        halfLl = (LinearLayout) rootView.findViewById(R.id.halfLl);
        halfLl.setVisibility(View.GONE);
        firstHalfCB = (CheckBox) rootView.findViewById(R.id.firstHalfCB);
        firstHalfCB.setOnCheckedChangeListener(this);
        secondHalfCB = (CheckBox) rootView.findViewById(R.id.secondHalfCB);
        secondHalfCB.setOnCheckedChangeListener(this);

        remarksDataLl = (LinearLayout) rootView.findViewById(R.id.remarksDataLl);
        remarksDataLl.setVisibility(View.GONE);
        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);

        etRemark = (EditText) rootView.findViewById(R.id.et_remark);
        etRemark.setText("");
        daysTV = (TextView) rootView.findViewById(R.id.daysTV);
        daysTV.setText("");
        rootView.findViewById(R.id.tv_select_leave_type).setOnClickListener(this);
        rootView.findViewById(R.id.tv_select_rest_leaves).setOnClickListener(this);

        rootView.findViewById(R.id.ll_from_date).setOnClickListener(this);
        rootView.findViewById(R.id.ll_to_date).setOnClickListener(this);

        datePickerDialog1 = pickDateFromCalenderToDate(context, ((TextView) rootView.findViewById(R.id.tv_to_date)),  ((TextView) rootView.findViewById(R.id.tv_to_day)), AppsConstant.DATE_FORMATE);

        datePickerDialog2 = pickDateFromCalenderFromDate(context, ((TextView) rootView.findViewById(R.id.tv_from_date)),  ((TextView) rootView.findViewById(R.id.tv_from_day)), AppsConstant.DATE_FORMATE);

        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
        rootView.findViewById(R.id.btn_save_as_draft).setOnClickListener(this);
        empNameTV = (TextView) rootView.findViewById(R.id.empNameTV);

        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setOnCheckedChangeListener(this);
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setOnClickListener(this);


        if (ModelManager.getInstance().getLeaveTypeModel() == null) {
            showHideProgressView(true);
        } else {
            showHideProgressView(false);
        }

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Leave Request");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveTypeModel != null) {
                    if (isSubmitClicked) {
                        isSubmitClicked = false;
                        fromButton = "Submit";
                        doSubmitOperation();
                    }
                } else {
                    new AlertCustomDialog(getContext(), "Please select leave type");
                }

            }
        });

        saveDraftBTN = (Button) rootView.findViewById(R.id.saveDraftBTN);

        saveDraftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Save";
                if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                    sendLeaveApprovalData();
                } else {
                    doSubmitOperationForSaveAsDraft();
                }
            }
        });

        deleteBTN = (Button) rootView.findViewById(R.id.deleteBTN);
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Delete";
                doSubmitOperationForSaveAsDraft();
            }
        });

        submitBTN = (Button) rootView.findViewById(R.id.submitBTN);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmitClicked) {
                    isSubmitClicked = false;
                    fromButton = "Submit";
                    doSubmitOperation();
                }
            }
        });


        rejectBTN = (Button) rootView.findViewById(R.id.rejectBTN);
        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectLeaveRequest();
            }
        });
        approvalBTN = (Button) rootView.findViewById(R.id.approvalBTN);
        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Approve";
                sendLeaveApprovalData();
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


        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
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
        });


        employItem = new EmployItem();
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();

        employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
        empId = loginUserModel.getUserModel().getEmpId();
        employItem.setName(loginUserModel.getUserModel().getUserName());
        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());


        if (!getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
            uploadFileList = new ArrayList<SupportDocsItemModel>();
            saveDraftBTN.setVisibility(View.VISIBLE);
        }

        setupLeave();
        updateEmploy();
        //etRemark.setText("");
        sendAdvanceRequestData();

        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        // etRemark.setText("");
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void disabledFieldData() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.tv_select_leave_type)).setEnabled(false);
    }

    private void disabledFieldDataForView() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.tv_select_leave_type)).setEnabled(false);
        rootView.findViewById(R.id.ll_from_date).setEnabled(false);
        rootView.findViewById(R.id.ll_to_date).setEnabled(false);
        plus_create_newIV.setVisibility(View.GONE);
        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setEnabled(false);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setEnabled(false);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setEnabled(false);
        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setEnabled(false);

    }

    private void updateEmploy() {
        empNameTV.setText(employItem.getName() + " (" + employItem.getEmpCode() + ")");
    }

    private void doSubmitOperationForSaveAsDraft() {
        if (startDate != null) {
            startDate1 = String.format("%1$td/%1$tm/%1$tY", this.startDate);
        }

        if (toDate != null) {
            endDate1 = String.format("%1$td/%1$tm/%1$tY", toDate);
        }
        String leaveId = "0";

        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        String empCode = String.valueOf(employItem.getEmpID());

        String remark = etRemark.getText().toString();
        if (leaveTypeModel != null && leaveTypeModel.getLeaveId() != null) {
            leaveId = leaveTypeModel.getLeaveId();
        }
        if (isRhSelected) {
            /*if (selectedRs == null || selectedRs.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please select leave date", Toast.LENGTH_LONG).show();
                return;
            }*/
            showHideProgressView(true);
            setActiveInActive(false);
            if (loginUserModel != null && loginUserModel != null) {
                MainActivity.isAnimationLoaded = false;
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
          /*      CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), selectedRs,
                                selectedRs, "1", remark, uploadFileList),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);*/


                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                empCode,
                                leaveId, selectedRs,
                                selectedRs, "1", remark, uploadFileList, fromButton, reqId, halfDayFS),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);


            }
            return;
        }

        if (leaveTypeModel != null && leaveTypeModel.getLeaveId() != null) {
            isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
        }
        if (isCompensatory) {
         /*   if (this.startDate == null) {
                Toast.makeText(getActivity(), "Please select Date Worked", Toast.LENGTH_LONG).show();
                isSubmitClicked = true;
                return;
            }
            if (this.toDate == null) {
                Toast.makeText(getActivity(), "Please select Compensatory Off Date", Toast.LENGTH_LONG).show();
                isSubmitClicked = true;
                return;
            }*/
            showHideProgressView(true);
            setActiveInActive(false);
            if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                // startDate1 = String.format("%1$td/%1$tm/%1$tY", this.startDate);
                //  endDate1 = String.format("%1$td/%1$tm/%1$tY", toDate);
                MainActivity.isAnimationLoaded = false;
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
            /*    CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), startDate, endDate,
                                "" + getOneDayData(), remark, uploadFileList),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);*/


                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                empCode,
                                leaveId, startDate1, endDate1,
                                "" + getOneDayData(), remark, uploadFileList, fromButton, reqId, halfDayFS),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);


            }
            return;
        }

        showHideProgressView(true);

        // startDate1 = String.format("%1$td/%1$tm/%1$tY", this.startDate);
        //  endDate1 = String.format("%1$td/%1$tm/%1$tY", this.toDate);
        if (getTotalDay(this.startDate, toDate) == 1) {
            setActiveInActive(false);
            if (getOneDayData() != 1) {
                if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                    MainActivity.isAnimationLoaded = false;
                    if (uploadFileList != null && uploadFileList.size() > 0) {
                        for (int i = 0; i < uploadFileList.size(); i++) {
                            SupportDocsItemModel model = uploadFileList.get(i);
                            model.setSeqNo(i + 1);
                            uploadFileList.set(i, model);
                        }
                    }
               /*     CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.getSaveLeaveRequestData(
                                    empCode,
                                    leaveTypeModel.getLeaveId(), startDate, endDate,
                                    "" + getOneDayData(), remark, uploadFileList),
                            CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);*/


                    CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                    empCode,
                                    leaveId, startDate1, endDate1,
                                    "" + getOneDayData(), remark, uploadFileList, fromButton, reqId, halfDayFS),
                            CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);

                }
            } else {
                if (leaveTypeModel != null && leaveTypeModel.getLeaveId() != null) {
                    if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                        MainActivity.isAnimationLoaded = false;
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.GetLeaveReqTotalDays(empCode,
                                        leaveTypeModel.getLeaveId(),
                                        startDate1,
                                        endDate1),
                                CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY,
                                true);
                    } else {
                        MainActivity.isAnimationLoaded = false;
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                        empCode,
                                        leaveId, startDate1, endDate1,
                                        "", remark, uploadFileList, fromButton, reqId, halfDayFS),
                                CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                    }
                }
            }
        } else {
            if (leaveTypeModel != null && leaveTypeModel.getLeaveId() != null) {
                if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                    MainActivity.isAnimationLoaded = false;
                    CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.GetLeaveReqTotalDays(empCode,
                                    leaveTypeModel.getLeaveId(),
                                    startDate1,
                                    endDate1),
                            CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY, true);
                }
            } else {
                MainActivity.isAnimationLoaded = false;
                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                empCode,
                                leaveId, startDate1, endDate1,
                                "", remark, uploadFileList, fromButton, reqId, halfDayFS),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
            }
        }

    }


    private void doSubmitOperation() {
        if (startDate1 != null && !startDate1.equalsIgnoreCase("")) {
            startDate = DateTimeUtil.convertStringDateToCalendar(startDate1);
        }

        if (endDate1 != null && !endDate1.equalsIgnoreCase("")) {
            toDate = DateTimeUtil.convertStringDateToCalendar(endDate1);
        }


        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        String empCode = String.valueOf(employItem.getEmpID());

        String remark = etRemark.getText().toString();
        if (isRhSelected) {
            if (selectedRs == null || selectedRs.equalsIgnoreCase("")) {
                isSubmitClicked = true;
                Toast.makeText(getActivity(), "Please select leave date", Toast.LENGTH_LONG).show();
                return;
            }
            showHideProgressView(true);
            setActiveInActive(false);
            if (loginUserModel != null && loginUserModel != null) {
                MainActivity.isAnimationLoaded = false;
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), selectedRs,
                                selectedRs, "1", remark, uploadFileList, reqId, halfDayFS),
                        CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);


            }
            return;
        }
        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
        if (isCompensatory) {
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
            if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                String startDate = String.format("%1$td/%1$tm/%1$tY", this.startDate);
                String endDate = String.format("%1$td/%1$tm/%1$tY", toDate);
                MainActivity.isAnimationLoaded = false;
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                CommunicationManager.getInstance().sendPostRequest(
                        this,
                        AppRequestJSONString.getSaveLeaveRequestData(
                                empCode,
                                leaveTypeModel.getLeaveId(), startDate, endDate,
                                "" + getOneDayData(), remark, uploadFileList, reqId, halfDayFS),
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
                    if (uploadFileList != null && uploadFileList.size() > 0) {
                        for (int i = 0; i < uploadFileList.size(); i++) {
                            SupportDocsItemModel model = uploadFileList.get(i);
                            model.setSeqNo(i + 1);
                            uploadFileList.set(i, model);
                        }
                    }
                    CommunicationManager.getInstance().sendPostRequest(
                            this,
                            AppRequestJSONString.getSaveLeaveRequestData(
                                    empCode,
                                    leaveTypeModel.getLeaveId(), startDate, endDate,
                                    "" + getOneDayData(), remark, uploadFileList, reqId, halfDayFS),
                            CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
/*
                    if (fromButton.equalsIgnoreCase(AppsConstant.SAVE_AS_DRAFT)) {
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                        empCode,
                                        leaveTypeModel.getLeaveId(), startDate, endDate,
                                        "" + getOneDayData(), remark, uploadFileList, fromButton),
                                CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                    }*/
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
                selectedRs = null;
                if (!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
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
            etRemark.setText("");
        }
    }

    protected void updateCompasatory(boolean isCompasatory) {
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

            if (!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
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

                if (!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
                } else {
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.VISIBLE);
                }


            } else {
                rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.GONE);
            }


            if (startDate1 != null && endDate1 != null && !startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("") && (dayBetween(startDate1, endDate1, "dd/mm/yyyy") == 1)) {

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

                if (!leaveTypeModel.isDayP25() && !leaveTypeModel.isDayP50() && !leaveTypeModel.isDayP75()) {
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

    public long dayBetween(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  showLog(CreateNewLeaveFragment.class, "Days Total " + (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000) + 1);
        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000) + 1;

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
                    String leaveResp1 = response.getResponseData();
                    Log.d("TAG", "Leave Response : " + leaveResp1);

                    LeaveResponseModel leaveResult = LeaveResponseModel.create(leaveResp1);
                    if (leaveResult != null && leaveResult.getSaveLeaveReqResult() != null
                            && leaveResult.getSaveLeaveReqResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                        isSubmitClicked = true;
                        etRemark.setText("");
                        CustomDialog.alertOkWithFinishFragment(context, leaveResult.getSaveLeaveReqResult().getErrorMessage(),
                                mUserActionListener, IAction.HOME_VIEW, true);
                    } else {
                        isSubmitClicked = true;
                        new AlertCustomDialog(getActivity(), leaveResult.getSaveLeaveReqResult().getErrorMessage());

                    }
                    break;
                   /* try {
                        JSONObject object = new JSONObject(response.getResponseData());
                        jsonObject = object.optJSONObject("SaveLeaveReqResult");
                        if (jsonObject.optInt("ErrorCode", -1) != 0) {
                            String errorMessage = jsonObject.optString("ErrorMessage", "");
                            new AlertCustomDialog(getActivity(), errorMessage);
                            isSubmitClicked = true;

                        } else {

                           // Utility.displayMessage(getContext(), "Leave submitted");
                            Utility.displayMessage(getContext(), jsonObject.optString("ErrorMessage", ""));

                            isSubmitClicked = true;
                            etRemark.setText("");
                            MenuItemModel itemModel = ModelManager.getInstance().getMenuItemModel();
                            if (itemModel != null) {
                                MenuItemModel model = itemModel.getItemModel(MenuItemModel.LEAVE_KEY);
                              *//*  if (model != null && model.isAccess()) {
                                    mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                                } else {*//*
                                mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                                // }
                            }


                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                        Crashlytics.logException(e);
                    }*/

                case CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY:
                    try {
                        String empCode = String.valueOf(employItem.getEmpID());
                        jsonObject = (new JSONObject(response.getResponseData())).optJSONObject("GetLeaveReqTotalDaysResult");
                        double d = jsonObject.optDouble("TotalDays", 0);
                        if (isDaysCount) {
                            daysTV.setText(d + "");
                            isDaysCount = false;
                            return;
                        }
                        showHideProgressView(true);

                        String fromDateFormatted = String.format("%1$td/%1$tm/%1$tY", startDate);
                        String toDateFormatted = String.format("%1$td/%1$tm/%1$tY", toDate);

                        String leaveId = leaveTypeModel.getLeaveId();

                        EditText etRemark = (EditText) rootView.findViewById(R.id.et_remark);
                        String remark = etRemark.getText().toString();
                        if (uploadFileList != null && uploadFileList.size() > 0) {
                            for (int i = 0; i < uploadFileList.size(); i++) {
                                SupportDocsItemModel model = uploadFileList.get(i);
                                model.setSeqNo(i + 1);
                                uploadFileList.set(i, model);
                            }
                        }
                        if (fromButton.equalsIgnoreCase("Submit")) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getSaveLeaveRequestData(
                                            empCode,
                                            leaveId, fromDateFormatted, toDateFormatted, "" + d,
                                            remark, uploadFileList, reqId, halfDayFS),
                                    CommunicationConstant.API_SAVE_LEAVE_REQUEST,
                                    true);
                        }

                        if (fromButton.equalsIgnoreCase(AppsConstant.SAVE_AS_DRAFT)) {

                            CommunicationManager.getInstance().sendPostRequest(
                                    this,
                                    AppRequestJSONString.getSaveAsDraftLeaveRequestData(
                                            empCode,
                                            leaveId, startDate1, endDate1,
                                            "", remark, uploadFileList, fromButton, reqId, halfDayFS),
                                    CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                        }
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
                        if (employeeLeaveModel != null && employeeLeaveModel.getmReqID() != null
                                && !employeeLeaveModel.getmReqID().equalsIgnoreCase("0")) {   //Approval edit
                            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.LEAVE_EDIT)) {
                                reqId = employeeLeaveModel.getmReqID();
                                remarksDataLl.setVisibility(View.VISIBLE);
                                sendViewLeaveRequestSummaryData();
                                disabledFieldData();
                            }

                            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.LEAVE_WITHDRAWAL)) {
                                reqId = employeeLeaveModel.getmReqID();
                                remarksDataLl.setVisibility(View.VISIBLE);
                                sendViewLeaveRequestSummaryData();
                                disabledFieldDataForView();
                            }
                        }

                        if (leaveReqsItem != null && leaveReqsItem.getReqID() != null && !leaveReqsItem.getReqID().equalsIgnoreCase("0")) {
                            reqId = leaveReqsItem.getReqID();
                            remarksDataLl.setVisibility(View.VISIBLE);
                            sendViewLeaveRequestSummaryData();
                        }

                        if (!getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.GetCorpEmpParam(), CommunicationConstant.API_GET_CORPEMP_PARAM,
                                    true);
                        }


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
                        Log.e("Leave", e.getMessage(), e);
                    }

                    break;


                case CommunicationConstant.API_GET_CORPEMP_PARAM:
                    try {
                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
                        if (responseData != null) {
                            GetCorpEmpParamResultResponse corpEmpParamResultResponse = GetCorpEmpParamResultResponse.create(responseData);
                            if (corpEmpParamResultResponse != null && corpEmpParamResultResponse.getGetCorpEmpParamResult() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode().equalsIgnoreCase("0")) {

                                if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList() != null &&
                                        corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().size() > 0) {
                                    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue() != null) {

                                        if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam().equalsIgnoreCase("LeaveOnBehalfOfYN") && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue().equalsIgnoreCase("Y")) {
                                            ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            } else {

                            }
                        }
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        Log.e("Leave", e.getMessage(), e);
                    }

                    break;
                case CommunicationConstant.API_GET_LEAVE_REQUEST_DETAIL:
                    String leaveResp = response.getResponseData();
                    Log.d("TAG", "Leave Response : " + leaveResp);
                    LeaveDetailResponseModel leaveDetailResponseModel = LeaveDetailResponseModel.create(leaveResp);
                    if (leaveDetailResponseModel != null && leaveDetailResponseModel.getGetLeaveRequestDetailsResult() != null
                            && leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                            && leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails() != null) {
                        reqId = leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails().getReqID() + "";
                        updateUI(leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails());
                        refreshRemarksList((leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails().getRemarkList()));
                        uploadFileList = leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails().getAttachments();
                        refreshDocumentList(leaveDetailResponseModel.getGetLeaveRequestDetailsResult().getLeaveRequestDetails().getAttachments());

                    }
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
                case CommunicationConstant.API_APPROVE_LEAVE_REQUEST:
                    String leaveApprove = response.getResponseData();
                    Log.d("TAG", "Leave Response : " + leaveApprove);
                    LeaveResponseModel leaveResponseModel = LeaveResponseModel.create(leaveApprove);
                    if (leaveResponseModel != null && leaveResponseModel.getApproveLeaveRequestResult() != null
                            && leaveResponseModel.getApproveLeaveRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                        CustomDialog.alertOkWithFinishFragment(context, leaveResponseModel.getApproveLeaveRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                    } else {
                        new AlertCustomDialog(getActivity(), leaveResponseModel.getApproveLeaveRequestResult().getErrorMessage());

                    }
                    break;
                case CommunicationConstant.API_GET_ADVANCE_PAGE_INIT:
                    String str1 = response.getResponseData();
                    Log.d("TAG", "Advance Response : " + str1);
                    advanceRequestResponseModel = AdvanceRequestResponseModel.create(str1);
                    if (advanceRequestResponseModel != null &&
                            advanceRequestResponseModel.getGetAdvancePageInitResult() != null &&
                            advanceRequestResponseModel.getGetAdvancePageInitResult().getErrorCode().
                                    equalsIgnoreCase(AppsConstant.SUCCESS)) {
                        extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
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

    private void refreshRemarksList(ArrayList<RemarkListItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            remarksRV.setVisibility(View.VISIBLE);
            RemarksAdapter adapter = new RemarksAdapter(remarksItems, context, screenName, remarksLinearLayout);
            remarksRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
        }
    }

    private void refreshDocumentList(ArrayList<SupportDocsItemModel> uploadFileList) {
        if (uploadFileList != null && uploadFileList.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            expenseRecyclerView.setVisibility(View.VISIBLE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList, context, AppsConstant.EDIT, errorLinearLayout, getActivity());
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            expenseRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_as_draft:
                new AlertCustomDialog(getActivity(), "This feature is under development.");
                break;
            case R.id.searchLayout:
                Intent theIntent = new Intent(getActivity(), SearchOnbehalfActivity.class);
                theIntent.putExtra("SearchType", LEAVE_EMP);
                startActivityForResult(theIntent, LEAVE_EMP);

                break;
            case R.id.btn_submit:
                String empCode = String.valueOf(employItem.getEmpID());
                LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                EditText etRemark = (EditText) rootView.findViewById(R.id.et_remark);
                String remark = etRemark.getText().toString();
                if (isRhSelected) {
                    showHideProgressView(true);
                    setActiveInActive(false);

                    if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                        MainActivity.isAnimationLoaded = false;
                        if (uploadFileList != null && uploadFileList.size() > 0) {
                            for (int i = 0; i < uploadFileList.size(); i++) {
                                SupportDocsItemModel model = uploadFileList.get(i);
                                model.setSeqNo(i + 1);
                                uploadFileList.set(i, model);
                            }
                        }
                        CommunicationManager.getInstance().sendPostRequest(
                                this,
                                AppRequestJSONString.getSaveLeaveRequestData(empCode,
                                        leaveTypeModel.getLeaveId(), selectedRs,
                                        selectedRs, "1", remark, uploadFileList, reqId, halfDayFS),
                                CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                    }
                    return;
                }
                if (startDate == null) {
                    Utility.displayMessage(getContext(), "Please select from date.");
                    return;
                }
                if (toDate == null) {
                    Utility.displayMessage(getContext(), "Please select to date.");
                    return;
                }
                if (getTotalDay(startDate, toDate) < 0) {
                    Utility.displayMessage(getContext(), "Please select to date later than from date.");
                    return;
                }
                showHideProgressView(true);
                String startDate = String.format("%1$td/%1$tm/%1$tY", this.startDate);
                String endDate = String.format("%1$td/%1$tm/%1$tY", toDate);
                if (getTotalDay(this.startDate, toDate) == 1) {
                    setActiveInActive(false);
                    if (getOneDayData() != 1) {
                        if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                            MainActivity.isAnimationLoaded = false;
                            if (uploadFileList != null && uploadFileList.size() > 0) {
                                for (int i = 0; i < uploadFileList.size(); i++) {
                                    SupportDocsItemModel model = uploadFileList.get(i);
                                    model.setSeqNo(i + 1);
                                    uploadFileList.set(i, model);
                                }
                            }
                            CommunicationManager.getInstance().sendPostRequest(
                                    this,
                                    AppRequestJSONString.getSaveLeaveRequestData(
                                            empCode, leaveTypeModel
                                                    .getLeaveId(), startDate, endDate,
                                            "" + getOneDayData(), remark, uploadFileList, reqId, halfDayFS),
                                    CommunicationConstant.API_SAVE_LEAVE_REQUEST, true);
                        }
                    } else {
                        if (loginUserModel != null && loginUserModel.getUserModel() != null) {
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
                    if (loginUserModel != null && loginUserModel.getUserModel() != null) {
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
                            if (!startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("")) {
                                if (leaveTypeModel != null) {
                                    daysCount();
                                }
                            }
                            builder.dismiss();

                        }
                    });
                    leaveTypeDialog.show();
                }


                break;
            case R.id.ll_from_date:
                datePickerDialog2.show();

               /* dialogCaldroidFragment = new CaldroidFragment();
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
                        startDate1 = formatedData;
                        ((TextView) rootView.findViewById(R.id.tv_from_date)).setText(formatedData);
                        dialogCaldroidFragment.dismiss();
                        if (leaveTypeModel == null)
                            return;
                        if (!startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("")) {
                            if (leaveTypeModel != null) {
                                daysCount();
                            }
                        }

                        updateLeaveDayType(leaveTypeModel);
                        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");

                        if (isCompensatory) {
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
                dialogCaldroidFragment.show(getChildFragmentManager(), dialogTag);*/
                break;
            case R.id.ll_to_date:
                    datePickerDialog1.show();
                /*// Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        toDate = calendar;
                        ((TextView) rootView.findViewById(R.id.tv_to_day)).setText(String.format("%1$tA", calendar));
                        String formatTodate = String.format("%1$td/%1$tm/%1$tY", calendar);
                        endDate1 = formatTodate;
                        ((TextView) rootView.findViewById(R.id.tv_to_date)).setText(formatTodate);
                        dialogCaldroidFragment.dismiss();
                        if (leaveTypeModel == null)
                            return;
                        if (!startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("")) {
                            if (leaveTypeModel != null) {
                                daysCount();
                            }
                        }
                        updateLeaveDayType(leaveTypeModel);
                        boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
                        if (isCompensatory) {
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
                dialogCaldroidFragment.show(getChildFragmentManager(), dialogTag2);*/
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
                    halfLl.setVisibility(View.GONE);
                    daysTV.setText("0.25");
                    break;
                case R.id.rb_half_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
                    daysTV.setText("0.50F");
                    halfLl.setVisibility(View.VISIBLE);
                    halfDayFS = "F";
                    Log.d("TAG", "Half day " + halfDayFS);
                    break;
                case R.id.rb_75_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
                    daysTV.setText("0.75");
                    halfLl.setVisibility(View.GONE);
                    break;
                case R.id.rb_full_day:
                    ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(false);
                    ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(false);
                    daysTV.setText("1.00");
                    halfLl.setVisibility(View.GONE);
                    break;
                case R.id.firstHalfCB:
                    secondHalfCB.setChecked(false);
                    halfDayFS = "F";
                    daysTV.setText("0.50F");
                    Log.d("TAG", "Half day " + halfDayFS);
                    break;
                case R.id.secondHalfCB:
                    firstHalfCB.setChecked(false);
                    halfDayFS = "S";
                    daysTV.setText("0.50S");
                    Log.d("TAG", "Half day " + halfDayFS);
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

    public void sendAdvanceRequestData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_PAGE_INIT, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LEAVE_EMP) {
            if (data != null) {
                EmployItem item = (EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_EMP);
                if (item != null) {
                    empNameTV.setText(item.getName());
                    empId = String.valueOf(item.getEmpID());
                    employItem = item;
                }
                leaveTypeModel = null;
                showHideProgressView(true);
                setupLeave();
                etRemark.setText("");
                daysTV.setText("");
                updateLeaveAvailablity(null);
                updateLeaveDayType(leaveTypeModel);
                updateLeaveSelectionType(leaveTypeModel);
                updateFromAndToDate(leaveTypeModel);

            }
        }

        final SupportDocsItemModel fileObj = new SupportDocsItemModel();
        if (requestCode == UPLOAD_DOC_REQUEST && resultCode == RESULT_OK) {
            boolean fileShow = true;
            final Uri uri = data.getData();
            String encodeFileToBase64Binary = null;
            if (data != null) {
                String path = data.getStringExtra("path");
                System.out.print(path);
                Uri uploadedFilePath = data.getData();
                String filename = Utility.getFileName(uploadedFilePath, context);
                filename = filename.toLowerCase();
                String fileDesc = Utility.getFileName(uploadedFilePath, context);
                String[] extList = filename.split("\\.");
                System.out.print(extList[1].toString());
                String extension = "." + extList[extList.length - 1];
                encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                Log.d("TAG", "RAR Base 64 :" + encodeFileToBase64Binary);
                List<String> extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getMessage());
                    return;
                }
                fileObj.setDocPathUri(uploadedFilePath);

                if (filename.contains(".pdf")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);
                        fileObj.setExtension(extension);

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
                                fileObj.setExtension(extension);
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
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);
                        fileObj.setExtension(extension);

                    } catch (Exception e) {

                    }
                } else if (filename.contains(".xlsx") || filename.contains(".xls")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);
                        fileObj.setExtension(extension);

                    } catch (Exception e) {

                    }
                } else if (filename.contains(".txt")) {
                    try {
                        encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);
                        fileObj.setExtension(extension);
                    } catch (Exception e) {

                    }
                } else if (filename.contains(".gif")) {
                    encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                    fileObj.setExtension(extension);
                } else if (filename.contains(".rar")) {
                    encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                    fileObj.setExtension(extension);
                } else if (filename.contains(".zip")) {
                    encodeFileToBase64Binary = Utility.fileToBase64Conversion(data.getData(), context);
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                    fileObj.setExtension(extension);
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
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.image_preview_expense);

            final TextView filenameET = (TextView) dialog.findViewById(R.id.filenameET);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_preview);
            imageView.setImageBitmap(bitmap);

            int textColor = Utility.getTextColorCode(preferences);
            TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
            tv_header_text.setTextColor(textColor);
            tv_header_text.setText("Supporting Documents");
            int bgColor = Utility.getBgColorCode(context, preferences);
            RelativeLayout fl_actionBarContainer = (RelativeLayout) dialog.findViewById(R.id.fl_actionBarContainer);
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
                                    fileObj.setExtension(".jpg");
                                    String seqNo = String.valueOf(i + 1);
                                    Log.d("seqNo", "seqNo");
                                    uploadFileList.add(fileObj);

                                    break;
                                }
                            } else {
                                fileObj.setBase64Data(encodeFileToBase64Binary);
                                fileObj.setFlag("N");
                                fileObj.setExtension(".jpg");
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
    }

    private void refreshList() {
        if (uploadFileList != null && uploadFileList.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            expenseRecyclerView.setVisibility(View.VISIBLE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList, context, AppsConstant.EDIT, errorLinearLayout, getActivity());
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);

            expenseRecyclerView.setVisibility(View.GONE);
        }
    }

    private void sendViewLeaveRequestSummaryData() {
        GetWFHRequestDetail requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(reqId);
        requestDetail.setAction(AppsConstant.EDIT_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_LEAVE_REQUEST_DETAIL, true);
    }

    private void rejectLeaveRequest() {
        if (etRemark.getText().toString().equalsIgnoreCase("")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
            return;
        }
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(reqId);
        rejectRequestModel.setComments(etRemark.getText().toString());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_LEAVE_REQUEST, true);

    }

    private void updateUI(LeaveRequestDetailsModel item) {
        ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setVisibility(View.GONE);
        ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setVisibility(View.GONE);
        ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.GONE);
        ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setVisibility(View.GONE);
        leaveId = item.getLeaveID();
        empId = item.getForEmpID() + "";
        approvalLevel = Integer.parseInt(item.getApprovalLevel());
        status = item.getReqStatus();

        empNameTV.setText(item.getForEmpName());
        ((TextView) rootView.findViewById(R.id.tv_select_leave_type)).setText(item.getLeaveDesc());
        LeaveTypeModel leaveType = ModelManager.getInstance().getLeaveTypeModel();
        if (leaveType != null) {
            for (LeaveTypeModel leaveItem : leaveType.getLeaveTypeList()) {
                if (leaveItem.getLeaveId().equalsIgnoreCase(item.getLeaveID())) {
                    leaveTypeModel = leaveItem;
                    break;
                }
            }
        }

        if (item.getAvailableDays() != null) {
            rootView.findViewById(R.id.ll_avail_leaves).setVisibility(View.VISIBLE);

            ((TextView) rootView.findViewById(R.id.ll_avail_leaves_count)).setText(item.getAvailableDays());
        }
        if (item.getConsumedDays() != null) {
            rootView.findViewById(R.id.ll_consume_leaves).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.ll_consume_leaves_count)).setText(item.getConsumedDays());
        }

        if (item.getPartialDay() != null && item.getPartialDay().getPartialDayParams() != null) {
            rootView.findViewById(R.id.rg_leave_time_type).setVisibility(View.VISIBLE);
            if (item.getPartialDay().getPartialDayParams().getDayP50Visible().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setVisibility(View.VISIBLE);
            }
            if (item.getPartialDay().getPartialDayParams().getDayP25Visible().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setVisibility(View.VISIBLE);
            }
            if (item.getPartialDay().getPartialDayParams().getDayFullVisible().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setVisibility(View.VISIBLE);
            }
            if (item.getPartialDay().getPartialDayParams().getDayP75Visible().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setVisibility(View.VISIBLE);
            }

        }
        if (item.getPartialDay() != null && item.getPartialDay().getPartialDayData() != null
                && item.getPartialDay().getPartialDayData().getDayFull().equalsIgnoreCase("Y") || item.getPartialDay().getPartialDayData().getDayP50().equalsIgnoreCase("Y")
                || item.getPartialDay().getPartialDayData().getDayP75().equalsIgnoreCase("Y") || item.getPartialDay().getPartialDayData().getDayP25().equalsIgnoreCase("Y")) {

            if (item.getPartialDay().getPartialDayData().getDayP50().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_half_day)).setChecked(true);
                if (item.getHalfDayFS() != null) {
                    halfLl.setVisibility(View.VISIBLE);
                    if (item.getHalfDayFS().equalsIgnoreCase("F")) {
                        firstHalfCB.setChecked(true);

                    } else {
                        secondHalfCB.setChecked(true);
                    }
                }

            }
            if (item.getPartialDay().getPartialDayData().getDayFull().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_full_day)).setChecked(true);
            }
            if (item.getPartialDay().getPartialDayData().getDayP75().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_75_day)).setChecked(true);
            }
            if (item.getPartialDay().getPartialDayData().getDayP25().equalsIgnoreCase("Y")) {
                ((CheckBox) rootView.findViewById(R.id.rb_25_day)).setChecked(true);
            }

        }
        ((TextView) rootView.findViewById(R.id.tv_from_date)).setText(item.getStartDate());
        startDate1 = item.getStartDate();
        endDate1 = item.getEndDate();
        ((TextView) rootView.findViewById(R.id.tv_to_date)).setText(item.getEndDate());
        daysTV.setText(item.getTotalDays());
        etRemark.setText(item.getRemark());
        setupButtons(item);
    }

    private void setupButtons(LeaveRequestDetailsModel item) {
        if (item.getButtons() != null) {
            for (String button : item.getButtons()) {
                if (button.equalsIgnoreCase(AppsConstant.DRAFT)) {
                    saveDraftBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.REJECT)) {
                    rejectBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.APPROVE)) {
                    approvalBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.DELETE)) {
                    deleteBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.SUBMIT)) {
                    submitBTN.setVisibility(View.GONE);
                }
            }
        }
    }

    private void sendLeaveApprovalData() {
        String startDate = "", endDate = "";
        if (!((TextView) rootView.findViewById(R.id.tv_from_date)).getText().toString().equalsIgnoreCase(value)) {
            startDate = ((TextView) rootView.findViewById(R.id.tv_from_date)).getText().toString();
        }
        if (!((TextView) rootView.findViewById(R.id.tv_to_date)).getText().toString().equalsIgnoreCase(value)) {
            endDate = ((TextView) rootView.findViewById(R.id.tv_to_date)).getText().toString();
        }

        LeaveReqDetailModel leaveReqDetailModel = new LeaveReqDetailModel();
        leaveReqDetailModel.setReqID(reqId);
        leaveReqDetailModel.setForEmpID(empId);
        leaveReqDetailModel.setStartDate(startDate);
        leaveReqDetailModel.setEndDate(endDate);
        leaveReqDetailModel.setLeaveID(leaveId);
        leaveReqDetailModel.setRemarks(etRemark.getText().toString());
        leaveReqDetailModel.setTotalDays(daysTV.getText().toString());
        leaveReqDetailModel.setHalfDayFS(halfDayFS);
        leaveReqDetailModel.setApprovalLevel(approvalLevel);
        leaveReqDetailModel.setStatus(status);
        leaveReqDetailModel.setButton(fromButton);
        if (uploadFileList != null && uploadFileList.size() > 0) {
            for (int i = 0; i < uploadFileList.size(); i++) {
                SupportDocsItemModel model = uploadFileList.get(i);
                model.setSeqNo(i + 1);
                uploadFileList.set(i, model);
            }
        }
        leaveReqDetailModel.setAttachments(uploadFileList);
        LeaveRequestModel leaveRequestModel = new LeaveRequestModel();
        leaveRequestModel.setLeaveReqDetail(leaveReqDetailModel);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.leaveRequest(leaveRequestModel),
                CommunicationConstant.API_APPROVE_LEAVE_REQUEST, true);
    }

    private void daysCount() {
        isDaysCount = true;
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.GetLeaveReqTotalDays(empId,
                        leaveTypeModel.getLeaveId(), startDate1, endDate1),
                CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY, true);
    }

    public DatePickerDialog pickDateFromCalenderFromDate(Context mContext, final TextView dobTextView, final TextView dayTV, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        String dateStr=null;
        if(dobTextView.getText().toString().equalsIgnoreCase("") || dobTextView.getText().toString().equalsIgnoreCase("--/--/----")){
            dateStr= DateTimeUtil.currentDate(AppsConstant.DATE_FORMATE);
        }else{
            dateStr=dobTextView.getText().toString();
        }
        String date[] =dateStr.split("/");
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();
                startDate=calendar;
                calendar.set(year, monthOfYear, dayOfMonth);

                dayTV.setText(String.format("%1$tA", calendar));
                String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
                startDate1 = formatedData;
                dobTextView.setText(formatedData);
                if (leaveTypeModel == null)
                    return;
                if (!startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("")) {
                    if (leaveTypeModel != null) {
                        daysCount();
                    }
                }

                updateLeaveDayType(leaveTypeModel);
                boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");

                if (isCompensatory) {
                    updateCompasatory(isCompensatory);
                }


            }

        },Integer.parseInt(date[2]), Integer.parseInt(date[1])-1,
                Integer.parseInt(date[0]));
        return datePickerDialog;
    }

    public DatePickerDialog pickDateFromCalenderToDate(Context mContext, final TextView dobTextView, final TextView dayTV, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        String dateStr=null;
        if(dobTextView.getText().toString().equalsIgnoreCase("") || dobTextView.getText().toString().equalsIgnoreCase("--/--/----")){
            dateStr= DateTimeUtil.currentDate(AppsConstant.DATE_FORMATE);
        }else{
            dateStr=dobTextView.getText().toString();
        }
        String date[] =dateStr.split("/");
        /*if(!predefinedDate.getText().toString().equalsIgnoreCase("")){
            String date[] =predefinedDate.getText().toString().split("/");
        }*/
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();
                toDate = calendar;

                calendar.set(year, monthOfYear, dayOfMonth);

                dayTV.setText(String.format("%1$tA", calendar));
                String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);

                dobTextView.setText(formatedData);
                endDate1 = formatedData;

                if (leaveTypeModel == null)
                    return;
                if (!startDate1.equalsIgnoreCase("") && !endDate1.equalsIgnoreCase("")) {
                    if (leaveTypeModel != null) {
                        daysCount();
                    }
                }
                updateLeaveDayType(leaveTypeModel);
                boolean isCompensatory = leaveTypeModel.getLeaveId().equalsIgnoreCase("E008001000") && leaveTypeModel.getProcessStep().equalsIgnoreCase("1");
                if (isCompensatory) {
                    updateCompasatory(isCompensatory);
                }


            }

        }, Integer.parseInt(date[2]), Integer.parseInt(date[1])-1,
                Integer.parseInt(date[0]));
        return datePickerDialog;
    }

    @Override
    public void onDestroyView() {
        rootView.destroyDrawingCache();
        super.onDestroyView();
    }
}
