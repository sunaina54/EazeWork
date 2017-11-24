package hr.eazework.com.model;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaveBalanceModel extends BaseAppResponseModel{
	private final String AVAILABLE_TAG="Available";
	private final String CONSUMED_TAG="Consumed";
	private double mAvailable;
	private double mConsumed;
	
	public LeaveBalanceModel() {
	}
	public LeaveBalanceModel(String response) {
		try {
			JSONObject jsonObject=new JSONObject(response);
			erroreCode=jsonObject.optInt(ERROR_CODE_TAG,-1);
			erroreMessage=jsonObject.optString(ERROR_MSG_TAG, FALLBACK_DATA);
			mAvailable =jsonObject.optDouble(AVAILABLE_TAG, 0);
			mConsumed =jsonObject.optDouble(CONSUMED_TAG, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public double getmAvailable() {
		return mAvailable;
	}
	public void setmAvailable(double mAvailable) {
		this.mAvailable = mAvailable;
	}
	public double getmConsumed() {
		return mConsumed;
	}
	public void setmConsumed(double mConsumed) {
		this.mConsumed = mConsumed;
	}
	
}
