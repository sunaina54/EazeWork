package hr.eazework.com.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.selfcare.communication.CommunicationConstant;

/**
 * Created by Dell3 on 30-10-2017.
 */

public class ViewDocumentFragment extends BaseFragment {
    private Context context;
    private Preferences preferences;
    private RecyclerView lineDocumentRecyclerView;
    private LineItemsModel lineItemsModel;
    private static ProgressDialog progress;
    private LineDocumentViewAdapter lineDocumentViewAdapter;

    public LineItemsModel getLineItemsModel() {
        return lineItemsModel;
    }

    public void setLineItemsModel(LineItemsModel lineItemsModel) {
        this.lineItemsModel = lineItemsModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_expense_document, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);

        lineDocumentRecyclerView= (RecyclerView) view.findViewById(R.id.lineDocumentRV);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Expense Claim Summary");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.EXPENSE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.EXPENSE_HOME, null, null);
                    }
                }
            }
        });

        refreshDocument(lineItemsModel.getDocListLineItem());
    }

    private void refreshDocument(ArrayList<SupportDocsItemModel> item){
            lineDocumentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

            public MyViewHolder(View v) {
                super(v);

                fileNameTV = (TextView) v.findViewById(R.id.fileNameTV);
                filedescriptionTV = (TextView) v.findViewById(R.id.filedescriptionTV);
                downloadBTN = (Button) v.findViewById(R.id.downloadBTN);


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
            holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = item.getDocPath().replace("~", "");
                    String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();
                   // new DownloadFile().execute(path, item.getName());
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



    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, "ProgressDialog", "Downloading File");
        }


        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FileEazeWork");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ViewAdvanceRequestSummaryFragment.FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progress.dismiss();
        }
    }
}
