package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 06-09-2017.
 */

public class OnBehalfOfListModel extends GenericResponse implements Serializable {
    private ArrayList<EmployeeListModel> epmployeeList;

    public ArrayList<EmployeeListModel> getEpmployeeList() {
        return epmployeeList;
    }

    public void setEpmployeeList(ArrayList<EmployeeListModel> epmployeeList) {
        this.epmployeeList = epmployeeList;
    }

    static public OnBehalfOfListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, OnBehalfOfListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
