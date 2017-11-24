package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SalaryMonthModel implements Serializable{
private String mMonthName;
private String mMontTitle;
ArrayList<SalaryMonthModel> months;
private String mSalarySlipPath;
public SalaryMonthModel() {
	// TODO Auto-generated constructor stub
}
public SalaryMonthModel(String stringJson) {
	JSONObject jsonObject;
	try {
		jsonObject = new JSONObject(stringJson);
	
	JSONArray array=jsonObject.optJSONArray("Months");
	if(array!=null){
		ArrayList<SalaryMonthModel> arrayList=new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject jsonObject2=array.getJSONObject(i);
				SalaryMonthModel model=new SalaryMonthModel(jsonObject2.toString());
				arrayList.add(model);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		months=arrayList;
	}
	mMonthName=jsonObject.optString("Month","");
	mMontTitle=jsonObject.optString("MonthDesc","");
	mSalarySlipPath=jsonObject.optString("SalarySlipPath", "");

	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}
public String getmMonthName() {
	return mMonthName;
}
public void setmMonthName(String mMonthName) {
	this.mMonthName = mMonthName;
}
public String getmMontTitle() {
	return mMontTitle;
}
public void setmMontTitle(String mMontTitle) {
	this.mMontTitle = mMontTitle;
}
public ArrayList<SalaryMonthModel> getMonths() {
	return (months!=null?months:new ArrayList<SalaryMonthModel>());
}
public void setMonths(ArrayList<SalaryMonthModel> months) {
	this.months = months;
}
public String getmSalarySlipPath() {
	return mSalarySlipPath;
}
public void setmSalarySlipPath(String mSalarySlipPath) {
	this.mSalarySlipPath = mSalarySlipPath;
}

}
