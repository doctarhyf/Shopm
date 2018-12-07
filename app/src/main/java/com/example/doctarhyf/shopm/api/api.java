package com.example.doctarhyf.shopm.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class api {

    private static final String TAG = Utils.TAG;
    public static final String SERVER_ADD = "serverAdd";
    private static final String KEY_SESSION_DATA_EMPTY = "no_session_var";
    private static final String ACTION_LOAD_ALL_ITEMS = "loadAllItems";
    public static String API_URL = "shopm/api.php?";
    private final Context context;
    private final SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private final String DEF_IP = "192.168.88.16";
    //private BitmapCacheManager bitmapCacheManager;

    //AlertDialog alertDialog;
    public api(Context context) {
        this.context = context;
        //setBitmapCacheManager(new BitmapCacheManager(context));
        preferences = context.getSharedPreferences(Utils.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = getPreferences().edit();

        editor.putString(SERVER_ADD, DEF_IP);
        editor.commit();

        //setupAlertDialogResponse();
        //SSV(KEY_NEW_ITEM_UNIQUE_ID, null);

        //Log.e(TAG, "FUCK -> " + GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));

        //alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(context, HelperMethods.getStringResource(this, R.string.pbMsgProcessing),false);

        //getAllItemCats();
    }

    public String GSA() {
        return GetServerAddress();
    }

    public String GetServerAddress() {



        return "http://" + GSV(SERVER_ADD) + "/";
    }

    public String GSV(String key) {
        return getSessionVar(key);
    }

    public String getSessionVar(String key) {

        return getPreferences().getString(key, KEY_SESSION_DATA_EMPTY);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public interface CallbacksItems {
        void onItemsLoaded(List<Item> items);
        //void onItemPublishResult(int code, String data);

        //void onLoadAllItemsResult(int code, List<ProductMyProducts> products);

        //void onLoadAllItemsNetworkError(String message);
    }

    public void loadAllItems(final CallbacksItems callbacks) {

        String url = GSA() + API_URL + "act=" + api.ACTION_LOAD_ALL_ITEMS ;

        Log.e(TAG, "loadAllItems: url -> "  + url );

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
                        Log.e(TAG, "onErrorResponse: err -> " + volleyError.getMessage() );
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
