package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 09-01-2018.
 */

public class GetWFHEmpListResult extends GenericResponse implements Serializable {
    private ArrayList<EmployItem> Employees;

    public ArrayList<EmployItem> getEmployees() {
        return Employees;
    }

    public void setEmployees(ArrayList<EmployItem> employees) {
        Employees = employees;
    }
}
