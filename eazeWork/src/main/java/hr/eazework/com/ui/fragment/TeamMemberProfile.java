package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.EmployeeDetailModel;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import io.fabric.sdk.android.services.common.Crash;

import static hr.eazework.com.ui.util.Utility.setCorpBackground;

/**
 * Created by Manjunath on 27-03-2017.
 */

public class
TeamMemberProfile extends BaseFragment {

    public static String TAG = "TeamMemberProfile";
    private String empId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empId = getArguments().getString(AppsConstant.EMP_ID, "");

    }

    public void updataProfileData(EmployeeDetailModel model,Context context, View rootView, String name, String department, String Designation, String empID, String profilePhoto) {
        setCorpBackground(context, rootView);
        if(name!=null && !name.equalsIgnoreCase("")) {
            ((TextView) rootView.findViewById(R.id.tv_profile_name)).setText(name);
        }else {
            ((TextView) rootView.findViewById(R.id.tv_profile_name)).setVisibility(View.GONE);
        }
        ((TextView) rootView.findViewById(R.id.tv_department)).setVisibility(View.GONE);

        if(model.getmDeptNameYN().equalsIgnoreCase("Y")){
            ((TextView) rootView.findViewById(R.id.tv_department)).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.tv_department)).setText(context.getString(R.string.msg_department) + " " + department);
        }

        ((TextView) rootView.findViewById(R.id.tv_role)).setVisibility(View.GONE);
        if(model.getmDesignationYN().equalsIgnoreCase("Y"))
            ((TextView) rootView.findViewById(R.id.tv_role)).setText(context.getString(R.string.msg_role) + " " + Designation);

        if(empID!=null && !empID.equalsIgnoreCase(""))
            ((TextView) rootView.findViewById(R.id.tv_employee)).setText(context.getString(R.string.msg_employee_id) + " " + empID);
        else
            ((TextView) rootView.findViewById(R.id.tv_employee)).setVisibility(View.GONE);

        Picasso.with(context).load(CommunicationConstant.getMobileCareURl() + profilePhoto)
                .fit()
                .into((ImageView) rootView.findViewById(R.id.img_user_img));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeam(true);
        rootView = inflater.inflate(R.layout.team_member_profile, container, false);
        requestAPI();
        setCorpBackground(getContext(), rootView);
        int textColor = Utility.getTextColorCode(new Preferences(getContext()));
        ((MainActivity) getActivity()).edit.setTextColor(textColor);

        return rootView;

    }


    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getEmployeeDetails(empId), CommunicationConstant.API_GET_EMPLOYEE_DETAIL, false);
    }


    @Override
    public void validateResponse(ResponseData response) {
        super.validateResponse(response);
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EMPLOYEE_DETAIL:
                JSONObject json;
                try {
                    json = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = json.optJSONObject("GetEmployeeDetailsResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        String getEmployeeDetailsResult = json.optJSONObject("GetEmployeeDetailsResult").toString();
                        JSONObject array = new JSONObject(getEmployeeDetailsResult).optJSONObject("employeeDetails");
                        populateViews(array);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
                break;
        }
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);

    }


    private void populateViews(JSONObject array) {

        ModelManager.getInstance().setEmployeeDetailModel(array.toString());
        EmployeeDetailModel model = ModelManager.getInstance().getEmployeeDetailModel();
        if (model != null) {
            updataProfileData(model,getContext(), rootView, array.optString("FirstName", "") + " " + array.optString("MiddleName","") + " " + array.optString("LastName", ""),
                    array.optString("DeptName", ""), array.optString("Designation", ""),
                    array.optString("EmpCode", ""),
                    array.optString("EmpImageUrl", ""));

            LinearLayout empPersonalFieldLayout = (LinearLayout) rootView.findViewById(R.id.ll_earnings_container);
            LinearLayout empOfficialFieldLayout = (LinearLayout) rootView.findViewById(R.id.ll_deduction_container);
            empOfficialFieldLayout.removeAllViews();
            empPersonalFieldLayout.removeAllViews();


            Activity activity = getActivity();
            if(model.getmEmail()!=null && !model.getmEmail().equalsIgnoreCase(""))
                Utility.addElementToView(activity,empPersonalFieldLayout,"Email",model.getmEmail());
            if(model.getmDateOfBirth()!=null && !model.getmDateOfBirth().equalsIgnoreCase(""))
                Utility.addElementToView(activity,empPersonalFieldLayout,"Date Of Birth",model.getmDateOfBirth());

            if(model.getmDateOfJoining()!=null && !model.getmDateOfJoining().equalsIgnoreCase(""))
                Utility.addElementToView(activity,empPersonalFieldLayout,"Date Of Joining",model.getmDateOfJoining());

            if(model.getmMaritalStatusYN().equalsIgnoreCase("Y"))
                Utility.addElementToView(activity,empPersonalFieldLayout,"Marital Status",model.getmMaritalStatusDesc());


            if(model.getmCompanyNameYN().equalsIgnoreCase("y")){
                Utility.addElementToView(activity,empOfficialFieldLayout,"Company Name",model.getmCompanyName());
            }

            if(model.getmMangerName()!=null && !model.getmMangerName().equalsIgnoreCase("")){
                Utility.addElementToView(activity, empOfficialFieldLayout, "Manager", model.getmMangerName());
            }
            if(model.getmFnMangerNameYN().equalsIgnoreCase("Y")){
                Utility.addElementToView(activity, empOfficialFieldLayout, "Functional Manager", model.getmFunctionalManager());
            }
            if(model.getmOfficeLocation()!=null && !model.getmOfficeLocation().equalsIgnoreCase("")) {
                Utility.addElementToView(activity, empOfficialFieldLayout, "Office Location", model.getmOfficeLocation());
            }
            if (model.getmWorkLocationYN().equalsIgnoreCase("Y")) {
                Utility.addElementToView(activity, empOfficialFieldLayout, "Work Location", model.getmWorkLocation());
            }
            if(model.getmDeptNameYN().equalsIgnoreCase("Y")){
                Utility.addElementToView(activity,empOfficialFieldLayout,"Department",model.getmDeptName());
            }
            if(model.getmSubDepartmentYN().equalsIgnoreCase("y")) {
                Utility.addElementToView(activity,empOfficialFieldLayout,"Sub-Department",model.getmSubDepartment());
            }
            if(model.getmDivNameYN().equalsIgnoreCase("y")) {
                Utility.addElementToView(activity,empOfficialFieldLayout,"Division",model.getmDivName());
            }
            if(model.getmSubDivisionNameYN().equalsIgnoreCase("y")) {
                Utility.addElementToView(activity,empOfficialFieldLayout,"Sub-Division",model.getmSubDivisionName());
            }
            Utility.addElementToView(activity, empOfficialFieldLayout, "Status", model.getmEmploymentStatusDesc());
        }
    }

}
