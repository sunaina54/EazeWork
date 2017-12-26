package hr.eazework.com.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hr.eazework.com.R;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.ui.util.Utility;

/**
 * Created by Dell3 on 07-12-2017.
 */

public class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {
    public ArrayList<SupportDocsItemModel> mDataset;
    private Context context;
    private String screen;
    private LinearLayout errorLinearLayout;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fileNameTV, fileDescriptionTV;
        public ImageView img_icon, img_menu_icon;

        public ViewHolder(View v) {
            super(v);
            fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
            fileDescriptionTV = (TextView) v.findViewById(R.id.fileDescriptionTV);
            img_icon = (ImageView) v.findViewById(R.id.img_icon);
            img_menu_icon = (ImageView) v.findViewById(R.id.img_menu_icon);

        }
    }

    public DocumentUploadAdapter(ArrayList<SupportDocsItemModel> myDataset, Context context, String screen, LinearLayout errorLinearLayout) {
        mDataset = myDataset;
        this.context=context;
        this.screen=screen;
        this.errorLinearLayout=errorLinearLayout;


    }

    @Override
    public DocumentUploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new1 view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DocumentUploadAdapter.ViewHolder vh = new DocumentUploadAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final DocumentUploadAdapter.ViewHolder holder, final int position) {

        final SupportDocsItemModel fileObject = mDataset.get(position);
        String fileType = "";

        final String filename = fileObject.getDocFile();
        final String name = fileObject.getName();
        if (filename.toString().contains(".pdf")) {
            fileType = "application/pdf";
            try {
                holder.img_icon.setImageDrawable((context.getResources().getDrawable(R.drawable.pdf_icon)));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.contains(".jpg") ||
                filename.contains(".png") || filename.contains(".jpeg")
                || filename.contains(".bmp") || filename.contains(".BMP")) {
            try {
                holder.img_icon.setImageDrawable((context.getResources().getDrawable(R.drawable.jpeg_icon)));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.toString().contains(".docx") || filename.toString().contains(".doc")) {
            fileType = "application/word";
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.doc_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.toString().contains(".xlsx") || filename.toString().contains(".xls")) {
            fileType = "application/word";
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.doc_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.toString().contains(".txt")) {
            fileType = "application/txt";
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.txt_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.contains(".gif")) {
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.gif_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.contains(".rar")) {
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rar_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        } else if (filename.contains(".zip")) {
            try {
                holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.zip_icon));
                holder.fileNameTV.setText(filename);
                holder.fileDescriptionTV.setText(name);
            } catch (Exception e) {

            }
        }

        holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Utility.showDocumentPopUp(context,screen,fileObject,DocumentUploadAdapter.this,errorLinearLayout);
                   /* ArrayList<String> list = new ArrayList<>();
                    list.add("Edit");
                    list.add("Delete");
                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                    customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {
                            if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.filename_advance_expense);
                                preferences = new Preferences(getContext());
                                int textColor = Utility.getTextColorCode(preferences);
                                int bgColor = Utility.getBgColorCode(context, preferences);
                                FrameLayout fl_actionBarContainer = (FrameLayout) dialog.findViewById(R.id.fl_actionBarContainer);
                                fl_actionBarContainer.setBackgroundColor(bgColor);
                                TextView tv_header_text = (TextView) dialog.findViewById(R.id.tv_header_text);
                                tv_header_text.setTextColor(textColor);
                                tv_header_text.setText("Edit");

                                final EditText editFilenameET = (EditText) dialog.findViewById(R.id.editFilenameET);
                                editFilenameET.setText(name);

                                (dialog).findViewById(R.id.ibRight).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fileObject.setName(editFilenameET.getText().toString());
                                        if (uploadFileList != null && uploadFileList.size() > 0) {
                                            uploadFileList.set(uploadFileList.indexOf(fileObject), fileObject);

                                        } else {
                                            uploadFileList = new ArrayList<SupportDocsItemModel>();
                                            uploadFileList.add(fileObject);
                                        }
                                        refreshList();
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
                            } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                                mDataset.remove(position);
                                DocumentUploadAdapter.this.notifyDataSetChanged();
                                if (mDataset.size() == 0) {
                                    errorLinearLayout.setVisibility(View.VISIBLE);
                                }

                            }
                            builder.dismiss();
                        }
                    });
                    customBuilder.show();
 */               }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
