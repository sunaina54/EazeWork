package hr.eazework.com.ui.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourRequestFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "TourRequestFragment";
    private String screenName = "TourRequestFragment";
    private Button saveDraftBTN;
    private Preferences preferences;
    private TextView empNameTV,empCodeTV,tv_to_day, tv_to_date, tv_from_date, tv_from_day,reasonTV,studentSpecialTV
            ,tv_photography_day,tv_photography_date;
    private EditText remarksET,travelFromEt,travelToET,descriptionET,classicToursEt,adventureET,familyPackageET,religiousTravelET;
    private DatePickerDialog datePickerDialog1, datePickerDialog2,photographyDatePickerDialog;
    private String fromButton;
    private LinearLayout ll_photography;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tour_request, container, false);
        context = getContext();
       setupScreen();
        return rootView;
    }

    private void setupScreen(){
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.tour);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton="Submit";
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
            }
        });
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
        tv_photography_day= (TextView) rootView.findViewById(R.id.tv_photography_day);
        tv_photography_date= (TextView) rootView.findViewById(R.id.tv_photography_date);
        photographyDatePickerDialog = CalenderUtils.pickDateFromCalender(context, tv_photography_date, tv_photography_day, AppsConstant.DATE_FORMATE);
        ll_photography= (LinearLayout) rootView.findViewById(R.id.ll_photography);
        ll_photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photographyDatePickerDialog.show();
            }
        });

        empNameTV= (TextView) rootView.findViewById(R.id.empNameTV);
        empCodeTV= (TextView) rootView.findViewById(R.id.empCodeTV);
        reasonTV= (TextView) rootView.findViewById(R.id.reasonTV);
        studentSpecialTV= (TextView) rootView.findViewById(R.id.studentSpecialTV);
        remarksET= (EditText) rootView.findViewById(R.id.remarksET);
        travelFromEt= (EditText) rootView.findViewById(R.id.travelFromEt);
        travelToET= (EditText) rootView.findViewById(R.id.travelToET);
        descriptionET= (EditText) rootView.findViewById(R.id.descriptionET);
        classicToursEt= (EditText) rootView.findViewById(R.id.classicToursEt);
        adventureET= (EditText) rootView.findViewById(R.id.adventureET);
        familyPackageET= (EditText) rootView.findViewById(R.id.familyPackageET);
        religiousTravelET= (EditText) rootView.findViewById(R.id.religiousTravelET);

        saveDraftBTN= (Button) rootView.findViewById(R.id.saveDraftBTN);
        saveDraftBTN.setVisibility(View.VISIBLE);
        saveDraftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton="Save";
            }
        });
    }
}
