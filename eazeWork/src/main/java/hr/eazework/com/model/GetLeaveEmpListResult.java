package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PSQ on 11/12/2017.
 */

public class GetLeaveEmpListResult extends GenericResponse implements Serializable {
    private ArrayList<EmployItem> LeaveEmps;

    public ArrayList<EmployItem> getLeaveEmps() {
        return LeaveEmps;
    }

    public void setLeaveEmps(ArrayList<EmployItem> leaveEmps) {
        LeaveEmps = leaveEmps;
    }
}
