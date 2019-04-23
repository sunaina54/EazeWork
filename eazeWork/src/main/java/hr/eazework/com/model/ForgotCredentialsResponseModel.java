package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 05-02-2018.
 */

public class ForgotCredentialsResponseModel implements Serializable {
    private ForgetCredentialsResultModel ForgetCredentialsResult;

    public ForgetCredentialsResultModel getForgetCredentialsResult() {
        return ForgetCredentialsResult;
    }

    public void setForgetCredentialsResult(ForgetCredentialsResultModel forgetCredentialsResult) {
        ForgetCredentialsResult = forgetCredentialsResult;
    }

    static public ForgotCredentialsResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ForgotCredentialsResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
