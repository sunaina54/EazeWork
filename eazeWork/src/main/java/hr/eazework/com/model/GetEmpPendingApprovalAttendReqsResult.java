package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 12-02-2018.
 */

public class GetEmpPendingApprovalAttendReqsResult extends GenericResponse implements Serializable {
private ArrayList<AttendanceItem> reqTypeDetails;

    public ArrayList<AttendanceItem> getReqTypeDetails() {
        return reqTypeDetails;
    }

    public void setReqTypeDetails(ArrayList<AttendanceItem> reqTypeDetails) {
        this.reqTypeDetails = reqTypeDetails;
    }
}
