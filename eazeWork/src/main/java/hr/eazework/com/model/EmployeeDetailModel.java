package hr.eazework.com.model;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Manjunath on 01-04-2017.
 */

public class EmployeeDetailModel {

    private String mActive;
    private String mActiveDesc;
    private String mCompanyID;
    private String mCompanyName;
    private String mCompanyNameYN;
    private String mDateOfBirth;
    private String mDateOfJoining;
    private String mDeptID;
    private String  mDeptName;
    private String  mDesignation;
    private String  mDivID;
    private String  mDivName;
    private String  mDivNameYN;
    private String  mEmail;
    private String  mEmpCode;
    private String  mEmpID;
    private String  mEmpImageUrl;
    private String  mEmpType;
    private String  mEmpTypeDesc;
    private String  mEmploymentStatus;
    private String  mEmploymentStatusDesc;
    private String  mFatherName;
    private String  mFirstName;
    private String  mLastName;
    private String  mLevelDesc;
    private String  mLevelID;
    private String  mManagerID;
    private String  mMangerName;
    private String  mMaritalStatus;
    private String  mMaritalStatusDesc;
    private String  mMiddleName;
    private String  mMobile;
    private String  mOfficeID;
    private String  mOfficeIDWork;
    private String  mOfficeLocation;
    private String  mSexCode;
    private String  mSourceID;
    private String  mSubDepartmentID;
    private String  mSubDepartment;
    private String  mSubDepartmentYN;
    private String  mSubDivisionNameID;
    private String  mSubDivisionName;
    private String  mSubDivisionNameYN;
    private String  mTitle;
    private String  mTitleID;
    private String  mWorkLocation;
    private String  mWorkLocationYN;
    private String mFunctionalManager;
    private String mDivisionYN;
    private String mDepartmentYN;
    private String mSubDivisionYN;
    private String mMaritalStatusYN;
    private String mDesignationYN;
    private String mDeptNameYN;
    private String mFnMangerNameYN;

    public String getmFnMangerNameYN() {
        return mFnMangerNameYN;
    }

    public void setmFnMangerNameYN(String mFnMangerNameYN) {
        this.mFnMangerNameYN = mFnMangerNameYN;
    }


    public EmployeeDetailModel(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            mActive = object.optString("Active","");
            mActiveDesc = object.optString("ActiveDesc","");
            mCompanyID = object.optString("CompanyID","");
            mCompanyName = object.optString("CompanyName","");
            mCompanyNameYN = object.optString("CompanyNameYN","N");
            mDateOfBirth = object.optString("DateOfBirth","");
            mDateOfJoining = object.optString("DateOfJoining","");
            mDeptID = object.optString("DeptID","");
            mDeptName = object.optString("DeptName","");
            mDesignation = object.optString("Designation","");
            mDivID = object.optString("DivID","");
            mDivName = object.optString("DivName","");
            mDivNameYN = object.optString("DivNameYN","N");
            mEmail = object.optString("Email","");
            mEmpCode = object.optString("EmpCode","");
            mEmpID = object.optString("EmpID","");
            mEmpImageUrl = object.optString("EmpImageUrl","");
            mEmpType = object.optString("EmpType","");
            mEmpTypeDesc = object.optString("EmpTypeDesc","");
            mEmploymentStatus = object.optString("EmploymentStatus","");
            mEmploymentStatusDesc = object.optString("EmploymentStatusDesc","");
            mFatherName = object.optString("FatherName","");
            mFirstName = object.optString("FirstName","");
            mLastName = object.optString("LastName","");
            mLevelDesc = object.optString("LevelDesc","");
            mLevelID = object.optString("LevelID","");
            mManagerID = object.optString("ManagerID","");
            mMangerName = object.optString("MangerName","");
          //  mFunctionalManager=object.optString("FnManagerName","");
            mFunctionalManager=object.optString("FnMangerName","");

            mMaritalStatus = object.optString("MaritalStatus","");
            mMaritalStatusDesc = object.optString("MaritalStatusDesc","");
            mMiddleName = object.optString("MiddleName","");
            mMobile = object.optString("Mobile","");
            mOfficeID = object.optString("OfficeID","");
            mOfficeIDWork = object.optString("OfficeIDWork","");
            mOfficeLocation = object.optString("OfficeLocation","");
            mSexCode = object.optString("SexCode","");
            mSourceID = object.optString("SourceID","");
            mSubDepartmentID = object.optString("SubDeptID","");
            mSubDepartment = object.optString("SubDeptName","");
            mSubDepartmentYN = object.optString("SubDeptNameYN","N");
            mSubDivisionNameID = object.optString("SubDivID","");
            mSubDivisionName = object.optString("SubDivName","");
            mSubDivisionNameYN = object.optString("SubDivNameYN","N");

            mDivisionYN= object.optString("DivisionYN","N");
            mDepartmentYN=object.optString("DepartmentYN","N");
            mSubDivisionYN=object.optString("SubDivisionYN","N");
            mMaritalStatusYN=object.optString("MaritalStatusYN","N");
            mDesignationYN=object.optString("DesignationYN","N");

            mTitle = object.optString("Title","");
            mTitleID = object.optString("TitleID","");
            mWorkLocation = object.optString("WorkLocation","");
            mWorkLocationYN = object.optString("WorkLocationYN","N");

            mDeptNameYN = object.optString("DeptNameYN","N");
            mFnMangerNameYN= object.optString("FnMangerNameYN","N");

        } catch (JSONException e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }


    public String getmDivisionYN() {
        return mDivisionYN;
    }

    public void setmDivisionYN(String mDivisionYN) {
        this.mDivisionYN = mDivisionYN;
    }

    public String getmDepartmentYN() {
        return mDepartmentYN;
    }

    public void setmDepartmentYN(String mDepartmentYN) {
        this.mDepartmentYN = mDepartmentYN;
    }

    public String getmSubDivisionYN() {
        return mSubDivisionYN;
    }

    public void setmSubDivisionYN(String mSubDivisionYN) {
        this.mSubDivisionYN = mSubDivisionYN;
    }

    public String getmMaritalStatusYN() {
        return mMaritalStatusYN;
    }

    public void setmMaritalStatusYN(String mMaritalStatusYN) {
        this.mMaritalStatusYN = mMaritalStatusYN;
    }

    public String getmDesignationYN() {
        return mDesignationYN;
    }

    public void setmDesignationYN(String mDesignationYN) {
        this.mDesignationYN = mDesignationYN;
    }

    public String getmFunctionalManager() {
        return mFunctionalManager;
    }

    public void setmFunctionalManager(String mFunctionalManager) {
        this.mFunctionalManager = mFunctionalManager;
    }

    public String getmActive() {
        return mActive;
    }

    public void setmActive(String mActive) {
        this.mActive = mActive;
    }

    public String getmActiveDesc() {
        return mActiveDesc;
    }

    public void setmActiveDesc(String mActiveDesc) {
        this.mActiveDesc = mActiveDesc;
    }

    public String getmCompanyID() {
        return mCompanyID;
    }

    public void setmCompanyID(String mCompanyID) {
        this.mCompanyID = mCompanyID;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmCompanyNameYN() {
        return mCompanyNameYN;
    }

    public void setmCompanyNameYN(String mCompanyNameYN) {
        this.mCompanyNameYN = mCompanyNameYN;
    }

    public String getmDateOfBirth() {
        return mDateOfBirth;
    }

    public void setmDateOfBirth(String mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public String getmDateOfJoining() {
        return mDateOfJoining;
    }

    public void setmDateOfJoining(String mDateOfJoining) {
        this.mDateOfJoining = mDateOfJoining;
    }

    public String getmDeptID() {
        return mDeptID;
    }

    public void setmDeptID(String mDeptID) {
        this.mDeptID = mDeptID;
    }

    public String getmDeptName() {
        return mDeptName;
    }

    public void setmDeptName(String mDeptName) {
        this.mDeptName = mDeptName;
    }

    public String getmDesignation() {
        return mDesignation;
    }

    public void setmDesignation(String mDesignation) {
        this.mDesignation = mDesignation;
    }

    public String getmDivID() {
        return mDivID;
    }

    public void setmDivID(String mDivID) {
        this.mDivID = mDivID;
    }

    public String getmDivName() {
        return mDivName;
    }

    public void setmDivName(String mDivName) {
        this.mDivName = mDivName;
    }

    public String getmDivNameYN() {
        return mDivNameYN;
    }

    public void setmDivNameYN(String mDivNameYN) {
        this.mDivNameYN = mDivNameYN;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmEmpCode() {
        return mEmpCode;
    }

    public void setmEmpCode(String mEmpCode) {
        this.mEmpCode = mEmpCode;
    }

    public String getmEmpID() {
        return mEmpID;
    }

    public void setmEmpID(String mEmpID) {
        this.mEmpID = mEmpID;
    }

    public String getmEmpImageUrl() {
        return mEmpImageUrl;
    }

    public void setmEmpImageUrl(String mEmpImageUrl) {
        this.mEmpImageUrl = mEmpImageUrl;
    }

    public String getmEmpType() {
        return mEmpType;
    }

    public void setmEmpType(String mEmpType) {
        this.mEmpType = mEmpType;
    }

    public String getmEmpTypeDesc() {
        return mEmpTypeDesc;
    }

    public void setmEmpTypeDesc(String mEmpTypeDesc) {
        this.mEmpTypeDesc = mEmpTypeDesc;
    }

    public String getmEmploymentStatus() {
        return mEmploymentStatus;
    }

    public void setmEmploymentStatus(String mEmploymentStatus) {
        this.mEmploymentStatus = mEmploymentStatus;
    }

    public String getmEmploymentStatusDesc() {
        return mEmploymentStatusDesc;
    }

    public void setmEmploymentStatusDesc(String mEmploymentStatusDesc) {
        this.mEmploymentStatusDesc = mEmploymentStatusDesc;
    }

    public String getmDeptNameYN() {
        return mDeptNameYN;
    }

    public void setmDeptNameYN(String mDeptNameYN) {
        this.mDeptNameYN = mDeptNameYN;
    }

    public String getmFatherName() {
        return mFatherName;
    }

    public void setmFatherName(String mFatherName) {
        this.mFatherName = mFatherName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmLevelDesc() {
        return mLevelDesc;
    }

    public void setmLevelDesc(String mLevelDesc) {
        this.mLevelDesc = mLevelDesc;
    }

    public String getmLevelID() {
        return mLevelID;
    }

    public void setmLevelID(String mLevelID) {
        this.mLevelID = mLevelID;
    }

    public String getmManagerID() {
        return mManagerID;
    }

    public void setmManagerID(String mManagerID) {
        this.mManagerID = mManagerID;
    }

    public String getmMangerName() {
        return mMangerName;
    }

    public void setmMangerName(String mMangerName) {
        this.mMangerName = mMangerName;
    }

    public String getmMaritalStatus() {
        return mMaritalStatus;
    }

    public void setmMaritalStatus(String mMaritalStatus) {
        this.mMaritalStatus = mMaritalStatus;
    }

    public String getmMaritalStatusDesc() {
        return mMaritalStatusDesc;
    }

    public void setmMaritalStatusDesc(String mMaritalStatusDesc) {
        this.mMaritalStatusDesc = mMaritalStatusDesc;
    }

    public String getmMiddleName() {
        return mMiddleName;
    }

    public void setmMiddleName(String mMiddleName) {
        this.mMiddleName = mMiddleName;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmOfficeID() {
        return mOfficeID;
    }

    public void setmOfficeID(String mOfficeID) {
        this.mOfficeID = mOfficeID;
    }

    public String getmOfficeIDWork() {
        return mOfficeIDWork;
    }

    public void setmOfficeIDWork(String mOfficeIDWork) {
        this.mOfficeIDWork = mOfficeIDWork;
    }

    public String getmOfficeLocation() {
        return mOfficeLocation;
    }

    public void setmOfficeLocation(String mOfficeLocation) {
        this.mOfficeLocation = mOfficeLocation;
    }

    public String getmSexCode() {
        return mSexCode;
    }

    public void setmSexCode(String mSexCode) {
        this.mSexCode = mSexCode;
    }

    public String getmSourceID() {
        return mSourceID;
    }

    public void setmSourceID(String mSourceID) {
        this.mSourceID = mSourceID;
    }

    public String getmSubDepartmentID() {
        return mSubDepartmentID;
    }

    public void setmSubDepartmentID(String mSubDepartmentID) {
        this.mSubDepartmentID = mSubDepartmentID;
    }

    public String getmSubDepartment() {
        return mSubDepartment;
    }

    public void setmSubDepartment(String mSubDepartment) {
        this.mSubDepartment = mSubDepartment;
    }

    public String getmSubDepartmentYN() {
        return mSubDepartmentYN;
    }

    public void setmSubDepartmentYN(String mSubDepartmentYN) {
        this.mSubDepartmentYN = mSubDepartmentYN;
    }

    public String getmSubDivisionNameID() {
        return mSubDivisionNameID;
    }

    public void setmSubDivisionNameID(String mSubDivisionNameID) {
        this.mSubDivisionNameID = mSubDivisionNameID;
    }

    public String getmSubDivisionName() {
        return mSubDivisionName;
    }

    public void setmSubDivisionName(String mSubDivisionName) {
        this.mSubDivisionName = mSubDivisionName;
    }

    public String getmSubDivisionNameYN() {
        return mSubDivisionNameYN;
    }

    public void setmSubDivisionNameYN(String mSubDivisionNameYN) {
        this.mSubDivisionNameYN = mSubDivisionNameYN;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTitleID() {
        return mTitleID;
    }

    public void setmTitleID(String mTitleID) {
        this.mTitleID = mTitleID;
    }

    public String getmWorkLocation() {
        return mWorkLocation;
    }

    public void setmWorkLocation(String mWorkLocation) {
        this.mWorkLocation = mWorkLocation;
    }

    public String getmWorkLocationYN() {
        return mWorkLocationYN;
    }

    public void setmWorkLocationYN(String mWorkLocationYN) {
        this.mWorkLocationYN = mWorkLocationYN;
    }

    public Map<String,String> getEmployeeDetailMapForEdit(){
        Map<String,String> employeeDetails = new LinkedHashMap<>();
        employeeDetails.put("Email",this.getmEmail());
        employeeDetails.put("Designation",this.getmDesignation());
        employeeDetails.put("Department",this.getmDeptName());
        employeeDetails.put("Date Of Birth",this.getmDateOfBirth());
        employeeDetails.put("Date Of Joining",this.getmDateOfJoining());
        employeeDetails.put("Marital Status",this.getmMaritalStatusDesc());
        employeeDetails.put("Company Name",this.getmCompanyName());
        employeeDetails.put("Div Name",this.getmDivName());
        employeeDetails.put("Sub Department",this.getmSubDepartment());
        employeeDetails.put("Sub Division Name",this.getmSubDivisionName());

        return  employeeDetails;
    }

}
