package com.example.doctarhyf.shopm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
