package hr.eazework.com.model;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 08-09-2017.
 */

public class DocListModel implements Serializable {
    private String Base64Data;
    private String Extension;
    private Bitmap bitmap;
    private int Length;
    private String Name;
    private String DocFile;
    private int DocID;
    private String DocPath;
    private String Flag;
    private int SeqNo;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBase64Data() {
        return Base64Data;
    }

    public void setBase64Data(String base64Data) {
        Base64Data = base64Data;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDocFile() {
        return DocFile;
    }

    public void setDocFile(String docFile) {
        DocFile = docFile;
    }

    public int getDocID() {
        return DocID;
    }

    public void setDocID(int docID) {
        DocID = docID;
    }

    public String getDocPath() {
        return DocPath;
    }

    public void setDocPath(String docPath) {
        DocPath = docPath;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public int getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(int seqNo) {
        SeqNo = seqNo;
    }

    static public DocListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DocListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
