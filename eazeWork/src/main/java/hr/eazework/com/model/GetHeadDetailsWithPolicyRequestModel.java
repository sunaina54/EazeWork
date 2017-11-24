package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class GetHeadDetailsWithPolicyRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private VisibilityExpenseModel expense;
    private String expCategory;
    private String headID;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public VisibilityExpenseModel getExpense() {
        return expense;
    }

    public void setExpense(VisibilityExpenseModel expense) {
        this.expense = expense;
    }

    public String getExpCategory() {
        return expCategory;
    }

    public void setExpCategory(String expCategory) {
        this.expCategory = expCategory;
    }

    public String getHeadID() {
        return headID;
    }

    public void setHeadID(String headID) {
        this.headID = headID;
    }

    static public GetHeadDetailsWithPolicyRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetHeadDetailsWithPolicyRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
