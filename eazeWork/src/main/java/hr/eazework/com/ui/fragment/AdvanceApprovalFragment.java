package hr.eazework.com.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceApprovalResponseModel;
import hr.eazework.com.model.ApprovalRoleResponseModel;
import hr.eazework.com.model.GetAdvanceApprovalRoleResult;
import hr.eazework.com.model.GetAdvanceDetailResultModel;
import hr.eazework.com.model.RoleListItem;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 27-09-2017.
 */

public class AdvanceApprovalFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "AdvanceApprovalFragment";
    private RecyclerView expenseApprovalRecyclerView;
    private AdvanceApprovalAdapter AdvanceApprovalAdapter;
    private TextView roleTV;
    private ApprovalRoleResponseModel approvalRoleResponseModel;
    private RoleListItem roleList;
    private String roleId;
    private GetAdvanceDetailResultModel getAdvanceDetailResultModel;
    private AdvanceApprovalResponseModel advanceApprovalResponseModel;
    private ImageView searchIV, searchCancelIV;
    private EditText searchET;
    private String searchTag = null;
    private RelativeLayout search_layout;
    private LinearLayout errorLinearLayout;
    private FrameLayout roleFrameLayout;
    private LinearLayout searchParentLayout;


    public GetAdvanceDetailResultModel getGetAdvanceDetailResultModel() {
        return getAdvanceDetailResultModel;
    }

    public void setGetAdvanceDetailResultModel(GetAdvanceDetailResultModel getAdvanceDetailResultModel) {
        this.getAdvanceDetailResultModel = getAdvanceDetailResultModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.advance_approval_fragment, container, false);
        context = getContext();
        setupScreen(rootView);
        return rootView;
    }

    private void setupScreen(View view) {
        searchET = (EditText) view.findViewById(R.id.searchET);
        searchIV = (ImageView) view.findViewById(R.id.searchIV);
        searchIV.setOnClickListener(this);
        search_layout = (RelativeLayout) view.findViewById(R.id.search_layout);
        searchParentLayout=(LinearLayout)view.findViewById(R.id.searchParentLayout);
        searchParentLayout.setVisibility(View.GONE);
        errorLinearLayout= (LinearLayout) view.findViewById(R.id.errorLinearLayout);
        errorLinearLayout.setVisibility(View.GONE);
        search_layout.setVisibility(View.INVISIBLE);
        searchIV.setVisibility(View.VISIBLE);

        ImageView filterIV=(ImageView)view.findViewById(R.id.filterIV);
        filterIV.setVisibility(View.GONE);
        roleFrameLayout= (FrameLayout) view.findViewById(R.id.roleFrameLayout);
        roleFrameLayout.setVisibility(View.GONE);
        searchCancelIV = (ImageView) view.findViewById(R.id.searchCancelIV);
        searchCancelIV.setOnClickListener(this);

        expenseApprovalRecyclerView = (RecyclerView) view.findViewById(R.id.expenseApprovalRecyclerView);
        ImageView clearTextIV = (ImageView) view.findViewById(R.id.clearTextIV);
        sendApprovalRoleData();
        roleTV = (TextView) view.findViewById(R.id.roleTV);
        roleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approvalRoleResponseModel != null && approvalRoleResponseModel.getGetAdvanceApprovalRoleResult() != null) {
                    GetAdvanceApprovalRoleResult advanceApprovalRoleResult = approvalRoleResponseModel.getGetAdvanceApprovalRoleResult();
                    if (advanceApprovalRoleResult != null) {
                        ArrayList<RoleListItem> roleListItems = advanceApprovalRoleResult.getRoleList();
                        CustomBuilder claimDialog = new CustomBuilder(getContext(), "Select Role", true);
                        claimDialog.setSingleChoiceItems(roleListItems, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                roleList = (RoleListItem) selectedObject;
                                roleTV.setText(roleList.getRoleDesc());
                                roleId = roleList.getRoleID();
                                sendAdvanceApprovalData();
                                builder.dismiss();

                            }
                        });
                        claimDialog.show();
                    }
                }
            }
        });

        clearTextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.request_id_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Request Id");
                } else if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Date(dd/mm/yyyy)");
                } else if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.pending_with_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Pending with person name");
                }
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchTag != null) {
                    if (!s.toString().equalsIgnoreCase("")) {
                        ArrayList<GetAdvanceDetailResultModel> list = new ArrayList<GetAdvanceDetailResultModel>();
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.voucher_no_search))) {
                            for (GetAdvanceDetailResultModel item : advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList()) {
                                if (item.getReqCode().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            for (GetAdvanceDetailResultModel item : advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList()) {
                                if (item.getReqDate().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.request_for_search))) {
                            for (GetAdvanceDetailResultModel item : advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList()) {
                                if (item.getName().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                    } else {
                        refresh(advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void sendApprovalRoleData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_APPROVAL_ROLE, true);
    }

    public void sendAdvanceApprovalData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceApprovalData(roleId),
                CommunicationConstant.API_GET_EMPLOYEE_ADVANCE_APPROVAL, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchCancelIV:
                searchTag = null;
                search_layout.setVisibility(View.INVISIBLE);
                searchIV.setVisibility(View.VISIBLE);
                searchCancelIV.setVisibility(View.GONE);
                break;
            case R.id.searchIV:
                ArrayList<String> searchList = new ArrayList<>();
                searchList.add(getResources().getString(R.string.voucher_no_search));
                searchList.add(getResources().getString(R.string.date_search));
                searchList.add(getResources().getString(R.string.request_for_search));
                CustomBuilder searchCustomBuilder = new CustomBuilder(getContext(), "Search By", false);
                searchCustomBuilder.setSingleChoiceItems(searchList, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        search_layout.setVisibility(View.VISIBLE);
                        searchCancelIV.setVisibility(View.VISIBLE);
                        searchIV.setVisibility(View.GONE);
                        searchTag = selectedObject.toString();
                        if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.voucher_no_search))) {
                            searchET.setVisibility(View.VISIBLE);
                            searchTag = getResources().getString(R.string.voucher_no_search);
                            searchET.setHint("Enter Voucher No");
                        } else if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            searchET.setVisibility(View.VISIBLE);
                            searchET.setHint("Enter Date(dd/mm/yyyy)");
                            searchTag = getResources().getString(R.string.date_search);
                        } else if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.request_for_search))) {
                            searchET.setVisibility(View.VISIBLE);
                            searchET.setHint("Enter Request For person name");
                            searchTag = getResources().getString(R.string.request_for_search);
                        }
                        builder.dismiss();
                    }
                });
                searchCustomBuilder.show();
                break;
            default:
                break;
        }
        super.onClick(v);
    }


    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ADVANCE_APPROVAL_ROLE:
                String responseData = response.getResponseData();
                Log.d("TAG", "Advance Response : " + responseData);
                approvalRoleResponseModel = ApprovalRoleResponseModel.create(responseData);
                roleFrameLayout.setVisibility(View.GONE);
                if (approvalRoleResponseModel != null && approvalRoleResponseModel.getGetAdvanceApprovalRoleResult() != null &&
                        approvalRoleResponseModel.getGetAdvanceApprovalRoleResult().getRoleList() != null) {
                    ArrayList<RoleListItem> roleListItems = approvalRoleResponseModel.getGetAdvanceApprovalRoleResult().getRoleList();
                    if (roleListItems.size() == 1) {
                        roleId=approvalRoleResponseModel.getGetAdvanceApprovalRoleResult().getRoleList().get(0).getRoleID();
                        sendAdvanceApprovalData();
                    } else if(roleListItems.size()>1){
                        searchParentLayout.setVisibility(View.VISIBLE);
                        roleFrameLayout.setVisibility(View.VISIBLE);
                        roleId=approvalRoleResponseModel.getGetAdvanceApprovalRoleResult().getRoleList().get(0).getRoleID();
                        roleTV.setText(approvalRoleResponseModel.getGetAdvanceApprovalRoleResult().getRoleList().get(0).getRoleDesc());
                        sendAdvanceApprovalData();
                        refresh(advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList());
                    }
                }

                break;

            case CommunicationConstant.API_GET_EMPLOYEE_ADVANCE_APPROVAL:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                advanceApprovalResponseModel = AdvanceApprovalResponseModel.create(str);
                if (advanceApprovalResponseModel != null && advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult() != null &&
                        advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList() != null) {
                    refresh(advanceApprovalResponseModel.getGetEmpAdvanceApprovalResult().getAdvanceList());
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void refresh(ArrayList<GetAdvanceDetailResultModel> advanceListModels) {
        if (advanceListModels != null && advanceListModels.size() > 0 && advanceListModels.get(0)!=null) {
            expenseApprovalRecyclerView.setVisibility(View.VISIBLE);
            errorLinearLayout.setVisibility(View.GONE);
            searchParentLayout.setVisibility(View.VISIBLE);
            AdvanceApprovalAdapter = new AdvanceApprovalAdapter(advanceListModels);
            expenseApprovalRecyclerView.setAdapter(AdvanceApprovalAdapter);
            expenseApprovalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            AdvanceApprovalAdapter.notifyDataSetChanged();
        }else {
            expenseApprovalRecyclerView.setVisibility(View.GONE);
            errorLinearLayout.setVisibility(View.VISIBLE);
            searchParentLayout.setVisibility(View.GONE);
        }

    }

    private class AdvanceApprovalAdapter extends
            RecyclerView.Adapter<AdvanceApprovalAdapter.MyViewHolder> {
        private ArrayList<GetAdvanceDetailResultModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView voucherNoTV, dateTV, descriptionTV, requestForTV, amountTV, descLabelTV;
            private Button actionBTN;
            private LinearLayout statusLinearLayout;


            public MyViewHolder(View v) {
                super(v);

                voucherNoTV = (TextView) v.findViewById(R.id.voucherNoTV);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                descriptionTV = (TextView) v.findViewById(R.id.descriptionTV);
                requestForTV = (TextView) v.findViewById(R.id.requestForTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);
                actionBTN = (Button) v.findViewById(R.id.actionBTN);
                actionBTN.setText("Edit");
                descLabelTV = (TextView) v.findViewById(R.id.descLabelTV);
                descLabelTV.setText("Reason");
                statusLinearLayout = (LinearLayout) v.findViewById(R.id.statusLinearLayout);
                statusLinearLayout.setVisibility(View.GONE);

            }

        }

        public void addAll(List<GetAdvanceDetailResultModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public AdvanceApprovalAdapter(List<GetAdvanceDetailResultModel> data) {
            this.dataSet = (ArrayList<GetAdvanceDetailResultModel>) data;

        }

        @Override
        public AdvanceApprovalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.advance_approval_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            AdvanceApprovalAdapter.MyViewHolder myViewHolder = new AdvanceApprovalAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final AdvanceApprovalAdapter.MyViewHolder holder, final int listPosition) {
            final GetAdvanceDetailResultModel item = dataSet.get(listPosition);
            holder.voucherNoTV.setText(item.getReqCode());
            holder.dateTV.setText(item.getReqDate());
            holder.descriptionTV.setText(item.getReason());
            holder.requestForTV.setText(item.getName());
            holder.amountTV.setText(item.getApprovedAmount());
            holder.actionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditAdvanceApprovalFragment advanceRequestFragment = new EditAdvanceApprovalFragment();
                    advanceRequestFragment.setAdvanceListModel(dataSet.get(listPosition));
                    Fragment fragment=advanceRequestFragment;
                                               /* FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);*/
                    //fragmentTransaction.addToBackStack(PendingActivityFragment.TAG);
                    mUserActionListener.performUserActionFragment(IAction.EDIT_EXPENSE_APPROVAL,fragment,null);


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
}
