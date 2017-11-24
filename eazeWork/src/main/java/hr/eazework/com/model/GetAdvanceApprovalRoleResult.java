package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class GetAdvanceApprovalRoleResult extends GenericResponse implements Serializable {
    private ArrayList<RoleListItem> RoleList;

    public ArrayList<RoleListItem> getRoleList() {
        return RoleList;
    }

    public void setRoleList(ArrayList<RoleListItem> roleList) {
        RoleList = roleList;
    }

    static public GetAdvanceApprovalRoleResult create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetAdvanceApprovalRoleResult.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
