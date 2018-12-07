package com.example.doctarhyf.shopm;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.doctarhyf.shopm.adapters.AdapterHomeItems;
import com.example.doctarhyf.shopm.fragments.FragmentHome;
import com.example.doctarhyf.shopm.fragments.FragmentSellItem;
import com.example.doctarhyf.shopm.fragments.FragmentSettings;
import com.example.doctarhyf.shopm.fragments.FragmentViewItem;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

public class ActivityHome extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentHome.OnFragmentHomeInteractionListener,
        FragmentSettings.OnFragmentSettingsInteractionListener,
        FragmentSellItem.OnFragmentSellInteractionListener,
        AdapterHomeItems.Callbacks,
        FragmentViewItem.OnFragmentViewItemInteractionListener
        {


            private static final String TAG = Utils.TAG;
            //private View fragCont;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getSupportFragmentManager();

        //fragCont = findViewById(R.id.fragCont);


        fragmentManager.beginTransaction().add(R.id.fragCont, FragmentHome.newInstance("","")).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentHome.newInstance("","")).commit();

            replaceFragWithBackstack(R.id.fragCont, FragmentHome.newInstance("",""));
        } else if (id == R.id.nav_sell_item) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentSellItem.newInstance("","")).commit();

            replaceFragWithBackstack(R.id.fragCont, FragmentSellItem.newInstance("",""));
        } else if (id == R.id.nav_settings) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentSettings.newInstance("","")).commit();

            replaceFragWithBackstack(R.id.fragCont, FragmentSettings.newInstance("",""));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

            @Override
            public void onFragmentHomeInteraction(Uri uri) {
                Log.e(Utils.TAG, "onFragmentHomeInteraction: " );
            }

            @Override
            public void onFragmentSellInteraction(Uri uri) {

            }

            @Override
            public void onFragmentSettingsInteraction(Uri uri) {

            }


            @Override
            public void onHomeItemClicked(Item item) {
                Log.e(TAG, "onHomeItemClicked: " );

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentViewItem.newInstance("","")).commit();
                replaceFragWithBackstack(R.id.fragCont, FragmentViewItem.newInstance("",""));

            }

            private void replaceFragWithBackstack(int fragCont, Fragment frag) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(fragCont, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
