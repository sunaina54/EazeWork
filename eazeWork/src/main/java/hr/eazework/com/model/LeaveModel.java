package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveModel implements Serializable{

	/**
	 * 
	 */
	public enum LEAVE_TYPE{CASUAL_LEAVE,SICK_LEAVE,PAID_LEAVE};
	private static final long serialVersionUID = 1L;
	private String mLeaveName;
	private LEAVE_TYPE mLeaveType;
	private float  mLeaveBalanceCount;
	private String mLeaveFrom;
	private String mLeaveTo;
	private String mLeaveMSG;
	private String mLeaveDays;
	private String mRequestId;
	private String mRequestCode;
	private String mStatus;
	private boolean isWithdrawen;
	private String[] Buttons;
	private String StatusDesc;
	private ArrayList<LeaveModel> mLeaveLIst;
	public LeaveModel() {
		// TODO Auto-generated constructor stub
	}

	public String[] fetchButton(JSONArray jsonArray){
		if(jsonArray!=null){
			Buttons=new String[jsonArray.length()];
			for(int j=0; j<jsonArray.length();j++){
				try {
					String str=(String) jsonArray.get(j);
					Buttons[j]=str;

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		return Buttons;
	}
	public LeaveModel(String jsonString) {
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			JSONArray array = jsonObject.optJSONArray("leaveReqs");
			mLeaveLIst =new ArrayList<LeaveModel>();
			if(array!=null){
				for (int i = 0; i < array.length(); i++) {
					LeaveModel leaveModel=new LeaveModel(array.optJSONObject(i).toString());
					mLeaveLIst.add(leaveModel);
				}
			}
			mLeaveName=jsonObject.optString("LeaveName", "");
			mLeaveFrom =jsonObject.optString("StartDate", "");
			mLeaveTo =jsonObject.optString("EndDate", "");
			mLeaveMSG =jsonObject.optString("Remarks", "");
			mLeaveDays =jsonObject.optString("TotalDays", "0");
			mRequestId =jsonObject.optString("ReqID", "");
			mRequestCode =jsonObject.optString("ReqCode", "");
			mStatus =jsonObject.optString("Status", "");
			StatusDesc =jsonObject.optString("StatusDesc", "");
			try {
				Buttons=fetchButton(jsonObject.getJSONArray("Buttons"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			isWithdrawen=jsonObject.optString("WithdrawYN", "N").equalsIgnoreCase("Y");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public String[] getButtons() {
		return Buttons;
	}

	public void setButtons(String[] buttons) {
		Buttons = buttons;
	}

	public String getStatusDesc() {
		return StatusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		StatusDesc = statusDesc;
	}

	public String getmLeaveName() {
		return mLeaveName;
	}
	public LEAVE_TYPE getmLeaveType() {
		return mLeaveType;
	}
	public void setmLeaveType(LEAVE_TYPE mLeaveType) {
		mLeaveName=getLeaveTitle(mLeaveType);
		this.mLeaveType = mLeaveType;
	}
	private String getLeaveTitle(LEAVE_TYPE type) {
		switch (type) {
		case CASUAL_LEAVE:
			return "Casual Leave";
		case PAID_LEAVE:
			return "Paid Leave";
		case SICK_LEAVE:
			return "Sick Leave";

		default:
			return "Casual Leave";
		}
	}


	public float getmLeaveBalance() {
		return mLeaveBalanceCount;
	}
	public void setmLeaveBalance(float mLeaveBalanceCount) {
		this.mLeaveBalanceCount = mLeaveBalanceCount;
	}
	public String getmLeaveFrom() {
		return mLeaveFrom;
	}
	public void setmLeaveFrom(String mLeaveFrom) {
		this.mLeaveFrom = mLeaveFrom;
	}
	public String getmLeaveTo() {
		return mLeaveTo;
	}
	public void setmLeaveTo(String mLeaveTo) {
		this.mLeaveTo = mLeaveTo;
	}
	public String getmLeaveMSG() {
		return mLeaveMSG;
	}
	public void setmLeaveMSG(String mLeaveMSG) {
		this.mLeaveMSG = mLeaveMSG;
	}
	public ArrayList<LeaveModel> getmLeaveLIst() {
		return mLeaveLIst;
	}
	public void setmLeaveLIst(ArrayList<LeaveModel> mLeaveLIst) {
		this.mLeaveLIst = mLeaveLIst;
	}
	public String getmLeaveDays() {
		return mLeaveDays;
	}
	public void setmLeaveDays(String mLeaveDays) {
		this.mLeaveDays = mLeaveDays;
	}
	public String getmRequestId() {
		return mRequestId;
	}
	public void setmRequestId(String mRequestId) {
		this.mRequestId = mRequestId;
	}
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public boolean isWithdrawen() {
		return isWithdrawen;
	}
	public void setWithdrawen(boolean isWithdrawen) {
		this.isWithdrawen = isWithdrawen;
	}
	public float getmLeaveBalanceCount() {
		return mLeaveBalanceCount;
	}
	public void setmLeaveName(String mLeaveName) {
		this.mLeaveName = mLeaveName;
	}
	public String getmRequestCode() {
		return mRequestCode;
	}
	public void setmRequestCode(String mRequestCode) {
		this.mRequestCode = mRequestCode;
	}

	
}
