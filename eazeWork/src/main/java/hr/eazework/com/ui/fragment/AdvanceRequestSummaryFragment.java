package hr.eazework.com.ui.fragment;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.AdvanceListModel;
import hr.eazework.com.model.AdvanceRequestSummaryModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class AdvanceRequestSummaryFragment extends BaseFragment {
    public static final String COLOR = "color";
    public static final String TEXT = "text";
    private RecyclerView summaryRecyclerView;
    private AdvanceRequestSummaryAdapter summaryAdapter;
    private Context context;
    private View view;
    public static final String TAG = "AdvanceRequestSummaryFragment";
    private ImageView searchIV, filterIV, searchCancelIV;
    private EditText searchET;
    private AdvanceRequestSummaryModel advanceRequestSummaryModel;
    private ArrayList<AdvanceListModel> filterAdvanceList;
    private String searchTag = null;
    private RelativeLayout search_layout;
    private LinearLayout errorLinearLayout;
    private LinearLayout searchParentLayout;


    public static Fragment newInstance(String text, int color) {
        AdvanceRequestSummaryFragment f = new AdvanceRequestSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(COLOR, color);
        args.putString(TEXT, text);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.advance_request_summary, container, false);

        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        filterAdvanceList = new ArrayList<>();
        summaryRecyclerView = (RecyclerView) view.findViewById(R.id.summaryRecyclerView);
        searchET = (EditText) view.findViewById(R.id.searchET);
        searchParentLayout = (LinearLayout) view.findViewById(R.id.searchParentLayout);
        searchParentLayout.setVisibility(View.GONE);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorLinearLayout);
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
                        ArrayList<AdvanceListModel> list = new ArrayList<AdvanceListModel>();
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.request_id_search))) {
                            for (AdvanceListModel item : advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList()) {
                                if (item.getReqCode().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }
                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.date_search))) {
                            for (AdvanceListModel item : advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList()) {
                                if (item.getReqDate().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                        if (searchTag.equalsIgnoreCase(getResources().getString(R.string.pending_with_search))) {
                            for (AdvanceListModel item : advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList()) {
                                if (item.getPendWith().toUpperCase().contains(s.toString().toUpperCase())) {
                                    list.add(item);
                                }
                            }
                            refresh(list);
                        }

                    } else {

                        refresh(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList());
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
                            searchET.setHint("Enter Request Id");
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
                list.add("Submitted");
                list.add("Paid");
                list.add("Rejected");
                list.add("Payment In Process");
                list.add("Approved");
                CustomBuilder customBuilder = new CustomBuilder(getContext(), "Filter By", false);
                customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        if (selectedObject.toString().equalsIgnoreCase("All")) {
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {
                                refresh(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList());
                            }

                        } else if (selectedObject.toString().equalsIgnoreCase("Submitted")) {
                            filterAdvanceList = new ArrayList<AdvanceListModel>();
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {

                                for (int i = 0; i < advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size(); i++) {
                                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i)
                                            .getStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                        filterAdvanceList.add(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i));
                                        refresh(filterAdvanceList);


                                    }

                                }
                            }

                        } else if (selectedObject.toString().equalsIgnoreCase("Paid")) {
                            filterAdvanceList = new ArrayList<AdvanceListModel>();
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {

                                for (int i = 0; i < advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size(); i++) {
                                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i)
                                            .getStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                        filterAdvanceList.add(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i));
                                        refresh(filterAdvanceList);


                                    }
                                }
                            }
                        } else if (selectedObject.toString().equalsIgnoreCase("Rejected")) {
                            filterAdvanceList = new ArrayList<AdvanceListModel>();
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {

                                for (int i = 0; i < advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size(); i++) {
                                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i)
                                            .getStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                        filterAdvanceList.add(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i));
                                        refresh(filterAdvanceList);


                                    }
                                }
                            }
                        } else if (selectedObject.toString().equalsIgnoreCase("Payment In Process")) {

                            filterAdvanceList = new ArrayList<AdvanceListModel>();
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {

                                for (int i = 0; i < advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size(); i++) {
                                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i)
                                            .getStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                        filterAdvanceList.add(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i));
                                        refresh(filterAdvanceList);

                                    }
                                }
                            }
                        } else if (selectedObject.toString().equalsIgnoreCase("Approved")) {
                            filterAdvanceList = new ArrayList<AdvanceListModel>();
                            if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null &&
                                    advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null) {

                                for (int i = 0; i < advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size(); i++) {
                                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i)
                                            .getStatusDesc().equalsIgnoreCase(selectedObject.toString())) {
                                        filterAdvanceList.add(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(i));
                                        refresh(filterAdvanceList);

                                    }
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            setupScreen(view);
        } catch (Exception ex) {

        }
    }

    public void sendRequestSummaryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAdvanceSummaryData(),
                CommunicationConstant.API_GET_ADVANCE_REQUEST_SUMMARY, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ADVANCE_REQUEST_SUMMARY:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                summaryRecyclerView.setVisibility(View.GONE);
                errorLinearLayout.setVisibility(View.VISIBLE);
                searchParentLayout.setVisibility(View.GONE);
                advanceRequestSummaryModel = AdvanceRequestSummaryModel.create(str);
                if (advanceRequestSummaryModel != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList() != null && advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size() > 0) {
                    Log.d("TAG", "Advance Response : " + advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().size());
                    if (advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList().get(0) != null) {
                        summaryRecyclerView.setVisibility(View.VISIBLE);
                        errorLinearLayout.setVisibility(View.GONE);
                        searchParentLayout.setVisibility(View.VISIBLE);
                        refresh(advanceRequestSummaryModel.getGetAdvanceRequestSummaryResult().getAdvanceList());
                    }

                }


                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void refresh(ArrayList<AdvanceListModel> advanceListModels) {
        if (advanceListModels != null && advanceListModels.size() > 0 && advanceListModels.get(0) != null) {
            summaryAdapter = new AdvanceRequestSummaryAdapter(advanceListModels);
            summaryRecyclerView.setAdapter(summaryAdapter);
            summaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            summaryAdapter.notifyDataSetChanged();
        }
    }

    private class AdvanceRequestSummaryAdapter extends
            RecyclerView.Adapter<AdvanceRequestSummaryAdapter.MyViewHolder> {
        private ArrayList<AdvanceListModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView requestIdTV, dateTV, reasonTV, currencyTV, balanceTV, pendingWithTV, statusTV;
            private Button viewBTN;


            public MyViewHolder(View v) {
                super(v);

                requestIdTV = (TextView) v.findViewById(R.id.requestIdTV);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                reasonTV = (TextView) v.findViewById(R.id.reasonTV);
                currencyTV = (TextView) v.findViewById(R.id.currencyTV);
                balanceTV = (TextView) v.findViewById(R.id.balanceTV);
                pendingWithTV = (TextView) v.findViewById(R.id.pendingWithTV);
                statusTV = (TextView) v.findViewById(R.id.statusTV);
                viewBTN = (Button) v.findViewById(R.id.viewBTN);
                viewBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAdvanceRequestSummaryFragment viewExpenseSummary = new ViewAdvanceRequestSummaryFragment();
                        viewExpenseSummary.setAdvanceListModel(dataSet.get(getPosition()));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, viewExpenseSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


            }

        }

        public void addAll(List<AdvanceListModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public AdvanceRequestSummaryAdapter(List<AdvanceListModel> data) {
            this.dataSet = (ArrayList<AdvanceListModel>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.advance_request_summary_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            final AdvanceListModel item = dataSet.get(listPosition);
            holder.requestIdTV.setText(item.getReqCode());
            holder.dateTV.setText(item.getReqDate());
            holder.reasonTV.setText(item.getReason());
            holder.currencyTV.setText(item.getCurrencyCode());
            holder.balanceTV.setText(item.getBalAmount());
            holder.pendingWithTV.setText(item.getPendWith());
            holder.statusTV.setText(item.getStatusDesc());

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
