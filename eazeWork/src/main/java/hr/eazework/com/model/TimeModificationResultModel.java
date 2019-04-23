package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 31-01-2018.
 */

public class TimeModificationResultModel extends GenericResponse implements Serializable {
    private int ReqID;

    public int getReqID() {
        return ReqID;
    }

    public void setReqID(int reqID) {
        ReqID = reqID;
    }
}
