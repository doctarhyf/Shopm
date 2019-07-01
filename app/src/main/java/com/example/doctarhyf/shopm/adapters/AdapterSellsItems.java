package com.example.doctarhyf.shopm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.objects.SellsItem;

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

        public TextView tvItemNumAndName, tvQty, tvPA, tvPV, tvB;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvItemNumAndName = layout.findViewById(R.id.tvItemNumAndName);
            tvQty = layout.findViewById(R.id.tvQty);
            tvPA = layout.findViewById(R.id.tvPA);
            tvPV = layout.findViewById(R.id.tvPV);
            tvB = layout.findViewById(R.id.tvB);


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

    //private static int spt = 0;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SellsItem item = sellsItems.get(position);

        String numAndName = item.getDataValue(SellsItem.KEY_ITEM_NAME);

        String qty = item.getDataValue(SellsItem.KEY_SELL_QTY);
        String pa = item.getDataValue(SellsItem.KEY_ITEM_PA);
        String pv = item.getDataValue(SellsItem.KEY_ITEM_PV);
        String b = item.getDataValue(SellsItem.KEY_ITEM_B);

        Log.e(TAG, "DIK: -> " + item.toString() );

        holder.tvItemNumAndName.setText(numAndName);
        holder.tvQty.setText(qty);
        holder.tvPA.setText(pa);
        holder.tvPV.setText(pv);
        holder.tvB.setText(b);
        //spt += Integer.parseInt(pt);
        //Log.e(TAG, "onBindViewHolder: PT : " +  spt);

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
