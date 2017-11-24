package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import hr.eazework.com.R;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.selfcare.communication.CommunicationConstant;

/**
 * Created by allsmartlt218 on 01-03-2017.
 */

public class StorePhotoFragment extends DialogFragment {

    private TextView btBack;
    private ImageView storePhoto;
    private String photo = "",localPhoto = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_photo,container,false);

        btBack = (TextView) view.findViewById(R.id.btBack);
        storePhoto = (ImageView) view.findViewById(R.id.storePhoto);
        photo = getArguments().getString("store_photo","");
        localPhoto = getArguments().getString("store_photo_local","");

        if(!TextUtils.isEmpty(photo) && !photo.equalsIgnoreCase("null")) {
            Picasso with = Picasso.with(getContext());
            RequestCreator requestCreator = with.load(CommunicationConstant.getMobileCareURl() + photo);
            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE);
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE);
            requestCreator.resize(768,1024);
            requestCreator.into(storePhoto);
        } else {
            if(!TextUtils.isEmpty(localPhoto)) {
                storePhoto.setImageBitmap(ImageUtil.decodeImageToBitmapAndResize(localPhoto));
            }
        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
