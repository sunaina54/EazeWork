package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.eazework.com.FileUtils;
import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceDataResponseModel;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.AdvanceListModel;
import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.DocListModel;
import hr.eazework.com.model.GetAdvanceDetailResponseModel;
import hr.eazework.com.model.GetAdvanceDetailResultModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RequestRemarksItem;
import hr.eazework.com.model.SupportDocsItemModel;
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

/**
 * Created by Dell3 on 28-09-2017.
 */

public class EditAdvanceApprovalFragment extends BaseFragment {
    private Context context;
    private TextView reasonTV, currencyTV;
    private EditText remarksET, amountET;
    private ImageView plus_create_newIV;
    private RecyclerView expenseRecyclerView, remarksRV;
    private LinearLayout errorTV, remarksLinearLayout;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private GetAdvanceDetailResultModel advanceListModel;
    private static int UPLOAD_DOC_REQUEST = 1;
    private Preferences preferences;
    private TextView dateTV, nameTV, remarksReasonTV, remarksStatusTV, voucherNoTV;
    private String remarks, amount, currency, empId, reasonCode, reason, requestId;
    private String fromButton;
    private Button rejectBTN, approvalBTN;
    private ProgressBar progressBar;
    private RemarksAdapter remarksAdapter;
    private AdvanceRequestResponseModel advanceRequestResponseModel;
    private GetAdvanceDetailResponseModel advanceDetailResponseModel;
    private Bitmap bitmap = null;
    private String purpose = "";

    public GetAdvanceDetailResultModel getAdvanceListModel() {
        return advanceListModel;
    }

    public void setAdvanceListModel(GetAdvanceDetailResultModel advanceListModel) {
        this.advanceListModel = advanceListModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_advance_aproval_fragment, container, false);
        context = getContext();
        if (advanceListModel != null && advanceListModel.getSupportDocs() != null && advanceListModel.getSupportDocs().size() > 0) {
            uploadFileList = advanceListModel.getSupportDocs();
        } else {
            uploadFileList = new ArrayList<SupportDocsItemModel>();
        }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);

        voucherNoTV = (TextView) rootView.findViewById(R.id.voucherNoTV);
        reasonTV = (TextView) rootView.findViewById(R.id.reasonTV);
        currencyTV = (TextView) rootView.findViewById(R.id.currencyTV);
        amountET = (EditText) rootView.findViewById(R.id.amountET);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);
        rejectBTN = (Button) rootView.findViewById(R.id.rejectBTN);
        approvalBTN = (Button) rootView.findViewById(R.id.approvalBTN);
        errorTV = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorTV.setVisibility(View.VISIBLE);
        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(expenseRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_line));
        expenseRecyclerView.addItemDecoration(itemDecoration);

        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksLinearLayout.setVisibility(View.VISIBLE);

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
                                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !PermissionUtil.checkLocationPermission(getContext())) {
                                        PermissionUtil.askAllPermission(EditAdvanceApprovalFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && PermissionUtil.checkLocationPermission(getContext())) {
                                        if (Utility.isLocationEnabled(getContext())) {
                                            if (Utility.isNetworkAvailable(getContext())) {
                                                Utility.openCamera(getActivity(), EditAdvanceApprovalFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore");
                                                customBuilder.dismiss();
                                            } else {
                                                Utility.showNetworkNotAvailableDialog(getContext());
                                            }
                                        } else {
                                            Utility.requestToEnableGPS(getContext(), new Preferences(getContext()));
                                        }
                                    } else {
                                        Utility.displayMessage(getContext(), "Please provide all permissions");
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

        if (advanceListModel != null) {
            setData(advanceListModel);
        }

        sendAdvanceRequestData();

        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Reject";
                sendAdvanceApprovalData();


            }
        });

        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Approve";
                sendAdvanceApprovalData();

            }
        });
        return rootView;
    }

    private void setData(GetAdvanceDetailResultModel advanceListModel) {
        voucherNoTV.setText(advanceListModel.getReqCode());
        reasonTV.setText(advanceListModel.getReason());
        currencyTV.setText(advanceListModel.getCurrencyCode());
        amountET.setText(advanceListModel.getApprovedAmount());
        remarksET.setText(advanceListModel.getRemarks());
        sendViewRequestSummaryData();
    }

    private void sendViewRequestSummaryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getViewAdvanceSummaryData(advanceListModel.getReqID(), advanceListModel.getAdvanceID()),
                CommunicationConstant.API_GET_ADVANCE_DETAIL, true);
    }

    private void sendAdvanceApprovalData() {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        reason = reasonTV.getText().toString();
        currency = currencyTV.getText().toString();
        amount = amountET.getText().toString();
        remarks = remarksET.getText().toString();
        reasonCode = advanceDetailResponseModel.getGetAdvanceDetailResult().getReasonCode();
        requestId = String.valueOf(advanceListModel.getReqID());
        if (uploadFileList != null && uploadFileList.size() > 0) {
            for (int i = 0; i < uploadFileList.size(); i++) {
                SupportDocsItemModel model = uploadFileList.get(i);
                model.setSeqNo(i + 1);
                uploadFileList.set(i, model);
            }
        }
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        empId = loginUserModel.getUserModel().getEmpId();
        if (reason.equalsIgnoreCase("")) {
            new AlertCustomDialog(context, "Please Select Reason");
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else if (currency.equalsIgnoreCase("")) {
            new AlertCustomDialog(context, "Please Select Currency");
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else if (amount.equalsIgnoreCase("")) {
            new AlertCustomDialog(context, "Please Enter Amount");
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {

            if (fromButton.equalsIgnoreCase("Reject")) {
                if (!remarks.equalsIgnoreCase("")) {
                    CommunicationManager.getInstance().sendPostRequest(this,
                            AppRequestJSONString.getAdvanceRequestData(fromButton, requestId,advanceDetailResponseModel.getGetAdvanceDetailResult().getApprovalLevel(), remarks, amount, currency, empId, reasonCode, reason, uploadFileList),
                            CommunicationConstant.API_GET_SAVE_ADVANCE, true);
                } else {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please Enter Remarks");
                }
            } else {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getAdvanceRequestData(fromButton, requestId,
                                advanceDetailResponseModel.getGetAdvanceDetailResult().getApprovalLevel(),remarks, amount, currency, empId, reasonCode, reason, uploadFileList),
                        CommunicationConstant.API_GET_SAVE_ADVANCE, true);
            }
        }
    }
    public void sendAdvanceRequestData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_PAGE_INIT, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ADVANCE_PAGE_INIT:
                String str1 = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str1);
                advanceRequestResponseModel = AdvanceRequestResponseModel.create(str1);

                break;
            case CommunicationConstant.API_GET_ADVANCE_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                advanceDetailResponseModel = GetAdvanceDetailResponseModel.create(str);
                if (advanceDetailResponseModel != null && advanceDetailResponseModel.getGetAdvanceDetailResult() != null) {
                    uploadFileList = advanceDetailResponseModel.getGetAdvanceDetailResult().getSupportDocs();
                    refreshList(uploadFileList);
                    refreshRemarksList(advanceDetailResponseModel.getGetAdvanceDetailResult().getRequestRemarks());
                }

                break;
            case CommunicationConstant.API_GET_SAVE_ADVANCE:
                String responseData = response.getResponseData();
                Log.d("TAG", "Advance request Response : " + responseData);
                AdvanceDataResponseModel advanceDataResponseModel = AdvanceDataResponseModel.create(responseData);
                if (advanceDataResponseModel != null && advanceDataResponseModel.getSaveAdvanceResult() != null
                        && advanceDataResponseModel.getSaveAdvanceResult().getErrorCode().equalsIgnoreCase("0")) {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    CustomDialog.alertOkWithFinishFragment(context, advanceDataResponseModel.getSaveAdvanceResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                }else {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new AlertCustomDialog(getActivity(), advanceDataResponseModel.getSaveAdvanceResult().getErrorMessage());
                }
                break;


            default:
                break;
        }
        super.validateResponse(response);
    }

    private class RemarksAdapter extends
            RecyclerView.Adapter<RemarksAdapter.MyViewHolder> {
        private ArrayList<RequestRemarksItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView dateTV, nameTV, remarksReasonTV, remarksStatusTV;

            public MyViewHolder(View v) {
                super(v);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                remarksReasonTV = (TextView) v.findViewById(R.id.remarksReasonTV);
                remarksStatusTV = (TextView) v.findViewById(R.id.remarksStatusTV);

            }
        }

        public void addAll(List<RequestRemarksItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public RemarksAdapter(List<RequestRemarksItem> data) {
            this.dataSet = (ArrayList<RequestRemarksItem>) data;

        }

        @Override
        public RemarksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.remarks_item_layout, parent, false);
            RemarksAdapter.MyViewHolder myViewHolder = new RemarksAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final RemarksAdapter.MyViewHolder holder, final int listPosition) {

            final RequestRemarksItem item = dataSet.get(listPosition);
            holder.dateTV.setText(item.getTranTime());
            holder.nameTV.setText(item.getRemarkBy());
            holder.remarksReasonTV.setText(item.getRemark());
            holder.remarksStatusTV.setText(item.getStatus());

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

    private void refreshRemarksList(ArrayList<RequestRemarksItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            remarksRV.setVisibility(View.VISIBLE);
            remarksAdapter = new RemarksAdapter(remarksItems);
            remarksRV.setAdapter(remarksAdapter);
            remarksAdapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
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

        final SupportDocsItemModel fileObj = new SupportDocsItemModel();
        if (requestCode == UPLOAD_DOC_REQUEST && resultCode == RESULT_OK) {
            boolean fileShow = true;
            final Uri uri = data.getData();
            String encodeFileToBase64Binary=null;
            if (data != null) {
                String path = data.getStringExtra("path");
                System.out.print(path);
                Uri uploadedFilePath = data.getData();
                String filename = getFileName(uploadedFilePath);
                String fileDesc = getFileName(uploadedFilePath);
                String[] extList = filename.split("\\.");
                System.out.print(extList[1].toString());
                String extension = "." + extList[extList.length - 1];
                List<String> extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getMessage());
                    return;
                }

                if (filename.contains(".pdf")) {
                    try {
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);

                    } catch (Exception e) {
                        System.out.print(e.toString());
                    }
                } else if (filename.contains(".jpg") || filename.contains(".png") || filename.contains(".jpeg") ||
                        filename.contains(".BMP") || filename.contains(".bmp")) {
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
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                }else if (filename.contains(".xlsx") || filename.contains(".xls")) {
                    try {
                        encodeFileToBase64Binary=fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                }  else if (filename.contains(".txt")) {
                    try {
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);

                    } catch (Exception e) {

                    }
                }else if(filename.contains(".gif")){
                    encodeFileToBase64Binary=fileToBase64Conversion(data.getData());
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                }else if(filename.contains(".rar")){
                    encodeFileToBase64Binary=fileToBase64Conversion(data.getData());
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                }else if(filename.contains(".zip")){
                    encodeFileToBase64Binary=fileToBase64Conversion(data.getData());
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
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
                    Log.d("encodedFile", encodeFileToBase64Binary);
                }
                refreshList(uploadFileList);

            }
        }

        if (requestCode == AppsConstant.REQ_CAMERA && resultCode == RESULT_OK) {

            final Intent intent = data;//new Intent();
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
                            String encodeFileToBase64Binary = Utility.converBitmapToBase64(bitmap) ;
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
                            Log.d("encodedFile", encodeFileToBase64Binary);
                        }
                        refreshList(uploadFileList);
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result.toLowerCase();
    }

    private String fileToBase64Conversion(Uri file) {
        String attachedFile;
        InputStream inputStream = null;//You can get an inputStream using any IO API
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            inputStream = context.getContentResolver().openInputStream(file);
            byte[] buffer = new byte[8192];
            int bytesRead;
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output64.close();
        } catch (Exception ex) {
            System.out.print(ex.toString());
        }
        attachedFile = output.toString();
        return attachedFile;
    }

    private void refreshList(ArrayList<SupportDocsItemModel> uploadFileList) {
        if (uploadFileList != null && uploadFileList.size() > 0) {
            errorTV.setVisibility(View.GONE);
            expenseRecyclerView.setVisibility(View.VISIBLE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList);
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorTV.setVisibility(View.VISIBLE);

            expenseRecyclerView.setVisibility(View.GONE);
        }
    }

    private class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {
        private ArrayList<SupportDocsItemModel> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView fileNameTV, fileDescriptionTV;
            public ImageView img_icon, img_menu_icon;
            public RelativeLayout documentParentLayout;

            public ViewHolder(View v) {
                super(v);
                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                fileDescriptionTV = (TextView) v.findViewById(R.id.fileDescriptionTV);
                img_icon = (ImageView) v.findViewById(R.id.img_icon);
                img_menu_icon = (ImageView) v.findViewById(R.id.img_menu_icon);
                documentParentLayout = (RelativeLayout) v.findViewById(R.id.documentParentLayout);

            }
        }

        public DocumentUploadAdapter(ArrayList<SupportDocsItemModel> myDataset) {
            mDataset = myDataset;

        }

        @Override
        public DocumentUploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            DocumentUploadAdapter.ViewHolder vh = new DocumentUploadAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final DocumentUploadAdapter.ViewHolder holder, final int position) {

            final SupportDocsItemModel fileObject = mDataset.get(position);
            holder.documentParentLayout.setVisibility(View.GONE);
            if (fileObject.getDocID() != 0 && fileObject.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)) {
                holder.documentParentLayout.setVisibility(View.GONE);

            } else {
                holder.documentParentLayout.setVisibility(View.VISIBLE);

                String fileType = "";

                final String filename = fileObject.getDocFile();
                final String name = fileObject.getName();
                if (filename.toString().contains(".pdf")) {
                    fileType = "application/pdf";
                    try {
                        holder.img_icon.setImageDrawable((context.getResources().getDrawable(R.drawable.pdf_icon)));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                } else if (filename.contains(".jpg") || filename.contains(".png") || filename.contains(".jpeg") ||
                        filename.contains(".BMP") || filename.contains(".bmp")) {
                    try {
                       // holder.img_icon.setImageBitmap(fileObject.getBitmap());
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                } else if (filename.toString().contains(".docx") || filename.toString().contains(".doc")) {
                    fileType = "application/word";
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.doc_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                } else if (filename.toString().contains(".xlsx") || filename.toString().contains(".xls")) {
                    fileType = "application/word";
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.doc_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                } else if (filename.toString().contains(".txt")) {
                    fileType = "application/txt";
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.txt_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                }else if(filename.contains(".gif")){
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.gif_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                }else if(filename.contains(".rar")){
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rar_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                }else if(filename.contains(".zip")){
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.zip_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                }

                holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        ArrayList<String> list = new ArrayList<>();
                        if(fileObject.getDocID()!=0) {
                            list.add("Edit");
                            list.add("Delete");
                            list.add("Download");
                        }else {
                            list.add("Edit");
                            list.add("Delete");
                        }
                        CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                        customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.filename_advance_expense);
                                    preferences = new Preferences(getContext());
                                    int textColor = Utility.getTextColorCode(preferences);
                                    int bgColor = Utility.getBgColorCode(context, preferences);
                                    FrameLayout fl_actionBarContainer = (FrameLayout) dialog.findViewById(R.id.fl_actionBarContainer);
                                    fl_actionBarContainer.setBackgroundColor(bgColor);
                                    TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
                                    tv_header_text.setTextColor(textColor);
                                    tv_header_text.setText("Edit");

                                    final EditText editFilenameET = (EditText) dialog.findViewById(R.id.editFilenameET);
                                    editFilenameET.setText(name);

                                    (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            fileObject.setName(editFilenameET.getText().toString());
                                            if (uploadFileList != null && uploadFileList.size() > 0) {
                                                uploadFileList.set(uploadFileList.indexOf(fileObject), fileObject);

                                            } else {
                                                uploadFileList = new ArrayList<SupportDocsItemModel>();
                                                uploadFileList.add(fileObject);
                                            }
                                            refreshList(uploadFileList);
                                            dialog.dismiss();

                                        }
                                    });
                                    (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                                    SupportDocsItemModel doc = mDataset.get(position);
                                    if (doc.getDocID() != 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.OLD_FLAG)) {
                                        doc.setFlag(AppsConstant.DELETE_FLAG);
                                        mDataset.set(position, doc);
                                    } else if (doc.getDocID() == 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {
                                        mDataset.remove(position);
                                    }
                                    DocumentUploadAdapter.this.notifyDataSetChanged();
                                    if (mDataset.size() == 0) {
                                        errorTV.setVisibility(View.VISIBLE);
                                    }

                                }else if (selectedObject.toString().equalsIgnoreCase("Download")) {

                                    String filePath = fileObject.getDocPath().replace("~", "");
                                    String path = CommunicationConstant.UrlFile + filePath + "/" + fileObject.getDocFile();

                                    Utility.downloadPdf(path,null,fileObject.getDocFile(),context,getActivity());
                                }
                                builder.dismiss();
                            }
                        });
                        customBuilder.show();
                    }
                });
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
