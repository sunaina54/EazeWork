package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 13-09-2017.
 */

public class ProjectListModel extends GenericResponse implements Serializable{
    private ArrayList<ProjectListItem> ProjectList;

    public ArrayList<ProjectListItem> getProjectList() {
        return ProjectList;
    }

    public void setProjectList(ArrayList<ProjectListItem> projectList) {
        ProjectList = projectList;
    }

    static public ProjectListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ProjectListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
