package hr.eazework.com.model;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeProfileModel extends BaseAppResponseModel {
    private String mName;
    private String mCompanyLogo;
    private String mImageUrl;
    private String mManagerImgUrl;
    private String mDepartment;
    private String mRole;
    private String mEmpCode;
    private String mManagerName;
    private String mTeamSize;
    private String mWorkLocationYN;
    private String mOfficeLocation;
    private String mOfficeCodeSM;
    private String mHeaderBGColor;
    private String mHeaderTextColor;

    private String mGeofencingYN;
    private String mLatitude;
    private String mLongitude;
    private String mMarkAttendanceYN;
    private String mRadius;
    private String mRefreshYN;
    private String mSelfieYN;
    private String mWorkLocation;
    private String mProfileMsgYN;
    private String mProfileMsg;

    private String mDepartmentYN;
    private String mDesignationYN;



    public String getmProfileMsgYN() {
        return mProfileMsgYN;
    }

    public void setmProfileMsgYN(String mProfileMsgYN) {
        this.mProfileMsgYN = mProfileMsgYN;
    }

    public String getmProfileMsg() {
        return mProfileMsg;
    }

    public void setmProfileMsg(String mProfileMsg) {
        this.mProfileMsg = mProfileMsg;
    }

    public String getmHeaderBGColor() {
        return mHeaderBGColor;
    }

    public void setmHeaderBGColor(String mHeaderBGColor) {
        this.mHeaderBGColor = mHeaderBGColor;
    }

    public String getmHeaderTextColor() {
        return mHeaderTextColor;
    }

    public void setmHeaderTextColor(String mHeaderTextColor) {
        this.mHeaderTextColor = mHeaderTextColor;
    }

    public String getmOfficeCodeSM() {
        return mOfficeCodeSM;
    }

    public void setmOfficeCodeSM(String mOfficeCodeSM) {
        this.mOfficeCodeSM = mOfficeCodeSM;
    }

    public String getmWorkLocationYN() {
        return mWorkLocationYN;
    }

    public void setmWorkLocationYN(String mWorkLocationYN) {
        this.mWorkLocationYN = mWorkLocationYN;
    }

    public String getmOfficeLocation() {
        return mOfficeLocation;
    }

    public void setmOfficeLocation(String mOfficeLocation) {
        this.mOfficeLocation = mOfficeLocation;
    }

    public String getmGeofencingYN() {
        return mGeofencingYN;
    }

    public void setmGeofencingYN(String mGeofencingYN) {
        this.mGeofencingYN = mGeofencingYN;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmMarkAttendanceYN() {
        return mMarkAttendanceYN;
    }

    public void setmMarkAttendanceYN(String mMarkAttendanceYN) {
        this.mMarkAttendanceYN = mMarkAttendanceYN;
    }

    public String getmRadius() {
        return mRadius;
    }

    public void setmRadius(String mRadius) {
        this.mRadius = mRadius;
    }

    public String getmRefreshYN() {
        return mRefreshYN;
    }

    public void setmRefreshYN(String mRefreshYN) {
        this.mRefreshYN = mRefreshYN;
    }

    public String getmSelfieYN() {
        return mSelfieYN;
    }

    public void setmSelfieYN(String mSelfieYN) {
        this.mSelfieYN = mSelfieYN;
    }

    public EmployeeProfileModel(){}

    public EmployeeProfileModel(String jsonString) {
        try {
            JSONObject jsonObject = (new JSONObject(jsonString)).optJSONObject("EmpProfileResult");
            if (jsonObject != null) {
                mName = jsonObject.optString("Name", "");
                mCompanyLogo = jsonObject.optString("CompanyImageUrl", "");
                mImageUrl = jsonObject.optString("EmpImageUrl", "");
                mManagerImgUrl = jsonObject.optString("ManagerImageUrl", "");
                mManagerName = jsonObject.optString("ManagerName", "");
                mDepartment = jsonObject.optString("Department", "");
                mRole = jsonObject.optString("Role", "");
                mEmpCode = jsonObject.optString("EmpCode", "");
                mOfficeCodeSM = jsonObject.optString("OfficeCodeSM", "M");
                mTeamSize = jsonObject.optString("TeamSize", "");
                mOfficeLocation = jsonObject.optString("OfficeLocation", "");
                mWorkLocation = jsonObject.optString("WorkLocation", "");
                mHeaderBGColor = jsonObject.optString("HeaderBGColor", "#d9020d");
                mHeaderTextColor = jsonObject.optString("HeaderTextColor", "#ffffff");
                if (jsonObject.has("EmpConfig")) {
                    JSONObject object = jsonObject.optJSONObject("EmpConfig");
                    mGeofencingYN = object.optString("GeoFencingYN", "N");
                    mLatitude = object.optString("Latitude", "");
                    mLongitude = object.optString("Longitude", "");
                    mMarkAttendanceYN = object.optString("MarkAttendanceYN", "N");
                    mRadius = object.optString("Radius", "0");
                    mRefreshYN = object.optString("RefreshYN", "N");
                    mSelfieYN = object.optString("SelfieYN", "N");
                    mWorkLocationYN = object.optString("WorkLocationYN", "N");
                    mProfileMsgYN = object.optString("ProfileMsgYN","N");
                    mProfileMsg = object.optString("ProfileMsg","");
                    mDepartmentYN=object.optString("DepartmentYN","N");
                    mDesignationYN=object.optString("DesignationYN","N");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public EmployeeProfileModel(JSONObject jsonObj) {
        try {
            JSONObject jsonObject = jsonObj;
            if (jsonObject != null) {
                mName = jsonObject.optString("Name", "");
                mCompanyLogo = jsonObject.optString("CompanyImageUrl", "");
                mImageUrl = jsonObject.optString("EmpImageUrl", "");
                mManagerImgUrl = jsonObject.optString("ManagerImageUrl", "");
                mManagerName = jsonObject.optString("ManagerName", "");
                mDepartment = jsonObject.optString("Department", "");
                mRole = jsonObject.optString("Role", "");
                mEmpCode = jsonObject.optString("EmpCode", "");
                mOfficeCodeSM = jsonObject.optString("OfficeCodeSM", "M");
                mTeamSize = jsonObject.optString("TeamSize", "");
                mOfficeLocation = jsonObject.optString("OfficeLocation", "");
                mWorkLocation = jsonObject.optString("WorkLocation", "");
                mHeaderBGColor = jsonObject.optString("HeaderBGColor", "#d9020d");
                mHeaderTextColor = jsonObject.optString("HeaderTextColor", "#ffffff");
                if (jsonObject.has("EmpConfig")) {
                    JSONObject object = jsonObject.optJSONObject("EmpConfig");
                    mGeofencingYN = object.optString("GeoFencingYN", "N");
                    mLatitude = object.optString("Latitude", "");
                    mLongitude = object.optString("Longitude", "");
                    mMarkAttendanceYN = object.optString("MarkAttendanceYN", "N");
                    mRadius = object.optString("Radius", "0");
                    mRefreshYN = object.optString("RefreshYN", "N");
                    mSelfieYN = object.optString("SelfieYN", "N");
                    mWorkLocationYN = object.optString("WorkLocationYN", "N");
                    mProfileMsgYN = object.optString("ProfileMsgYN","N");
                    mProfileMsg = object.optString("ProfileMsg","");
                    mDepartmentYN=object.optString("DepartmentYN","N");
                    mDesignationYN=object.optString("DesignationYN","N");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public String getmDepartmentYN() {
        return mDepartmentYN;
    }

    public void setmDepartmentYN(String mDepartmentYN) {
        this.mDepartmentYN = mDepartmentYN;
    }

    public String getmDesignationYN() {
        return mDesignationYN;
    }

    public void setmDesignationYN(String mDesignationYN) {
        this.mDesignationYN = mDesignationYN;
    }

    public String getmWorkLocation() {
        return mWorkLocation;
    }

    public void setmWorkLocation(String mWorkLocation) {
        this.mWorkLocation = mWorkLocation;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmCompanyLogo() {
        return mCompanyLogo;
    }

    public void setmCompanyLogo(String mCompanyLogo) {
        this.mCompanyLogo = mCompanyLogo;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmManagerImgUrl() {
        return mManagerImgUrl;
    }

    public void setmManagerImgUrl(String mManagerImgUrl) {
        this.mManagerImgUrl = mManagerImgUrl;
    }

    public String getmDepartment() {
        return mDepartment;
    }

    public void setmDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }

    public String getmRole() {
        return mRole;
    }

    public void setmRole(String mRole) {
        this.mRole = mRole;
    }

    public String getmEmpCode() {
        return mEmpCode;
    }

    public void setmEmpCode(String mEmpCode) {
        this.mEmpCode = mEmpCode;
    }

    public String getmManagerName() {
        return mManagerName;
    }

    public void setmManagerName(String mManagerName) {
        this.mManagerName = mManagerName;
    }

    public String getmTeamSize() {
        return mTeamSize;
    }

    public int getTeamSizeInt() {
        int count = 0;
        try {
            count = Integer.parseInt(mTeamSize);
        } catch (NumberFormatException e) {
            Crashlytics.logException(e);
        }
        return count;
    }

    public void setmTeamSize(String mTeamSize) {
        this.mTeamSize = mTeamSize;
    }

}
