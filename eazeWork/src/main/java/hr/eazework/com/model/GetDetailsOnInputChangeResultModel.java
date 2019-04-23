package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class GetDetailsOnInputChangeResultModel extends GenericResponse implements Serializable {
    private PartialDayParamsModel PartialDayParams;

    public PartialDayParamsModel getPartialDayParams() {
        return PartialDayParams;
    }

    public void setPartialDayParams(PartialDayParamsModel partialDayParams) {
        PartialDayParams = partialDayParams;
    }
    static public GetDetailsOnInputChangeResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetDetailsOnInputChangeResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
