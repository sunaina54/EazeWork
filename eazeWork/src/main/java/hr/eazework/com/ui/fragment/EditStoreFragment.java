package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.Chips;
import hr.eazework.com.model.LocationDetailsModel;
import hr.eazework.com.model.MappedEmployee;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.OnRemoveListener;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
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
 * Created by Manjunath on 28-03-2017.
 */

public class EditStoreFragment extends ManageStore implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static String TAG = "EditStoreFragment";
    private String officeId = "";
    private GoogleApiClient googleApiClient;
    private RecyclerView rvTest;
    private boolean isSubmitClicked = true;
    private ChipViewRecyclerAdapter chipAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowEditTeamButtons(true);
        this.setShowPlusMenu(false);
        try {
            officeId = getArguments().getString(AppsConstant.OFFICE_ID, "");
        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_store, container, false);
        initializeViews(rootView);
        requestAPI();
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
        rvTest.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.margin_10),
                getResources().getDimensionPixelOffset(R.dimen.margin_10)));

        rvTest.setAdapter(chipAdapter);
        storePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (model != null && !TextUtils.isEmpty(model.getmPhoto())) {
                    String photoFromApi = model.getmPhoto();
                    StorePhotoFragment f = new StorePhotoFragment();
                    FragmentManager fm = getFragmentManager();
                    Bundle bundle = new Bundle();
                    if (photoFromApi.equalsIgnoreCase("null")) {
                        bundle.putString("store_photo_local", model.getmLocalPhoto());
                        f.setArguments(bundle);
                    } else {
                        bundle.putString("store_photo", photoFromApi);
                        f.setArguments(bundle);
                    }
                    f.show(fm, "Photo_Dialog");
                } else {
                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext())) {
                        PermissionUtil.askAllPermission(EditStoreFragment.this);
                    }

                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                        if (Utility.isNetworkAvailable(getContext())) {
                            Utility.openCamera(getActivity(), EditStoreFragment.this, AppsConstant.BACK_CAMREA_OPEN, "ForStore");
                        } else {
                            new AlertCustomDialog(getContext(), getString(R.string.msg_internet_connection));
                        }
                    } else {
                        Utility.displayMessage(getContext(), "Please provide all permission");
                    }
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
                ((MainActivity) getActivity()).showHideProgress(true);
                CommunicationManager.getInstance().sendPostRequest(new IBaseResponse() {
                    @Override
                    public void validateResponse(ResponseData response) {
                        validateGetMappedEmployeeResponse(getActivity(), chipAdapter, response);
                    }

                }, AppRequestJSONString.getCommonService("ddLocationMapEligibleEmpList", officeId), CommunicationConstant.API_GET_TYPE_WISE_LIST, false);

            }
        });

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Edit Location");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phone.getText().toString())) {

                    submitLocationData();


                } else {
                    ((MainActivity) getActivity()).displayMessage("Please enter phone number");
                }

            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrCancel();
            }
        });


        return rootView;
    }

    public void saveOrCancel() {
        MenuItemModel model = ModelManager.getInstance().getMenuItemModel().getItemModel(MenuItemModel.LOCATION_KEY);
        if (model != null && model.isAccess()) {
            mUserActionListener.performUserAction(IAction.STORE_LIST_VIEW, null, null);
        } else {
            mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
        }
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
                storePhoto.setImageBitmap(ImageUtil.decodeImageToBitmap(strFile));
                model.setmPhoto("null");
                model.setmLocalPhoto(strFile);
                model.setmBinary(ImageUtil.encodeImage(strFile));
            }
        }
    }

    private void submitLocationData() {
        if (model != null && isSubmitClicked) {
            setModelForSubmission();
            isSubmitClicked = false;
            String flag = "E";
            if (!TextUtils.isEmpty(model.getmBinary())) {
                flag = "C";
            }
            MainActivity.isAnimationLoaded = false;
            ((MainActivity) getActivity()).showHideProgress(true);
            CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.UpdateLocationData(model, flag)
                    , CommunicationConstant.API_GET_LOCATION_UPDATE, false);
        }
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity) getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getLocationDetails(officeId),
                CommunicationConstant.API_GET_LOCATION_DETAIL, false);
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        ((MainActivity) getActivity()).showHideProgress(false);
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_LOCATION_DETAIL:
                JSONObject json;
                try {
                    json = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = json
                            .optJSONObject("GetLocationDetailsResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                        isSubmitClicked = true;
                    } else {
                        JSONObject jsonObj = new JSONObject(json.optJSONObject("GetLocationDetailsResult").toString()).optJSONObject("locationDetail");
                        populateViews(jsonObj.toString());
                    }

                } catch (JSONException e) {
                    isSubmitClicked = true;
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
                break;

            case CommunicationConstant.API_GET_LOCATION_UPDATE:

                JSONObject json2;
                try {
                    json2 = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = json2
                            .optJSONObject("UpdateLocationResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        ((MainActivity) getActivity()).displayMessage("Location updated");
                        saveOrCancel();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }

                break;

        }

        super.validateResponse(response);

    }

    private void populateViews(String json) {
        LocationDetailsModel model = new LocationDetailsModel(json);
        this.model = model;
        ///      getOfficeType();
        getMappedEmployee();
        Picasso.with(getContext()).load(CommunicationConstant.getMobileCareURl() + model.getmPhoto())
                .fit().placeholder(R.drawable.camera)
                .into(storePhoto);

        storeName.setText(model.getmOfficeName());
        disableViews();
        if (model.getmOfficeCodeSM().equalsIgnoreCase("S")) {
            storeCode.setText(model.getmOfficeCode());
            storeCode.setEnabled(false);

            if (Utility.isInclusiveAndAboveMarshmallow()) {
                storeCode.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
            } else {
                storeCode.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
            }
        } else if (model.getmOfficeCodeSM().equalsIgnoreCase("M")) {
            storeCode.setText(model.getmOfficeCode());
            storeCode.setEnabled(true);

            if (Utility.isInclusiveAndAboveMarshmallow()) {
                storeCode.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
            } else {
                storeCode.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        }
        storeCode.setText(model.getmOfficeCode());

        if (model.getmStateCode().equalsIgnoreCase("E004000000")) {
            state.setText(model.getmStateOther());
        } else {
            state.setText(model.getmState());
        }
        tvOfficeType.setText(model.getmOfficeTypeDescription());
        this.model.setmOfficeType(model.getmOfficeType());
        tvOfficeType.setEnabled(false);
        if (Utility.isInclusiveAndAboveMarshmallow()) {
            tvOfficeType.setBackgroundColor(getResources().getColor(R.color.lighter_gray, null));
        } else {
            tvOfficeType.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
        }
        pincode.setText(model.getmPincode());
        country.setText(model.getmCountry());
        city.setText(model.getmCity());
        phone.setText(model.getmPhone());
        address1.setText(model.getmAddress1());
        address2.setText(model.getmAddress2());
        latitude.setText(model.getmLatitude());
        longitude.setText(model.getmLongitude());
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

    private void getMappedEmployee() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity) getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(
                new IBaseResponse() {
                    @Override
                    public void validateResponse(ResponseData response) {
                        JSONObject json3;
                        try {
                            json3 = new JSONObject(response.getResponseData());
                            JSONObject mainJSONObject = json3.optJSONObject("GetTypeWiseListResult");
                            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                new AlertCustomDialog(getActivity(), errorMessage);
                            } else {
                                JSONArray array = mainJSONObject.optJSONArray("list");
                                TypeWiseListModel typeWiseListModel = new TypeWiseListModel(array);
                                if (typeWiseListModel != null) {
                                    for (int i = 0; i < typeWiseListModel.getList().size(); i++) {
                                        TypeWiseListModel m = typeWiseListModel.getList().get(i);

                                        MappedEmployee map1 = new MappedEmployee();
                                        map1.setEmpCode(m.getCode());
                                        map1.setEmpName(m.getValue());
                                        map1.setFlag("A");

                                        if (!employees.contains(map1)) {
                                            employees.add(map1);
                                        }

                                        Chips chip = new Chips(R.drawable.icon_profile, m.getValue(), R.drawable.ic_close, m.getCode());
                                        if (!chipList.contains(chip)) {
                                            chipList.add(chip);
                                        }
                                    }
                                    chipAdapter.refreshData(chipList);
                                }
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage(), e);
                            Crashlytics.logException(e);
                        }
                        MainActivity.isAnimationLoaded = true;
                        ((MainActivity) getActivity()).showHideProgress(false);
                    }
                }, AppRequestJSONString.getCommonService("ddLocationMappedEmpList", officeId),
                CommunicationConstant.API_GET_TYPE_WISE_LIST, false);

    }
}
