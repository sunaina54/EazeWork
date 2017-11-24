package hr.eazework.com.model;

import java.util.ArrayList;

/**
 * Created by Manjunath on 06-04-2017.
 */

public class FileInfoModel {
    private String mName;
    private String mFilePath;
    private String mExtension;
    private String mLength;
    private String mBase64Data;
    private String mText;
    private ArrayList<FileInfoModel> fileInfoModelList;

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getmExtension() {
        return mExtension;
    }

    public void setmExtension(String mExtension) {
        this.mExtension = mExtension;
    }

    public String getmLength() {
        return mLength;
    }

    public void setmLength(String mLength) {
        this.mLength = mLength;
    }

    public String getmBase64Data() {
        return mBase64Data;
    }

    public void setmBase64Data(String mBase64Data) {
        this.mBase64Data = mBase64Data;
    }

    public ArrayList<FileInfoModel> getFileInfoModelList() {
        return fileInfoModelList;
    }

    public void setFileInfoModelList(ArrayList<FileInfoModel> fileInfoModelList) {
        this.fileInfoModelList = fileInfoModelList;
    }
}
