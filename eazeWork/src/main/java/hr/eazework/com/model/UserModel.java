package hr.eazework.com.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String companyId;
	private String corpId;
	private String empId;
	private String loginId;
	private String userName;
	private String DOB;
	private String empCode;
	public UserModel(String response) throws JSONException{
		JSONObject jsonObject=new JSONObject(response);
		initializeData(jsonObject);
	}
	private void initializeData(JSONObject jsonObject) throws JSONException{
		companyId=jsonObject.optString("CompanyID", BaseAppResponseModel.FALLBACK_DATA);
		corpId=jsonObject.optString("CorpID", BaseAppResponseModel.FALLBACK_DATA);
		DOB=jsonObject.optString("DateOfBirth", BaseAppResponseModel.FALLBACK_DATA);
		empId=jsonObject.optString("EmpID", BaseAppResponseModel.FALLBACK_DATA);
		loginId=jsonObject.optString("Login", BaseAppResponseModel.FALLBACK_DATA);
		userName=jsonObject.optString("Name", BaseAppResponseModel.FALLBACK_DATA);
		empCode=jsonObject.optString("EmpCode", BaseAppResponseModel.FALLBACK_DATA);
	}
	public UserModel(JSONObject jsonObject) throws JSONException{
		initializeData(jsonObject);
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
}
