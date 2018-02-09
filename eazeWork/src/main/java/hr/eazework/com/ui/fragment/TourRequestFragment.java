package hr.eazework.com.ui.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hr.eazework.com.FileUtils;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.SearchOnbehalfActivity;
import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.CorpEmpParamListItem;
import hr.eazework.com.model.CustomFieldsModel;
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetCorpEmpParamResultResponse;
import hr.eazework.com.model.GetDetailsOnEmpChangeRequestModel;
import hr.eazework.com.model.GetDetailsOnEmpChangeResponseModel;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveRejectResponseModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TourCustomListResponse;
import hr.eazework.com.model.TourReasonListModel;
import hr.eazework.com.model.TourReqDetail;
import hr.eazework.com.model.TourRequestModel;
import hr.eazework.com.model.TourResponseModel;
import hr.eazework.com.model.TourSummaryRequestDetail;
import hr.eazework.com.model.TourSummaryResponse;
import hr.eazework.com.model.WFHRejectRequestModel;
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

import static android.app.Activity.RESULT_OK;
import static hr.eazework.com.ui.util.ImageUtil.rotateImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourRequestFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "TourRequestFragment";
    private String screenName = "TourRequestFragment";
    private Button saveDraftBTN, deleteBTN, submitBTN, rejectBTN, approvalBTN;
    private Preferences preferences;
    private TextView empNameTV, empCodeTV, tv_to_day, tv_to_date, tv_from_date, tv_from_day, reasonTV;
    private EditText remarksET, travelFromEt, travelToET, descriptionET, classicToursEt, adventureET, familyPackageET, religiousTravelET, accommodationET, ticketsET;
    private DatePickerDialog datePickerDialog1, datePickerDialog2, photographyDatePickerDialog;
    private String fromButton;
    private LinearLayout errorLinearLayout, travelToLl, travelFromLl, reasonLl;
    private RecyclerView expenseRecyclerView;
    private ImageView plus_create_newIV;
    private static int UPLOAD_DOC_REQUEST = 1;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private Bitmap bitmap = null;
    private String purpose = "";
    public static int TOUR_EMP = 1;
    private RelativeLayout searchLayout;
    private EmployItem employItem;
    private String empId;
    private GetDetailsOnEmpChangeRequestModel empChangeRequestModel;
    private GetDetailsOnEmpChangeResponseModel empChangeResponseModel;
    private TourReasonListModel tourReasonList;
    private String reasonCode = "";
    private GetWFHRequestDetail requestDetail;
    private String reqId;
    private TourCustomListResponse tourCustomListResponse;
    private ArrayList<CustomFieldsModel> customFieldsModel;
    private RadioButton yesRB, noRB, yesTicketRB, noTicketRB;
    private RadioGroup accommodationRG, ticketsRG;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private List<String> extensionList;
    private TourRequestModel tourRequestModel;
    private TourReqDetail tourReqDetail;
    private LinearLayout accommodationLl, ticketsLl;
    private String accommodationYes, accommodationNo, ticketYes, ticketNo, studentYes, studentNo;
    private String startDate = "", endDate = "", photographyDate = "";
    private TourReasonListModel reasonListModel;
    private ArrayList<CustomFieldsModel> customFields;
    private RecyclerView customFieldsRV;
    private CustomFieldsAdapter adapter;
    private TourResponseModel tourResponseModel;
    private LinearLayout remarksLinearLayout, remarksDataLl;
    private RecyclerView remarksRV;
    private EmployeeLeaveModel employeeLeaveModel;
    private TourSummaryResponse tourSummaryResponse;
    private LoginUserModel loginUserModel;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public EmployeeLeaveModel getEmployeeLeaveModel() {
        return employeeLeaveModel;
    }

    public void setEmployeeLeaveModel(EmployeeLeaveModel employeeLeaveModel) {
        this.employeeLeaveModel = employeeLeaveModel;
    }

    private GetEmpWFHResponseItem getEmpWFHResponseItem;

    public GetEmpWFHResponseItem getGetEmpWFHResponseItem() {
        return getEmpWFHResponseItem;
    }

    public void setGetEmpWFHResponseItem(GetEmpWFHResponseItem getEmpWFHResponseItem) {
        this.getEmpWFHResponseItem = getEmpWFHResponseItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tour_request, container, false);
        context = getContext();
        setupScreen();
        return rootView;
    }

    private void setupScreen() {
        reqId = "0";
        remarksDataLl = (LinearLayout) rootView.findViewById(R.id.remarksDataLl);
        remarksDataLl.setVisibility(View.GONE);

        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.tour);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Submit";
                doSubmitOperation();
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
            }
        });

        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        tv_to_day = ((TextView) rootView.findViewById(R.id.tv_to_day));
        tv_to_date = ((TextView) rootView.findViewById(R.id.tv_to_date));
        tv_from_day = ((TextView) rootView.findViewById(R.id.tv_from_day));
        tv_from_date = ((TextView) rootView.findViewById(R.id.tv_from_date));

        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_to_date, tv_to_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);
        datePickerDialog1.updateDate(2018,02,05);
        rootView.findViewById(R.id.ll_from_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });
        rootView.findViewById(R.id.ll_to_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog1.show();

            }
        });
      /*  tv_photography_day = (TextView) rootView.findViewById(R.id.tv_photography_day);
        tv_photography_date = (TextView) rootView.findViewById(R.id.tv_photography_date);
        photographyDatePickerDialog = CalenderUtils.pickDateFromCalender(context, tv_photography_date, tv_photography_day, AppsConstant.DATE_FORMATE);
        ll_photography= (LinearLayout) rootView.findViewById(R.id.ll_photography);
        ll_photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photographyDatePickerDialog.show();
            }
        });*/

        empNameTV = (TextView) rootView.findViewById(R.id.empNameTV);
        empCodeTV = (TextView) rootView.findViewById(R.id.empCodeTV);
        reasonLl = (LinearLayout) rootView.findViewById(R.id.reasonLl);
        reasonLl.setVisibility(View.GONE);
        reasonTV = (TextView) rootView.findViewById(R.id.reasonTV);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);

        travelToLl = (LinearLayout) rootView.findViewById(R.id.travelToLl);
        travelToLl.setVisibility(View.GONE);
        travelFromLl = (LinearLayout) rootView.findViewById(R.id.travelFromLl);
        travelFromLl.setVisibility(View.GONE);
        travelFromEt = (EditText) rootView.findViewById(R.id.travelFromEt);
        travelToET = (EditText) rootView.findViewById(R.id.travelToET);
        customFieldsRV = (RecyclerView) rootView.findViewById(R.id.customFieldsRV);
        customFieldsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        //descriptionET= (EditText) rootView.findViewById(R.id.descriptionET);

       /* classicToursEt= (EditText) rootView.findViewById(R.id.classicToursEt);
        adventureET= (EditText) rootView.findViewById(R.id.adventureET);
        familyPackageET= (EditText) rootView.findViewById(R.id.familyPackageET);
        religiousTravelET= (EditText) rootView.findViewById(R.id.religiousTravelET);*/
        accommodationLl = (LinearLayout) rootView.findViewById(R.id.accommodationLl);
        accommodationLl.setVisibility(View.GONE);
        ticketsLl = (LinearLayout) rootView.findViewById(R.id.ticketsLl);
        ticketsLl.setVisibility(View.GONE);
        accommodationET = (EditText) rootView.findViewById(R.id.accommodationET);
        ticketsET = (EditText) rootView.findViewById(R.id.ticketsET);

        yesRB = (RadioButton) rootView.findViewById(R.id.yesRB);
        yesRB.setChecked(false);
        noRB = (RadioButton) rootView.findViewById(R.id.noRB);
        noRB.setChecked(true);
        accommodationYes = "N";
        accommodationNo = "Y";

        accommodationRG = (RadioGroup) rootView.findViewById(R.id.accommodationRG);
        accommodationRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yesRB) {
                    yesRB.setChecked(true);
                    noRB.setChecked(false);
                    accommodationYes = "Y";
                    accommodationNo = "N";
                    accommodationLl.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.noRB) {
                    yesRB.setChecked(false);
                    noRB.setChecked(true);
                    accommodationYes = "N";
                    accommodationNo = "Y";
                    accommodationLl.setVisibility(View.GONE);

                }
            }
        });

        yesTicketRB = (RadioButton) rootView.findViewById(R.id.yesTicketRB);
        noTicketRB = (RadioButton) rootView.findViewById(R.id.noTicketRB);
        noTicketRB.setChecked(true);
        ticketYes = "N";
        ticketNo = "Y";

        ticketsRG = (RadioGroup) rootView.findViewById(R.id.ticketsRG);
        ticketsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yesTicketRB) {
                    yesTicketRB.setChecked(true);
                    noTicketRB.setChecked(false);
                    ticketYes = "Y";
                    ticketNo = "N";
                    ticketsLl.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.noTicketRB) {
                    yesTicketRB.setChecked(false);
                    noTicketRB.setChecked(true);
                    ticketYes = "N";
                    ticketNo = "Y";
                    ticketsLl.setVisibility(View.GONE);
                }
            }
        });

      /*  yesStudentRB= (RadioButton) rootView.findViewById(R.id.yesTicketRB);
        yesStudentRB.setChecked(true);
        noStudentRB= (RadioButton) rootView.findViewById(R.id.noStudentRB);
        noStudentRB.setChecked(false);
        studentYes="Y";
        studentNo="N";
        studentRG = (RadioGroup) rootView.findViewById(R.id.studentRG);
        studentRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                if (checkedId == R.id.yesStudentRB) {
                    yesStudentRB.setChecked(true);
                    noStudentRB.setChecked(false);
                    studentYes="Y";
                    studentNo="N";
                } else if (checkedId == R.id.noStudentRB) {
                    yesStudentRB.setChecked(false);
                    noStudentRB.setChecked(true);
                    studentYes="N";
                    studentNo="Y";
                }
            }
        });*/

        saveDraftBTN = (Button) rootView.findViewById(R.id.saveDraftBTN);

        saveDraftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Save";
                doSubmitOperation();
            }
        });

        deleteBTN = (Button) rootView.findViewById(R.id.deleteBTN);
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Delete";
                doSubmitOperation();
            }
        });

        submitBTN = (Button) rootView.findViewById(R.id.submitBTN);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Submit";
                doSubmitOperation();
            }
        });

        approvalBTN = (Button) rootView.findViewById(R.id.approvalBTN);
        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Approve";
                doSubmitOperation();
            }
        });

        rejectBTN = (Button) rootView.findViewById(R.id.rejectBTN);
        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectTourRequest();
            }
        });

        employItem = new EmployItem();
        loginUserModel = ModelManager.getInstance().getLoginUserModel();

   /*     employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
        empId = loginUserModel.getUserModel().getEmpId();
        employItem.setName(loginUserModel.getUserModel().getUserName());
        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());

        empNameTV.setText(employItem.getName());
        empCodeTV.setText(employItem.getEmpCode());*/

        searchLayout = (RelativeLayout) rootView.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(getActivity(), SearchOnbehalfActivity.class);
                startActivityForResult(theIntent, TOUR_EMP);
            }
        });

        reasonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (empChangeResponseModel != null && empChangeResponseModel.getGetDetailsOnEmpChangeResult() != null
                        && empChangeResponseModel.getGetDetailsOnEmpChangeResult().getShowReasonYN().equalsIgnoreCase("Y")
                        && empChangeResponseModel.getGetDetailsOnEmpChangeResult().getTourReasonList().size() > 0) {

                    final ArrayList<TourReasonListModel> tourReasonListModel = empChangeResponseModel.getGetDetailsOnEmpChangeResult().getTourReasonList();
                    CustomBuilder reasonDialog = new CustomBuilder(getContext(), "Select Reason", true);
                    reasonDialog.setSingleChoiceItems(tourReasonListModel, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            tourReasonList = (TourReasonListModel) selectedObject;
                            reasonTV.setText(tourReasonList.getReason());
                            reasonCode = tourReasonList.getReasonCode();
                            builder.dismiss();
                            builder.dismiss();
                        }
                    });
                    reasonDialog.show();

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
                                        PermissionUtil.askAllPermissionCamera(TourRequestFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), TourRequestFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", screenName);
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


        if (getEmpWFHResponseItem != null && getEmpWFHResponseItem.getReqID() != null &&
                !getEmpWFHResponseItem.getReqID().equalsIgnoreCase("0")) {
            reqId = getEmpWFHResponseItem.getReqID();
            remarksDataLl.setVisibility(View.VISIBLE);
            sendViewRequestSummaryData();
            getSearchEmployeeData();
        } else if (employeeLeaveModel != null && employeeLeaveModel.getmReqID() != null
                && !employeeLeaveModel.getmReqID().equalsIgnoreCase("0")) {
            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.TOUR_EDIT)) {
                reqId = employeeLeaveModel.getmReqID();
                remarksDataLl.setVisibility(View.VISIBLE);
                sendViewTourRequestSummaryData();
                disabledFieldData();
            }

            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.TOUR_WITHDRAWAL)) {
                reqId = employeeLeaveModel.getmReqID();
                remarksDataLl.setVisibility(View.VISIBLE);
                sendViewTourRequestSummaryData();
                disabledFieldDataForView();
            }

        } else {
            saveDraftBTN.setVisibility(View.VISIBLE);
            uploadFileList = new ArrayList<SupportDocsItemModel>();
            getSearchEmployeeData();
        }

     /*   sendTourRequestCustomFieldList();
        sendAdvanceRequestData();*/

    }

    private void disabledFieldData() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        yesRB.setEnabled(false);
        noRB.setEnabled(false);
        reasonTV.setEnabled(false);
        yesTicketRB.setEnabled(false);
        noTicketRB.setEnabled(false);
    }

    private void disabledFieldDataForView() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        yesRB.setEnabled(false);
        noRB.setEnabled(false);
        reasonTV.setEnabled(false);
        yesTicketRB.setEnabled(false);
        noTicketRB.setEnabled(false);
        travelFromEt.setEnabled(false);
        travelToET.setEnabled(false);
        rootView.findViewById(R.id.ll_from_date).setEnabled(false);
        rootView.findViewById(R.id.ll_to_date).setEnabled(false);
        plus_create_newIV.setVisibility(View.GONE);


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
        if (requestCode == TOUR_EMP) {
            if (data != null) {
                EmployItem item = (EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_TOUR_EMP);
                if (item != null) {
                    String[] empname = item.getName().split("\\(");
                    empNameTV.setText(empname[0]);
                    empId = String.valueOf(item.getEmpID());
                    empCodeTV.setText(item.getEmpCode());
                    employItem = item;
                }

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
                extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
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

    private void setTravelAndReasonData() {
        showHideProgressView(true);
        empChangeRequestModel = new GetDetailsOnEmpChangeRequestModel();
        empChangeRequestModel.setForEmpID(empId);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getTravelAndReasonData(empChangeRequestModel),
                CommunicationConstant.API_GET_DETAILS_ON_EMP_CHANGE, true);

    }

    private void sendTourRequestCustomFieldList() {
        showHideProgressView(true);
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(reqId);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_TOUR_CUSTOM_FIELD_LIST, true);
    }

    public void sendAdvanceRequestData() {
        showHideProgressView(true);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_PAGE_INIT, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        ((MainActivity) getActivity()).showHideProgress(false);
        Log.d("TAG", "response data " + response.isSuccess());
        switch (response.getRequestData().getReqApiId()) {
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
            case CommunicationConstant.API_GET_DETAILS_ON_EMP_CHANGE:
                String responseData = response.getResponseData();
                Log.d("TAG", "Emp Change response : " + responseData);
                empChangeResponseModel = GetDetailsOnEmpChangeResponseModel.create(responseData);
                if (empChangeResponseModel != null && empChangeResponseModel.getGetDetailsOnEmpChangeResult() != null
                        && empChangeResponseModel.getGetDetailsOnEmpChangeResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    if (empChangeResponseModel.getGetDetailsOnEmpChangeResult().getShowTravelYN().equalsIgnoreCase(AppsConstant.YES)) {
                        travelFromLl.setVisibility(View.VISIBLE);
                        travelToLl.setVisibility(View.VISIBLE);
                    }
                    if (empChangeResponseModel.getGetDetailsOnEmpChangeResult().getShowReasonYN().equalsIgnoreCase(AppsConstant.YES)) {
                        reasonLl.setVisibility(View.VISIBLE);
                    }
                    sendTourRequestCustomFieldList();

                } else {
                    new AlertCustomDialog(getActivity(), empChangeResponseModel.getGetDetailsOnEmpChangeResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_GET_TOUR_CUSTOM_FIELD_LIST:
                String resp = response.getResponseData();
                Log.d("TAG", "Custom List response : " + resp);
                tourCustomListResponse = TourCustomListResponse.create(resp);

                if (tourCustomListResponse != null && tourCustomListResponse.getGetTourRequestCustomFieldListResult() != null
                        && tourCustomListResponse.getGetTourRequestCustomFieldListResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && tourCustomListResponse.getGetTourRequestCustomFieldListResult().getCustomFields() != null
                        && tourCustomListResponse.getGetTourRequestCustomFieldListResult().getCustomFields().size() > 0) {
                    customFieldsModel = tourCustomListResponse.getGetTourRequestCustomFieldListResult().getCustomFields();
                    refreshCustomFields(customFieldsModel);
                    sendAdvanceRequestData();
                }
                break;
            case CommunicationConstant.API_GET_CORPEMP_PARAM:
                String responseData1 = response.getResponseData();
                try {
                    ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
                    if (responseData1 != null) {
                        empNameTV.setText("");
                        GetCorpEmpParamResultResponse corpEmpParamResultResponse = GetCorpEmpParamResultResponse.create(responseData1);
                        if (corpEmpParamResultResponse != null && corpEmpParamResultResponse.getGetCorpEmpParamResult() != null &&
                                corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode().equalsIgnoreCase("0")) {

                            if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList() != null &&
                                    corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().size() > 0) {
                               /* if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue() != null) {

                                    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam().equalsIgnoreCase("TourOnBehalfOfYN")
                                            && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue().equalsIgnoreCase("Y")) {
                                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);
                                    }
                                }*/

                                for(CorpEmpParamListItem item:corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList()){
                                    if(item.getParam().equalsIgnoreCase("TourOnBehalfOfYN") && item.getValue().equalsIgnoreCase("Y")){
                                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);

                                    }

                                    if(item.getParam().equalsIgnoreCase("TourSelfInitYN") && item.getValue().equalsIgnoreCase("Y")){
                                        employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
                                        empId = loginUserModel.getUserModel().getEmpId();
                                        employItem.setName(loginUserModel.getUserModel().getUserName());
                                        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());
                                        empNameTV.setText(employItem.getName());

                                    }
                                }
                            }
                            setTravelAndReasonData();
                        } else {

                        }
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e("Leave", e.getMessage(), e);
                }

                break;
            case CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Tour Response : " + str);
                tourSummaryResponse = TourSummaryResponse.create(str);
                if (tourSummaryResponse != null && tourSummaryResponse.getGetTourRequestDetailResult() != null
                        && tourSummaryResponse.getGetTourRequestDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail() != null) {
                    updateUI(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail());
                    refreshRemarksList(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getRemarkList());
                    uploadFileList = tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getAttachments();
                    refreshDocumentList(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getAttachments());
                }

                break;
            case CommunicationConstant.API_SAVE_TOUR_REQUEST:
                String tourResponse = response.getResponseData();
                Log.d("TAG", "wfh response : " + tourResponse);
                tourResponseModel = TourResponseModel.create(tourResponse);
                if (tourResponseModel != null && tourResponseModel.getSaveTourReqResult() != null
                        && tourResponseModel.getSaveTourReqResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, tourResponseModel.getSaveTourReqResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), tourResponseModel.getSaveTourReqResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_REJECT_TOUR_REQUEST:
                String respData = response.getResponseData();
                Log.d("TAG", "reject response : " + respData);
                LeaveRejectResponseModel rejectTourResponse = LeaveRejectResponseModel.create(respData);
                if (rejectTourResponse != null && rejectTourResponse.getRejectTourRequestResult() != null
                        && rejectTourResponse.getRejectTourRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, rejectTourResponse.getRejectTourRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), rejectTourResponse.getRejectTourRequestResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_APPROVE_TOUR_REQUEST:
                String tourResp = response.getResponseData();
                Log.d("TAG", "Tour approval response : " + tourResp);
                TourResponseModel tourResponseModel = TourResponseModel.create(tourResp);
                if (tourResponseModel != null && tourResponseModel.getApproveTourRequestResult() != null
                        && tourResponseModel.getApproveTourRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, tourResponseModel.getApproveTourRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), tourResponseModel.getApproveTourRequestResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    private void getSearchEmployeeData() {
        showHideProgressView(true);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.GetCorpEmpParam(), CommunicationConstant.API_GET_CORPEMP_PARAM,
                true);
    }

    private void doSubmitOperation() {
        tourRequestModel = new TourRequestModel();
        tourReqDetail = new TourReqDetail();
        reasonListModel = new TourReasonListModel();
        customFields = new ArrayList<>();
        startDate = tv_from_date.getText().toString();
        endDate = tv_to_date.getText().toString();
        if (fromButton.equalsIgnoreCase(AppsConstant.SUBMIT)) {
            for (CustomFieldsModel item : customFieldsModel) {
                if (item.getFieldCode().equalsIgnoreCase(AppsConstant.CLASSICAL_TOUR) && item.getMandatoryYN().equalsIgnoreCase(AppsConstant.YES)
                        && classicToursEt.getText().toString().equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.enter_classical_tour));
                    return;

                } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.ADVENTURE) && item.getMandatoryYN().equalsIgnoreCase(AppsConstant.YES)
                        && adventureET.getText().toString().equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.enter_adventure));
                    return;
                } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.FAMILY_PACKAGE) && item.getMandatoryYN().equalsIgnoreCase(AppsConstant.YES)
                        && familyPackageET.getText().toString().equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.enter_family_package));
                    return;
                } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.RELIGIOUS_TRAVEL) && item.getMandatoryYN().equalsIgnoreCase(AppsConstant.YES)
                        && religiousTravelET.getText().toString().equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.enter_religious_travel));
                    return;
                } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.PHOTOGRAPHY) && item.getMandatoryYN().equalsIgnoreCase(AppsConstant.YES)
                        && photographyDate.equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.enter_photography_date));
                    return;
                }
            }

            if (travelFromEt.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_travel_from));
                return;
            }

            if (travelToET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_travel_to));
                return;
            }

            if (empChangeResponseModel != null && empChangeResponseModel.getGetDetailsOnEmpChangeResult() != null &&
                    empChangeResponseModel.getGetDetailsOnEmpChangeResult().getTourReasonList() != null) {
                if (reasonCode.equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, getResources().getString(R.string.select_reason));
                    return;
                }
            }

            /*if (remarksET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_remarks));
                return;
            }*/

            tourReqDetail.setForEmpID(empId);
            tourReqDetail.setReqID(reqId);
            tourReqDetail.setStartDate(startDate);
            tourReqDetail.setEndDate(endDate);
            tourReqDetail.setTravelFrom(travelFromEt.getText().toString());
            tourReqDetail.setTravelTo(travelToET.getText().toString());
            tourReqDetail.setRemarks(remarksET.getText().toString());
            tourReqDetail.setAccomodationYN(accommodationYes);
            tourReqDetail.setAccomodationDet(accommodationET.getText().toString());
            tourReqDetail.setTicketYN(ticketYes);
            tourReqDetail.setTicketDet(ticketsET.getText().toString());
            tourReqDetail.setButton(fromButton);
            reasonListModel.setReasonCode(reasonCode);
            tourReqDetail.setTourReason(reasonListModel);

            if (adapter.dataSet != null) {
                tourReqDetail.setCustomFields(adapter.dataSet);
            }

            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }

            tourReqDetail.setAttachments(uploadFileList);
            tourRequestModel.setTourReqDetail(tourReqDetail);
            MainActivity.isAnimationLoaded = false;
            ((MainActivity) getActivity()).showHideProgress(true);
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.tourRequest(tourRequestModel),
                    CommunicationConstant.API_SAVE_TOUR_REQUEST, true);
        }

        if (fromButton.equalsIgnoreCase(AppsConstant.APPROVE)) {
            tourRequestModel = new TourRequestModel();
            tourReqDetail = new TourReqDetail();
            reasonListModel = new TourReasonListModel();
            customFields = new ArrayList<>();
            startDate = tv_from_date.getText().toString();
            endDate = tv_to_date.getText().toString();
            if (travelFromEt.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_travel_from));
                return;
            }

            if (travelToET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_travel_to));
                return;
            }

            if (tourSummaryResponse != null && tourSummaryResponse.getGetTourRequestDetailResult() != null
                    && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail() != null
                    && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getApprovalLevel() != null) {
                tourReqDetail.setApprovalLevel(Integer.parseInt(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getApprovalLevel()));
            }

            tourReqDetail.setForEmpID(empId);
            tourReqDetail.setReqID(reqId);
            tourReqDetail.setStartDate(startDate);
            tourReqDetail.setEndDate(endDate);
            tourReqDetail.setTravelFrom(travelFromEt.getText().toString());
            tourReqDetail.setTravelTo(travelToET.getText().toString());
            tourReqDetail.setRemarks(remarksET.getText().toString());
            tourReqDetail.setAccomodationYN(accommodationYes);
            tourReqDetail.setAccomodationDet(accommodationET.getText().toString());
            tourReqDetail.setTicketYN(ticketYes);
            tourReqDetail.setTicketDet(ticketsET.getText().toString());
            reasonListModel.setReasonCode(reasonCode);
            tourReqDetail.setTourReason(reasonListModel);

            if (adapter.dataSet != null) {
                tourReqDetail.setCustomFields(adapter.dataSet);
            }

            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }

            tourReqDetail.setAttachments(uploadFileList);
            tourRequestModel.setTourReqDetail(tourReqDetail);
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.tourRequest(tourRequestModel),
                    CommunicationConstant.API_APPROVE_TOUR_REQUEST, true);

        }


        if (fromButton.equalsIgnoreCase(AppsConstant.SAVE_AS_DRAFT) || fromButton.equalsIgnoreCase(AppsConstant.DELETE)) {
            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                if (tourSummaryResponse != null && tourSummaryResponse.getGetTourRequestDetailResult() != null
                        && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail() != null
                        && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getApprovalLevel() != null) {
                    tourReqDetail.setApprovalLevel(Integer.parseInt(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getApprovalLevel()));
                }
            }
            tourReqDetail.setForEmpID(empId);
            tourReqDetail.setReqID(reqId);
            tourReqDetail.setStartDate(startDate);
            tourReqDetail.setEndDate(endDate);
            tourReqDetail.setTravelFrom(travelFromEt.getText().toString());
            tourReqDetail.setTravelTo(travelToET.getText().toString());
            tourReqDetail.setRemarks(remarksET.getText().toString());
            tourReqDetail.setAccomodationYN(accommodationYes);
            tourReqDetail.setAccomodationDet(accommodationET.getText().toString());
            tourReqDetail.setTicketYN(ticketYes);
            tourReqDetail.setTicketDet(ticketsET.getText().toString());
            tourReqDetail.setButton(fromButton);
            reasonListModel.setReasonCode(reasonCode);
            tourReqDetail.setTourReason(reasonListModel);

            if (adapter.dataSet != null) {
                tourReqDetail.setCustomFields(adapter.dataSet);
            }

            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }

            tourReqDetail.setAttachments(uploadFileList);
            tourRequestModel.setTourReqDetail(tourReqDetail);
            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.tourRequest(tourRequestModel),
                        CommunicationConstant.API_APPROVE_TOUR_REQUEST, true);
            } else {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.tourRequest(tourRequestModel),
                        CommunicationConstant.API_SAVE_TOUR_REQUEST, true);
            }
        }

    }

    private void refreshCustomFields(ArrayList<CustomFieldsModel> customFieldsModel) {
        if (customFieldsModel != null && customFieldsModel.size() > 0) {
            adapter = new CustomFieldsAdapter(customFieldsModel);
            customFieldsRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    public class CustomFieldsAdapter extends
            RecyclerView.Adapter<CustomFieldsAdapter.MyViewHolder> {
        public ArrayList<CustomFieldsModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView labelTV, nameTV, remarksReasonTV, remarksStatusTV, tv_photography_day, tv_photography_date;
            public LinearLayout photographyLayout, radiotextLayout, edittextLayout, ll_photography;
            private TextView photographyTV, radioTV, editTextTV;
            private EditText editTextLayoutEt;
            private RadioGroup studentRG;
            private RadioButton yesStudentRB, noStudentRB;

            public MyViewHolder(View v) {
                super(v);
                editTextLayoutEt = (EditText) v.findViewById(R.id.editTextLayoutEt);
                editTextTV = (TextView) v.findViewById(R.id.editTextTV);
                photographyTV = (TextView) v.findViewById(R.id.photographyTV);

                radioTV = (TextView) v.findViewById(R.id.radioTV);
                photographyLayout = (LinearLayout) v.findViewById(R.id.photographyLayout);
                radiotextLayout = (LinearLayout) v.findViewById(R.id.radiotextLayout);
                edittextLayout = (LinearLayout) v.findViewById(R.id.edittextLayout);

                yesStudentRB = (RadioButton) v.findViewById(R.id.yesTicketRB);
                noStudentRB = (RadioButton) v.findViewById(R.id.noStudentRB);

                studentRG = (RadioGroup) v.findViewById(R.id.studentRG);

                tv_photography_day = (TextView) v.findViewById(R.id.tv_photography_day);
                tv_photography_date = (TextView) v.findViewById(R.id.tv_photography_date);
                ll_photography = (LinearLayout) v.findViewById(R.id.ll_photography);


            }
        }

        public void addAll(List<CustomFieldsModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public CustomFieldsAdapter(List<CustomFieldsModel> data) {
            this.dataSet = (ArrayList<CustomFieldsModel>) data;

        }

        @Override
        public CustomFieldsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_fields_tour, parent, false);
            CustomFieldsAdapter.MyViewHolder myViewHolder = new CustomFieldsAdapter.MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final CustomFieldsAdapter.MyViewHolder holder, final int listPosition) {

            final CustomFieldsModel item = dataSet.get(listPosition);
            holder.edittextLayout.setVisibility(View.GONE);
            holder.radiotextLayout.setVisibility(View.GONE);
            holder.photographyLayout.setVisibility(View.GONE);

            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                RadioButton radio = (RadioButton) holder.studentRG.findViewById(R.id.yesStudentRB);
                radio.setEnabled(false);

                RadioButton radio1 = (RadioButton) holder.studentRG.findViewById(R.id.noStudentRB);
                radio1.setEnabled(false);

                holder.editTextLayoutEt.setEnabled(false);
                holder.ll_photography.setEnabled(false);
            }
            if (item.getFieldCode().equalsIgnoreCase(AppsConstant.STUDENT_SPECIAL)) {
                holder.radiotextLayout.setVisibility(View.VISIBLE);
                holder.radioTV.setText(item.getFieldLabel());
                if (reqId.equalsIgnoreCase("0")) {
                    studentYes = "Y";
                    item.setFieldValue(studentYes);
                    dataSet.set(listPosition, item);
                }
                if (item.getFieldValue().equalsIgnoreCase(AppsConstant.YES)) {
                    RadioButton radio = (RadioButton) holder.studentRG.findViewById(R.id.yesStudentRB);
                    radio.setChecked(true);
                    studentYes = "Y";
                    item.setFieldValue(studentYes);
                    dataSet.set(listPosition, item);
                    Log.d("fsgfdh", "radio : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());

                } else if (item.getFieldValue().equalsIgnoreCase(AppsConstant.NO)) {
                    RadioButton radio = (RadioButton) holder.studentRG.findViewById(R.id.noStudentRB);
                    radio.setChecked(true);
                    studentYes = "N";
                    item.setFieldValue(studentYes);
                    dataSet.set(listPosition, item);
                    Log.d("fsgfdh", "radio : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());

                }
                holder.studentRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.yesStudentRB) {
                            studentYes = "Y";
                            item.setFieldValue(studentYes);
                            dataSet.set(listPosition, item);
                        } else if (checkedId == R.id.noStudentRB) {
                            studentYes = "N";
                            item.setFieldValue(studentYes);
                            dataSet.set(listPosition, item);
                        }

                        Log.d("fsgfdh", "radio : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());

                    }
                });


            } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.PHOTOGRAPHY)) {
                holder.photographyLayout.setVisibility(View.VISIBLE);
                holder.photographyTV.setText(item.getFieldLabel());
                if (item.getFieldValue() != null && !item.getFieldValue().equalsIgnoreCase("")) {
                    holder.tv_photography_date.setText(item.getFieldValue());
                    photographyDate = item.getFieldValue();
                    item.setFieldValue(photographyDate);
                    dataSet.set(listPosition, item);
                }

                photographyDatePickerDialog = pickDateFromCalender(dataSet, listPosition, item, context, holder.tv_photography_date, holder.tv_photography_day, AppsConstant.DATE_FORMATE);
                holder.ll_photography.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photographyDatePickerDialog.show();

                    }
                });


            } else if (item.getFieldCode().equalsIgnoreCase(AppsConstant.FAMILY_PACKAGE)) {
                holder.edittextLayout.setVisibility(View.VISIBLE);
                holder.editTextTV.setText(item.getFieldLabel());
                holder.editTextLayoutEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                holder.editTextLayoutEt.setText(item.getFieldValue());
                holder.editTextLayoutEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String value = s.toString();
                        item.setFieldValue(value);
                        dataSet.set(listPosition, item);
                        Log.d("fsgfdh", "Family : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            } else {
                holder.edittextLayout.setVisibility(View.VISIBLE);
                holder.editTextTV.setText(item.getFieldLabel());
                holder.editTextLayoutEt.setText(item.getFieldValue());
                holder.editTextLayoutEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String value = s.toString();
                        item.setFieldValue(value);
                        dataSet.set(listPosition, item);
                        Log.d("fsgfdh", "Tourname : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }


        }

        public DatePickerDialog pickDateFromCalender(ArrayList<CustomFieldsModel> list, final int listPosition, final CustomFieldsModel item, Context mContext, final TextView dobTextView, final TextView dayTV, String dateFormat) {
            Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    Calendar calendar = Calendar.getInstance();

                    calendar.set(year, monthOfYear, dayOfMonth);

                    dayTV.setText(String.format("%1$tA", calendar));
                    String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
                    photographyDate = formatedData;
                    dobTextView.setText(photographyDate);
                    item.setFieldValue(photographyDate);
                    dataSet.set(listPosition, item);
                    Log.d("fsgfdh", "date : " + dataSet.get(listPosition).getFieldLabel() + " : " + dataSet.get(listPosition).getFieldValue());

                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH));
            return datePickerDialog;
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

    private void sendViewRequestSummaryData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL, true);
    }

    private void sendViewTourRequestSummaryData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(employeeLeaveModel.getmReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL, true);
    }


    private void updateUI(TourSummaryRequestDetail item) {
        empId = item.getForEmpID();
        empNameTV.setText(item.getForEmpName());
        tv_from_date.setText(item.getStartDate());
        startDate = item.getStartDate();
        tv_to_date.setText(item.getEndDate());
        endDate = item.getEndDate();
        if (item.getTravelFrom() != null && item.getTravelTo() != null) {
            travelFromLl.setVisibility(View.VISIBLE);
            travelToLl.setVisibility(View.VISIBLE);
            travelFromEt.setText(item.getTravelFrom());
            travelToET.setText(item.getTravelTo());
        }

        if (item.getTourReason().getReasonCode() != null && !item.getTourReason().getReasonCode().equalsIgnoreCase("")) {
            reasonLl.setVisibility(View.VISIBLE);
            reasonTV.setText(item.getTourReason().getReason());
            reasonCode = item.getTourReason().getReasonCode();
        }

        if (item.getAccomodationYN().equalsIgnoreCase(AppsConstant.YES)) {
            yesRB.setChecked(true);
            noRB.setChecked(false);
            accommodationYes = item.getAccomodationYN();
        } else {
            noRB.setChecked(true);
            yesRB.setChecked(false);
            accommodationYes = item.getAccomodationYN();
        }
        accommodationET.setText(item.getAccomodationDet());

        if (item.getTicketYN().equalsIgnoreCase(AppsConstant.YES)) {
            yesTicketRB.setChecked(true);
            noTicketRB.setChecked(false);
            ticketYes = item.getTicketYN();
        } else {
            noTicketRB.setChecked(true);
            yesTicketRB.setChecked(false);
            ticketYes = item.getTicketYN();
        }
        ticketsET.setText(item.getTicketDet());
        if (item.getCustomFields() != null) {
            customFieldsModel = item.getCustomFields();
            refreshCustomFields(customFieldsModel);
        }
        remarksET.setText(item.getRemark());

        setupButtons(item);
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

    private void setupButtons(TourSummaryRequestDetail item) {
        if (item.getButtons() != null) {
            for (String button : item.getButtons()) {
                if (button.equalsIgnoreCase(AppsConstant.DELETE)) {
                    deleteBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.SUBMIT)) {
                    submitBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.SAVE_DRAFT)) {
                    saveDraftBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.REJECT)) {
                    rejectBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.APPROVE)) {
                    approvalBTN.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void rejectTourRequest() {
        if (remarksET.getText().toString().equalsIgnoreCase("")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
            return;
        }
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(reqId);
        rejectRequestModel.setComments(remarksET.getText().toString());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_TOUR_REQUEST, true);

    }
}
