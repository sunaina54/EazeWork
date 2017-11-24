package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.Chips;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.LocationDetailsModel;
import hr.eazework.com.model.MappedEmployee;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.OnRemoveListener;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.GeoCoder;
import hr.eazework.com.ui.util.GeoUtil;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.StringUtils;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ChipViewRecyclerAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;


/**
 * Created by Manjunath on 20-03-2017.
 */

public class CreateStoreFragment extends ManageStore implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = "CreateStoreFragment";
    private ArrayList<TypeWiseListModel> officeTypeList = new ArrayList<>();
    private RecyclerView rvTest;
    private ChipViewRecyclerAdapter chipAdapter;
    private boolean isSubmitClicked = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.location_creation_screen, container, false);
        initializeViews(rootView);
        model = new LocationDetailsModel();
        isSubmitClicked = true;
        rvTest = (RecyclerView) rootView.findViewById(R.id.rvTest);

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setScrollingEnabled(true)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        chipAdapter = new ChipViewRecyclerAdapter(getContext(), chipList, new OnRemoveListener() {
            @Override
            public void onItemRemoved(int position) {
                Chips c = chipAdapter.getItem(position);
                MappedEmployee m = new MappedEmployee();
                m.setEmpCode(c.getCode());
                m.setFlag("R");
                m.setEmpName(c.getDescription());
                employees.add(m);
                chipAdapter.RemoveItem(position);
            }
        });
        rvTest.setLayoutManager(chipsLayoutManager);
        rvTest.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.margin_10), getResources().getDimensionPixelOffset(R.dimen.margin_10)));

        rvTest.setAdapter(chipAdapter);
        tvOfficeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (officeTypeList != null) {
                    CustomBuilder typeWiseLocationList = new CustomBuilder(getContext(), "Select Location", true);
                    typeWiseLocationList.setSingleChoiceItems(officeTypeList, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            String code = ((TypeWiseListModel) selectedObject).getCode();
                            officeType = code;
                            tvOfficeType.setText(((TypeWiseListModel) selectedObject).getValue());
                            builder.dismiss();

                        }
                    });
                    typeWiseLocationList.show();

                }
            }
        });

        EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();

        if (employeeProfileModel != null) {
            String OfficeCodeSM = employeeProfileModel.getmOfficeCodeSM();
            if (!TextUtils.isEmpty(OfficeCodeSM) && OfficeCodeSM.equalsIgnoreCase("S")) {
                storeCode.setEnabled(false);
                if (Utility.isInclusiveAndAboveMarshmallow()) {
                    storeCode.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
                } else {
                    storeCode.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                }
            } else {
                storeCode.setEnabled(true);
                if (Utility.isInclusiveAndAboveMarshmallow()) {
                    storeCode.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    storeCode.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }
        }


        storePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !PermissionUtil.checkLocationPermission(getContext())) {
                    PermissionUtil.askAllPermission(CreateStoreFragment.this);
                }
                if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && PermissionUtil.checkLocationPermission(getContext())) {
                    if (Utility.isLocationEnabled(getContext())) {
                        if (Utility.isNetworkAvailable(getContext())) {
                            Utility.openCamera(getActivity(), CreateStoreFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore");
                        } else {
                            Utility.showNetworkNotAvailableDialog(getContext());
                        }
                    } else {
                        Utility.requestToEnableGPS(getContext(), new Preferences(getContext()));
                    }
                } else {
                    Utility.displayMessage(getContext(), "Please provide all permission");
                }
            }
        });

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext()).
                    addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).
                    addApi(LocationServices.API).build();
        }

        tvMapEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonPostParam = AppRequestJSONString.getCommonService("ddLocationMapEligibleEmpList", "0");
                ((MainActivity) getActivity()).showHideProgress(true);
                MainActivity.isAnimationLoaded = false;
                CommunicationManager.getInstance().sendPostRequest(new IBaseResponse() {
                    @Override
                    public void validateResponse(ResponseData response) {
                        validateGetMappedEmployeeResponse(getActivity(), chipAdapter, response);
                    }
                }, jsonPostParam, CommunicationConstant.API_GET_TYPE_WISE_LIST, false);

            }
        });

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Create Location");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(storeName.getText().toString()) ||
                        TextUtils.isEmpty(phone.getText().toString()) ||
                        TextUtils.isEmpty(tvOfficeType.getText().toString()) ||
                        TextUtils.isEmpty(state.getText().toString()) ||
                        TextUtils.isEmpty(address1.getText().toString()) ||
                        TextUtils.isEmpty(latitude.getText().toString()) ||
                        TextUtils.isEmpty(longitude.getText().toString())) {
                    Utility.displayMessage(getContext(), "Please fill all the fields");
                } else {
                    submitLocationData("C");
                }
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenuItemModel model = ModelManager.getInstance().getMenuItemModel();
                if (model != null) {
                    MenuItemModel itemModel = model.getItemModel(MenuItemModel.LOCATION_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.STORE_LIST_VIEW, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                    }
                }
                eraseFieldsData();
                chipList.clear();

                chipAdapter.refreshData(chipList);
            }
        });
        disableViews();
        getOfficeType();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppsConstant.REQ_CAMERA) {
                final String strFile = data.getStringExtra("response");
                if (addExifData(strFile)) {
                    storePhoto.setImageBitmap(ImageUtil.decodeImageToBitmap(strFile));
                    model.setmBinary(ImageUtil.encodeImage(strFile));

                    latitude.setText(userLat);
                    longitude.setText(userLong);
                    getPostalCodeByCoordinates(StringUtils.getDouble(userLat), StringUtils.getDouble(userLong));
                } else {
                    ((MainActivity) getActivity()).displayMessage("GeoTag not added please take again");
                }
            }
        }
    }


    @Override
    public void validateResponse(ResponseData response) {

        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {

            case CommunicationConstant.API_GET_LOCATION_UPDATE:

                JSONObject updateLocationResponse;
                try {
                    updateLocationResponse = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = updateLocationResponse.optJSONObject("UpdateLocationResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                        isSubmitClicked = true;
                    } else {
                        Utility.displayMessage(getContext(), "Location created");
                        MenuItemModel model = ModelManager.getInstance().getMenuItemModel().getItemModel(MenuItemModel.LOCATION_KEY);
                        if (model != null && model.isAccess()) {
                            mUserActionListener.performUserAction(IAction.STORE_LIST_VIEW, null, null);
                        } else {
                            mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                        }

                        eraseFieldsData();
                        chipList.clear();
                        chipAdapter.refreshData(chipList);
                    }

                } catch (JSONException e) {
                    isSubmitClicked = true;
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
                break;
        }
        super.validateResponse(response);
        MainActivity.isAnimationLoaded = true;
        ((MainActivity) getActivity()).showHideProgress(false);
    }

    private void getPostalCodeByCoordinates(double lat, double lon) {
        GeoCoderOnLocation coder = new GeoCoderOnLocation();
        coder.setModel(model);
        coder.execute(GeoUtil.getGeoCoderUrl(lat, lon));
    }

    private class GeoCoderOnLocation extends GeoCoder {
        @Override
        protected void onPostExecute(String geoCoderResponse) {
            //super will parse the response and set to address
            super.onPostExecute(geoCoderResponse);

            pincode.setText(address.getmPincode());
            country.setText(address.getmCountry());
            city.setText(address.getmCity());
            state.setText(address.getmState());
            address1.setText(address.getmAddress1());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getOfficeType() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity) getActivity()).showHideProgress(true);
        String jsonPostParam = AppRequestJSONString.getCommonService("ddLocationTypeSMList", "");
        CommunicationManager.getInstance().sendPostRequest(new IBaseResponse() {
            @Override
            public void validateResponse(ResponseData response) {
                ((MainActivity) getActivity()).showHideProgress(false);
                MainActivity.isAnimationLoaded = true;
                JSONObject officeTypeResponse;
                try {
                    officeTypeResponse = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = officeTypeResponse.optJSONObject("GetTypeWiseListResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        JSONArray array = mainJSONObject.optJSONArray("list");
                        TypeWiseListModel typeWiseListModel = new TypeWiseListModel(array);
                        if (typeWiseListModel.getList().size() == 1) {
                            TypeWiseListModel selectedOfficeType = typeWiseListModel.getList().get(0);
                            officeType = selectedOfficeType.getCode();
                            model.setmOfficeType(officeType);
                            tvOfficeType.setText(selectedOfficeType.getValue());
                            tvOfficeType.setEnabled(false);
                            if (Utility.isInclusiveAndAboveMarshmallow()) {
                                tvOfficeType.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
                            } else {
                                tvOfficeType.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                            }
                        } else {
                            officeTypeList = typeWiseListModel.getList();
                            tvOfficeType.setEnabled(true);
                            if (Utility.isInclusiveAndAboveMarshmallow()) {
                                tvOfficeType.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
                            } else {
                                tvOfficeType.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
            }
        }, jsonPostParam, CommunicationConstant.API_GET_TYPE_WISE_LIST, false);

    }

    private void submitLocationData(String mode) {
        model.setmOfficeId("0");
        model.setmOfficeType(officeType);
        setModelForSubmission();
        if (model != null && isSubmitClicked) {
            isSubmitClicked = false;
            MainActivity.isAnimationLoaded = false;
            ((MainActivity) getActivity()).showHideProgress(true);
            CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.UpdateLocationData(model, mode), CommunicationConstant.API_GET_LOCATION_UPDATE, false);
        }
    }

}

