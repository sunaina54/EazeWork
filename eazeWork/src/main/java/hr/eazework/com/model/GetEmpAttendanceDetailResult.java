package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 23-02-2018.
 */

public class GetEmpAttendanceDetailResult extends GenericResponse implements Serializable {
private AttendanceCalStatusItem attendanceCalStatus;

    public AttendanceCalStatusItem getAttendanceCalStatus() {
        return attendanceCalStatus;
    }

    public void setAttendanceCalStatus(AttendanceCalStatusItem attendanceCalStatus) {
        this.attendanceCalStatus = attendanceCalStatus;
    }


}
