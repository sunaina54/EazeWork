package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 19-09-2017.
 */

public class AdvanceListItemModel implements Serializable {
    private String AdjAmount;
    private String paidAmount;
    private int AdvanceID;
    private String Flag;
    private String ReqCode;
    private int SeqNo;
    private int TranID ;
    private String Reason;

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        this.Reason = reason;
    }

    public String getAdjAmount() {
        return AdjAmount;
    }

    public void setAdjAmount(String adjAmount) {
        AdjAmount = adjAmount;
    }

    public int getAdvanceID() {
        return AdvanceID;
    }

    public void setAdvanceID(int advanceID) {
        AdvanceID = advanceID;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getReqCode() {
        return ReqCode;
    }

    public void setReqCode(String reqCode) {
        ReqCode = reqCode;
    }

    public int getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(int seqNo) {
        SeqNo = seqNo;
    }

    public int getTranID() {
        return TranID;
    }

    public void setTranID(int tranID) {
        TranID = tranID;
    }
    static public AdvanceListItemModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceListItemModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
