package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-11-2017.
 */

public class PeriodicExpenseRequest implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
     private CategoryExpenseModel expense;
    private String [] monthList;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public CategoryExpenseModel getExpense() {
        return expense;
    }

    public void setExpense(CategoryExpenseModel expense) {
        this.expense = expense;
    }

    public String[] getMonthList() {
        return monthList;
    }

    public void setMonthList(String[] monthList) {
        this.monthList = monthList;
    }

    static public PeriodicExpenseRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PeriodicExpenseRequest.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
