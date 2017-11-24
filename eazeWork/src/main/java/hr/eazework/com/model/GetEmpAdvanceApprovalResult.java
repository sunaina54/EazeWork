package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class GetEmpAdvanceApprovalResult extends GenericResponse implements Serializable {
    private String AdvanceYN;
    private ArrayList<GetAdvanceDetailResultModel> advanceList;

    public String getAdvanceYN() {
        return AdvanceYN;
    }

    public void setAdvanceYN(String advanceYN) {
        AdvanceYN = advanceYN;
    }

    public ArrayList<GetAdvanceDetailResultModel> getAdvanceList() {
        return advanceList;
    }

    public void setAdvanceList(ArrayList<GetAdvanceDetailResultModel> advanceList) {
        this.advanceList = advanceList;
    }

    static public GetEmpAdvanceApprovalResult create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetEmpAdvanceApprovalResult.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
