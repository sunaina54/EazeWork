package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ExpenseClaimSummaryResponseModel extends GenericResponse implements Serializable {

    private GetEmpExpenseResultModel GetEmpExpenseResult;

    public GetEmpExpenseResultModel getGetEmpExpenseResult() {
        return GetEmpExpenseResult;
    }

    public void setGetEmpExpenseResult(GetEmpExpenseResultModel getEmpExpenseResult) {
        GetEmpExpenseResult = getEmpExpenseResult;
    }

    static public ExpenseClaimSummaryResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ExpenseClaimSummaryResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
