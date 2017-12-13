package hr.eazework.com.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.SearchOnbehalfActivity;
import hr.eazework.com.model.EmployItem;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;

public class WorkFromHomeRequestFragment extends BaseFragment {
    private Context context;
    public static final String TAG = "WorkFromHomeRequestFragment";
    private String screenName = "WorkFromHomeRequestFragment";
    private Button saveDraftBTN;
    private Preferences preferences;
    private TextView empNameTV,empCodeTV,tv_to_day, tv_to_date, tv_from_date, tv_from_day;
    private EditText remarksET;
    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private String fromButton;
    private EmployItem employItem;
    private RelativeLayout searchLayout;
    public static int WFH_EMP=1;
    private String empId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_work_from_home_request, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        setupScreen();
        return rootView;
    }

    private void setupScreen(){
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText(R.string.work_from_home);
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
        empNameTV= (TextView) rootView.findViewById(R.id.empNameTV);
        empCodeTV= (TextView) rootView.findViewById(R.id.empCodeTV);
        remarksET= (EditText) rootView.findViewById(R.id.remarksET);
        saveDraftBTN= (Button) rootView.findViewById(R.id.saveDraftBTN);
        saveDraftBTN.setVisibility(View.VISIBLE);
        saveDraftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromButton="Save";
            }
        });

        employItem=new EmployItem();
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();

        employItem.setEmpID(Long.parseLong(loginUserModel.getUserModel().getEmpId()));
        employItem.setName(loginUserModel.getUserModel().getUserName());
        employItem.setEmpCode(loginUserModel.getUserModel().getEmpCode());

        empNameTV.setText(employItem.getName());
        empCodeTV.setText(employItem.getEmpCode());

        searchLayout= (RelativeLayout) rootView.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(getActivity(), SearchOnbehalfActivity.class);
                startActivityForResult(theIntent,WFH_EMP);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WFH_EMP) {
            if (data != null) {
                EmployItem item = (EmployItem) data.getSerializableExtra(SearchOnbehalfActivity.SELECTED_WFH_EMP);
                if (item != null) {
                    String[] empname=item.getName().split("\\(");
                    empNameTV.setText(empname[0]);
                    empId = String.valueOf(item.getEmpID());
                    empCodeTV.setText(item.getEmpCode());
                    employItem = item;
                }

            }
        }

    }
}
