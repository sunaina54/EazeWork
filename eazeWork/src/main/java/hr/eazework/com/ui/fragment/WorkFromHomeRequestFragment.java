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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import android.widget.Toast;

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
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetCorpEmpParamResultResponse;
import hr.eazework.com.model.GetDetailsOnInputChangeRequestModel;
import hr.eazework.com.model.GetDetailsOnInputChangeResponseModel;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetEmpWFHResponseModel;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveRejectResponseModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PartialDayDataModel;
import hr.eazework.com.model.PartialDayModel;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.WFHRejectRequestModel;
import hr.eazework.com.model.WFHRequestDetailItem;
import hr.eazework.com.model.WFHRequestDetailModel;
import hr.eazework.com.model.WFHRequestModel;
import hr.eazework.com.model.WFHResponseModel;
import hr.eazework.com.model.WFHSummaryResponse;
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

public class WorkFromHomeRequestFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "WorkFromHomeRequestFragment";
    private String screenName = "WorkFromHomeRequestFragment";
    private Button saveDraftBTN, deleteBTN, submitBTN, rejectBTN, approvalBTN;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private List<String> extensionList;
    private Preferences preferences;
    private TextView empNameTV, empCodeTV, tv_to_day, tv_to_date, tv_from_date, tv_from_day;
    private EditText remarksET;
    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private String fromButton;
    private EmployItem employItem;
    private RelativeLayout searchLayout;
    public static int WFH_EMP = 1;
    private String empId;
    private LinearLayout errorLinearLayout;
    private RecyclerView expenseRecyclerView;
    private ImageView plus_create_newIV;
    private static int UPLOAD_DOC_REQUEST = 1;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private Bitmap bitmap = null;
    private String purpose = "";
    private RadioGroup leaveRG;
    private RadioButton rb_half_day, rb_full_day, wfhRadioButton;
    private RelativeLayout wfhTimeTypeRL;
    private String fromDate = "", toDate = "";
    private String reqId;
    private String dayP50, dayFull;
    private GetDetailsOnInputChangeRequestModel inputChangeRequestModel;
    private GetDetailsOnInputChangeResponseModel inputChangeResponseModel;
    private WFHRequestDetailModel wfhRequestDetailModel;
    private RadioButton[] WFHRadioButtonArray = new RadioButton[2];
    private WFHResponseModel wfhResponseModel;
    private PartialDayDataModel partialDayDataModel;
    private GetWFHRequestDetail requestDetail;
    private WFHSummaryResponse wfhSummaryResponse;
    private LinearLayout remarksLinearLayout, remarksDataLl;
    private RecyclerView remarksRV;
    private LoginUserModel loginUserModel;

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
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_work_from_home_request, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        setupScreen();
        return rootView;
    }

    private void setupScreen() {
        reqId = "0";
        remarksDataLl = (LinearLayout) rootView.findViewById(R.id.remarksDataLl);
        remarksDataLl.setVisibility(View.GONE);


        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.work_from_home);
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

        tv_to_day = ((TextView) rootView.findViewById(R.id.tv_to_day));
        tv_to_date = ((TextView) rootView.findViewById(R.id.tv_to_date));
        tv_from_day = ((TextView) rootView.findViewById(R.id.tv_from_day));
        tv_from_date = ((TextView) rootView.findViewById(R.id.tv_from_date));
        datePickerDialog1 = pickDateFromCalenderFromDate(context, tv_to_date, tv_to_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = pickDateFromCalenderToDate(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);

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
        wfhTimeTypeRL = (RelativeLayout) rootView.findViewById(R.id.wfhTimeTypeRL);
        //wfhTimeTypeRL.setVisibility(View.GONE);
        dayFull = "N";
        dayP50 = "N";

        rb_half_day = (RadioButton) rootView.findViewById(R.id.rb_half_day);
        rb_full_day = (RadioButton) rootView.findViewById(R.id.rb_full_day);
        rb_half_day.setVisibility(View.GONE);
        rb_full_day.setVisibility(View.GONE);

        leaveRG = (RadioGroup) rootView.findViewById(R.id.leaveRG);

        leaveRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_full_day) {
                    rb_full_day.setChecked(true);
                    rb_half_day.setChecked(false);
                    dayFull = "Y";
                    dayP50 = "N";
                } else if (checkedId == R.id.rb_half_day) {
                    rb_full_day.setChecked(false);
                    rb_half_day.setChecked(true);
                    dayFull = "N";
                    dayP50 = "Y";
                }
            }
        });

        empNameTV = (TextView) rootView.findViewById(R.id.empNameWFHTV);
        empCodeTV = (TextView) rootView.findViewById(R.id.empCodeTV);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);

        saveDraftBTN = (Button) rootView.findViewById(R.id.saveDraftBTN);
        saveDraftBTN.setVisibility(View.VISIBLE);
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

                rejectWFHRequest();
            }
        });


        employItem = new EmployItem();
        loginUserModel = ModelManager.getInstance().getLoginUserModel();


       // empNameTV.setText(employItem.getName());
        //empCodeTV.setText(employItem.getEmpCode());

        searchLayout = (RelativeLayout) rootView.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(getActivity(), SearchOnbehalfActivity.class);
                startActivityForResult(theIntent, WFH_EMP);
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
                                        PermissionUtil.askAllPermissionCamera(WorkFromHomeRequestFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), WorkFromHomeRequestFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", screenName);
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
                !getEmpWFHResponseItem.getReqID().equalsIgnoreCase("0")) {   //Edit
            reqId = getEmpWFHResponseItem.getReqID();
            remarksDataLl.setVisibility(View.VISIBLE);
            sendViewRequestSummaryData();
            getSearchEmployeeData();
        } else if (employeeLeaveModel != null && employeeLeaveModel.getmReqID() != null
                && !employeeLeaveModel.getmReqID().equalsIgnoreCase("0")) {   //Approval edit
            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.WFH_EDIT)) {
                reqId = employeeLeaveModel.getmReqID();
                remarksDataLl.setVisibility(View.VISIBLE);
                sendViewWFHRequestSummaryData();
                disabledFieldData();
            }

            if (employeeLeaveModel.getmReqType() != null && employeeLeaveModel.getmReqType().equalsIgnoreCase(AppsConstant.WFH_WITHDRAWAL)) {
                reqId = employeeLeaveModel.getmReqID();
                remarksDataLl.setVisibility(View.VISIBLE);
                sendViewWFHRequestSummaryData();
                disabledFieldDataForView();
            }


        } else {
            uploadFileList = new ArrayList<SupportDocsItemModel>();
            getSearchEmployeeData();
        }

        sendAdvanceRequestData();
    }

    private void disabledFieldData() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
    }

    private void disabledFieldDataForView() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_from_date).setEnabled(false);
        rootView.findViewById(R.id.ll_to_date).setEnabled(false);
        plus_create_newIV.setVisibility(View.GONE);
        rb_half_day.setEnabled(false);
        rb_full_day.setEnabled(false);
    }

    public void sendAdvanceRequestData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_PAGE_INIT, true);
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
        if (requestCode == WFH_EMP) {
            if (data != null) {
                EmployItem item = (EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_WFH_EMP);
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

    public DatePickerDialog pickDateFromCalenderToDate(Context mContext, final TextView dobTextView, final TextView dayTV, String dateFormat) {
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
                dobTextView.setText(formatedData);
                fromDate = tv_from_date.getText().toString();
                if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
                    //service call
                    setupWFHType();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }

    public DatePickerDialog pickDateFromCalenderFromDate(Context mContext, final TextView dobTextView, final TextView dayTV, String dateFormat) {
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
                dobTextView.setText(formatedData);
                toDate = tv_to_date.getText().toString();
                if (!toDate.equalsIgnoreCase("") && !fromDate.equalsIgnoreCase("")) {
                    setupWFHType();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }

    private void setupWFHType() {
        inputChangeRequestModel = new GetDetailsOnInputChangeRequestModel();
        inputChangeRequestModel.setForEmpID(empId);
        inputChangeRequestModel.setStartDate(fromDate);
        inputChangeRequestModel.setEndDate(toDate);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.leaveWFHTypeRequest(inputChangeRequestModel),
                CommunicationConstant.API_GET_DETAILS_ON_INPUT_CHANGE, true);
    }

    private void doSubmitOperation() {
        wfhRequestDetailModel = new WFHRequestDetailModel();
        partialDayDataModel = new PartialDayDataModel();

        if (fromButton.equalsIgnoreCase(AppsConstant.SUBMIT)) {
            if (fromDate.equalsIgnoreCase("") || fromDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, "Please Enter From Date");
                return;
            } else if (toDate.equalsIgnoreCase("") || toDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, "Please Enter To Date");
                return;
            } else if (remarksET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, "Please Enter Remarks");
                return;
            } else {
                wfhRequestDetailModel.setStartDate(fromDate);
                wfhRequestDetailModel.setEndDate(toDate);
                wfhRequestDetailModel.setForEmpID(empId);
                wfhRequestDetailModel.setReqID(reqId);
                wfhRequestDetailModel.setRemarks(remarksET.getText().toString());
                partialDayDataModel.setDayP50(dayP50);
                partialDayDataModel.setDayFull(dayFull);
                PartialDayModel partialDayModel = new PartialDayModel();
                partialDayModel.setPartialDayData(partialDayDataModel);
                wfhRequestDetailModel.setPartialDay(partialDayModel);
                wfhRequestDetailModel.setButton(fromButton);
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                wfhRequestDetailModel.setAttachments(uploadFileList);
                WFHRequestModel wfhRequestModel = new WFHRequestModel();
                wfhRequestModel.setWfhRequestDetail(wfhRequestDetailModel);

                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.WFHRequest(wfhRequestModel),
                        CommunicationConstant.API_SAVE_WFH_REQUEST, true);

            }

        }

        if (fromButton.equalsIgnoreCase(AppsConstant.APPROVE)) {
            if (fromDate.equalsIgnoreCase("") || fromDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, "Please Enter From Date");
                return;
            } else if (toDate.equalsIgnoreCase("") || toDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, "Please Enter To Date");
                return;
            } else {
                if (wfhSummaryResponse != null && wfhSummaryResponse.getGetWFHRequestDetailResult() != null
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail() != null
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail().getApprovalLevel() != null) {
                    wfhRequestDetailModel.setApprovalLevel(Integer.parseInt(wfhSummaryResponse.getGetWFHRequestDetailResult().
                            getWFHRequestDetail().getApprovalLevel()));
                }

                wfhRequestDetailModel.setStartDate(fromDate);
                wfhRequestDetailModel.setEndDate(toDate);
                wfhRequestDetailModel.setForEmpID(empId);
                wfhRequestDetailModel.setReqID(reqId);
                wfhRequestDetailModel.setRemarks(remarksET.getText().toString());
                partialDayDataModel.setDayP50(dayP50);
                partialDayDataModel.setDayFull(dayFull);
                PartialDayModel partialDayModel = new PartialDayModel();
                partialDayModel.setPartialDayData(partialDayDataModel);
                wfhRequestDetailModel.setPartialDay(partialDayModel);
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                wfhRequestDetailModel.setAttachments(uploadFileList);
                WFHRequestModel wfhRequestModel = new WFHRequestModel();
                wfhRequestModel.setWfhRequestDetail(wfhRequestDetailModel);

                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.WFHRequest(wfhRequestModel),
                        CommunicationConstant.API_APPROVE_WFH_REQUEST, true);


            }
        }

        if (fromButton.equalsIgnoreCase(AppsConstant.SAVE_AS_DRAFT) || fromButton.equalsIgnoreCase(AppsConstant.DELETE)) {
            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                if (wfhSummaryResponse != null && wfhSummaryResponse.getGetWFHRequestDetailResult() != null
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail() != null
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail().getApprovalLevel() != null) {
                    wfhRequestDetailModel.setApprovalLevel(Integer.parseInt(wfhSummaryResponse.getGetWFHRequestDetailResult().
                            getWFHRequestDetail().getApprovalLevel()));
                }
            }
            wfhRequestDetailModel.setStartDate(fromDate);
            wfhRequestDetailModel.setEndDate(toDate);
            wfhRequestDetailModel.setForEmpID(empId);
            wfhRequestDetailModel.setReqID(reqId);
            wfhRequestDetailModel.setRemarks(remarksET.getText().toString());
            partialDayDataModel.setDayP50(dayP50);
            partialDayDataModel.setDayFull(dayFull);
            PartialDayModel partialDayModel = new PartialDayModel();
            partialDayModel.setPartialDayData(partialDayDataModel);
            wfhRequestDetailModel.setPartialDay(partialDayModel);
            wfhRequestDetailModel.setButton(fromButton);
            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }
            wfhRequestDetailModel.setAttachments(uploadFileList);
            WFHRequestModel wfhRequestModel = new WFHRequestModel();
            wfhRequestModel.setWfhRequestDetail(wfhRequestDetailModel);

            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.WFHRequest(wfhRequestModel),
                        CommunicationConstant.API_APPROVE_WFH_REQUEST, true);
            } else {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.WFHRequest(wfhRequestModel),
                        CommunicationConstant.API_SAVE_WFH_REQUEST, true);
            }
        }

    }

    @Override
    public void validateResponse(ResponseData response) {
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
            case CommunicationConstant.API_GET_DETAILS_ON_INPUT_CHANGE:
                String str = response.getResponseData();
                Log.d("TAG", "wfh leave response : " + str);
                inputChangeResponseModel = GetDetailsOnInputChangeResponseModel.create(str);
                if (inputChangeResponseModel != null && inputChangeResponseModel.getGetDetailsOnInputChangeResult() != null &&
                        inputChangeResponseModel.getGetDetailsOnInputChangeResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    //wfhTimeTypeRL.setVisibility(View.GONE);
                    rb_full_day.setVisibility(View.GONE);
                    rb_half_day.setVisibility(View.GONE);
                    if (inputChangeResponseModel.getGetDetailsOnInputChangeResult().getPartialDayParams().getDayFullVisible().equalsIgnoreCase("Y")) {
                        //wfhTimeTypeRL.setVisibility(View.VISIBLE);
                        rb_full_day.setVisibility(View.VISIBLE);
                        rb_full_day.setChecked(true);
                        rb_half_day.setChecked(false);
                        dayFull = "Y";
                        dayP50 = "N";

                    }
                    if (inputChangeResponseModel.getGetDetailsOnInputChangeResult().getPartialDayParams().getDayP50Visible().equalsIgnoreCase("Y")) {
                        ///wfhTimeTypeRL.setVisibility(View.VISIBLE);
                         rb_half_day.setVisibility(View.VISIBLE);
                        dayFull = "Y";
                        dayP50 = "N";
                    }
                }
                break;
            case CommunicationConstant.API_SAVE_WFH_REQUEST:
                String responseData = response.getResponseData();
                Log.d("TAG", "wfh response : " + responseData);
                wfhResponseModel = WFHResponseModel.create(responseData);
                if (wfhResponseModel != null && wfhResponseModel.getSaveWFHReqResult() != null
                        && wfhResponseModel.getSaveWFHReqResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, wfhResponseModel.getSaveWFHReqResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), wfhResponseModel.getSaveWFHReqResult().getErrorMessage());
                }
                break;

            case CommunicationConstant.API_GET_WFH_REQUEST_DETAIL:
                String resp = response.getResponseData();
                Log.d("TAG", "WFH Details Response : " + resp);
                wfhSummaryResponse = WFHSummaryResponse.create(resp);
                if (wfhSummaryResponse != null && wfhSummaryResponse.getGetWFHRequestDetailResult() != null
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail() != null) {
                    updateUI(wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail());
                    refreshRemarksList(wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail().getRemarkList());
                    uploadFileList = wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail().getAttachments();
                    refreshDocumentList(wfhSummaryResponse.getGetWFHRequestDetailResult().getWFHRequestDetail().getAttachments());
                }

                break;
            case CommunicationConstant.API_GET_CORPEMP_PARAM:
                String responseData1 = response.getResponseData();
                try {
                    ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
                    if (responseData1 != null) {
                        empNameTV.setText("");
                        GetCorpEmpParamResultResponse corpEmpParamResultResponse = GetCorpEmpParamResultResponse.create(responseData1);
                        if (corpEmpParamResultResponse != null && corpEmpParamResultResponse.getGetCorpEmpParamResult() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getErrorCode().equalsIgnoreCase("0")) {

                            if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList() != null &&
                                    corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().size() > 0) {
                                /*if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue() != null) {

                                    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam().equalsIgnoreCase("WFHOnBehalfOfYN")
                                            && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue().equalsIgnoreCase("Y")) {
                                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);
                                    }
                                }*/
                              for(CorpEmpParamListItem item:corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList()){
                                  if(item.getParam().equalsIgnoreCase("WFHOnBehalfOfYN") && item.getValue().equalsIgnoreCase("Y")){
                                      ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);

                                  }

                                  if(item.getParam().equalsIgnoreCase("WFHSelfInitYN") && item.getValue().equalsIgnoreCase("Y")){
                                      employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
                                      empId = loginUserModel.getUserModel().getEmpId();
                                      employItem.setName(loginUserModel.getUserModel().getUserName());
                                      employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());
                                      empNameTV.setText(employItem.getName());

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
            case CommunicationConstant.API_APPROVE_WFH_REQUEST:
                String res = response.getResponseData();
                Log.d("TAG", "wfh response : " + res);
                WFHResponseModel wfhResponseModel = WFHResponseModel.create(res);
                if (wfhResponseModel != null && wfhResponseModel.getApproveWFHRequestResult() != null
                        && wfhResponseModel.getApproveWFHRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, wfhResponseModel.getApproveWFHRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), wfhResponseModel.getApproveWFHRequestResult().getErrorMessage());
                }
                break;
            case CommunicationConstant.API_REJECT_WFH_REQUEST:
                String rejectResponse = response.getResponseData();
                Log.d("TAG", "reject response : " + rejectResponse);
                LeaveRejectResponseModel leaveRejectResponse = LeaveRejectResponseModel.create(rejectResponse);
                if (leaveRejectResponse != null && leaveRejectResponse.getRejectWFHRequestResult() != null
                        && leaveRejectResponse.getRejectWFHRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, leaveRejectResponse.getRejectWFHRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), leaveRejectResponse.getRejectWFHRequestResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    private void getSearchEmployeeData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.GetCorpEmpParam(), CommunicationConstant.API_GET_CORPEMP_PARAM,
                true);
    }

    private void sendViewRequestSummaryData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_WFH_REQUEST_DETAIL, true);
    }

    private void sendViewWFHRequestSummaryData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(employeeLeaveModel.getmReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_WFH_REQUEST_DETAIL, true);
    }

    private void updateUI(WFHRequestDetailItem item) {
        empId = item.getForEmpID();
        empNameTV.setText(item.getForEmpName());
        tv_from_date.setText(item.getStartDate());
        fromDate = item.getStartDate();
        tv_to_date.setText(item.getEndDate());
        toDate = item.getEndDate();
        remarksET.setText(item.getRemark());

        if(item.getPartialDay().getPartialDayParams().getDayP50Visible().equalsIgnoreCase("Y")){
            rb_half_day.setVisibility(View.VISIBLE);
        }
        if(item.getPartialDay().getPartialDayParams().getDayFullVisible().equalsIgnoreCase("Y")){
            rb_full_day.setVisibility(View.VISIBLE);
        }

        if (item.getPartialDay().getPartialDayData().getDayFull().equalsIgnoreCase("Y") &&
                item.getPartialDay().getPartialDayData().getDayP50().equalsIgnoreCase("N")) {
          //  wfhTimeTypeRL.setVisibility(View.VISIBLE);
            rb_full_day.setChecked(true);
            rb_half_day.setChecked(false);
            dayFull = "Y";
            dayP50 = "N";
        }

        if (item.getPartialDay().getPartialDayData().getDayFull().equalsIgnoreCase("N") &&
                item.getPartialDay().getPartialDayData().getDayP50().equalsIgnoreCase("Y")) {
          //  wfhTimeTypeRL.setVisibility(View.VISIBLE);
            rb_half_day.setChecked(true);
            rb_full_day.setChecked(false);
            dayFull = "N";
            dayP50 = "Y";
        }

        setupButtons(item);
    }

    private void setupButtons(WFHRequestDetailItem item) {
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

    private void rejectWFHRequest() {
        if (remarksET.getText().toString().equalsIgnoreCase("")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
            return;
        }
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(reqId);
        rejectRequestModel.setComments(remarksET.getText().toString());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_WFH_REQUEST, true);

    }
}
