package hr.eazework.com.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;


public class ViewTourSummaryFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "ViewTourSummaryFragment";
    private String screenName = "ViewTourSummaryFragment";
    private Preferences preferences;
    private TextView requestIdTV,empNameTV,empCodeTV,statusTV,reqInitiatorTV,reqDateTV,startDateTV,endDateTV,daysTV;
    private TextView travelFromTV,travelToTV,descriptionTV,classicToursTV,adventureTV,familyPackageTV,studentSpecialTV,
            religiousTravelTV,photographyTV,remarksTV;



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
    }

}
