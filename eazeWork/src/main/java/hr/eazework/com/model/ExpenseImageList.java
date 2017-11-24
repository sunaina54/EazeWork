package hr.eazework.com.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Dell3 on 10-08-2017.
 */

public class ExpenseImageList implements Serializable {
    private int imageId;
    private Bitmap imageBitmap;
    private String filename;
   /* private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }*/

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
