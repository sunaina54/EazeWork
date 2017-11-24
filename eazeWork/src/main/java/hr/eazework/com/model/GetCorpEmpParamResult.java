package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 17-11-2017.
 */

public class GetCorpEmpParamResult extends GenericResponse implements Serializable{
    private ArrayList<CorpEmpParamListItem> CorpEmpParamList;

    public ArrayList<CorpEmpParamListItem> getCorpEmpParamList() {
        return CorpEmpParamList;
    }

    public void setCorpEmpParamList(ArrayList<CorpEmpParamListItem> corpEmpParamList) {
        CorpEmpParamList = corpEmpParamList;
    }
}
