package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.Chips;
import hr.eazework.com.model.LocationDetailsModel;
import hr.eazework.com.model.MappedEmployee;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.util.GeoUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ChipViewRecyclerAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 28-04-2017.
 */

public class ManageStore extends BaseFragment {
    private static final String TAG = ManageStore.class.getName();

    protected GoogleApiClient googleApiClient;
    protected LocationDetailsModel model = null;
    protected ArrayList<MappedEmployee> employees = new ArrayList<>();
    protected ArrayList<Chips> chipList = new ArrayList<>();


    protected String userLat = "";
    protected String userLong = "";
    protected String officeType = "";

    protected EditText storeName;
    protected EditText storeCode;
    protected EditText phone;
    protected EditText address1;
    protected EditText address2;
    protected EditText latitude;
    protected EditText longitude;
    protected EditText state;
    protected EditText city;
    protected EditText country;
    protected EditText pincode;
    protected  TextView tvOfficeType;
    protected ImageView tvMapEmployee;
    protected ImageView storePhoto;
    protected Preferences preferences;

    protected void initializeViews(View v) {
        preferences = new Preferences(getContext());

        int textColor = Utility.getTextColorCode(preferences);
        ((TextView)getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        storeName = (EditText) v.findViewById(R.id.etStoreName);
        tvOfficeType = (TextView) v.findViewById(R.id.etOfficeType);
        storeCode = (EditText) v.findViewById(R.id.etStoreCode);
        phone = (EditText) v.findViewById(R.id.etPhoneNum);
        address1 = (EditText) v.findViewById(R.id.etAddress1);
        address2 = (EditText) v.findViewById(R.id.etAddress2);
        city = (EditText) v.findViewById(R.id.etCity);
        country = (EditText) v.findViewById(R.id.etCoutry);
        pincode = (EditText) v.findViewById(R.id.etPincode);
        state = (EditText) v.findViewById(R.id.etState);
        latitude = (EditText) v.findViewById(R.id.etLatitude);
        longitude = (EditText) v.findViewById(R.id.etLongitude);
        tvMapEmployee = (ImageView) v.findViewById(R.id.tvMapEmployee);

        storePhoto = (ImageView) v.findViewById(R.id.ivStorePhoto);
    }

    protected void eraseFieldsData () {
        storeName.setText("");
        tvOfficeType.setText("");
        storeCode.setText("");
        phone.setText("");
        address1.setText("");
        address2.setText("");
        city.setText("");
        country.setText("");
        pincode.setText("");
        state.setText("");
        latitude.setText("");
        longitude.setText("");
        employees.clear();
        model = null;
    }


    protected void disableViews() {
        state.setEnabled(false);
        city.setEnabled(false);
        country.setEnabled(false);
        pincode.setEnabled(false);
        if (Utility.isInclusiveAndAboveMarshmallow()) {
            country.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
            pincode.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
            city.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
            state.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
        } else {
            pincode.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
            country.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
            city.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
            state.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
        }
    }

    public void onStart() {
        super.onStart();
        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }


    protected boolean addExifData(String imagePath) {
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
        boolean isExifAdded = false;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            if (exif != null) {
                Location location = GeoUtil.getLocation(getContext(), googleApiClient, this);

                if (location != null) {
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GeoUtil.convert(location.getLatitude()));
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GeoUtil.convert(location.getLongitude()));
                    if (location.getLatitude() > 0) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");

                    } else {
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
                    }

                    if (location.getLongitude() > 0) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                    } else {
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "F");
                    }
                    if (exif != null) {
                        exif.saveAttributes();
                        userLat = location.getLatitude() + "";
                        userLong = location.getLongitude() + "";
                        isExifAdded = true;
                    }
                } else {
                    isExifAdded = false;
                    ((MainActivity) getActivity()).displayMessage("GeoTag was not added");
                }
            } else {
                isExifAdded = false;
                ((MainActivity) getActivity()).displayMessage("Error in adding GeoTag");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            isExifAdded = false;
        }

        return isExifAdded;

    }


    protected void setModelForSubmission() {
        model.setmOfficeName(storeName.getText().toString());
        model.setmOfficeCode(storeCode.getText().toString());
        model.setmPhone(phone.getText().toString());
        model.setmCity(city.getText().toString());
        if (TextUtils.isEmpty(model.getmISOCodeState())) {
            model.setmISOCodeState(state.getText().toString());
        }

        if (TextUtils.isEmpty(model.getmISOCodeCountry())) {
            model.setmISOCodeCountry(country.getText().toString());
        }
        model.setmPincode(pincode.getText().toString());
        model.setmAddress1(address1.getText().toString());
        model.setmAddress2(address2.getText().toString());
        model.setmLatitude(latitude.getText().toString());
        model.setmLongitude(longitude.getText().toString());

        if (employees != null) {
            model.setMappedEmployees(employees);
        }
    }

    protected void validateGetMappedEmployeeResponse(Activity activity, final ChipViewRecyclerAdapter chipAdapter, ResponseData response) {
        JSONObject employeesMappedList;
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);

        try {
            employeesMappedList = new JSONObject(response.getResponseData());
            JSONObject mainJSONObject = employeesMappedList.optJSONObject("GetTypeWiseListResult");
            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                new AlertCustomDialog(activity, errorMessage);
            } else {
                JSONArray array = mainJSONObject.optJSONArray("list");
                TypeWiseListModel typeWiseListModel = new TypeWiseListModel(array);

                if (typeWiseListModel.getList().size() > 0) {
                    CustomBuilder employeeSelectableList = new CustomBuilder(activity, "Select Employee", true);
                    employeeSelectableList.setSingleChoiceItems(typeWiseListModel.getList(), null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            String empCode = ((TypeWiseListModel) selectedObject).getCode();
                            MappedEmployee map1 = new MappedEmployee();
                            map1.setEmpCode(empCode);
                            map1.setEmpName(((TypeWiseListModel) selectedObject).getValue());
                            map1.setFlag("A");

                            if (!employees.contains(map1)) {
                                employees.add(map1);
                            }
                            Chips c = new Chips(R.drawable.icon_profile, ((TypeWiseListModel) selectedObject).getValue(),
                                    R.drawable.ic_close, ((TypeWiseListModel) selectedObject).getCode());
                            if (!chipList.contains(c)) {
                                chipList.add(c);
                                chipAdapter.refreshData(chipList);
                            }
                            builder.dismiss();

                        }
                    });
                    employeeSelectableList.show();

                } else {
                    Utility.displayMessage(activity,"No employees");
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }



    }

}

