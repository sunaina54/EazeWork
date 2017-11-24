package hr.eazework.mframe.model.base;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hr.eazework.com.ui.util.MLogger;


public class AppResponse extends ModelDataBase {// implements IAPIConstants {

	final String RESPONSE_KEY_OP_STATUS = "opStatus";
	final String RESPONSE_KEY_ERROR_MESSAGE = "errorMessage";
	final String RESPONSE_KEY_MESSAGE_DESCRIPTION = "messageDescription";
	final String RESPONSE_KEY_CURRENT_DATE = "currentDate";
	final String RESPONSE_KEY_CACHE_DATE_IN_MIN = "cacheDataInMins";
	final String RESPONSE_KEY_REF_NO = "refNo";
	final String RESPONSE_KEY_MSISDN_LAUNCH = "msisdn";
	public static String APP_UPDATED_CURRENT_DATE = "";

	protected Hashtable<String, JSONObject> mapJSONObject = new Hashtable<String, JSONObject>();
	protected Hashtable<String, JSONArray> mapJSONArray = new Hashtable<String, JSONArray>();
	protected Hashtable<String, String> mapJSONString = new Hashtable<String, String>();

	protected String opStatus = null;
	protected String errorMessage = null;
	protected String currentDate = null;
	protected String cacheDataInMins = null;
	protected String refNo = null;
	protected String msisdn_launch = null;
	protected String srRefNumber = null;

	public final int INVALID_OPSTATUS = -999;

	protected AppResponse() {
	}

	public AppResponse(String response) throws JSONException {
		MLogger.debug(getClass().getName(), "AppResponse");
		JSONObject root = null;
		try {
			root = new JSONObject(response);
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		// the following is sent/receive in json
		JSONObject mAppData = null;
		JSONObject businessOutput = null;
		try {
			mAppData = root.getJSONObject("mAppData");
			if (mAppData != null) {
				businessOutput = mAppData.getJSONObject("businessOutput");
			}
		} catch (Exception e) {
			businessOutput = root.getJSONObject("businessOutput");
		}
		Iterator<String> it = businessOutput.keys();
		if (it == null) {
			return;
		}
		String name = null;
		while (it.hasNext()) {
			name = (String) it.next();
			if (name.equalsIgnoreCase(RESPONSE_KEY_OP_STATUS)) {
				opStatus = (String) businessOutput.get(RESPONSE_KEY_OP_STATUS)
						.toString();
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_ERROR_MESSAGE)) {
				errorMessage = (String) businessOutput
						.get(RESPONSE_KEY_ERROR_MESSAGE);
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_CURRENT_DATE)) {
				currentDate = (String) businessOutput
						.get(RESPONSE_KEY_CURRENT_DATE);
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_CACHE_DATE_IN_MIN)) {
				cacheDataInMins = (String) businessOutput
						.get(RESPONSE_KEY_CACHE_DATE_IN_MIN);
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_REF_NO)) {
				refNo = (String) businessOutput.get(RESPONSE_KEY_REF_NO);
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_MSISDN_LAUNCH)) {
				msisdn_launch = (String) businessOutput
						.get(RESPONSE_KEY_MSISDN_LAUNCH);
			} else if (name.equalsIgnoreCase("srRefNumber")) {
				srRefNumber = (String) businessOutput.get("srRefNumber");
			} else {
				Object o = businessOutput.get(name);
				if (o instanceof JSONObject) {
					mapJSONObject.put(name, (JSONObject) o);
				} else if (o instanceof JSONArray) {
					mapJSONArray.put(name, (JSONArray) o);
				} else if (o instanceof String) {
					mapJSONString.put(name, (String) o);
				}
			}
			if (currentDate != null && currentDate.length() != 0) {
				APP_UPDATED_CURRENT_DATE = currentDate;
			}
		}
		MLogger.debug(getClass().getName(), "AppResponse.........");
	}

	public AppResponse(Hashtable<String, Object> response) throws JSONException {
		Enumeration<Object> enumKey = response.elements();
		String name = null;
		while (enumKey.hasMoreElements()) {
			name = (String) enumKey.nextElement();
			Object o = response.get(name);
			if (name.equalsIgnoreCase(RESPONSE_KEY_OP_STATUS)) {
				opStatus = (String) o;
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_ERROR_MESSAGE)) {
				errorMessage = (String) o;
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_CURRENT_DATE)) {
				currentDate = (String) o;
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_CACHE_DATE_IN_MIN)) {
				cacheDataInMins = (String) o;
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_REF_NO)) {
				refNo = (String) o;
			} else if (name.equalsIgnoreCase(RESPONSE_KEY_MSISDN_LAUNCH)) {
				msisdn_launch = (String) o;
			} else if (name.equalsIgnoreCase("srRefNumber")) {
				srRefNumber = (String) o;
			}

			else if (o instanceof JSONObject) {
				mapJSONObject.put(name, (JSONObject) o);
			} else if (o instanceof JSONArray) {
				mapJSONArray.put(name, (JSONArray) o);
			} else if (o instanceof String) {
				mapJSONString.put(name, (String) o);
			}

		}

	}

	public int getOpStatus() {
		int opstatusVal = INVALID_OPSTATUS;
		try {
			opstatusVal = Integer.parseInt(opStatus.trim());
		} catch (Exception e) {
		}
		return opstatusVal;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public int getCacheDataInMins() {
		int cacheDataMins = 0;
		try {
			cacheDataMins = Integer.parseInt(cacheDataInMins);
		} catch (Exception e) {
		}
		return cacheDataMins;
	}

	public String getCacheDataInMinsString() {
		return cacheDataInMins;
	}

	public long getCacheDataInMilliSec() {
		long cacheDataMins = 0;
		try {
			cacheDataMins = 60000 * (Long.parseLong(cacheDataInMins));
		} catch (Exception e) {
		}
		return cacheDataMins;
	}

	public String getrefNo() {
		return refNo;
	}

	public String getSrRefNumber() {
		return srRefNumber;
	}

	public String getmsisdnLaunch() {
		return msisdn_launch;
	}

}