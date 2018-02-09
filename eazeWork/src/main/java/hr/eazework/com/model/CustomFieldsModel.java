package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 16-01-2018.
 */

public class CustomFieldsModel implements Serializable {
    private String FieldCode;
    private String FieldLabel;
    private String FieldTypeID;
    private String FieldValue;
    private String MandatoryYN;
    private String SeqNo;


    public String getFieldCode() {
        return FieldCode;
    }

    public void setFieldCode(String fieldCode) {
        FieldCode = fieldCode;
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

    public String getMandatoryYN() {
        return MandatoryYN;
    }

    public void setMandatoryYN(String mandatoryYN) {
        MandatoryYN = mandatoryYN;
    }

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }
}
