package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class CategoryListResponseModel implements Serializable {
    private GetCategoryListResultModel GetCategoryListResult;

    public GetCategoryListResultModel getGetCategoryListResult() {
        return GetCategoryListResult;
    }

    public void setGetCategoryListResult(GetCategoryListResultModel getCategoryListResult) {
        GetCategoryListResult = getCategoryListResult;
    }

    static public CategoryListResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CategoryListResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
