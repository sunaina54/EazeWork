package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class PartialDayParamsModel implements Serializable {
    private String DayFullVisible;
    private String DayP25Visible;
    private String DayP50Visible;
    private String DayP75Visible;

    public String getDayFullVisible() {
        return DayFullVisible;
    }

    public void setDayFullVisible(String dayFullVisible) {
        DayFullVisible = dayFullVisible;
    }

    public String getDayP25Visible() {
        return DayP25Visible;
    }

    public void setDayP25Visible(String dayP25Visible) {
        DayP25Visible = dayP25Visible;
    }

    public String getDayP50Visible() {
        return DayP50Visible;
    }

    public void setDayP50Visible(String dayP50Visible) {
        DayP50Visible = dayP50Visible;
    }

    public String getDayP75Visible() {
        return DayP75Visible;
    }

    public void setDayP75Visible(String dayP75Visible) {
        DayP75Visible = dayP75Visible;
    }
}
