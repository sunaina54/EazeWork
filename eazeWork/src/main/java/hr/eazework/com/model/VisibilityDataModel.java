package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class VisibilityDataModel implements Serializable {
    private String CompulsoryYN;
    private String DocPath;
    private String FieldCode;
    private String FieldDocTranID;
    private String FieldLabel;
    private String FieldTypeID;
    private String FieldValue;
    private String SeqNo;
    private String TranID;
    private String LabelRight;

    public String getLabelRight() {
        return LabelRight;
    }

    public void setLabelRight(String labelRight) {
        LabelRight = labelRight;
    }

    public String getCompulsoryYN() {
        return CompulsoryYN;
    }

    public void setCompulsoryYN(String compulsoryYN) {
        CompulsoryYN = compulsoryYN;
    }

    public String getDocPath() {
        return DocPath;
    }

    public void setDocPath(String docPath) {
        DocPath = docPath;
    }

    public String getFieldCode() {
        return FieldCode;
    }

    public void setFieldCode(String fieldCode) {
        FieldCode = fieldCode;
    }

    public String getFieldDocTranID() {
        return FieldDocTranID;
    }

    public void setFieldDocTranID(String fieldDocTranID) {
        FieldDocTranID = fieldDocTranID;
    }

    public String getFieldLabel() {
        return FieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        FieldLabel = fieldLabel;
    }

    public String getFieldTypeID() {
        return FieldTypeID;
    }

    public void setFieldTypeID(String fieldTypeID) {
        FieldTypeID = fieldTypeID;
    }

    public String getFieldValue() {
        return FieldValue;
    }

    public void setFieldValue(String fieldValue) {
        FieldValue = fieldValue;
    }

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    public String getTranID() {
        return TranID;
    }

    public void setTranID(String tranID) {
        TranID = tranID;
    }

    static public VisibilityDataModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VisibilityDataModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
