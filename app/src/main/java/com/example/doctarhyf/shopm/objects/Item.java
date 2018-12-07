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
    private String itemID = "0";
    private String itemDesc = "The Description";
    private String itemName = "Item Name";
    private String itemPrice = "$00.00";
    private String itemUniqueName = "";
    private int itemStockCount = 0;
    //private String itemCategory;

    //public static final String ITEM_CATEGORY_ELEC = "catElec";
    //public static final String ITEM_CATEGORY_CLOTH = "catCloth";

    public Item(){

    }

    public Item(String itemID, String itemName, String itemPrice, String itemDesc, String itemUniqueName, int itemStockCount){
        this.setItemID(itemID);
        this.setItemName(itemName);
        this.setItemPrice(itemPrice);
        this.setItemDesc(itemDesc);
        this.setItemUniqueName(itemUniqueName);
        this.setItemStockCount(itemStockCount);
        //this.setItemCategory(itemCategory);
    }



    public static Item FromJSON(String jsonData){



        return new Gson().fromJson(jsonData, Item.class);
    }


    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }



    public String toJSON() {

        JSONObject jsonObject = new JSONObject();

        return new GsonBuilder().create().toJson(this, Item.class);
    }

    public String getItemUniqueName() {
        return itemUniqueName;
    }

    public void setItemUniqueName(String itemUniqueName) {
        this.itemUniqueName = itemUniqueName;
    }

    public int getItemStockCount() {
        return itemStockCount;
    }

    public void setItemStockCount(int itemStockCount) {
        this.itemStockCount = itemStockCount;
    }
}
