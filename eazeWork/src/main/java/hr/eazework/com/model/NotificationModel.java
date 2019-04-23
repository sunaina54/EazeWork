package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 04-12-2017.
 */

public class NotificationModel implements Serializable {
    private NotificationItemModel data;

    public NotificationItemModel getData() {
        return data;
    }

    public void setData(NotificationItemModel data) {
        this.data = data;
    }
    static public NotificationModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, NotificationModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
