package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class GetCategoryListResultModel extends GenericResponse implements Serializable {

    private ArrayList<HeadCategoryListModel> headCategoryList;

    public ArrayList<HeadCategoryListModel> getHeadCategoryList() {
        return headCategoryList;
    }

    public void setHeadCategoryList(ArrayList<HeadCategoryListModel> headCategoryList) {
        this.headCategoryList = headCategoryList;
    }

    static public GetCategoryListResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetCategoryListResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
