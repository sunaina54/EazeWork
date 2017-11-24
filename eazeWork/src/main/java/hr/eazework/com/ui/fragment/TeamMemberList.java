package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.TeamMemberListViewAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 21-03-2017.
 */

public class TeamMemberList extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "TeamMemberList";

    private TeamMemberListViewAdapter adapter;
    private String empId = "";
    private ListView listView;
    private ArrayList<TeamMember> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.team_member_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_team_members);
        adapter = new TeamMemberListViewAdapter(getContext(), R.layout.team_list_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
   //     TeamMember.setIsInLoop(false);
        requestAPI();
        return rootView;
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        empId = TeamMember.getmCurrentEmpId();
        String jsonPostParam = AppRequestJSONString.getTeamList("1", empId);
        CommunicationManager.getInstance().sendPostRequest(this, jsonPostParam, CommunicationConstant.API_GET_TEAM_MEMBER_LIST, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TeamMember m = list.get(position);
        final String employeeId = m.getmEmpId();
        int teamCount = 0;
        try {
            teamCount = Integer.parseInt(m.getmTeamCount());
        } catch (NumberFormatException e) {
            Log.e(TAG,e.getMessage(),e);
            Crashlytics.logException(e);
        }

        if (teamCount > 0) {
            CustomBuilder builder = new CustomBuilder(getContext(), "Select Action", true);
            builder.setSingleChoiceItems(getTeamItems(), null, new CustomBuilder.OnClickListener() {
                @Override
                public void onClick(CustomBuilder builder, Object selectedObject) {
                    if (selectedObject.toString().equalsIgnoreCase(getString(R.string.msg_view_profile))) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.EMP_ID, employeeId);
                        mUserActionListener.performUserAction(IAction.TEAM_MEMBER_PROFILE, null, b);
                    } else if (selectedObject.toString().equalsIgnoreCase(getString(R.string.msg_attendance_detail))) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.EMP_ID, employeeId);
                        mUserActionListener.performUserAction(IAction.TEAM_MEMBER_HISTORY, null, b);
                    } else if (selectedObject.toString().equalsIgnoreCase(getString(R.string.msg_team_members))) {
                        Bundle b = new Bundle();
                        TeamMember.setmPreviousEmpId(empId);
                        TeamMember.setmCurrentEmpId(employeeId);
                        TeamMember.loopCount++ ;
                        b.putString(AppsConstant.OPTION_SELECTED, "subTeamMembers");
                        mUserActionListener.performUserAction(IAction.TEAM_MEMBER_LIST, null, b);
                    }
                    builder.dismiss();
                }
            });
            builder.show();

        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(getString(R.string.msg_view_profile));
            MenuItemModel itemModel = ModelManager.getInstance().getMenuItemModel().getItemModel(MenuItemModel.ATTANDANCE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                arrayList.add(getString(R.string.msg_attendance_detail));
            }
            CustomBuilder actionDialog = new CustomBuilder(getContext(), "Select Action", true);
            actionDialog.setSingleChoiceItems(arrayList, null, new CustomBuilder.OnClickListener() {
                @Override
                public void onClick(CustomBuilder builder, Object selectedObject) {
                    if (selectedObject.toString().equalsIgnoreCase(getString(R.string.msg_view_profile))) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.EMP_ID, employeeId);
                        mUserActionListener.performUserAction(IAction.TEAM_MEMBER_PROFILE, null, b);
                    } else if (selectedObject.toString().equalsIgnoreCase(getString(R.string.msg_attendance_detail))) {
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.EMP_ID, employeeId);
                        mUserActionListener.performUserAction(IAction.TEAM_MEMBER_HISTORY, null, b);
                    }
                    builder.dismiss();
                }
            });
            actionDialog.show();

        }

    }

    @Override
    public void validateResponse(ResponseData response) {
        super.validateResponse(response);
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_TEAM_MEMBER_LIST:
                JSONObject getTeamList;
                try {
                    getTeamList = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = getTeamList.optJSONObject("GetTeamMemberListResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        String getTeamMemberListResult = getTeamList.optJSONObject("GetTeamMemberListResult").toString();
                        JSONArray array = new JSONObject(getTeamMemberListResult).optJSONArray("list");
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

    private void populateViews(JSONArray array) {
        TeamMember model = new TeamMember(array);
        this.list = model.getmTeamMemberList();
        if(adapter != null) {
            adapter.refresh(list);
        }
    }

    public ArrayList<String> getTeamItems() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.msg_view_profile));
        list.add(getString(R.string.msg_team_members));
        MenuItemModel itemModel = ModelManager.getInstance().getMenuItemModel().getItemModel(MenuItemModel.ATTANDANCE_KEY);
        if (itemModel != null && itemModel.isAccess()) {
            list.add(getString(R.string.msg_attendance_detail));
        }
        return list;
    }
}
