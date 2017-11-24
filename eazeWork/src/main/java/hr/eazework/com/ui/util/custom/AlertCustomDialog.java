package hr.eazework.com.ui.util.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hr.eazework.com.R;


public class AlertCustomDialog {

    public interface AlertClickListener {
        void onPositiveBtnListener();

        void onNegativeBtnListener();
    }

    private AlertDialog alertDialog1;

    public AlertCustomDialog(Context context, String message) {
        this(context, null, message);
    }

    public AlertCustomDialog(Context context, String title, String message) {
        this(context, title, message, null);
    }

    public AlertCustomDialog(Context context, String message,
                             AlertClickListener listener) {
        this(context, message, null, null, listener);
    }

    public AlertCustomDialog(Context context, String title, String message,
                             AlertClickListener listener) {
        this(context, title, message, null, null, listener);
    }

    

    public AlertCustomDialog(Context context, String message,
                             String negativeBtn, String positiveBtn, AlertClickListener listener) {
        this(context, null, message, negativeBtn, positiveBtn, listener);
    }

    public AlertCustomDialog(Context context, String message, String positiveBtn, boolean isMaterial, final AlertClickListener listener) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context,
                isMaterial ? R.style.MyDialogTheme
                        : R.style.MyDialogThemeTransparent);
        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(ctw);
        // set title
        String titleStr = "Information";

        if (isMaterial) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.material_dialog_layout, null, false);
            alertDialogBuilder1.setView(view);
            ((TextView) view.findViewById(R.id.tv_title)).setText(titleStr);
            ((TextView) view.findViewById(R.id.tv_message)).setText(message);
            ((TextView) view.findViewById(R.id.tv_cancel)).setVisibility(View.GONE);

            ((TextView) view.findViewById(R.id.tv_ok)).setText(positiveBtn != null ? positiveBtn : "Upgrade");

            view.findViewById(R.id.tv_ok).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog1.cancel();
                            if (listener != null) {
                                listener.onPositiveBtnListener();
                            }
                        }
                    });
        } else {
            alertDialogBuilder1.setTitle(titleStr);
            if (message != null)
                alertDialogBuilder1.setMessage(message);
            alertDialogBuilder1.setCancelable(false);





            if (positiveBtn != null) {
                alertDialogBuilder1.setPositiveButton(positiveBtn,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (listener != null) {
                                    listener.onPositiveBtnListener();
                                }
                            }
                        });
            }
        }
        // create alert dialog Commented as it is not in use
        alertDialog1 = alertDialogBuilder1.create();

        // show it To show the Alert dialogue build above
        alertDialog1.show();
    }

    public AlertCustomDialog(Context context, String title, String message,
                             String negativeBtn, String positiveBtn,
                             final AlertClickListener listener, boolean isMaterial) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context,
                isMaterial ? R.style.MyDialogTheme
                        : R.style.MyDialogThemeTransparent);
        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(ctw);
        // set title
        String titleStr = title;
        if (titleStr == null) {
            if (positiveBtn == null)
                titleStr = "Information";
            else
                titleStr = "Confirmation";
        }
        if (isMaterial) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.material_dialog_layout, null, false);
            alertDialogBuilder1.setView(view);
            ((TextView) view.findViewById(R.id.tv_title)).setText(titleStr);
            ((TextView) view.findViewById(R.id.tv_message)).setText(message);
            if (negativeBtn != null) {
                ((TextView) view.findViewById(R.id.tv_cancel)).setText(negativeBtn);
                ((TextView) view.findViewById(R.id.tv_cancel)).setVisibility(View.VISIBLE);
                view.findViewById(R.id.tv_cancel).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alertDialog1.cancel();
                                if (listener != null) {
                                    listener.onNegativeBtnListener();
                                }
                            }
                        });
            } else {
                ((TextView) view.findViewById(R.id.tv_cancel)).setVisibility(View.GONE);
            }
            ((TextView) view.findViewById(R.id.tv_ok)).setText(positiveBtn != null ? positiveBtn : context.getString(R.string.dlg_ok));

            view.findViewById(R.id.tv_ok).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog1.cancel();
                            if (listener != null) {
                                listener.onPositiveBtnListener();
                            }
                        }
                    });
        } else {
            alertDialogBuilder1.setTitle(titleStr);
            if (message != null)
                alertDialogBuilder1.setMessage(message);
            alertDialogBuilder1.setCancelable(false);

            if (positiveBtn == null && negativeBtn == null) {
                positiveBtn = context.getString(R.string.dlg_ok);
            }

            if (negativeBtn != null) {
                alertDialogBuilder1.setNegativeButton(negativeBtn,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (listener != null) {
                                    listener.onNegativeBtnListener();
                                }
                            }
                        });
            }

            if (positiveBtn != null) {
                alertDialogBuilder1.setPositiveButton(positiveBtn,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (listener != null) {
                                    listener.onPositiveBtnListener();
                                }
                            }
                        });
            }
        }
        // create alert dialog Commented as it is not in use
        alertDialog1 = alertDialogBuilder1.create();

        // show it To show the Alert dialogue build above
        alertDialog1.show();
    }

    public AlertCustomDialog(Context context, String title, String message,
                             String negativeBtn, String positiveBtn,
                             final AlertClickListener listener) {
        this(context, title, message, negativeBtn, positiveBtn, listener, true);
    }

    public AlertCustomDialog(Context context, View view, int clickableId,
                             final AlertClickListener listener) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context,
                R.style.MyDialogThemeTransparent);
        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(ctw);

        alertDialogBuilder1.setCancelable(false);

        View clickView = view.findViewById(clickableId);
        if (clickView != null) {
            clickView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog1.cancel();
                    if (listener != null) {
                        listener.onPositiveBtnListener();
                    }
                }
            });
        }
        alertDialogBuilder1.setView(view);
        // create alert dialog Commented as it is not in use
        alertDialog1 = alertDialogBuilder1.create();

        // show it To show the Alert dialogue build above
        alertDialog1.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog1;
    }
}