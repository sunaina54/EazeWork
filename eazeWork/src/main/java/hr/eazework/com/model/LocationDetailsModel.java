package hr.eazework.com.model;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manjunath on 30-03-2017.
 */

public class LocationDetailsModel extends BaseAppResponseModel {

    private String mOfficeName;
    private String mOfficeCode;
    private String mPhone;
    private String mAddress1;
    private String mAddress2;
    private String mGeoLocation;
    private String mLatitude;
    private String mLongitude;
    private String mPhoto;
    private boolean mActive;
    private int mGeoRadius;
    private String mOfficeType;
    private String mOfficeTypeDescription;
    private String mOfficeId;
    private String mCity;
    private String mPincode;
    private String mCountryCode;
    private String mStateCode;
    private String mStateOther;
    private String mBinary;
    private ArrayList<MappedEmployee> mappedEmployees;

    private String mCompanyID;
    private String mCountry;
    private String mCreatedBy;
    private String mCreatedByEmpID;
    private String mFileInfo;
    private String mISOCodeCountry;
    private String mISOCodeState;
    private String mOfficeCodeSM;
    private String mParentOfficeID;
    private String mSourceID;
    private String mState;
    private String mLocalPhoto;


    public LocationDetailsModel() {
    }

    public LocationDetailsModel(String json) {
        try {
            JSONObject parent = new JSONObject(json);
            mActive = parent.optString("Active", "N").equalsIgnoreCase("Y");
            mOfficeName = parent.optString("OfficeName", "");
            mOfficeCode = parent.optString("OfficeCode", "");
            mPhone = parent.optString("PhoneNo", "");
            mAddress1 = parent.optString("Address1", "");
            mAddress2 = parent.optString("Address2", "");
            mGeoLocation = parent.optString("mGeoLocation", "");
            mLongitude = parent.optString("Longitude", "");
            mLatitude = parent.optString("Latitude", "");
            mPhoto = parent.optString("Photo", "");
            mGeoRadius = parent.optInt("GeoRadius", 0);
            mOfficeTypeDescription = parent.optString("OfficeTypeDesc", "");
            mOfficeId = parent.optString("OfficeID", "");
            mOfficeType = parent.optString("OfficeType", "");
            mCity = parent.optString("City", "");
            mPincode = parent.optString("PinCode", "");
            mCountryCode = parent.optString("CountryCode", "");
            mStateCode = parent.optString("StateCode", "");
            mStateOther = parent.optString("StateOther", "");
            mCompanyID = parent.optString("mCompanyID", "");
            mCountry = parent.optString("Country", "");
            mCreatedBy = parent.optString("CreatedBy", "");
            mCreatedByEmpID = parent.optString("CreatedByEmpID", "");
            mFileInfo = parent.optString("FileInfo", "");
            mISOCodeCountry = parent.optString("ISOCodeCountry", "");
            mISOCodeState = parent.optString("ISOCodeState", "");
            mOfficeCodeSM = parent.optString("OfficeCodeSM", "");
            mParentOfficeID = parent.optString("ParentOfficeID", "");
            mSourceID = parent.optString("SourceID", "");
            mState = parent.optString("State", "");

        } catch (JSONException e) {
            Crashlytics.log(e.getMessage());
        }
    }


    public String getmLocalPhoto() {
        return mLocalPhoto;
    }

    public void setmLocalPhoto(String mLocalPhoto) {
        this.mLocalPhoto = mLocalPhoto;
    }

    public String getmOfficeName() {
        return mOfficeName;
    }

    public void setmOfficeName(String mOfficeName) {
        this.mOfficeName = mOfficeName;
    }

    public String getmOfficeCode() {
        return mOfficeCode;
    }

    public void setmOfficeCode(String mOfficeCode) {
        this.mOfficeCode = mOfficeCode;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public void setmAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }

    public String getmAddress2() {
        return mAddress2;
    }

    public void setmAddress2(String mAddress2) {
        this.mAddress2 = mAddress2;
    }

    public String getmGeoLocation() {
        return mGeoLocation;
    }

    public void setmGeoLocation(String mGeoLocation) {
        this.mGeoLocation = mGeoLocation;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public boolean ismActive() {
        return mActive;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
    }

    public int getmGeoRadius() {
        return mGeoRadius;
    }

    public void setmGeoRadius(int mGeoRadius) {
        this.mGeoRadius = mGeoRadius;
    }

    public String getmOfficeType() {
        return mOfficeType;
    }

    public void setmOfficeType(String mOfficeType) {
        this.mOfficeType = mOfficeType;
    }

    public String getmOfficeTypeDescription() {
        return mOfficeTypeDescription;
    }

    public void setmOfficeTypeDescription(String mOfficeTypeDescription) {
        this.mOfficeTypeDescription = mOfficeTypeDescription;
    }

    public String getmOfficeId() {
        return mOfficeId;
    }

    public void setmOfficeId(String mOfficeId) {
        this.mOfficeId = mOfficeId;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmPincode() {
        return mPincode;
    }

    public void setmPincode(String mPincode) {
        this.mPincode = mPincode;
    }

    public String getmCountryCode() {
        return mCountryCode;
    }

    public void setmCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getmCreatedBy() {
        return mCreatedBy;
    }

    public void setmCreatedBy(String mCreatedBy) {
        this.mCreatedBy = mCreatedBy;
    }

    public String getmCreatedByEmpID() {
        return mCreatedByEmpID;
    }

    public void setmCreatedByEmpID(String mCreatedByEmpID) {
        this.mCreatedByEmpID = mCreatedByEmpID;
    }

    public String getmFileInfo() {
        return mFileInfo;
    }

    public void setmFileInfo(String mFileInfo) {
        this.mFileInfo = mFileInfo;
    }

    public String getmISOCodeCountry() {
        return mISOCodeCountry;
    }

    public void setmISOCodeCountry(String mISOCodeCountry) {
        this.mISOCodeCountry = mISOCodeCountry;
    }

    public String getmISOCodeState() {
        return mISOCodeState;
    }

    public void setmISOCodeState(String mISOCodeState) {
        this.mISOCodeState = mISOCodeState;
    }

    public String getmOfficeCodeSM() {
        return mOfficeCodeSM;
    }

    public void setmOfficeCodeSM(String mOfficeCodeSM) {
        this.mOfficeCodeSM = mOfficeCodeSM;
    }

    public String getmParentOfficeID() {
        return mParentOfficeID;
    }

    public void setmParentOfficeID(String mParentOfficeID) {
        this.mParentOfficeID = mParentOfficeID;
    }

    public String getmSourceID() {
        return mSourceID;
    }

    public void setmSourceID(String mSourceID) {
        this.mSourceID = mSourceID;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmStateCode() {
        return mStateCode;
    }

    public void setmStateCode(String mStateCode) {
        this.mStateCode = mStateCode;
    }

    public String getmStateOther() {
        return mStateOther;
    }

    public void setmStateOther(String mStateOther) {
        this.mStateOther = mStateOther;
    }

    public String getmBinary() {
        return mBinary;
    }

    public void setmBinary(String mBinary) {
        this.mBinary = mBinary;
    }

    public ArrayList<MappedEmployee> getMappedEmployees() {
        return mappedEmployees;
    }

    public void setMappedEmployees(ArrayList<MappedEmployee> mappedEmployees) {
        this.mappedEmployees = mappedEmployees;
    }

    public String getmCompanyID() {
        return mCompanyID;
    }

    public void setmCompanyID(String mCompanyID) {
        this.mCompanyID = mCompanyID;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public void setFieldFromGeoCoder(GeoCoderModel address) {
        this.setmPincode(address.getmPincode());
        this.setmGeoLocation(address.getmCompleteAddress());


        this.setmAddress1(address.getmAddress1());

        if (!TextUtils.isEmpty(address.getmCountryCode())) {
            this.setmISOCodeCountry(address.getmCountryCode());
        } else {
            if (!TextUtils.isEmpty(address.getmCountry())) {
                this.setmISOCodeCountry(address.getmCountry());
            }
        }

        if (!TextUtils.isEmpty(address.getmStateCode())) {
            this.setmISOCodeState(address.getmStateCode());
        } else {
            if (!TextUtils.isEmpty(address.getmState())) {
                this.setmISOCodeState(address.getmState());
            }
        }
        if (!TextUtils.isEmpty(address.getmCity())) {
            this.setmCity(address.getmCity());
        }
    }

}
