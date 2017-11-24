package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 17-11-2017.
 */

public class CorpEmpParamListItem implements Serializable {
    private String Param;
    private String Value;

    public String getParam() {
        return Param;
    }

    public void setParam(String param) {
        Param = param;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
