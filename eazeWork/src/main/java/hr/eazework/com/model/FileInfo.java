package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 20-02-2018.
 */

public class FileInfo implements Serializable {
    private String Name;
    private String Extension;
    private String Base64Data;
    private String Length;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getBase64Data() {
        return Base64Data;
    }

    public void setBase64Data(String base64Data) {
        Base64Data = base64Data;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }
}
