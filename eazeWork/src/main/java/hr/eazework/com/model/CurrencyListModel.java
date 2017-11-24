package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Array;
import java.util.Arrays;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class CurrencyListModel extends GenericResponse implements Serializable {
    private String CurrencyList[] ;

    public String[] getCurrencyList() {
        return CurrencyList;
    }

    public void setCurrencyList(String[] currencyList) {
        CurrencyList = currencyList;
    }

    static public CurrencyListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CurrencyListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
