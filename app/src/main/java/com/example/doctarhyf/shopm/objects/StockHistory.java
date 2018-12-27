package com.example.doctarhyf.shopm.objects;

import android.os.Bundle;

public class StockHistory {
    public static final String KEY_DATE = "sh_date";
    public static final String KEY_OLD_STOCK = "sh_old";
    public static final String KEY_NEW_STOCK = "sh_new";
    public static final String KEY_ID = "sh_id";
    public static final String KEY_ITEM_ID = "sh_item_id";
    private  Bundle data = null;

    public StockHistory(){

    }

    public StockHistory(Bundle newData){
        this.data = newData;
    }

    public String getProp(String k){

        String v = "no_val";

        v = data == null ? v : data.getString(k);


        return v;
    }

    public static StockHistory FromJSON(String s) {
        return null;
    }
}
