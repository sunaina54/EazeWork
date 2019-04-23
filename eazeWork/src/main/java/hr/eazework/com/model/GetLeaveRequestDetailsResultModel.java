package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-02-2018.
 */

public class GetLeaveRequestDetailsResultModel extends  GenericResponse implements Serializable {
    private LeaveRequestDetailsModel LeaveRequestDetails;

    public LeaveRequestDetailsModel getLeaveRequestDetails() {
        return LeaveRequestDetails;
    }

    public void setLeaveRequestDetails(LeaveRequestDetailsModel leaveRequestDetails) {
        LeaveRequestDetails = leaveRequestDetails;
    }
}
