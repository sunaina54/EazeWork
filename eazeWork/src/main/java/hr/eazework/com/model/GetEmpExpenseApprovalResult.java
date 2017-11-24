package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class GetEmpExpenseApprovalResult extends GenericResponse implements Serializable {
    private ArrayList<ExpenseApprovalList> expenseApprovalList;

    public ArrayList<ExpenseApprovalList> getExpenseApprovalList() {
        return expenseApprovalList;
    }

    public void setExpenseApprovalList(ArrayList<ExpenseApprovalList> expenseApprovalList) {
        this.expenseApprovalList = expenseApprovalList;
    }

    static public GetEmpExpenseApprovalResult create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetEmpExpenseApprovalResult.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
