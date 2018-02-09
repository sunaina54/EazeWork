package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class GetWFHRequestDetailResult extends GenericResponse implements Serializable {
    private WFHRequestDetailItem WFHRequestDetail;

    public WFHRequestDetailItem getWFHRequestDetail() {
        return WFHRequestDetail;
    }

    public void setWFHRequestDetail(WFHRequestDetailItem WFHRequestDetail) {
        this.WFHRequestDetail = WFHRequestDetail;
    }
}
