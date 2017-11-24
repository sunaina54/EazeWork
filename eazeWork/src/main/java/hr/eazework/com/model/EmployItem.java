package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by PSQ on 11/12/2017.
 */

public class EmployItem implements Serializable {
    private String EmpCode;
    private Long  EmpID;
    private String Name;

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public Long getEmpID() {
        return EmpID;
    }

    public void setEmpID(Long empID) {
        EmpID = empID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
