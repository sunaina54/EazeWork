package hr.eazework.com.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class EditTourApprovalFragment extends BaseFragment {

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
        reqInitiatorTV= (TextView) rootView.findViewById(R.id.reqInitiatorTV);
        reqDateTV= (TextView) rootView.findViewById(R.id.reqDateTV);
        startDateTV= (TextView) rootView.findViewById(R.id.startDateTV);
        endDateTV= (TextView) rootView.findViewById(R.id.endDateTV);
        daysTV= (TextView) rootView.findViewById(R.id.daysTV);
        travelFromTV= (TextView) rootView.findViewById(R.id.travelFromTV);
        travelToTV= (TextView) rootView.findViewById(R.id.travelToTV);
        descriptionTV= (TextView) rootView.findViewById(R.id.descriptionTV);
        classicToursTV= (TextView) rootView.findViewById(R.id.classicToursTV);
        adventureTV= (TextView) rootView.findViewById(R.id.adventureTV);
        familyPackageTV= (TextView) rootView.findViewById(R.id.familyPackageTV);
        studentSpecialTV= (TextView) rootView.findViewById(R.id.studentSpecialTV);
        religiousTravelTV= (TextView) rootView.findViewById(R.id.religiousTravelTV);
        photographyTV= (TextView) rootView.findViewById(R.id.photographyTV);
        remarksTV= (TextView) rootView.findViewById(R.id.remarksTV);
        //third
        reasonTV= (TextView) rootView.findViewById(R.id.reasonTV);
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
        travelToET= (EditText) rootView.findViewById(R.id.travelToET);
        totalDayTV= (TextView) rootView.findViewById(R.id.daysTV);
        remarksET= (EditText) rootView.findViewById(R.id.remarksET);
    }

}
