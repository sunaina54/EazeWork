package hr.eazework.com.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Manjunath on 27-03-2017.
 */

public class Chips {

    private Integer drawableResId;
    private Integer close;
    private String description;
    private String name;
    private String code;

    public Chips() {

    }

    public Chips(Integer drawableResId, String description, Integer close , String code) {
        this.drawableResId = drawableResId;
        this.description = description;
        this.close = close;
        this.code = code;
    }

    /*public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
*/

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if (obj instanceof Chips) {

            Chips chip = (Chips) obj;

            if ((chip.getDescription() == null && description == null) || (chip.getDescription().equals(description) &&
                    ((chip.getCode() == null && code == null) || chip.getCode().equals(code) ))) {
                return true;
            }

        }
        return false;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(Integer drawableResId) {
        this.drawableResId = drawableResId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
