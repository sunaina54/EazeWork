package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PSQ on 11/19/2017.
 */

public class CategoryLineItemLabelItem implements Serializable {
    private int categoryID;
    private ArrayList<LineItemColumnsItem> lineItemColumns;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public ArrayList<LineItemColumnsItem> getLineItemColumns() {
        return lineItemColumns;
    }

    public void setLineItemColumns(ArrayList<LineItemColumnsItem> lineItemColumns) {
        this.lineItemColumns = lineItemColumns;
    }
}
