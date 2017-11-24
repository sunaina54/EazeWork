package hr.eazework.com.ui.fragment;


import android.content.Context;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.ExpenseApprovalList;
import hr.eazework.com.model.ExpenseApprovalResponseModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 27-09-2017.
 */

public class ExpenseApprovalFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "ExpenseApprovalFragment";
    private RecyclerView expenseApprovalRecyclerView;
    private ExpenseApprovalAdapter expenseApprovalAdapter;
    private ImageView searchIV, filterIV, searchCancelIV;
    private EditText searchET;
    private String searchTag = null;
    private ExpenseApprovalResponseModel expenseApprovalResponseModel;
    private ArrayList<ExpenseApprovalList> filterExpenseApprovalList;
    private RelativeLayout search_layout;
    private LinearLayout searchParentLayout,errorLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.expense_approval_fragment, container, false);
        context = getContext();
        setupScreen(rootView);
        return rootView;
    }

    private void setupScreen(View view) {
        expenseApprovalRecyclerView = (RecyclerView) view.findViewById(R.id.expenseApprovalRecyclerView);
        searchET = (EditText) view.findViewById(R.id.searchET);
        searchParentLayout = (LinearLayout) view.findViewById(R.id.searchParentLayout);
        searchParentLayout.setVisibility(View.GONE);
        errorLinearLayout= (LinearLayout) view.findViewById(R.id.errorLinearLayout);
        errorLinearLayout.setVisibility(View.GONE);
        search_layout = (RelativeLayout) view.findViewById(R.id.search_layout);
        search_layout.setVisibility(View.INVISIBLE);
        searchIV = (ImageView) view.findViewById(R.id.searchIV);
        searchIV.setOnClickListener(this);
        searchIV.setVisibility(View.VISIBLE);
        searchCancelIV = (ImageView) view.findViewById(R.id.searchCancelIV);
        searchCancelIV.setOnClickListener(this);
        filterIV = (ImageView) view.findViewById(R.id.filterIV);
        filterIV.setOnClickListener(this);
        ImageView clearTextIV = (ImageView) view.findViewById(R.id.clearTextIV);

        sendRequestSummaryData();
        clearTextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.voucher_no_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Voucher No");
                } else if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Date(dd/mm/yyyy)");

                } else if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.request_for_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Request For person name");

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
                        ArrayList<ExpenseApprovalList> list = new ArrayList<ExpenseApprovalList>();
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.voucher_no_search))) {
                            for (ExpenseApprovalList item : expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList()) {
                                if (item.getReqCode().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            for (ExpenseApprovalList item : expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList()) {
                                if (item.getReqDate().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.request_for_search))) {
                            for (ExpenseApprovalList item : expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList()) {
                                if (item.getName().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                    } else {
                        refresh(expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void sendRequestSummaryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseClaimSummaryData(),
                CommunicationConstant.API_GET_EMPLOYEE_EXPENSE_APPROVAL, true);
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

            case R.id.filterIV:
                ArrayList<String> list = new ArrayList<>();
                list.add("All");
                list.add("Submitted");

                CustomBuilder customBuilder = new CustomBuilder(getContext(), "Filter By", false);
                customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        if (selectedObject.toString().equalsIgnoreCase("All")) {
                            refresh(expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList());

                        } else if (selectedObject.toString().equalsIgnoreCase("Submitted")) {

                            filterExpenseApprovalList = new ArrayList<ExpenseApprovalList>();
                            for (int i = 0; i < expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList().size(); i++) {
                                if (expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList().get(i)
                                        .getReqStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                    filterExpenseApprovalList.add(expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList().get(i));
                                    refresh(filterExpenseApprovalList);


                                }
                            }

                        }


                        builder.dismiss();
                    }


                });
                customBuilder.show();
                break;


            default:
                break;
        }
        super.onClick(v);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EMPLOYEE_EXPENSE_APPROVAL:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                expenseApprovalRecyclerView.setVisibility(View.GONE);
                errorLinearLayout.setVisibility(View.VISIBLE);
                searchParentLayout.setVisibility(View.GONE);
                expenseApprovalResponseModel = ExpenseApprovalResponseModel.create(str);
                if (expenseApprovalResponseModel != null && expenseApprovalResponseModel.getGetEmpExpenseApprovalResult() != null && expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList() != null && expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList().size() > 0) {
                    if (expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList().get(0) != null) {
                        expenseApprovalRecyclerView.setVisibility(View.VISIBLE);
                        errorLinearLayout.setVisibility(View.GONE);
                        searchParentLayout.setVisibility(View.VISIBLE);
                        refresh(expenseApprovalResponseModel.getGetEmpExpenseApprovalResult().getExpenseApprovalList());
                    }
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void refresh(ArrayList<ExpenseApprovalList> expenseItemListModels) {
        if (expenseItemListModels != null && expenseItemListModels.size() > 0 && expenseItemListModels.get(0) != null) {
            expenseApprovalAdapter = new ExpenseApprovalAdapter(expenseItemListModels);
            expenseApprovalRecyclerView.setAdapter(expenseApprovalAdapter);
            expenseApprovalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            expenseApprovalAdapter.notifyDataSetChanged();
        }
    }

    private class ExpenseApprovalAdapter extends
            RecyclerView.Adapter<ExpenseApprovalAdapter.MyViewHolder> {
        private ArrayList<ExpenseApprovalList> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView voucherNoTV, dateTV, descriptionTV, requestForTV, statusTV, requestedAmountTV, approvedAmountTV;
            private Button actionBTN;


            public MyViewHolder(View v) {
                super(v);

                voucherNoTV = (TextView) v.findViewById(R.id.voucherNoTV);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                descriptionTV = (TextView) v.findViewById(R.id.descriptionTV);
                requestForTV = (TextView) v.findViewById(R.id.requestForTV);
                statusTV = (TextView) v.findViewById(R.id.statusTV);
                requestedAmountTV = (TextView) v.findViewById(R.id.amountTV);
                approvedAmountTV = (TextView) v.findViewById(R.id.approvedAmountTV);
                actionBTN = (Button) v.findViewById(R.id.actionBTN);
                actionBTN.setText("Edit");

            }
        }

        public void addAll(List<ExpenseApprovalList> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public ExpenseApprovalAdapter(List<ExpenseApprovalList> data) {
            this.dataSet = (ArrayList<ExpenseApprovalList>) data;

        }

        @Override
        public ExpenseApprovalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.expense_approval_item, parent, false);
            ExpenseApprovalAdapter.MyViewHolder myViewHolder = new ExpenseApprovalAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ExpenseApprovalAdapter.MyViewHolder holder, final int listPosition) {

            final ExpenseApprovalList item = dataSet.get(listPosition);
            String[] date = item.getReqDate().split(" ");
            holder.voucherNoTV.setText(item.getReqCode());
            holder.dateTV.setText(date[0]);
            holder.descriptionTV.setText(item.getDescription());
            holder.requestForTV.setText(item.getName());
            holder.requestedAmountTV.setText(item.getRequested());
            holder.approvedAmountTV.setText(item.getApproved());
            holder.statusTV.setText(item.getReqStatusDesc());

            holder.actionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditExpenseApprovalFragment advanceRequestFragment = new EditExpenseApprovalFragment();
                    advanceRequestFragment.setExpenseApprovalList(dataSet.get(listPosition));
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.expense_approval, advanceRequestFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
