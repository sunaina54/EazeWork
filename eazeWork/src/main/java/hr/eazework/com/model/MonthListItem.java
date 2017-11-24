package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-11-2017.
 */

public class MonthListItem implements Serializable{

    private String Date;
    private String MonthYear;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMonthYear() {
        return MonthYear;
    }

    public void setMonthYear(String monthYear) {
        MonthYear = monthYear;
    }

    static public MonthListItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MonthListItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
