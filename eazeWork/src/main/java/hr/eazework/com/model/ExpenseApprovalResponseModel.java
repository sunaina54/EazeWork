package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class ExpenseApprovalResponseModel implements Serializable {
    private GetEmpExpenseApprovalResult GetEmpExpenseApprovalResult;

    public GetEmpExpenseApprovalResult getGetEmpExpenseApprovalResult() {
        return GetEmpExpenseApprovalResult;
    }

    public void setGetEmpExpenseApprovalResult(GetEmpExpenseApprovalResult getEmpExpenseApprovalResult) {
        GetEmpExpenseApprovalResult = getEmpExpenseApprovalResult;
    }

    static public ExpenseApprovalResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ExpenseApprovalResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
