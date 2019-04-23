package hr.eazework.com.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.fragment.ViewAdvanceRequestSummaryFragment;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.selfcare.communication.CommunicationConstant;

/**
 * Created by Dell3 on 19-01-2018.
 */

public class DocumentViewAdapter extends
        RecyclerView.Adapter<DocumentViewAdapter.MyViewHolder> {
    private ArrayList<SupportDocsItemModel> dataSet;
    private Context context;
    private String screen;
    private LinearLayout errorLinearLayout;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView fileNameTV, filedescriptionTV;
        public Button downloadBTN;
        private ImageView img_menu_icon;


        public MyViewHolder(View v) {
            super(v);

            fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
            filedescriptionTV = (TextView) v.findViewById(R.id.filedescriptionTV);
            downloadBTN = (Button) v.findViewById(R.id.downloadBTN);
            img_menu_icon = (ImageView) v.findViewById(R.id.img_icon_rounded);


        }
    }

    public void addAll(List<SupportDocsItemModel> list) {

        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public DocumentViewAdapter(List<SupportDocsItemModel> data) {
        this.dataSet = (ArrayList<SupportDocsItemModel>) data;

    }

    public DocumentViewAdapter(ArrayList<SupportDocsItemModel> myDataset, Context context, String screen, LinearLayout errorLinearLayout, Activity activity) {
        dataSet = myDataset;
        this.context=context;
        this.screen=screen;
        this.errorLinearLayout=errorLinearLayout;
        this.activity=activity;

    }

    @Override
    public DocumentViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.document_item, parent, false);
        DocumentViewAdapter.MyViewHolder myViewHolder = new DocumentViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final DocumentViewAdapter.MyViewHolder holder, final int listPosition) {

        final SupportDocsItemModel item = dataSet.get(listPosition);
        holder.fileNameTV.setText(item.getDocFile());
        holder.filedescriptionTV.setText(item.getName());
        holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = item.getDocPath().replace("~", "");
                String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();
                Utility.downloadPdf(path, null, item.getDocFile(), context, activity);


            }
        });
        holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Download");
                CustomBuilder customBuilder = new CustomBuilder(context, "Options", false);
                customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {

                        if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                        } else if (selectedObject.toString().equalsIgnoreCase("Download")) {

                            String filePath = item.getDocPath().replace("~", "");
                            String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();

                            Utility.downloadPdf(path, null, item.getDocFile(), context, activity);
                        }
                        builder.dismiss();
                    }
                });
                customBuilder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void clearDataSource() {
        dataSet.clear();
        notifyDataSetChanged();
    }
}
