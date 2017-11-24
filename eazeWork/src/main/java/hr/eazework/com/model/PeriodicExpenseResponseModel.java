package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-11-2017.
 */

public class PeriodicExpenseResponseModel implements Serializable {

    private ValidateMonthListForPeriodicExpenseResult ValidateMonthListForPeriodicExpenseResult;

    public ValidateMonthListForPeriodicExpenseResult getValidateMonthListForPeriodicExpenseResult() {
        return ValidateMonthListForPeriodicExpenseResult;
    }

    public void setValidateMonthListForPeriodicExpenseResult(ValidateMonthListForPeriodicExpenseResult validateMonthListForPeriodicExpenseResult) {
        ValidateMonthListForPeriodicExpenseResult = validateMonthListForPeriodicExpenseResult;
    }

    static public PeriodicExpenseResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PeriodicExpenseResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
