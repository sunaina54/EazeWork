package hr.eazework.com.model;

import org.json.JSONObject;

public class SalarySlipItemModel {

	private String mAmount;
	private String mArrear;
	private String mCorpHeadName;
	private String mHeadType;
	private String mRate;
	private String mYTDAmount;
	private boolean mIsEarning;

	public SalarySlipItemModel(JSONObject jsonObject) {
		if (jsonObject != null) {
			mAmount = jsonObject.optString("Amount", "0");
			mArrear = jsonObject.optString("Arrear", "0");
			mRate = jsonObject.optString("Rate", "0");
			mYTDAmount = jsonObject.optString("YTDAmt","0");
			mCorpHeadName = jsonObject.optString("CorpHeadName", "");
			mHeadType = jsonObject.optString("HeadType", "E");
			mIsEarning = mHeadType.equalsIgnoreCase("E");
		}
	}

	public String getmAmount() {
		return mAmount;
	}

	public void setmAmount(String mAmount) {
		this.mAmount = mAmount;
	}

	public String getmArrear() {
		return mArrear;
	}

	public void setmArrear(String mArrear) {
		this.mArrear = mArrear;
	}

	public String getmCorpHeadName() {
		return mCorpHeadName;
	}

	public void setmCorpHeadName(String mCorpHeadName) {
		this.mCorpHeadName = mCorpHeadName;
	}

	public String getmHeadType() {
		return mHeadType;
	}

	public void setmHeadType(String mHeadType) {
		this.mHeadType = mHeadType;
	}

	public String getmRate() {
		return mRate;
	}

	public void setmRate(String mRate) {
		this.mRate = mRate;
	}

	public String getmYTDAmount() {
		return mYTDAmount;
	}

	public void setmYTDAmount(String mYTDAmount) {
		this.mYTDAmount = mYTDAmount;
	}

	public boolean ismIsEarning() {
		return mIsEarning;
	}

	public void setmIsEarning(boolean mIsEarning) {
		this.mIsEarning = mIsEarning;
	}

}
