package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-09-2017.
 */

public class GetApproverDetailsRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private ApproverExpenseModel expense;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public ApproverExpenseModel getExpense() {
        return expense;
    }

    public void setExpense(ApproverExpenseModel expense) {
        this.expense = expense;
    }

    static public GetApproverDetailsRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetApproverDetailsRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


}
