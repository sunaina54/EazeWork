package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.PipedReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class AdvanceRequestSummaryResultModel extends GenericResponse implements Serializable {
    private String AdvanceYN;
    private ArrayList<AdvanceListModel> advanceList;

    public String getAdvanceYN() {
        return AdvanceYN;
    }

    public void setAdvanceYN(String advanceYN) {
        AdvanceYN = advanceYN;
    }

    public ArrayList<AdvanceListModel> getAdvanceList() {
        return advanceList;
    }

    public void setAdvanceList(ArrayList<AdvanceListModel> advanceList) {
        this.advanceList = advanceList;
    }

    static public AdvanceRequestSummaryResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceRequestSummaryResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
