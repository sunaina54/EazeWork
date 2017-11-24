package hr.eazework.com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manjunath on 21-03-2017.
 */

public class TeamMember {
    private String mDayStatus;
    private String mDesignation;
    private String mEmpId;
    private String mImageUrl;
    private String mManagerId;
    private String mManagerName;
    private String mName;
    private String mTeamCount;
    public static int loopCount = 0;
    public static String mCurrentEmpId;
    public static String mPreviousEmpId;
    private ArrayList<TeamMember> mTeamMemberList = new ArrayList<>();


    public ArrayList<TeamMember> getmTeamMemberList() {
        return mTeamMemberList;
    }

    public void setmTeamMemberList(ArrayList<TeamMember> mTeamMemberList) {
        this.mTeamMemberList = mTeamMemberList;
    }

    public TeamMember() {}

    public TeamMember(JSONArray array) {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    mTeamMemberList.add(new TeamMember(array.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public TeamMember(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            mDayStatus = object.optString("DayStatus", "");
            mDesignation = object.optString("Designation", "");
            mEmpId = object.optString("EmpID", "");
            mImageUrl = object.optString("EmpImageUrl", "");
            mManagerId = object.optString("ManagerID", "");
            mManagerName = object.optString("ManagerName", "");
            mName = object.optString("Name", "");
            mTeamCount = object.optString("TeamCount", "");
        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

    public static String getmPreviousEmpId() {
        return mPreviousEmpId;
    }

    public static void setmPreviousEmpId(String mPreviousEmpId) {
        TeamMember.mPreviousEmpId = mPreviousEmpId;
    }

    public static int getLoopCount() {
        return loopCount;
    }

    public static void setLoopCount(int loopCount) {
        TeamMember.loopCount = loopCount;
    }

    public String getmDayStatus() {
        return mDayStatus;
    }

    public void setmDayStatus(String mDayStatus) {
        this.mDayStatus = mDayStatus;
    }

    public String getmDesignation() {
        return mDesignation;
    }

    public void setmDesignation(String mDesignation) {
        this.mDesignation = mDesignation;
    }

    public String getmEmpId() {
        return mEmpId;
    }

    public void setmEmpId(String mEmpId) {
        this.mEmpId = mEmpId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmManagerId() {
        return mManagerId;
    }

    public void setmManagerId(String mManagerId) {
        this.mManagerId = mManagerId;
    }

    public String getmManagerName() {
        return mManagerName;
    }

    public void setmManagerName(String mManagerName) {
        this.mManagerName = mManagerName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTeamCount() {
        return mTeamCount;
    }

    public void setmTeamCount(String mTeamCount) {
        this.mTeamCount = mTeamCount;
    }

    public static String getmCurrentEmpId() {
        return mCurrentEmpId;
    }

    public static void setmCurrentEmpId(String mCurrentEmpId) {
        TeamMember.mCurrentEmpId = mCurrentEmpId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(obj instanceof TeamMember){
            TeamMember member = (TeamMember) obj;
            if((member.getmEmpId() == null && mEmpId == null) || (member.getmEmpId().equalsIgnoreCase(mEmpId))) {
                return true;
            }
        }
        return false;
    }
}
