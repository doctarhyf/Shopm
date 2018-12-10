package com.example.doctarhyf.shopm.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Franvanna on 12/22/2017.
 */

public class AdapterHomeItems extends RecyclerView.Adapter<AdapterHomeItems.ViewHolder> implements
        Filterable{


    private static final String TAG = "DASH";
    private Context context;

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItemName, tvItemPrice, tvItemStockCount;
        public ImageView ivItemPic;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvItemName = layout.findViewById(R.id.tvItemName);
            tvItemPrice = layout.findViewById(R.id.tvItemPrice);
            ivItemPic = layout.findViewById(R.id.ivItemPic);
            tvItemStockCount = layout.findViewById(R.id.tvItemStockCount);


        }
    }

    private List<Item> items;
    private List<Item> itemsFiltered;
    private Callbacks callbacks;
    //private SOS_API sosApi;



    public AdapterHomeItems(Context context, List<Item> items, Callbacks callbacks){

        this.context = context;
        this.items = items;
        this.callbacks = callbacks;
        //this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_adapter_item_home, parent,false);
        //ViewHolder viewHolder = new ViewHolder(view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = items.get(position);

        holder.tvItemName.setText(item.getItem_name());
        holder.tvItemPrice.setText("Prix : " + item.getItem_price() + " FC");
        holder.tvItemStockCount.setText("En stock : " + item.getItem_stock_count());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "onClick: " + item.getItemCategory() );
                callbacks.onHomeItemClicked(item);
            }
        });

        String url = ShopmApplication.GI().getApi().GetServerAddress() + Utils.ROOT_FOLDER + "/" + Utils.IMG_FOLDER_NAME +
                "/" + item.getItem_unique_name() + ".jpg";

        //Log.e(TAG, "onBindViewHolder: url -> " + url );

        Uri uri = Uri.parse(url);

        Glide.with(holder.ivItemPic.getContext())
                .load(uri)
                .asBitmap()
                //.error(R.drawable.ic_error)
                //.placeholder(R.drawable.progress_animation)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(holder.ivItemPic);

        //holder.tvCatTitle.setText(homeCategoryItem.getTitle());


        /*
        String url = homeCategoryItem.getImageUrl();

        //final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdImg();
        Uri uri = Uri.parse(url);


        String picName = url.split("/")[url.split("/").length-1];
        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_ITEMS_CATEGORIES, picName);
        if(BitmapCacheManager.FileExists(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + uri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + uri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }



        //callbacks.onItemClicked(homeCategoryItem);

        holder.ivCatBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == 0){
                    holder.ivCatBg.setAlpha(0.5f);

                }

                if(event.getAction() == 1 || event.getAction() == 3) {
                    holder.ivCatBg.setAlpha(1f);


                }

                if(event.getAction() == 1){
                    callbacks.onItemClicked(homeCategoryItem);
                }




                return true;





            }
        });*/

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemsFiltered = items;
                } else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item item : items) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (item.getItem_name().toLowerCase().contains(charString.toLowerCase()) || item.getItem_desc().contains(charSequence)) {
                            filteredList.add(item);
                        }
                    }

                    itemsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public  interface Callbacks {
        void onHomeItemClicked(Item item);



    }


}
