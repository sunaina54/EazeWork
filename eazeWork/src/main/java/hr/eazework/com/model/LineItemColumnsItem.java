package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by PSQ on 11/19/2017.
 */

public class LineItemColumnsItem implements Serializable {
    private String columnName;
    private String  lableName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }
}
