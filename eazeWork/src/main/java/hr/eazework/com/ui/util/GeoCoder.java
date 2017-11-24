package hr.eazework.com.ui.util;

import android.os.AsyncTask;

import hr.eazework.com.model.GeoCoderModel;
import hr.eazework.com.model.LocationDetailsModel;

/**
 * Created by Manjunath on 28-04-2017.
 */

public class GeoCoder extends AsyncTask<String, Void, String> {
    private LocationDetailsModel model;
    protected GeoCoderModel address;

    @Override
    protected String doInBackground(String... params) {
        return GeoUtil.fetchAddressFromGeoCoder(params);
    }


    @Override
    protected void onPostExecute(String geoCoderResponse) {
        super.onPostExecute(geoCoderResponse);
        address = GeoUtil.getGeoCoderModel(geoCoderResponse);
        if(model!=null && address != null) {
            model.setFieldFromGeoCoder(address);
        }
    }

    public LocationDetailsModel getModel() {
        return model;
    }

    public void setModel(LocationDetailsModel model) {
        this.model = model;
    }

    public GeoCoderModel getAddress() {
        return address;
    }
}
