package hr.eazework.com.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AdjustmentItem;
import hr.eazework.com.model.AdvanceListModel;
import hr.eazework.com.model.GetAdvanceDetailResponseModel;
import hr.eazework.com.model.GetAdvanceDetailResultModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PaymentDetailsItem;
import hr.eazework.com.model.RequestRemarksItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Dell3 on 30-08-2017.
 */

public class ViewAdvanceRequestSummaryFragment extends BaseFragment {
    private static Context context;
    private Preferences preferences;
    private TextView requestForTV, reasonTV, currencyTV, amountTV, statusTV, advanceVoucherTV, submittedOnTV;
    private GetAdvanceDetailResponseModel advanceDetailResponseModel;
    private AdvanceListModel advanceListModel;
    private RecyclerView documentRV, advanceRV, paymentRV, remarksRV;
    private DocumentViewAdapter documentViewAdapter;
    private LinearLayout errorLinearLayout, advanceLinearLayout, paymentLinearLayout, remarksLinearLayout;
    private static int downloadedSize = 0, totalsize;
    private static float per = 0;
    private static ProgressDialog progress;
    private RemarksAdapter remarksAdapter;
    private PaymentAdapter paymentAdapter;
    private AdjustmentAdapter adjustmentAdapter;
    private TextView totalAdjustmentAmountTV, totalAmountTV;
    private LinearLayout totalAdjustmentLl, totalPaymentLl;
    private ImageView plus_create_newIV;


    public AdvanceListModel getAdvanceListModel() {
        return advanceListModel;
    }

    public void setAdvanceListModel(AdvanceListModel advanceListModel) {
        this.advanceListModel = advanceListModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_advance_request_summary, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        preferences = new Preferences(getContext());
        int textColor = Utility.getTextColorCode(preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("View Expense Summary");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
                    if (itemModel != null && itemModel.isAccess()) {
                        mUserActionListener.performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
                    } else {
                        mUserActionListener.performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
                    }
                }
            }
        });

        advanceVoucherTV = (TextView) view.findViewById(R.id.advanceVoucherTV);
        requestForTV = (TextView) view.findViewById(R.id.requestForTV);
        reasonTV = (TextView) view.findViewById(R.id.reasonTV);
        currencyTV = (TextView) view.findViewById(R.id.currencyTV);
        amountTV = (TextView) view.findViewById(R.id.amountTV);
        statusTV = (TextView) view.findViewById(R.id.statusTV);

        submittedOnTV = (TextView) view.findViewById(R.id.submittedOnTV);

        documentRV = (RecyclerView) view.findViewById(R.id.expenseRecyclerView);
        errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorDocTV);
        errorLinearLayout.setVisibility(View.VISIBLE);

        plus_create_newIV = (ImageView) view.findViewById(R.id.plus_create_newIV);
        plus_create_newIV.setVisibility(View.GONE);

        advanceRV = (RecyclerView) view.findViewById(R.id.advanceRV);
        advanceLinearLayout = (LinearLayout) view.findViewById(R.id.adjustmentLinearLayout);
        advanceLinearLayout.setVisibility(View.VISIBLE);
        totalAdjustmentAmountTV = (TextView) view.findViewById(R.id.totalAdjustmentAmountTV);
        totalAdjustmentLl = (LinearLayout) view.findViewById(R.id.totalAdjustmentLl);
        totalAdjustmentLl.setVisibility(View.GONE);

        paymentRV = (RecyclerView) view.findViewById(R.id.paymentRV);
        paymentLinearLayout = (LinearLayout) view.findViewById(R.id.paymentLinearLayout);
        paymentLinearLayout.setVisibility(View.VISIBLE);
        totalAmountTV = (TextView) view.findViewById(R.id.totalAmountTV);
        totalPaymentLl = (LinearLayout) view.findViewById(R.id.totalLabelLl);
        totalPaymentLl.setVisibility(View.GONE);

        remarksRV = (RecyclerView) view.findViewById(R.id.remarksRV);
        remarksLinearLayout = (LinearLayout) view.findViewById(R.id.remarksLinearLayout);
        remarksLinearLayout.setVisibility(View.VISIBLE);


        sendViewRequestSummaryData();
    }

    private void sendViewRequestSummaryData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getViewAdvanceSummaryData(advanceListModel.getReqID(), advanceListModel.getAdvanceID()),
                CommunicationConstant.API_GET_ADVANCE_DETAIL, true);
    }

    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ADVANCE_DETAIL:
                String str = response.getResponseData();
                Log.d("TAG", "Advance Response : " + str);
                GetAdvanceDetailResponseModel advanceDetailResponseModel = GetAdvanceDetailResponseModel.create(str);
                if (advanceDetailResponseModel != null && advanceDetailResponseModel.getGetAdvanceDetailResult() != null) {
                    updateUI(advanceDetailResponseModel.getGetAdvanceDetailResult());
                    refreshDocumentList(advanceDetailResponseModel.getGetAdvanceDetailResult().getSupportDocs());
                    refreshRemarksList(advanceDetailResponseModel.getGetAdvanceDetailResult().getRequestRemarks());
                    refreshPaymentList(advanceDetailResponseModel.getGetAdvanceDetailResult().getPaymentDetails());
                    refreshAdjustmentList(advanceDetailResponseModel.getGetAdvanceDetailResult().getAdjustments());
                }

                break;

            default:
                break;
        }
        super.validateResponse(response);
    }

    private void updateUI(GetAdvanceDetailResultModel item) {
        advanceVoucherTV.setText(item.getReqCode());
        requestForTV.setText(item.getName());
        reasonTV.setText(item.getReason());
        currencyTV.setText(item.getCurrencyCode());
        amountTV.setText(item.getApprovedAmount());
        statusTV.setText(item.getStatusDesc());
        submittedOnTV.setText(item.getReqDate());
    }


    private class AdjustmentAdapter extends
            RecyclerView.Adapter<AdjustmentAdapter.MyViewHolder> {
        private ArrayList<AdjustmentItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView voucherNoTV, detailsTV, amountTV;

            public MyViewHolder(View v) {
                super(v);

                voucherNoTV = (TextView) v.findViewById(R.id.voucherNoTV);
                detailsTV = (TextView) v.findViewById(R.id.detailsTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);

            }
        }

        public void addAll(List<AdjustmentItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public AdjustmentAdapter(List<AdjustmentItem> data) {
            this.dataSet = (ArrayList<AdjustmentItem>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adjustment_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final AdjustmentItem item = dataSet.get(listPosition);
            holder.voucherNoTV.setText(item.getReqCode());
            holder.detailsTV.setText(item.getDetails());
            holder.amountTV.setText(item.getAmount());

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

    private void refreshAdjustmentList(ArrayList<AdjustmentItem> adjustmentItems) {
        if (adjustmentItems != null && adjustmentItems.size() > 0) {
            advanceLinearLayout.setVisibility(View.GONE);
            advanceRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            advanceRV.setVisibility(View.VISIBLE);
            adjustmentAdapter = new AdjustmentAdapter(adjustmentItems);
            advanceRV.setAdapter(adjustmentAdapter);
            adjustmentAdapter.notifyDataSetChanged();
            if (adjustmentItems.size() > 0) {
                totalAdjustmentLl.setVisibility(View.VISIBLE);
            }
            double total = 0;
            for (AdjustmentItem item : adjustmentItems) {
                total = total + Double.parseDouble(item.getAmount());
            }

            //totalAdjustmentAmountTV.setText();
            Utility.formatAmount(totalAdjustmentAmountTV, total);
        } else {
            advanceLinearLayout.setVisibility(View.VISIBLE);
            advanceRV.setVisibility(View.GONE);
        }
    }


    private class PaymentAdapter extends
            RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
        private ArrayList<PaymentDetailsItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView dateTV, modeTV, detailsTV, amountTV;

            public MyViewHolder(View v) {
                super(v);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                modeTV = (TextView) v.findViewById(R.id.modeTV);
                detailsTV = (TextView) v.findViewById(R.id.detailsTV);
                amountTV = (TextView) v.findViewById(R.id.amountTV);

            }
        }

        public void addAll(List<PaymentDetailsItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public PaymentAdapter(List<PaymentDetailsItem> data) {
            this.dataSet = (ArrayList<PaymentDetailsItem>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_payment_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final PaymentDetailsItem item = dataSet.get(listPosition);
            holder.dateTV.setText(item.getDate());
            holder.modeTV.setText(item.getMode());
            holder.detailsTV.setText(item.getDetails());
            holder.amountTV.setText(item.getAmount());

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

    private void refreshPaymentList(ArrayList<PaymentDetailsItem> paymentDetailsItems) {
        if (paymentDetailsItems != null && paymentDetailsItems.size() > 0) {
            paymentLinearLayout.setVisibility(View.GONE);
            paymentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            paymentRV.setVisibility(View.VISIBLE);
            paymentAdapter = new PaymentAdapter(paymentDetailsItems);
            paymentRV.setAdapter(paymentAdapter);
            paymentAdapter.notifyDataSetChanged();

            if (paymentDetailsItems.size() > 0) {
                totalPaymentLl.setVisibility(View.VISIBLE);
            }
            double total = 0;
            for (PaymentDetailsItem item : paymentDetailsItems) {
                total = total + Double.parseDouble(item.getAmount());
            }

            //totalAmountTV.setText(total + "");
            Utility.formatAmount(totalAmountTV, total);
        } else {
            paymentLinearLayout.setVisibility(View.VISIBLE);
            paymentRV.setVisibility(View.GONE);
        }
    }


    private class RemarksAdapter extends
            RecyclerView.Adapter<RemarksAdapter.MyViewHolder> {
        private ArrayList<RequestRemarksItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView dateTV, nameTV, remarksReasonTV, remarksStatusTV;

            public MyViewHolder(View v) {
                super(v);
                dateTV = (TextView) v.findViewById(R.id.dateTV);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                remarksReasonTV = (TextView) v.findViewById(R.id.remarksReasonTV);
                remarksStatusTV = (TextView) v.findViewById(R.id.remarksStatusTV);

            }
        }

        public void addAll(List<RequestRemarksItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public RemarksAdapter(List<RequestRemarksItem> data) {
            this.dataSet = (ArrayList<RequestRemarksItem>) data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.remarks_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final RequestRemarksItem item = dataSet.get(listPosition);
            holder.dateTV.setText(item.getTranTime());
            holder.nameTV.setText(item.getRemarkBy());
            holder.remarksReasonTV.setText(item.getRemark());
            holder.remarksStatusTV.setText(item.getStatus());

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

    private void refreshRemarksList(ArrayList<RequestRemarksItem> remarksItems) {
        if (remarksItems != null && remarksItems.size() > 0) {
            remarksLinearLayout.setVisibility(View.GONE);
            remarksRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            remarksRV.setVisibility(View.VISIBLE);
            remarksAdapter = new RemarksAdapter(remarksItems);
            remarksRV.setAdapter(remarksAdapter);
            remarksAdapter.notifyDataSetChanged();
        } else {
            remarksLinearLayout.setVisibility(View.VISIBLE);
            remarksRV.setVisibility(View.GONE);
        }
    }

    private void refreshDocumentList(ArrayList<SupportDocsItemModel> docListModels) {
        if (docListModels != null && docListModels.size() > 0) {
            errorLinearLayout.setVisibility(View.GONE);
            documentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            documentRV.setVisibility(View.VISIBLE);
            documentViewAdapter = new DocumentViewAdapter(docListModels);
            documentRV.setAdapter(documentViewAdapter);
            documentViewAdapter.notifyDataSetChanged();
        } else {
            errorLinearLayout.setVisibility(View.VISIBLE);
            documentRV.setVisibility(View.GONE);
        }
    }


    private class DocumentViewAdapter extends
            RecyclerView.Adapter<DocumentViewAdapter.MyViewHolder> {
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

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.document_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final SupportDocsItemModel item = dataSet.get(listPosition);
            holder.fileNameTV.setText(item.getDocFile());
            holder.filedescriptionTV.setText(item.getName());
            holder.downloadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = item.getDocPath().replace("~", "");
                    String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();
                    //  new DownloadFile().execute(path, item.getName());
                   /* String filePath = fileObject.getDocPath().replace("~", "");
                    String path = CommunicationConstant.UrlFile + filePath + "/" + fileObject.getDocFile();*/

                    Utility.downloadPdf(path, null, item.getDocFile(), context, getActivity());


                }
            });
            holder.img_menu_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Download");
                    CustomBuilder customBuilder = new CustomBuilder(getContext(), "Options", false);
                    customBuilder.setSingleChoiceItems(list, null, new CustomBuilder.OnClickListener() {
                        @Override
                        public void onClick(CustomBuilder builder, Object selectedObject) {

                            if (selectedObject.toString().equalsIgnoreCase("Edit")) {
                  /*      final Dialog dialog = new Dialog(context);
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
                                //  SupportDocsItemModel docItem = new SupportDocsItemModel();
                                fileObject.setName(editFilenameET.getText().toString());
                                if (uploadFileList != null && uploadFileList.size() > 0) {
                                    uploadFileList.set(uploadFileList.indexOf(fileObject), fileObject);

                                } else {
                                    uploadFileList = new ArrayList<SupportDocsItemModel>();
                                    uploadFileList.add(fileObject);
                                }
                                refreshList(uploadFileList);
                                dialog.dismiss();

                            }
                        });
                        (dialog).findViewById(R.id.ibWrong).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();*/
                            } else if (selectedObject.toString().equalsIgnoreCase("Delete")) {
                       /* SupportDocsItemModel doc = mDataset.get(position);
                        if (doc.getDocID() != 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.OLD_FLAG)) {
                            doc.setFlag(AppsConstant.DELETE_FLAG);
                            mDataset.set(position, doc);
                        } else if (doc.getDocID() == 0 && doc.getFlag().equalsIgnoreCase(AppsConstant.NEW_FLAG)) {
                            mDataset.remove(position);
                        }
                        EditAdvanceApprovalFragment.DocumentUploadAdapter.this.notifyDataSetChanged();
                        if (mDataset.size() == 0) {
                            errorTV.setVisibility(View.VISIBLE);
                        }
*/
                            } else if (selectedObject.toString().equalsIgnoreCase("Download")) {

                                String filePath = item.getDocPath().replace("~", "");
                                String path = CommunicationConstant.UrlFile + filePath + "/" + item.getDocFile();

                                Utility.downloadPdf(path, null, item.getDocFile(), context, getActivity());
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
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progress.dismiss();
        }
    }

    public static class FileDownloader {
        private static final int MEGABYTE = 1024 * 1024;

        public static void downloadFile(String fileUrl, File directory) {
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                //  InputStream inputStream = urlConnection.getInputStream();
                InputStream inputStream = new URL(fileUrl).openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();
                //Toast.makeText(context,"Starting PDF download...",Toast.LENGTH_LONG).show();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                   /* downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalSize) * 100;
                    Toast.makeText(context,"Total PDF File size  : "
                            + (totalsize / 1024)
                            + " KB\n\nDownloading PDF " + (int) per
                            + "% complete",Toast.LENGTH_LONG).show();*/
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
