package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class GetEmpWFHRequestsResultModel extends GenericResponse implements Serializable {

    private ArrayList<GetEmpWFHResponseItem> requests;

    public ArrayList<GetEmpWFHResponseItem> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<GetEmpWFHResponseItem> requests) {
        this.requests = requests;
    }
}
