package hr.eazework.com.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.CustomFieldsModel;
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.GetWFHRequestDetail;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.TourCustomListResponse;
import hr.eazework.com.model.TourSummaryRequestDetail;
import hr.eazework.com.model.TourSummaryResponse;
import hr.eazework.com.model.WithdrawWFHResponse;
import hr.eazework.com.ui.adapter.DocumentUploadAdapter;
import hr.eazework.com.ui.adapter.RemarksAdapter;
import hr.eazework.com.ui.customview.CustomDialog;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class ViewTourSummaryFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "ViewTourSummaryFragment";
    private String screenName = "ViewTourSummaryFragment";
    private Preferences preferences;
    private TextView requestIdTV, empNameTV, empCodeTV, statusTV, reqInitiatorTV, reqDateTV, startDateTV, endDateTV, daysTV;
    private TextView travelFromTV, travelToTV, descriptionTV, classicToursTV, adventureTV, familyPackageTV, studentSpecialTV,
            religiousTravelTV, photographyTV, remarksTV, submittedByTV, pendingWithTV, reasonTV, accommodationTV, accmmodationDetailsTV;
    private TextView ticketsTV, ticketDetailsTV;
    private GetWFHRequestDetail requestDetail;
    private LinearLayout errorLinearLayout, remarksLinearLayout;
    private DocumentUploadAdapter documentViewAdapter;
    private ImageView plus_create_newIV;
    private RecyclerView documentRV, remarksRV;
    private Button withdrawBTN;
    private LinearLayout wfhSummaryLl,tourSummaryLl,odSummaryLl,docLl;
    private GetEmpWFHResponseItem getEmpWFHResponseItem;
    private TourCustomListResponse tourCustomListResponse;
    private ArrayList<CustomFieldsModel> customFieldsModel;
    private CustomFieldsAdapter adapter;
    private RecyclerView customFieldsRV;
    
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
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
                    }
                }
            }
        });

        wfhSummaryLl= (LinearLayout) rootView.findViewById(R.id.wfhSummaryLl);
        wfhSummaryLl.setVisibility(View.GONE);
        tourSummaryLl= (LinearLayout) rootView.findViewById(R.id.tourSummaryLl);
        tourSummaryLl.setVisibility(View.VISIBLE);
        odSummaryLl= (LinearLayout) rootView.findViewById(R.id.odSummaryLl);
        odSummaryLl.setVisibility(View.GONE);

        plus_create_newIV= (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        plus_create_newIV.setVisibility(View.GONE);
        //First
        requestIdTV = (TextView) rootView.findViewById(R.id.tourRequestIdTV);
        empNameTV = (TextView) rootView.findViewById(R.id.tourEmpNameTV);
        statusTV = (TextView) rootView.findViewById(R.id.tourStatusTV);
        // Second

        startDateTV = (TextView) rootView.findViewById(R.id.tourStartDateTV);
        endDateTV = (TextView) rootView.findViewById(R.id.tourEndDateTV);
        daysTV = (TextView) rootView.findViewById(R.id.tourDaysTV);
        travelFromTV = (TextView) rootView.findViewById(R.id.travelFromTV);
        travelToTV = (TextView) rootView.findViewById(R.id.travelToTV);
        reasonTV = (TextView) rootView.findViewById(R.id.reasonTV);

        accommodationTV = (TextView) rootView.findViewById(R.id.accommodationTV);
        accmmodationDetailsTV = (TextView) rootView.findViewById(R.id.accmmodationDetailsTV);
        ticketsTV = (TextView) rootView.findViewById(R.id.ticketsTV);
        ticketDetailsTV = (TextView) rootView.findViewById(R.id.ticketDetailsTV);

        descriptionTV = (TextView) rootView.findViewById(R.id.descriptionTV);
    /*    classicToursTV = (TextView) rootView.findViewById(R.id.classicToursTV);
        adventureTV = (TextView) rootView.findViewById(R.id.adventureTV);
        familyPackageTV = (TextView) rootView.findViewById(R.id.familyPackageTV);
        studentSpecialTV = (TextView) rootView.findViewById(R.id.studentSpecialTV);
        religiousTravelTV = (TextView) rootView.findViewById(R.id.religiousTravelTV);
        photographyTV = (TextView) rootView.findViewById(R.id.photographyTV);*/
     //   remarksTV = (TextView) rootView.findViewById(R.id.remarksTV);
        submittedByTV = (TextView) rootView.findViewById(R.id.tourSubmittedByTV);
        pendingWithTV = (TextView) rootView.findViewById(R.id.tourPendingWithTV);
        remarksLinearLayout = (LinearLayout) rootView.findViewById(R.id.remarksLinearLayout);
        remarksRV = (RecyclerView) rootView.findViewById(R.id.remarksRV);
        documentRV = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);

        customFieldsRV = (RecyclerView) rootView.findViewById(R.id.customFieldsRV);
        customFieldsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        withdrawBTN= (Button) rootView.findViewById(R.id.withdrawBTN);
        withdrawBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWithdrawRequestData();
            }
        });

        sendViewRequestSummaryData();

    }

    private void sendViewRequestSummaryData() {
        requestDetail = new GetWFHRequestDetail();

        requestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        requestDetail.setAction(AppsConstant.VIEW_ACTION);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL, true);
    }

    private void sendWithdrawRequestData() {
        requestDetail = new GetWFHRequestDetail();
        requestDetail.setReqID(getEmpWFHResponseItem.getReqID());
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.WFHSummaryDetails(requestDetail),
                CommunicationConstant.API_WITHDRAW_TOUR_REQUEST, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Tour Response : " + str);
                TourSummaryResponse tourSummaryResponse = TourSummaryResponse.create(str);
                if (tourSummaryResponse != null && tourSummaryResponse.getGetTourRequestDetailResult() != null
                        && tourSummaryResponse.getGetTourRequestDetailResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)
                        && tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail() != null) {
                   // sendTourRequestCustomFieldList();
                    updateUI(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail());
                    refreshRemarksList(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getRemarkList());
                    refreshDocumentList(tourSummaryResponse.getGetTourRequestDetailResult().getTourRequestDetail().getAttachments());
                }
                break;
            case CommunicationConstant.API_WITHDRAW_TOUR_REQUEST:
                String strResponse = response.getResponseData();
                Log.d("TAG", "Tour Response : " + strResponse);
                WithdrawWFHResponse withdrawWFHResponse = WithdrawWFHResponse.create(strResponse);
                if (withdrawWFHResponse != null && withdrawWFHResponse.getWithdrawTourRequestResult() != null
                        && withdrawWFHResponse.getWithdrawTourRequestResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    CustomDialog.alertOkWithFinishFragment(context, withdrawWFHResponse.getWithdrawTourRequestResult().getErrorMessage(), mUserActionListener, IAction.HOME_VIEW, true);
                } else {
                    new AlertCustomDialog(getActivity(), withdrawWFHResponse.getWithdrawTourRequestResult().getErrorMessage());
                }
                break;
            default:
                break;
        }
        super.validateResponse(response);
    }

    private void updateUI(TourSummaryRequestDetail item) {
        requestIdTV.setText(item.getReqCode());
        statusTV.setText(item.getStatusDesc());
        empNameTV.setText(item.getForEmpName());
        submittedByTV.setText(item.getSubmittedBy());
        pendingWithTV.setText(item.getPendWithName());
        startDateTV.setText(item.getStartDate());
        endDateTV.setText(item.getEndDate());
        daysTV.setText(item.getTotalDays() +" "+ "day(s)");
        travelFromTV.setText(item.getTravelFrom());
        travelToTV.setText(item.getTravelTo());
        reasonTV.setText(item.getTourReason().getReason());
        if (item.getAccomodationYN() != null && item.getAccomodationYN().equalsIgnoreCase("Y")) {
            accommodationTV.setText(getResources().getString(R.string.yes));
        }

        if (item.getAccomodationYN() != null && item.getAccomodationYN().equalsIgnoreCase("N")) {
            accommodationTV.setText(getResources().getString(R.string.no));
        }

        accmmodationDetailsTV.setText(item.getAccomodationDet());

        if (item.getTicketYN() != null && item.getTicketYN().equalsIgnoreCase("Y")) {
            ticketsTV.setText(getResources().getString(R.string.yes));
        }

        if (item.getTicketYN() != null && item.getTicketYN().equalsIgnoreCase("N")) {
            ticketsTV.setText(getResources().getString(R.string.no));
        }
        ticketDetailsTV.setText(item.getTicketDet());
        //remarksTV.setText(item.getRemark());

     /*   if(item.getCustomFields()!=null && item.getCustomFields().size()>0){
            for(CustomFieldsModel customFieldsModel:item.getCustomFields()){
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.CLASSICAL_TOUR)){
                    classicToursTV.setText(customFieldsModel.getFieldValue());
                }
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.ADVENTURE)){
                    adventureTV.setText(customFieldsModel.getFieldValue());
                }
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.FAMILY_PACKAGE)){
                    familyPackageTV.setText(customFieldsModel.getFieldValue());
                }
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.STUDENT_SPECIAL)){
                    studentSpecialTV.setText(customFieldsModel.getFieldValue());
                }
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.RELIGIOUS_TRAVEL)){
                    religiousTravelTV.setText(customFieldsModel.getFieldValue());
                }
                if(customFieldsModel.getFieldCode().equalsIgnoreCase(AppsConstant.PHOTOGRAPHY)){
                    photographyTV.setText(customFieldsModel.getFieldValue());
                }
            }
        }*/
        refreshCustomFields(item.getCustomFields());

        setupButtons(item);
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

    private void refreshDocumentList(ArrayList<SupportDocsItemModel> docListModels) {
        if (docListModels != null && docListModels.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            documentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            documentRV.setVisibility(View.VISIBLE);
            documentViewAdapter = new DocumentUploadAdapter(docListModels, context, AppsConstant.VIEW, errorLinearLayout, getActivity());
            documentRV.setAdapter(documentViewAdapter);
            documentViewAdapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            documentRV.setVisibility(View.GONE);
        }
    }

    private void setupButtons(TourSummaryRequestDetail item){
        if(item.getButtons()!=null){
            for(String button : item.getButtons() ){
                if(button.equalsIgnoreCase(AppsConstant.WITHDRAW)){
                    withdrawBTN.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void refreshCustomFields(ArrayList<CustomFieldsModel> customFieldsModel) {
        if (customFieldsModel != null && customFieldsModel.size() > 0) {
            adapter = new CustomFieldsAdapter(customFieldsModel);
            customFieldsRV.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    public class CustomFieldsAdapter extends
            RecyclerView.Adapter<CustomFieldsAdapter.MyViewHolder> {
        public ArrayList<CustomFieldsModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView editTextLabelTV;
            public LinearLayout editTextLinearLayout;
            private TextView editTextValueEt;


            public MyViewHolder(View v) {
                super(v);
                editTextValueEt = (TextView) v.findViewById(R.id.editTextValueEt);
                editTextLabelTV= (TextView) v.findViewById(R.id.editTextLabelTV);
            }
        }

        public void addAll(List<CustomFieldsModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public CustomFieldsAdapter(List<CustomFieldsModel> data) {
            this.dataSet = (ArrayList<CustomFieldsModel>) data;

        }

        @Override
        public CustomFieldsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_custom_fields, parent, false);
            CustomFieldsAdapter.MyViewHolder myViewHolder = new CustomFieldsAdapter.MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final CustomFieldsAdapter.MyViewHolder holder, final int listPosition) {

            final CustomFieldsModel item = dataSet.get(listPosition);
            holder.editTextLabelTV.setText(item.getFieldLabel());
            holder.editTextValueEt.setText(item.getFieldValue());


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
