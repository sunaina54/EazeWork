package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ExpenseImageList;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;

import static android.app.Activity.RESULT_OK;
import static hr.eazework.com.ui.util.ImageUtil.rotateImage;

/**
 * Created by Dell3 on 17-08-2017.
 */

public class AdvanceSubmissionFragment extends BaseFragment {
    private Context context;
    private Preferences preferences;
    private ImageView plus_create_newIV;
    private RecyclerView expenseRecyclerView;
    private static int RESULT_LOAD_IMAGE = 1;
    private ExpenseAdapter expenseAdapter;
    private Bitmap bitmap = null;
    private String purpose = "";
    private ArrayList<ExpenseImageList> imageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(false);
        this.setShowEditTeamButtons(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.advance_submission_fragment, container, false);
        context = getContext();
        imageList=new ArrayList<>();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);

        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Draft");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isSubmitClicked) {
                    isSubmitClicked = false;
                    doSubmitOperation();
                } else {
                    // new AlertCustomDialog(getContext(), "Please select leave type");
                }*/

            }
        });
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.ADVANCE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                    }
                }
            }
        });
        plus_create_newIV = (ImageView) rootView.findViewById(R.id.plus_create_newIV);
        expenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        plus_create_newIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Take a photo");
                list.add("Gallery");
                final CustomBuilder customBuilder = new CustomBuilder(getContext(), "Upload From", false);
                customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                            @Override
                            public void onClick(CustomBuilder builder, Object selectedObject) {
                                if (selectedObject.toString().equalsIgnoreCase("Take a photo")) {
                                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !PermissionUtil.checkLocationPermission(getContext())) {
                                        PermissionUtil.askAllPermission(AdvanceSubmissionFragment.this);
                                    }
                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && PermissionUtil.checkLocationPermission(getContext())) {
                                        if (Utility.isLocationEnabled(getContext())) {
                                            if (Utility.isNetworkAvailable(getContext())) {
                                                Utility.openCamera(getActivity(),AdvanceSubmissionFragment.this, AppsConstant.BACK_CAMREA_OPEN, "AddExpense");
                                                customBuilder.dismiss();
                                            } else {
                                                Utility.showNetworkNotAvailableDialog(getContext());
                                            }
                                        } else {
                                            Utility.requestToEnableGPS(getContext(), new Preferences(getContext()));
                                        }
                                    } else {
                                        Utility.displayMessage(getContext(), "Please provide all permission");
                                    }
                                } else if (selectedObject.toString().equalsIgnoreCase("Gallery")) {

                                    galleryIntent();
                                    customBuilder.dismiss();
                                }
                            }
                        }
                );
                customBuilder.show();
            }

        });

        return rootView;
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
    }

    private class ExpenseAdapter extends RecyclerView.Adapter<AdvanceSubmissionFragment.ExpenseAdapter.ViewHolder> {
        private List<ExpenseImageList> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView fileNameTV;
            public ImageView img_icon, img_menu_icon;

            public ViewHolder(View v) {
                super(v);
                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                img_icon = (ImageView) v.findViewById(R.id.img_icon);
                img_menu_icon = (ImageView) v.findViewById(R.id.img_menu_icon);

            }
        }

        public ExpenseAdapter(List<ExpenseImageList> myDataset) {
            mDataset = myDataset;

        }

        @Override
        public AdvanceSubmissionFragment.ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                               int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            AdvanceSubmissionFragment.ExpenseAdapter.ViewHolder vh = new AdvanceSubmissionFragment.ExpenseAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(AdvanceSubmissionFragment.ExpenseAdapter.ViewHolder holder, int position) {

            ExpenseImageList item = mDataset.get(position);
            holder.fileNameTV.setText(item.getFilename());
            holder.img_icon.setImageBitmap(item.getImageBitmap());
            holder.fileNameTV.setText(mDataset.get(position).getFilename());
            holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Save as");
                    list.add("Open");
                    list.add("Delete");
                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                    customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {

                        }
                    });
                    customBuilder.show();
                }
            });
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri extras = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), extras);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File mediaFile = null;
            if (bitmap != null) {
                byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                if (mediaFile != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream(mediaFile);
                        fos.write(imageBytes);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        Crashlytics.log(1, getClass().getName(), e.getMessage());
                        Crashlytics.logException(e);
                    } catch (IOException e) {
                        Crashlytics.log(1, getClass().getName(), e.getMessage());
                        Crashlytics.logException(e);
                    }
                }
            }
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.image_preview_expense);
            final TextView filenameET = (TextView) dialog.findViewById(R.id.filenameET);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_preview);
            imageView.setImageBitmap(bitmap);

            int textColor = Utility.getTextColorCode(preferences);
            TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
            tv_header_text.setTextColor(textColor);
            tv_header_text.setText("Supporting Documents");
            (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpenseImageList imageItem=new ExpenseImageList();
                    imageItem.setImageBitmap(bitmap);
                    imageItem.setFilename(filenameET.getText().toString());
                    imageList.add(imageItem);
                    refreshRecycle();
                    dialog.dismiss();

                }
            });
            (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();


        }

        if (requestCode == AppsConstant.REQ_CAMERA && resultCode == RESULT_OK) {
            Intent intent=data;//new Intent();
            String path = intent.getStringExtra("response");
            Uri uri=Uri.fromFile(new File(path));
            if(uri==null){
                Log.d("uri","null");
            }
            else {
                //  Uri extras = data.getData();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File mediaFile = null;
                if (bitmap != null) {
                    byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                    File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                    if (mediaFile != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(mediaFile);
                            fos.write(imageBytes);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Crashlytics.log(1, getClass().getName(), e.getMessage());
                            Crashlytics.logException(e);
                        } catch (IOException e) {
                            Crashlytics.log(1, getClass().getName(), e.getMessage());
                            Crashlytics.logException(e);
                        }
                    }
                }
            }
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.image_preview_expense);
            final TextView filenameET = (TextView) dialog.findViewById(R.id.filenameET);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_preview);
            imageView.setImageBitmap(bitmap);
            // imageView.setImageURI(uri);

            int textColor = Utility.getTextColorCode(preferences);
            TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
            tv_header_text.setTextColor(textColor);
            tv_header_text.setText("Supporting Documents");
            (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpenseImageList imageItem=new ExpenseImageList();
                    imageItem.setImageBitmap(bitmap);
                    imageItem.setFilename(filenameET.getText().toString());
                    imageList.add(imageItem);
                    refreshRecycle();
                    dialog.dismiss();

                }
            });
            (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();


        }
    }

    private void refreshRecycle(){
        expenseAdapter = new AdvanceSubmissionFragment.ExpenseAdapter(imageList);
        expenseRecyclerView.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged();

    }
}
