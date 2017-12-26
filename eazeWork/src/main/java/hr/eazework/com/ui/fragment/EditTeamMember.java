package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.EmployeeDetailModel;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

import static hr.eazework.com.ui.util.Utility.navigateToTeamOrHome;

/**
 * Created by Manjunath on 27-03-2017.
 */

public class EditTeamMember extends BaseFragment {

    public static String TAG = "EditTeamMember";
    private String workLocationID = "";
    private String managerID = "";
    private TextView manager;
    private TextView office;
    private TextView work;
    private String reqId = "0";
    private boolean isSubmitClicked = true;
    private TextView managerTV,workLocationTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_team_member, container, false);
        manager = (TextView) rootView.findViewById(R.id.tvManager);
        office = (TextView) rootView.findViewById(R.id.tvOfficeLocation);
        work = (TextView) rootView.findViewById(R.id.tvWorkLocation);
        manager.setOnClickListener(this);
        work.setOnClickListener(this);

        setPrefillData();
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmitClicked) {
                    isSubmitClicked = false;
                    String empId = ModelManager.getInstance().getEmployeeDetailModel().getmEmpID();
                    EmployeeDetailModel model = ModelManager.getInstance().getEmployeeDetailModel();
                    if (TextUtils.isEmpty(managerID)) {
                        if (TextUtils.isEmpty(model.getmManagerID())) {
                            managerID = "0";
                        } else {
                            managerID = model.getmManagerID();
                        }
                    }
                    if (TextUtils.isEmpty(workLocationID)) {
                        if (TextUtils.isEmpty(model.getmOfficeIDWork())) {
                            workLocationID = "0";
                        } else {
                            workLocationID = model.getmOfficeIDWork();
                        }
                    }
                    MainActivity.isAnimationLoaded = false;
                    ((MainActivity)getActivity()).showHideProgress(true);
                    CommunicationManager.getInstance().sendPostRequest(
                            new IBaseResponse() {
                                @Override
                                public void validateResponse(ResponseData response) {

                                    JSONObject json;
                                    try {
                                        json = new JSONObject(response.getResponseData());
                                        JSONObject mainJSONObject = json
                                                .optJSONObject("UpdateEmployeeDetailsResult");
                                        if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                            String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                            new AlertCustomDialog(getActivity(), errorMessage);
                                            isSubmitClicked = true;
                                        } else {
                                            ((MainActivity) getActivity()).displayMessage("Details updated");
                                            navigateToTeamOrHome(mUserActionListener);
                                        }

                                    } catch (JSONException e) {
                                        Log.e(TAG, e.getMessage(), e);
                                        Crashlytics.logException(e);
                                    }
                                    MainActivity.isAnimationLoaded = true;
                                    ((MainActivity)getActivity()).showHideProgress(false);
                                }

                            },
                            AppRequestJSONString.getUpdateEmployeeDetail(empId, managerID, workLocationID),
                            CommunicationConstant.API_GET_UPDATE_EMPLOYEE_DETAILS, false);
                }
            }
        });


        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Edit Employee");
        int textColor = Utility.getTextColorCode(new Preferences(getContext()));
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTeamOrHome(mUserActionListener);
            }
        });


        return rootView;
    }


    private void setPrefillData() {
        EmployeeDetailModel model = ModelManager.getInstance().getEmployeeDetailModel();
        if (model != null) {
            manager.setText(model.getmMangerName());
            office.setText(model.getmOfficeLocation());

            LinearLayout empPersonalFieldLayout = (LinearLayout) rootView.findViewById(R.id.ll_empfield_container);
            LinearLayout empOfficialFieldLayout=(LinearLayout) rootView.findViewById(R.id.ll_official_container);
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
                View emailView = LayoutInflater.from(activity).inflate(R.layout.profile_item_view1, null, false);
                ((TextView) emailView.findViewById(R.id.tv_item_title)).setText("Manager");
                ((TextView) emailView.findViewById(R.id.tv_item_value1)).setText( model.getmMangerName());
                empOfficialFieldLayout.addView(emailView);
                managerTV=(TextView) emailView.findViewById(R.id.tv_item_value1);
                managerTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                        if (loginUserModel != null) {
                            UserModel userModel = loginUserModel.getUserModel();
                            if (userModel != null) {
                                String empId = userModel.getEmpId();
                                MainActivity.isAnimationLoaded = false;
                                ((MainActivity)getActivity()).showHideProgress(true);
                                CommunicationManager.getInstance().sendPostRequest(
                                        new IBaseResponse() {
                                            @Override
                                            public void validateResponse(ResponseData response) {
                                                JSONObject managerJsonList;
                                                try {
                                                    managerJsonList = new JSONObject(response.getResponseData());
                                                    JSONObject mainJSONObject = managerJsonList.optJSONObject("GetTeamMemberListResult");
                                                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                        new AlertCustomDialog(getActivity(), errorMessage);
                                                    } else {
                                                        JSONArray array = new JSONObject(managerJsonList.optJSONObject("GetTeamMemberListResult").toString()).optJSONArray("list");
                                                        TeamMember member = new TeamMember(array);
                                                        List<TeamMember> teamMemebers = member.getmTeamMemberList();
                                                        EmployeeDetailModel model = ModelManager.getInstance().getEmployeeDetailModel();
                                                        String employeeId = model.getmEmpID();
                                                        TeamMember teamMember = new TeamMember();
                                                        teamMember.setmEmpId(employeeId);
                                                        if (teamMemebers.contains(teamMember)) {
                                                            int index = teamMemebers.indexOf(teamMember);
                                                            teamMemebers.remove(index);
                                                        }
                                                        if (teamMemebers.size() > 0) {
                                                            CustomBuilder builder = new CustomBuilder(getContext(), "Select Manager", true);
                                                            builder.setSingleChoiceItems(teamMemebers, managerTV.getTag(), new CustomBuilder.OnClickListener() {
                                                                @Override
                                                                public void onClick(CustomBuilder builder, Object selectedObject) {
                                                                    managerTV.setTag(selectedObject);
                                                                    managerTV.setText(((TeamMember) selectedObject).getmName());
                                                                    managerID = ((TeamMember) selectedObject).getmEmpId();

                                                                    builder.dismiss();
                                                                }
                                                            });
                                                            builder.show();
                                                        } else {
                                                            ((MainActivity) getActivity()).displayMessage("No managers");
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    Crashlytics.logException(e);
                                                    Crashlytics.log(1, TAG, Utility.LogUserDetails());
                                                }
                                                MainActivity.isAnimationLoaded = true;
                                                ((MainActivity)getActivity()).showHideProgress(false);
                                            }
                                        }, AppRequestJSONString.getTeamList("9999", empId), CommunicationConstant.API_GET_TEAM_MEMBER_LIST, false);


                            }
                        }
                    }
                });
            }
            if(model.getmFnMangerNameYN().equalsIgnoreCase("Y")){
                Utility.addElementToView(activity, empOfficialFieldLayout, "Functional Manager", model.getmFunctionalManager());
            }
            if(model.getmOfficeLocation()!=null && !model.getmOfficeLocation().equalsIgnoreCase("")) {
                Utility.addElementToView(activity, empOfficialFieldLayout, "Office Location", model.getmOfficeLocation());
            }
            if (model.getmWorkLocationYN().equalsIgnoreCase("Y")) {
                View emailView = LayoutInflater.from(activity).inflate(R.layout.profile_item_view2, null, false);
                ((TextView) emailView.findViewById(R.id.tv_item_title)).setText("Work Location");
                ((TextView) emailView.findViewById(R.id.tv_item_value2)).setText( model.getmWorkLocation());

                 empOfficialFieldLayout.addView(emailView);
                workLocationTV=(TextView)emailView.findViewById(R.id.tv_item_value2);
                workLocationTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.isAnimationLoaded = false;
                        ((MainActivity)getActivity()).showHideProgress(true);
                        CommunicationManager.getInstance().sendPostRequest(
                                new IBaseResponse() {
                                    @Override
                                    public void validateResponse(ResponseData response) {
                                        JSONObject workLocationJsonList;
                                        try {
                                            workLocationJsonList = new JSONObject(response.getResponseData());
                                            JSONObject mainJSONObject = workLocationJsonList.optJSONObject("GetTypeWiseListResult");
                                            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                String errorMesaage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                new AlertCustomDialog(getActivity(), errorMesaage);
                                            } else {
                                                JSONArray array = mainJSONObject.optJSONArray("list");
                                                TypeWiseListModel model = new TypeWiseListModel(array);
                                                CustomBuilder builder = new CustomBuilder(getContext(), "Select Location", true);
                                                builder.setSingleChoiceItems(model.getList(), workLocationTV.getTag(), new CustomBuilder.OnClickListener() {
                                                    @Override
                                                    public void onClick(CustomBuilder builder, Object selectedObject) {
                                                        workLocationTV.setTag(selectedObject);
                                                        workLocationTV.setText(((TypeWiseListModel) selectedObject).getValue());
                                                        workLocationID = ((TypeWiseListModel) selectedObject).getCode();
                                                        builder.dismiss();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        } catch (JSONException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                        }
                                        MainActivity.isAnimationLoaded = true;
                                        ((MainActivity)getActivity()).showHideProgress(false);
                                    }
                                }, AppRequestJSONString.getCommonService("ddLocationList", ""), CommunicationConstant.API_GET_TYPE_WISE_LIST, false);
                    }
                });
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


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tvManager:

                LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                if (loginUserModel != null) {
                    UserModel userModel = loginUserModel.getUserModel();
                    if (userModel != null) {
                        String empId = userModel.getEmpId();
                        MainActivity.isAnimationLoaded = false;
                        ((MainActivity)getActivity()).showHideProgress(true);
                        CommunicationManager.getInstance().sendPostRequest(
                                new IBaseResponse() {
                                    @Override
                                    public void validateResponse(ResponseData response) {
                                        JSONObject managerJsonList;
                                        try {
                                            managerJsonList = new JSONObject(response.getResponseData());
                                            JSONObject mainJSONObject = managerJsonList.optJSONObject("GetTeamMemberListResult");
                                            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                new AlertCustomDialog(getActivity(), errorMessage);
                                            } else {
                                                JSONArray array = new JSONObject(managerJsonList.optJSONObject("GetTeamMemberListResult").toString()).optJSONArray("list");
                                                TeamMember member = new TeamMember(array);
                                                List<TeamMember> teamMemebers = member.getmTeamMemberList();
                                                EmployeeDetailModel model = ModelManager.getInstance().getEmployeeDetailModel();
                                                String employeeId = model.getmEmpID();
                                                TeamMember teamMember = new TeamMember();
                                                teamMember.setmEmpId(employeeId);
                                                if (teamMemebers.contains(teamMember)) {
                                                    int index = teamMemebers.indexOf(teamMember);
                                                    teamMemebers.remove(index);
                                                }
                                                if (teamMemebers.size() > 0) {
                                                    CustomBuilder builder = new CustomBuilder(getContext(), "Select Manager", true);
                                                    builder.setSingleChoiceItems(teamMemebers, work.getTag(), new CustomBuilder.OnClickListener() {
                                                        @Override
                                                        public void onClick(CustomBuilder builder, Object selectedObject) {
                                                            manager.setTag(selectedObject);
                                                            manager.setText(((TeamMember) selectedObject).getmName());
                                                            managerID = ((TeamMember) selectedObject).getmEmpId();
                                                            builder.dismiss();
                                                        }
                                                    });
                                                    builder.show();
                                                } else {
                                                    ((MainActivity) getActivity()).displayMessage("No managers");
                                                }
                                            }

                                        } catch (JSONException e) {
                                            Crashlytics.logException(e);
                                            Crashlytics.log(1, TAG, Utility.LogUserDetails());
                                        }
                                        MainActivity.isAnimationLoaded = true;
                                        ((MainActivity)getActivity()).showHideProgress(false);
                                    }
                                }, AppRequestJSONString.getTeamList("9999", empId), CommunicationConstant.API_GET_TEAM_MEMBER_LIST, false);


                    }
                }
                break;
            case R.id.tvWorkLocation:
                MainActivity.isAnimationLoaded = false;
                ((MainActivity)getActivity()).showHideProgress(true);
                CommunicationManager.getInstance().sendPostRequest(
                        new IBaseResponse() {
                            @Override
                            public void validateResponse(ResponseData response) {
                                JSONObject workLocationJsonList;
                                try {
                                    workLocationJsonList = new JSONObject(response.getResponseData());
                                    JSONObject mainJSONObject = workLocationJsonList.optJSONObject("GetTypeWiseListResult");
                                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                        String errorMesaage = mainJSONObject.optString("ErrorMessage", "Failed");
                                        new AlertCustomDialog(getActivity(), errorMesaage);
                                    } else {
                                        JSONArray array = mainJSONObject.optJSONArray("list");
                                        TypeWiseListModel model = new TypeWiseListModel(array);
                                        CustomBuilder builder = new CustomBuilder(getContext(), "Select Location", true);
                                        builder.setSingleChoiceItems(model.getList(), work.getTag(), new CustomBuilder.OnClickListener() {
                                            @Override
                                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                                work.setTag(selectedObject);
                                                work.setText(((TypeWiseListModel) selectedObject).getValue());
                                                workLocationID = ((TypeWiseListModel) selectedObject).getCode();
                                                builder.dismiss();
                                            }
                                        });
                                        builder.show();
                                    }
                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                MainActivity.isAnimationLoaded = true;
                                ((MainActivity)getActivity()).showHideProgress(false);
                            }
                        }, AppRequestJSONString.getCommonService("ddLocationList", ""), CommunicationConstant.API_GET_TYPE_WISE_LIST, false);
                break;
        }

    }


}
