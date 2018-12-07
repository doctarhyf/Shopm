package com.example.doctarhyf.shopm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.doctarhyf.shopm.utils.Utils;

public class ActivitySellItem extends AppCompatActivity {


    private String itemUniqueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        getSupportActionBar().setTitle(getResources().getString(R.string.strSellItem));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        itemUniqueName = intent.getStringExtra(Utils.ITEM_UNIQUE_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
