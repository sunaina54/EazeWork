package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 22-01-2018.
 */

public class GetTourRequestDetailResult extends GenericResponse implements Serializable {

    private TourSummaryRequestDetail TourRequestDetail;

    public TourSummaryRequestDetail getTourRequestDetail() {
        return TourRequestDetail;
    }

    public void setTourRequestDetail(TourSummaryRequestDetail tourRequestDetail) {
        TourRequestDetail = tourRequestDetail;
    }

}
