package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hr.eazework.com.ui.util.SharedPreference;


public class LoginUserModel extends BaseAppResponseModel{
	private final String LOGIN_USER_RESULT_TAG="LogInUserResult";
	private final String LOGIN_USER_MODEL_TAG="LoggedInUser";
	private UserModel userModel;
	private boolean isLoggedIn;
	private ArrayList<String> roles;
	public LoginUserModel() {
	}
	public LoginUserModel(String response) {
		try {
			JSONObject jsonObject=new JSONObject(response);
			SharedPreference.setSessionId(jsonObject.optString("SessionID", ""));
			erroreCode=jsonObject.optInt(ERROR_CODE_TAG,-1);
			erroreMessage=jsonObject.optString(ERROR_MSG_TAG, FALLBACK_DATA);
			
			userModel=new UserModel(jsonObject.getJSONObject(LOGIN_USER_MODEL_TAG));
			isLoggedIn=jsonObject.optBoolean("IsLoggedIn", false);
			JSONArray array=jsonObject.getJSONArray("Roles");
			roles=new ArrayList<String>();
			for(int i=0;i<array.length();i++){
				roles.add((String)array.get(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public UserModel getUserModel() {
		return userModel;
	}
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public ArrayList<String> getRoles() {
		return roles;
	}
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
}
