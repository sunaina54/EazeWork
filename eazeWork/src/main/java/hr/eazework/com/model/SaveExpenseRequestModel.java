package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 19-09-2017.
 */

public class SaveExpenseRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private SaveExpenseModel expense;
    private GetExpensePageInitResultModel pageInitResultModel;
    private int screenName;
    private String[] Buttons;

    public GetExpensePageInitResultModel getPageInitResultModel() {
        return pageInitResultModel;
    }

    public void setPageInitResultModel(GetExpensePageInitResultModel pageInitResultModel) {
        this.pageInitResultModel = pageInitResultModel;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public int getScreenName() {
        return screenName;
    }

    public void setScreenName(int screenName) {
        this.screenName = screenName;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public SaveExpenseModel getExpense() {

        return expense;
    }

    public void setExpense(SaveExpenseModel expense) {
        this.expense = expense;
    }
    static public SaveExpenseRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SaveExpenseRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
