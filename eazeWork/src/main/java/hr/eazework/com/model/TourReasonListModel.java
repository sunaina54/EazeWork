package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class TourReasonListModel implements Serializable {
    private String Reason;
    private String ReasonCode;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }
}
