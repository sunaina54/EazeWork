package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ViewGetExpenseDetailResultModel extends GenericResponse implements Serializable {
    private String ButtionInfo;
    private String FromButton;
    private int Source;
    private String TopLabel;
    private String[] Buttons;
    private ViewExpenseItemModel expenseItem;
    private ArrayList<CategoryLineItemLabelItem>  categoryLineItemLabel;

    public ArrayList<CategoryLineItemLabelItem> getCategoryLineItemLabel() {
        return categoryLineItemLabel;
    }

    public void setCategoryLineItemLabel(ArrayList<CategoryLineItemLabelItem> categoryLineItemLabel) {
        this.categoryLineItemLabel = categoryLineItemLabel;
    }

    public String getTopLabel() {
        return TopLabel;
    }

    public void setTopLabel(String topLabel) {
        TopLabel = topLabel;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public String getButtionInfo() {
        return ButtionInfo;
    }

    public void setButtionInfo(String buttionInfo) {
        ButtionInfo = buttionInfo;
    }

    public String getFromButton() {
        return FromButton;
    }

    public void setFromButton(String fromButton) {
        FromButton = fromButton;
    }

    public int getSource() {
        return Source;
    }

    public void setSource(int source) {
        Source = source;
    }

    public ViewExpenseItemModel getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(ViewExpenseItemModel expenseItem) {
        this.expenseItem = expenseItem;
    }

    static public ViewGetExpenseDetailResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ViewGetExpenseDetailResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
