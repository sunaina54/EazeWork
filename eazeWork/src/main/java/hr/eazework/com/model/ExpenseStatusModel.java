package hr.eazework.com.model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 28-10-2017.
 */

public class ExpenseStatusModel extends BaseAppResponseModel implements Serializable{
    private ArrayList<ExpenseStatusData> ExpenseStatusData;


    public ExpenseStatusModel() {
    }

    public ArrayList<hr.eazework.com.model.ExpenseStatusData> getExpenseStatusData() {
        return ExpenseStatusData;
    }

    public void setExpenseStatusData(ArrayList<hr.eazework.com.model.ExpenseStatusData> expenseStatusData) {
        ExpenseStatusData = expenseStatusData;
    }

    public ExpenseStatusModel(String stringJson) {
        /*JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringJson);

            JSONArray array=jsonObject.optJSONArray("ExpenseStatusData");
            if(array!=null){
                ArrayList<ExpenseStatusData> arrayList=new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject jsonObject2=array.getJSONObject(i);
                        ExpenseStatusModel model=new ExpenseStatusModel(jsonObject2.toString());
                        arrayList.add(model);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                expenseStatusData=arrayList;
            }
            mAmount =jsonObject.optString("Amount", "");
            mCurrencyCode =jsonObject.optString("CurrencyCode","");
            mLabel=jsonObject.optString("Label","");
*/
       /* } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/
    }





    static public ExpenseStatusModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        try {
            String data = gson.fromJson(serializedData, ExpenseStatusModel.class).serialize();
            Log.d("TAG", "dataa : " + data);
        }catch (Exception e){
            Log.d("TAG", "Exception : " + e.getStackTrace());
        }
        return gson.fromJson(serializedData, ExpenseStatusModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
