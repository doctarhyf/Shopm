package com.example.doctarhyf.shopm.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.doctarhyf.shopm.api.ShopMApi;

public class ShopmApplication extends Application {

    public static final String TAG = ShopmApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ProgressDialog progressDialog;

    private static ShopmApplication mInstance;

    private ShopMApi sosApi;

    private Context lastProgressDialogContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static String GSA(){
        return getInstance().getApi().GSA();
    }

    public static synchronized ShopmApplication GI(){
        return getInstance();
    }

    public void dissmissProgressDialog(){
        if(progressDialog != null) {
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    public void DPD(){
        dissmissProgressDialog();
    }

    public static synchronized ShopmApplication getInstance(){
        return mInstance;
    }

    public ShopMApi getApi(){
        if(sosApi == null){
            sosApi = new ShopMApi(getApplicationContext());
        }

        return sosApi;
    }

    public ProgressDialog GUPD(Context context, String title, String message){
        return getUndefinedProgressDialog(context, title, message);
    }

    public ProgressDialog getUndefinedProgressDialog(Context context, String title, String message){


        if(progressDialog == null){

            if(context != null) {

                if(lastProgressDialogContext == null){
                    lastProgressDialogContext = context;

                }else {
                    if(lastProgressDialogContext != context) lastProgressDialogContext = context;
                }



            }else{
                if(lastProgressDialogContext != null) {
                    lastProgressDialogContext = getApplicationContext();
                }
            }

            progressDialog = new ProgressDialog(lastProgressDialogContext);
        }


        progressDialog.setCancelable(false);

        progressDialog.setTitle(title);

        if(message != null){
            progressDialog.setMessage(message);
        }



        return progressDialog;

    }



    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}

