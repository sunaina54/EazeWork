package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class VisibilityExpenseModel implements Serializable {
    private VisibilityExpenseItem expenseItem;

    public VisibilityExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(VisibilityExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    static public VisibilityExpenseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VisibilityExpenseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
