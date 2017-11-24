package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 13-09-2017.
 */

public class EvtClaimTypeChangeResult extends GenericResponse implements Serializable {
    private ProjectListModel ProjectList;
    private String ProjectYN;

    public String getProjectYN() {
        return ProjectYN;
    }

    public void setProjectYN(String projectYN) {
        ProjectYN = projectYN;
    }

    public ProjectListModel getProjectList() {
        return ProjectList;
    }

    public void setProjectList(ProjectListModel projectList) {
        ProjectList = projectList;
    }

    static public EvtClaimTypeChangeResult create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, EvtClaimTypeChangeResult.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
