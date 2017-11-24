package hr.eazework.com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manjunath on 04-04-2017.
 */

public class TypeWiseListModel {
    private String code;
    private String value;
    private ArrayList<TypeWiseListModel> list = new ArrayList<>();

    public TypeWiseListModel(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public TypeWiseListModel(String json){
        JSONObject object = null;
        try {
            object = new JSONObject(json);
            code = object.optString("Code","");
            value = object.optString("Value","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TypeWiseListModel(JSONArray array){
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    list.add(new TypeWiseListModel(array.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<TypeWiseListModel> getList() {
        return list;
    }

    public void setList(ArrayList<TypeWiseListModel> list) {
        this.list = list;
    }
}
