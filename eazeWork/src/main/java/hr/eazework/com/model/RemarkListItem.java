package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class RemarkListItem implements Serializable {

    private String Date;
    private String Name;
    private String Remark;
    private String Status;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
