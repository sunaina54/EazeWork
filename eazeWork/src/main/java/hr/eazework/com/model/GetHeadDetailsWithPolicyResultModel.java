package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class GetHeadDetailsWithPolicyResultModel extends GenericResponse implements Serializable {
    private long BeforeLimitedTo ;
    private String CurrencyCode;
    private String ExceedingPolicyYN;
    private String HeadID;
    private BigDecimal LimitedTo;
    private String PolicyID;
    private String Scope;
    private String ShowInput;
    private String Unit;
    private String UnitDesc;
    private ArrayList<VisibilityDataModel> Visibility;

   /* public double getLimitedTo() {
        return LimitedTo;
    }

    public void setLimitedTo(double limitedTo) {
        LimitedTo = limitedTo;
    }*/

    public BigDecimal getLimitedTo() {
        return LimitedTo;
    }

    public void setLimitedTo(BigDecimal limitedTo) {
        LimitedTo = limitedTo;
    }

    public void setBeforeLimitedTo(int beforeLimitedTo) {
        BeforeLimitedTo = beforeLimitedTo;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getExceedingPolicyYN() {
        return ExceedingPolicyYN;
    }

    public void setExceedingPolicyYN(String exceedingPolicyYN) {
        ExceedingPolicyYN = exceedingPolicyYN;
    }

    public String getHeadID() {
        return HeadID;
    }

    public void setHeadID(String headID) {
        HeadID = headID;
    }

    public long getBeforeLimitedTo() {
        return BeforeLimitedTo;
    }

    public void setBeforeLimitedTo(long beforeLimitedTo) {
        BeforeLimitedTo = beforeLimitedTo;
    }

   /* public long getLimitedTo() {
        return LimitedTo;
    }

    public void setLimitedTo(long limitedTo) {
        LimitedTo = limitedTo;
    }*/

    public String getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(String policyID) {
        PolicyID = policyID;
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String scope) {
        Scope = scope;
    }

    public String getShowInput() {
        return ShowInput;
    }

    public void setShowInput(String showInput) {
        ShowInput = showInput;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitDesc() {
        return UnitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        UnitDesc = unitDesc;
    }

    public ArrayList<VisibilityDataModel> getVisibility() {
        return Visibility;
    }

    public void setVisibility(ArrayList<VisibilityDataModel> visibility) {
        Visibility = visibility;
    }
    static public GetHeadDetailsWithPolicyResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetHeadDetailsWithPolicyResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
