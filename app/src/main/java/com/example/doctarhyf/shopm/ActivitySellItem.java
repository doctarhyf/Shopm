package com.example.doctarhyf.shopm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doctarhyf.shopm.api.ShopmApi;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.barcode.BarcodeCaptureActivity;
import com.example.doctarhyf.shopm.fragments.FragmentSettings;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActivitySellItem extends AppCompatActivity {


    private static final String TAG = Utils.TAG;
    //private String itemJson;
    private Item item;
    private int mQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        Intent intent = getIntent();

        String itemJson = intent.getStringExtra(Utils.ITEM_JSON);

        item = Item.FromJSON(itemJson);

        getSupportActionBar().setTitle(item.getItem_name());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();

    }

    private void loadData() {

        final double itemPrice = Double.parseDouble(item.getItem_price());
        //final double itemTotalPrice = 0.0;

        ImageView imageView = findViewById(R.id.ivItemPic);

        String url = ShopmApplication.GI().getApi().GetServerAddress() + Utils.ROOT_FOLDER + "/" + Utils.IMG_FOLDER_NAME +
                "/" + item.getItem_unique_name() + ".jpg";

        Log.e("COOL", "onBindViewHolder: da url -> " + url );

        Uri uri = Uri.parse(url);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .error(R.drawable.no_img_found)
                //.placeholder(R.drawable.progress_animation)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);

        final TextView tvItemName = findViewById(R.id.tvItemName);
        TextView tvItemPrice = findViewById(R.id.tvItemPrice);
        final TextView tvItemTotalPrice = findViewById(R.id.tvItemTotalPrice);

        tvItemName.setText(item.getItem_name());
        tvItemPrice.setText(item.getItem_price() + " FC");

        int maxSellable = Integer.parseInt(item.getItem_stock_count());//Integer.parseInt(ShopmApplication.GI().getApi().GSV(ShopmApi.SV_MAX_SELLABLE_NUM, ShopmApi.SV_DEF_MAX_SELLABLE_NUM));
        Spinner spQty = findViewById(R.id.spQty);

        List<String> numbers = new ArrayList<>();

        for(int i = 0; i < maxSellable; i++){
            numbers.add( "" + (i+1));
        }



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                numbers);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQty.setAdapter(spinnerArrayAdapter);


        spQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int qty = i+1;
                updateItemQty(qty);
                tvItemTotalPrice.setText(((int)(itemPrice * qty) + " FC"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Log.e(TAG, "onNothingSelected: " );
            }
        });



        if(maxSellable == 0){
            Button btnSell = findViewById(R.id.btnSellItem);
            btnSell.setVisibility(View.GONE);

            View llItemSoldOut = findViewById(R.id.llItemSoldOut);

            llItemSoldOut.setVisibility(View.VISIBLE);
        }

    }

    private void updateItemQty(int qty) {
        mQty = qty;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell_item, menu);

        return true;

    }

    public void sellItem(View view){
        //Log.e(TAG, "sellItem: " );

        final ShopmApi api = ShopmApplication.GI().getApi();

        final String item_id = item.getItem_id();
        final String item_qty = "" + mQty;
        final String exch_rate = api.GSV(ShopmApi.SV_EXCH_RATE, ShopmApi.SV_DEF_EXCH_RATE);
        final String rem_stock = (Integer.parseInt(item.getItem_stock_count()) - mQty) + "";
        final String item_cur_price = (Integer.parseInt(item.getItem_price())) + "";

        String title = "Confirmer vente " + item.getItem_name();
        String message = "Article : " + item.getItem_name() + "\n" +
                "Prix Un. : " + item.getItem_price() + " FC\n" +
                "Qunantite : " + item_qty + "\n" +
                "--------------------------\n" +
                "Total : " + (Integer.parseInt(item_qty) * Integer.parseInt(item.getItem_price()) + " FC");

         Utils.GetAlertDialogWithTitleAndMessage(this,
                new Utils.ListernerAlertDialogWithTitleMessage() {
                    @Override
                    public void onPositiveButton() {

                        api.sellItem(new ShopmApi.CallbacksSellItem() {
                            @Override
                            public void onItemSellError(String errorMessage) {
                                Toast.makeText(ActivitySellItem.this, "Error selling item.\nError : " + errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onItemSellSuccess(String itemJson) {

                                Intent intent = new Intent(ActivitySellItem.this, ActivityHome.class);
                                intent.putExtra(Utils.ITEM_JSON, itemJson);
                                intent.putExtra(Utils.ITEM_SOLD, true);
                                startActivity(intent);

                            }
                        },item_id, item_qty, exch_rate, rem_stock, item_cur_price);

                    }

                    @Override
                    public void onNegativeButton() {

                    }
                },title,message, true);








    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan_item) {

            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, Utils.BARCODE_READER_REQUEST_CODE);

            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
