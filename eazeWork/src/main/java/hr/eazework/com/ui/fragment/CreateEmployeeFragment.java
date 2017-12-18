package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.FileInfoModel;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.model.MemberReqInputModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.OnImageClickListner;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.EditTeamAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

import static hr.eazework.com.ui.util.ImageUtil.encodeImage;

/**
 * Created by Manjunath on 20-03-2017.
 */

public class CreateEmployeeFragment extends BaseFragment {

    public static String TAG = "CreateEmployeeFragment";

    private ArrayList<MemberReqInputModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditTeamAdapter adapter;
    private String imagePurpose = "";
    private int currentPosition = 0;
    ArrayList<MemberReqInputModel> reqInputModels;
    private boolean isSubmitClicked = true;
    private Preferences preference;
    private static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity) getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(
                new IBaseResponse() {
                    @Override
                    public void validateResponse(ResponseData response) {
                        JSONObject json;
                        try {
                            json = new JSONObject(response.getResponseData());
                            JSONObject mainJSONObject = json.optJSONObject("GetMemberReqInputFieldsResult");
                            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                new AlertCustomDialog(getActivity(), errorMessage);
                            } else {
                                JSONArray array = new JSONObject(json.optJSONObject("GetMemberReqInputFieldsResult").toString()).optJSONArray("ReqFields");
                                populateViews(array);
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage(), e);
                            Crashlytics.logException(e);
                        }
                        MainActivity.isAnimationLoaded = true;
                        ((MainActivity) getActivity()).showHideProgress(false);
                    }
                }, AppRequestJSONString.getMemberRequestInpFields("0")
                , CommunicationConstant.API_GET_MEMBER_INPUT_FIELD, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.employee_creation_screen, container, false);
        context = getContext();
        if(preference == null) {
            preference = new Preferences(getContext());
        }
        int textColor = Utility.getTextColorCode(new Preferences(getContext()));
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.editProfileRV);
        adapter = new EditTeamAdapter((MainActivity) getActivity(), list, new OnImageClickListner() {
            @Override
            public void capturePhoto(final int position, String purpose, String fieldCode) {
                if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext())) {
                    PermissionUtil.askAllPermissionCamera(CreateEmployeeFragment.this);
                }
                if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext())) {
                    if (Utility.isNetworkAvailable(getContext())) {
                        currentPosition = position;

                        String fieldTypeID = adapter.getItemList().get(position).getmFieldTypeID();
                        purpose="ForStore";
                        if (fieldTypeID.equalsIgnoreCase("99") || fieldTypeID.equalsIgnoreCase("66")) {
                            Utility.openCamera(getActivity(), CreateEmployeeFragment.this, AppsConstant.BACK_CAMREA_OPEN, purpose,TAG);
                            if (fieldTypeID.equalsIgnoreCase("99")) {
                                adapter.getItemList().get(position).setmFieldValue("0");
                            }
                        } else {
                            MainActivity.isAnimationLoaded = false;
                            ((MainActivity) getActivity()).showHideProgress(true);
                            CommunicationManager.getInstance().sendPostRequest(
                                    new IBaseResponse() {
                                        @Override
                                        public void validateResponse(ResponseData response) {
                                            try {
                                                JSONObject imageTypeWiseList = new JSONObject(response.getResponseData());
                                                JSONObject mainJSONObject = imageTypeWiseList.optJSONObject("GetTypeWiseListResult");
                                                if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                    String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                    new AlertCustomDialog(getContext(), errorMessage);
                                                } else {

                                                    JSONArray array = mainJSONObject.optJSONArray("list");
                                                    TypeWiseListModel m = new TypeWiseListModel(array);
                                                    String dialogHeaderText = "Select " + adapter.getItemList().get(position).getmFieldLabel();
                                                    CustomBuilder imageTypeWiseBuilder = new CustomBuilder(getContext(), dialogHeaderText, true);
                                                    imageTypeWiseBuilder.setSingleChoiceItems(m.getList(), null, new CustomBuilder.OnClickListener() {
                                                        @Override
                                                        public void onClick(CustomBuilder builder, Object selectedObject) {
                                                            String code = ((TypeWiseListModel) selectedObject).getCode();
                                                            String value = ((TypeWiseListModel) selectedObject).getValue();
                                                            adapter.getItemList().get(currentPosition).setmFieldValue(code);
                                                            adapter.getItemList().get(currentPosition).getmFileInfoModel().setmText(value);
                                                            String purpose ="ForStore" ;//((TypeWiseListModel) selectedObject).getValue().trim();
                                                            Utility.openCamera(getActivity(), CreateEmployeeFragment.this, AppsConstant.BACK_CAMREA_OPEN, purpose,TAG);
                                                            builder.dismiss();
                                                        }
                                                    });
                                                    imageTypeWiseBuilder.show();
                                                }
                                            } catch (JSONException e) {
                                                Log.e(TAG, e.getMessage(), e);
                                                Crashlytics.logException(e);
                                            }
                                            MainActivity.isAnimationLoaded = true;
                                            ((MainActivity) getActivity()).showHideProgress(false);
                                        }


                                    }, AppRequestJSONString.getCommonService("FieldCode", fieldCode), CommunicationConstant.API_GET_TYPE_WISE_LIST, false);

                        }


                    } else {
                        new AlertCustomDialog(getContext(), getString(R.string.msg_internet_connection));
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        requestAPI();
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Create Employee");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean areMandatoryFieldsFilled = true;
                reqInputModels = adapter.getItemList();

                for (int i = 0; i < list.size(); i++) {
                    MemberReqInputModel model = list.get(i);
                    if (model.getmCompulsoryYN().equalsIgnoreCase("Y")) {
                        String value = model.getmFieldValue();
                        if (model.getmFieldTypeID().equalsIgnoreCase("4") || model.getmFieldTypeID().equalsIgnoreCase("66") || model.getmFieldTypeID().equalsIgnoreCase("99")) {
                            value = model.getmFileInfoModel().getmBase64Data();
                        }
                        if (TextUtils.isEmpty(value)) {
                            areMandatoryFieldsFilled = false;
                            Utility.displayMessage(getContext(), model.getmFieldLabel() + " is not filled");
                            break;
                        }
                    }
                }
                if (areMandatoryFieldsFilled) {
                    if (isSubmitClicked) {
                        isSubmitClicked = false;
                        adapter.refresh(adapter.getItemList());
                        MainActivity.isAnimationLoaded = false;
                        ((MainActivity) getActivity()).showHideProgress(true);
                        CommunicationManager.getInstance().sendPostRequest(
                                new IBaseResponse() {
                                    @Override
                                    public void validateResponse(ResponseData response) {
                                        JSONObject updateMemberRequestFieldJSON;
                                        try {
                                            updateMemberRequestFieldJSON = new JSONObject(response.getResponseData());
                                            JSONObject mainJSONObject = updateMemberRequestFieldJSON.optJSONObject("UpdateMemberReqInputFieldsResult");
                                            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                isSubmitClicked = true;
                                                String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                                                new AlertCustomDialog(getActivity(), errorMessage);
                                            } else {
                                                Utility.displayMessage(getContext(), "Employee created");
                                                Utility.navigateToTeamOrHome(mUserActionListener);
                                            }

                                        } catch (JSONException e) {
                                            isSubmitClicked = true;
                                            Log.e(TAG, e.getMessage(), e);
                                            Crashlytics.logException(e);
                                        }
                                        MainActivity.isAnimationLoaded = true;
                                        ((MainActivity) getActivity()).showHideProgress(false);
                                    }
                                }, AppRequestJSONString.getCreateEmployee(list)
                                , CommunicationConstant.APT_GET_CREATE_EMPLOYEE,
                                false);
                    }
                }
            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.navigateToTeamOrHome(mUserActionListener);

            }
        });
        return rootView;
    }


    private void populateViews(JSONArray array) {
        MemberReqInputModel model = new MemberReqInputModel(array);
        list = model.getmMemberReqInputList();
        Log.e("Size", "" + list.size());


        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String fieldTypeID = adapter.getItemList().get(position).getmFieldTypeID();
                return (position < adapter.getItemCount() && (fieldTypeID.equalsIgnoreCase("4") || fieldTypeID.equalsIgnoreCase("99") || fieldTypeID.equalsIgnoreCase("66")) ? 1 : 2);
            }
        });
        adapter.refresh(list);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppsConstant.REQ_CAMERA) {
                String strFile = data.getStringExtra("response");
                String purpose = data.getStringExtra("image_purpose");
                ArrayList<MemberReqInputModel> itemList = adapter.getItemList();
                FileInfoModel fileInfoModel = itemList.get(currentPosition).getmFileInfoModel();
                String base64Data = encodeImage(strFile);
                fileInfoModel.setmBase64Data(base64Data);
                fileInfoModel.setmExtension(".jpg");
                fileInfoModel.setmName(fileInfoModel.getmText() + ".jpg");
                fileInfoModel.setmFilePath(strFile);
                fileInfoModel.setmLength(base64Data.length() + "");

               adapter.refresh(itemList);
                Log.d(MainActivity.TAG, purpose + "     " + base64Data);
            }
        }
    }
}
