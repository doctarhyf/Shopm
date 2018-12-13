package com.example.doctarhyf.shopm;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.SearchView;
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

import android.widget.ImageView;
import android.widget.Toast;

import com.example.doctarhyf.shopm.adapters.AdapterHomeItems;
import com.example.doctarhyf.shopm.api.ShopmApi;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.barcode.BarcodeCaptureActivity;
import com.example.doctarhyf.shopm.fragments.FragmentAddItem;
import com.example.doctarhyf.shopm.fragments.FragmentHome;
import com.example.doctarhyf.shopm.fragments.FragmentSellItem;
import com.example.doctarhyf.shopm.fragments.FragmentSells;
import com.example.doctarhyf.shopm.fragments.FragmentSettings;
import com.example.doctarhyf.shopm.fragments.FragmentViewItem;
import com.example.doctarhyf.shopm.fragments.FragnentErrorMessage;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityHome extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentHome.OnFragmentHomeInteractionListener,
        FragmentSettings.OnFragmentSettingsInteractionListener,
        FragmentSellItem.OnFragmentSellInteractionListener,
        AdapterHomeItems.Callbacks,
        FragmentViewItem.OnFragmentViewItemInteractionListener,
        FragmentSells.OnFragmentSellsInteractionListener,
        FragnentErrorMessage.OnFragmentErrorMessageInteractionListener,
        FragmentAddItem.OnFragmentAddItemInteractionListener
        {


            private static final String TAG = Utils.TAG;
            private static final int REQUEST_IMAGE_CAPTURE = 100;
            private static final int REQUEST_TAKE_PHOTO = 101;
            private View fragCont;
    private FragmentManager fragmentManager;
            private SearchView searchView;
            //private MenuItem menuItemSearch;
            private View pbCont;
            private ImageView mImageView;
            private String mCurrentPhotoPath;
            private Menu mMenu;
            //private Button btnAddItem;
            //private MenuItem searchView;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //btnAddItem = findViewById(R.id.btnAddItem);
        fragmentManager = getSupportFragmentManager();

        fragCont = findViewById(R.id.fragCont);
        pbCont = findViewById(R.id.pbCont);


        initHome();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                scanBarCode();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        boolean itemSold = getIntent().getBooleanExtra(Utils.ITEM_SOLD, false);

        if(itemSold){
            String itemJson = getIntent().getStringExtra(Utils.ITEM_JSON);
            Item item = Item.FromJSON(itemJson);
            //Toast.makeText(this, "Item : " + item.getItem_name() + ", sold!", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.fab),  "Item : " + item.getItem_name() + ", sold! Remaining in stock : " + item.getItem_stock_count(), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
        }




        /*btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });*/
    }

            @Override
            protected void onResume() {
                super.onResume();

                //Log.e(TAG, "onResume: " );
                //initHome();
            }


    /*private void addItem() {


                String itemName = GetEditTextValue((EditText)findViewById(R.id.etItemName));
                String itemPrice = GetEditTextValue((EditText)findViewById(R.id.etItemPrice));
                String itemInitStock = GetEditTextValue((EditText)findViewById(R.id.etItemInitStock));
                String itemDesc = GetEditTextValue((EditText)findViewById(R.id.etItemDesc));


                Log.e(TAG, "addItem: \nItem Name : " + itemName + "\nItem Price : " + itemPrice +
                "\nStock : " + itemInitStock + "\nItem Desc : " + itemDesc);



            }*/



            private void initHome() {

                showAllMenuItems();
                //setItemsListVisible(false);
                if(ShopmApplication.GI().getApi().IsOnline(this)) {
                    //fragmentManager.beginTransaction().add(R.id.fragCont, FragmentHome.newInstance("", "")).commit();
                    setItemsListVisible(false);
                    //menuItemSearch.setVisible(true);
                    replaceFragWithBackstack(R.id.fragCont, FragmentHome.newInstance("",""), null, null);
                }else{
                    String msg = getResources().getString(R.string.msgNoConnection);
                    fragmentManager.beginTransaction().add(R.id.fragCont, FragnentErrorMessage.newInstance(msg,"")).commit();
                }

                ShopmApi api = ShopmApplication.GI().getApi();
                String itemDelete = api.GSV(Utils.ITEM_DELETED, null);
                String itemAdded = api.GSV(Utils.ITEM_ADDED, null);
                String itemUpdated = api.GSV(Utils.ITEM_UPDATED, null);

                if(itemUpdated != null){
                    Snackbar.make(findViewById(R.id.fab),  "Article mis a jour avec succes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    api.SSV(Utils.ITEM_UPDATED, null);


                }

                if(itemDelete != null){
                    Snackbar.make(findViewById(R.id.fab),  "Article efface avec succes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    api.SSV(Utils.ITEM_DELETED, null);


                }

                if(itemAdded != null){
                    Snackbar.make(findViewById(R.id.fab),  "Article ajoute avec succes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    api.SSV(Utils.ITEM_ADDED, null);


                }
            }

            private void scanBarCode() {
                Log.e(TAG, "scanBarCode: " );

                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, Utils.BARCODE_READER_REQUEST_CODE);
            }


            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = image.getAbsolutePath();
                return image;
            }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

                String barcodeMessage = "No message";

       // mImageView.setBackgroundColor(Color.RED);

                //Log.e(TAG, "onActivityResult: DA RESCODE -> " + resultCode );

        if (requestCode == REQUEST_TAKE_PHOTO ) {

            if(resultCode == RESULT_OK) {
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //mImageView.setImageBitmap(imageBitmap);

                setPic();

            }else{

                Log.e(TAG, "onActivityResult: CAPTURE FAILED" );

            }
        }else if (requestCode == Utils.BARCODE_READER_REQUEST_CODE) {
                    if (resultCode == CommonStatusCodes.SUCCESS) {
                        if (data != null) {





                            //Bundle extra = data.getExtras();

                            Barcode barcode = data.getParcelableExtra("Barcode");//Barcode>(BarcodeCaptureActivity.BarcodeObject)
                            Point p[] = barcode.cornerPoints;

                            //mResultTextView.setText(barcode.displayValue);
                            barcodeMessage = barcode.displayValue;

                            Log.e(TAG, "onActivityResult: da br msg : " + barcodeMessage );
                            if(barcodeMessage.indexOf("ip_") != -1) {
                                String ip = barcodeMessage.replace("ip_", "");
                                connectToServer(ip);
                            }else{
                                showItemFromBarcode(barcodeMessage);
                            }

                        } else {
                            //mResultTextView.setText(R.string.no_barcode_captured);
                            barcodeMessage = getString(R.string.no_barcode_captured);
                            showBarcodeErrorMessage(barcodeMessage);

                        }
                    } else {

                        barcodeMessage = String.format(getString(R.string.barcode_error_format));
                        //Log.e(Utils.TAG, barcodeMessage);
                        Log.e(TAG, "onActivityResult: NO SUCCESS -> " + barcodeMessage );
                        //CommonStatusCodes.getStatusCodeString(resultCode)))
                        showBarcodeErrorMessage(barcodeMessage);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }

            private void connectToServer(String ip) {

                ShopmApi api = ShopmApplication.GI().getApi();

                api.SSV(ShopmApi.SV_SERVER_ADD, ip);
                //initHome();

                setItemsListVisible(true);
                showAllMenuItems();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentHome.newInstance("","")).commitAllowingStateLoss();

            }

            @Override
            protected void onSaveInstanceState(Bundle outState) {
                outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
                super.onSaveInstanceState(outState);
            }

            private void setPic() {
                // Get the dimensions of the View
                int targetW = mImageView.getWidth();
                int targetH = mImageView.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                mImageView.setImageBitmap(bitmap);
            }

            private void showItemFromBarcode(final String barcodeMessage) {
                //Log.e(TAG, "showItemFromBarcode: showing -> " + barcodeMessage );

                //replaceFragWithBackstack(R.id.fragCont, FragmentViewItem.newInstance(barcodeMessage,""));

                ShopmApplication.GI().getApi().loadItemByUniqueID(new ShopmApi.CallbackLoadItem() {
                    @Override
                    public void onItemLoaded(String itemJson) {
                        Intent intent = new Intent(ActivityHome.this, ActivitySellItem.class);
                        intent.putExtra(Utils.ITEM_JSON, itemJson);
                        startActivity(intent);

                        Log.e(TAG, "onItemLoaded: " );
                        setItemsListVisible(true);

                    }

                    @Override
                    public void onItemNotFound() {

                        Log.e(TAG, "onItemNotFound: " );
                        setItemsListVisible(false);
                        Toast.makeText(ActivityHome.this,"This item : " + barcodeMessage + " doesn't exist", Toast.LENGTH_SHORT).show();

                    }
                }, barcodeMessage);


            }

            private void setItemsListVisible(boolean show) {
                if(show){

                    fragCont.setVisibility(View.VISIBLE);
                    pbCont.setVisibility(View.GONE);

                }else{
                    fragCont.setVisibility(View.GONE);
                    pbCont.setVisibility(View.VISIBLE);
                }
            }

            private void showBarcodeErrorMessage(String barcodeMessage) {
                Toast.makeText(this, barcodeMessage, Toast.LENGTH_SHORT).show();
            }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        showAllMenuItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);


        mMenu = menu;
        // Associate searchable configuration with the SearchView
        //menuItemSearch = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //mAdapter.getFilter().filter(query);
                return false;
            }
        });
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

            replaceFragWithBackstack(R.id.fragCont, FragmentSettings.newInstance("",""),
                    new boolean[]{false,false},
                    new int[]{R.id.action_settings, R.id.action_search});
            return true;
        }

        if(id == R.id.action_add_item){
            replaceFragWithBackstack(R.id.fragCont, FragmentAddItem.newInstance(null),
                    new boolean[]{false, false},
                    new int[]{R.id.action_search, R.id.action_add_item});
            return true;
        }


        if(id == R.id.action_connect_via_qr){
            //Log.e(TAG, "onOptionsItemSelected: connect via qr" );
            scanBarCode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //searchView.setVisible(false);
        int id = item.getItemId();

        //menuItemSearch.setVisible(false);
        if (id == R.id.nav_stock) {
            // Handle the camera action
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentHome.newInstance("","")).commit();

            //searchView.setVisible(true);
            setItemsListVisible(false);
            //menuItemSearch.setVisible(true);
            replaceFragWithBackstack(R.id.fragCont, FragmentHome.newInstance("",""), null, null);
        } else if (id == R.id.nav_sells) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentSellItem.newInstance("","")).commit();
            //scanBarCode();
            replaceFragWithBackstack(R.id.fragCont, FragmentSells.newInstance("",""),
                    new boolean[]{false},
                    new int[]{R.id.action_search});
        } else if (id == R.id.nav_settings) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragCont, FragmentSettings.newInstance("","")).commit();

            replaceFragWithBackstack(R.id.fragCont, FragmentSettings.newInstance("",""),
                    new boolean[]{false, false},
                    new int[]{R.id.action_settings, R.id.action_search});
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
            public void onFragmentHomeItemsLoadError(String errorMessage) {
                String currentIP = ShopmApplication.GI().getApi().GSV(ShopmApi.SV_SERVER_ADD, ShopmApi.SV_DEF_SERVER_ADD);
                String msg = String.format(getResources().getString(R.string.errorLoadingItems), errorMessage, currentIP);
                replaceFragWithBackstack(R.id.fragCont, FragnentErrorMessage.newInstance(msg,""), null, null);

                setItemsListVisible(true);
            }

            @Override
            public void onFragmentHomeItemsLoadSuccess() {
                setItemsListVisible(true);
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


                boolean[] show = {false};

                int[] ids = {R.id.action_search};
                replaceFragWithBackstack(R.id.fragCont, FragmentViewItem.newInstance(item.toJSON()), show, ids);

            }

    private void replaceFragWithBackstack(int fragCont, Fragment frag, boolean showMenusItems[], int menuIds[]) {

                showAllMenuItems();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.setCustomAnimations(R.animator.anim_in, R.animator.anim_out);
                fragmentTransaction.replace(fragCont, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                if(mMenu != null && showMenusItems != null && menuIds != null) {
                    //mMenu.findItem(R.id.action_search).setVisible(false);
                    for(int i = 0 ; i < showMenusItems.length; i++ ){
                        MenuItem menuItem = mMenu.findItem(menuIds[i]);
                        menuItem.setVisible(showMenusItems[i]);
                    }
                }




            }

            private void showAllMenuItems() {


                    if(mMenu != null) {
                        for (int i = 0; i < mMenu.size(); i++) {
                            MenuItem menuItem = mMenu.getItem(i);
                            menuItem.setVisible(true);
                        }
                    }

            }

            @Override
     public void onFragmentSellsInteraction(Uri uri) {

            }


            @Override
            public void addItemToStock(Bundle itemData) {
                //Log.e(TAG, "addItemToStock: DA NITEM -> " + itemData.toString() );
                ShopmApi api = ShopmApplication.getInstance().getApi();
                api.addItemToStock(new ShopmApi.CallbackStock() {
                    @Override
                    public void onItemAddToStockSuccess(String itemData) {

                        ShopmApi api = ShopmApplication.GI().getApi();
                        api.SSV(Utils.ITEM_ADDED, itemData);
                        initHome();
                    }

                    @Override
                    public void onItemAddToStockError(String errorMessage) {
                        Toast.makeText(ActivityHome.this, "Error adding item.\nError message : " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, itemData);

            }

            @Override
            public void takeItemPic(ImageView ivItemPic) {
                mImageView = ivItemPic;

                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }*/

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e(TAG, "createImageFile : File Create Exception -> " + ex.getMessage() );
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.doctarhyf.shopm",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }

            @Override
            public void onItemAddNoEmptyFieldsAllowed() {
                Toast.makeText(ActivityHome.this, "Erreur! Veuillez remplir tous les champs SVP.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateItem(final Item item) {
                //Log.e(TAG, "updateItem: id -> " + item.getItem_id() );

                String title = getString(R.string.strSureUpdateTitle) + " " + item.getItem_name() + " ?";
                String message = getResources().getString(R.string.strSureUpdateMessage);


                AlertDialog alertDialog = Utils.GetAlertDialogWithTitleAndMessage(this, new Utils.ListernerAlertDialogWithTitleMessage() {
                    @Override
                    public void onPositiveButton() {
                        ShopmApi api = ShopmApplication.GI().getApi();

                        api.updateItem(new ShopmApi.CallbackAPIActionConfirmation() {
                            @Override
                            public void onActionSuccess(String actionName, String data) {

                                ShopmApi api = ShopmApplication.GI().getApi();
                                api.SSV(Utils.ITEM_UPDATED, data);
                                initHome();
                            }

                            @Override
                            public void onActionFailure(String actionName, String data) {
                                Toast.makeText(ActivityHome.this, "Erreur -> " + data, Toast.LENGTH_SHORT).show();
                            }
                        }, item);
                        //Log.e(TAG, "onPositiveButton: deleting -> id : " + item.getItem_id() );
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                },title, message, true);
            }

            @Override
            public void refreshStockList() {
                initHome();
            }

            @Override
            public void deleteItem(final Item item) {
                //Log.e(TAG, "deleteItem: id -> " + item.getItem_id() );
                String title = getString(R.string.strSureDeleteTitle) + " " + item.getItem_name() + " ?";
                String message = getResources().getString(R.string.strSureDeleteMessage);


                AlertDialog alertDialog = Utils.GetAlertDialogWithTitleAndMessage(this, new Utils.ListernerAlertDialogWithTitleMessage() {
                    @Override
                    public void onPositiveButton() {
                        ShopmApi api = ShopmApplication.GI().getApi();

                        api.deleteItem(new ShopmApi.CallbackAPIActionConfirmation() {
                            @Override
                            public void onActionSuccess(String actionName, String data) {

                                ShopmApi api = ShopmApplication.GI().getApi();
                                api.SSV(Utils.ITEM_DELETED, data);
                                initHome();
                            }

                            @Override
                            public void onActionFailure(String actionName, String data) {
                                Toast.makeText(ActivityHome.this, "Erreur -> " + data, Toast.LENGTH_SHORT).show();
                            }
                        }, item.getItem_id());
                        //Log.e(TAG, "onPositiveButton: deleting -> id : " + item.getItem_id() );
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                },title, message, true);


            }

            @Override
            public void editItem(Item item) {
                Log.e(TAG, "editItem: id -> " + item.getItem_id() );
                replaceFragWithBackstack(R.id.fragCont, FragmentAddItem.newInstance(item),
                        new boolean[]{false},
                        new int[]{R.id.action_search});
            }
        }
