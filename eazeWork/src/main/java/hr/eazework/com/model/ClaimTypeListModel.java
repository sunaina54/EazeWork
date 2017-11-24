package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 06-09-2017.
 */

public class ClaimTypeListModel extends GenericResponse implements Serializable {
    private boolean MultipleVoucherTypeYN;
    private ArrayList<ClaimTypeListItem> claimTypeList;

    public boolean isMultipleVoucherTypeYN() {
        return MultipleVoucherTypeYN;
    }

    public void setMultipleVoucherTypeYN(boolean multipleVoucherTypeYN) {
        MultipleVoucherTypeYN = multipleVoucherTypeYN;
    }

    public ArrayList<ClaimTypeListItem> getClaimTypeList() {
        return claimTypeList;
    }

    public void setClaimTypeList(ArrayList<ClaimTypeListItem> claimTypeList) {
        this.claimTypeList = claimTypeList;
    }
    static public ClaimTypeListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ClaimTypeListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
