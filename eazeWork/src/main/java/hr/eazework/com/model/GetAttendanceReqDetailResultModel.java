package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 07-02-2018.
 */

public class GetAttendanceReqDetailResultModel extends GenericResponse implements Serializable {
    private AttendanceReqDetail attendanceReqDetail;

    public AttendanceReqDetail getAttendanceReqDetail() {
        return attendanceReqDetail;
    }

    public void setAttendanceReqDetail(AttendanceReqDetail attendanceReqDetail) {
        this.attendanceReqDetail = attendanceReqDetail;
    }
}
