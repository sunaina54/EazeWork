package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.vision.text.Line;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.eazework.com.AddExpenseActivity;
import hr.eazework.com.FileUtils;
import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceAdjustmentResponseModel;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.CategoryLineItemLabelItem;
import hr.eazework.com.model.DocListModel;
import hr.eazework.com.model.ExpenseApprovalList;
import hr.eazework.com.model.ExpenseClaimResponseModel;
import hr.eazework.com.model.ExpensePaymentDetailsItem;
import hr.eazework.com.model.GetAdvanceDetailResultModel;
import hr.eazework.com.model.GetAdvanceListForExpenseResult;
import hr.eazework.com.model.GetExpensePageInitResponseModel;
import hr.eazework.com.model.LineItemColumnsItem;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PeriodicExpenseResponseModel;
import hr.eazework.com.model.RequestRemarksItem;
import hr.eazework.com.model.SaveExpenseItem;
import hr.eazework.com.model.SaveExpenseModel;
import hr.eazework.com.model.SaveExpenseRequestModel;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.ViewClaimSummaryResponseModel;
import hr.eazework.com.model.ViewExpenseItemModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.customview.PaymentAdapter;
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
 * Created by Dell3 on 29-09-2017.
 */

public class EditExpenseApprovalFragment extends BaseFragment {
    private double totalExpenseAmt = 0;
    private String screenName = "EditExpenseRequestFragment";
    String description = "", remarks = "",onBehalf, currency;
    private double totalAdvanceAdjustInCaseExpenseSumLesser;
    private double balanceAmt = 0;
    private PaymentAdapter paymentAdapter;
    private Context context;
    private ExpenseApprovalList expenseApprovalList;
    private TextView claimTypeTV,totalPaymentTV;
    private TextView currencyTV;
    private TextView projectTV, totalExpenseClaimedTV, netAmountTV;
    private TextView approverTV;
    private TextView onBehalfTV, requestTV,totalTV;
    private LinearLayout errorDocTV, requestVoucherLl,paymentLinearLayout;
    private EditText detailsET, remarksET;
    private LinearLayout projectLinearLayout, onBehalfLinearLayout, errorTV,totalPaymentLabelLl;
    private ImageView add_expenseIV, advance_expenseIV, plus_create_newIV;
    private static int UPLOAD_DOC_REQUEST = 1;
    private ViewExpenseClaimSummaryAdapter summaryAdapter;
    private RecyclerView expenseDetailsRecyclerView, advanceRV, documentRV, remarksRV;
    private LinearLayout expenseErrorLl, advanceErrorLinearLayout, errorLinearLayout,
            remarksLinearLayout, claimLinearLayout, approverLl;
    private static ProgressDialog progress;
    private ViewClaimSummaryResponseModel viewClaimSummaryResponseModel;
    private AdjustmentDetailAdapter adjustemntDetailAdapter;
    private ExpenseClaimResponseModel expenseClaimResponseModel;
    private Preferences preferences;
    private ArrayList<LineItemsModel> lineItemsList = new ArrayList<LineItemsModel>();
    private double totalAmountTobeAdjusted;
    private String fromButton;
    private Button rejectBTN, returnBTN, approvalBTN,withdrawBTN;
    private ArrayList<DocListModel> uploadFileList;
    private int empId = 0, claimTypeId = 0, projectId = 0;
    private String requestId = "", approverId = "";
    private String requestCode, reasonCode, amount;
    private SaveExpenseRequestModel saveExpenseRequestModel;
    private RecyclerView paymentRV;
    private ProgressBar progressBar;
    private Bitmap bitmap = null;
    private String purpose = "";
    private String approverName;
    private RemarksAdapter remarksAdapter;
    private GetExpensePageInitResponseModel expensePageInitResponseModel;
    private GetAdvanceDetailResultModel getAdvanceDetailResultModel;
    private AdvanceAdjustmentResponseModel advanceAdjustmentResponseModel;
    private LinearLayout advance_adjustment_Ll;
    private TextView voucherNoTV;


    public SaveExpenseRequestModel getSaveExpenseRequestModel() {
        return saveExpenseRequestModel;
    }

    public void setSaveExpenseRequestModel(SaveExpenseRequestModel saveExpenseRequestModel) {
        this.saveExpenseRequestModel = saveExpenseRequestModel;
    }

    public ExpenseApprovalList getExpenseApprovalList() {
        return expenseApprovalList;
    }

    public void setExpenseApprovalList(ExpenseApprovalList expenseApprovalList) {
        this.expenseApprovalList = expenseApprovalList;
    }

    private ArrayList<AdvanceListItemModel> advanceList;
    ArrayList<AdvanceListItemModel> advanceDropdownList;
    private PeriodicExpenseResponseModel periodicExpenseResponseModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_expense_approval_fragment, container, false);
        context = getContext();
        preferences = new Preferences(getContext());

        setUpData();
        return rootView;
    }

    private void setUpData() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);

        advance_adjustment_Ll= (LinearLayout) rootView.findViewById(R.id.advance_adjustment_Ll);
        advance_adjustment_Ll.setVisibility(View.GONE);

        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_ADVANCE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                advance_adjustment_Ll.setVisibility(View.VISIBLE);
            }
        }

        claimLinearLayout = (LinearLayout) rootView.findViewById(R.id.claimLinearLayout);
        claimLinearLayout.setVisibility(View.GONE);
        voucherNoTV=(TextView)rootView.findViewById(R.id.voucherNoTV);
        approverLl = (LinearLayout) rootView.findViewById(R.id.approverLl);
        approverLl.setVisibility(View.GONE);
        totalTV = (TextView) rootView.findViewById(R.id.totalAmountTV);
        add_expenseIV = (ImageView) rootView.findViewById(R.id.add_expenseIV);
        add_expenseIV.setVisibility(View.GONE);
        onBehalfTV = (TextView) rootView.findViewById(R.id.onBehalfTV);
        claimTypeTV = (TextView) rootView.findViewById(R.id.claimTypeTV);
        currencyTV = (TextView) rootView.findViewById(R.id.currencyTV);
        projectTV = (TextView) rootView.findViewById(R.id.projectTV);
        requestTV = (TextView) rootView.findViewById(R.id.requestTV);
        approverTV = (TextView) rootView.findViewById(R.id.approverTV);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);
        totalExpenseClaimedTV = (TextView) rootView.findViewById(R.id.totalExpenseClaimedTV);
        netAmountTV = (TextView) rootView.findViewById(R.id.netAmountTV);
        advance_expenseIV = (ImageView) rootView.findViewById(R.id.advance_expenseIV);
        projectLinearLayout = (LinearLayout) rootView.findViewById(R.id.projectLinearLayout);
        projectLinearLayout.setVisibility(View.GONE);
        detailsET = (EditText) rootView.findViewById(R.id.detailsET);
        totalExpenseClaimedTV = (TextView) rootView.findViewById(R.id.totalExpenseClaimedTV);
        expenseDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseDetailsRecyclerView);
        expenseErrorLl = (LinearLayout) rootView.findViewById(R.id.errorTV);
        expenseErrorLl.setVisibility(View.GONE);

        advanceRV = (RecyclerView) rootView.findViewById(R.id.advance_expenseRecyclerView);
        advanceErrorLinearLayout = (LinearLayout) rootView.findViewById(R.id.advanceErrorLinearLayout);
        advanceErrorLinearLayout.setVisibility(View.GONE);
        advanceRV.setVisibility(View.GONE);

        documentRV = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        documentRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);

        requestVoucherLl = (LinearLayout) rootView.findViewById(R.id.requestVoucherLl);
        requestVoucherLl.setVisibility(View.VISIBLE);
        requestTV = (TextView) rootView.findViewById(R.id.requestTV);


        paymentRV = (RecyclerView) rootView.findViewById(R.id.paymentRV);
        paymentLinearLayout = (LinearLayout) rootView.findViewById(R.id.paymentLinearLayout);
        paymentLinearLayout.setVisibility(View.VISIBLE);
        totalPaymentTV= (TextView) rootView.findViewById(R.id.totalPaymentTV);
        totalPaymentLabelLl= (LinearLayout) rootView.findViewById(R.id.totalPaymentLabelLl);
        totalPaymentLabelLl.setVisibility(View.GONE);

        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksLinearLayout.setVisibility(View.VISIBLE);
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
                                        PermissionUtil.askAllPermissionCamera(EditExpenseApprovalFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                                        Utility.openCamera(getActivity(), EditExpenseApprovalFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore", screenName);
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
        // old with edited data

        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList().size() > 0) {
            advanceList = saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList();
            advanceDropdownList = new ArrayList<AdvanceListItemModel>();//= saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList();
            for (AdvanceListItemModel item : saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList()) {
                advanceDropdownList.add(item);
            }
            refreshAdjustmentRecycle(advanceList);

        } else {
            advanceList = new ArrayList<AdvanceListItemModel>();
        }

        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getDocList() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getDocList().size() > 0) {
            uploadFileList = saveExpenseRequestModel.getExpense().getExpenseItem().getDocList();
            refreshList(uploadFileList);

        } else {
            uploadFileList = new ArrayList<DocListModel>();
        }

        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem() != null) {

            detailsET.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getDescription());

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc() != null &&
                    !saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc().equalsIgnoreCase("")) {
                claimLinearLayout.setVisibility(View.VISIBLE);
                claimTypeTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc());
                claimTypeId = saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID();
            }

            onBehalfTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getEmpName());
            currencyTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode());
            currency=saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode();

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getApproverID() != null) {
                approverTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getApproverName());
                approverLl.setVisibility(View.VISIBLE);
                approverId = String.valueOf(saveExpenseRequestModel.getExpense().getExpenseItem().getApproverID());

            }

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID() >0 ) {

                projectLinearLayout.setVisibility(View.VISIBLE);
                projectTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectName());
                projectId = saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID();

            }

            netAmountTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getNetAmount());
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getReqRemark() != null) {
                remarksET.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getReqRemark());
            }
            totalAmountTobeAdjusted = 0;
            if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                    && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {
                for (LineItemsModel itemsModel : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                    totalAmountTobeAdjusted = totalAmountTobeAdjusted + Double.parseDouble(itemsModel.getClaimAmt());
                }

                if (advanceDropdownList != null && advanceDropdownList.size()>0) {
                    advanceAdjustmentResponseModel = saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceAdjustmentResponseModel();
                    setAdvanceAdjustmentData();
                    for (AdvanceListItemModel item : advanceDropdownList) {
                        if (totalAmountTobeAdjusted != 0) {
                            if (item.getPaidAmount() != null) {
                                totalAmountTobeAdjusted = totalAmountTobeAdjusted - Double.parseDouble(item.getPaidAmount());
                            } else {
                                totalAmountTobeAdjusted = totalAmountTobeAdjusted - Double.parseDouble(item.getAdjAmount());
                            }
                        }
                    }
                } else {

                }
                refresh(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems());


            } else {
                expenseErrorLl.setVisibility(View.VISIBLE);

                expenseDetailsRecyclerView.setVisibility(View.GONE);
            }
        }

        rejectBTN = (Button) rootView.findViewById(R.id.rejectBTN);
        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Reject";
                sendExpenseClaimData();

            }
        });
        returnBTN = (Button) rootView.findViewById(R.id.returnBTN);
        returnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Return";
                sendExpenseClaimData();
            }
        });
        approvalBTN = (Button) rootView.findViewById(R.id.approvalBTN);
        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Approve";
                sendExpenseClaimData();
            }
        });

        withdrawBTN = (Button) rootView.findViewById(R.id.withdrawBTN);
        withdrawBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Withdraw";
                sendExpenseClaimData();
            }
        });
        sendExpenseInitData();
    }

    private void setupButtons(){
        if(saveExpenseRequestModel.getButtons()!=null){
            for(String button : saveExpenseRequestModel.getButtons() ){
                if(button.equalsIgnoreCase(AppsConstant.APPROVE)){
                    approvalBTN.setVisibility(View.VISIBLE);
                }
                if(button.equalsIgnoreCase(AppsConstant.RETURN)){
                    returnBTN.setVisibility(View.VISIBLE);
                }
                if(button.equalsIgnoreCase(AppsConstant.REJECT)){
                    rejectBTN.setVisibility(View.VISIBLE);
                }
                if(button.equalsIgnoreCase(AppsConstant.WITHDRAW)){
                    withdrawBTN.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setAdvanceAdjustmentData() {
        if (currency!=null && !currency.equalsIgnoreCase("")) {

            if (advanceAdjustmentResponseModel != null &&
                    advanceAdjustmentResponseModel.getGetAdvanceListForExpenseResult() != null) {
                requestTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final GetAdvanceListForExpenseResult getAdvanceListForExpenseResult = advanceAdjustmentResponseModel.getGetAdvanceListForExpenseResult();

                        if (getAdvanceListForExpenseResult != null && getAdvanceListForExpenseResult.getAdvanceList().size() > 0) {
                            final ArrayList<GetAdvanceDetailResultModel> advanceListtt = getAdvanceListForExpenseResult.getAdvanceList();

                            final CustomBuilder claimDialog = new CustomBuilder(getContext(), "Select Request Id", true);
                            claimDialog.setSingleChoiceItems(advanceListtt, null, new CustomBuilder.OnClickListener() {
                                @Override
                                public void onClick(CustomBuilder builder, Object selectedObject) {

                                    getAdvanceDetailResultModel = (GetAdvanceDetailResultModel) selectedObject;

                                    double paidAmount = 0;
                                    double balanceAmt = 0;
                                    double totalAdvanceAdjustInCaseExpenseSumLesserTemp = 0;
                                    if (currency != null && saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().size() > 0) {
                                    } else {
                                        claimDialog.dismiss();
                                        new AlertCustomDialog(context, "Please Add Expense");
                                        return;
                                    }
                                    if (getAdvanceDetailResultModel.getBalAmount() != null && !getAdvanceDetailResultModel.getBalAmount().equalsIgnoreCase("")) {
                                        balanceAmt = Double.parseDouble(getAdvanceDetailResultModel.getBalAmount());
                                    }
                                    for (AdvanceListItemModel advance : advanceList) {
                                        if (!advance.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)) {
                                            totalAdvanceAdjustInCaseExpenseSumLesserTemp = totalAdvanceAdjustInCaseExpenseSumLesserTemp + Double.parseDouble(advance.getAdjAmount());
                                            if (advance.getReqCode().equalsIgnoreCase
                                                    (getAdvanceDetailResultModel.getReqCode())) {
                                                paidAmount = paidAmount + Double.parseDouble(advance.getAdjAmount());
                                            }
                                        }
                                    }
                                    totalAdvanceAdjustInCaseExpenseSumLesser = totalAdvanceAdjustInCaseExpenseSumLesserTemp;

                                    if (advanceAdjustmentResponseModel.getGetAdvanceListForExpenseResult()
                                            .getAdvanceList().contains(getAdvanceDetailResultModel)) {
                                        getAdvanceDetailResultModel.setPaidAmount(paidAmount + "");
                                        advanceAdjustmentResponseModel.getGetAdvanceListForExpenseResult().
                                                getAdvanceList().set(advanceAdjustmentResponseModel.getGetAdvanceListForExpenseResult()
                                                .getAdvanceList().indexOf(getAdvanceDetailResultModel), getAdvanceDetailResultModel);
                                    }

                                    requestTV.setText(getAdvanceDetailResultModel.getReqCode());
                                    requestCode = getAdvanceDetailResultModel.getReqCode();
                                    reasonCode = getAdvanceDetailResultModel.getReason();

                                    amount = getAdvanceDetailResultModel.getBalAmount();
                                    if (balanceAmt == paidAmount) {
                                        builder.dismiss();
                                        new AlertCustomDialog(context, "You have not advance amount to adjust");
                                        return;
                                    }
                                    showPopupForAdjustExpense(getAdvanceDetailResultModel);
                                    builder.dismiss();
                                }
                            });
                            claimDialog.show();
                        } else {
                            new AlertCustomDialog(context, getResources().getString(R.string.error_no_advance));
                            return;
                        }
                    }
                });
            }
        } else {
            new AlertCustomDialog(context, "Please Select Currency");
            return;}
    }

    private void sendViewRequestSummaryData(ExpenseApprovalList item) {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getViewExpenseClaimSummaryData(item.getReqID()),
                CommunicationConstant.API_GET_EXPENSE_CLAIM_DETAIL, true);
    }

    private void updateUI(final ViewExpenseItemModel item) {
        onBehalfTV.setText(item.getName());
        voucherNoTV.setText(item.getReqCode());
        if (item.getClaimTypeID() != 0) {
            claimLinearLayout.setVisibility(View.VISIBLE);
            claimTypeTV.setText(item.getClaimTypeDesc());
            claimTypeId = item.getClaimTypeID();

        }

        currencyTV.setText(item.getCurrencyCode());
        currency=item.getCurrencyCode();
        if (item.getApproverName() != null) {
            approverLl.setVisibility(View.VISIBLE);
            approverTV.setText(item.getApproverName());
            approverId = String.valueOf(item.getApproverID());

        }

        detailsET.setText(item.getDescription());
        totalExpenseClaimedTV.setText(item.getTotalExpenseClaimed());

        netAmountTV.setText(item.getNetAmountToBePaid());

        if (item.getProjectName() != null && !item.getProjectName().equalsIgnoreCase("")) {
            projectLinearLayout.setVisibility(View.VISIBLE);
            projectTV.setText(item.getProjectName());
        }
        remarksET.setText(item.getReqRemark());

        if (saveExpenseRequestModel != null) {

        } else {
            saveExpenseRequestModel = new SaveExpenseRequestModel();
            SaveExpenseItem saveExpenceItem = new SaveExpenseItem();
            saveExpenceItem.setCurrencyCode(currency);
            saveExpenceItem.setForEmpID(item.getForEmpID());
            saveExpenceItem.setTotalExpenseClaimedAmount(totalExpenseClaimedTV.getText().toString());
            saveExpenceItem.setNetAmount(netAmountTV.getText().toString());

            projectId = item.getProjectID();

            if (item.getApproverID() != 0) {
                approverId = item.getApproverID() + "";
                saveExpenceItem.setApproverID(item.getApproverID() + "");
                saveExpenceItem.setApproverName(item.getApproverName());

            }

            if (projectTV.getText().toString().equalsIgnoreCase("")) {

                saveExpenceItem.setProjectID(0);
                projectId = 0;
                saveExpenceItem.setProjectID(projectId);
            } else {
                projectId = item.getProjectID();
                saveExpenceItem.setProjectID(item.getProjectID());
                saveExpenceItem.setProjectName(item.getProjectName());

            }

            if (item.getClaimTypeID() != 0) {
                claimTypeId = item.getClaimTypeID();
                saveExpenceItem.setClaimTypeID(item.getClaimTypeID());
                saveExpenceItem.setClaimTypeDesc(item.getClaimTypeDesc());
            }


            saveExpenceItem.setReqID(String.valueOf(item.getReqID()));
            saveExpenceItem.setReqRemark(remarksET.getText().toString());
            saveExpenceItem.setDescription(item.getDescription());

            SaveExpenseModel expense = new SaveExpenseModel();
            expense.setExpenseItem(saveExpenceItem);
            saveExpenseRequestModel.setExpense(expense);
        }

    }


    public void sendAdvanceAdjustmentData() {
        String currencyValue, forEmpId;
        currencyValue = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getCurrencyCode();
        forEmpId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getForEmpID() + "";
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceAdjustmentData(currencyValue, forEmpId),
                CommunicationConstant.API_GET_ADVANCE_LIST_FOR_EXPENSE, true);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.remarks_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

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

    public String[] sendPeriodicMonthData() {
        String[] monthList = null;
        ArrayList<String> list = new ArrayList<>();
        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().size() > 0) {
            for (LineItemsModel itemsModel : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                if (itemsModel.getCategoryID() == AppsConstant.PERIODIC_EXPENSE) {
                    list.add(itemsModel.getHeadID()+"#"+itemsModel.getDateTo());
                }
            }

            if (list.size() > 0) {
                monthList = new String[list.size()];
                list.toArray(monthList);
            }
            return monthList;
        }
        return monthList;
    }

    public void sendExpenseClaimData() {

        ArrayList<LineItemsModel> lineItemsModel;


        onBehalf = onBehalfTV.getText().toString();
        description = detailsET.getText().toString();
        currency = currencyTV.getText().toString();
        remarks = remarksET.getText().toString();
        approverName = approverTV.getText().toString();


        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {

            approverId = saveExpenseRequestModel.getExpense().getExpenseItem().getApproverID() + "";
            if(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID()>0) {
                projectId = saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID();
            }else{
                projectId=0;
            }
            empId = saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID();
            claimTypeId = saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID();
            requestId = saveExpenseRequestModel.getExpense().getExpenseItem().getReqID();

            if (advanceList != null && advanceList.size() > 0) {
                for (int i = 0; i < advanceList.size(); i++) {
                    AdvanceListItemModel model = advanceList.get(i);
                    model.setSeqNo(i + 1);
                    advanceList.set(i, model);
                }
            }

            if (uploadFileList != null && uploadFileList.size() > 0) {
                for (int i = 0; i < uploadFileList.size(); i++) {
                    DocListModel model = uploadFileList.get(i);
                    model.setSeqNo(i + 1);
                    model.setBitmap(null);
                    uploadFileList.set(i, model);
                }
            }

            String[] monthList = sendPeriodicMonthData();
            if (monthList != null && monthList.length > 0) {
                CommunicationManager.getInstance().sendPostRequest(this,
                        AppRequestJSONString.getPeriodicMonthData(empId, Integer.parseInt(requestId), monthList),
                        CommunicationConstant.API_GET_MONTH_LIST, true);
            } else {

                if (fromButton.equalsIgnoreCase("Reject") || fromButton.equalsIgnoreCase("Return")) {
                    if (!remarks.equalsIgnoreCase("")) {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId, requestId, saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList,
                                        description, remarks, currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),
                                        saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                                CommunicationConstant.API_GET_SAVE_EXPENSE, true);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        new AlertCustomDialog(context, "Enter Remarks");
                    }
                } else {
                    CommunicationManager.getInstance().sendPostRequest(this,
                            AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId, requestId,
                                    saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description,
                                    remarks, currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                            CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                }
            }


        } else {
            if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult() != null &&
                    viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem() != null) {
                approverId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getApproverID() + "";
                requestId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqID() + "";
                if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList() != null) {
                    advanceList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList();
                }
                claimTypeId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getClaimTypeID();
                if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getProjectID() >0) {
                    projectId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getProjectID();
                } else {
                    projectId = 0;
                }
                if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList() != null) {
                    uploadFileList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList();
                }
                empId = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getForEmpID();
                String[] monthList = sendPeriodicMonthData();
                if (monthList != null && monthList.length > 0) {
                    CommunicationManager.getInstance().sendPostRequest(this,
                            AppRequestJSONString.getPeriodicMonthData(empId, Integer.parseInt(requestId), monthList),
                            CommunicationConstant.API_GET_MONTH_LIST, true);
                } else {

                    if (fromButton.equalsIgnoreCase("Reject") || fromButton.equalsIgnoreCase("Return")) {
                        if (!remarks.equalsIgnoreCase("")) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId, requestId,
                                            viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems(), advanceList, description,
                                            remarks, currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                                    CommunicationConstant.API_GET_SAVE_EXPENSE, true);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            new AlertCustomDialog(context, "Enter Remarks");
                        }
                    } else {
                        if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult() != null &&
                                viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem() != null) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId,
                                            requestId, viewClaimSummaryResponseModel.getGetExpenseDetailResult()
                                                    .getExpenseItem().getLineItems(), advanceList, description, remarks,
                                            currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                                    CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                        }
                        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                                && saveExpenseRequestModel.getExpense().getExpenseItem() != null){
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId, requestId,
                                            saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description,
                                            remarks, currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                                    CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                        }

                    }
                }
            }
        }

    }


    public void sendExpenseInitData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseInitData(),
                CommunicationConstant.API_GET_EXPENSE_PAGE_INIT, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EXPENSE_PAGE_INIT:
                String str1 = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str1);
                expensePageInitResponseModel = GetExpensePageInitResponseModel.create(str1);
                if (expensePageInitResponseModel != null && !expensePageInitResponseModel.getGetExpensePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    new AlertCustomDialog(getActivity(),expensePageInitResponseModel.getGetExpensePageInitResult().getErrorMessage());
                    return;
                }

                if (expenseApprovalList != null) {
                    sendViewRequestSummaryData(expenseApprovalList);
                }
                break;
            case CommunicationConstant.API_GET_EXPENSE_CLAIM_DETAIL:

                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                viewClaimSummaryResponseModel = ViewClaimSummaryResponseModel.create(str);
                if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult() != null) {
                    advanceDropdownList = new ArrayList<AdvanceListItemModel>();

                    if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult() != null
                            && viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem() != null) {
                        sendAdvanceAdjustmentData();
                    }
                    updateUI(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem());

                    saveExpenseRequestModel.getExpense().getExpenseItem().setReqID(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqID()+"");
                    saveExpenseRequestModel.getExpense().getExpenseItem().setLineItems(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems());

                    refresh(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems());

                    saveExpenseRequestModel.setButtons(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getButtons());
                    saveExpenseRequestModel.setPageInitResultModel(expensePageInitResponseModel.getGetExpensePageInitResult());
                    saveExpenseRequestModel.getExpense().getExpenseItem().setDocValidation(expensePageInitResponseModel.getGetExpensePageInitResult().getDocValidation());
                    setupButtons();

                    advanceList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList();
                    saveExpenseRequestModel.getExpense().getExpenseItem().setAdvanceList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList());
                    refreshAdjustmentRecycle(advanceList);
                    refreshRemarksList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getRequestRemarks());
                    saveExpenseRequestModel.getExpense().getExpenseItem().setRequestRemarks(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getRequestRemarks());
                    refreshPaymentList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getPaymentDetails());
                    saveExpenseRequestModel.getExpense().getExpenseItem().setPaymentDetails(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getPaymentDetails());
                    uploadFileList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList();
                    saveExpenseRequestModel.getExpense().getExpenseItem().setDocList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList());
                    refreshList(uploadFileList);
                    saveExpenseRequestModel.getExpense().getExpenseItem().setReqStatus(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqStatus()+"");

                }

                break;
            case CommunicationConstant.API_GET_ADVANCE_LIST_FOR_EXPENSE:
                String advanceResponse = response.getResponseData();
                Log.d("TAG", "Advance Response : " + advanceResponse);
                advanceAdjustmentResponseModel = AdvanceAdjustmentResponseModel.create(advanceResponse);
                saveExpenseRequestModel.getExpense().getExpenseItem().setAdvanceAdjustmentResponseModel(advanceAdjustmentResponseModel);
                setAdvanceAdjustmentData();
                break;

            case CommunicationConstant.API_GET_MONTH_LIST:
                String responseData1 = response.getResponseData();
                Log.d("TAG", "Advance Response : " + responseData1);
                periodicExpenseResponseModel = PeriodicExpenseResponseModel.create(responseData1);
                if (periodicExpenseResponseModel != null && periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult() != null
                        && !periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult().getErrorCode().equalsIgnoreCase("0")) {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new AlertCustomDialog(getActivity(), periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult().getErrorMessage());
                    return;
                } else {

                    CommunicationManager.getInstance().sendPostRequest(this,
                            AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverId, requestId,
                                    viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems(), advanceList,
                                    description, remarks, currency, claimTypeId, String.valueOf(projectId), uploadFileList, String.valueOf(empId),saveExpenseRequestModel.getExpense().getExpenseItem().getReqStatus()),
                            CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                }


                break;
            case CommunicationConstant.API_GET_SAVE_EXPENSE:
                String responseData = response.getResponseData();
                Log.d("TAG", "Advance Response : " + responseData);
                expenseClaimResponseModel = ExpenseClaimResponseModel.create(responseData);
                if (expenseClaimResponseModel != null && expenseClaimResponseModel.getSaveExpenseResult() != null &&
                        expenseClaimResponseModel.getSaveExpenseResult().getErrorCode().equalsIgnoreCase("0")) {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    CustomDialog.alertOkWithFinishFragment(context, expenseClaimResponseModel.getSaveExpenseResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new AlertCustomDialog(getActivity(), expenseClaimResponseModel.getSaveExpenseResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }


    private void refresh(ArrayList<LineItemsModel> lineItemsModels) {
        if (lineItemsModels != null && lineItemsModels.size() > 0) {
            expenseErrorLl.setVisibility(View.GONE);
            expenseDetailsRecyclerView.setVisibility(View.VISIBLE);
            Double totalNetAmout = 0.0;
            for (LineItemsModel item : lineItemsModels) {
                item.setAmtApproved(item.getClaimAmt());
                lineItemsModels.set(lineItemsModels.indexOf(item), item);
                if (item.getClaimAmt() != null && !item.getClaimAmt().equalsIgnoreCase("")) {
                    totalNetAmout = totalNetAmout + Double.parseDouble(item.getClaimAmt());
                }

            }
            Utility.formatAmount(totalTV,totalNetAmout);
            updateNetAmount();
            summaryAdapter = new ViewExpenseClaimSummaryAdapter(lineItemsModels);
            expenseDetailsRecyclerView.setAdapter(summaryAdapter);
            expenseDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            summaryAdapter.notifyDataSetChanged();
        } else {
            expenseErrorLl.setVisibility(View.VISIBLE);
            expenseDetailsRecyclerView.setVisibility(View.GONE);

        }
    }


    private class ViewExpenseClaimSummaryAdapter extends
            RecyclerView.Adapter<ViewExpenseClaimSummaryAdapter.MyViewHolder> {
        private ArrayList<LineItemsModel> dataSet;
        private int totalCount;
        private RecyclerView lineDocumentRecyclerView;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView toDateLabel, fromDateLabel, fromDateTV, toDateTV, detailsTV, claimHeadTV, inputTV, amountTV, totalAmountTV, approvedAmountTV, categoryDescTV;
            private Button editButton, viewDocBTN, statusBT;
            private LinearLayout statusLl, statusMsgLl, approvedAmountLl;
            private LinearLayout viewDocLl,lineDocumentLl,categoryLinearLayout,detailsLinearLayout,claimHeadLinearLayout,inputAmtLinearLayout,amountLinearLayout,fromDateLinearLayout,toDateLinearLayout;
            private ImageView img_menu_icon;

            public MyViewHolder(View v) {
                super(v);
                fromDateLabel = (TextView) v.findViewById(R.id.fromDateLabel);
                toDateLabel = (TextView) v.findViewById(R.id.toDateLabel);
                categoryDescTV = (TextView) v.findViewById(R.id.categoryDescTV);
                fromDateTV = (TextView) v.findViewById(R.id.fromDateTV);
                toDateTV = (TextView) v.findViewById(R.id.toDateTV);
                detailsTV = (TextView) v.findViewById(R.id.detailsTV);
                claimHeadTV = (TextView) v.findViewById(R.id.claimHeadTV);
                inputTV = (TextView) v.findViewById(R.id.inputTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);
                fromDateLinearLayout = (LinearLayout) v.findViewById(R.id.fromDateLinearLayout);
                fromDateLinearLayout.setVisibility(View.GONE);
                editButton = (Button) v.findViewById(R.id.actionBTN);
                editButton.setText("Edit");
                editButton.setVisibility(View.VISIBLE);

                lineDocumentLl = (LinearLayout) v.findViewById(R.id.lineDocumentLl);
                lineDocumentLl.setVisibility(View.GONE);

                inputAmtLinearLayout = (LinearLayout) v.findViewById(R.id.inputAmtLinearLayout);

                viewDocLl = (LinearLayout) v.findViewById(R.id.viewDocLl);
                viewDocBTN = (Button) v.findViewById(R.id.viewDocBTN);
                viewDocLl.setVisibility(View.GONE);

                toDateLinearLayout = (LinearLayout) v.findViewById(R.id.toDateLinearLayout);
                statusBT = (Button) v.findViewById(R.id.statusBT);

                statusLl = (LinearLayout) v.findViewById(R.id.statusLl);
                statusLl.setVisibility(View.GONE);

                statusMsgLl = (LinearLayout) v.findViewById(R.id.statusMsgLl);
                statusMsgLl.setVisibility(View.GONE);
                statusBT = (Button) v.findViewById(R.id.statusBT);

                approvedAmountLl = (LinearLayout) v.findViewById(R.id.approvedAmountLl);
                approvedAmountTV = (TextView) v.findViewById(R.id.approvedAmountTV);
                approvedAmountLl.setVisibility(View.GONE);

                categoryLinearLayout=(LinearLayout)  v.findViewById(R.id.categoryLinearLayout);
                detailsLinearLayout=(LinearLayout)  v.findViewById(R.id.detailsLinearLayout);
                claimHeadLinearLayout=(LinearLayout)  v.findViewById(R.id.claimHeadLinearLayout);
                amountLinearLayout=(LinearLayout)  v.findViewById(R.id.amountLinearLayout);

                img_menu_icon= (ImageView) v.findViewById(R.id.img_menu_icon);
                img_menu_icon.setVisibility(View.VISIBLE);

            }
        }

        public void addAll(List<LineItemsModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public ViewExpenseClaimSummaryAdapter(List<LineItemsModel> data) {
            this.dataSet = (ArrayList<LineItemsModel>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_expense_claim_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final LineItemsModel item = dataSet.get(listPosition);
            if (item.getCategoryID() == 1) {
                String[] fromDate = item.getDateFrom().split(" ");
                String[] toDate = item.getDateTo().split(" ");
                holder.fromDateLinearLayout.setVisibility(View.VISIBLE);
                holder.toDateLinearLayout.setVisibility(View.VISIBLE);
                holder.toDateLabel.setText("To Date");
                holder.fromDateTV.setText(fromDate[0]);
                holder.toDateTV.setText(toDate[0]);
            } else {
                String[] fromDate = item.getDateFrom().split(" ");
                holder.fromDateLinearLayout.setVisibility(View.VISIBLE);
                holder.toDateLinearLayout.setVisibility(View.GONE);
                holder.fromDateTV.setText(" ");
                holder.fromDateLabel.setText("Date");
                holder.fromDateTV.setText(fromDate[0]);
            }

            holder.categoryDescTV.setText(item.getCategoryDesc());
            holder.detailsTV.setText(item.getLineItemDetail());
            holder.claimHeadTV.setText(item.getHeadDesc());
            if (item.getInputUnit() != null && !item.getInputUnit().equalsIgnoreCase("0.00")) {
                holder.inputAmtLinearLayout.setVisibility(View.VISIBLE);
                holder.inputTV.setText(item.getInputUnit());
                holder.amountTV.setText(item.getClaimAmt());
                if (item.getCategoryID() == AppsConstant.PERIODIC_EXPENSE) {
                    holder.toDateLinearLayout.setVisibility(View.VISIBLE);
                    holder.toDateTV.setText(item.getDateTo());
                    holder.toDateLabel.setText("Period");
                }

            } else {
                holder.inputAmtLinearLayout.setVisibility(View.GONE);
                holder.amountTV.setText(item.getClaimAmt());
            }

            if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqStatusDesc().equalsIgnoreCase("Approved")) {
                holder.approvedAmountLl.setVisibility(View.VISIBLE);
                holder.approvedAmountTV.setText(item.getAmtApproved());

            }
        /*    if (!item.getPolicyID().equalsIgnoreCase("")) {
                holder.statusMsgLl.setVisibility(View.VISIBLE);
                holder.statusBT.setText(Utility.policyStatus(item.getPolicyID(), item.getPolicyLimitValue(), item.getInputUnit(), item.getClaimAmt()));
                holder.statusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utility.openPolicyStatusPopUp(item, context, preferences);
                    }
                });
            } else {
                holder.statusLl.setVisibility(View.VISIBLE);
            }

            if (item.getDocListLineItem() != null && item.getDocListLineItem().size() > 0) {
                holder.viewDocLl.setVisibility(View.VISIBLE);
                holder.viewDocBTN.setText("Document");
                holder.viewDocBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                        AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                        AddExpenseActivity.lineItemsModel=item;
                        startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                    }
                });
            }*/

            if (item.getPolicyID().equalsIgnoreCase("")) {
                holder.statusLl.setVisibility(View.VISIBLE);
            }

            holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Edit");
                    if (item.getDocListLineItem() != null && item.getDocListLineItem().size() > 0) {
                        list.add("Document " + item.getDocListLineItem().size());
                    }
                    if (!item.getPolicyID().equalsIgnoreCase("")) {
                        list.add("Policy Status");
                    }

                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                    customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                                Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                                AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                                AddExpenseActivity.lineItemsModel=item;
                                startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                            } else if(selectedObject.toString().equalsIgnoreCase("Document " + item.getDocListLineItem().size())){
                                Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                                AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                                AddExpenseActivity.lineItemsModel=item;
                                startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                            }else if(selectedObject.toString().equalsIgnoreCase("Policy Status")){
                                Utility.openPolicyStatusPopUp(item, context, preferences);
                            }
                            builder.dismiss();
                        }


                    });
                    customBuilder.show();
                }
            });

          /*  holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                    AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                    AddExpenseActivity.lineItemsModel=item;
                    startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                }
            });*/
            setLineItemLable( holder,item);
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }

        private void setLineItemLable(MyViewHolder holder, LineItemsModel item){
            ArrayList<CategoryLineItemLabelItem> labelItemArrayList=saveExpenseRequestModel.getPageInitResultModel().getCategoryLineItemLabel();

            CategoryLineItemLabelItem matchCat=null;
            if(labelItemArrayList!=null && labelItemArrayList.size()>0){
                for(CategoryLineItemLabelItem cateItem : labelItemArrayList){
                    if(item.getCategoryID()==cateItem.getCategoryID()){
                        matchCat=cateItem;
                        break;
                    }
                }
            }
            holder.fromDateLinearLayout.setVisibility(View.GONE);
            holder.toDateLinearLayout.setVisibility(View.GONE);
            if(item.getInputUnit()==null || item.getInputUnit().equalsIgnoreCase("")){
                holder.inputAmtLinearLayout.setVisibility(View.GONE);
            }
            if(matchCat!=null){
                if(item.getDateFrom()!=null && !item.getDateFrom().equalsIgnoreCase("")){
                    holder.fromDateLinearLayout.setVisibility(View.VISIBLE);
                }
                if(item.getDateTo()!=null && !item.getDateTo().equalsIgnoreCase("")){
                    holder.toDateLinearLayout.setVisibility(View.VISIBLE);
                }
                for(LineItemColumnsItem column : matchCat.getLineItemColumns()){
                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.DATE_FROM_TAG)){
                        ((TextView)holder.fromDateLinearLayout.findViewById(R.id.fromDateLabel)).setText(column.getLableName());
                    }
                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.DATE_TO_TAG)){
                        ((TextView)holder.toDateLinearLayout.findViewById(R.id.toDateLabel)).setText(column.getLableName());
                    }
                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.CLAIM_HEAD_TAG)){
                        ((TextView)holder.claimHeadLinearLayout.findViewById(R.id.claimHeadLabelTV)).setText(column.getLableName());
                    }
                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.CLAIM_AMT_TAG)){
                        ((TextView)holder.amountLinearLayout.findViewById(R.id.amtLabelTV)).setText(column.getLableName());
                    }
                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.LINE_ITEM_DETAIL_LABEL)){
                        ((TextView)holder.detailsLinearLayout.findViewById(R.id.detailLableTV)).setText(column.getLableName());
                    }

                    if(column.getColumnName().equalsIgnoreCase(LineItemsModel.INPUT_UNIT_TAG)){
                        ((TextView)holder.inputAmtLinearLayout.findViewById(R.id.inputLabelTV)).setText(column.getLableName());
                    }
                }
            }

        }

    }

    private class AdjustmentDetailAdapter extends RecyclerView.Adapter<AdjustmentDetailAdapter.ViewHolder> {
        private List<AdvanceListItemModel> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView reasonTV, amountTV, requestIdTV;
            public Button deleteBTN;
            public RelativeLayout advanceListParentRL;


            public ViewHolder(View v) {
                super(v);
                requestIdTV = (TextView) v.findViewById(R.id.requestTV);
                reasonTV = (TextView) v.findViewById(R.id.reasonTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);
                advanceListParentRL = (RelativeLayout) v.findViewById(R.id.advanceListParentRL);
                deleteBTN = (Button) v.findViewById(R.id.deleteBTN);
                deleteBTN.setVisibility(View.VISIBLE);


            }
        }

        public AdjustmentDetailAdapter(List<AdvanceListItemModel> myDataset) {
            mDataset = myDataset;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_adjustment_detail_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final AdvanceListItemModel item = mDataset.get(position);
            holder.advanceListParentRL.setVisibility(View.GONE);
            if (item.getAdvanceID() != 0 && item.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)) {
                holder.advanceListParentRL.setVisibility(View.GONE);

            } else {
                holder.advanceListParentRL.setVisibility(View.VISIBLE);
                holder.requestIdTV.setText(item.getReqCode() + "");
                holder.reasonTV.setText(item.getReason());
                if (item.getPaidAmount() != null && !item.getPaidAmount().equalsIgnoreCase("")) {
                    holder.amountTV.setText(item.getPaidAmount());
                } else {
                    holder.amountTV.setText(item.getAdjAmount());
                }
                holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getPaidAmount() != null && !item.getPaidAmount().equalsIgnoreCase("")) {
                            totalAmountTobeAdjusted = totalAmountTobeAdjusted + Double.parseDouble(item.getPaidAmount());
                        } else {
                            totalAmountTobeAdjusted = totalAmountTobeAdjusted + Double.parseDouble(item.getAdjAmount());
                        }
                        netAmountTV.setText(totalAmountTobeAdjusted + "");
                        AdvanceListItemModel listItemModel = mDataset.get(position);
                        if (listItemModel.getTranID() != 0 && listItemModel.getFlag().equalsIgnoreCase(AppsConstant.OLD_FLAG)) {
                            listItemModel.setFlag(AppsConstant.DELETE_FLAG);
                            mDataset.set(position, listItemModel);
                        } else if (listItemModel.getTranID() == 0 && listItemModel.getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {

                            mDataset.remove(position);
                        }
                        AdjustmentDetailAdapter.this.notifyDataSetChanged();
                        updateNetAmount();
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

    private void refreshAdjustmentRecycle(ArrayList<AdvanceListItemModel> list) {
        double netAmount = 0;
        advanceErrorLinearLayout.setVisibility(View.VISIBLE);
        if (list != null && list.size() > 0) {
            advanceErrorLinearLayout.setVisibility(View.GONE);
            advanceRV.setVisibility(View.VISIBLE);
            requestVoucherLl.setVisibility(View.VISIBLE);
            advanceRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            adjustemntDetailAdapter = new AdjustmentDetailAdapter(list);
            advanceRV.setAdapter(adjustemntDetailAdapter);
            adjustemntDetailAdapter.notifyDataSetChanged();
            updateNetAmount();
        } else {
            advanceRV.setVisibility(View.GONE);
            requestVoucherLl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AddExpenseActivity.REQUEST_CODE){
            if(data!=null) {

                saveExpenseRequestModel=AddExpenseFragment.expenseRequestModel;
                AddExpenseActivity.lineItemsModel=null;
                AddExpenseFragment.expenseRequestModel=null;
                AddExpenseActivity.saveExpenseRequestModel=null;
                refresh(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems());
            }else{
                AddExpenseActivity.lineItemsModel=null;
                AddExpenseFragment.expenseRequestModel=null;
                AddExpenseActivity.saveExpenseRequestModel=null;
            }
            return;
        }

        final DocListModel fileObj = new DocListModel();
        if (requestCode == UPLOAD_DOC_REQUEST && resultCode == RESULT_OK) {
            boolean fileShow = true;
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
                List<String> extensionList = Arrays.asList(expensePageInitResponseModel.getGetExpensePageInitResult().getDocValidation().getExtensions());
                if (!extensionList.contains(extension.toLowerCase())) {
                    CustomDialog.alertWithOk(context, expensePageInitResponseModel.getGetExpensePageInitResult().getDocValidation().getMessage());
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
                        fileObj.setBitmap(bitmap);
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
                        encodeFileToBase64Binary=fileToBase64Conversion(data.getData());
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

                if(Utility.calcBase64SizeInKBytes(encodeFileToBase64Binary)>Utility.maxLimit){
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
                    Log.d("encodedFile", encodeFileToBase64Binary);
                }
                refreshList(uploadFileList);


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
                            String encodeFileToBase64Binary = Utility.converBitmapToBase64(bitmap);//fileToBase64Conversion(data.getData());

                            if (uploadFileList.size() > 0) {
                                for (int i = 1; i <= uploadFileList.size(); i++) {
                                    fileObj.setBase64Data(encodeFileToBase64Binary);
                                    fileObj.setFlag("N");
                                    fileObj.setBitmap(bitmap);
                                    String seqNo = String.valueOf(i + 1);
                                    Log.d("seqNo", "seqNo");
                                    uploadFileList.add(fileObj);

                                    break;
                                }
                            } else {
                                fileObj.setBase64Data(encodeFileToBase64Binary);
                                fileObj.setFlag("N");
                                fileObj.setBitmap(bitmap);
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

    private void refreshList(ArrayList<DocListModel> uploadFileList) {
        if (uploadFileList != null && uploadFileList.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            documentRV.setVisibility(View.VISIBLE);
            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList);
            documentRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            documentRV.setVisibility(View.GONE);
        }
    }

    private class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {
        private ArrayList<DocListModel> mDataset;

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

        public DocumentUploadAdapter(ArrayList<DocListModel> myDataset) {
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

            final DocListModel fileObject = mDataset.get(position);
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
                }  else if (filename.toString().contains(".xlsx") || filename.toString().contains(".xls")) {
                    fileType = "application/word";
                    try {
                        holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.doc_icon));
                        holder.fileNameTV.setText(filename);
                        holder.fileDescriptionTV.setText(name);
                    } catch (Exception e) {

                    }
                }else if (filename.toString().contains(".txt")) {
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
                                            //  SupportDocsItemModel docItem = new SupportDocsItemModel();
                                            fileObject.setName(editFilenameET.getText().toString());
                                            if (uploadFileList != null && uploadFileList.size() > 0) {
                                                uploadFileList.set(uploadFileList.indexOf(fileObject), fileObject);

                                            } else {
                                                uploadFileList = new ArrayList<DocListModel>();
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
                                    DocListModel doc = mDataset.get(position);
                                    if (doc.getDocID() != 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.OLD_FLAG)) {
                                        doc.setFlag(AppsConstant.DELETE_FLAG);
                                        mDataset.set(position, doc);
                                    } else if (doc.getDocID() == 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {
                                        mDataset.remove(position);
                                    }
                                    DocumentUploadAdapter.this.notifyDataSetChanged();
                                    if (mDataset.size() == 0) {
                                        errorLinearLayout.setVisibility(View.VISIBLE);
                                    }

                                } else if (selectedObject.toString().equalsIgnoreCase("Download")) {

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

    private void showPopupForAdjustExpense(final GetAdvanceDetailResultModel model) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.advance_adjustment_detail_item);
        final TextView reasonTV, amountTV, requestIdTV;
        final LinearLayout amountEditableLinearLayout, amountLinearLayout;
        Button deleteBTN;
        LinearLayout header_layout;
        requestIdTV = (TextView) dialog.findViewById(R.id.requestTV);
        reasonTV = (TextView) dialog.findViewById(R.id.reasonTV);
        amountEditableLinearLayout = (LinearLayout) dialog.findViewById(R.id.amountEditableLinearLayout);
        amountEditableLinearLayout.setVisibility(View.VISIBLE);
        amountLinearLayout = (LinearLayout) dialog.findViewById(R.id.amountLinearLayout);
        amountLinearLayout.setVisibility(View.GONE);
        amountTV = (TextView) dialog.findViewById(R.id.amountET);
        deleteBTN = (Button) dialog.findViewById(R.id.deleteBTN);
        deleteBTN.setVisibility(View.GONE);
        header_layout = (LinearLayout) dialog.findViewById(R.id.header_layout);
        header_layout.setVisibility(View.VISIBLE);
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(context, preferences);
        TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
        tv_header_text.setTextColor(textColor);
        header_layout.setBackgroundColor(bgColor);
        tv_header_text.setText("Advance Adjustments");
        requestIdTV.setText(model.getReqCode());
        reasonTV.setText(model.getReason());
        ArrayList<LineItemsModel> expenseList = null;
        if (viewClaimSummaryResponseModel != null) {
            expenseList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems();
        } else {
            expenseList = saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems();
        }

        double expenseAmtTemp = 0;
        for (LineItemsModel model1 : expenseList) {
            if (model1 != null && !model1.getClaimAmt().equalsIgnoreCase("")) {
                expenseAmtTemp = expenseAmtTemp + Double.parseDouble(model1.getClaimAmt());
            }
        }
        totalExpenseAmt = expenseAmtTemp;
        totalExpenseAmt = totalExpenseAmt - totalAdvanceAdjustInCaseExpenseSumLesser;
        if (model.getBalAmount() != null && !model.getBalAmount().equalsIgnoreCase("")) {
            balanceAmt = Double.parseDouble(model.getBalAmount());
        }


        if (totalExpenseAmt > balanceAmt) {
            if ((model.getPaidAmount() != null && !model.getPaidAmount().equalsIgnoreCase(""))
                    && (model.getBalAmount() != null && !model.getBalAmount().equalsIgnoreCase(""))) {
                double amount = Double.parseDouble(model.getBalAmount()) - Double.parseDouble(model.getPaidAmount());
                amountTV.setText(amount + "");
            }
        } else if (totalExpenseAmt < balanceAmt) {

            double amount = totalExpenseAmt - totalAdvanceAdjustInCaseExpenseSumLesser;

            amountTV.setText(totalExpenseAmt + "");
        }

        (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                double amount = Double.parseDouble(amountTV.getText().toString());
                if (amount == 0) {
                    new AlertCustomDialog(context, "Please enter amount");
                    return;
                }
                if (amount > totalExpenseAmt) {
                    new AlertCustomDialog(context, "Amount cannot be greater than Expense Amount");
                    return;
                } else if (amount > balanceAmt) {
                    new AlertCustomDialog(context, "Amount cannot be greater than Advance Amount");
                    return;
                } else {
                    AdvanceListItemModel item = new AdvanceListItemModel();
                    item.setAdjAmount(amount + "");
                    item.setReqCode(model.getReqCode());
                    item.setAdvanceID(model.getAdvanceID());
                    item.setReason(model.getReason());
                    item.setTranID(0);
                    item.setFlag("N");
                    advanceList.add(item);
                    refreshAdjustmentRecycle(advanceList);
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

    private void updateNetAmount() {
        double netAmount = 0;
        double advanceAdjustAmt = 0;
        double totalExpense = 0;
        if (advanceList != null) {
            for (AdvanceListItemModel item : advanceList) {
                if (!item.getFlag().equalsIgnoreCase(AppsConstant.DELETE_FLAG)) {
                    advanceAdjustAmt = advanceAdjustAmt + Double.parseDouble(item.getAdjAmount());
                }
            }
        }
            if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null &&
                    saveExpenseRequestModel.getExpense().getExpenseItem() != null
                    && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {
                for (LineItemsModel item : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                    totalExpense = totalExpense + Double.parseDouble(item.getClaimAmt());
                }
            }

        saveExpenseRequestModel.getExpense().getExpenseItem().setTotalExpenseClaimedAmount(totalExpense + "");
        if (totalExpense != 0.0) {
            netAmount = totalExpense - advanceAdjustAmt;
            saveExpenseRequestModel.getExpense().getExpenseItem().setNetAmount(netAmount + "");
            netAmountTV.setText(netAmount + "");
        } else {
            netAmount = 0.0;
            saveExpenseRequestModel.getExpense().getExpenseItem().setNetAmount(netAmount + "");
            netAmountTV.setText(netAmount + "");

        }
        Utility.formatAmount(netAmountTV,netAmount);
    }

    private void refreshPaymentList(ArrayList<ExpensePaymentDetailsItem> ExpensePaymentDetailsItems) {
        if (ExpensePaymentDetailsItems != null && ExpensePaymentDetailsItems.size() > 0) {
            paymentLinearLayout.setVisibility(View.GONE);
            paymentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            paymentRV.setVisibility(View.VISIBLE);
            paymentAdapter = new PaymentAdapter(ExpensePaymentDetailsItems);
            paymentRV.setAdapter(paymentAdapter);
            paymentAdapter.notifyDataSetChanged();

            if (ExpensePaymentDetailsItems.size() > 0) {
                totalPaymentLabelLl.setVisibility(View.VISIBLE);
            }
            double total = 0;
            for (ExpensePaymentDetailsItem item : ExpensePaymentDetailsItems) {
                total = total + Double.parseDouble(item.getAmount());
            }

            totalPaymentTV.setText("");
            Utility.formatAmount(totalPaymentTV,total);
        } else {
            paymentLinearLayout.setVisibility(View.VISIBLE);
            paymentRV.setVisibility(View.GONE);
        }
    }

}
