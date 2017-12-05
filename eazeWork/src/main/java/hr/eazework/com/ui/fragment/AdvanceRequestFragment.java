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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceDataResponseModel;
import hr.eazework.com.model.AdvanceItemModel;
import hr.eazework.com.model.AdvanceListModel;
import hr.eazework.com.model.AdvanceRequestResponseModel;
import hr.eazework.com.model.CurrencyListModel;
import hr.eazework.com.model.ExpenseImageList;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.ReasonCodeListItemModel;
import hr.eazework.com.model.ReasonCodeListModel;
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
 * Created by Dell3 on 01-09-2017.
 */

public class AdvanceRequestFragment extends BaseFragment {
    public static final String TAG = "AdvanceRequestFragment";
    private String screenName = "AdvanceRequestFragment";
    private Context context;
    private Preferences preferences;
    private ImageView plus_create_newIV;
    private RecyclerView expenseRecyclerView;
    private static int UPLOAD_DOC_REQUEST = 1;
    private Bitmap bitmap = null;
    private String purpose = "";
    private AdvanceRequestResponseModel advanceRequestResponseModel;

    private Spinner reasonSpinner, currencySpinner;
    private ArrayList<String> reasonList, currencyList;
    private EditText remarksET, amountET;
    private AdvanceItemModel advanceItem;
    private ArrayList<File> filePathList;
    private String reason, currency, reasonCode;
    private LinearLayout errorTV;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private TextView reasonTV, currencyTV;
    private ReasonCodeListItemModel reasonCodeModel;
    private CurrencyListModel currencyListModel;
    private ProgressBar progressBar;
    private String currencyValue;
    private AdvanceListModel advanceListModel;
    private boolean isClicked;

    public AdvanceListModel getAdvanceListModel() {
        return advanceListModel;
    }

    public void setAdvanceListModel(AdvanceListModel advanceListModel) {
        this.advanceListModel = advanceListModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.advance_request_fragment, container, false);
        context = getContext();
        filePathList = new ArrayList<File>();
        uploadFileList = new ArrayList<SupportDocsItemModel>();
        preferences = new Preferences(getContext());

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);

        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Advance Request");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    setAdvanceData();
                }
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.EDIT_PROFILE_KEY);
                    mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                }
            }
        });

        reasonTV = (TextView) rootView.findViewById(R.id.reasonTV);
        reasonTV.setOnClickListener(this);
        currencyTV = (TextView) rootView.findViewById(R.id.currencyTV);
        currencyTV.setOnClickListener(this);
        amountET = (EditText) rootView.findViewById(R.id.amountET);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);
        errorTV = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorTV.setVisibility(View.VISIBLE);
        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        reasonSpinner = (Spinner) rootView.findViewById(R.id.reasonSpinner);
        currencySpinner = (Spinner) rootView.findViewById(R.id.currencySpinner);
        expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(expenseRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_line));
        expenseRecyclerView.addItemDecoration(itemDecoration);
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
                                        PermissionUtil.askAllPermission(AdvanceRequestFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        if (Utility.isLocationEnabled(getContext())) {
                                            if (Utility.isNetworkAvailable(getContext())) {
                                                Utility.openCamera(getActivity(), AdvanceRequestFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", screenName);
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
        sendAdvanceRequestData();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reasonTV:
                if (advanceRequestResponseModel != null && advanceRequestResponseModel.getGetAdvancePageInitResult() != null
                        && advanceRequestResponseModel.getGetAdvancePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && advanceRequestResponseModel.getGetAdvancePageInitResult().getReasonCodeList() != null
                        && advanceRequestResponseModel.getGetAdvancePageInitResult().getReasonCodeList().getReasonCodeList().size() > 0) {
                    ReasonCodeListModel reasonCodeListModel = advanceRequestResponseModel.getGetAdvancePageInitResult().getReasonCodeList();
                    if (reasonCodeListModel != null) {
                        ArrayList<ReasonCodeListItemModel> reasonCodeList = reasonCodeListModel.getReasonCodeList();

                        CustomBuilder reasonDialog = new CustomBuilder(getContext(), "Select Reason", true);
                        reasonDialog.setSingleChoiceItems(reasonCodeList, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                reasonCodeModel = (ReasonCodeListItemModel) selectedObject;
                                reasonTV.setText(reasonCodeModel.getReason());
                                reasonCode = reasonCodeModel.getReasonCode();
                                builder.dismiss();

                            }
                        });
                        reasonDialog.show();
                    }
                } else {
                    new AlertCustomDialog(context, advanceRequestResponseModel.getGetAdvancePageInitResult().getErrorMessage());
                    return;
                }
                break;
            case R.id.currencyTV:
                currencyListModel = advanceRequestResponseModel.getGetAdvancePageInitResult().getCurrencyList();
                if (currencyListModel != null) {
                    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(currencyListModel.getCurrencyList()));
                    CustomBuilder currencyDialog = new CustomBuilder(getContext(), "Select Currency", true);
                    currencyDialog.setSingleChoiceItems(arrayList, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            currencyValue = (String) selectedObject;
                            currencyTV.setText(currencyValue);
                            builder.dismiss();

                        }
                    });
                    currencyDialog.show();
                }
                break;
            default:
                break;
        }
        super.onClick(v);
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
    public void validateResponse(ResponseData response) {
        Log.d("TAG", "response data " + response.isSuccess());
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ADVANCE_PAGE_INIT:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                if (str!=null && !str.equalsIgnoreCase("")) {
                    advanceRequestResponseModel = AdvanceRequestResponseModel.create(str);
                    if (advanceRequestResponseModel != null &&
                            advanceRequestResponseModel.getGetAdvancePageInitResult() != null && advanceRequestResponseModel.getGetAdvancePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                        if (advanceRequestResponseModel != null && advanceRequestResponseModel.getGetAdvancePageInitResult() != null
                                && advanceRequestResponseModel.getGetAdvancePageInitResult().getCurrencyList() != null
                                && advanceRequestResponseModel.getGetAdvancePageInitResult().getCurrencyList().getCurrencyList() != null) {
                            currencyListModel = advanceRequestResponseModel.getGetAdvancePageInitResult().getCurrencyList();
                            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(currencyListModel.getCurrencyList()));
                            if (arrayList.size() == 1) {
                                currencyValue = arrayList.get(0);
                                currencyTV.setText(arrayList.get(0));
                            }

                        }
                    }
                } else {
                    CustomDialog.alertOkWithFinishFragment(context, context.getResources().getResourceName(R.string.network_error), mUserActionListener, IAction.HOME_VIEW, true);
                }

                break;
            case CommunicationConstant.API_GET_SAVE_ADVANCE:
                isClicked = false;
                String responseData = response.getResponseData();
                Log.d("TAG", "Advance request Response : " + responseData);
                if(responseData!=null && !responseData.equalsIgnoreCase("")){
                AdvanceDataResponseModel advanceDataResponseModel = AdvanceDataResponseModel.create(responseData);
                if (advanceDataResponseModel != null && advanceDataResponseModel.getSaveAdvanceResult() != null
                        && advanceDataResponseModel.getSaveAdvanceResult().getErrorCode().equalsIgnoreCase("0")) {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    CustomDialog.alertOkWithFinishFragment(context, advanceDataResponseModel.getSaveAdvanceResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new AlertCustomDialog(getActivity(), advanceDataResponseModel.getSaveAdvanceResult().getErrorMessage());
                }}else {
                    CustomDialog.alertOkWithFinishFragment(context, context.getResources().getResourceName(R.string.network_error), mUserActionListener, IAction.HOME_VIEW, true);
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final SupportDocsItemModel fileObj = new SupportDocsItemModel();
        if (requestCode == UPLOAD_DOC_REQUEST && resultCode == RESULT_OK) {
            boolean fileShow = true;
            final Uri uri = data.getData();
            String encodeFileToBase64Binary = null;
            if (data != null) {
                String path = data.getStringExtra("path");
                System.out.print(path);
                Uri uploadedFilePath = data.getData();
                String filename = getFileName(uploadedFilePath);
                filename = filename.toLowerCase();
                String fileDesc = getFileName(uploadedFilePath);
                String[] extList = filename.split("\\.");
                System.out.print(extList[1].toString());
                String extension = "." + extList[extList.length - 1];
                encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                Log.d("TAG", "RAR Base 64 :" + encodeFileToBase64Binary);
                List<String> extensionList = Arrays.asList(advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, advanceRequestResponseModel.getGetAdvancePageInitResult().getDocValidation().getMessage());
                    return;
                }
                fileObj.setDocPathUri(uploadedFilePath);

                if (filename.contains(".pdf")) {
                    try {
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
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
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                } else if (filename.contains(".xlsx") || filename.contains(".xls")) {
                    try {
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);


                    } catch (Exception e) {

                    }
                } else if (filename.contains(".txt")) {
                    try {
                        encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                        fileObj.setDocFile(filename);
                        fileObj.setName(fileDesc);

                    } catch (Exception e) {

                    }
                } else if (filename.contains(".gif")) {
                    encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                } else if (filename.contains(".rar")) {
                    encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
                    fileObj.setDocFile(filename);
                    fileObj.setName(fileDesc);
                } else if (filename.contains(".zip")) {
                    encodeFileToBase64Binary = fileToBase64Conversion(data.getData());
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
                            String encodeFileToBase64Binary = Utility.converBitmapToBase64(bitmap);

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

    }

    private void refreshList() {
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

            public ViewHolder(View v) {
                super(v);
                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                fileDescriptionTV = (TextView) v.findViewById(R.id.fileDescriptionTV);
                img_icon = (ImageView) v.findViewById(R.id.img_icon);
                img_menu_icon = (ImageView) v.findViewById(R.id.img_menu_icon);

            }
        }

        public DocumentUploadAdapter(ArrayList<SupportDocsItemModel> myDataset) {
            mDataset = myDataset;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final SupportDocsItemModel fileObject = mDataset.get(position);
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
            } else if (filename.contains(".jpg") ||
                    filename.contains(".png") || filename.contains(".jpeg")
                    || filename.contains(".bmp") || filename.contains(".BMP")) {
                try {
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
            } else if (filename.contains(".gif")) {
                try {
                    holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.gif_icon));
                    holder.fileNameTV.setText(filename);
                    holder.fileDescriptionTV.setText(name);
                } catch (Exception e) {

                }
            } else if (filename.contains(".rar")) {
                try {
                    holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rar_icon));
                    holder.fileNameTV.setText(filename);
                    holder.fileDescriptionTV.setText(name);
                } catch (Exception e) {

                }
            } else if (filename.contains(".zip")) {
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
                    list.add("Edit");
                    list.add("Delete");
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
                                        refreshList();
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
                                mDataset.remove(position);
                                DocumentUploadAdapter.this.notifyDataSetChanged();
                                if (mDataset.size() == 0) {
                                    errorTV.setVisibility(View.VISIBLE);
                                }

                            }
                            builder.dismiss();
                        }
                    });
                    customBuilder.show();
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private void setAdvanceData() {
        String amount = amountET.getText().toString();
        String remarks = remarksET.getText().toString();
        String reason = reasonTV.getText().toString();
        String currency = currencyTV.getText().toString();
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        String empId = loginUserModel.getUserModel().getEmpId();
        String requestId = String.valueOf(0);
        String fromButton = "Submit";
        String approvalLevel = "0";
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (reasonCodeModel == null) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            isClicked = false;
            new AlertCustomDialog(context, "Please Select Reason");
            return;
        } else if (currencyListModel == null) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            isClicked = false;
            new AlertCustomDialog(context, "Please Select Currency");
            return;
        } else if (amount.equalsIgnoreCase("")) {
            progressBar.setVisibility(View.GONE);
            isClicked = false;
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new AlertCustomDialog(context, "Please Enter Amount");
            return;
        } else {
            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    SupportDocsItemModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    uploadFileList.set(i, model);
                }
            }
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getAdvanceRequestData(fromButton, requestId, approvalLevel, remarks, amount, currencyValue, empId, reasonCode, reason, uploadFileList),
                    CommunicationConstant.API_GET_SAVE_ADVANCE, true);

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
        InputStream inputStream = null;
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

}
