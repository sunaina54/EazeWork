package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeLeaveModel {
	private String mApprovalLevel;
	private String mDetails;
	private String mForEmpID;
	private String mForEmpName;
	private String mRemark;
	private String mReqCode;
	private String mReqID;
	private String mReqType;
	private String mReqTypeDesc;
	private String mStatus;
	private ArrayList<EmployeeLeaveModel> mPendingLeaveList;
	public EmployeeLeaveModel(JSONArray jsonArray) {
		mPendingLeaveList=new ArrayList<>();
		if(jsonArray!=null){
			for (int i = 0; i < jsonArray.length(); i++) {
				mPendingLeaveList.add(new EmployeeLeaveModel(jsonArray.optJSONObject(i)));
			}
		}
	}

	public EmployeeLeaveModel(JSONObject jsonObject) {
		mApprovalLevel=jsonObject.optString("ApprovalLevel", "");
		mDetails=jsonObject.optString("Details", "");
		mForEmpID=jsonObject.optString("ForEmpID", "");
		mForEmpName=jsonObject.optString("ForEmpName", "");
		mRemark=jsonObject.optString("Remark", "");
		mReqCode=jsonObject.optString("ReqCode", "");
		mReqID=jsonObject.optString("ReqID", "");
		mReqType=jsonObject.optString("ReqType", "");
		mReqTypeDesc=jsonObject.optString("ReqTypeDesc", "");
		mStatus=jsonObject.optString("Status", "");
	}

	public String getmApprovalLevel() {
		return mApprovalLevel;
	}

	public void setmApprovalLevel(String mApprovalLevel) {
		this.mApprovalLevel = mApprovalLevel;
	}

	public String getmDetails() {
		return mDetails;
	}

	public void setmDetails(String mDetails) {
		this.mDetails = mDetails;
	}

	public String getmForEmpID() {
		return mForEmpID;
	}

	public void setmForEmpID(String mForEmpID) {
		this.mForEmpID = mForEmpID;
	}

	public String getmForEmpName() {
		return mForEmpName;
	}

	public void setmForEmpName(String mForEmpName) {
		this.mForEmpName = mForEmpName;
	}

	public String getmRemark() {
		return mRemark;
	}

	public void setmRemark(String mRemark) {
		this.mRemark = mRemark;
	}

	public String getmReqCode() {
		return mReqCode;
	}

	public void setmReqCode(String mReqCode) {
		this.mReqCode = mReqCode;
	}

	public String getmReqID() {
		return mReqID;
	}

	public void setmReqID(String mReqID) {
		this.mReqID = mReqID;
	}

	public String getmReqType() {
		return mReqType;
	}

	public void setmReqType(String mReqType) {
		this.mReqType = mReqType;
	}

	public String getmReqTypeDesc() {
		return mReqTypeDesc;
	}

	public void setmReqTypeDesc(String mReqTypeDesc) {
		this.mReqTypeDesc = mReqTypeDesc;
	}

	public String getmStatus() {
		return mStatus;
	}

	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public ArrayList<EmployeeLeaveModel> getmPendingLeaveList() {
		return mPendingLeaveList;
	}

	public void setmPendingLeaveList(ArrayList<EmployeeLeaveModel> mPendingLeaveList) {
		this.mPendingLeaveList = mPendingLeaveList;
	}
	
}
