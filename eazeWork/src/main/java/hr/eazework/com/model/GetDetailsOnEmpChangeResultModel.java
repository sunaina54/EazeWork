package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class GetDetailsOnEmpChangeResultModel extends GenericResponse implements Serializable {
    private String ShowReasonYN;
    private String ShowTravelYN;
    private ArrayList<TourReasonListModel> TourReasonList;

    public String getShowReasonYN() {
        return ShowReasonYN;
    }

    public void setShowReasonYN(String showReasonYN) {
        ShowReasonYN = showReasonYN;
    }

    public String getShowTravelYN() {
        return ShowTravelYN;
    }

    public void setShowTravelYN(String showTravelYN) {
        ShowTravelYN = showTravelYN;
    }

    public ArrayList<TourReasonListModel> getTourReasonList() {
        return TourReasonList;
    }

    public void setTourReasonList(ArrayList<TourReasonListModel> tourReasonList) {
        TourReasonList = tourReasonList;
    }
}
