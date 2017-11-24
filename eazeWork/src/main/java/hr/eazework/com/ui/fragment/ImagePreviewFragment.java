package hr.eazework.com.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hr.eazework.com.R;
import hr.eazework.com.ui.util.Preferences;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Dell3 on 11-08-2017.
 */

public class ImagePreviewFragment extends BaseFragment {
    private static int RESULT_LOAD_IMAGE = 1;
    private Context context;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.image_preview_expense, container, false);
        context = getContext();

        imageView = (ImageView) rootView.findViewById(R.id.img_preview);
      //  galleryIntent();
        return rootView;
    }

    public void galleryIntent(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
