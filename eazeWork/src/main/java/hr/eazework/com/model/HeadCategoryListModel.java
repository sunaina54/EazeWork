package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class HeadCategoryListModel implements Serializable {
    private String CategoryDesc;
    private int Category;
    private ArrayList<HeadsItemModel> Heads;

    public ArrayList<HeadsItemModel> getHeads() {
        return Heads;
    }

    public void setHeads(ArrayList<HeadsItemModel> heads) {
        Heads = heads;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        CategoryDesc = categoryDesc;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }
    static public HeadCategoryListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HeadCategoryListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
