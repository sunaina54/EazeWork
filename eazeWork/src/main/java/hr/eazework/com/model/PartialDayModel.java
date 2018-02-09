package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 11-01-2018.
 */

public class PartialDayModel implements Serializable {
    private PartialDayDataModel PartialDayData;

    public PartialDayDataModel getPartialDayData() {
        return PartialDayData;
    }

    public void setPartialDayData(PartialDayDataModel partialDayData) {
        PartialDayData = partialDayData;
    }
}
