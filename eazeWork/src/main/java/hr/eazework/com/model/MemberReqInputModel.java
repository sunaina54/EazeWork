package hr.eazework.com.model;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hr.eazework.com.MainActivity;

/**
 * Created by Manjunath on 04-04-2017.
 */

public class MemberReqInputModel implements Comparator<MemberReqInputModel>{
    private String mCompulsoryYN;
    private String mDocPath;
    private String mFieldCode;
    private String mFieldDocTranID;
    private String mFieldLabel;
    private String mFieldTypeID;
    private String mFieldValue;
    private String mSeqNo;
    private String mTranID;
    private TypeWiseListModel mCommonServiceModel;
    private FileInfoModel mFileInfoModel = new FileInfoModel();
    private ArrayList<MemberReqInputModel> mMemberReqInputList = new ArrayList<>();
    private ArrayList<FileInfoModel> mFileInfoModelList;

    public MemberReqInputModel(){}


    public MemberReqInputModel(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            mCompulsoryYN = object.optString("CompulsoryYN","N");
            mDocPath = object.optString("DocPath","");
            mFieldCode = object.optString("FieldCode","");
            mFieldDocTranID = object.optString("FieldDocTranID","");
            mFieldLabel = object.optString("FieldLabel","");
            mFieldTypeID = object.optString("FieldTypeID","");
            mFieldValue = object.optString("FieldValue","");
            mSeqNo = object.optString("SeqNo","");
            mTranID = object.optString("TranID","");

        } catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

    public TypeWiseListModel getmCommonServiceModel() {
        return mCommonServiceModel;
    }

    public void setmCommonServiceModel(TypeWiseListModel mCommonServiceModel) {
        this.mCommonServiceModel = mCommonServiceModel;
    }

    public FileInfoModel getmFileInfoModel() {
        return mFileInfoModel;
    }

    public void setmFileInfoModel(FileInfoModel mFileInfoModel) {
        this.mFileInfoModel = mFileInfoModel;
    }

    public ArrayList<FileInfoModel> getmFileInfoModelList() {
        return mFileInfoModelList;
    }

    public void setmFileInfoModelList(ArrayList<FileInfoModel> mFileInfoModelList) {
        this.mFileInfoModelList = mFileInfoModelList;
    }

    public MemberReqInputModel(JSONArray array) {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.optJSONObject(i);
                    mMemberReqInputList.add(new MemberReqInputModel(object.toString()));
            }
            Collections.sort(mMemberReqInputList,new MemberReqInputModel());
            for (MemberReqInputModel m : mMemberReqInputList){
                Log.d(MainActivity.TAG,m.getmSeqNo() + "  ");
            }
        }
    }

    public String getmCompulsoryYN() {
        return mCompulsoryYN;
    }

    public void setmCompulsoryYN(String mCompulsoryYN) {
        this.mCompulsoryYN = mCompulsoryYN;
    }

    public String getmDocPath() {
        return mDocPath;
    }

    public void setmDocPath(String mDocPath) {
        this.mDocPath = mDocPath;
    }

    public String getmFieldCode() {
        return mFieldCode;
    }

    public void setmFieldCode(String mFieldCode) {
        this.mFieldCode = mFieldCode;
    }

    public String getmFieldDocTranID() {
        return mFieldDocTranID;
    }

    public void setmFieldDocTranID(String mFieldDocTranID) {
        this.mFieldDocTranID = mFieldDocTranID;
    }

    public String getmFieldLabel() {
        return mFieldLabel;
    }

    public void setmFieldLabel(String mFieldLabel) {
        this.mFieldLabel = mFieldLabel;
    }

    public String getmFieldTypeID() {
        return mFieldTypeID;
    }

    public void setmFieldTypeID(String mFieldTypeID) {
        this.mFieldTypeID = mFieldTypeID;
    }

    public String getmFieldValue() {
        return mFieldValue;
    }

    public void setmFieldValue(String mFieldValue) {
        this.mFieldValue = mFieldValue;
    }

    public String getmSeqNo() {
        return mSeqNo;
    }

    public void setmSeqNo(String mSeqNo) {
        this.mSeqNo = mSeqNo;
    }

    public String getmTranID() {
        return mTranID;
    }

    public void setmTranID(String mTranID) {
        this.mTranID = mTranID;
    }

    public ArrayList<MemberReqInputModel> getmMemberReqInputList() {
        return mMemberReqInputList;
    }

    public void setmMemberReqInputList(ArrayList<MemberReqInputModel> mMemberReqInputList) {
        this.mMemberReqInputList = mMemberReqInputList;
    }

    @Override
    public int compare(MemberReqInputModel o1, MemberReqInputModel o2) {
        int seq1 = 0,seq2 = 0;
        try {
            seq1 = Integer.parseInt(o1.getmSeqNo());
            seq2 = Integer.parseInt(o2.getmSeqNo());
        }  catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }

        if ((o1.getmFieldTypeID().equals("99") || o1.getmFieldTypeID().equals("4")) || seq1 > seq2) {
            return 1;
        } else if ((o2.getmFieldTypeID().equals("99") || o2.getmFieldTypeID().equals("4")) || seq1 < seq2) {
            return -1;
        } else {
            return 0;
        }
    }
}
