package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 19-02-2018.
 */

public class HolidayListResponseModel implements Serializable {
    private GetHolidayListResult GetHolidayListResult;


    public hr.eazework.com.model.GetHolidayListResult getGetHolidayListResult() {
        return GetHolidayListResult;
    }

    public void setGetHolidayListResult(hr.eazework.com.model.GetHolidayListResult getHolidayListResult) {
        GetHolidayListResult = getHolidayListResult;
    }

    static public HolidayListResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HolidayListResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


}
