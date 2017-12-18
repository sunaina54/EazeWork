package hr.eazework.com.ui.fragment;

import android.app.DatePickerDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.vision.text.Line;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hr.calender.caldroid.CaldroidFragment;
import hr.eazework.com.AddExpenseActivity;
import hr.eazework.com.FileUtils;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.CategoryListResponseModel;
import hr.eazework.com.model.ExpenseImageList;
import hr.eazework.com.model.GetCategoryListResultModel;
import hr.eazework.com.model.GetHeadDetailsWithPolicyResultModel;
import hr.eazework.com.model.HeadCategoryListModel;
import hr.eazework.com.model.HeadsItemModel;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.MonthListItem;
import hr.eazework.com.model.PeriodicExpenseResponseModel;
import hr.eazework.com.model.SaveExpenseItem;
import hr.eazework.com.model.SaveExpenseModel;
import hr.eazework.com.model.SaveExpenseRequestModel;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.VisibilityDataModel;
import hr.eazework.com.model.VisibilityResponseModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.SharedPreference;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

import static android.app.Activity.RESULT_OK;
import static hr.eazework.com.ui.util.ImageUtil.rotateImage;

/**
 * Created by Dell3 on 10-08-2017.
 */

public class AddExpenseFragment extends MyBaseFragment {
    public static final String TAG = "AddExpenseFragment";
    private boolean isComingFromEdit;
    private Preferences preferences;
    private LinearLayout dynamicLayout;
    private RecyclerView expenseRecyclerView;
    private ImageView plus_create_newIV;
    private Context context;
    private String purpose = "";
    private ArrayList<ExpenseImageList> imageList;
    private Bitmap bitmap = null;
    private DatePickerDialog datePickerDialog1, datePickerDialog2, datePickerDialog3;
    private TextView tv_to_day, tv_to_date, tv_from_date, tv_from_day, categoryTypeTV, categoryHeadTV, tv_from_Month, tv_from_Year;
    private TextView tv_from_date_top;
    private CategoryListResponseModel categoryListResponseModel;
    private HeadCategoryListModel headCategoryList;
    private int category;
    private LinearLayout claimHeadLinearLayout, fromDateLinearLayout, todateLinearLayout;
    private RelativeLayout saveLl;
    private LinearLayout calendarLinearLayout, inputLinearLayout, amountLinearLayout, periodLinearLayout;
    private ArrayList<HeadsItemModel> selectedCategoryHeads;
    private HeadsItemModel headsItemModel;
    private String headId, inputValue = "";
    private VisibilityResponseModel visibilityResponseModel;
    private EditText detailsET, inputET, amountET;
    private GetHeadDetailsWithPolicyResultModel model;
    private static int UPLOAD_DOC_REQUEST = 1;
    private ArrayList<SupportDocsItemModel> uploadFileList;
    private LinearLayout errorTV;
    private ProgressBar progressBar;
    private String categoryDesc, headDesc;
    private static ArrayList<HeadCategoryListModel> headCategoryListModels;
    public static SaveExpenseRequestModel expenseRequestModel;
    private GetCategoryListResultModel getCategoryListResultModel;
    private LineItemsModel lineItemList;
    private TextView inputTV, periodTV, amountTV, inputRightTV;
    private String labelInput, labelAmount, labelPeriod, labelRight, periodDate;
    int reqId, empId;
    private LinearLayout rl_edit_team_member;
    private View rootView;


    public SaveExpenseRequestModel getExpenseRequestModel() {
        return expenseRequestModel;
    }

    public void setExpenseRequestModel(SaveExpenseRequestModel expenseRequestModel) {
        this.expenseRequestModel = expenseRequestModel;
    }


    public LineItemsModel getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(LineItemsModel lineItemList) {
        this.lineItemList = lineItemList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_fragment, container, false);
        context = getActivity();
        imageList = new ArrayList<>();
        preferences = new Preferences(getActivity());
        if (lineItemList != null && lineItemList.getDocListLineItem() != null) {
            uploadFileList = lineItemList.getDocListLineItem();
        } else {
            uploadFileList = new ArrayList<SupportDocsItemModel>();
        }

        saveLl = (RelativeLayout) rootView.findViewById(R.id.saveLl);
        saveLl.setVisibility(View.GONE);
        empId = expenseRequestModel.getExpense().getExpenseItem().getForEmpID();
        if (expenseRequestModel!=null && expenseRequestModel.getExpense()!=null &&
                expenseRequestModel.getExpense().getExpenseItem()!=null && expenseRequestModel.getExpense().getExpenseItem().getReqID() != null) {
            reqId = Integer.parseInt(expenseRequestModel.getExpense().getExpenseItem().getReqID());
        }
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        progressBar.setVisibility(View.GONE);
        categoryTypeTV = (TextView) rootView.findViewById(R.id.categoryTypeTV);
        categoryHeadTV = (TextView) rootView.findViewById(R.id.categoryHeadTV);
        categoryHeadTV.setOnClickListener(this);
        categoryTypeTV.setOnClickListener(this);
        claimHeadLinearLayout = (LinearLayout) rootView.findViewById(R.id.claimHeadLinearLayout);
        fromDateLinearLayout = (LinearLayout) rootView.findViewById(R.id.fromDateLinearLayout);
        todateLinearLayout = (LinearLayout) rootView.findViewById(R.id.todateLinearLayout);
        todateLinearLayout.setVisibility(View.GONE);
        fromDateLinearLayout.setVisibility(View.GONE);
        inputTV = (TextView) rootView.findViewById(R.id.inputTV);
        periodTV = (TextView) rootView.findViewById(R.id.periodTV);
        amountTV = (TextView) rootView.findViewById(R.id.amountTV);
        inputET = (EditText) rootView.findViewById(R.id.inputET);
        inputRightTV = (TextView) rootView.findViewById(R.id.inputRightTV);
        amountET = (EditText) rootView.findViewById(R.id.amountET);
        tv_from_Year = (TextView) rootView.findViewById(R.id.tv_from_Year);
        tv_from_Month = (TextView) rootView.findViewById(R.id.tv_from_Month);
        calendarLinearLayout = (LinearLayout) rootView.findViewById(R.id.calendarLinearLayout);
        inputLinearLayout = (LinearLayout) rootView.findViewById(R.id.inputLinearLayout);
        amountLinearLayout = (LinearLayout) rootView.findViewById(R.id.amountLinearLayout);
        periodLinearLayout = (LinearLayout) rootView.findViewById(R.id.periodLinearLayout);

        errorTV = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorTV.setVisibility(View.VISIBLE);

        detailsET = (EditText) rootView.findViewById(R.id.detailsET);
        tv_from_date_top = (TextView) rootView.findViewById(R.id.tv_from_date_top);
        tv_to_day = ((TextView) rootView.findViewById(R.id.tv_to_day));
        tv_to_date = ((TextView) rootView.findViewById(R.id.tv_to_date));
        tv_from_day = ((TextView) rootView.findViewById(R.id.tv_from_day));
        tv_from_date = ((TextView) rootView.findViewById(R.id.tv_from_date));
        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_to_date, tv_to_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);
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

        int textColor = Utility.getTextColorCode(preferences);
        int headerColr = Utility.getBgColorCode(context, preferences);
        rl_edit_team_member = (LinearLayout) rootView.findViewById(R.id.rl_edit_team_member);
        ImageView ibRight = (ImageView) rootView.findViewById(R.id.ibRight);
        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmitOperation();
            }
        });
        ImageView ibWrongh = (ImageView) rootView.findViewById(R.id.ibWrong);
        ibWrongh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rl_edit_team_member.setBackgroundColor(headerColr);
        ((TextView) rootView.findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) rootView.findViewById(R.id.tv_header_text)).setText("Add Expense");
        final FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.flCapture);
        frameLayout.setVisibility(View.GONE);
        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);
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
                                        PermissionUtil.askAllPermissionCamera(AddExpenseFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), AddExpenseFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", TAG);
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
        sendExpenseCategoryData();


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.categoryTypeTV:
                Log.d("TAG", "Category List Model : " + categoryListResponseModel.serialize());
                if (categoryListResponseModel != null) {
                    getCategoryListResultModel = categoryListResponseModel.getGetCategoryListResult();
                    headCategoryListModels = getCategoryListResultModel.getHeadCategoryList();
                    CustomBuilder categoryDialog = new CustomBuilder(getContext(), "Select Category", true);
                    categoryDialog.setSingleChoiceItems(headCategoryListModels, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            headCategoryList = (HeadCategoryListModel) selectedObject;
                            categoryTypeTV.setText(headCategoryList.getCategoryDesc());
                            categoryDesc = headCategoryList.getCategoryDesc();
                            category = headCategoryList.getCategory();

                            if (lineItemList != null && lineItemList.getLineItemID() != 0 && lineItemList.getCategoryID() == category) {

                            } else {
                                if (lineItemList == null) {
                                    lineItemList = new LineItemsModel();
                                }
                                lineItemList.setCategoryID(category);
                                categoryHeadTV.setText("Select Claim Head");
                                selectedCategoryHeads = headCategoryList.getHeads();
                                tv_to_date.setText("--/--/----");
                                tv_from_date.setText("--/--/----");
                                calendarLinearLayout.setVisibility(View.GONE);
                                inputLinearLayout.setVisibility(View.GONE);
                                amountLinearLayout.setVisibility(View.GONE);
                                if (category == 1) {
                                    todateLinearLayout.setVisibility(View.VISIBLE);
                                    fromDateLinearLayout.setVisibility(View.VISIBLE);
                                    tv_from_date_top.setText("From Date");
                                    tv_from_day.setText("From Date");
                                } else {
                                    todateLinearLayout.setVisibility(View.GONE);
                                    fromDateLinearLayout.setVisibility(View.VISIBLE);
                                    tv_from_date_top.setText("Claim Date");
                                    tv_from_day.setText("Claim Date");
                                }
                            }
                            builder.dismiss();
                        }
                    });
                    categoryDialog.show();
                }
                break;

            case R.id.categoryHeadTV:
                if (headCategoryList != null && selectedCategoryHeads != null && selectedCategoryHeads.size() > 0) {
                    CustomBuilder categoryHeadDialog = new CustomBuilder(getContext(), "Select Category Head", true);
                    categoryHeadDialog.setSingleChoiceItems(selectedCategoryHeads, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {

                            headsItemModel = (HeadsItemModel) selectedObject;
                            categoryHeadTV.setText(headsItemModel.getHeadDesc());
                            if (lineItemList != null && lineItemList.getLineItemID() != 0 && lineItemList.getHeadID().equalsIgnoreCase(headsItemModel.getHeadID())) {

                            } else {
                                headDesc = headsItemModel.getHeadDesc();
                                headId = headsItemModel.getHeadID();
                                lineItemList.setHeadDesc(headDesc);
                                lineItemList.setHeadID(headId);
                                sendHeadDetailsWithPolicyData();
                            }
                            builder.dismiss();
                        }
                    });
                    categoryHeadDialog.show();
                } else {
                    new AlertCustomDialog(context, "Please Select Category");
                    return;
                }
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    private void doSubmitOperation() {
        String fromDate = "", toDate = "";
        String amountValue = "";
        String details = detailsET.getText().toString();
        if (amountET != null)
            amountValue = amountET.getText().toString();
        if (inputET != null) {
            inputValue = inputET.getText().toString();
        }
        if (headCategoryList == null) {
            progressBar.setVisibility(View.GONE);
            new AlertCustomDialog(context, "Please Select Category");
            return;
        }
        if (!(String.valueOf(category).equalsIgnoreCase(""))) {
            if (category == 1) {
                fromDate = tv_from_date.getText().toString();
                toDate = tv_to_date.getText().toString();
                if (fromDate.equalsIgnoreCase("") || fromDate.equalsIgnoreCase("--/--/----")) {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please Enter From Date");
                    return;
                }
                if (toDate.equalsIgnoreCase("") || toDate.equalsIgnoreCase("--/--/----")) {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please Enter To Date");
                    return;
                }
            } else {

                toDate = tv_from_date.getText().toString();
                if (toDate.equalsIgnoreCase("") || toDate.equalsIgnoreCase("--/--/----")) {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please Enter Claim Date");
                    return;
                }
            }
        }
        if ((!fromDate.equalsIgnoreCase("") && !fromDate.equalsIgnoreCase("--/--/----")) &&
                (!toDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("--/--/----"))) {

            if (!CalenderUtils.isDateGreater(fromDate, toDate)) {
                progressBar.setVisibility(View.GONE);
                new AlertCustomDialog(context, "To date must be greater from date");
                return;
            }
        }

        if (headsItemModel == null) {
            progressBar.setVisibility(View.GONE);
            new AlertCustomDialog(context, "Please select claim head");
            return;
        }
        if (visibilityResponseModel != null && visibilityResponseModel.getGetHeadDetailsWithPolicyResult() != null
                && visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getShowInput().equalsIgnoreCase("Y")) {
            if (inputValue.equalsIgnoreCase("")) {
                progressBar.setVisibility(View.GONE);
                new AlertCustomDialog(context, "Please enter input value");
                return;
            }
            if (amountValue.equalsIgnoreCase("")) {
                progressBar.setVisibility(View.GONE);
                new AlertCustomDialog(context, "Please enter amount");
                return;
            }
        } else if (visibilityResponseModel != null && visibilityResponseModel.getGetHeadDetailsWithPolicyResult() != null
                && visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getShowInput().equalsIgnoreCase("N")) {
            if (amountValue.equalsIgnoreCase("")) {
                progressBar.setVisibility(View.GONE);
                new AlertCustomDialog(context, "Please enter amount");
                return;
            }
        }

        if (details.equalsIgnoreCase("")) {
            progressBar.setVisibility(View.GONE);
            new AlertCustomDialog(context, "Please enter details");
            return;
        }


        if (lineItemList != null) {

        } else {
            lineItemList = new LineItemsModel();
            lineItemList.setPolicyID(visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getPolicyID());
            lineItemList.setUnit(model.getUnit());
        }

        if (visibilityResponseModel != null) {
            lineItemList.setPolicyID(visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getPolicyID());
            lineItemList.setPolicyLimitValue(String.valueOf(visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getLimitedTo()));
            lineItemList.setUnit(visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getUnitDesc());
            if (visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getVisibility().get(0)
                    .getFieldTypeID().equalsIgnoreCase("MonthCalendar")) {
                if ((tv_from_Month.getText().toString().equalsIgnoreCase("") ||
                        tv_from_Month.getText().toString().equalsIgnoreCase("Month")) &&
                        (tv_from_Year.getText().toString().equalsIgnoreCase("") ||
                                tv_from_Year.getText().toString().equalsIgnoreCase("Year"))) {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please enter period");
                    return;
                }
            }
        } else {
            Log.d("TAG", "label" + lineItemList.getLabelPeriod());
            if (lineItemList.getLabelPeriod() != null && lineItemList.getLabelPeriod().equalsIgnoreCase("Period")) {
                if ((tv_from_Month.getText().toString().equalsIgnoreCase("") ||
                        tv_from_Month.getText().toString().equalsIgnoreCase("Month")) &&
                        (tv_from_Year.getText().toString().equalsIgnoreCase("") ||
                                tv_from_Year.getText().toString().equalsIgnoreCase("Year"))) {
                    progressBar.setVisibility(View.GONE);
                    new AlertCustomDialog(context, "Please enter period");
                    return;
                }
            }

        }

        if (category == 1) {
            lineItemList.setDateFrom(fromDate);
            lineItemList.setDateTo(toDate);
        } else {
            lineItemList.setDateFrom(toDate);
        }

        lineItemList.setCategoryID(category);
        lineItemList.setHeadID(headId);


        if (visibilityResponseModel != null && visibilityResponseModel.getGetHeadDetailsWithPolicyResult() != null
                && visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getShowInput().equalsIgnoreCase("Y")) {
            inputLinearLayout.setVisibility(View.VISIBLE);
            amountLinearLayout.setVisibility(View.VISIBLE);
            lineItemList.setClaimAmt(amountValue);
            lineItemList.setInputUnit(inputValue);
            lineItemList.setLabelInput(labelInput);
            lineItemList.setLabelRight(labelRight);
            lineItemList.setLabelAmount(labelAmount);

            if (visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getVisibility().get(0).getFieldTypeID().equalsIgnoreCase("MonthCalendar")) {
                lineItemList.setLabelPeriod(labelPeriod);
                lineItemList.setDateTo(periodDate);
            }
        } else if (visibilityResponseModel != null && visibilityResponseModel.getGetHeadDetailsWithPolicyResult() != null
                && visibilityResponseModel.getGetHeadDetailsWithPolicyResult().getShowInput().equalsIgnoreCase("N")) {
            inputLinearLayout.setVisibility(View.GONE);
            amountLinearLayout.setVisibility(View.VISIBLE);
            lineItemList.setClaimAmt(amountValue);
            lineItemList.setLabelAmount(labelAmount);

        }

        lineItemList.setClaimAmt(amountValue);


        if (!inputET.getText().toString().equalsIgnoreCase("") && !inputValue.equalsIgnoreCase("")) {
            lineItemList.setInputUnit(inputValue);
        }
        lineItemList.setLineItemDetail(details);
        lineItemList.setCategoryDesc(categoryDesc);
        lineItemList.setHeadDesc(headDesc);
        if (uploadFileList != null && uploadFileList.size() > 0) {
            for (int i = 0; i < uploadFileList.size(); i++) {
                SupportDocsItemModel model = uploadFileList.get(i);
                model.setSeqNo(i + 1);
                uploadFileList.set(i, model);
            }
            lineItemList.setDocListLineItem(uploadFileList);
        }

        lineItemList.setAmount2(amountValue);
        Log.d("AddExpense", "AddExpense : " + lineItemList.serialize());

        ArrayList<LineItemsModel> lineItem;
        int dataSize = 0;
        if (expenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {
            lineItem = expenseRequestModel.getExpense().getExpenseItem().getLineItems();
            dataSize = lineItem.size();
            System.out.print(dataSize);

        } else {
            lineItem = new ArrayList<>();
        }

        if (lineItem != null) {
            LineItemsModel tempModel = null;
            for (int i = 0; i < lineItem.size(); i++) {

                if (lineItem.get(i).getLineItemID() == lineItemList.getLineItemID() && !lineItem.get(i).getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {
                    tempModel = lineItem.get(i);
                    if (lineItemList.getLineItemID() != 0) {
                        lineItemList.setLineItemID(lineItemList.getLineItemID());
                    }
                    lineItem.set(i, lineItemList);
                    break;
                }

                if (lineItem.get(i).getI() == lineItemList.getI() && lineItemList.getFlag() != null && lineItemList.getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {
                    tempModel = lineItem.get(i);
                    lineItemList.setLineItemID(0);
                    lineItemList.setFlag("N");
                    lineItem.set(i, lineItemList);
                    break;
                }

            }
            if (tempModel == null) {
                lineItemList.setLineItemID(0);
                lineItemList.setFlag("N");
                lineItemList.setI(lineItem.size() + 1);
                lineItem.add(lineItemList);
            }

        }
        dataSize = lineItem.size();
        System.out.print(dataSize);
        ArrayList<AdvanceListItemModel> avanceListItem;

        if (expenseRequestModel.getExpense().getExpenseItem().getAdvanceList() != null) {
            avanceListItem = expenseRequestModel.getExpense().getExpenseItem().getAdvanceList();
        } else {
            avanceListItem = new ArrayList<AdvanceListItemModel>();
        }


        SaveExpenseItem saveExpenseItem;// = new SaveExpenseItem();
        if (expenseRequestModel.getExpense().getExpenseItem() != null) {
            saveExpenseItem = expenseRequestModel.getExpense().getExpenseItem();
        } else {
            saveExpenseItem = new SaveExpenseItem();
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getDescription() != null) {

            saveExpenseItem.setDescription(expenseRequestModel.getExpense().getExpenseItem().getDescription());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc() != null) {

            saveExpenseItem.setClaimTypeDesc(expenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc());
            saveExpenseItem.setClaimTypeID(expenseRequestModel.getExpense().getExpenseItem().getClaimTypeID());
        }

        if (expenseRequestModel.getExpense().getExpenseItem().getApproverID() != null) {

            saveExpenseItem.setApproverName(expenseRequestModel.getExpense().getExpenseItem().getApproverName());
            saveExpenseItem.setApproverID(expenseRequestModel.getExpense().getExpenseItem().getApproverID());
        }

        if (expenseRequestModel.getExpense().getExpenseItem().getEmpName() != null) {

            saveExpenseItem.setEmpName(expenseRequestModel.getExpense().getExpenseItem().getEmpName());
            saveExpenseItem.setForEmpID(expenseRequestModel.getExpense().getExpenseItem().getForEmpID());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getCurrencyCode() != null) {

            saveExpenseItem.setCurrencyCode(expenseRequestModel.getExpense().getExpenseItem().getCurrencyCode());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getReqRemark() != null) {

            saveExpenseItem.setReqRemark(expenseRequestModel.getExpense().getExpenseItem().getReqRemark());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getTotalExpenseClaimedAmount() != null) {

            saveExpenseItem.setTotalExpenseClaimedAmount(expenseRequestModel.getExpense().getExpenseItem().getTotalExpenseClaimedAmount());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getNetAmount() != null) {

            saveExpenseItem.setNetAmount(expenseRequestModel.getExpense().getExpenseItem().getNetAmount());
        }
        if (expenseRequestModel.getExpense().getExpenseItem().getDocList() != null) {

            saveExpenseItem.setDocList(expenseRequestModel.getExpense().getExpenseItem().getDocList());
        }

        if (expenseRequestModel.getExpense().getExpenseItem().getProjectName() != null) {

            saveExpenseItem.setProjectName(expenseRequestModel.getExpense().getExpenseItem().getProjectName());
            saveExpenseItem.setProjectID(expenseRequestModel.getExpense().getExpenseItem().getProjectID());
        }

        saveExpenseItem.setAdvanceList(avanceListItem);
        saveExpenseItem.setLineItems(lineItem);
        SaveExpenseModel saveExpModel;

        if (expenseRequestModel.getExpense() != null) {
            saveExpModel = expenseRequestModel.getExpense();
        } else {
            saveExpModel = new SaveExpenseModel();
        }
        saveExpModel.setExpenseItem(saveExpenseItem);
        expenseRequestModel.setExpense(saveExpModel);
        Intent theIntent = new Intent();
        getActivity().setResult(AddExpenseActivity.REQUEST_CODE, theIntent);
        getActivity().finish();
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
            final Uri uri = data.getData();
            String encodeFileToBase64Binary = null;
            if (data != null) {
                String path = data.getStringExtra("path");
                System.out.print(path);
                Uri uploadedFilePath = data.getData();
                String filename = getFileName(uploadedFilePath);
                String fileDesc = getFileName(uploadedFilePath);
                String[] extList = filename.split("\\.");
                System.out.print(extList[1].toString());
                String extension = "." + extList[extList.length - 1];

                List<String> extensionList = Arrays.asList(expenseRequestModel.getExpense().getExpenseItem().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, expenseRequestModel.getExpense().getExpenseItem().getDocValidation().getMessage());
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

                refreshList();
                Log.d("encodedFile", encodeFileToBase64Binary);


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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final SupportDocsItemModel fileObject = mDataset.get(position);
            if (fileObject.getDocID() != 0 && fileObject.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)) {

            } else {
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
                    holder.img_icon.setImageDrawable((context.getResources().getDrawable(R.drawable.jpeg_icon)));
                    holder.fileNameTV.setText(filename);
                    holder.fileDescriptionTV.setText(name);

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
                        if (fileObject.getDocID() != 0) {
                            list.add("Edit");
                            list.add("Delete");
                            list.add("Download");
                        } else {
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
                                    if (fileObject.getDocID() != 0) {
                                        fileObject.setFlag(AppsConstant.DELETE_FLAG);
                                        uploadFileList.set(uploadFileList.indexOf(fileObject), fileObject);
                                    } else {
                                        uploadFileList.remove(position);
                                    }
                                    refreshList();
                                    if (mDataset.size() == 0) {
                                        errorTV.setVisibility(View.VISIBLE);
                                    }

                                } else if (selectedObject.toString().equalsIgnoreCase("Download")) {

                                    if (fileObject.getDocID() == 0) {
                                        return;
                                    }
                                    String filePath = fileObject.getDocPath().replace("~", "");
                                    String path = CommunicationConstant.UrlFile + filePath + "/" + fileObject.getDocFile();

                                    Utility.downloadPdf(path, null, fileObject.getDocFile(), context, getActivity());
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


    public void sendExpenseCategoryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getCategoryData(empId, reqId),
                CommunicationConstant.API_GET_CATEGORY_LIST_DETAILS, true);
    }


    public void sendHeadDetailsWithPolicyData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getHeadDetailsWithPolicyData(empId, category, headId, expenseRequestModel.getExpense().getExpenseItem().getCurrencyCode(), reqId),
                CommunicationConstant.API_GET_HEAD_DETAILS_WITH_POLICY, true);
    }

    private void callApiForSetEditedData() {
        category = lineItemList.getCategoryID();
        headId = lineItemList.getHeadID();
        isComingFromEdit = true;
        sendHeadDetailsWithPolicyData();
    }

    private void setEditedData() {
        if (lineItemList != null && lineItemList.getLineItemDetail() != null) {
            detailsET.setText(lineItemList.getLineItemDetail());
        }
        for (int i = 0; i < headCategoryListModels.size(); i++) {
            if (lineItemList.getCategoryID() == headCategoryListModels.get(i).getCategory()) {
                headCategoryList = headCategoryListModels.get(i);
                categoryTypeTV.setText(headCategoryList.getCategoryDesc());
                categoryDesc = headCategoryList.getCategoryDesc();
                category = headCategoryList.getCategory();
                selectedCategoryHeads = headCategoryList.getHeads();
                if (lineItemList != null && lineItemList.getLineItemID() == 0) {
                    if (category == 1) {
                        todateLinearLayout.setVisibility(View.VISIBLE);
                        fromDateLinearLayout.setVisibility(View.VISIBLE);
                        tv_from_date_top.setText("From Date");
                        tv_from_date.setText(lineItemList.getDateFrom());
                        tv_to_date.setText(lineItemList.getDateTo());

                    } else {
                        todateLinearLayout.setVisibility(View.GONE);
                        fromDateLinearLayout.setVisibility(View.VISIBLE);
                        tv_from_date_top.setText("Claim Date");
                        tv_from_day.setText("Claim Date");
                        tv_from_date.setText(lineItemList.getDateFrom());
                    }
                } else {
                    if (category == 1) {
                        todateLinearLayout.setVisibility(View.VISIBLE);
                        fromDateLinearLayout.setVisibility(View.VISIBLE);
                        tv_from_date_top.setText("From Date");
                        String[] dateFrom = lineItemList.getDateFrom().split(" ");
                        String[] dateTo = lineItemList.getDateTo().split(" ");
                        tv_from_date.setText(dateFrom[0]);
                        tv_to_date.setText(dateTo[0]);

                    } else {
                        todateLinearLayout.setVisibility(View.GONE);
                        fromDateLinearLayout.setVisibility(View.VISIBLE);
                        tv_from_date_top.setText("Claim Date");
                        tv_from_day.setText("Claim Date");
                        String[] dateTo = lineItemList.getDateFrom().split(" ");
                        tv_from_date.setText(dateTo[0]);
                    }
                }

                if (selectedCategoryHeads != null && selectedCategoryHeads.size() > 0) {
                    for (int j = 0; j < selectedCategoryHeads.size(); j++) {
                        if (lineItemList.getHeadID().equalsIgnoreCase(selectedCategoryHeads.get(j).getHeadID())) {
                            headsItemModel = selectedCategoryHeads.get(j);
                            categoryHeadTV.setText(headsItemModel.getHeadDesc());
                            headDesc = headsItemModel.getHeadDesc();
                            headId = headsItemModel.getHeadID();
                            setEditedVisibilityData();
                        }
                    }
                }

                break;
            }
        }


        if (lineItemList != null && lineItemList.getDocListLineItem() != null && lineItemList.getDocListLineItem().size() > 0) {
            expenseRecyclerView.setVisibility(View.VISIBLE);
            errorTV.setVisibility(View.GONE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(lineItemList.getDocListLineItem());
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorTV.setVisibility(View.VISIBLE);
            expenseRecyclerView.setVisibility(View.GONE);
        }

    }

    private void setEditedVisibilityData() {
        if (lineItemList.getInputUnit() != null && lineItemList.getInputUnit().equalsIgnoreCase("")) {
            lineItemList.setInputUnit("0.00");
        }
        if (lineItemList.getInputUnit() != null && lineItemList.getClaimAmt() != null
                && !lineItemList.getInputUnit().equalsIgnoreCase("0.00")) {

            inputLinearLayout.setVisibility(View.VISIBLE);
            amountLinearLayout.setVisibility(View.VISIBLE);

            inputTV.setText(lineItemList.getLabelInput());
            inputRightTV.setText(lineItemList.getLabelRight());
            amountTV.setText(lineItemList.getLabelAmount());
            inputET.setText(lineItemList.getInputUnit());

            amountET.setText(lineItemList.getClaimAmt());

            if (model.getExceedingPolicyYN().equalsIgnoreCase("N")) {
                amountET.setEnabled(false);
            } else if (model.getExceedingPolicyYN().equalsIgnoreCase("Y")) {
                amountET.setEnabled(true);
            }

            inputET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputValue = inputET.getText().toString();

                }

                @Override
                public void afterTextChanged(Editable s) {
                    double amountValue = 0;
                    if (inputValue != null && !inputValue.equalsIgnoreCase("") && lineItemList.getInputUnit() != null) {

                        if (model.getPolicyID() != null && !model.getPolicyID().equalsIgnoreCase("")) {
                            amountValue = Double.parseDouble(inputValue) * model.getLimitedTo().doubleValue();
                        } else {
                            amountValue = Double.parseDouble(inputValue);
                        }
                       /* amountValue = Double.parseDouble(inputValue) * model.getLimitedTo().doubleValue();
                        amountET.setText(amountValue + "");*/
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        String tempStr = twoDForm.format(amountValue);
                        amountValue = Double.parseDouble(tempStr);
                        amountET.setText(amountValue + "");
                    } else {
                        amountET.setText("");

                    }
                }
            });

        } else {
            inputLinearLayout.setVisibility(View.GONE);
            amountLinearLayout.setVisibility(View.VISIBLE);
            amountTV.setText(lineItemList.getLabelAmount());
            amountET.setText(lineItemList.getClaimAmt());

        }

        if (lineItemList.getLabelPeriod() != null && lineItemList.getLabelPeriod().equalsIgnoreCase("Period")) {
            calendarLinearLayout.setVisibility(View.VISIBLE);
            periodTV.setText(lineItemList.getLabelPeriod());
            String[] periodDate1 = lineItemList.getDateTo().split("-");
            tv_from_Year.setText(periodDate1[0]);
            tv_from_Month.setText(periodDate1[1]);
            periodDate = lineItemList.getDateTo();

        }
    }


    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_CATEGORY_LIST_DETAILS:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                categoryListResponseModel = CategoryListResponseModel.create(str);
                headCategoryListModels = categoryListResponseModel.getGetCategoryListResult().getHeadCategoryList();
                if (headCategoryListModels != null && headCategoryListModels.size() > 0) {
                    if (lineItemList != null && lineItemList.getLineItemID() == 0) {
                        callApiForSetEditedData();
                    } else if (lineItemList != null) {
                        callApiForSetEditedData();
                    }
                }
                break;
            case CommunicationConstant.API_GET_HEAD_DETAILS_WITH_POLICY:
                String strResponse = response.getResponseData();
                Log.d("TAG", "Advance Response : " + strResponse);
                try {
                    visibilityResponseModel = VisibilityResponseModel.create(strResponse);
                    Log.d("TAG", "Advance Response : " + visibilityResponseModel.serialize());
                } catch (Exception e) {
                    Log.d("TAG", "Advance Response : " + e.toString());
                }
                if (isComingFromEdit) {
                    isComingFromEdit = false;
                    setLabelForEditedData(visibilityResponseModel);
                    setEditedData();

                } else {
                    if (visibilityResponseModel != null && visibilityResponseModel.getGetHeadDetailsWithPolicyResult() != null) {
                        addLayout(visibilityResponseModel);
                    }
                }
                break;


            default:
                break;
        }
        super.validateResponse(response);
    }

    private void setLabelForEditedData(VisibilityResponseModel responseModel) {
        if (responseModel != null) {
            model = responseModel.getGetHeadDetailsWithPolicyResult();
            if (model != null && model.getShowInput().equalsIgnoreCase("Y")) {
                for (VisibilityDataModel visibility : model.getVisibility()) {
                    if (visibility.getFieldTypeID() != null && visibility.getFieldTypeID().equalsIgnoreCase("MonthCalendar")) {
                        labelPeriod = visibility.getFieldLabel();
                        datePickerDialog3 = pickYearMonthFromCalender(context, AppsConstant.DATE_YEAR_MONTH_FORMAT);// CalenderUtils.pickYearMonthFromCalender(context, tv_from_Year, tv_from_Month, AppsConstant.DATE_YEAR_MONTH_FORMAT);
                        periodLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datePickerDialog3.show();
                            }
                        });
                    } else if (visibility.getFieldTypeID() != null && visibility.getFieldTypeID().equalsIgnoreCase("TextBox")) {

                        if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtInput")) {
                            labelInput = visibility.getFieldLabel();
                            labelRight = visibility.getLabelRight();


                        } else if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtAmount")) {
                            labelAmount = visibility.getFieldLabel();
                        }

                    }
                }

            } else if (model != null && model.getShowInput().equalsIgnoreCase("N")) {
                for (VisibilityDataModel visibility : model.getVisibility()) {
                    if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtAmount")) {
                        amountTV.setText(visibility.getFieldLabel());
                        amountET.setEnabled(true);
                        labelAmount = visibility.getFieldLabel();
                        amountLinearLayout.setVisibility(View.VISIBLE);
                        inputLinearLayout.setVisibility(View.GONE);
                    }
                }
            }
        }

        lineItemList.setLabelInput(labelInput);
        lineItemList.setLabelRight(labelRight);
        lineItemList.setLabelAmount(labelAmount);
        lineItemList.setLabelPeriod(labelPeriod);

    }

    private void addLayout(VisibilityResponseModel responseModel) {
        dynamicLayout = (LinearLayout) rootView.findViewById(R.id.dynamicLayout);
        calendarLinearLayout.setVisibility(View.GONE);
        inputLinearLayout.setVisibility(View.GONE);
        amountLinearLayout.setVisibility(View.GONE);
        amountET.setText("");
        inputValue = "";
        inputET.setText("");
        if (responseModel != null) {
            model = responseModel.getGetHeadDetailsWithPolicyResult();
            if (model != null && model.getShowInput().equalsIgnoreCase("Y")) {
                for (VisibilityDataModel visibility : model.getVisibility()) {
                    if (visibility.getFieldTypeID() != null && visibility.getFieldTypeID().equalsIgnoreCase("MonthCalendar")) {

                        calendarLinearLayout.setVisibility(View.VISIBLE);
                        periodTV.setText(visibility.getFieldLabel());
                        labelPeriod = visibility.getFieldLabel();
                        datePickerDialog3 = pickYearMonthFromCalender(context, AppsConstant.DATE_YEAR_MONTH_FORMAT);// CalenderUtils.pickYearMonthFromCalender(context, tv_from_Year, tv_from_Month, AppsConstant.DATE_YEAR_MONTH_FORMAT);
                        periodLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datePickerDialog3.show();
                            }
                        });

                    } else if (visibility.getFieldTypeID() != null && visibility.getFieldTypeID().equalsIgnoreCase("TextBox")) {

                        if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtInput")) {

                            inputLinearLayout.setVisibility(View.VISIBLE);
                            inputTV.setText(visibility.getFieldLabel());
                            labelInput = visibility.getFieldLabel();
                            inputRightTV.setText(visibility.getLabelRight());
                            labelRight = visibility.getLabelRight();
                            if (model.getExceedingPolicyYN().equalsIgnoreCase("N")) {
                                amountET.setEnabled(false);
                            } else if (model.getExceedingPolicyYN().equalsIgnoreCase("Y")) {
                                amountET.setEnabled(true);
                            }
                            inputET.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    inputValue = inputET.getText().toString();
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!inputValue.equalsIgnoreCase("")) {
                                        double amountValue = 0;
                                        if (model.getPolicyID() != null && !model.getPolicyID().equalsIgnoreCase("")) {
                                            amountValue = Double.parseDouble(inputValue) * model.getLimitedTo().doubleValue();
                                        } else {
                                            amountValue = Double.parseDouble(inputValue);
                                        }
                                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                                        String tempStr = twoDForm.format(amountValue);
                                        amountValue = Double.parseDouble(tempStr);
                                        amountET.setText(amountValue + "");
                                    } else {
                                        amountET.setText("");
                                    }
                                }
                            });


                        } else if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtAmount")) {

                            amountTV.setText(visibility.getFieldLabel());
                            labelAmount = visibility.getFieldLabel();
                            amountLinearLayout.setVisibility(View.VISIBLE);
                        }

                    }
                }

            } else if (model != null && model.getShowInput().equalsIgnoreCase("N")) {
                for (VisibilityDataModel visibility : model.getVisibility()) {
                    if (visibility.getFieldLabel() != null && visibility.getFieldCode().equalsIgnoreCase("txtAmount")) {
                        amountTV.setText(visibility.getFieldLabel());
                        amountET.setEnabled(true);
                        labelAmount = visibility.getFieldLabel();
                        amountLinearLayout.setVisibility(View.VISIBLE);
                        inputLinearLayout.setVisibility(View.GONE);
                    }

                }
            }
        }
    }

    public DatePickerDialog pickYearMonthFromCalender(Context mContext, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int yearr, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();
                calendar.set(yearr, monthOfYear, dayOfMonth);
                tv_from_Month.setText(String.format(Locale.US, "%tB", calendar));
                String formatedData = String.format("%1$tY", calendar);
                tv_from_Year.setText(formatedData);
                String year = "", month = "";
                year = tv_from_Year.getText().toString();
                month = tv_from_Month.getText().toString();
                month = month.substring(0, 3);
                tv_from_Month.setText(month);
                periodDate = month + "-" + year;
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }
}


