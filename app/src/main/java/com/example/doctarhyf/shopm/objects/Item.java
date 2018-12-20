package com.example.doctarhyf.shopm.objects;

import com.example.doctarhyf.shopm.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by rhyfdocta on 11/10/17.
 */

public class Item {

    //public static final String ITEM_CATEGORY_TREND = "catTrend";
    private static final String TAG = Utils.TAG;
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_ITEM_PRICE = "item_price";
    public static final String KEY_ITEM_INIT_STOCK = "item_stock_count";
    public static final String KEY_ITEM_DESC = "item_desc";
    public static final String KEY_ITEM_JSON = "itemJson";
    public static final String KEY_ITEM_ID = "item_id";

    //{"item_id":"3","item_name":"myitem","item_desc":"thedesc","item_price":"2000","item_unique_name":"abd3b9d1c3cfb968eda57808034072f167b181c8294375283bf8237d81cc30eb","item_stock_count":"100","item_last_stock_upd":"0000-00-00 00:00:00","item_added_date":"0000-00-00 00:00:00"}

    private String item_id = "0";
    private String item_name = "Item Name";
    private String item_desc = "The Description";

    private String item_price = "$00.00";
    private String item_unique_name = "";
    private String item_stock_count = "";
    private String item_last_stock_upd = "";
    private String item_added_date ="";
    //private String itemCategory;

    //public static final String ITEM_CATEGORY_ELEC = "catElec";
    //public static final String ITEM_CATEGORY_CLOTH = "catCloth";

    public Item(){

    }

    @Override
    public String toString() {
        return "ITEM TOSTRING -> " + toJSON();
    }

    public Item(String item_id, String item_name, String item_price, String item_desc, String item_unique_name, String item_stock_count,
                String item_last_stock_upd, String item_added_date){
        this.setItem_id(item_id);
        this.setItem_name(item_name);
        this.setItem_price(item_price);
        this.setItem_desc(item_desc);
        this.setItem_unique_name(item_unique_name);
        this.setItem_stock_count(item_stock_count);
        //this.setItemCategory(itemCategory);
    }




    public static Item FromJSON(String jsonData){



        return new Gson().fromJson(jsonData, Item.class);
    }


    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }



    public String toJSON() {

        JSONObject jsonObject = new JSONObject();

        return new GsonBuilder().create().toJson(this, Item.class);
    }

    public String getItem_unique_name() {
        return item_unique_name;
    }

    public void setItem_unique_name(String item_unique_name) {
        this.item_unique_name = item_unique_name;
    }

    public String getItem_stock_count() {
        return item_stock_count;
    }

    public void setItem_stock_count(String item_stock_count) {
        this.item_stock_count = item_stock_count;
    }

    public String getItem_last_stock_upd() {
        return item_last_stock_upd;
    }

    public void setItem_last_stock_upd(String item_last_stock_upd) {
        this.item_last_stock_upd = item_last_stock_upd;
    }

    public String getItem_added_date() {
        return item_added_date;
    }

    public void setItem_added_date(String item_added_date) {
        this.item_added_date = item_added_date;
    }
}
