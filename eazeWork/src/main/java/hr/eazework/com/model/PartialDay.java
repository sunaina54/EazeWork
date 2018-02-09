package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class PartialDay implements Serializable {
    private PartialDayData PartialDayData;
    private PartialDayParamsModel PartialDayParams;

    public hr.eazework.com.model.PartialDayData getPartialDayData() {
        return PartialDayData;
    }

    public void setPartialDayData(hr.eazework.com.model.PartialDayData partialDayData) {
        PartialDayData = partialDayData;
    }

    public PartialDayParamsModel getPartialDayParams() {
        return PartialDayParams;
    }

    public void setPartialDayParams(PartialDayParamsModel partialDayParams) {
        PartialDayParams = partialDayParams;
    }
}
