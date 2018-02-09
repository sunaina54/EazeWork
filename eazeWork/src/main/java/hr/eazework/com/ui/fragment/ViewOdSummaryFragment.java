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
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetODRequestDetail;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.ODRequestDetail;
import hr.eazework.com.model.ODSummaryResponse;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TourSummaryRequestDetail;
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
 * Created by Dell3 on 17-01-2018.
 */

public class ViewOdSummaryFragment extends BaseFragment {

    private Context context;
    public static final String TAG = "ViewTourSummaryFragment";
    private String screenName = "ViewTourSummaryFragment";
    private Preferences preferences;
    private TextView requestIdTV,empNameTV,empCodeTV,statusTV,reqInitiatorTV,reqDateTV,startDateTV,endDateTV,daysTV;
    private TextView dateTV,submittedByTV,pendingWithTV,startTimeTV,endTimeTV,placeOdTV;
    private LinearLayout remarksLinearLayout,wfhSummaryLl,tourSummaryLl,odSummaryLl;
    private RecyclerView remarksRV;
    private GetODRequestDetail getODRequestDetail;

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

    private void setupScreen(){
        context = getActivity();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.view_tour_summary);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);

        wfhSummaryLl= (LinearLayout) rootView.findViewById(R.id.wfhSummaryLl);
        wfhSummaryLl.setVisibility(View.GONE);
        tourSummaryLl= (LinearLayout) rootView.findViewById(R.id.tourSummaryLl);
        tourSummaryLl.setVisibility(View.GONE);
        odSummaryLl= (LinearLayout) rootView.findViewById(R.id.odSummaryLl);
        odSummaryLl.setVisibility(View.VISIBLE);

        remarksLinearLayout= (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV= (RecyclerView) rootView.findViewById(R.id.remarksRV);
        plus_create_newIV= (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        plus_create_newIV.setVisibility(View.GONE);

        //First
        requestIdTV= (TextView) rootView.findViewById(R.id.requestIdOdTV);
        empNameTV= (TextView) rootView.findViewById(R.id.empNameOdTV);
        statusTV= (TextView) rootView.findViewById(R.id.statusOdTV);
        submittedByTV= (TextView) rootView.findViewById(R.id.submittedByTV1);
        pendingWithTV= (TextView) rootView.findViewById(R.id.pendingWithOdTV);
        dateTV= (TextView) rootView.findViewById(R.id.dateTV);
        startTimeTV= (TextView) rootView.findViewById(R.id.startTimeTV);
        endTimeTV= (TextView) rootView.findViewById(R.id.endTimeTV);
        placeOdTV= (TextView) rootView.findViewById(R.id.placeOdTV);

        documentRV = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);

        sendViewRequestSummaryData();

        withdrawBTN= (Button) rootView.findViewById(R.id.withdrawBTN);
        withdrawBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendODRequestData();
            }
        });

    }

    private void sendViewRequestSummaryData() {
        getODRequestDetail = new GetODRequestDetail();
        getODRequestDetail.setReqID(Integer.parseInt(getEmpWFHResponseItem.getReqID()));
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.ODSummaryDetails(getODRequestDetail),
                CommunicationConstant.API_GET_OD_REQUEST_DETAIL, true);
    }

    private void sendODRequestData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_WITHDRAW_OD_REQUEST, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_OD_REQUEST_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "OD Summary Response : " + str);
                ODSummaryResponse odSummaryResponse = ODSummaryResponse.create(str);
                if (odSummaryResponse != null && odSummaryResponse.getGetODRequestDetailResult() != null
                        && odSummaryResponse.getGetODRequestDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail()!=null) {
                    updateUI(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail());
                    refreshRemarksList(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getRemarkList());
                    refreshDocumentList(odSummaryResponse.getGetODRequestDetailResult().getODRequestDetail().getAttachments());
                }

                break;

            case CommunicationConstant.API_WITHDRAW_OD_REQUEST:
                String strResponse = response.getResponseData();
                Log.d("TAG", "od withdraw response : " + strResponse);
                WithdrawWFHResponse withdrawWFHResponse = WithdrawWFHResponse.create(strResponse);
                if (withdrawWFHResponse != null && withdrawWFHResponse.getWithdrawODRequestResult() != null
                        && withdrawWFHResponse.getWithdrawODRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, withdrawWFHResponse.getWithdrawODRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                }else {
                    new AlertCustomDialog(getActivity(), withdrawWFHResponse.getWithdrawODRequestResult().getErrorMessage());
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }


    private void updateUI(ODRequestDetail item){
        submittedByTV.setText(item.getSubmittedBy());
        pendingWithTV.setText(item.getPendWithName());
        requestIdTV.setText(item.getReqCode());
        empNameTV.setText(item.getForEmpName());
        statusTV.setText(item.getStatusDesc());
        dateTV.setText(item.getDate());
        startTimeTV.setText(item.getStartTime());
        endTimeTV.setText(item.getEndTime());
        placeOdTV.setText(item.getPlace());
        setupButtons(item);
    }

    private void setupButtons(ODRequestDetail item){
        if(item.getButtons()!=null){
            for(String button : item.getButtons() ){
                if(button.equalsIgnoreCase(AppsConstant.WITHDRAW)){
                    withdrawBTN.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void refreshRemarksList(ArrayList<RemarkListItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            remarksRV.setVisibility(View.VISIBLE);
            RemarksAdapter adapter = new RemarksAdapter(remarksItems,context,screenName,remarksLinearLayout);
            remarksRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
        }
    }

    private void refreshDocumentList(ArrayList<SupportDocsItemModel> docListModels) {
        if (docListModels != null && docListModels.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            documentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            documentRV.setVisibility(View.VISIBLE);
            documentViewAdapter = new DocumentUploadAdapter(docListModels,context,AppsConstant.VIEW,errorLinearLayout,getActivity());
            documentRV.setAdapter(documentViewAdapter);
            documentViewAdapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            documentRV.setVisibility(View.GONE);
        }
    }
}
