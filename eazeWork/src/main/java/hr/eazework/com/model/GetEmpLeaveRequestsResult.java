package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 13-02-2018.
 */

public class GetEmpLeaveRequestsResult extends GenericResponse implements Serializable {
    private ArrayList<LeaveReqsItem> leaveReqs;

    public ArrayList<LeaveReqsItem> getLeaveReqs() {
        return leaveReqs;
    }

    public void setLeaveReqs(ArrayList<LeaveReqsItem> leaveReqs) {
        this.leaveReqs = leaveReqs;
    }
}
