package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SalarySlipDataModel {
	private ArrayList<SalarySlipItemModel> dataItems;
	private String mGrossDeduction;
	private String mGrossPay;
	private String mLWP;
	private String mNetSalary;
	private String mPayableDays;
	private String mSalarySlipPath;

	public SalarySlipDataModel(JSONObject jsonObject) {
		if (jsonObject != null) {
			mGrossDeduction = jsonObject.optString("GrossDeduction", "");
			mGrossPay = jsonObject.optString("GrossPay", "");
			mLWP = jsonObject.optString("LWP", "");
			mNetSalary = jsonObject.optString("NetSalary", "");
			mPayableDays = jsonObject.optString("PayableDays", "");
			mSalarySlipPath = jsonObject.optString("SalarySlipPath", "");
			JSONArray array=jsonObject.optJSONArray("data");
			if(array==null){
				dataItems=new ArrayList<>();
			} else {
				dataItems=new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					try {
						dataItems.add(new SalarySlipItemModel(array.getJSONObject(i)));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public ArrayList<SalarySlipItemModel> getDataItems() {
		return dataItems;
	}

	public void setDataItems(ArrayList<SalarySlipItemModel> dataItems) {
		this.dataItems = dataItems;
	}

	public String getmGrossDeduction() {
		return mGrossDeduction;
	}

	public void setmGrossDeduction(String mGrossDeduction) {
		this.mGrossDeduction = mGrossDeduction;
	}

	public String getmGrossPay() {
		return mGrossPay;
	}

	public void setmGrossPay(String mGrossPay) {
		this.mGrossPay = mGrossPay;
	}

	public String getmLWP() {
		return mLWP;
	}

	public void setmLWP(String mLWP) {
		this.mLWP = mLWP;
	}

	public String getmNetSalary() {
		return mNetSalary;
	}

	public void setmNetSalary(String mNetSalary) {
		this.mNetSalary = mNetSalary;
	}

	public String getmPayableDays() {
		return mPayableDays;
	}

	public void setmPayableDays(String mPayableDays) {
		this.mPayableDays = mPayableDays;
	}

	public String getmSalarySlipPath() {
		return mSalarySlipPath;
	}

	public void setmSalarySlipPath(String mSalarySlipPath) {
		this.mSalarySlipPath = mSalarySlipPath;
	}
	
}
