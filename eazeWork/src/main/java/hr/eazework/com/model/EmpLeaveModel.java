package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmpLeaveModel {
	private String mBalance;
	private String mLeaveMessage;
	private String mLeaveId;
	private String mLeaveShortCode;
	private String mConsumed;
	private ArrayList<EmpLeaveModel> mEmpLeaveList;

	public EmpLeaveModel() {
		// TODO Auto-generated constructor stub
	}
	public EmpLeaveModel(String jsonString) {
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			JSONArray array=jsonObject.optJSONArray("leavesBalanceList");
			mEmpLeaveList =new ArrayList<EmpLeaveModel>();
			if(array!=null){
				for (int i = 0; i < array.length(); i++) {
					EmpLeaveModel mEmpLeaveModel=new EmpLeaveModel(array.getJSONObject(i).toString());
					mEmpLeaveList.add(mEmpLeaveModel);
				}
			}
			mBalance=jsonObject.optString("Balance", "0");
			mLeaveMessage=jsonObject.optString("Leave", "0");
			mLeaveId=jsonObject.optString("LeaveID", "0");
			mLeaveShortCode=jsonObject.optString("LeaveShortCode", "0");
			mConsumed=jsonObject.optString("Consumed", "0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getmBalance() {
		return mBalance;
	}
	public void setmBalance(String mBalance) {
		this.mBalance = mBalance;
	}
	public String getmLeaveMessage() {
		return mLeaveMessage;
	}
	public void setmLeaveMessage(String mLeaveMessage) {
		this.mLeaveMessage = mLeaveMessage;
	}
	public String getmLeaveId() {
		return mLeaveId;
	}
	public void setmLeaveId(String mLeaveId) {
		this.mLeaveId = mLeaveId;
	}
	public String getmLeaveShortCode() {
		return mLeaveShortCode;
	}
	public void setmLeaveShortCode(String mLeaveShortCode) {
		this.mLeaveShortCode = mLeaveShortCode;
	}
	public ArrayList<EmpLeaveModel> getmEmpLeaveList() {
		return mEmpLeaveList;
	}
	public void setmEmpLeaveList(ArrayList<EmpLeaveModel> mEmpLeaveList) {
		this.mEmpLeaveList = mEmpLeaveList;
	}
	public EmpLeaveModel getEmpLeaveById(String leaveId) {
		EmpLeaveModel model=null;
		for(EmpLeaveModel leaveModel: mEmpLeaveList){
			if(leaveModel.getmLeaveId().equalsIgnoreCase(leaveId)){
				model=leaveModel;
			}
		}
		return model;
	}
	public String getVisibleFormatedMessage(){
		String string="";
		for(EmpLeaveModel leaveModel: mEmpLeaveList){
			string+=(""+leaveModel.getmLeaveShortCode()+":"+leaveModel.getmBalance()+" ");
		}
		return string;
	}
	public String getmConsumed() {
		return mConsumed;
	}
	public void setmConsumed(String mConsumed) {
		this.mConsumed = mConsumed;
	}
	
}
