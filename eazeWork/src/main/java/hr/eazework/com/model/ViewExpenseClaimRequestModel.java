package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ViewExpenseClaimRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private ExpenseRequestModel expense;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public ExpenseRequestModel getExpense() {
        return expense;
    }

    public void setExpense(ExpenseRequestModel expense) {
        this.expense = expense;
    }

    static public ViewExpenseClaimRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ViewExpenseClaimRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
