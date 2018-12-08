package com.example.doctarhyf.shopm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

        final TextView tvItemName = findViewById(R.id.tvItemName);
        TextView tvItemPrice = findViewById(R.id.tvItemPrice);
        final TextView tvItemTotalPrice = findViewById(R.id.tvItemTotalPrice);

        tvItemName.setText(item.getItem_name());
        tvItemPrice.setText(item.getItem_price() + " FC");

        int maxSellable = Integer.parseInt(ShopmApplication.GI().getApi().GSV(ShopmApi.SV_MAX_SELLABLE_NUM, ShopmApi.SV_DEF_MAX_SELLABLE_NUM));
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
                tvItemTotalPrice.setText(((int)(itemPrice * (i+1)) + " FC"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell_item, menu);

        return true;

    }

    public void sellItem(View view){
        Log.e(TAG, "sellItem: " );
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
