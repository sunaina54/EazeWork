package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 18-01-2018.
 */

public class GetODRequestDetailResult extends GenericResponse implements Serializable {
    private ODRequestDetail ODRequestDetail;

    public hr.eazework.com.model.ODRequestDetail getODRequestDetail() {
        return ODRequestDetail;
    }

    public void setODRequestDetail(hr.eazework.com.model.ODRequestDetail ODRequestDetail) {
        this.ODRequestDetail = ODRequestDetail;
    }
}
