package hr.eazework.com.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manjunath on 17-04-2017.
 */

public class GeoCoderModel {
    private String mPincode;
    private String mCity;
    private String mCountry;
    private String mState;
    private String mStateCode;
    private String mAddress1;
    private String mCompleteAddress;
    private String mCountryCode;

    public GeoCoderModel(JSONArray array){
        try {
            JSONObject parentObject = array.getJSONObject(0);
            if(parentObject.has("formatted_address")){
                mCompleteAddress = parentObject.optString("formatted_address","");
            }
            if(parentObject.has("address_components")) {
                JSONArray childArray = parentObject.optJSONArray("address_components");
                List<String> address1List = new ArrayList<>();
                for (int i = 0 ; i < childArray.length() ; i++) {
                    JSONObject object = childArray.optJSONObject(i);
                    if(object.has("types")) {
                        JSONArray premiseArray = object.optJSONArray("types");
                        for (int j = 0 ; j < premiseArray.length() ; j++ ) {
                            String type = premiseArray.optString(j);
                            if(type.equals("premise") || type.equals("sublocality_level_1") || type.equals("sublocality_level_2") || type.equals("route")) {
                                address1List.add(object.optString("long_name"));
                            }
                            if(type.equals("country")) {
                                if(object.has("long_name")) {
                                    mCountry = object.optString("long_name","");
                                }
                                if(object.has("short_name")) {
                                    mCountryCode = object.optString("short_name","");
                                }
                            }

                            if(type.equals("administrative_area_level_1")) {
                                if(object.has("long_name")) {
                                    mState = object.optString("long_name","");
                                }
                                if(object.has("short_name")) {
                                    mStateCode = object.optString("short_name","");
                                }
                            }
                            if(type.equals("locality")) {
                                if(object.has("long_name")) {
                                    mCity = object.optString("long_name","");
                                }
                            }

                            if(type.equals("postal_code")) {
                                if(object.has("long_name")) {
                                    mPincode = object.optString("long_name","");
                                }
                            }
                        }
                    }
                }
                mAddress1 = TextUtils.join(", ",address1List);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getmStateCode() {
        return mStateCode;
    }

    public void setmStateCode(String mStateCode) {
        this.mStateCode = mStateCode;
    }

    public String getmPincode() {
        return mPincode;
    }

    public void setmPincode(String mPincode) {
        this.mPincode = mPincode;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public void setmAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }

    public String getmCompleteAddress() {
        return mCompleteAddress;
    }

    public void setmCompleteAddress(String mCompleteAddress) {
        this.mCompleteAddress = mCompleteAddress;
    }

    public String getmCountryCode() {
        return mCountryCode;
    }

    public void setmCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }
}
