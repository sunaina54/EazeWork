package hr.eazework.com.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Dell3 on 04-09-2017.
 */

public class UploadFileModel implements Serializable{
    private String filename;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {

        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
