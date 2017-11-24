package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class GetAdvancePageInitResultResponseModel extends GenericResponse implements Serializable {
    private String AdvanceProcessYN;
    private String Buttons;
    private DocValidationModel DocValidation;
    private CurrencyListModel CurrencyList;
    private ReasonCodeListModel ReasonCodeList;

    public DocValidationModel getDocValidation() {
        return DocValidation;
    }

    public void setDocValidation(DocValidationModel docValidation) {
        DocValidation = docValidation;
    }

    public String getAdvanceProcessYN() {
        return AdvanceProcessYN;
    }

    public void setAdvanceProcessYN(String advanceProcessYN) {
        AdvanceProcessYN = advanceProcessYN;
    }

    public String getButtons() {
        return Buttons;
    }

    public void setButtons(String buttons) {
        Buttons = buttons;
    }

    public CurrencyListModel getCurrencyList() {
        return CurrencyList;
    }

    public void setCurrencyList(CurrencyListModel currencyList) {
        CurrencyList = currencyList;
    }

    public ReasonCodeListModel getReasonCodeList() {
        return ReasonCodeList;
    }

    public void setReasonCodeList(ReasonCodeListModel reasonCodeList) {
        ReasonCodeList = reasonCodeList;
    }

    static public GetAdvancePageInitResultResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetAdvancePageInitResultResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
