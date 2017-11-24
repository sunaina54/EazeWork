package hr.eazework.selfcare.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hr.calender.caldroid.CaldroidFragment;
import hr.calender.caldroid.CaldroidListener;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.FileInfoModel;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.model.MemberReqInputModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.OnImageClickListner;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

public class EditTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<MemberReqInputModel> memberRequestInputFields;
    private static final int TYPE_STRING = 1;
    private static final int TYPE_NUMBER = 2;
    private static final int TYPE_DATE = 3;
    private static final int TYPE_TABLE = 4;
    private static final int TYPE_LIST = 5;
    private static final int TYPE_SINGLE_ATTACHMENT = 66;
    private static final int TYPE_PROFILE = 99;


    private CaldroidFragment dialogCaldroidFragment;
    public Activity mContext;
    private OnImageClickListner listner;


    public void refresh(ArrayList<MemberReqInputModel> memberRequestInputFields) {
        this.memberRequestInputFields = memberRequestInputFields;
        notifyDataSetChanged();
    }

    public ArrayList<MemberReqInputModel> getItemList() {
        return memberRequestInputFields;
    }


    public EditTeamAdapter(Activity mContext, ArrayList<MemberReqInputModel> memberRequestInputFields, OnImageClickListner listner) {
        this.memberRequestInputFields = memberRequestInputFields;
        this.mContext = mContext;
        this.listner = listner;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_STRING:
                View v1 = inflater.inflate(R.layout.edittext_layout, parent, false);
                viewHolder = new EditTextViewHolder(v1);
                break;
            case TYPE_NUMBER:
                View v2 = inflater.inflate(R.layout.edittext_layout, parent, false);
                viewHolder = new EditTextViewHolder(v2);
                break;
            case TYPE_DATE:
                View v3 = inflater.inflate(R.layout.textview_layout, parent, false);
                viewHolder = new TextViewHolder(v3);
                break;
            case TYPE_LIST:
                View v4 = inflater.inflate(R.layout.textview_layout, parent, false);
                viewHolder = new TextViewHolder(v4);
                break;
            case TYPE_TABLE:
                View v5 = inflater.inflate(R.layout.employee_photo_list_item, parent, false);
                viewHolder = new ImageTextViewHolder(v5);
                break;
            case TYPE_PROFILE:
                View v6 = inflater.inflate(R.layout.employee_photo_list_item, parent, false);
                viewHolder = new ImageTextViewHolder(v6);
                break;
            case TYPE_SINGLE_ATTACHMENT:
                View v7 = inflater.inflate(R.layout.employee_photo_list_item, parent, false);
                viewHolder = new ImageTextViewHolder(v7);
                break;
            default:
                View v8 = inflater.inflate(R.layout.textview_layout,parent,false);
                viewHolder = new TextViewHolder(v8);
        }
        return viewHolder;

    }

    private MemberReqInputModel getItem(int position) {
        return memberRequestInputFields.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        switch (viewholder.getItemViewType()){
            case TYPE_STRING:
                EditTextViewHolder vh1 = (EditTextViewHolder) viewholder;
                configureEditTextViewHolder(vh1, position);
                break;
            case TYPE_NUMBER:
                EditTextViewHolder vh2 = (EditTextViewHolder) viewholder;
                configureEditTextViewHolder(vh2, position);
                break;
            case TYPE_DATE:
                TextViewHolder vh3 = (TextViewHolder) viewholder;
                configureTextViewHolder(vh3, position);
                break;
            case TYPE_LIST:
                TextViewHolder vh4 = (TextViewHolder) viewholder;
                configureTextViewHolder(vh4, position);
                break;
            case TYPE_TABLE:
                ImageTextViewHolder vh5 = (ImageTextViewHolder) viewholder;
                configureImageTextViewHolder(vh5, position);
                break;
            case TYPE_PROFILE:
                ImageTextViewHolder vh6 = (ImageTextViewHolder) viewholder;
                configureImageTextViewHolder(vh6, position);
                break;
            case TYPE_SINGLE_ATTACHMENT:
                ImageTextViewHolder vh7 = (ImageTextViewHolder) viewholder;
                configureImageTextViewHolder(vh7, position);
        }
    }



    private void configureEditTextViewHolder(final EditTextViewHolder vh2, int position) {
        final MemberReqInputModel model = getItem(position);
        Log.d("TAG","data list : "+model.getmFieldLabel()+" Field Value : "+model.getmFieldValue()+" : "+position);
        switch (vh2.getItemViewType()) {
            case TYPE_STRING:
                vh2.editText.setHint(model.getmFieldLabel());

                if(!TextUtils.isEmpty(getItem(position).getmFieldValue())) {
                    vh2.editText.setText(getItem(position).getmFieldValue());
                } else {
                    vh2.editText.setText("");
                }

                vh2.editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        getItem(vh2.getAdapterPosition()).setmFieldValue(str);
                    }
                });
                break;
            case TYPE_NUMBER:
                vh2.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                vh2.editText.setHint(model.getmFieldLabel());

                if(!TextUtils.isEmpty(getItem(position).getmFieldValue())) {
                    vh2.editText.setText(getItem(position).getmFieldValue());
                } else {
                    vh2.editText.setText("");
                }
                vh2.editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        getItem(vh2.getAdapterPosition()).setmFieldValue(str);
                    }
                });
                break;
        }
    }

    private void configureImageTextViewHolder(ImageTextViewHolder vh2, final int position) {

        final MemberReqInputModel model = getItem(position);
        FileInfoModel fileInfoModel = model.getmFileInfoModel();
        switch (vh2.getItemViewType()) {
            case TYPE_TABLE:
                vh2.imageView.setImageResource(R.drawable.camera);
                vh2.textView.setText(model.getmFieldLabel());
                if(!TextUtils.isEmpty(fileInfoModel.getmFilePath())) {
                    vh2.imageView.setImageBitmap(ImageUtil.decodeImageToBitmap(fileInfoModel.getmFilePath()));
                }

                if(!TextUtils.isEmpty(fileInfoModel.getmText())) {
                    vh2.textView.setText(fileInfoModel.getmText());
                } else {
                    vh2.textView.setText(model.getmFieldLabel());
                }
                vh2.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listner.capturePhoto(position,model.getmFieldLabel().trim(),
                                getItem(position).getmFieldCode());
                    }
                });
                break;
            case TYPE_PROFILE:

                vh2.textView.setText(model.getmFieldLabel());
                if(!TextUtils.isEmpty(fileInfoModel.getmFilePath())) {
                    vh2.imageView.setImageBitmap(ImageUtil.decodeImageToBitmap(fileInfoModel.getmFilePath()));
                } else {
                    vh2.imageView.setImageResource(R.drawable.photo);
                }

                if(!TextUtils.isEmpty(fileInfoModel.getmText())) {
                    vh2.textView.setText(fileInfoModel.getmText());
                } else {
                    vh2.textView.setText(model.getmFieldLabel());
                }

                vh2.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listner.capturePhoto(position,model.getmFieldLabel().trim(),
                                getItem(position).getmFieldCode());
                    }
                });
                break;
            case TYPE_SINGLE_ATTACHMENT:
                vh2.imageView.setImageResource(R.drawable.camera);
                vh2.textView.setText(model.getmFieldLabel());
                if(!TextUtils.isEmpty(fileInfoModel.getmFilePath())) {
                    vh2.imageView.setImageBitmap(ImageUtil.decodeImageToBitmap(fileInfoModel.getmFilePath()));
                }

                if(!TextUtils.isEmpty(fileInfoModel.getmText())) {
                    vh2.textView.setText(fileInfoModel.getmText());
                } else {
                    vh2.textView.setText(model.getmFieldLabel());
                }

                vh2.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listner.capturePhoto(position,model.getmFieldLabel().trim(),
                                getItem(position).getmFieldCode());
                    }
                });
                break;
        }
    }


    private void configureTextViewHolder(final TextViewHolder vh2, final int position) {
        final MemberReqInputModel model = getItem(position);
        switch (vh2.getItemViewType()) {
            case TYPE_DATE:
                vh2.textView.setHint(model.getmFieldLabel());

                if(!TextUtils.isEmpty(model.getmFieldValue())) {
                    vh2.textView.setText(model.getmFieldValue());
                }
                vh2.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCaldroidFragment = new CaldroidFragment();
                        dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                            @Override
                            public void onSelectDate(Date date, View view) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                // if (calendar.compareTo(calendarCurrent) >= 0) {

                                vh2.textView.setText(String.format("%1$td/%1$tm/%1$tY",
                                        calendar));
                                getItem(position).setmFieldValue(String.format("%1$td/%1$tm/%1$tY",
                                        calendar));
                                dialogCaldroidFragment.dismiss();

                            }
                        });
                        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                        Bundle state = dialogCaldroidFragment.getArguments();
                        if (state != null) {
                            dialogCaldroidFragment.restoreDialogStatesFromKey(
                                    ((MainActivity)mContext).getTopFragment().getChildFragmentManager(), state,
                                    "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                            Bundle args = dialogCaldroidFragment.getArguments();
                            if (args == null) {
                                args = new Bundle();
                                dialogCaldroidFragment.setArguments(args);
                            }
                        } else {
                            // Setup arguments
                            Bundle bundle = new Bundle();
                            // Setup dialogTitle
                            dialogCaldroidFragment.setArguments(bundle);
                        }
                        dialogCaldroidFragment.show(((MainActivity)mContext).getTopFragment().getChildFragmentManager(),dialogTag);
                    }
                });
                break;
            case TYPE_LIST:
                vh2.textView.setText("");
                vh2.textView.setHint(model.getmFieldLabel());
              if(model.getmFieldCode()!=null && model.getmFieldCode().equalsIgnoreCase("EC00000004")){

                  if(!TextUtils.isEmpty(model.getmFieldValue())) {
                      vh2.textView.setText(model.getmCommonServiceModel().getValue());
                  }
              }
                vh2.textView.setHint(model.getmFieldLabel());
                if(model.getmFieldCode()!=null && model.getmFieldCode().equalsIgnoreCase("EC10000007")){

                    if(!TextUtils.isEmpty(model.getmFieldValue())) {
                        vh2.textView.setText(model.getmCommonServiceModel().getValue());
                    }
                }

                vh2.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        CommunicationManager.getInstance().sendPostRequest(
                                new IBaseResponse() {
                                    @Override
                                    public void validateResponse(ResponseData response) {
                                        switch (response.getRequestData().getReqApiId()) {
                                            case CommunicationConstant.API_GET_TYPE_WISE_LIST:
                                                JSONObject json3;
                                                try {
                                                    json3 = new JSONObject(response.getResponseData());
                                                    JSONObject mainJSONObject = json3
                                                            .optJSONObject("GetTypeWiseListResult");
                                                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                                                        new AlertCustomDialog(mContext, mainJSONObject
                                                                .optString("ErrorMessage", "Failled")
                                                                .equalsIgnoreCase("") ? "Failled"
                                                                : mainJSONObject.optString("ErrorMessage",
                                                                "Failled"), new AlertCustomDialog.AlertClickListener() {

                                                            @Override
                                                            public void onPositiveBtnListener() {
                                                            }

                                                            @Override
                                                            public void onNegativeBtnListener() {
                                                                // TODO Auto-generated method stub
                                                            }
                                                        });
                                                    } else {

                                                        JSONArray array = mainJSONObject.optJSONArray("list");
                                                        final TypeWiseListModel m = new TypeWiseListModel(array);

                                                        CustomBuilder builder2 = new CustomBuilder(mContext,"Select "+ model.getmFieldLabel(),true);
                                                        builder2.setSingleChoiceItems(m.getList(),vh2.textView.getTag(), new CustomBuilder.OnClickListener() {
                                                            @Override
                                                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                                                String genderCode = ((TypeWiseListModel)selectedObject).getCode();
                                                                String genderValue = ((TypeWiseListModel)selectedObject).getValue();
                                                                vh2.textView.setText(((TypeWiseListModel)selectedObject).getValue());
                                                                builder.dismiss();
                                                                TypeWiseListModel typeWiseListModel = new TypeWiseListModel(genderCode,genderValue);
                                                                getItem(vh2.getAdapterPosition()).setmCommonServiceModel(typeWiseListModel);
                                                                getItem(vh2.getAdapterPosition()).setmFieldValue(genderCode);

                                                            }
                                                        });
                                                        builder2.setCancelListener(new CustomBuilder.OnCancelListener() {
                                                            @Override
                                                            public void onCancel() {

                                                            }
                                                        });
                                                        builder2.show();



                                                    }
                                                } catch (JSONException e1) {
                                                    // TODO Auto-generated catch block
                                                    e1.printStackTrace();
                                                }

                                                break;
                                        }
                                    }
                                }
                                , AppRequestJSONString.getCommonService("FieldCode",model.getmFieldCode()), CommunicationConstant.API_GET_TYPE_WISE_LIST, false);
                    }
                });

                break;
            default:
                vh2.textView.setVisibility(View.GONE);
                break;
        }
    }



    private void removeItem(ArrayList<MemberReqInputModel> list,int position){
        list.remove(position);
        notifyItemRemoved(position);
    }


    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        String Type = getItem(position).getmFieldTypeID();
        int fieldType = Integer.parseInt(Type);
        return fieldType;
    }

    @Override
    public int getItemCount() {
        if(memberRequestInputFields !=null && memberRequestInputFields.size()>0) {
            return memberRequestInputFields.size() ;
        }else{
            return  0;
        }
    }
    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public TextViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tvField);
        }
    }

    class EditTextViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        public EditTextViewHolder(View itemView) {
            super(itemView);
            this.editText = (EditText) itemView.findViewById(R.id.etField);
        }

    }

    class ImageTextViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ImageTextViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            this.textView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}