package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.ViewDocumentActivity;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.CategoryLineItemLabelItem;
import hr.eazework.com.model.DocListModel;
import hr.eazework.com.model.ExpenseClaimResponseModel;
import hr.eazework.com.model.ExpenseItemListModel;
import hr.eazework.com.model.ExpensePaymentDetailsItem;
import hr.eazework.com.model.GetExpensePageInitResponseModel;
import hr.eazework.com.model.LineItemColumnsItem;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RequestRemarksItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.ViewClaimSummaryResponseModel;
import hr.eazework.com.model.ViewExpenseItemModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.customview.PaymentAdapter;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ViewExpenseClaimSummaryFragment extends BaseFragment {

    private Context context;
    private Preferences preferences;
    private TextView totalApprovedAmtTV, claimForTV, descriptionTV, currencyTV, claimTypeTV, submittedByTV, statusTV, advanceVoucherTV, submittedOnTV;
    private TextView dateTV, nameTV, remarksReasonTV, remarksStatusTV, totalExpenseClaimedTV, netAmountTV;
    private LinearLayout errorLinearLayout, expenseErrorLl, remarksLinearLayout;
    private ExpenseItemListModel expenseItemListModel;
    private ViewExpenseClaimSummaryAdapter summaryAdapter;
    private RecyclerView expenseDetailsRecyclerView, documentRV, advanceRV, remarksRV;
    private DocumentViewAdapter documentViewAdapter;
    private static ProgressDialog progress;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private TextView totalTV;
    private LinearLayout layout, advanceErrorLinearLayout, requestVoucherLl, totalApprovedAmountLl;
    //  private LineDocumentViewAdapter lineDocumentViewAdapter;
    private AdjustmentDetailAdapter adjustemntDetailAdapter;
    private String fromButton, approverName = "", approverID = "", description = "", currency, remarks = "", forEmpId, projectId;
    private int claimTypeID = 0;
    private ProgressBar progressBar;
    private ExpenseClaimResponseModel expenseClaimResponseModel;
    private ViewClaimSummaryResponseModel viewClaimSummaryResponseModel;
    private RemarksAdapter remarksAdapter;
    private ImageView add_expenseIV, plus_create_newIV;
    private Button rejectBTN, returnBTN,approvalBTN,withdrawBTN,resubmitBTN;
    private PaymentAdapter paymentAdapter;
    private TextView totalPaymentTV;
    private RecyclerView paymentRV;
    private LinearLayout paymentLinearLayout,totalPaymentLabelLl;
    private GetExpensePageInitResponseModel expensePageInitResponseModel;
    private LinearLayout advance_adjustment_Ll;

    public ExpenseItemListModel getExpenseItemListModel() {
        return expenseItemListModel;
    }

    public void setExpenseItemListModel(ExpenseItemListModel expenseItemListModel) {
        this.expenseItemListModel = expenseItemListModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_expense_claim_summary_fragment, container, false);
        Log.d("TAG"," Log H erer");
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);

        advance_adjustment_Ll= (LinearLayout) view.findViewById(R.id.advance_adjustment_Ll);
        advance_adjustment_Ll.setVisibility(View.GONE);

        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_ADVANCE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                advance_adjustment_Ll.setVisibility(View.VISIBLE);
            }
        }

        layout = (LinearLayout) view.findViewById(R.id.totalLabelLl);
        layout.setVisibility(View.GONE);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);


        totalTV = (TextView) view.findViewById(R.id.totalAmountTV);

        totalApprovedAmountLl = (LinearLayout) view.findViewById(R.id.totalApprovedAmountLl);
        totalApprovedAmountLl.setVisibility(View.GONE);
        totalApprovedAmtTV = (TextView) view.findViewById(R.id.totalApprovedAmtTV);

        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text))
                .setText("Expense Claim Summary");
        //  ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.EXPENSE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.EXPENSE_HOME, null, null);
                    }
                }
            }
        });

        advanceVoucherTV = (TextView) view.findViewById(R.id.advanceVoucherTV);
        paymentRV=(RecyclerView)view.findViewById(R.id.paymentRV);
        add_expenseIV = (ImageView) view.findViewById(R.id.add_expenseIV);
        add_expenseIV.setVisibility(View.GONE);
        claimForTV = (TextView) view.findViewById(R.id.claimForTV);
        descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
        currencyTV = (TextView) view.findViewById(R.id.currencyTV);
        statusTV = (TextView) view.findViewById(R.id.statusTV);
        claimTypeTV = (TextView) view.findViewById(R.id.claimTypeTV);
        submittedByTV = (TextView) view.findViewById(R.id.submittedByTV);
        submittedOnTV = (TextView) view.findViewById(R.id.submittedOnTV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        nameTV = (TextView) view.findViewById(R.id.nameTV);
        remarksReasonTV = (TextView) view.findViewById(R.id.remarksReasonTV);
        remarksStatusTV = (TextView) view.findViewById(R.id.remarksStatusTV);
        expenseDetailsRecyclerView = (RecyclerView) view.findViewById(R.id.expenseDetailsRecyclerView);

        documentRV = (RecyclerView) view.findViewById(R.id.expenseRecyclerView);
        plus_create_newIV = (ImageView) view.findViewById(R.id.plus_create_newIV);
        plus_create_newIV.setVisibility(View.GONE);
        totalExpenseClaimedTV = (TextView) view.findViewById(R.id.totalExpenseClaimedTV);
        netAmountTV = (TextView) view.findViewById(R.id.netAmountTV);
        resubmitBTN = (Button) view.findViewById(R.id.resubmitBTN);
        resubmitBTN.setVisibility(View.GONE);
        withdrawBTN = (Button) view.findViewById(R.id.withdrawBTN);
        withdrawBTN.setVisibility(View.GONE);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);
        expenseErrorLl = (LinearLayout) view.findViewById(R.id.errorTV);
        expenseErrorLl.setVisibility(View.GONE);
        advanceRV = (RecyclerView) view.findViewById(R.id.advance_expenseRecyclerView);
        advanceErrorLinearLayout = (LinearLayout) view.findViewById(R.id.advanceErrorLinearLayout);
        advanceErrorLinearLayout.setVisibility(View.GONE);
        advanceRV.setVisibility(View.GONE);
        requestVoucherLl = (LinearLayout) view.findViewById(R.id.requestVoucherLl);
        requestVoucherLl.setVisibility(View.GONE);
        paymentLinearLayout= (LinearLayout) view.findViewById(R.id.paymentLinearLayout);
        remarksRV = (RecyclerView) view.findViewById(R.id.remarksRV);
        remarksLinearLayout = (LinearLayout) view.findViewById(R.id.remarksLinearLayout);
        remarksLinearLayout.setVisibility(View.VISIBLE);

        // sendViewRequestSummaryData();
        sendExpenseInitData();

        rejectBTN = (Button) view.findViewById(R.id.rejectBTN);
        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Reject";
                sendExpenseClaimData();

            }
        });

        returnBTN = (Button) view.findViewById(R.id.returnBTN);
        returnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Return";
                sendExpenseClaimData();
            }
        });
        approvalBTN = (Button) view.findViewById(R.id.approvalBTN);
        approvalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Approve";
                sendExpenseClaimData();
            }
        });

        withdrawBTN = (Button) view.findViewById(R.id.withdrawBTN);
        withdrawBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Withdraw";
                sendExpenseClaimData();
            }
        });

        resubmitBTN = (Button) view.findViewById(R.id.resubmitBTN);
        resubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = AppsConstant.RESUBMIT;
                sendExpenseClaimData();
            }
        });
    }

    private void sendViewRequestSummaryData() {
        showHideProgressView(true);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getViewExpenseClaimSummaryData(expenseItemListModel.getReqID()),
                CommunicationConstant.API_GET_EXPENSE_CLAIM_DETAIL, true);
    }

    @Override
    public void validateResponse(ResponseData response) {

        switch (response.getRequestData().getReqApiId()) {

            case CommunicationConstant.API_GET_EXPENSE_PAGE_INIT:
                showHideProgressView(false);
                String strResponse = response.getResponseData();
                Log.d("TAG", "Advance Response : " + strResponse);
                expensePageInitResponseModel = GetExpensePageInitResponseModel.create(strResponse);
                if(expensePageInitResponseModel!=null && !expensePageInitResponseModel.getGetExpensePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    new AlertCustomDialog(getActivity(),expensePageInitResponseModel.getGetExpensePageInitResult().getErrorMessage());
                    return;
                }
                if(expensePageInitResponseModel!=null && expensePageInitResponseModel.getGetExpensePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)){
                    sendViewRequestSummaryData();
                }


                break;

            case CommunicationConstant.API_GET_EXPENSE_CLAIM_DETAIL:
                showHideProgressView(false);
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                viewClaimSummaryResponseModel = ViewClaimSummaryResponseModel.create(str);
                Log.d("TAG","Rejected Response : "+viewClaimSummaryResponseModel.serialize());
                if (viewClaimSummaryResponseModel != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult() != null && viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem() != null) {

                    updateUI(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem());
                    setupButtons();
                    refresh(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems());

                    if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList() != null) {

                        refreshAdjustmentRecycle(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList());
                    }

                    if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getRequestRemarks() != null) {
                        refreshRemarksList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getRequestRemarks());
                    }

                    if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getPaymentDetails() != null) {
                        refreshPaymentList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getPaymentDetails());
                    }

                    if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList() != null) {
                        refreshDocumentList(viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList());
                    }

                }

                break;

            case CommunicationConstant.API_GET_SAVE_EXPENSE:
                showHideProgressView(false);
                String responseData = response.getResponseData();
                Log.d("TAG", "Advance Response : " + responseData);
                expenseClaimResponseModel = ExpenseClaimResponseModel.create(responseData);
                if (expenseClaimResponseModel != null && expenseClaimResponseModel.getSaveExpenseResult() != null) {
                    MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                    if (menuItemModel != null) {
                        MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
                        if (itemModel != null && itemModel.isAccess()) {
                            progressBar.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            CustomDialog.alertOkWithFinishFragment(context, expenseClaimResponseModel.getSaveExpenseResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            CustomDialog.alertOkWithFinishFragment(context, expenseClaimResponseModel.getSaveExpenseResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        CustomDialog.alertWithOk(context, expenseClaimResponseModel.getSaveExpenseResult().getErrorMessage());
                    }
                }
                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void sendExpenseClaimData() {
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ArrayList<LineItemsModel> approvedList = viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getLineItems();
        for (int index = 0; index < approvedList.size(); index++) {
            LineItemsModel itemsModel = approvedList.get(index);
            itemsModel.setAmtApproved(itemsModel.getClaimAmt());
            approvedList.set(index, itemsModel);
        }


        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, expenseItemListModel.getReqID() + "",
                        approvedList,
                        viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getAdvanceList(),
                        description, remarks, currency, claimTypeID, projectId,
                        viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getDocList(), forEmpId,viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqStatus()+""),
                CommunicationConstant.API_GET_SAVE_EXPENSE, true);
    }

    private void updateUI(ViewExpenseItemModel item) {
        advanceVoucherTV.setText(item.getReqCode());
        claimForTV.setText(item.getName());
        descriptionTV.setText(item.getDescription());
        currencyTV.setText(item.getCurrencyCode());
        currency = item.getCurrencyCode();
        statusTV.setText(item.getReqStatusDesc());
        claimTypeTV.setText(item.getClaimTypeDesc());
        submittedByTV.setText(item.getSubmittedByName());
        submittedOnTV.setText(item.getReqDate());

        totalExpenseClaimedTV.setText(item.getTotalExpenseClaimed());
        netAmountTV.setText(item.getNetAmountToBePaid());
    /*    dateTV.setText(item.getReqDate());
        nameTV.setText(item.getSubmittedByName());
        remarksStatusTV.setText(item.getReqStatusDesc());*/

        forEmpId = item.getForEmpID() + "";
        if (item.getApproverID() != 0) {
            approverID = item.getApproverID() + "";
            approverName = item.getApproverName();
        }

        if (item.getDescription() != null) {
            description = item.getDescription();
        }

        if (item.getClaimTypeID() != 0) {
            claimTypeID = item.getClaimTypeID();
        }

        if (item.getProjectID() != 0) {
            projectId = item.getProjectID() + "";
        }

        if (item.getReqRemark() != null) {
            remarks = item.getReqRemark();
        }

       /* if (item.getReqStatusDesc().equalsIgnoreCase("Submitted")) {
            withdrawBTN.setVisibility(View.VISIBLE);
            withdrawBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromButton = "Withdraw";
                    sendExpenseClaimData();
                }
            });

        }

        if (item.getReqStatusDesc().equalsIgnoreCase("Returned")) {
            resubmitBTN.setVisibility(View.VISIBLE);
            resubmitBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromButton = "Resubmit";
                    sendExpenseClaimData();

                }
            });
        }*/
    }

    private void refresh(ArrayList<LineItemsModel> lineItemsModels) {
        if (lineItemsModels != null && lineItemsModels.size() > 0) {
            expenseErrorLl.setVisibility(View.GONE);
            expenseDetailsRecyclerView.setVisibility(View.VISIBLE);
            summaryAdapter = new ViewExpenseClaimSummaryAdapter(lineItemsModels);
            expenseDetailsRecyclerView.setAdapter(summaryAdapter);
            expenseDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            summaryAdapter.notifyDataSetChanged();

            //  layout.setVisibility(View.VISIBLE);

            // layout.setVisibility(View.GONE);
            if (lineItemsModels.size() > 0) {
                layout.setVisibility(View.VISIBLE);
            }
            double total = 0, totalApprovedAmount = 0;
            for (LineItemsModel item : lineItemsModels) {
                if(item.getClaimAmt()!=null && !item.getClaimAmt().equalsIgnoreCase(""))
                    total = total + Double.parseDouble(item.getClaimAmt());
                if(item.getAmtApproved()!=null && !item.getAmtApproved().equalsIgnoreCase(""))
                    totalApprovedAmount = totalApprovedAmount + Double.parseDouble(item.getAmtApproved());
            }

            totalTV.setText(total + "");
            Utility.formatAmount(totalTV,total);


            if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().
                    getReqStatusDesc().equalsIgnoreCase("Approved")) {
                totalApprovedAmountLl.setVisibility(View.VISIBLE);
                totalApprovedAmtTV.setText(totalApprovedAmount + "");
                Utility.formatAmount(totalApprovedAmtTV,totalApprovedAmount);
            }
        } else {
            expenseErrorLl.setVisibility(View.VISIBLE);
            expenseDetailsRecyclerView.setVisibility(View.GONE);

        }
    }

    private void refreshDocumentList(ArrayList<DocListModel> docListModels) {
        if (docListModels != null && docListModels.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            documentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            documentRV.setVisibility(View.VISIBLE);
            documentViewAdapter = new DocumentViewAdapter(docListModels);
            documentRV.setAdapter(documentViewAdapter);
            documentViewAdapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            documentRV.setVisibility(View.GONE);
        }
    }

    public void sendExpenseInitData() {
        showHideProgressView(true);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseInitData(),
                CommunicationConstant.API_GET_EXPENSE_PAGE_INIT, true);
    }
    private class ViewExpenseClaimSummaryAdapter extends
            RecyclerView.Adapter<ViewExpenseClaimSummaryAdapter.MyViewHolder> {
        private ArrayList<LineItemsModel> dataSet;
        private LinearLayout  lineDocumentLl;
        private int totalCount;
        private RecyclerView lineDocumentRecyclerView;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView fromDateTV, toDateTV, detailsTV, claimHeadTV, inputTV, amountTV,
                    totalAmountTV, approvedAmountTV, categoryDescTV,toDateLabel,fromDateLabel;
            private LinearLayout viewDocLl, approvedAmountLl, statusMsgLl, statusLl;
            private Button viewDocBTN, actionBTN, statusBT;
            private LinearLayout categoryLinearLayout,detailsLinearLayout,claimHeadLinearLayout,inputAmtLinearLayout,amountLinearLayout,fromDateLinearLayout,toDateLinearLayout;


            public MyViewHolder(View v) {
                super(v);
                toDateLabel = (TextView) v.findViewById(R.id.toDateLabel);
                fromDateLabel = (TextView) v.findViewById(R.id.fromDateLabel);
                viewDocLl = (LinearLayout) v.findViewById(R.id.viewDocLl);
                viewDocBTN = (Button) v.findViewById(R.id.viewDocBTN);
                actionBTN = (Button) v.findViewById(R.id.actionBTN);
                actionBTN.setVisibility(View.GONE);
                categoryDescTV = (TextView) v.findViewById(R.id.categoryDescTV);
                fromDateTV = (TextView) v.findViewById(R.id.fromDateTV);
                toDateTV = (TextView) v.findViewById(R.id.toDateTV);
                detailsTV = (TextView) v.findViewById(R.id.detailsTV);
                claimHeadTV = (TextView) v.findViewById(R.id.claimHeadTV);
                inputTV = (TextView) v.findViewById(R.id.inputTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);
                // totalAmountTV = (TextView) v.findViewById(R.id.totalAmountTV);
                categoryLinearLayout=(LinearLayout)  v.findViewById(R.id.categoryLinearLayout);
                detailsLinearLayout=(LinearLayout)  v.findViewById(R.id.detailsLinearLayout);
                claimHeadLinearLayout=(LinearLayout)  v.findViewById(R.id.claimHeadLinearLayout);
                amountLinearLayout=(LinearLayout)  v.findViewById(R.id.amountLinearLayout);

                fromDateLinearLayout = (LinearLayout) v.findViewById(R.id.fromDateLinearLayout);
                fromDateLinearLayout.setVisibility(View.GONE);
                lineDocumentLl = (LinearLayout) v.findViewById(R.id.lineDocumentLl);
                lineDocumentLl.setVisibility(View.GONE);

                inputAmtLinearLayout = (LinearLayout) v.findViewById(R.id.inputAmtLinearLayout);

                approvedAmountLl = (LinearLayout) v.findViewById(R.id.approvedAmountLl);
                approvedAmountTV = (TextView) v.findViewById(R.id.approvedAmountTV);
                approvedAmountLl.setVisibility(View.GONE);

                statusLl = (LinearLayout) v.findViewById(R.id.statusLl);
                statusLl.setVisibility(View.GONE);

                statusMsgLl = (LinearLayout) v.findViewById(R.id.statusMsgLl);
                statusMsgLl.setVisibility(View.GONE);
                statusBT = (Button) v.findViewById(R.id.statusBT);

                toDateLinearLayout= (LinearLayout) v.findViewById(R.id.toDateLinearLayout);

                // lineDocumentRecyclerView = (RecyclerView) v.findViewById(R.id.lineDocumentRecyclerView);

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
            //view.setOnClickListener(MainActivity.myOnClickListener);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final LineItemsModel item = dataSet.get(listPosition);
          /*  if (item.getCategoryID() == 1) {
                String[] fromDate = item.getDateFrom().split(" ");
                String[] toDate = item.getDateTo().split(" ");
                holder.fromDateTV.setText(fromDate[0]);
                holder.toDateTV.setText(toDate[0]);
                fromDateLinearLayout.setVisibility(View.VISIBLE);
            } else {
                String[] toDate = item.getDateFrom().split(" ");
                holder.toDateTV.setText(toDate[0]);
                fromDateLinearLayout.setVisibility(View.GONE);
            }*/

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
            holder.inputAmtLinearLayout.setVisibility(View.VISIBLE);
          //  if (item.getInputUnit() != null && !item.getInputUnit().equalsIgnoreCase("0.00")) {

                if (item.getCategoryID()== AppsConstant.PERIODIC_EXPENSE) {
                    holder.inputAmtLinearLayout.setVisibility(View.GONE);
                    holder.toDateLinearLayout.setVisibility(View.VISIBLE);
                    holder.toDateTV.setText(item.getDateTo());
                    holder.toDateLabel.setText("Period");
                }

            /*} else {
                holder.inputAmtLinearLayout.setVisibility(View.GONE);
                holder.amountTV.setText(item.getClaimAmt());
            }*/

            holder.categoryDescTV.setText(item.getCategoryDesc());
            holder.detailsTV.setText(item.getLineItemDetail());
            holder.claimHeadTV.setText(item.getHeadDesc());
            holder.inputTV.setText(item.getInputUnit());
            holder.amountTV.setText(item.getClaimAmt());
            holder.viewDocLl.setVisibility(View.GONE);
            if (item.getDocListLineItem() != null && item.getDocListLineItem().size() > 0) {
                holder.viewDocLl.setVisibility(View.VISIBLE);
                // holder.viewDocBTN.setText("Download Doc");
                holder.viewDocBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent theIntent=new Intent(getActivity(), ViewDocumentActivity.class);
                        theIntent.putExtra("LineItemDocument",item);
                        startActivity(theIntent);

                        /*ViewDocumentFragment viewExpenseSummary = new ViewDocumentFragment();
                        viewExpenseSummary.setLineItemsModel(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.add_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/
                    }
                });

            }

            if (viewClaimSummaryResponseModel.getGetExpenseDetailResult().getExpenseItem().getReqStatusDesc().equalsIgnoreCase("Approved")) {
                holder.approvedAmountLl.setVisibility(View.VISIBLE);
                holder.approvedAmountTV.setText(item.getAmtApproved());

            }

            if (!item.getPolicyID().equalsIgnoreCase("")) {
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
//            holder.approvedAmountTV.setText(item.getAmtApprovedLevel1());
         /*   if (item.getDocListLineItem() != null && item.getDocListLineItem().size() > 0) {
                lineDocumentLl.setVisibility(View.VISIBLE);
                lineDocumentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                lineDocumentViewAdapter = new LineDocumentViewAdapter(item.getDocListLineItem());
                lineDocumentRecyclerView.setAdapter(documentViewAdapter);
                lineDocumentViewAdapter.notifyDataSetChanged();
            }*/
            setLineItemLable(holder,item);

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
            ArrayList<CategoryLineItemLabelItem> labelItemArrayList=expensePageInitResponseModel.getGetExpensePageInitResult().getCategoryLineItemLabel();

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
                       // holder.inputAmtLinearLayout.setVisibility(View.VISIBLE);
                        ((TextView)holder.inputAmtLinearLayout.findViewById(R.id.inputLabelTV)).setText(column.getLableName());
                    }
                }
            }

        }


    }


  /*  private class LineDocumentViewAdapter extends
            RecyclerView.Adapter<LineDocumentViewAdapter.MyViewHolder> {
        private ArrayList<SupportDocsItemModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView fileNameTV, filedescriptionTV;
            public Button downloadBTN;

            public MyViewHolder(View v) {
                super(v);

                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                filedescriptionTV = (TextView) v.findViewById(R.id.filedescriptionTV);
                downloadBTN = (Button) v.findViewById(R.id.downloadBTN);


            }
        }

        public void addAll(List<SupportDocsItemModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public LineDocumentViewAdapter(List<SupportDocsItemModel> data) {
            this.dataSet = (ArrayList<SupportDocsItemModel>) data;

        }

        @Override
        public LineDocumentViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.document_item, parent, false);
            LineDocumentViewAdapter.MyViewHolder myViewHolder = new LineDocumentViewAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final LineDocumentViewAdapter.MyViewHolder holder, final int listPosition) {

            final SupportDocsItemModel item = dataSet.get(listPosition);
            holder.fileNameTV.setText(item.getDocFile());
            holder.filedescriptionTV.setText(item.getName());
            holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = item.getDocPath().replace("~", "");
                    String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();
                    new DownloadFile().execute(path, item.getName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }*/

    private class DocumentViewAdapter extends
            RecyclerView.Adapter<DocumentViewAdapter.MyViewHolder> {
        private ArrayList<DocListModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView fileNameTV, filedescriptionTV;
            public Button downloadBTN;
            private ImageView img_menu_icon;

            public MyViewHolder(View v) {
                super(v);

                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                filedescriptionTV = (TextView) v.findViewById(R.id.filedescriptionTV);
                downloadBTN = (Button) v.findViewById(R.id.downloadBTN);
                img_menu_icon = (ImageView) v.findViewById(R.id.img_icon_rounded);


            }
        }

        public void addAll(List<DocListModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public DocumentViewAdapter(List<DocListModel> data) {
            this.dataSet = (ArrayList<DocListModel>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.document_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final DocListModel item = dataSet.get(listPosition);
            holder.fileNameTV.setText(item.getDocFile());
            holder.filedescriptionTV.setText(item.getName());
            //  holder.downloadBTN.setVisibility(View.GONE);
            holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = item.getDocPath().replace("~", "");
                    String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();
                    Utility.downloadPdf(path,null,item.getDocFile(),context,getActivity());
                    // new DownloadFile().execute(path, item.getName());
                }
            });



            holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Download");
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
                                editFilenameET.setText(item.getName());

                              /*  (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  SupportDocsItemModel docItem = new SupportDocsItemModel();
                                        item.setName(editFilenameET.getText().toString());
                                        if (dataSet != null && dataSet.size() > 0) {
                                            dataSet.set(dataSet.indexOf(item), item);

                                        } else {
                                            dataSet = new ArrayList<DocListModel>();
                                            dataSet.add(item);
                                        }
                                        refreshDocumentList();
                                        dialog.dismiss();

                                    }
                                });*/
                                (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                                dataSet.remove(listPosition);
                                  notifyDataSetChanged();
                                /*if (mDataset.size() == 0) {
                                    errorTV.setVisibility(View.VISIBLE);
                                }*/

                            }else if (selectedObject.toString().equalsIgnoreCase("Download")) {
                                String filePath = item.getDocPath().replace("~", "");
                                String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();

                                Utility.downloadPdf(path,null,item.getDocFile(),context,getActivity());
                            }
                            builder.dismiss();
                        }
                    });
                    customBuilder.show();
                }
            });

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

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, "ProgressDialog", "Downloading File");
        }


        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FileEazeWork");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ViewAdvanceRequestSummaryFragment.FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progress.dismiss();
        }
    }


    private class AdjustmentDetailAdapter extends RecyclerView.Adapter<AdjustmentDetailAdapter.ViewHolder> {
        private List<AdvanceListItemModel> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView reasonTV, amountTV, requestIdTV;
            public Button deleteBTN;


            public ViewHolder(View v) {
                super(v);
                requestIdTV = (TextView) v.findViewById(R.id.requestTV);
                reasonTV = (TextView) v.findViewById(R.id.reasonTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);
                deleteBTN = (Button) v.findViewById(R.id.deleteBTN);
                deleteBTN.setVisibility(View.GONE);


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

            holder.requestIdTV.setText(item.getReqCode() + "");
            holder.reasonTV.setText(item.getReason());
            holder.amountTV.setText(item.getAdjAmount());

        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }

    private void refreshAdjustmentRecycle(ArrayList<AdvanceListItemModel> list) {
        advanceRV.setVisibility(View.GONE);
        advanceErrorLinearLayout.setVisibility(View.VISIBLE);
        Log.d("TAG"," Log H erer");
        if (list != null && list.size() > 0) {
            advanceRV.setVisibility(View.VISIBLE);
            advanceErrorLinearLayout.setVisibility(View.GONE);
            advanceRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            adjustemntDetailAdapter = new AdjustmentDetailAdapter(list);
            advanceRV.setAdapter(adjustemntDetailAdapter);
            adjustemntDetailAdapter.notifyDataSetChanged();
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
    private void setupButtons(){
        if(viewClaimSummaryResponseModel!=null && viewClaimSummaryResponseModel.getGetExpenseDetailResult().getButtons()!=null){
            for(String button : viewClaimSummaryResponseModel.getGetExpenseDetailResult().getButtons() ){
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
  /*  public static class FileDownloader {
        private static final int MEGABYTE = 1024 * 1024;

        public static void downloadFile(String fileUrl, File directory) {
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                //  InputStream inputStream = urlConnection.getInputStream();
                InputStream inputStream = new URL(fileUrl).openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();
                //Toast.makeText(context,"Starting PDF download...",Toast.LENGTH_LONG).show();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                   *//* downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalSize) * 100;
                    Toast.makeText(context,"Total PDF File size  : "
                            + (totalsize / 1024)
                            + " KB\n\nDownloading PDF " + (int) per
                            + "% complete",Toast.LENGTH_LONG).show();*//*
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}