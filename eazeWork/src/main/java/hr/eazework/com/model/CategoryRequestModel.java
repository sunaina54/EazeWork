package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class CategoryRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private CategoryExpenseModel expense;

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

    static public CategoryRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CategoryRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
