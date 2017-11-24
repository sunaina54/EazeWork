package hr.eazework.com.model;

/**
 * Created by Manjunath on 31-03-2017.
 */

public class MappedEmployee {
    private String empName;
    private String empCode;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if (obj instanceof MappedEmployee) {

            MappedEmployee map = (MappedEmployee) obj;

            if ((map.getEmpCode() == null && empCode == null) || map.getEmpCode().equals(empCode)) {
                return true;
            }

        }
        return false;
    }
}
