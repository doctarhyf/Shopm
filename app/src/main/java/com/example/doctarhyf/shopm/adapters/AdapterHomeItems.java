package com.example.doctarhyf.shopm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private int row_index = -1;
    private List<View> itemViewList = new ArrayList<>();

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItemName, tvItemPrice, tvItemStockCount;
        public ImageView ivItemPic;
        public View layout;
        public Button btnSell;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvItemName = layout.findViewById(R.id.tvItemName);
            tvItemPrice = layout.findViewById(R.id.tvItemPrice);
            ivItemPic = layout.findViewById(R.id.ivItemPic);
            tvItemStockCount = layout.findViewById(R.id.tvItemStockCount);
            btnSell = layout.findViewById(R.id.btnVendre);


        }
    }

    private List<Item> items;
    private List<Item> itemsFiltered;
    private Callbacks callbacks;
    //private SOS_API sosApi;



    public AdapterHomeItems(Context context, List<Item> items, Callbacks callbacks){

        this.context = context;
        this.items = items;
        this.itemsFiltered = items;
        this.callbacks = callbacks;
        //this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_adapter_item_home, parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        itemViewList.add(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(View tempItemView : itemViewList){
                    if(itemViewList.get(viewHolder.getAdapterPosition()) == tempItemView){
                        tempItemView.setBackgroundResource(R.color.colorSelected);
                    }else{
                        tempItemView.setBackgroundResource(R.color.colorDefault);
                    }
                }
            }
        });



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item item = itemsFiltered.get(position);



        holder.tvItemName.setText(item.getItem_name());
        holder.tvItemPrice.setText(item.getItem_price() + " FC");
        holder.tvItemStockCount.setText("En stock : " + item.getItem_stock_count());


        /*if(Integer.parseInt(item.getItem_stock_count()) == 0){

            holder.tvItemStockCount.setTextColor(Color.WHITE);
            holder.tvItemStockCount.setBackgroundColor(Color.RED);

        }*/


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "onClick: " + item.getItemCategory() );
                callbacks.onHomeItemClicked(item);

            }
        });

        holder.btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: selling -> " + holder.tvItemName.getText().toString() );
                callbacks.onBtnSellClicked(item);
            }
        });

        String url = ShopmApplication.GI().getApi().GetServerAddress() + Utils.ROOT_FOLDER + "/" + Utils.IMG_FOLDER_NAME +
                "/" + item.getItem_unique_name() + ".jpg";

        //Log.e(TAG, "onBindViewHolder: url -> " + url );

        Uri uri = Uri.parse(url);

        Glide.with(holder.ivItemPic.getContext())
                .load(uri)
                .asBitmap()
                .error(R.drawable.no_img_found)
                //.placeholder(R.drawable.progress_animation)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
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
                    //items.clear();
                    List<Item> filteredList = new ArrayList<>();
                    for (Item item : items) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (item.getItem_name().toLowerCase().contains(charString.toLowerCase())) {
                           filteredList.add(item);

                            Log.e(TAG, "performFiltering: item found -> " + item.getItem_name() );
                        }


                    }

                    itemsFiltered = filteredList;

                    Log.e(TAG, "performFiltering: filtered count -> " + itemsFiltered.size() );
                    Log.e(TAG, "performFiltering: filtered total count -> " + items.size() );
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered = (ArrayList<Item>) filterResults.values;
                //items = itemsFiltered;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    public  interface Callbacks {
        void onHomeItemClicked(Item item);
        void onBtnSellClicked(Item item);



    }


}
