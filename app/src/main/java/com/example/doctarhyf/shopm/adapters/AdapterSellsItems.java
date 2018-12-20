package com.example.doctarhyf.shopm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.objects.SellsItem;
import com.example.doctarhyf.shopm.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Franvanna on 12/22/2017.
 */

public class AdapterSellsItems extends RecyclerView.Adapter<AdapterSellsItems.ViewHolder> implements
        Filterable{


    private static final String TAG = "DASH";
    private Context context;
    private int row_index = -1;
    private List<View> itemViewList = new ArrayList<>();

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItemNumAndName, tvQty, tvPU, tvPT;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvItemNumAndName = layout.findViewById(R.id.tvItemNumAndName);
            tvQty = layout.findViewById(R.id.tvQty);
            tvPU = layout.findViewById(R.id.tvPU);
            tvPT = layout.findViewById(R.id.tvPT);


        }
    }

    private List<SellsItem> sellsItems;
    private List<SellsItem> sellsItemsFiltered;
    private Callbacks callbacks;
    //private SOS_API sosApi;



    public AdapterSellsItems(Context context, List<SellsItem> sellsItems, Callbacks callbacks){

        this.context = context;
        this.sellsItems = sellsItems;
        this.callbacks = callbacks;
        //this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_adapter_item_sells, parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        /*
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
        });*/

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SellsItem item = sellsItems.get(position);

        String numAndName = item.getDataValue(SellsItem.KEY_ITEM_NAME);

        String qty = item.getDataValue(SellsItem.KEY_SELL_QTY);
        String pu = item.getDataValue(SellsItem.KEY_ITEM_CUR_PRICE);
        String pt = item.getDataValue(SellsItem.KEY_ITEM_TOTAL_SELLS);

        holder.tvItemNumAndName.setText(numAndName);
        holder.tvQty.setText(qty);
        holder.tvPU.setText(pu);
        holder.tvPT.setText(pt);



        /*
        if(Integer.parseInt(item.getItem_stock_count()) == 0){
            holder.tvItemStockCount.setTextColor(Color.WHITE);
            holder.tvItemStockCount.setBackgroundColor(Color.RED);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "onClick: " + item.getItemCategory() );
                callbacks.onHomeItemClicked(item);

            }
        });



        */

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    sellsItemsFiltered = sellsItems;
                } else {
                    List<SellsItem> filteredList = new ArrayList<>();
                    for (SellsItem item : sellsItems) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        /*if (item.getItem_name().toLowerCase().contains(charString.toLowerCase()) || item.getItem_desc().contains(charSequence)) {
                            filteredList.add(item);
                        }*/
                    }

                    sellsItemsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sellsItemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sellsItemsFiltered = (ArrayList<SellsItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public int getItemCount() {
        return sellsItems.size();
    }

    public  interface Callbacks {
        void onSellItemClicked(SellsItem sellsItem);



    }


}
