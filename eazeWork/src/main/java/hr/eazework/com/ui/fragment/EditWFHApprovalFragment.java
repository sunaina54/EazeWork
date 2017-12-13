package hr.eazework.com.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;

/**
 * Created by Dell3 on 11-12-2017.
 */

public class EditWFHApprovalFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "ViewTourSummaryFragment";
    private String screenName = "ViewTourSummaryFragment";
    private Preferences preferences;
    private TextView requestIdTV,empNameTV,empCodeTV,statusTV,reqInitiatorTV,reqDateTV,startDateTV,endDateTV,daysTV;
    private TextView travelFromTV,travelToTV,descriptionTV,classicToursTV,adventureTV,familyPackageTV,studentSpecialTV,
            religiousTravelTV,photographyTV,remarksTV,reasonTV;
    private TextView tv_to_day, tv_to_date, tv_from_date, tv_from_day,totalDayTV;
    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private EditText remarksET,travelFromEt,travelToET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_tour_approval, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        setupScreen();
        return rootView;
    }
    private void setupScreen(){
        context = getActivity();
        //First
        requestIdTV= (TextView) rootView.findViewById(R.id.requestIdTV);
        empNameTV= (TextView) rootView.findViewById(R.id.empNameTV);
        empCodeTV= (TextView) rootView.findViewById(R.id.empCodeTV);
        statusTV= (TextView) rootView.findViewById(R.id.statusTV);
        // Second
        travelFromTV= (TextView) rootView.findViewById(R.id.travelFromTV);
        travelFromTV.setVisibility(View.GONE);
        travelToTV= (TextView) rootView.findViewById(R.id.travelToTV);
        travelFromTV.setVisibility(View.GONE);
        descriptionTV= (TextView) rootView.findViewById(R.id.descriptionTV);
        descriptionTV.setVisibility(View.GONE);
        classicToursTV= (TextView) rootView.findViewById(R.id.classicToursTV);
        classicToursTV.setVisibility(View.GONE);
        adventureTV= (TextView) rootView.findViewById(R.id.adventureTV);
        adventureTV.setVisibility(View.GONE);
        familyPackageTV= (TextView) rootView.findViewById(R.id.familyPackageTV);
        familyPackageTV.setVisibility(View.GONE);
        studentSpecialTV= (TextView) rootView.findViewById(R.id.studentSpecialTV);
        studentSpecialTV.setVisibility(View.GONE);
        religiousTravelTV= (TextView) rootView.findViewById(R.id.religiousTravelTV);
        religiousTravelTV.setVisibility(View.GONE);
        photographyTV= (TextView) rootView.findViewById(R.id.photographyTV);
        photographyTV.setVisibility(View.GONE);
        remarksTV= (TextView) rootView.findViewById(R.id.remarksTV);
        //third
        reasonTV= (TextView) rootView.findViewById(R.id.reasonTV);
        reasonTV.setVisibility(View.GONE);
        tv_to_day = ((TextView) rootView.findViewById(R.id.tv_to_day));
        tv_to_date = ((TextView) rootView.findViewById(R.id.tv_to_date));
        tv_from_day = ((TextView) rootView.findViewById(R.id.tv_from_day));
        tv_from_date = ((TextView) rootView.findViewById(R.id.tv_from_date));
        datePickerDialog1 = CalenderUtils.pickDateFromCalender(context, tv_to_date, tv_to_day, AppsConstant.DATE_FORMATE);
        datePickerDialog2 = CalenderUtils.pickDateFromCalender(context, tv_from_date, tv_from_day, AppsConstant.DATE_FORMATE);
        rootView.findViewById(R.id.ll_from_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });
        rootView.findViewById(R.id.ll_to_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog1.show();

            }
        });
        travelFromEt= (EditText) rootView.findViewById(R.id.travelFromEt);
        travelFromEt.setVisibility(View.GONE);
        travelToET= (EditText) rootView.findViewById(R.id.travelToET);
        travelToET.setVisibility(View.GONE);
        totalDayTV= (TextView) rootView.findViewById(R.id.daysTV);
        remarksET= (EditText) rootView.findViewById(R.id.remarksET);

    }


}
