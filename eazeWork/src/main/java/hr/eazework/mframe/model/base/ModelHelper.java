package hr.eazework.mframe.model.base;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelHelper {
	
	public static JSONObject getJSONObjectFromHashtable(Hashtable<String, JSONObject> mapJSONObject , String key){
		JSONObject jObj = null;
		try{
			jObj = (JSONObject) mapJSONObject.get(key);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return jObj;
	}
	
	public static JSONArray getJSONArrayFromHashtable(Hashtable<String, JSONArray> mapJSONObject , String key){
		JSONArray jArray = null;
		try{
			jArray = (JSONArray) mapJSONObject.get(key);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return jArray;
	}
	
	public static String getStringFromHashtable(Hashtable<String, String> mapJSONObject , String key){
		String strObj = null;
		try{
			strObj = (String) mapJSONObject.get(key);
			strObj= strObj.trim();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return strObj;
	}
	
	public static String getJSONValue(JSONObject jObj , String key){
		String strObj = "";
		try{
			if (jObj.has(key)) {
				strObj = (String) jObj.get(key);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return strObj;
	}
	
	@SuppressWarnings("finally")
	public static JSONObject getJSONObjectFromJSONObject(JSONObject jObj, String key){
		JSONObject j_obj = null;
		try {
			j_obj = jObj.getJSONObject(key);
		} catch (Exception e) {
			// TODO: handle exception
			j_obj = null;
		}finally{
			return j_obj;
		}
	}
	
	@SuppressWarnings("finally")
	public static JSONArray getJSONArrayFromJSONObject(JSONObject jObj, String key){
		JSONArray j_array = null;
		try {
			j_array = jObj.getJSONArray(key);
		} catch (Exception e) {
			// TODO: handle exception
			j_array = null;
		}finally{
			return j_array;
		}
	}
	
	@SuppressWarnings("finally")
	public static boolean checkNodeInJson(JSONObject jObj, String key){
		boolean ret = false;
		try{
			jObj.getString(key);
			ret = true;
		}catch (Exception e) {
			ret = false;			
		}
		finally{
			return ret;
		}
	}
	
	

	public boolean checkIfNodeJSONObject(JSONObject jObj, String key){
		boolean retVal = false;// return false if key s not a json object
		try {
			Object jsonObj = jObj.getJSONObject(key);
			if(jsonObj instanceof JSONObject){
				retVal = true;
			}
		} catch (JSONException e) {
			retVal = false;
		}
		return retVal;

	}

	public boolean checkIfNodeJSONArrayJSONObject (JSONObject jObj, String key){
		boolean retVal = false;// return false if key s not a json array
		try {
			Object jsonArrayObj = jObj.getJSONArray(key);
			if(jsonArrayObj instanceof JSONArray){
				retVal = true;
			}
		} catch (JSONException e) {
			retVal = false;
		}
		return retVal;

	}

}
