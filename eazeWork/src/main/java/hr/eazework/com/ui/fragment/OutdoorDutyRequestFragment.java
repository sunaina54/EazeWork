package hr.eazework.com.ui.fragment;


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
import android.widget.EditText;
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

import hr.eazework.com.FileUtils;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.SearchOnbehalfActivity;
import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.AttendanceItem;
import hr.eazework.com.model.CorpEmpParamListItem;
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.EmployeeLeaveModel;
import hr.eazework.com.model.GetCorpEmpParamResultResponse;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetODRequestDetail;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.LeaveRejectResponseModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.ODRequestDetail;
import hr.eazework.com.model.ODRequestModel;
import hr.eazework.com.model.ODResponseModel;
import hr.eazework.com.model.ODSummaryResponse;
import hr.eazework.com.model.OdReqDetailModel;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
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
public class OutdoorDutyRequestFragment extends BaseFragment {
    public static final String TAG = "OutdoorDutyRequestFragment";
    private String screenName = "OutdoorDutyRequestFragment";
    List<String> extensionList;
    private Context context;
    private Button saveDraftBTN, deleteBTN, submitBTN, rejectBTN, approvalBTN;
    private Preferences preferences;
    private TextView empNameTV, tv_from_date, tv_from_day, tv_start_time, tv_end_time;
    private EditText remarksET, placeEt;
    private DatePickerDialog datePickerDialog2;
    private String fromButton;
    private RecyclerView expenseRecyclerView;
    private ImageView plus_create_newIV;
    private static int UPLOAD_DOC_REQUEST = 1;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private Bitmap bitmap = null;
    private String purpose = "";
    private LinearLayout errorLinearLayout, ll_start_time, ll_end_time;
    private TimePickerDialog timePickerDialog1, timePickerDialog2;
    private EmployItem employItem;
    private RelativeLayout searchLayout;
    public static int OD_EMP = 3;
    private String empId;
    private String reqId;
    private LinearLayout remarksLinearLayout, remarksDataLl;
    private RecyclerView remarksRV;
    private String fromDate = "", startTime = "", endTime = "", place = "";
    private ODRequestModel odRequestModel;
    private OdReqDetailModel odReqDetailModel;
    private ODResponseModel odResponseModel;
    private GetODRequestDetail getODRequestDetail;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private AttendanceItem employeeLeaveModel;
    private ODSummaryResponse odSummaryResponse;
    private LoginUserModel loginUserModel;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public AttendanceItem getEmployeeLeaveModel() {
        return employeeLeaveModel;
    }

    public void setEmployeeLeaveModel(AttendanceItem employeeLeaveModel) {
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
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_outdoor_duty_request, container, false);
        context = getContext();
        setupScreen();
        return rootView;
    }

    private void setupScreen() {

        remarksDataLl = (LinearLayout) rootView.findViewById(R.id.remarksDataLl);
        remarksDataLl.setVisibility(View.GONE);
        preferences = new Preferences(getContext());
        reqId = "0";


        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setVisibility(View.VISIBLE);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.outdoor_duty);
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

        tv_from_day = ((TextView) rootView.findViewById(R.id.tv_from_day));
        tv_from_date = ((TextView) rootView.findViewById(R.id.tv_from_date));
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);
        rootView.findViewById(R.id.ll_from_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });

        empNameTV = (TextView) rootView.findViewById(R.id.empNameTV);
        placeEt = (EditText) rootView.findViewById(R.id.placeEt);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);

        tv_start_time = (TextView) rootView.findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) rootView.findViewById(R.id.tv_end_time);
        ll_start_time = (LinearLayout) rootView.findViewById(R.id.ll_start_time);
        timePickerDialog1 = Utility.setTime(context, tv_start_time);
        timePickerDialog2 = Utility.setTime(context, tv_end_time);
        ll_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog1.show();
            }
        });

        ll_end_time = (LinearLayout) rootView.findViewById(R.id.ll_end_time);
        ll_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog2.show();
            }
        });

        saveDraftBTN = (Button) rootView.findViewById(R.id.saveDraftBTN);
        // saveDraftBTN.setVisibility(View.VISIBLE);
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
                rejectODRequest();
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
                                        PermissionUtil.askAllPermissionCamera(OutdoorDutyRequestFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), OutdoorDutyRequestFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", screenName);
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
        loginUserModel = ModelManager.getInstance().getLoginUserModel();

     /*   employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
        empId = loginUserModel.getUserModel().getEmpId();
        employItem.setName(loginUserModel.getUserModel().getUserName());
        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());
        empNameTV.setText(employItem.getName());*/

        searchLayout = (RelativeLayout) rootView.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(getActivity(), SearchOnbehalfActivity.class);
                theIntent.putExtra("SearchType",OD_EMP);
                startActivityForResult(theIntent, OD_EMP);
            }
        });

        if (getEmpWFHResponseItem != null && getEmpWFHResponseItem.getReqID() != null &&
                !getEmpWFHResponseItem.getReqID().equalsIgnoreCase("0")) {
            reqId = getEmpWFHResponseItem.getReqID();
            remarksDataLl.setVisibility(View.VISIBLE);
            sendViewRequestSummaryData();
            getSearchEmployeeData();
        } else if (employeeLeaveModel != null && employeeLeaveModel.getReqID() != null
                && !employeeLeaveModel.getReqID().equalsIgnoreCase("0")) {
            if (employeeLeaveModel.getReqType() != null && employeeLeaveModel.getReqType().equalsIgnoreCase(AppsConstant.OD_EDIT)) {
                reqId = employeeLeaveModel.getReqID();
                remarksDataLl.setVisibility(View.VISIBLE);
                sendViewWFHRequestSummaryData();
                disabledFieldData();
            }

            if (employeeLeaveModel.getReqType() != null && employeeLeaveModel.getReqType().equalsIgnoreCase(AppsConstant.OD_WITHDRAWAL)) {
                reqId = employeeLeaveModel.getReqID();
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
        saveDraftBTN.setVisibility(View.GONE);

    }
    private void disabledFieldDataForView() {
        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.GONE);
        saveDraftBTN.setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_from_date).setEnabled(false);
        ll_start_time.setEnabled(false);
        ll_end_time.setEnabled(false);
        plus_create_newIV.setVisibility(View.GONE);
        placeEt.setEnabled(false);

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
        if (requestCode == OD_EMP) {
            if (data != null) {
                EmployItem item = (EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_OD_EMP);
                if (item != null) {
                    String[] empname = item.getName().split("\\(");
                    empNameTV.setText(empname[0]);
                    empId = String.valueOf(item.getEmpID());
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

    private void doSubmitOperation() {

        fromDate = tv_from_date.getText().toString();
        startTime = tv_start_time.getText().toString();
        endTime = tv_end_time.getText().toString();
        odRequestModel = new ODRequestModel();
        odReqDetailModel = new OdReqDetailModel();
        if (fromButton.equalsIgnoreCase(AppsConstant.SUBMIT)) {
            if(empNameTV.getText().toString().equalsIgnoreCase("")){
                new AlertCustomDialog(context, getResources().getString(R.string.select_user));
                return;
            }
            if (fromDate.equalsIgnoreCase("") || fromDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_date));
                return;
            } else if (startTime.equalsIgnoreCase("") || startTime.equalsIgnoreCase("--:-- AM")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_start_time));
                return;
            } else if (endTime.equalsIgnoreCase("") || endTime.equalsIgnoreCase("--:-- AM")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_end_time));
                return;
            } else if (remarksET.getText().toString().equalsIgnoreCase("")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_remarks));
                return;
            } else {
                odReqDetailModel.setForEmpID(empId);
                odReqDetailModel.setReqID(reqId);
                odReqDetailModel.setDate(fromDate);
                odReqDetailModel.setStartTime(startTime);
                odReqDetailModel.setEndTime(endTime);
                odReqDetailModel.setPlace(placeEt.getText().toString());
                odReqDetailModel.setButton(fromButton);
                odReqDetailModel.setRemarks(remarksET.getText().toString());
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                odReqDetailModel.setAttachments(uploadFileList);
                odRequestModel.setOdReqDetail(odReqDetailModel);
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.ODRequest(odRequestModel),
                        CommunicationConstant.API_SAVE_OD_REQUEST, true);
            }

        }

        if (fromButton.equalsIgnoreCase(AppsConstant.APPROVE)) {
            if (fromDate.equalsIgnoreCase("") || fromDate.equalsIgnoreCase("--/--/----")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_date));
                return;
            } else if (startTime.equalsIgnoreCase("") || startTime.equalsIgnoreCase("--:-- AM")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_start_time));
                return;
            } else if (endTime.equalsIgnoreCase("") || endTime.equalsIgnoreCase("--:-- AM")) {
                new AlertCustomDialog(context, getResources().getString(R.string.enter_end_time));
                return;
            } else {
                if (odResponseModel != null && odSummaryResponse.getGetODRequestDetailResult() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getApprovalLevel() != null) {
                    odReqDetailModel.setApprovalLevel(Integer.parseInt(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getApprovalLevel()));
                }
                odReqDetailModel.setForEmpID(empId);
                odReqDetailModel.setReqID(reqId);
                odReqDetailModel.setDate(fromDate);
                odReqDetailModel.setStartTime(startTime);
                odReqDetailModel.setEndTime(endTime);
                odReqDetailModel.setPlace(placeEt.getText().toString());
                odReqDetailModel.setRemarks(remarksET.getText().toString());
                if (uploadFileList != null && uploadFileList.size() > 0) {
                    for (int i = 0; i < uploadFileList.size(); i++) {
                        SupportDocsItemModel model = uploadFileList.get(i);
                        model.setSeqNo(i + 1);
                        uploadFileList.set(i, model);
                    }
                }
                odReqDetailModel.setAttachments(uploadFileList);
                odRequestModel.setOdReqDetail(odReqDetailModel);
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.ODRequest(odRequestModel),
                        CommunicationConstant.API_APPROVE_OD_REQUEST, true);
            }
        }

        if (fromButton.equalsIgnoreCase("Save") || fromButton.equalsIgnoreCase(AppsConstant.DELETE)) {
            if(empNameTV.getText().toString().equalsIgnoreCase("")){
                new AlertCustomDialog(context, getResources().getString(R.string.select_user));
                return;
            }

            if (startTime.equalsIgnoreCase("--:-- AM")) {
                startTime = "";
            }
            if (endTime.equalsIgnoreCase("--:-- AM")) {
                endTime = "";
            }
            if (fromDate.equalsIgnoreCase("--/--/----")) {
                fromDate = "";
            }
            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                if (odResponseModel != null && odSummaryResponse.getGetODRequestDetailResult() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getApprovalLevel() != null) {
                    odReqDetailModel.setApprovalLevel(Integer.parseInt(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getApprovalLevel()));
                }
            }
            odReqDetailModel.setForEmpID(empId);
            odReqDetailModel.setReqID(reqId);
            odReqDetailModel.setDate(fromDate);
            odReqDetailModel.setStartTime(startTime);
            odReqDetailModel.setEndTime(endTime);
            odReqDetailModel.setPlace(placeEt.getText().toString());
            odReqDetailModel.setRemarks(remarksET.getText().toString());
            odReqDetailModel.setButton(fromButton);
            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }
            odReqDetailModel.setAttachments(uploadFileList);
            odRequestModel.setOdReqDetail(odReqDetailModel);

            if (getScreenName().equalsIgnoreCase(AppsConstant.PENDING_APPROVAL)) {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.ODRequest(odRequestModel),
                        CommunicationConstant.API_APPROVE_OD_REQUEST, true);
            } else {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.ODRequest(odRequestModel),
                        CommunicationConstant.API_SAVE_OD_REQUEST, true);
            }

        }

    }


    private void sendViewRequestSummaryData() {
        getODRequestDetail = new GetODRequestDetail();
        getODRequestDetail.setReqID(Integer.parseInt(getEmpWFHResponseItem.getReqID()));
        getODRequestDetail.setAction(AppsConstant.EDIT_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.ODSummaryDetails(getODRequestDetail),
                CommunicationConstant.API_GET_OD_REQUEST_DETAIL, true);
    }

    private void sendViewWFHRequestSummaryData() {
        getODRequestDetail = new GetODRequestDetail();
        getODRequestDetail.setReqID(Integer.parseInt(employeeLeaveModel.getReqID()));
        getODRequestDetail.setAction(AppsConstant.EDIT_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.ODSummaryDetails(getODRequestDetail),
                CommunicationConstant.API_GET_OD_REQUEST_DETAIL, true);
    }

    public void sendAdvanceRequestData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_PAGE_INIT, true);
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

            case CommunicationConstant.API_SAVE_OD_REQUEST:
                String responseData = response.getResponseData();
                Log.d("TAG", "od response : " + responseData);
                odResponseModel = ODResponseModel.create(responseData);
                if (odResponseModel != null && odResponseModel.getSaveODReqResult() != null
                        && odResponseModel.getSaveODReqResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, odResponseModel.getSaveODReqResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), odResponseModel.getSaveODReqResult().getErrorMessage());
                }
                break;

            case CommunicationConstant.API_GET_OD_REQUEST_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "OD Summary Response : " + str);
                odSummaryResponse = ODSummaryResponse.create(str);
                if (odSummaryResponse != null && odSummaryResponse.getGetODRequestDetailResult() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail() != null) {
                    updateUI(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail());
                    refreshRemarksList(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getRemarkList());
                    uploadFileList = odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getAttachments();
                    refreshDocumentList(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getAttachments());
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
                            /*    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam() != null && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue() != null) {

                                    if (corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getParam().equalsIgnoreCase("ODOnBehalfOfYN")
                                            && corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList().get(0).getValue().equalsIgnoreCase("Y")) {
                                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);
                                    }
                                }*/
                                for(CorpEmpParamListItem item:corpEmpParamResultResponse.getGetCorpEmpParamResult().getCorpEmpParamList()){
                                    if(item.getParam().equalsIgnoreCase("ODOnBehalfOfYN") && item.getValue().equalsIgnoreCase("Y")){
                                        ((RelativeLayout) rootView.findViewById(R.id.searchLayout)).setVisibility(View.VISIBLE);

                                    }

                                    if(item.getParam().equalsIgnoreCase("ODSelfInitYN") && item.getValue().equalsIgnoreCase("Y")){
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
            case CommunicationConstant.API_APPROVE_OD_REQUEST:
                String odresponseData = response.getResponseData();
                Log.d("TAG", "od response : " + odresponseData);
                ODResponseModel odResponseModel = ODResponseModel.create(odresponseData);
                if (odResponseModel != null && odResponseModel.getApproveODRequestResult() != null
                        && odResponseModel.getApproveODRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, odResponseModel.getApproveODRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), odResponseModel.getApproveODRequestResult().getErrorMessage());
                }
                break;

            case CommunicationConstant.API_REJECT_OD_REQUEST:
                String data = response.getResponseData();
                Log.d("TAG", "reject response : " + data);
                LeaveRejectResponseModel rejectODResponse = LeaveRejectResponseModel.create(data);
                if (rejectODResponse != null && rejectODResponse.getRejectODRequestResult() != null
                        && rejectODResponse.getRejectODRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, rejectODResponse.getRejectODRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), rejectODResponse.getRejectODRequestResult().getErrorMessage());
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

    private void updateUI(ODRequestDetail item) {
        empId = String.valueOf(item.getForEmpID());
        empNameTV.setText(item.getForEmpName());
        tv_from_date.setText(item.getDate());
        fromDate = item.getDate();
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);
        tv_start_time.setText(item.getStartTime());
        startTime = item.getStartTime();
        tv_end_time.setText(item.getEndTime());
        endTime = item.getEndTime();
        placeEt.setText(item.getPlace());
        remarksET.setText(item.getRemark());

        setupButtons(item);

    }

    private void setupButtons(ODRequestDetail item) {
        if (item.getButtons() != null) {
            for (String button : item.getButtons()) {
                if (button.equalsIgnoreCase(AppsConstant.DELETE)) {
                    deleteBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.SUBMIT)) {
                    submitBTN.setVisibility(View.VISIBLE);
                }
                if (button.equalsIgnoreCase(AppsConstant.DRAFT)) {
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
            remarksRV.setVisibility(View.VISIBLE);
        }
    }

    private void rejectODRequest() {
        if (remarksET.getText().toString().equalsIgnoreCase("")) {
            new AlertCustomDialog(context, context.getResources().getString(R.string.enter_remarks));
            return;
        }
        WFHRejectRequestModel rejectRequestModel = new WFHRejectRequestModel();
        rejectRequestModel.setReqID(reqId);
        rejectRequestModel.setComments(remarksET.getText().toString());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.rejectRequest(rejectRequestModel),
                CommunicationConstant.API_REJECT_OD_REQUEST, true);

    }
}
