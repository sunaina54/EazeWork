package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class RoleListItem implements Serializable {

    private String RoleDesc;
    private String RoleID;

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    public String getRoleDesc() {

        return RoleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        RoleDesc = roleDesc;
    }
    static public RoleListItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RoleListItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
