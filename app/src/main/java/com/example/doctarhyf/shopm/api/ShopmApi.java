package com.example.doctarhyf.shopm.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.doctarhyf.shopm.adapters.AdapterHomeItems;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.objects.SellsItem;
import com.example.doctarhyf.shopm.objects.StockHistory;
import com.example.doctarhyf.shopm.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopmApi {

    private static final String TAG = Utils.TAG;
    public static final String SV_SERVER_ADD = "serverAdd";
    private static final String KEY_SESSION_DATA_EMPTY = "no_session_var";
    private static final String ACTION_LOAD_ALL_ITEMS = "loadAllItems";
    private static final String ACTION_LOAD_ITEM = "loadItem";
    public static final String SV_EXCH_RATE = "exchRate";
    public static final String SV_DEF_EXCH_RATE = "1600";
    public static final String SV_MAX_SELLABLE_NUM = "maxSellableItemNum";
    public static final String SV_DEF_MAX_SELLABLE_NUM = "30";
    public static final String SV_DEF_SERVER_ADD = "192.168.1.3";
    private static final String ACTION_SELL_ITEM = "sellItem";
    private static final String ACTION_ADD_ITEM_TO_STOCK = "addItemToStock";
    private static final String ACTION_DEL_ITEM = "delItem";
    private static final String ACTION_UPDATE_ITEM = "updItem";
    private static final String ACTION_GET_ITEM_DAILY_SELLS = "getItemDaillySells";
    private static final String ACTION_GET_ITEM_MONTHLY_SELLS = "getItemMonthlySells";
    public static final String SV_REPPORT_EMAIL = "repportEmail";
    public static final String SV_DEF_REPPORT_EMAIL = "drrhyf@gmail.com";
    private static final String ACTION_GEN_PDF_REPPORT = "genSellsPDFRepport";
    private static final String ACTION_LOAD_ITEM_STOCK_HISTORY = "loadItemStockHistory";
    public static String API_URL = "shopm/api.php?";
    private final Context context;
    private final SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    //public final String SV_DEF_SERVER_ADD = "";
    //private BitmapCacheManager bitmapCacheManager;

    //AlertDialog alertDialog;
    public ShopmApi(Context context) {
        this.context = context;
        //setBitmapCacheManager(new BitmapCacheManager(context));
        preferences = context.getSharedPreferences(Utils.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = getPreferences().edit();

        initSessionVar(SV_SERVER_ADD, SV_DEF_SERVER_ADD);
        initSessionVar(SV_EXCH_RATE, SV_DEF_EXCH_RATE);
        initSessionVar(SV_MAX_SELLABLE_NUM, SV_DEF_MAX_SELLABLE_NUM);

        //setupAlertDialogResponse();
        //SSV(KEY_NEW_ITEM_UNIQUE_ID, null);

        //Log.e(TAG, "FUCK -> " + GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));

        //alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(context, HelperMethods.getStringResource(this, R.string.pbMsgProcessing),false);

        //getAllItemCats();
    }

    private void initSessionVar(String kVarName, String defVal) {
        String data = GSV(kVarName, defVal);
        //String exchRate = GSV(SV_EXCH_RATE);

        if(data.equals("")) {
            editor.putString(kVarName, defVal);
            editor.commit();
        }
    }

    public void SSV(String key, String val) {
        setSessionVar(key, val);
    }

    public void setSessionVar(String key, String val) {

        editor.putString(key, val);
        editor.apply();
        editor.commit();
    }

    public String GSA() {
        return GetServerAddress();
    }

    public String GetServerAddress() {



        return "http://" + GSV(SV_SERVER_ADD, SV_DEF_SERVER_ADD) + "/";
    }

    public String GSV(String key, String defVal) {
        return getSessionVar(key, defVal);
    }

    public String getSessionVar(String key, String defVal) {

        return getPreferences().getString(key, defVal);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void updateItem(final CallbackAPIActionConfirmation callback, Item newItem) {
        final String actName = ShopmApi.ACTION_UPDATE_ITEM;
        //String url = GSA() + API_URL + "act=" + actName + "&item_id=" + item_id;

        String item_id = newItem.getItem_id();
        String item_name = newItem.getItem_name();
        String item_price = newItem.getItem_price();
        String item_stock_count = newItem.getItem_stock_count();
        String item_desc = newItem.getItem_desc();

        String url = GSA() + API_URL + "act=" + actName + "&item_name=" + item_name + "&item_id=" + item_id +
                "&item_price=" + item_price + "&item_stock_count=" + item_stock_count + "&item_desc=" + item_desc;

        //Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            //Log.e(TAG, "onResponse: FAAKK" );
                            callback.onActionFailure(actName, s);
                        }else{
                            callback.onActionSuccess(actName, s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        callback.onActionFailure(actName,volleyError.getMessage());
                    }
                }
        );

        ShopmApplication.GI().addToRequestQueue(request);
    }



    public void generateReportPDF(final CallbackAPIActionConfirmation callback, String mSellDataType, String mPeriode) {

        Log.e(TAG, "mPeriode : " + mPeriode );

        String[] dataComp = mPeriode.split("/"); //2018/12/20
        String y = dataComp[0];
        String m = dataComp[1];
        String d = "";
        d = dataComp.length > 2 ? dataComp[2] : "";

        final String actName = ShopmApi.ACTION_GEN_PDF_REPPORT;
        String url = GSA() + API_URL + "act=" + actName + "&sellsType=" + mSellDataType + "&y=" +  y + "&m=" + m + "&d=" + d + "&save";

        Log.e(TAG, "generateReportPDF: url -> " + url );
        //http://localhost/shopm/api.php?act=genSellsPDFRepport&sellsType=dailly&y=2018&m=12&d=20
        //Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            //Log.e(TAG, "onResponse: FAAKK" );
                            callback.onActionFailure(actName, s);
                        }else{
                            callback.onActionSuccess(actName, s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        callback.onActionFailure(actName, volleyError.getMessage());
                    }
                }
        );

        ShopmApplication.GI().addToRequestQueue(request);
    }

    public interface CallbackAPIActionConfirmation {
        void onActionSuccess(String actionName, String data);
        void onActionFailure(String actionName, String data);
    }

    public void deleteItem(final CallbackAPIActionConfirmation callback, String item_id) {


        final String actName = ShopmApi.ACTION_DEL_ITEM;
        String url = GSA() + API_URL + "act=" + actName + "&item_id=" + item_id;

        //Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            //Log.e(TAG, "onResponse: FAAKK" );
                            callback.onActionFailure(actName, s);
                        }else{
                            callback.onActionSuccess(actName, s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        callback.onActionFailure(actName, volleyError.getMessage());
                    }
                }
        );

        ShopmApplication.GI().addToRequestQueue(request);

    }

    public interface CallbackStock {
        void onItemAddToStockSuccess(String itemData);
        void onItemAddToStockError(String errorMessage);
    }

    public void addItemToStock(final CallbackStock callbackStock, Bundle itemData) {

        String item_name = itemData.getString(Item.KEY_ITEM_NAME);
        String item_price = itemData.getString(Item.KEY_ITEM_PRICE);
        String item_stock_count = itemData.getString(Item.KEY_ITEM_INIT_STOCK);
        String item_desc = itemData.getString(Item.KEY_ITEM_DESC);

        String url = GSA() + API_URL + "act=" + ShopmApi.ACTION_ADD_ITEM_TO_STOCK + "&item_name=" + item_name +
                "&item_price=" + item_price + "&item_stock_count=" + item_stock_count + "&item_desc=" + item_desc;

        //Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            //Log.e(TAG, "onResponse: FAAKK" );
                            callbackStock.onItemAddToStockError(s);
                        }else{
                            callbackStock.onItemAddToStockSuccess(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        callbackStock.onItemAddToStockError(volleyError.getMessage());
                    }
                }
        );

        ShopmApplication.GI().addToRequestQueue(request);

    }

    public interface CallbacksSellItem{

        void onItemSellError(String errorMessage);

        void onItemSellSuccess(String itemJson);
    }

    public void sellItem(final CallbacksSellItem callbacksSellItem, String item_id, String item_qty, String exch_rate, String rem_stock, String item_cur_price) {

        String url = GSA() + API_URL + "act=" + ShopmApi.ACTION_SELL_ITEM + "&item_id=" + item_id +
                "&item_qty=" + item_qty + "&exch_rate=" + exch_rate + "&rem_stock=" + rem_stock + "&sell_item_cur_price=" + item_cur_price;

        Log.e(TAG, "sellItem: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, "onResponse: FAKREZ -> " + s );
                        if(s.equals("false")){
                            //Log.e(TAG, "onResponse: FAAKK" );
                            callbacksSellItem.onItemSellError(s);
                        }else{
                            callbacksSellItem.onItemSellSuccess(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: -> " + volleyError.getMessage() );
                        callbacksSellItem.onItemSellError(volleyError.getMessage());
                    }
                }
        );

        ShopmApplication.GI().addToRequestQueue(request);
    }

    public interface CallbackLoadItem{

        void onItemLoaded(String itemJson);
        void onItemNotFound();
    }

    public static boolean IsOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void loadItemByUniqueID(final CallbackLoadItem callback, String barcodeMessage) {

        String url = GSA() + API_URL + "act=" + ShopmApi.ACTION_LOAD_ITEM + "&item_unique_name=" + barcodeMessage ;

       // Log.e(TAG, "loadAllItems: url -> "  + url );
        //Log.e(TAG, "loadItemByUniqueID: url -> " + url );

        JsonArrayRequest request = new JsonArrayRequest(

                url,

                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        //Log.e(TAG, "onResponse: DALEN -> " + jsonArray.length() );

                        if(jsonArray.length() == 1){

                            try {
                                callback.onItemLoaded(jsonArray.get(0).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            callback.onItemNotFound();
                        }

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }

        );


        ShopmApplication.GI().addToRequestQueue(request);



    }

    public interface CallbacksItemSells {
        void onItemsLoaded(List<SellsItem> newSellsItems, String tot_qty, String tot_cash);

        void onItemsLoadeError(String errorMessage);

        void onEmptyList();
    }

    public void loadItemSells(final CallbacksItemSells callbacks, String sellsDataType, String periode){

        final String sellsAct = sellsDataType == Utils.SELL_DATA_TYPE_DAILY ? ShopmApi.ACTION_GET_ITEM_DAILY_SELLS : ShopmApi.ACTION_GET_ITEM_MONTHLY_SELLS;
        String[] arrPeriode = periode.split("/");
        String y = arrPeriode[0];
        String m = arrPeriode[1];
        String d = "";
        if(arrPeriode.length == 3) d = arrPeriode[2];

        String url = GSA() + API_URL + "act=" + sellsAct + "&y=" + y + "&m=" + m + "&d=" + d ;

        Log.e(TAG, "loadItemSells: url -> "  + url );

        JsonArrayRequest request = new JsonArrayRequest(
                url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        Log.e(TAG, "onResponse: da sells data -> " + jsonArray.toString() );

                        if(jsonArray.length() > 0){

                            List<SellsItem> sellsItems = new ArrayList<>();

                            for(int i = -1; i < jsonArray.length() - 1; i++){
                                Bundle data = new Bundle();

                                SellsItem sellsItem = new SellsItem(data);
                                try {

                                    if(i == -1){
                                        data.putString(SellsItem.KEY_ITEM_ID, i + "");
                                        data.putString(SellsItem.KEY_ITEM_NAME, "No. Article");
                                        data.putString(SellsItem.KEY_SELL_QTY, "Qte");
                                        data.putString(SellsItem.KEY_ITEM_CUR_PRICE, "PU" );
                                        data.putString(SellsItem.KEY_ITEM_TOTAL_SELLS, "PT");
                                    }else {
                                        JSONObject json = jsonArray.getJSONObject(i);

                                        data.putString(Item.KEY_ITEM_JSON, json.toString());

                                        String name = json.getString(SellsItem.KEY_ITEM_NAME);
                                        String qty = json.getString(SellsItem.KEY_SELL_QTY);
                                        String pu = json.getString(SellsItem.KEY_ITEM_CUR_PRICE);


                                        data.putString(SellsItem.KEY_ITEM_ID, i + "");
                                        data.putString(SellsItem.KEY_ITEM_NAME, (i + 1) + ". " + name);
                                        data.putString(SellsItem.KEY_SELL_QTY, qty);
                                        data.putString(SellsItem.KEY_ITEM_CUR_PRICE, pu);
                                        data.putString(SellsItem.KEY_ITEM_TOTAL_SELLS, (Integer.parseInt(pu) * Integer.parseInt(qty)) + "");
                                    }



                                    sellsItems.add(sellsItem);




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    callbacks.onItemsLoadeError(e.getMessage());
                                }


                            }

                            String tot_qty = "0";
                            String tot_cash = "0";

                            try {
                                JSONObject tot = jsonArray.getJSONObject(jsonArray.length()-1);
                                tot_qty = tot.getString(SellsItem.TOT_QTY);
                                tot_cash = tot.getString(SellsItem.TOT_CASH);


                                //sellsItems.add(new SellsItem())

                            } catch (JSONException e) {
                                e.printStackTrace();
                                callbacks.onItemsLoadeError(e.getMessage());
                            }


                            callbacks.onItemsLoaded(sellsItems, tot_qty, tot_cash);

                        }else if(jsonArray.length() == 0){
                            callbacks.onEmptyList();
                        }
                        /*try {
                            JSONArray data = jsonObject.getJSONArray("data");

                            List<Item> items = new ArrayList<>();

                            for(int i = 0; i < data.length(); i++){

                                JSONObject jo = data.getJSONObject(i);

                                Item item = Item.FromJSON(jo.toString());

                                items.add(item);

                                callbacks.onItemsLoaded(items);

                                //Log.e(TAG, "onResponse: da item json -> " + item.toJSON() );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: err -> \"" + volleyError.getMessage() + "\"" );

                        callbacks.onItemsLoadeError(volleyError.getMessage());

                    }
                }

        );



        ShopmApplication.GI().addToRequestQueue(request);

    }



    public interface CallbacksItems {
        void onItemsLoaded(List<Item> items);

        void onItemsLoadeError(String errorMessage);
    }

    public void loadAllItems(final CallbacksItems callbacks) {

        String url = GSA() + API_URL + "act=" + ShopmApi.ACTION_LOAD_ALL_ITEMS ;

        Log.e(TAG, "LDFEKZ: url -> "  + url );

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        //Log.e(TAG, "onResponse: DAOBJ -> " + jsonObject.toString() );

                        try {
                            JSONArray data = jsonObject.getJSONArray("data");

                            List<Item> items = new ArrayList<>();

                            for(int i = 0; i < data.length(); i++){

                                JSONObject jo = data.getJSONObject(i);

                                Item item = Item.FromJSON(jo.toString());

                                items.add(item);

                                callbacks.onItemsLoaded(items);

                                //Log.e(TAG, "onResponse: da item json -> " + item.toJSON() );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: err -> \"" + volleyError.getMessage() + "\"" );

                        callbacks.onItemsLoadeError(volleyError.getMessage());

                    }
                }

        );



        ShopmApplication.GI().addToRequestQueue(request);


        /*StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject res = new JSONObject(s);
                            int code = res.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE);
                            List<ProductMyProducts> products = null;

                            if (code == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS) {

                                String data = res.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);

                                JSONArray items = new JSONArray(data);

                                if (items.length() == 0) {
                                    callbacks.onLoadAllItemsResult(NETWORK_RESULT_CODES.RESULT_CODE_EMPTY_LIST, null);
                                } else {

                                    products = new ArrayList<>();
                                    for (int i = 0; i < items.length(); i++) {

                                        JSONObject jo = items.getJSONObject(i);

                                        ProductMyProducts pd = new ProductMyProducts(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(ProductWishList.KEY_DATE_ADDED));


                                        //Log.e(TAG, "date -> " + dateJson );

                                        Bundle b = new Bundle();
                                        HelperMethods.PutAllJSONIntoBundle(jo, b);
                                        b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                        b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                        b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                        b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                        b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                        b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));
                                        b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                        String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                        //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                        String postedDate = HM.CLDTAS(context,
                                                HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                        b.putString(Product.KEY_PD_DATE_ADDED, postedDate);

                                        pd.setDataBundle(b);


                                        products.add(pd);


                                    }

                                }


                            }

                            callbacks.onLoadAllItemsResult(code, products);


                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener(VolleyError) {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onLoadAllItemsNetworkError(volleyError.getMessage());
                    }
                }
        );*/
        //ShopmApplication.GI().addToRequestQueue(request);
    }

    public interface CallbacksStockHistoryItems {
        void onItemsLoaded(List<StockHistory> items);

        void onItemsLoadeError(String errorMessage);
    }

    public void loadItemStockHistory(final CallbacksStockHistoryItems callbacks, String itemID) {

        String url = GSA() + API_URL + "act=" + ShopmApi.ACTION_LOAD_ITEM_STOCK_HISTORY + "&item_id=" + itemID ;

        Log.e(TAG, "load history : url -> "  + url );

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        //Log.e(TAG, "onResponse: DAOBJ -> " + jsonObject.toString() );

                        try {
                            JSONArray data = jsonObject.getJSONArray("data");

                            List<StockHistory> items = new ArrayList<>();

                            for(int i = -1; i < data.length(); i++){



                                if(i == -1){

                                    //jo = data.getJSONObject(i);

                                    Bundle bundle = new Bundle();
                                    bundle.putString(StockHistory.KEY_ID, "-1");//jo.getString(StockHistory.KEY_ID));
                                    bundle.putString(StockHistory.KEY_ITEM_ID, "id");// jo.getString(StockHistory.KEY_ITEM_ID));
                                    bundle.putString(StockHistory.KEY_OLD_STOCK, "Encien");//jo.getString(StockHistory.KEY_OLD_STOCK));
                                    bundle.putString(StockHistory.KEY_NEW_STOCK, "Nouveau");//jo.getString(StockHistory.KEY_NEW_STOCK));
                                    bundle.putString(StockHistory.KEY_DATE, "Date");//jo.getString(StockHistory.KEY_DATE));

                                    items.add(new StockHistory(bundle));

                                }else {
                                    JSONObject jo = data.getJSONObject(i);

                                    Bundle bundle = new Bundle();
                                    bundle.putString(StockHistory.KEY_ID, jo.getString(StockHistory.KEY_ID));
                                    bundle.putString(StockHistory.KEY_ITEM_ID, jo.getString(StockHistory.KEY_ITEM_ID));
                                    bundle.putString(StockHistory.KEY_OLD_STOCK, jo.getString(StockHistory.KEY_OLD_STOCK));
                                    bundle.putString(StockHistory.KEY_NEW_STOCK, jo.getString(StockHistory.KEY_NEW_STOCK));
                                    bundle.putString(StockHistory.KEY_DATE, jo.getString(StockHistory.KEY_DATE));

                                    items.add(new StockHistory(bundle));//StockHistory.FromJSON(jo.toString());

                                }
                                //(item);



                                //Log.e(TAG, "onResponse: da item json -> " + item.toJSON() );
                            }

                            callbacks.onItemsLoaded(items);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: err -> \"" + volleyError.getMessage() + "\"" );

                        callbacks.onItemsLoadeError(volleyError.getMessage());

                    }
                }

        );



        ShopmApplication.GI().addToRequestQueue(request);


        /*StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject res = new JSONObject(s);
                            int code = res.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE);
                            List<ProductMyProducts> products = null;

                            if (code == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS) {

                                String data = res.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);

                                JSONArray items = new JSONArray(data);

                                if (items.length() == 0) {
                                    callbacks.onLoadAllItemsResult(NETWORK_RESULT_CODES.RESULT_CODE_EMPTY_LIST, null);
                                } else {

                                    products = new ArrayList<>();
                                    for (int i = 0; i < items.length(); i++) {

                                        JSONObject jo = items.getJSONObject(i);

                                        ProductMyProducts pd = new ProductMyProducts(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(ProductWishList.KEY_DATE_ADDED));


                                        //Log.e(TAG, "date -> " + dateJson );

                                        Bundle b = new Bundle();
                                        HelperMethods.PutAllJSONIntoBundle(jo, b);
                                        b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                        b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                        b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                        b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                        b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                        b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));
                                        b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                        String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                        //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                        String postedDate = HM.CLDTAS(context,
                                                HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                        b.putString(Product.KEY_PD_DATE_ADDED, postedDate);

                                        pd.setDataBundle(b);


                                        products.add(pd);


                                    }

                                }


                            }

                            callbacks.onLoadAllItemsResult(code, products);


                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener(VolleyError) {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onLoadAllItemsNetworkError(volleyError.getMessage());
                    }
                }
        );*/
        //ShopmApplication.GI().addToRequestQueue(request);
    }

}
