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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import hr.eazework.com.AddExpenseActivity;
import hr.eazework.com.FileUtils;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceAdjustmentResponseModel;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.CategoryLineItemLabelItem;
import hr.eazework.com.model.ClaimTypeListItem;
import hr.eazework.com.model.ClaimTypeListModel;
import hr.eazework.com.model.CurrencyListModel;
import hr.eazework.com.model.DocListModel;
import hr.eazework.com.model.EmployeeListModel;
import hr.eazework.com.model.EvtClaimTypeChangeResult;
import hr.eazework.com.model.ExpenseClaimResponseModel;
import hr.eazework.com.model.ExpenseDetailItemModel;
import hr.eazework.com.model.GetAdvanceDetailResultModel;
import hr.eazework.com.model.GetAdvanceListForExpenseResult;
import hr.eazework.com.model.GetApproverResponseModel;
import hr.eazework.com.model.GetExpensePageInitResponseModel;
import hr.eazework.com.model.LineItemColumnsItem;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.OnBehalfOfListModel;
import hr.eazework.com.model.PeriodicExpenseResponseModel;
import hr.eazework.com.model.ProjectListItem;
import hr.eazework.com.model.ProjectListModel;
import hr.eazework.com.model.ProjectListResponseModel;
import hr.eazework.com.model.SaveExpenseItem;
import hr.eazework.com.model.SaveExpenseModel;
import hr.eazework.com.model.SaveExpenseRequestModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
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

import static android.app.Activity.RESULT_OK;
import static hr.eazework.com.ui.util.ImageUtil.rotateImage;


/**
 * Created by Dell3 on 16-08-2017.
 */

public class AddExpenseClaimFragment extends BaseFragment {
    boolean isClickedSubmit;
    String description = "", remarks = "", totalExpenseClaimed = "";
    private double totalExpenseAmt = 0;
    private double totalAdvanceAdjustInCaseExpenseSumLesser;
    private double balanceAmt = 0;
    private PeriodicExpenseResponseModel periodicExpenseResponseModel;
    private Context context;
    public static final String TAG = "AddExpenseClaimFragment";
    private Preferences preferences;
    private ImageView add_expenseIV, advance_expenseIV, plus_create_newIV;
    private RecyclerView expenseDetailsRecyclerView, advance_expenseRecyclerView, expenseRecyclerView;
    private AdjustmentDetailAdapter adjustemntDetailAdapter;
    private String currency = null;
    private TextView claimTypeTV;
    private TextView currencyTV;
    private TextView projectTV, totalExpenseClaimedTV, netAmountTV;
    private TextView approverTV;
    private TextView onBehalfTV, requestTV;
    private LinearLayout errorDocTV, claimLinearLayout;
    private EditText detailsET, remarksET;
    private GetExpensePageInitResponseModel expensePageInitResponseModel;
    private ClaimTypeListItem claimTypeListItems;
    private int claimTypeID, empId;
    private GetApproverResponseModel getApproverResponseModel;
    private ProjectListResponseModel projectListResponseModel;
    private AdvanceAdjustmentResponseModel advanceAdjustmentResponseModel;
    private ExpenseClaimResponseModel expenseClaimResponseModel;
    private ProjectListItem projectListItem;
    private EmployeeListModel employeeList;
    private String projectId, loginUserName, projectName;
    private LinearLayout projectLinearLayout, onBehalfLinearLayout, errorTV, approvalLl;
    private ExpenseClaimDetailsAdapter expenseClaimDetailsAdapter;
    private ArrayList<LineItemsModel> lineItemsList = new ArrayList<LineItemsModel>();
    private LineItemsModel lineData;
    private SaveExpenseRequestModel saveExpenseRequestModel;
    private ArrayList<DocListModel> uploadFileList;
    private static int UPLOAD_DOC_REQUEST = 1;
    private CurrencyListModel currencyListModel;
    private OnBehalfOfListModel onBehalfOfListModel;
    private String loginEmpId;
    private GetAdvanceDetailResultModel getAdvanceDetailResultModel;
    private ArrayList<AdvanceListItemModel> advanceList;
    private String currencyValue = "", requestCode, reasonCode, amount;
    private double totalAmountTobeAdjusted;
    private String fromButton;
    private ProgressBar progressBar;
    private String approverID = "", approverName = "";
    private ClaimTypeListModel claimTypeListModel;
    private Button saveDraftBTN;
    private AdvanceListItemModel item;
    private Bitmap bitmap = null;
    private String purpose = "";
    private String reqStatus="1";
    private TextView totalTV;
    private LinearLayout advance_adjustment_Ll;

    public SaveExpenseRequestModel getSaveExpenseRequestModel() {
        return saveExpenseRequestModel;
    }

    public void setSaveExpenseRequestModel(SaveExpenseRequestModel saveExpenseRequestModel) {
        this.saveExpenseRequestModel = saveExpenseRequestModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_claim_fragment, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        loginEmpId = loginUserModel.getUserModel().getEmpId();
        advance_expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.advance_expenseRecyclerView);
        advance_expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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


        approvalLl = (LinearLayout) rootView.findViewById(R.id.approvalLl);
        add_expenseIV = (ImageView) rootView.findViewById(R.id.add_expenseIV);
        onBehalfTV = (TextView) rootView.findViewById(R.id.onBehalfTV);
        onBehalfTV.setOnClickListener(this);
        claimTypeTV = (TextView) rootView.findViewById(R.id.claimTypeTV);
        claimTypeTV.setOnClickListener(this);
        claimLinearLayout = (LinearLayout) rootView.findViewById(R.id.claimLinearLayout);
        claimLinearLayout.setVisibility(View.GONE);

        currencyTV = (TextView) rootView.findViewById(R.id.currencyTV);
        currencyTV.setOnClickListener(this);
        projectTV = (TextView) rootView.findViewById(R.id.projectTV);
        projectTV.setOnClickListener(this);
        requestTV = (TextView) rootView.findViewById(R.id.requestTV);
        requestTV.setOnClickListener(this);
        approverTV = (TextView) rootView.findViewById(R.id.approverTV);
        remarksET = (EditText) rootView.findViewById(R.id.remarksET);
        totalExpenseClaimedTV = (TextView) rootView.findViewById(R.id.totalExpenseClaimedTV);
        netAmountTV = (TextView) rootView.findViewById(R.id.netAmountTV);
        advance_expenseIV = (ImageView) rootView.findViewById(R.id.advance_expenseIV);
        projectLinearLayout = (LinearLayout) rootView.findViewById(R.id.projectLinearLayout);
        projectLinearLayout.setVisibility(View.GONE);
        onBehalfLinearLayout = (LinearLayout) rootView.findViewById(R.id.onBehalfLinearLayout);
        detailsET = (EditText) rootView.findViewById(R.id.detailsET);
        totalTV = (TextView) rootView.findViewById(R.id.totalAmountTV);


        saveDraftBTN = (Button) rootView.findViewById(R.id.saveDraftBTN);
        saveDraftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Save";
                if(isClickedSubmit==false) {
                    isClickedSubmit=true;
                    sendExpenseClaimData();

                }

            }
        });


        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Expense Claim");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton = "Submit";
                if(isClickedSubmit==false) {
                    isClickedSubmit=true;
                    sendExpenseClaimData();

                }
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                    }
                }
            }
        });

        add_expenseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (claimTypeListItems == null && expensePageInitResponseModel.getGetExpensePageInitResult().getClaimTypeYN().equalsIgnoreCase("Y")) {
                    new AlertCustomDialog(context, "Please select claim type");
                } else if (currencyValue.equalsIgnoreCase("")) {
                    new AlertCustomDialog(context, "Please select currency");
                } else {
                    updateExpenseOnAddClick();
                    Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                    AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                    startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                }

            }
        });

        expenseDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseDetailsRecyclerView);
        expenseDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        errorTV = (LinearLayout) rootView.findViewById(R.id.errorTV);
        errorTV.setVisibility(View.VISIBLE);
        sendExpenseInitData();

        loginUserName = loginUserModel.getUserModel().getLoginId();
        if (loginUserName.equalsIgnoreCase("admin")) {
            onBehalfLinearLayout.setVisibility(View.VISIBLE);

        } else {
            onBehalfLinearLayout.setVisibility(View.GONE);
        }

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
                                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !PermissionUtil.checkLocationPermission(getContext())) {
                                        PermissionUtil.askAllPermission(AddExpenseClaimFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && PermissionUtil.checkLocationPermission(getContext())) {
                                        if (Utility.isLocationEnabled(getContext())) {
                                            if (Utility.isNetworkAvailable(getContext())) {
                                                Utility.openCamera(getActivity(), AddExpenseClaimFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore");
                                                customBuilder.dismiss();
                                            } else {
                                                Utility.showNetworkNotAvailableDialog(getContext());
                                            }
                                        } else {
                                            Utility.requestToEnableGPS(getContext(), new Preferences(getContext()));
                                        }
                                    } else {
                                        Utility.displayMessage(getContext(), "Please provide all permission");
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

        errorDocTV = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorDocTV.setVisibility(View.VISIBLE);
        expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(expenseRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_line));
        expenseRecyclerView.addItemDecoration(itemDecoration);
        if (saveExpenseRequestModel != null) {
            setUpData();
        } else {
            saveExpenseRequestModel = new SaveExpenseRequestModel();
            saveExpenseRequestModel.setExpense(new SaveExpenseModel());
            saveExpenseRequestModel.getExpense().setExpenseItem(new SaveExpenseItem());
            uploadFileList = new ArrayList<DocListModel>();
        }


        return rootView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.claimTypeTV:
                if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult().getClaimTypeYN().equalsIgnoreCase("Y")) {
                    claimTypeListModel = expensePageInitResponseModel.getGetExpensePageInitResult().getClaimTypeList();
                    if (claimTypeListModel != null) {
                        final ArrayList<ClaimTypeListItem> claimTypeList = claimTypeListModel.getClaimTypeList();

                        CustomBuilder claimDialog = new CustomBuilder(getContext(), "Select Claim Type", true);
                        claimDialog.setSingleChoiceItems(claimTypeList, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                claimTypeListItems = (ClaimTypeListItem) selectedObject;
                                if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                                        && saveExpenseRequestModel.getExpense().getExpenseItem() != null) {
                                    if (saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID() != claimTypeListItems.getClaimTypeID()) {

                                        // line item list
                                        saveExpenseRequestModel.getExpense().getExpenseItem().setLineItems(new ArrayList<LineItemsModel>());
                                        refreshLineItemList();

                                        //advance list
                                        advanceList = new ArrayList<AdvanceListItemModel>();
                                        saveExpenseRequestModel.getExpense().getExpenseItem().setAdvanceList(advanceList);
                                        refreshAdjustmentRecycle(saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList());

                                        updateNetAmount();
                                    }
                                }
                                claimTypeTV.setText(claimTypeListItems.getClaimType());
                                claimTypeID = claimTypeListItems.getClaimTypeID();

                                saveExpenseRequestModel.getExpense().getExpenseItem().setClaimTypeID(claimTypeID);
                                saveExpenseRequestModel.getExpense().getExpenseItem().setClaimTypeDesc(claimTypeListItems.getClaimType());
                                sendExpenseApproverData();
                                sendProjectData();
                                builder.dismiss();

                            }
                        });
                        claimDialog.show();
                    } else {

                    }
                } else {

                }
                break;
            case R.id.currencyTV:
                currencyListModel = expensePageInitResponseModel.getGetExpensePageInitResult().getCurrencyList();

                if (currencyListModel != null) {
                    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(currencyListModel.getCurrencyList()));
                    CustomBuilder currencyDialog = new CustomBuilder(getContext(), "Select Currency", true);
                    currencyDialog.setSingleChoiceItems(arrayList, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            currencyValue = (String) selectedObject;
                            currencyTV.setText(currencyValue);
                            sendAdvanceAdjustmentData();
                            builder.dismiss();

                        }
                    });
                    currencyDialog.show();
                }
                break;
            case R.id.projectTV:
                EvtClaimTypeChangeResult evtClaimTypeChangeResult = projectListResponseModel.getEvtClaimTypeChangeResult();
                if (evtClaimTypeChangeResult != null && evtClaimTypeChangeResult.getProjectYN().equalsIgnoreCase("Y")) {
                    ProjectListModel projectListModel = evtClaimTypeChangeResult.getProjectList();

                    ArrayList<ProjectListItem> projectList = projectListModel.getProjectList();
                    if (projectList != null && projectList.size() > 0) {
                        CustomBuilder projectDialog = new CustomBuilder(getContext(), "Select Project", true);
                        projectDialog.setSingleChoiceItems(projectList, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                projectListItem = (ProjectListItem) selectedObject;
                                projectTV.setText(projectListItem.getProjectName());
                                projectName = projectListItem.getProjectName();
                                projectId = projectListItem.getProjectID();
                                saveExpenseRequestModel.getExpense().getExpenseItem().setProjectID(Integer.parseInt(projectId));
                                saveExpenseRequestModel.getExpense().getExpenseItem().setProjectName(projectName);

                                sendExpenseApproverData();

                                builder.dismiss();
                            }
                        });
                        projectDialog.show();

                    } else {
                        Toast.makeText(context, "No project allotted", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.onBehalfTV:
                if (expensePageInitResponseModel != null &&
                        expensePageInitResponseModel.getGetExpensePageInitResult() != null &&
                        expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
                    onBehalfOfListModel = expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfList();
                    if (onBehalfOfListModel != null) {
                        ArrayList<EmployeeListModel> employeeListModels = onBehalfOfListModel.getEpmployeeList();

                        CustomBuilder onBehalfDialog = new CustomBuilder(getContext(), "Select On Behalf", true);
                        onBehalfDialog.setSingleChoiceItems(employeeListModels, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                employeeList = (EmployeeListModel) selectedObject;
                                onBehalfTV.setText(employeeList.getName());
                                empId = employeeList.getEmpID();
                                sendExpenseApproverData();
                                saveExpenseRequestModel.getExpense().getExpenseItem().setForEmpID(empId);
                                saveExpenseRequestModel.getExpense().getExpenseItem().setEmpName(employeeList.getName());
                                builder.dismiss();
                            }
                        });
                        onBehalfDialog.show();
                    }
                }
                break;
            default:
                break;
        }
        super.onClick(v);
    }

    private void refreshLineItemList() {
        if(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().size()>0 ){
            errorTV.setVisibility(View.GONE);
            expenseDetailsRecyclerView.setVisibility(View.VISIBLE);
        }
        expenseClaimDetailsAdapter = new ExpenseClaimDetailsAdapter(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems());
        expenseDetailsRecyclerView.setAdapter(expenseClaimDetailsAdapter);
        expenseClaimDetailsAdapter.notifyDataSetChanged();
        updateNetAmount();
    }


    public void sendExpenseInitData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseInitData(),
                CommunicationConstant.API_GET_EXPENSE_PAGE_INIT, true);
    }

    public String[] sendPeriodicMonthData() {
        String[] monthList = null;
        ArrayList<String> list = new ArrayList<>();
        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null &&
                saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().size() > 0) {
            for (LineItemsModel itemsModel : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                if (itemsModel.getCategoryID() == 4) {
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



    public void sendExpenseApproverData() {
        if (projectId == null || projectId.equalsIgnoreCase("")) {
            projectId = "0";
        }
        if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null
                && expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getExpenseApproverData(claimTypeID, empId, projectId),
                    CommunicationConstant.API_GET_APPROVER_DETAILS, true);
        } else {
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getExpenseApproverData(claimTypeID, Integer.parseInt(loginEmpId), projectId),
                    CommunicationConstant.API_GET_APPROVER_DETAILS, true);
        }
    }

    public void sendAdvanceAdjustmentData() {

        if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getAdvanceAdjustmentData(currencyValue, String.valueOf(empId)),
                    CommunicationConstant.API_GET_ADVANCE_LIST_FOR_EXPENSE, true);
        } else {

            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getAdvanceAdjustmentData(currencyValue, loginEmpId),
                    CommunicationConstant.API_GET_ADVANCE_LIST_FOR_EXPENSE, true);
        }


    }

    public void sendProjectData() {

        if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {

            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getProjectData(claimTypeID, empId),
                    CommunicationConstant.API_GET_PROJECT_LIST_DETAILS, true);

        } else {
            CommunicationManager.getInstance().sendPostRequest(this,
                    AppRequestJSONString.getProjectData(claimTypeID, Integer.parseInt(loginEmpId)),
                    CommunicationConstant.API_GET_PROJECT_LIST_DETAILS, true);
        }
    }

    public void sendExpenseClaimData() {

        totalExpenseClaimed = totalExpenseClaimedTV.getText().toString();
        description = detailsET.getText().toString();
        remarks = remarksET.getText().toString();
        currency = currencyTV.getText().toString();
        String requestId = 0 + "";

        approverName = approverTV.getText().toString();

        if(fromButton.equalsIgnoreCase("Submit")) {

            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (claimTypeListItems == null && expensePageInitResponseModel.getGetExpensePageInitResult().getClaimTypeYN().equalsIgnoreCase("Y")) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                isClickedSubmit=false;
                new AlertCustomDialog(context, "Please Select Claim Type");
                return;

            } else if (currencyListModel == null) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                isClickedSubmit=false;
                new AlertCustomDialog(context, "Please Select Currency");
                return;

            } else if (description.equalsIgnoreCase("")) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                isClickedSubmit=false;
                new AlertCustomDialog(context, "Please Enter Description");
                return;

            } else {
                if (projectListItem != null && !projectListItem.getProjectID().equalsIgnoreCase("0")) {
                } else {
                    projectId = 0 + "";
                }

                if (saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null &&
                        saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {

                    if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null &&
                            expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN() != null &&
                            expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
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
                                model.setBitmap(null);
                                model.setSeqNo(i + 1);
                                uploadFileList.set(i, model);
                            }
                        }



                        String[] monthList = sendPeriodicMonthData();
                        if (monthList != null && monthList.length > 0) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getPeriodicMonthData(empId, 0, monthList),
                                    CommunicationConstant.API_GET_MONTH_LIST, true);
                        } else {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, requestId, saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(empId),reqStatus),
                                    CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                        }

                    } else {

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
                                uploadFileList.set(i, model);
                            }
                        }
                        String[] monthList = sendPeriodicMonthData();
                        if (monthList != null && monthList.length > 0) {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getPeriodicMonthData(Integer.parseInt(loginEmpId), 0, monthList),
                                    CommunicationConstant.API_GET_MONTH_LIST, true);
                        } else {
                            CommunicationManager.getInstance().sendPostRequest(this,
                                    AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, requestId, saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(loginEmpId),reqStatus),
                                    CommunicationConstant.API_GET_SAVE_EXPENSE, true);

                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    isClickedSubmit=false;
                    new AlertCustomDialog(context, "Add Expense Detail");

                }
            }
        }

        if(fromButton.equalsIgnoreCase("Save")){
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (projectListItem != null && !projectListItem.getProjectID().equalsIgnoreCase("0")) {
            } else {
                projectId = 0 + "";
            }

            if (saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null) {
                if (expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
                    if(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()==null){
                        saveExpenseRequestModel.getExpense().getExpenseItem().setLineItems(new ArrayList<LineItemsModel>());
                    }

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
                            model.setBitmap(null);
                            model.setSeqNo(i + 1);
                            uploadFileList.set(i, model);
                        }
                    }


                    String[] monthList = sendPeriodicMonthData();
                    if (monthList != null && monthList.length > 0) {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getPeriodicMonthData(empId, 0, monthList),
                                CommunicationConstant.API_GET_MONTH_LIST, true);
                    } else {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, requestId, saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(empId),reqStatus),
                                CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                    }

                } else {
                    if(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()==null){
                        saveExpenseRequestModel.getExpense().getExpenseItem().setLineItems(new ArrayList<LineItemsModel>());
                    }

                    if (advanceList!=null && advanceList.size() > 0) {
                        for (int i = 0; i < advanceList.size(); i++) {
                            AdvanceListItemModel model = advanceList.get(i);
                            model.setSeqNo(i + 1);
                            advanceList.set(i, model);
                        }
                    }else{
                        advanceList=new ArrayList<>();
                    }

                    if (uploadFileList!=null && uploadFileList.size() > 0) {
                        for (int i = 0; i < uploadFileList.size(); i++) {
                            DocListModel model = uploadFileList.get(i);
                            model.setSeqNo(i + 1);
                            uploadFileList.set(i, model);
                        }
                    }

                    String[] monthList = sendPeriodicMonthData();
                    if (monthList!=null && monthList.length > 0) {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getPeriodicMonthData(Integer.parseInt(loginEmpId), 0, monthList),
                                CommunicationConstant.API_GET_MONTH_LIST, true);
                    } else {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, requestId, saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(), advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(loginEmpId),reqStatus),
                                CommunicationConstant.API_GET_SAVE_EXPENSE, true);

                    }
                }
            }
        }

    }

    private void updateExpenseOnAddClick() {
        saveExpenseRequestModel.setScreenName(AppsConstant.ADD_EXPENSE_CLAIM_FRAGMENT);
        saveExpenseRequestModel.getExpense().getExpenseItem().setDescription(detailsET.getText().toString());
        if (!remarksET.getText().toString().equalsIgnoreCase("")) {
            saveExpenseRequestModel.getExpense().getExpenseItem().setReqRemark(remarksET.getText().toString());
        }
        if (!totalExpenseClaimedTV.getText().toString().equalsIgnoreCase("")) {
            saveExpenseRequestModel.getExpense().getExpenseItem().setTotalExpenseClaimedAmount(totalExpenseClaimedTV.getText().toString());
        }
        if (!netAmountTV.getText().toString().equalsIgnoreCase("")) {
            saveExpenseRequestModel.getExpense().getExpenseItem().setNetAmount(netAmountTV.getText().toString());
        }
        saveExpenseRequestModel.getExpense().getExpenseItem().setCurrencyCode(currencyTV.getText().toString());
        int reqId = 0;
        saveExpenseRequestModel.getExpense().getExpenseItem().setAdvanceList(advanceList);

        saveExpenseRequestModel.getExpense().getExpenseItem().setDocList(uploadFileList);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EXPENSE_PAGE_INIT:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                expensePageInitResponseModel = GetExpensePageInitResponseModel.create(str);
                if(expensePageInitResponseModel != null && !expensePageInitResponseModel.getGetExpensePageInitResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)){
                    new AlertCustomDialog(getActivity(),expensePageInitResponseModel.getGetExpensePageInitResult().getErrorMessage());
                    return;
                }
                if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null &&
                        expensePageInitResponseModel.getGetExpensePageInitResult().getDocValidation() != null) {
                    saveExpenseRequestModel.getExpense().getExpenseItem().setDocValidation(expensePageInitResponseModel.
                            getGetExpensePageInitResult().getDocValidation());
                    saveExpenseRequestModel.setPageInitResultModel(expensePageInitResponseModel.getGetExpensePageInitResult());
                }
                if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null
                        && expensePageInitResponseModel.getGetExpensePageInitResult().getClaimTypeYN().equalsIgnoreCase("Y")) {
                    claimLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    claimLinearLayout.setVisibility(View.GONE);
                }

                if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null
                        && expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
                    onBehalfLinearLayout.setVisibility(View.VISIBLE);
                    if (expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfList() != null) {
                        OnBehalfOfListModel onBehalfOfListModel = expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfList();
                        if (onBehalfOfListModel != null) {
                            ArrayList<EmployeeListModel> employeeListModels = onBehalfOfListModel.getEpmployeeList();
                            for (EmployeeListModel item : employeeListModels) {
                                if (saveExpenseRequestModel != null) {
                                    if (saveExpenseRequestModel.getExpense() != null) {
                                        if (saveExpenseRequestModel.getExpense().getExpenseItem() != null &&
                                                saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID() == item.getEmpID()) {
                                            onBehalfTV.setText(item.getName());
                                            empId = item.getEmpID();
                                            employeeList = item;
                                            sendProjectData();
                                            sendExpenseApproverData();
                                            break;
                                        }
                                    }
                                }
                                if (item.getEmpID() == Integer.parseInt(loginEmpId)) {
                                    onBehalfTV.setText(item.getName());
                                    empId = item.getEmpID();
                                    employeeList = item;
                                    sendProjectData();
                                    sendExpenseApproverData();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    sendProjectData();
                    sendExpenseApproverData();
                    onBehalfLinearLayout.setVisibility(View.GONE);
                }

                if (expensePageInitResponseModel.getGetExpensePageInitResult().getCurrencyList() != null &&
                        expensePageInitResponseModel.getGetExpensePageInitResult().getCurrencyList().getCurrencyList() != null) {
                    currencyListModel = expensePageInitResponseModel.getGetExpensePageInitResult().getCurrencyList();
                    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(currencyListModel.getCurrencyList()));
                    if (arrayList.size() == 1) {
                        currencyValue = arrayList.get(0);
                        currencyTV.setText(arrayList.get(0));
                        sendAdvanceAdjustmentData();
                    }

                }
                if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
                    if (employeeList != null) {
                        saveExpenseRequestModel.getExpense().getExpenseItem().setForEmpID(empId);
                        saveExpenseRequestModel.getExpense().getExpenseItem().setEmpName(employeeList.getName());
                    }

                } else {
                    saveExpenseRequestModel.getExpense().getExpenseItem().setForEmpID(Integer.parseInt(loginEmpId));
                }


                break;
            case CommunicationConstant.API_GET_APPROVER_DETAILS:
                String strRes = response.getResponseData();
                Log.d("TAG", "Advance Response : " + strRes);
                getApproverResponseModel = GetApproverResponseModel.create(strRes);
                if (getApproverResponseModel != null && getApproverResponseModel.getGetApproverDetailsResult() != null
                        && getApproverResponseModel.getGetApproverDetailsResult().getEmpCode()!=null) {
                    approverTV.setText(getApproverResponseModel.getGetApproverDetailsResult().getName());
                    approverID = getApproverResponseModel.getGetApproverDetailsResult().getEmpID()+"";
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverID(approverID);
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverName(getApproverResponseModel.getGetApproverDetailsResult().getName());

                }else {
                    approverTV.setText("");
                    approverID=0+"";
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverID(approverID);
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverName(getApproverResponseModel.getGetApproverDetailsResult().getName());
                }

                break;
            case CommunicationConstant.API_GET_PROJECT_LIST_DETAILS:
                String projectResponse = response.getResponseData();
                Log.d("TAG", "Advance Response : " + projectResponse);
                projectListResponseModel = ProjectListResponseModel.create(projectResponse);
                if (projectListResponseModel != null && projectListResponseModel.getEvtClaimTypeChangeResult() != null
                        && projectListResponseModel.getEvtClaimTypeChangeResult().getProjectList() != null &&
                        projectListResponseModel.getEvtClaimTypeChangeResult().getProjectList().getProjectList() != null
                        && projectListResponseModel.getEvtClaimTypeChangeResult().getProjectYN().equalsIgnoreCase("Y")) {
                    projectLinearLayout.setVisibility(View.VISIBLE);
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverName(getApproverResponseModel.getGetApproverDetailsResult().getName());
                    saveExpenseRequestModel.getExpense().getExpenseItem().setApproverID(approverID);

                } else {
                    projectLinearLayout.setVisibility(View.GONE);
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
                if (periodicExpenseResponseModel != null && periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult()!=null
                        && !periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult().getErrorCode().equalsIgnoreCase("0")) {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new AlertCustomDialog(getActivity(), periodicExpenseResponseModel.getValidateMonthListForPeriodicExpenseResult().getErrorMessage());
                return;
                }else {

                    if (expensePageInitResponseModel != null && expensePageInitResponseModel.getGetExpensePageInitResult() != null &&
                            expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN() != null &&
                            expensePageInitResponseModel.getGetExpensePageInitResult().getOnBehalfOfYN().equalsIgnoreCase("Y")) {
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, "0", saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(),
                                        advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(empId),reqStatus),
                                CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                    }else{
                        CommunicationManager.getInstance().sendPostRequest(this,
                                AppRequestJSONString.getExpenseClaimData(fromButton, approverName, approverID, "0", saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems(),
                                        advanceList, description, remarks, currency, claimTypeID, projectId, uploadFileList, String.valueOf(loginEmpId),reqStatus),
                                CommunicationConstant.API_GET_SAVE_EXPENSE, true);
                    }
                }

                break;

            case CommunicationConstant.API_GET_SAVE_EXPENSE:
                isClickedSubmit=false;
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


    private class ExpenseClaimDetailsAdapter extends
            RecyclerView.Adapter<ExpenseClaimDetailsAdapter.MyViewHolder> {
        private ArrayList<LineItemsModel> dataSet;
        private LinearLayout viewDocLl;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView fromDateLabel, toDateLabel, fromDateTV, toDateTV, detailsTV, claimHeadTV, inputTV, amountTV, totalAmountTV, approvedAmountTV, categoryDescTV;
            private LinearLayout statusMsgLl, statusLl;
            private Button actionBTN, viewDocBTN, statusBT;
            private LinearLayout categoryLinearLayout,detailsLinearLayout,claimHeadLinearLayout,inputAmtLinearLayout,amountLinearLayout,fromDateLinearLayout,toDateLinearLayout;


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
                inputAmtLinearLayout = (LinearLayout) v.findViewById(R.id.inputAmtLinearLayout);
                inputAmtLinearLayout.setVisibility(View.GONE);

                toDateLinearLayout = (LinearLayout) v.findViewById(R.id.toDateLinearLayout);

                actionBTN = (Button) v.findViewById(R.id.actionBTN);
                actionBTN.setVisibility(View.VISIBLE);

                viewDocLl = (LinearLayout) v.findViewById(R.id.viewDocLl);
                viewDocBTN = (Button) v.findViewById(R.id.viewDocBTN);
                statusBT = (Button) v.findViewById(R.id.statusBT);

                viewDocLl.setVisibility(View.GONE);

                statusLl = (LinearLayout) v.findViewById(R.id.statusLl);
                statusLl.setVisibility(View.GONE);

                statusMsgLl = (LinearLayout) v.findViewById(R.id.statusMsgLl);
                statusMsgLl.setVisibility(View.GONE);
                statusBT = (Button) v.findViewById(R.id.statusBT);

                categoryLinearLayout=(LinearLayout)  v.findViewById(R.id.categoryLinearLayout);
                detailsLinearLayout=(LinearLayout)  v.findViewById(R.id.detailsLinearLayout);
                claimHeadLinearLayout=(LinearLayout)  v.findViewById(R.id.claimHeadLinearLayout);
                amountLinearLayout=(LinearLayout)  v.findViewById(R.id.amountLinearLayout);


            }
        }

        public void addAll(List<LineItemsModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public ExpenseClaimDetailsAdapter(List<LineItemsModel> data) {
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
            if (item.getInputUnit() != null) {
                holder.inputAmtLinearLayout.setVisibility(View.VISIBLE);
                holder.inputTV.setText(item.getInputUnit());
                holder.amountTV.setText(item.getClaimAmt());
                Utility.formatAmount(holder.amountTV,Double.parseDouble(item.getClaimAmt()));
                if (item.getLabelPeriod() != null && item.getLabelPeriod().equalsIgnoreCase("Period")) {
                    holder.toDateLinearLayout.setVisibility(View.VISIBLE);
                    holder.toDateTV.setText(item.getDateTo());
                    holder.toDateLabel.setText(item.getLabelPeriod());
                }

            } else {
                holder.inputAmtLinearLayout.setVisibility(View.GONE);
                holder.amountTV.setText(item.getClaimAmt());
                Utility.formatAmount(holder.amountTV,Double.parseDouble(item.getClaimAmt()));
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


            if (item.getDocListLineItem() != null && item.getDocListLineItem().size() > 0) {
                viewDocLl.setVisibility(View.VISIBLE);
                holder.viewDocBTN.setText("Document " + item.getDocListLineItem().size());
                holder.viewDocBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateExpenseOnAddClick();
                        Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                        AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                        AddExpenseActivity.lineItemsModel=item;
                        startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                    }
                });
            }


            holder.actionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Edit");
                    list.add("Delete");
                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                    customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                                updateExpenseOnAddClick();
                                Intent theIntent=new Intent(getActivity(), AddExpenseActivity.class);
                                AddExpenseActivity.saveExpenseRequestModel=saveExpenseRequestModel;
                                AddExpenseActivity.lineItemsModel=item;
                                startActivityForResult(theIntent,AddExpenseActivity.REQUEST_CODE);
                            } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                                dataSet.remove(listPosition);
                                ExpenseClaimDetailsAdapter.this.notifyDataSetChanged();

                                if (dataSet.size() == 0) {
                                    errorTV.setVisibility(View.VISIBLE);
                                }
                                saveExpenseRequestModel.getExpense().getExpenseItem().setLineItems(dataSet);
                                refreshLineItemList();

                            }
                            builder.dismiss();
                        }


                    });
                    customBuilder.show();
                }
            });
            setLineItemLable(holder, item);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
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
            holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totalAmountTobeAdjusted = totalAmountTobeAdjusted + Double.parseDouble(item.getAdjAmount());
                    mDataset.remove(position);
                    AdjustmentDetailAdapter.this.notifyDataSetChanged();
                    updateNetAmount();
                }
            });
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }

    private void refreshAdjustmentRecycle(ArrayList<AdvanceListItemModel> list) {
        adjustemntDetailAdapter = new AdjustmentDetailAdapter(list);
        advance_expenseRecyclerView.setAdapter(adjustemntDetailAdapter);
        adjustemntDetailAdapter.notifyDataSetChanged();
        updateNetAmount();

    }


    private void setUpData() {
        if (saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList().size() > 0) {
            advanceList = saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceList();
            refreshAdjustmentRecycle(advanceList);
        } else {
            advanceList = new ArrayList<AdvanceListItemModel>();
        }
        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem() != null) {
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getDescription() != null) {
                detailsET.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getDescription());
            }
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getApproverName() != null) {
                Log.d("TAG", "Approveer id : " + saveExpenseRequestModel.getExpense().getExpenseItem().getApproverID());
                approverID = saveExpenseRequestModel.getExpense().getExpenseItem().getApproverID();
                Log.d("TAG", "Approveer name : " + saveExpenseRequestModel.getExpense().getExpenseItem().getApproverName());
                approverTV.setHint(saveExpenseRequestModel.getExpense().getExpenseItem().getApproverName());

            }

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc() != null) {
                claimTypeTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc());
                claimTypeID = saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID();
                if (claimTypeListItems != null) {
                    claimTypeListItems.setClaimType(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc());
                    claimTypeListItems.setClaimTypeID(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID());
                } else {
                    claimTypeListItems = new ClaimTypeListItem();
                    claimTypeListItems.setClaimType(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeDesc());
                    claimTypeListItems.setClaimTypeID(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID());
                }

                saveExpenseRequestModel.getExpense().getExpenseItem().setClaimTypeID(saveExpenseRequestModel.getExpense().getExpenseItem().getClaimTypeID());
            }
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getEmpName() != null) {
                onBehalfTV.setText("");
                onBehalfTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getEmpName());
                saveExpenseRequestModel.getExpense().getExpenseItem().setForEmpID(saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID());
                if (employeeList != null) {
                    employeeList.setEmpID(saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID());
                } else {
                    employeeList = new EmployeeListModel();
                    employeeList.setEmpID(saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID());
                }
                empId = saveExpenseRequestModel.getExpense().getExpenseItem().getForEmpID();

            }
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode() != null) {
                currencyTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode());
                saveExpenseRequestModel.getExpense().getExpenseItem().setCurrencyCode(saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode());
                String[] currencyArray = new String[1];
                currencyArray[0] = saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode();
                if (currencyListModel != null && currencyListModel.getCurrencyList() != null) {
                    currencyListModel.setCurrencyList(currencyArray);
                } else {
                    currencyListModel = new CurrencyListModel();

                    currencyArray[0] = saveExpenseRequestModel.getExpense().getExpenseItem().getCurrencyCode();
                    currencyListModel.setCurrencyList(currencyArray);
                }
                currencyValue = currencyArray[0];

                sendAdvanceAdjustmentData();

            }

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getTotalExpenseClaimedAmount() != null) {
                totalExpenseClaimedTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getTotalExpenseClaimedAmount());
            }
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getNetAmount() != null) {
                netAmountTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getNetAmount());
            }
            if (saveExpenseRequestModel.getExpense().getExpenseItem().getReqRemark() != null) {
                remarksET.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getReqRemark());
            }

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getProjectName() != null) {
                projectTV.setText(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectName());
                projectLinearLayout.setVisibility(View.VISIBLE);
                projectId = String.valueOf(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID());
                if (projectListItem != null) {
                    projectListItem.setProjectName(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectName());
                    projectListItem.setProjectID(String.valueOf(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID()));
                } else {
                    projectListItem = new ProjectListItem();
                    projectListItem.setProjectName(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectName());
                    projectListItem.setProjectID(String.valueOf(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID()));
                }

                saveExpenseRequestModel.getExpense().getExpenseItem().setProjectID(saveExpenseRequestModel.getExpense().getExpenseItem().getProjectID());
            }

            if (saveExpenseRequestModel.getExpense().getExpenseItem().getDocList() != null) {
                uploadFileList = saveExpenseRequestModel.getExpense().getExpenseItem().getDocList();
                refreshList();
            } else {
                uploadFileList = new ArrayList<DocListModel>();
            }

        }

        totalAmountTobeAdjusted = 0;
        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {
            for (LineItemsModel itemsModel : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                totalAmountTobeAdjusted = totalAmountTobeAdjusted + Double.parseDouble(itemsModel.getClaimAmt());
                itemsModel.setAmtApproved(itemsModel.getClaimAmt());
                saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().set(saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().indexOf(itemsModel), itemsModel);


            }

            if (advanceList != null) {
                advanceAdjustmentResponseModel = saveExpenseRequestModel.getExpense().getExpenseItem().getAdvanceAdjustmentResponseModel();
                setAdvanceAdjustmentData();
                for (AdvanceListItemModel item : advanceList) {
                    if (totalAmountTobeAdjusted != 0) {
                        if (item.getPaidAmount() != null) {
                            totalAmountTobeAdjusted = totalAmountTobeAdjusted - Double.parseDouble(item.getPaidAmount());
                        }
                    }
                }
            } else {

            }
            refreshLineItemList();
            errorTV.setVisibility(View.GONE);
            expenseDetailsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            errorTV.setVisibility(View.VISIBLE);

            expenseDetailsRecyclerView.setVisibility(View.GONE);
        }
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
                setUpData();
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

                if(Utility.calcBase64SizeInKBytes(encodeFileToBase64Binary)>Utility.maxLimit){

                    CustomDialog.alertWithOk(context, Utility.sizeMsg);
                    return;
                }

                if (fileShow) {
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
            errorDocTV.setVisibility(View.GONE);
            expenseRecyclerView.setVisibility(View.VISIBLE);

            DocumentUploadAdapter adapter = new DocumentUploadAdapter(uploadFileList);
            expenseRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            errorDocTV.setVisibility(View.VISIBLE);
            expenseRecyclerView.setVisibility(View.GONE);
        }
    }

    private class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {
        private ArrayList<DocListModel> mDataset;

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
                holder.img_icon.setImageBitmap(fileObject.getBitmap());
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
            }else if (filename.toString().contains(".txt")) {
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
                                            uploadFileList = new ArrayList<DocListModel>();
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

    private void setAdvanceAdjustmentData() {

        if (!currencyValue.equalsIgnoreCase("")) {

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
                                    if (currencyValue != null && saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems().size() > 0) {
                                    } else {
                                        claimDialog.dismiss();
                                        isClickedSubmit=false;
                                        new AlertCustomDialog(context, "Please Add Expense Details");
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
                            new AlertCustomDialog(context, "No Request Id");
                            return;
                        }
                    }
                });
            }
        } else {
            new AlertCustomDialog(context, "Please Select Currency");
            return;
        }

    }

    private void showPopupForAdjustExpense(final GetAdvanceDetailResultModel model) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        expenseList = saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems();
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

        if (saveExpenseRequestModel != null && saveExpenseRequestModel.getExpense() != null && saveExpenseRequestModel.getExpense().getExpenseItem() != null
                && saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems() != null) {
            for (LineItemsModel item : saveExpenseRequestModel.getExpense().getExpenseItem().getLineItems()) {
                totalExpense = totalExpense + Double.parseDouble(item.getClaimAmt());
            }
        }

        Utility.formatAmount(totalTV,totalExpense);
        Utility.formatAmount(totalExpenseClaimedTV,totalExpense);
        saveExpenseRequestModel.getExpense().getExpenseItem().setTotalExpenseClaimedAmount(totalExpense + "");
        if (totalExpense != 0.0) {
            netAmount = totalExpense - advanceAdjustAmt;
            saveExpenseRequestModel.getExpense().getExpenseItem().setNetAmount(netAmount + "");
            netAmountTV.setText(netAmount + "");
            Utility.formatAmount(netAmountTV,netAmount);
        } else {
            netAmount = 0.0;
            saveExpenseRequestModel.getExpense().getExpenseItem().setNetAmount(netAmount + "");
            netAmountTV.setText(netAmount + "");
            Utility.formatAmount(netAmountTV,netAmount);
        }

    }
}
