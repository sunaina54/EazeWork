package hr.eazework.com.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AttendanceReqDetail;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetODRequestDetail;
import hr.eazework.com.model.GetTimeModificationRequestDetail;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.ODRequestDetail;
import hr.eazework.com.model.ODSummaryResponse;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TimeModificationSummaryResponseModel;
import hr.eazework.com.model.WithdrawWFHResponse;
import hr.eazework.com.ui.adapter.DocumentUploadAdapter;
import hr.eazework.com.ui.adapter.RemarksAdapter;
import hr.eazework.com.ui.customview.CustomDialog;
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
 * Created by Dell3 on 07-02-2018.
 */

public class ViewTimeModificationSummary extends BaseFragment {

    private Context context;
    public static final String TAG = "ViewTimeModificationSummary";
    private String screenName = "ViewTimeModificationSummary";
    private Preferences preferences;
    private TextView requestIdTV, empNameTV, empCodeTV, statusTV, reqInitiatorTV, reqDateTV, startDateTV, endDateTV, daysTV;
    private TextView dateTV, submittedByTV, pendingWithTV, startTimeTV, endTimeTV, requestedInTimeTV, requestedOutTimeTV;
    private LinearLayout remarksLinearLayout, wfhSummaryLl, tourSummaryLl, odSummaryLl,timeModificationSummaryLl,docLl,requestedOutTimeLl
            ,requestedInTimeLl;
    private RecyclerView remarksRV;
    private GetTimeModificationRequestDetail getTimeModificationRequestDetail;

    private GetWFHRequestDetail requestDetail;
    private Button withdrawBTN;
    private LinearLayout errorLinearLayout;
    private DocumentUploadAdapter documentViewAdapter;
    private RecyclerView documentRV;
    private ImageView plus_create_newIV;
    private GetEmpWFHResponseItem getEmpWFHResponseItem;

    public GetEmpWFHResponseItem getGetEmpWFHResponseItem() {
        return getEmpWFHResponseItem;
    }

    public void setGetEmpWFHResponseItem(GetEmpWFHResponseItem getEmpWFHResponseItem) {
        this.getEmpWFHResponseItem = getEmpWFHResponseItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_tour_summary, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        setupScreen();
        return rootView;
    }

    private void setupScreen() {
        context = getActivity();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.view_tour_summary);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);

        wfhSummaryLl = (LinearLayout) rootView.findViewById(R.id.wfhSummaryLl);
        wfhSummaryLl.setVisibility(View.GONE);
        tourSummaryLl = (LinearLayout) rootView.findViewById(R.id.tourSummaryLl);
        tourSummaryLl.setVisibility(View.GONE);
        odSummaryLl = (LinearLayout) rootView.findViewById(R.id.odSummaryLl);
        odSummaryLl.setVisibility(View.GONE);
        timeModificationSummaryLl= (LinearLayout) rootView.findViewById(R.id.timeModificationSummaryLl);
        timeModificationSummaryLl.setVisibility(View.VISIBLE);
        docLl= (LinearLayout) rootView.findViewById(R.id.docLl);
        docLl.setVisibility(View.GONE);

        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        plus_create_newIV.setVisibility(View.GONE);

        //First
        requestIdTV = (TextView) rootView.findViewById(R.id.requestIdTmTV);
        empNameTV = (TextView) rootView.findViewById(R.id.empNameTmTV);
        statusTV = (TextView) rootView.findViewById(R.id.statusTmTV);
        submittedByTV = (TextView) rootView.findViewById(R.id.submittedByTmTV1);
        pendingWithTV = (TextView) rootView.findViewById(R.id.pendingWithTmTV);
        dateTV = (TextView) rootView.findViewById(R.id.dateTmTV);
        startTimeTV = (TextView) rootView.findViewById(R.id.startTimeTmTV);
        endTimeTV = (TextView) rootView.findViewById(R.id.endTimeTmTV);
        requestedInTimeTV = (TextView) rootView.findViewById(R.id.requestedInTimeTV);
        requestedOutTimeTV = (TextView) rootView.findViewById(R.id.requestedOutTimeTV);
        requestedInTimeLl= (LinearLayout) rootView.findViewById(R.id.requestedInTimeLl);
        requestedOutTimeLl= (LinearLayout) rootView.findViewById(R.id.requestedOutTimeLl);

        sendViewRequestSummaryData();


    }

    private void sendViewRequestSummaryData() {
        getTimeModificationRequestDetail = new GetTimeModificationRequestDetail();
        getTimeModificationRequestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.timeModificationSummaryDetails(getTimeModificationRequestDetail),
                CommunicationConstant.API_GET_ATTENDANCE_DETAIL, true);
    }


    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ATTENDANCE_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Time Modification Summary Response : " + str);
                TimeModificationSummaryResponseModel summaryResponseModel = TimeModificationSummaryResponseModel.create(str);
                if (summaryResponseModel != null && summaryResponseModel.getGetAttendanceReqDetailResult() != null
                        && summaryResponseModel.getGetAttendanceReqDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail() != null) {
                    updateUI(summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail());
                    refreshRemarksList(summaryResponseModel.getGetAttendanceReqDetailResult().getAttendanceReqDetail().getRemarkList());
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }


    private void updateUI(AttendanceReqDetail item) {
        if (item.getReqCategoryDesc().equalsIgnoreCase(AppsConstant.BACK_DATED_ATTENDANCE)) {
            submittedByTV.setText(item.getSubmittedBy());
            pendingWithTV.setText(item.getPendWithName());
            requestIdTV.setText(item.getReqCode());
            empNameTV.setText(item.getName());
            statusTV.setText(item.getStatusDesc());
            dateTV.setText(item.getMarkDate());
            String[] reqTime= item.getReqTime().split(" ");
            String[] reqOutTime= item.getReqOutTime().split(" ");
            startTimeTV.setText(reqTime[1]+" "+reqTime[2]);
            endTimeTV.setText(reqOutTime[1]+" "+reqOutTime[2]);
            requestedOutTimeLl.setVisibility(View.GONE);
            requestedInTimeLl.setVisibility(View.GONE);

        } else {
            submittedByTV.setText(item.getSubmittedBy());
            pendingWithTV.setText(item.getPendWithName());
            requestIdTV.setText(item.getReqCode());
            empNameTV.setText(item.getName());
            statusTV.setText(item.getStatusDesc());
            dateTV.setText(item.getMarkDate());
            String[] existingTime= item.getExistingTime().split(" ");
            startTimeTV.setText(existingTime[1] + " "+existingTime[2]);
            String[] existingOutTime= item.getExistingTime().split(" ");
            endTimeTV.setText(existingOutTime[1]+" "+ existingOutTime[2]);
            requestedOutTimeLl.setVisibility(View.VISIBLE);
            requestedInTimeLl.setVisibility(View.VISIBLE);
            String[] reqTime= item.getReqTime().split(" ");
            String[] reqOutTime= item.getReqOutTime().split(" ");
            requestedInTimeTV.setText(reqTime[1]+" "+reqTime[2]);
            requestedOutTimeTV.setText(reqOutTime[1]+" "+reqOutTime[2]);
        }
    }


    private void refreshRemarksList(ArrayList<RemarkListItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            remarksRV.setVisibility(View.VISIBLE);
            RemarksAdapter adapter = new RemarksAdapter(remarksItems, context, screenName, remarksLinearLayout);
            remarksRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
        }
    }
}
