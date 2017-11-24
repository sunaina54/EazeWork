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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.ExpenseClaimSummaryResponseModel;
import hr.eazework.com.model.ExpenseItemListModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class ExpenseClaimSummaryFragment extends BaseFragment {
    public static final String COLOR = "color";
    public static final String TEXT = "text";
    private RecyclerView summaryRecyclerView;
    private Context context;
    private ExpenseClaimSummaryAdapter summaryAdapter;
    public static final String TAG = "ExpenseClaimSummaryFragment";
    private ImageView searchIV, filterIV, searchCancelIV;
    private EditText searchET;
    private ExpenseClaimSummaryResponseModel expenseClaimSummaryResponseModel;
    private ArrayList<ExpenseItemListModel> filterExpenseList;
    private ArrayList<ExpenseItemListModel> searchExpenseList;
    private String searchTag = null;
    private RelativeLayout search_layout, norecordLayout;
    private LinearLayout errorLinearLayout;
    private LinearLayout searchParentLayout;

    public static Fragment newInstance(String text, int color) {
        ExpenseClaimSummaryFragment f = new ExpenseClaimSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(COLOR, color);
        args.putString(TEXT, text);
        f.setArguments(args);
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expense_claim_summary_fragment, container, false);
        setupScreen(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHideProgressView(true);
    }
    private void setupScreen(View view) {
        context = getActivity();
        filterExpenseList = new ArrayList<>();
        searchExpenseList = new ArrayList<>();
        search_layout = (RelativeLayout) view.findViewById(R.id.search_layout);
        search_layout.setVisibility(View.INVISIBLE);
        searchET = (EditText) view.findViewById(R.id.searchET);
        searchParentLayout = (LinearLayout) view.findViewById(R.id.searchParentLayout);
        searchParentLayout.setVisibility(View.GONE);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorLinearLayout);
        errorLinearLayout.setVisibility(View.GONE);
        searchIV = (ImageView) view.findViewById(R.id.searchIV);
        searchIV.setOnClickListener(this);
        filterIV = (ImageView) view.findViewById(R.id.filterIV);
        filterIV.setOnClickListener(this);
        searchCancelIV = (ImageView) view.findViewById(R.id.searchCancelIV);
        searchCancelIV.setOnClickListener(this);
        summaryRecyclerView = (RecyclerView) view.findViewById(R.id.summaryRecyclerView);
         norecordLayout=(RelativeLayout) view.findViewById(R.id.noRecordLayout);
        ImageView clearTextIV = (ImageView) view.findViewById(R.id.clearTextIV);
        sendRequestSummaryData();

        clearTextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchTag.toString().equalsIgnoreCase(getResources().getString(R.string.request_id_search))) {
                    searchET.setText("");
                    searchET.setHint("Enter Voucher No");
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
                        ArrayList<ExpenseItemListModel> list = new ArrayList<>();
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.request_id_search))) {
                            for (ExpenseItemListModel item : expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList()) {
                                if (item.getReqCode().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            for (ExpenseItemListModel item : expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList()) {
                                if (item.getReqDate().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.pending_with_search))) {
                            for (ExpenseItemListModel item : expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList()) {
                                if (item.getPendingWith().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                    } else {
                        refresh(expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                searchList.add(getResources().getString(R.string.request_id_search));
                searchList.add(getResources().getString(R.string.date_search));
                searchList.add(getResources().getString(R.string.pending_with_search));
                CustomBuilder searchCustomBuilder = new CustomBuilder(getContext(), "Search By", false);
                searchCustomBuilder.setSingleChoiceItems(searchList, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        search_layout.setVisibility(View.INVISIBLE);
                        searchCancelIV.setVisibility(View.VISIBLE);
                        searchIV.setVisibility(View.GONE);
                        searchTag = selectedObject.toString();
                        if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.request_id_search))) {
                            search_layout.setVisibility(View.VISIBLE);
                            searchTag = getResources().getString(R.string.request_id_search);
                            searchET.setHint("Enter Voucher No");
                        } else if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            search_layout.setVisibility(View.VISIBLE);
                            searchET.setHint("Enter Date(dd/mm/yyyy)");
                            searchTag = getResources().getString(R.string.date_search);
                        } else if (selectedObject.toString().equalsIgnoreCase(getResources().getString(R.string.pending_with_search))) {
                            search_layout.setVisibility(View.VISIBLE);
                            searchET.setHint("Enter Pending with person name");
                            searchTag = getResources().getString(R.string.pending_with_search);
                        }


                        builder.dismiss();
                    }


                });
                searchCustomBuilder.show();

                break;
            case R.id.filterIV:
                ArrayList<String> list = new ArrayList<>();
                list.add("All");
                ArrayList<ExpenseItemListModel> listModels= Utility.prepareFilterList( expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList());
                if(listModels!=null && listModels.size()>0) {
                    for (ExpenseItemListModel model : listModels) {
                        list.add(model.getReqStatusDesc());
                    }
                    ExpenseItemListModel itemListModel = new ExpenseItemListModel();
                    itemListModel.setReqStatus(-1);
                    itemListModel.setReqStatusDesc("All");
                    listModels.add(0, itemListModel);
                }

                if (expenseClaimSummaryResponseModel != null && expenseClaimSummaryResponseModel.getGetEmpExpenseResult() != null &&
                        expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList() != null &&
                        expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().size() > 0) {
                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Filter By", true);
                    customBuilder.setSingleChoiceItems(listModels, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            ExpenseItemListModel model=(ExpenseItemListModel)selectedObject;

                            if (model.getReqStatus()==-1) {
                                refresh(expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList());

                            }else {
                                filterExpenseList = new ArrayList<>();
                                for (int i = 0; i < expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().size(); i++) {
                                    if (expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().get(i)
                                            .getReqStatus()==model.getReqStatus()) {
                                        filterExpenseList.add(expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().get(i));
                                    }
                                }
                                refresh(filterExpenseList);
                            }
                            builder.dismiss();
                        }


                    });
                    customBuilder.show();
                }
                break;


            default:
                break;
        }
        super.onClick(v);
    }

    public void sendRequestSummaryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getExpenseClaimSummaryData(),
                CommunicationConstant.API_GET_EXPENSE_CLAIM_SUMMARY, true);
    }


    @Override
    public void validateResponse(ResponseData response) {
        showHideProgressView(false);
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EXPENSE_CLAIM_SUMMARY:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                summaryRecyclerView.setVisibility(View.GONE);
                errorLinearLayout.setVisibility(View.VISIBLE);
                searchParentLayout.setVisibility(View.GONE);
                expenseClaimSummaryResponseModel = ExpenseClaimSummaryResponseModel.create(str);
                if (expenseClaimSummaryResponseModel != null && expenseClaimSummaryResponseModel.getGetEmpExpenseResult() != null &&
                        expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList() != null &&
                        expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().size() > 0) {
                    if (expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList().get(0) != null) {
                        summaryRecyclerView.setVisibility(View.VISIBLE);
                        errorLinearLayout.setVisibility(View.GONE);
                        searchParentLayout.setVisibility(View.VISIBLE);
                        refresh(expenseClaimSummaryResponseModel.getGetEmpExpenseResult().getExpenseItemList());
                    }
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void refresh(ArrayList<ExpenseItemListModel> expenseItemListModels) {

        norecordLayout.setVisibility(View.GONE);
        if(expenseItemListModels!=null && expenseItemListModels.size()<=0) {
            norecordLayout.setVisibility(View.VISIBLE);
        }
        summaryAdapter = new ExpenseClaimSummaryAdapter(expenseItemListModels);
        summaryRecyclerView.setAdapter(summaryAdapter);
        summaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        summaryAdapter.notifyDataSetChanged();
    }

    private class ExpenseClaimSummaryAdapter extends
            RecyclerView.Adapter<ExpenseClaimSummaryAdapter.MyViewHolder> {
        private ArrayList<ExpenseItemListModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView voucherNoTV, dateTV, descriptionTV, pendingWithTV, statusTV;
            private Button viewBTN;


            public MyViewHolder(View v) {
                super(v);

                voucherNoTV = (TextView) v.findViewById(R.id.voucherNoTV);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                descriptionTV = (TextView) v.findViewById(R.id.descriptionTV);
                pendingWithTV = (TextView) v.findViewById(R.id.pendingWithTV);
                statusTV = (TextView) v.findViewById(R.id.statusTV);
                viewBTN = (Button) v.findViewById(R.id.viewBTN);
                viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewExpenseClaimSummaryFragment viewExpenseSummary = new ViewExpenseClaimSummaryFragment();
                        viewExpenseSummary.setExpenseItemListModel(dataSet.get(getPosition()));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        }

        public void addAll(List<ExpenseItemListModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public ExpenseClaimSummaryAdapter(List<ExpenseItemListModel> data) {
            this.dataSet = (ArrayList<ExpenseItemListModel>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.expense_claim_summary_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final ExpenseItemListModel item = dataSet.get(listPosition);
            String[] date = item.getReqDate().split(" ");
            holder.voucherNoTV.setText(item.getReqCode());
            holder.dateTV.setText(date[0]);
            holder.descriptionTV.setText(item.getDescription());
            holder.pendingWithTV.setText(item.getPendingWith());
            holder.statusTV.setText(item.getReqStatusDesc());

            holder.viewBTN.setText(item.getButtons()[0]);
            if (item.getReqStatusDesc() != null && item.getReqStatusDesc().equalsIgnoreCase("Draft")) {
                holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditViewExpenseClaimFragment viewExpenseSummary = new EditViewExpenseClaimFragment();
                        viewExpenseSummary.setExpenseItemListModel(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                });
            } else if (item.getReqStatusDesc() != null && item.getReqStatusDesc().equalsIgnoreCase("Submitted")) {
                holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewExpenseClaimSummaryFragment viewExpenseSummary = new ViewExpenseClaimSummaryFragment();
                        viewExpenseSummary.setExpenseItemListModel(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            } else if (item.getReqStatusDesc() != null && item.getReqStatusDesc().equalsIgnoreCase("Returned")) {
                holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditViewExpenseClaimFragment viewExpenseSummary = new EditViewExpenseClaimFragment();
                        viewExpenseSummary.setExpenseItemListModel(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                });
            } else {
                holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewExpenseClaimSummaryFragment viewExpenseSummary = new ViewExpenseClaimSummaryFragment();
                        viewExpenseSummary.setExpenseItemListModel(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }

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
