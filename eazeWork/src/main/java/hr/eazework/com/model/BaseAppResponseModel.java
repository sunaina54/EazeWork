package hr.eazework.com.model;

import org.json.JSONException;
import org.json.JSONObject;

import hr.eazework.com.ui.util.MLogger;


public class BaseAppResponseModel {
	public int erroreCode; 
	public String erroreMessage; 
	public static final String FALLBACK_DATA="";
	public final String ERROR_MSG_TAG="ErrorMessage"; 
	public final String ERROR_CODE_TAG="ErrorCode"; 
	
	public BaseAppResponseModel() {
		// TODO Auto-generated constructor stub
	}
	public BaseAppResponseModel(String response) throws JSONException{
		JSONObject root = null;
		try {
			root = new JSONObject(response);
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		erroreCode=root.optInt(ERROR_CODE_TAG,-1);
		erroreMessage=root.optString(ERROR_MSG_TAG, FALLBACK_DATA);
	}
	public int getErroreCode() {
		return erroreCode;
	}
	public String getErroreMessage() {
		return erroreMessage;
	}
	
}
