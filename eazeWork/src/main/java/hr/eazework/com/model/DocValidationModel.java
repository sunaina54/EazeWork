package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 26-10-2017.
 */

public class DocValidationModel implements Serializable {
    private String Message;
    private String Message1;
    private String[] Extensions;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage1() {
        return Message1;
    }

    public void setMessage1(String message1) {
        Message1 = message1;
    }

    public String[] getExtensions() {
        return Extensions;
    }

    public void setExtensions(String[] extensions) {
        Extensions = extensions;
    }
    static public DocValidationModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DocValidationModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
