package com.example.doctarhyf.shopm.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

import com.example.doctarhyf.shopm.R;

public class Utils {

    public static final String TAG = "SHOPM";
    public static final int BARCODE_READER_REQUEST_CODE = 100;
    public static final String SHARED_PREF_NAME = "sharedPrefs";
    public static final String JSON_RESP_CODE = "code";
    public static final int JSON_RESP_CODE_SUCCESS = 1;
    public static final String ITEM_UNIQUE_NAME = "item_unique_name";
    public static final String ITEM_JSON = "itemJson";
    public static final String STR_STOCK_PREFIX = "En stock : ";
    public static final String ITEM_SOLD = "itemSold";
    public static final String ROOT_FOLDER = "shopm";
    public static final String IMG_FOLDER_NAME = "img";
    public static final String ITEM_DELETED = "itemDeleted";
    public static final String ITEM_ADDED = "itemAdded";
    public static final String ITEM_UPDATED = "itemUpdated";

    public static String GetEditTextValue(Context context, EditText et){
        return et.getText().toString();
    }

    public static void SetEditTextValue(Context context, EditText et, String val){
         et.setText(val);
    }

    public interface ListernerAlertDialogWithTitleMessage{
        void onPositiveButton();
        void onNegativeButton();
    }


    public static AlertDialog GetAlertDialogWithTitleAndMessage(Context context, final ListernerAlertDialogWithTitleMessage listener, String title, String message, boolean show) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.e(TAG, "onClick: " );
                        listener.onPositiveButton();
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onNegativeButton();
                    }
                });


        AlertDialog alertDialog = builder.create();

        if(show) alertDialog.show();

        return alertDialog;

    }
}
