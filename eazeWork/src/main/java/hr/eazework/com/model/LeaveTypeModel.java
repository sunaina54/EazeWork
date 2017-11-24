package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveTypeModel {
	private boolean isDayP25;
	private boolean isDayP50;
	private boolean isDayP75;
	private String leaveDesc;
	private String leaveId;
	private String leaveType;
	private String leaveName;
	private String processStep;
	private ArrayList<LeaveTypeModel> leaveTypeList;

	public LeaveTypeModel() {
	}

	public LeaveTypeModel(String string) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(string);
			isDayP25=jsonObject.optString("DayP25", "N").equalsIgnoreCase("Y");
			isDayP50=jsonObject.optString("DayP50", "N").equalsIgnoreCase("Y");
			isDayP75=jsonObject.optString("DayP75", "N").equalsIgnoreCase("Y");
			leaveDesc=jsonObject.optString("LeaveDesc", "N");
			leaveId=jsonObject.optString("LeaveID", "N");
			leaveType=jsonObject.optString("LeaveType", "N");
			leaveName=jsonObject.optString("LeaveName", "N");
			processStep=jsonObject.optString("ProcessStep", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public LeaveTypeModel(JSONArray array) {
		leaveTypeList=new ArrayList<LeaveTypeModel>();
		if(array!=null){
			for (int i = 0; i < array.length(); i++) {
				try {
					leaveTypeList.add(new LeaveTypeModel(array.getJSONObject(i).toString()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isDayP25() {
		return isDayP25;
	}

	public void setDayP25(boolean isDayP25) {
		this.isDayP25 = isDayP25;
	}

	public boolean isDayP50() {
		return isDayP50;
	}

	public void setDayP50(boolean isDayP50) {
		this.isDayP50 = isDayP50;
	}

	public boolean isDayP75() {
		return isDayP75;
	}

	public void setDayP75(boolean isDayP75) {
		this.isDayP75 = isDayP75;
	}

	public String getLeaveDesc() {
		return leaveDesc;
	}

	public void setLeaveDesc(String leaveDesc) {
		this.leaveDesc = leaveDesc;
	}

	public String getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveName() {
		return leaveName;
	}

	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	public ArrayList<LeaveTypeModel> getLeaveTypeList() {
		return leaveTypeList;
	}

	public void setLeaveTypeList(ArrayList<LeaveTypeModel> leaveTypeList) {
		this.leaveTypeList = leaveTypeList;
	}

	public ArrayList<String> getLeaveTypeTitleList() {
		ArrayList<String> arrayList=new ArrayList<String>();
		for(LeaveTypeModel leaveTypeModel:leaveTypeList){
			arrayList.add(leaveTypeModel.getLeaveName());
		}
		return arrayList;
	}

	public String getProcessStep() {
		return processStep;
	}

	public void setProcessStep(String processStep) {
		this.processStep = processStep;
	}
	
}
