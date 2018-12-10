package com.example.doctarhyf.shopm.utils;

import android.content.Context;
import android.widget.EditText;

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

    public static String GetEditTextValue(Context context, EditText et){
        return et.getText().toString();
    }


}
