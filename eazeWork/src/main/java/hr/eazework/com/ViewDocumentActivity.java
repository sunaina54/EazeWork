package hr.eazework.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.selfcare.communication.CommunicationConstant;

public class ViewDocumentActivity extends BaseActivity {
    private Context context;
    private Preferences preferences;
    private RecyclerView lineDocumentRecyclerView;
    private LineItemsModel lineItemsModel;
    private static ProgressDialog progress;
    private LineDocumentViewAdapter lineDocumentViewAdapter;
    private LinearLayout header_layout,rl_edit_team_member;
    private TextView tv_header_text;
    private RelativeLayout backLayout;
    private ImageView  ibRightIV;
    public LineItemsModel getLineItemsModel() {
        return lineItemsModel;
    }

    public void setLineItemsModel(LineItemsModel lineItemsModel) {
        this.lineItemsModel = lineItemsModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_expense_document);
        context=this;
        preferences=new Preferences(context);
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor=Utility.getBgColorCode(context,preferences);
        rl_edit_team_member= (LinearLayout) findViewById(R.id.rl_edit_team_member);
        rl_edit_team_member.setBackgroundColor(bgColor);
        tv_header_text= (TextView) findViewById(R.id.tv_header_text);
        tv_header_text.setTextColor(textColor);
        tv_header_text.setText("View Document");
        backLayout=(RelativeLayout)findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibRightIV=(ImageView)findViewById(R.id.ibRight) ;
        ibRightIV.setVisibility(View.GONE);
        lineDocumentRecyclerView = (RecyclerView) findViewById(R.id.lineDocumentRV);
        lineItemsModel = (LineItemsModel) getIntent().getSerializableExtra("LineItemDocument");
        if (lineItemsModel.getDocListLineItem() != null && lineItemsModel.getDocListLineItem().size() > 0) {
            refreshDocument(lineItemsModel.getDocListLineItem());
        }
    }


    private void refreshDocument(ArrayList<SupportDocsItemModel> item) {
        lineDocumentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lineDocumentViewAdapter = new LineDocumentViewAdapter(item);
        lineDocumentRecyclerView.setAdapter(lineDocumentViewAdapter);
        lineDocumentViewAdapter.notifyDataSetChanged();

    }

    private class LineDocumentViewAdapter extends
            RecyclerView.Adapter<LineDocumentViewAdapter.MyViewHolder> {
        private ArrayList<SupportDocsItemModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView fileNameTV, filedescriptionTV;
            public Button downloadBTN;
            private ImageView img_menu_icon;

            public MyViewHolder(View v) {
                super(v);

                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                filedescriptionTV = (TextView) v.findViewById(R.id.filedescriptionTV);
                downloadBTN = (Button) v.findViewById(R.id.downloadBTN);
                img_menu_icon=(ImageView)v.findViewById(R.id.img_icon_rounded);


            }
        }

        public void addAll(List<SupportDocsItemModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public LineDocumentViewAdapter(List<SupportDocsItemModel> data) {
            this.dataSet = (ArrayList<SupportDocsItemModel>) data;

        }

        @Override
        public LineDocumentViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.document_item, parent, false);
            LineDocumentViewAdapter.MyViewHolder myViewHolder = new LineDocumentViewAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final LineDocumentViewAdapter.MyViewHolder holder, final int listPosition) {

            final SupportDocsItemModel item = dataSet.get(listPosition);
            holder.fileNameTV.setText(item.getDocFile());
            holder.filedescriptionTV.setText(item.getName());
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

                            } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                            } else if (selectedObject.toString().equalsIgnoreCase("Download")) {

                                String filePath = item.getDocPath().replace("~", "");
                                String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();

                                Utility.downloadPdf(path, null, item.getDocFile(), context, ViewDocumentActivity.this
                                );
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
}
