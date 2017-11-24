package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 14-11-2017.
 */

public class GetMonthListForPeriodicExpenseResult extends GenericResponse implements Serializable {

    private ArrayList<MonthListItem> monthList;

    public ArrayList<MonthListItem> getMonthList() {
        return monthList;
    }

    public void setMonthList(ArrayList<MonthListItem> monthList) {
        this.monthList = monthList;
    }

    static public GetMonthListForPeriodicExpenseResult create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetMonthListForPeriodicExpenseResult.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
