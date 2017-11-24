package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class GetEmpExpenseResultModel extends GenericResponse implements Serializable {

   private ArrayList<ExpenseItemListModel> expenseItemList;

    public ArrayList<ExpenseItemListModel> getExpenseItemList() {
        return expenseItemList;
    }

    public void setExpenseItemList(ArrayList<ExpenseItemListModel> expenseItemList) {
        this.expenseItemList = expenseItemList;
    }

    static public GetEmpExpenseResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetEmpExpenseResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
