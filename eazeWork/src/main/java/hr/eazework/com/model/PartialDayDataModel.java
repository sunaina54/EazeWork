package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 11-01-2018.
 */

public class PartialDayDataModel implements Serializable {
    private String DayP50;
    private String DayFull;

    public String getDayP50() {
        return DayP50;
    }

    public void setDayP50(String dayP50) {
        DayP50 = dayP50;
    }

    public String getDayFull() {
        return DayFull;
    }

    public void setDayFull(String dayFull) {
        DayFull = dayFull;
    }
}

