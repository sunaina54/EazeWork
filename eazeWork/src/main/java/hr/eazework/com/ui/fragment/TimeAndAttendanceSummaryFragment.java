package hr.eazework.com.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.GetEmpWFHRequestsModel;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetEmpWFHResponseModel;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class TimeAndAttendanceSummaryFragment extends BaseFragment {

    public static final String TAG = "WFHSummaryFragment";
    private RecyclerView summaryRecyclerView;
    private Context context;
    private LinearLayout errorLinearLayout;
    private View view;
    private String beginYearDate,calendarDate;
    private GetEmpWFHRequestsModel getEmpWFHRequestsModel;
    public static final long daysForFilter = 15552000000L;
    private GetEmpWFHResponseModel getEmpWFHResponseModel;
    private WFHSummaryAdapter summaryAdapter;
    private Button attendanceHistoryBTN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_tour_summary, container, false);
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        context = getActivity();
        summaryRecyclerView = (RecyclerView) view.findViewById(R.id.summaryRecyclerView);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorLinearLayout);
        errorLinearLayout.setVisibility(View.GONE);
        attendanceHistoryBTN= (Button) view.findViewById(R.id.attendanceHistoryBTN);
        attendanceHistoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceHistory viewWFHSummaryFragment = new AttendanceHistory();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.view_advance_expense, viewWFHSummaryFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Calendar calendar = Calendar.getInstance();
        Calendar beginYear = Calendar.getInstance();
        beginYear.setTimeInMillis(beginYear.getTimeInMillis() - daysForFilter);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + daysForFilter);
        beginYearDate = String.format("%1$td/%1$tm/%1$tY", beginYear);
        calendarDate = String.format("%1$td/%1$tm/%1$tY", calendar);
        sendRequestSummaryData();
    }

    public void sendRequestSummaryData() {
        getEmpWFHRequestsModel=new GetEmpWFHRequestsModel();
        getEmpWFHRequestsModel.setDateFrom(calendarDate);
        getEmpWFHRequestsModel.setDateTo(beginYearDate);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.timeAttendanceSummary(getEmpWFHRequestsModel),
                CommunicationConstant.API_GET_EMP_ATTENDANCE_REQUEST ,true);


    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EMP_ATTENDANCE_REQUEST:
                String str = response.getResponseData();
                Log.d("TAG", "Attendance Response : " + str);
                summaryRecyclerView.setVisibility(View.GONE);
                errorLinearLayout.setVisibility(View.VISIBLE);
               // searchParentLayout.setVisibility(View.GONE);
                getEmpWFHResponseModel = GetEmpWFHResponseModel.create(str);
                if (getEmpWFHResponseModel != null && getEmpWFHResponseModel.getGetEmpAttendanceRequestsResult() != null &&
                        getEmpWFHResponseModel.getGetEmpAttendanceRequestsResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && getEmpWFHResponseModel.getGetEmpAttendanceRequestsResult().getRequests().size() > 0) {
                    if (getEmpWFHResponseModel.getGetEmpAttendanceRequestsResult().getRequests().get(0) != null) {
                        summaryRecyclerView.setVisibility(View.VISIBLE);
                        errorLinearLayout.setVisibility(View.GONE);
                     //   searchParentLayout.setVisibility(View.VISIBLE);
                        refresh(getEmpWFHResponseModel.getGetEmpAttendanceRequestsResult().getRequests());
                    }

                }
                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void refresh(ArrayList<GetEmpWFHResponseItem> getEmpWFHResponseList) {
        if (getEmpWFHResponseList != null && getEmpWFHResponseList.size() > 0 && getEmpWFHResponseList.get(0) != null) {
            summaryAdapter = new WFHSummaryAdapter(getEmpWFHResponseList);
            summaryRecyclerView.setAdapter(summaryAdapter);
            summaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            summaryAdapter.notifyDataSetChanged();
        }
    }

    private class WFHSummaryAdapter extends
            RecyclerView.Adapter<WFHSummaryAdapter.MyViewHolder> {
        private ArrayList<GetEmpWFHResponseItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView requestIdTV, descriptionTV, startDateTV, endDateTV, daysTV, pendingWithTV, statusTV,startDateLabelTV;
            private Button viewBTN;
            private LinearLayout endDateLl,daysLl;


            public MyViewHolder(View v) {
                super(v);

                requestIdTV = (TextView) v.findViewById(R.id.requestIdTV);
                descriptionTV = (TextView) v.findViewById(R.id.descriptionTV);
                startDateTV = (TextView) v.findViewById(R.id.startDateTV);
                endDateTV = (TextView) v.findViewById(R.id.endDateTV);
                pendingWithTV = (TextView) v.findViewById(R.id.pendingWithTV);
                daysTV = (TextView) v.findViewById(R.id.daysTV);
                endDateLl= (LinearLayout) v.findViewById(R.id.endDateLl);
                startDateLabelTV= (TextView) v.findViewById(R.id.startDateLabelTV);
                statusTV = (TextView) v.findViewById(R.id.statusTV);
                viewBTN = (Button) v.findViewById(R.id.viewBTN);
                daysLl= (LinearLayout) v.findViewById(R.id.daysLl);


            }

        }

        public void addAll(List<GetEmpWFHResponseItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public WFHSummaryAdapter(List<GetEmpWFHResponseItem> data) {
            this.dataSet = (ArrayList<GetEmpWFHResponseItem>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tour_summary_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            final GetEmpWFHResponseItem item = dataSet.get(listPosition);
            holder.requestIdTV.setText(item.getReqCode());
            holder.descriptionTV.setText(item.getRequestTypeDesc());
            holder.daysLl.setVisibility(View.VISIBLE);

            if(item.getRequestTypeDesc().equalsIgnoreCase("OD")){
                holder.startDateLabelTV.setText(getResources().getString(R.string.date));
                holder.startDateTV.setText(item.getStartDate());
                holder.endDateLl.setVisibility(View.GONE);
            }else{
                holder.startDateLabelTV.setText(getResources().getString(R.string.start_date));
                holder.endDateLl.setVisibility(View.VISIBLE);
                holder.startDateTV.setText(item.getStartDate());
                holder.endDateTV.setText(item.getEndDate());
            }

            if(item.getRequestTypeDesc().equalsIgnoreCase("Time Modification") || item.getRequestTypeDesc().equalsIgnoreCase("Attendance")){
                holder.daysLl.setVisibility(View.GONE);
            }

            holder.daysTV.setText(item.getTotalDays());
            holder.pendingWithTV.setText(item.getPendWithName());
            holder.statusTV.setText(item.getStatusDesc());
            holder.viewBTN.setText(item.getButtons()[0]);

            holder.viewBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.WFH) && item.getStatusDesc()!=null && !item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)  ) {
                    ViewWFHSummaryFragment viewWFHSummaryFragment = new ViewWFHSummaryFragment();
                    viewWFHSummaryFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.view_advance_expense, viewWFHSummaryFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                    if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.WFH) && item.getStatusDesc()!=null && item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)) {
                        WorkFromHomeRequestFragment requestFragment = new WorkFromHomeRequestFragment();
                        requestFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, requestFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.OD) && item.getStatusDesc()!=null &&
                            !item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        ViewOdSummaryFragment viewOdSummaryFragment = new ViewOdSummaryFragment();
                        viewOdSummaryFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, viewOdSummaryFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.OD) && item.getStatusDesc()!=null &&
                            item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        OutdoorDutyRequestFragment outdoorDutyRequestFragment = new OutdoorDutyRequestFragment();
                        outdoorDutyRequestFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, outdoorDutyRequestFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }


                    if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.TOUR)&& item.getStatusDesc()!=null &&
                            !item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        ViewTourSummaryFragment viewTourSummaryFragment = new ViewTourSummaryFragment();
                        viewTourSummaryFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, viewTourSummaryFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    if(item.getRequestTypeDesc()!=null && item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.TOUR)&& item.getStatusDesc()!=null &&
                            item.getStatusDesc().equalsIgnoreCase(AppsConstant.DRAFT)){
                        TourRequestFragment tourRequestFragment = new TourRequestFragment();
                        tourRequestFragment.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, tourRequestFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    if(item.getRequestTypeDesc()!=null &&
                            item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.TIME_MODIFICATION)
                            ||  item.getRequestTypeDesc().equalsIgnoreCase(AppsConstant.BACK_DATED_ATTENDANCE)){
                        ViewTimeModificationSummary viewTimeModificationSummary = new ViewTimeModificationSummary();
                        viewTimeModificationSummary.setGetEmpWFHResponseItem(dataSet.get(listPosition));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_advance_expense, viewTimeModificationSummary);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }



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
