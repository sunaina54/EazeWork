package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 16-01-2018.
 */

public class GetTourRequestCustomFieldListResult extends GenericResponse implements Serializable {

    private ArrayList<CustomFieldsModel> CustomFields;

    public ArrayList<CustomFieldsModel> getCustomFields() {
        return CustomFields;
    }

    public void setCustomFields(ArrayList<CustomFieldsModel> customFields) {
        CustomFields = customFields;
    }
}
