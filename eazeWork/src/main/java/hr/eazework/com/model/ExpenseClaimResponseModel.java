package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 20-09-2017.
 */

public class ExpenseClaimResponseModel implements Serializable
{
    private SaveExpenseResultModel SaveExpenseResult;

    public SaveExpenseResultModel getSaveExpenseResult() {
        return SaveExpenseResult;
    }

    public void setSaveExpenseResult(SaveExpenseResultModel saveExpenseResult) {
        SaveExpenseResult = saveExpenseResult;
    }
    static public ExpenseClaimResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ExpenseClaimResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
