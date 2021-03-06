package com.example.doctarhyf.shopm.objects;

import android.os.Bundle;

public class SellsItem {

    public static final String KEY_SELL_QTY = "sell_qty";
    public static final String KEY_ITEM_NAME = Item.KEY_ITEM_NAME;
    public static final String KEY_ITEM_CUR_PRICE = "sell_item_cur_price";
    public static final String KEY_ITEM_TOTAL_SELLS = "sell_total";
    public static final String KEY_ITEM_ID = Item.KEY_ITEM_ID;
    public static final String TOT_QTY = "tot_qty";
    public static final String TOT_CASH = "tot_cash";
    public static final String KEY_ITEM_PA = "PA";
    public static final String KEY_ITEM_PV = "PV" ;
    public static final String KEY_ITEM_B = "B";
    private Bundle data = new Bundle();

    public SellsItem(){

    }

    public SellsItem(Bundle data) {
        this.data = data;
    }

    public String getDataValue(String k){

        return this.data.getString(k, "no_val");
    }
}
