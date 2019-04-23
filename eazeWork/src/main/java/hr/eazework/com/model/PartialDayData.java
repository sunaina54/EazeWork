package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class PartialDayData implements Serializable {

    private String DayFull;
    private String DayP25;
    private String DayP50;
    private String DayP75;

    public String getDayFull() {
        return DayFull;
    }

    public void setDayFull(String dayFull) {
        DayFull = dayFull;
    }

    public String getDayP25() {
        return DayP25;
    }

    public void setDayP25(String dayP25) {
        DayP25 = dayP25;
    }

    public String getDayP50() {
        return DayP50;
    }

    public void setDayP50(String dayP50) {
        DayP50 = dayP50;
    }

    public String getDayP75() {
        return DayP75;
    }

    public void setDayP75(String dayP75) {
        DayP75 = dayP75;
    }
}
