package hr.eazework.com;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.AttandanceCalenderStatusItem;
import hr.eazework.com.model.AttendanceItem;
import hr.eazework.com.model.AttendanceRejectItem;
import hr.eazework.com.model.AttendanceRejectRequestModel;
import hr.eazework.com.model.AttendanceRejectResponseModel;
import hr.eazework.com.model.AttendanceReqDetail;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetTimeModificationRequestDetail;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TimeModificationItem;
import hr.eazework.com.model.TimeModificationRequestModel;
import hr.eazework.com.model.TimeModificationResponseModel;
import hr.eazework.com.model.TimeModificationSummaryResponseModel;
import hr.eazework.com.ui.adapter.DocumentUploadAdapter;
import hr.eazework.com.ui.adapter.RemarksAdapter;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

import static hr.eazework.com.ui.util.ImageUtil.rotateImage;

/**
 * Created by Dell3 on 12-12-2017.
 */

public class TimeModificationActivity extends BaseActivity {

    public static int TIMEMODIFICATIONREQUESTCODE=11;
    private String screenName="TimeModificationActivity";
    private Context context;
    private Preferences preferences;
    private TextView empNameTV,tv_header_text,dateTV,statusTV,tv_in_day,tv_in_date,tv_in_time,tv_out_day,tv_out_date,tv_out_time,remarksET;
    private LinearLayout rl_edit_team_member,statusLinearLayout,requestedOutTimeLl,requestedInTimeLl,remarksLinearLayout,remarksLl;
    private String currentDate,inDate="",outDate="",inTime="",outTime="",empId,reqInTime,reqOutTime,attendId;
    private LinearLayout inDateLl,outDateLl,inTimeLl,outTimeLl;
    private RelativeLayout backLayout,rightRL;
    private List<String> extensionList;
    private DatePickerDialog datePickerDialog1,datePickerDialog2;
    private TimePickerDialog timePickerDialog1,timePickerDialog2;
    private TimeModificationResponseModel timeModificationResponseModel;
    public static AttandanceCalenderStatusItem attandanceCalenderStatusItem;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private static int UPLOAD_DOC_REQUEST = 1;
    private Bitmap bitmap = null;
    private String purpose = "";
    private LinearLayout errorLinearLayout;
    private RecyclerView expenseRecyclerView;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private ImageView plus_create_newIV;
    public static AttendanceItem employeeLeaveModel;
    private RecyclerView remarksRV;
    private String fromButton="";
    private Button rejectBTN,approvalBTN;
    private  TimeModificationSummaryResponseModel summaryResponseModel;
    private AttendanceReqDetail reqDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.attendance_details_layout);
        preferences = new Preferences(context);
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(context, preferences);
        tv_header_text=(TextView)findViewById(R.id.tv_header_text);
        tv_header_text.setText(context.getResources().getString(R.string.Time_Modification_Request));
        tv_header_text.setTextColor(textColor);
        rl_edit_team_member=(LinearLayout) findViewById(R.id.rl_edit_team_member);
        rl_edit_team_member.setBackgroundColor(bgColor);
        remarksLinearLayout = (LinearLayout)findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView)findViewById(R.id.remarksRV);
        backLayout=(RelativeLayout)findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rightRL= (RelativeLayout) findViewById(R.id.rightRL);
        rightRL.setVisibility(View.VISIBLE);
        rightRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton="Submit";
                doSubmitOperation();
            }
        });

        empNameTV = (TextView)findViewById(R.id.empNameTV);
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        empId = loginUserModel.getUserModel().getEmpId();
        empNameTV.setText(loginUserModel.getUserModel().getUserName());

        remarksLl= (LinearLayout) findViewById(R.id.remarksLl);
        remarksLl.setVisibility(View.GONE);
        dateTV= (TextView) findViewById(R.id.dateTV);
        statusTV= (TextView) findViewById(R.id.statusTV);
        tv_in_day= (TextView) findViewById(R.id.tv_in_day);
        tv_in_date= (TextView) findViewById(R.id.tv_in_date);
        tv_in_time= (TextView) findViewById(R.id.tv_in_time);
        tv_out_day= (TextView) findViewById(R.id.tv_out_day);
        tv_out_date= (TextView) findViewById(R.id.tv_out_date);
        tv_out_time= (TextView) findViewById(R.id.tv_out_time);
        remarksET= (TextView) findViewById(R.id.remarksET);
        inDateLl= (LinearLayout) findViewById(R.id.inDateLl);
        outDateLl= (LinearLayout) findViewById(R.id.outDateLl);
        inTimeLl= (LinearLayout) findViewById(R.id.inTimeLl);
        outTimeLl= (LinearLayout) findViewById(R.id.outTimeLl);

        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_in_date, tv_in_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_out_date, tv_out_day, AppsConstant.DATE_FORMATE);

        inDateLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog1.show();
            }
        });
        outDateLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });

        timePickerDialog1 = Utility.setTime(context, tv_in_time);
        timePickerDialog2 = Utility.setTime(context, tv_out_time);
        inTimeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog1.show();
            }
        });
        outTimeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog2.show();
            }
        });

        if(attandanceCalenderStatusItem!=null){
            updateUI();
        }
        if(employeeLeaveModel != null && employeeLeaveModel.getReqID() != null
                && !employeeLeaveModel.getReqID().equalsIgnoreCase("0")){
            sendViewRequestSummaryData();
            rightRL.setVisibility(View.GONE);
        }
        rejectBTN = (Button)findViewById(R.id.rejectBTN);
        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectAttendanceRequest();
            }
        });

        approvalBTN = (Button)findViewById(R.id.approvalBTN);

        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton="Approve";
                doSubmitOperation();
            }
        });

    }


    private void doSubmitOperation(){
        inDate=tv_in_date.getText().toString();
        outDate=tv_out_date.getText().toString();
        inTime=tv_in_time.getText().toString();
        outTime=tv_out_time.getText().toString();
        if (inDate.equalsIgnoreCase("") || inDate.equalsIgnoreCase("--/--/----")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_in_date));
            return;
        } else if (inTime.equalsIgnoreCase("") || inTime.equalsIgnoreCase("--:-- AM")) {
            new AlertCustomDialog(context, getResources().getString(R.string.enter_in_time));
            return;
        } else if (outDate.equalsIgnoreCase("") || outDate.equalsIgnoreCase("--/--/----")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_out_date));
            return;
        } else if (outTime.equalsIgnoreCase("") || outTime.equalsIgnoreCase("--:-- AM")) {
            new AlertCustomDialog(context, getResources().getString(R.string.enter_out_time));
            return;
        }else if (remarksET.getText().toString().equalsIgnoreCase("")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
            return;
        }else {
            if(fromButton.equalsIgnoreCase(AppsConstant.APPROVE)){
                reqInTime=inDate + " " + inTime;
                reqOutTime=outDate + " " + outTime;
                TimeModificationItem timeModificationItem=new TimeModificationItem();
                timeModificationItem.setForEmpID(empId);
                timeModificationItem.setAttendID(attendId);
                timeModificationItem.setReqTime(reqInTime);
                timeModificationItem.setReqOutTime(reqOutTime);
                timeModificationItem.setReqId(employeeLeaveModel.getReqID());
                timeModificationItem.setApprovalLevel(reqDetail.getApprovalLevel());
                timeModificationItem.setStatus(reqDetail.getStatus());
                timeModificationItem.setRemark(remarksET.getText().toString());

                TimeModificationRequestModel timeModificationRequestModel =new TimeModificationRequestModel();
                timeModificationRequestModel.setRequest(timeModificationItem);
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.timeModificationRequest(timeModificationRequestModel),
                        CommunicationConstant.API_APPROVE_ATTENDANCE_REQUEST, true);
            }

            if(fromButton.equalsIgnoreCase("Submit")){
                reqInTime=inDate + " " + inTime;
                reqOutTime=outDate + " " + outTime;
                TimeModificationItem timeModificationItem=new TimeModificationItem();
                timeModificationItem.setForEmpID(empId);
                timeModificationItem.setAttendID(attendId);
                timeModificationItem.setBreakID("0");
                timeModificationItem.setReqTime(reqInTime);
                timeModificationItem.setReqOutTime(reqOutTime);
                timeModificationItem.setRemark(remarksET.getText().toString());

                TimeModificationRequestModel timeModificationRequestModel =new TimeModificationRequestModel();
                timeModificationRequestModel.setRequest(timeModificationItem);
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.timeModificationRequest(timeModificationRequestModel),
                        CommunicationConstant.API_TIME_MODIFICATION_REQUEST, true);
            }
        }

    }
    private void sendViewRequestSummaryData() {
        GetTimeModificationRequestDetail getTimeModificationRequestDetail = new GetTimeModificationRequestDetail();
        getTimeModificationRequestDetail.setReqID(employeeLeaveModel.getReqID());
        getTimeModificationRequestDetail.setAction(AppsConstant.EDIT_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.timeModificationSummaryDetails(getTimeModificationRequestDetail),
                CommunicationConstant.API_GET_ATTENDANCE_DETAIL, true);
    }
    @Override
    public void validateResponse(ResponseData response) {
        Log.d("TAG", "response data " + response.isSuccess());
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_TIME_MODIFICATION_REQUEST:
                String responseData = response.getResponseData();
                Log.d("TAG", "time modification response : " + responseData);
                timeModificationResponseModel = TimeModificationResponseModel.create(responseData);
                if (timeModificationResponseModel != null && timeModificationResponseModel.getTimeModificationResult() != null
                        && timeModificationResponseModel.getTimeModificationResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                  //  CustomDialog.alertOkWithFinish(context,timeModificationResponseModel.getTimeModificationResult().getErrorMessage());
                    CustomDialog.alertOkWithFinishActivity(context, timeModificationResponseModel.getTimeModificationResult().getErrorMessage(), TimeModificationActivity.this, true);

                    //CustomDialog.alertOkWithFinishFragment(context, timeModificationResponseModel.getTimeModificationResult().getErrorMessage(), null, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(context, timeModificationResponseModel.getTimeModificationResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_GET_ATTENDANCE_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Time Modification Summary Response : " + str);
                summaryResponseModel = TimeModificationSummaryResponseModel.create(str);
                if (summaryResponseModel != null && summaryResponseModel.getGetAttendanceReqDetailResult() != null
                        && summaryResponseModel.getGetAttendanceReqDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail() != null) {
                    reqDetail=summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail();
                    updateUIForApproval(summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail());
                    refreshRemarksList(summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail().getRemarkList());
                }

                break;
            case CommunicationConstant.API_REJECT_ATTENDANCE_REQUEST:
                String respDataAttendance = response.getResponseData();
                Log.d("TAG", "reject attendance response : " + respDataAttendance);
                AttendanceRejectResponseModel attendanceRejectResponseModel = AttendanceRejectResponseModel.create(respDataAttendance);
                if (attendanceRejectResponseModel != null && attendanceRejectResponseModel.getRejectRequestResult() != null
                        && attendanceRejectResponseModel.getRejectRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    //CustomDialog.alertOkWithFinishFragment(context, attendanceRejectResponseModel.getRejectRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                    CustomDialog.alertOkWithFinish(context, attendanceRejectResponseModel.getRejectRequestResult().getErrorMessage());

                } else {
                    new AlertCustomDialog(context, attendanceRejectResponseModel.getRejectRequestResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_APPROVE_ATTENDANCE_REQUEST:
                String respDataAttend = response.getResponseData();
                Log.d("TAG", "approve attendance response : " + respDataAttend);
                AttendanceRejectResponseModel attendanceApproveResponseModel = AttendanceRejectResponseModel.create(respDataAttend);
                if (attendanceApproveResponseModel != null && attendanceApproveResponseModel.getApproveRequestResult() != null
                        && attendanceApproveResponseModel.getApproveRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    //CustomDialog.alertOkWithFinishFragment(context, attendanceApproveResponseModel.getApproveRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                    CustomDialog.alertOkWithFinishActivity(context, attendanceApproveResponseModel.getApproveRequestResult().getErrorMessage(),TimeModificationActivity.this,true);

                } else {
                    new AlertCustomDialog(context, attendanceApproveResponseModel.getApproveRequestResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    private void updateUI(){
            attendId=attandanceCalenderStatusItem.getAttendID();
            tv_in_date.setText(attandanceCalenderStatusItem.getInDate());
            inDate=attandanceCalenderStatusItem.getInDate();
            tv_out_date.setText(attandanceCalenderStatusItem.getOutDate());
            outDate=attandanceCalenderStatusItem.getOutDate();
            tv_in_time.setText(attandanceCalenderStatusItem.getTimeIn());
            inTime=attandanceCalenderStatusItem.getTimeIn();
            tv_out_time.setText(attandanceCalenderStatusItem.getTimeOut());
            outTime=attandanceCalenderStatusItem.getTimeOut();
            dateTV.setText(attandanceCalenderStatusItem.getMarkDate());
            statusTV.setText(attandanceCalenderStatusItem.getStatusDesc());
        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_in_date, tv_in_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_out_date, tv_out_day, AppsConstant.DATE_FORMATE);
    }

    private void updateUIForApproval(AttendanceReqDetail item){
        attendId=item.getAttendID();
        empId=item.getEmpID()+"";
        dateTV.setText(item.getMarkDate());
        statusTV.setText(item.getStatusDesc());
        empNameTV.setText(item.getName());
        String[] dateTime = item.getReqTime().split(" ");
        inDate=dateTime[0];
        inTime=dateTime[1]+ " " + dateTime[2];
        tv_in_date.setText(inDate);
        tv_in_time.setText(inTime);

        String[] outTimeDate= item.getReqOutTime().split(" ");
        outDate=outTimeDate[0];
        outTime=outTimeDate[1]+ " " + outTimeDate[2];
        tv_out_date.setText(outDate);
        tv_out_time.setText(outTime);
        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_in_date, tv_in_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_out_date, tv_out_day, AppsConstant.DATE_FORMATE);
        remarksET.setText(item.getRemark());
        setupButtons(item);
    }

    private void refreshRemarksList(ArrayList<RemarkListItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(context));
            remarksRV.setVisibility(View.VISIBLE);
            RemarksAdapter adapter = new RemarksAdapter(remarksItems, context, screenName, remarksLinearLayout);
            remarksRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
        }
    }

    private void rejectAttendanceRequest() {
        if (summaryResponseModel != null && summaryResponseModel.getGetAttendanceReqDetailResult() != null
                && summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail() != null) {
            if (remarksET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
                return;
            }
            AttendanceRejectRequestModel rejectRequestModel = new AttendanceRejectRequestModel();
            AttendanceRejectItem attendanceRejectItem = new AttendanceRejectItem();
            attendanceRejectItem.setReqID(reqDetail.getReqID());
            attendanceRejectItem.setRemark(remarksET.getText().toString());
            attendanceRejectItem.setApprovalLevel(reqDetail.getApprovalLevel());
            attendanceRejectItem.setStatus(reqDetail.getStatus());
            rejectRequestModel.setRequest(attendanceRejectItem);
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.rejectAttendanceRequest(rejectRequestModel),
                    CommunicationConstant.API_REJECT_ATTENDANCE_REQUEST, true);
        }
    }

    private void setupButtons(AttendanceReqDetail item) {
        if (item.getButtons() != null) {
            for (String button : item.getButtons()) {
                if (button.equalsIgnoreCase(AppsConstant.REJECT)) {
                    rejectBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.APPROVE)) {
                    approvalBTN.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
