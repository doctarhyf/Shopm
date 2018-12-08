package com.example.doctarhyf.shopm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentViewItemInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentViewItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentViewItem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEM_JSON = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mItemJson;
    private String mParam2;

    private OnFragmentViewItemInteractionListener mListener;

    public FragmentViewItem() {
        // Required empty public constructor
    }


    public static FragmentViewItem newInstance(String itemJson) {
        FragmentViewItem fragment = new FragmentViewItem();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_JSON, itemJson);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemJson = getArguments().getString(ARG_ITEM_JSON);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_item, container, false);

        if(!mItemJson.equals("")){

            Item item = Item.FromJSON(mItemJson);

            TextView tvItemName = rootView.findViewById(R.id.tvItemName);
            TextView tvItemPrice = rootView.findViewById(R.id.tvItemPrice);
            TextView tvStockCount = rootView.findViewById(R.id.tvStockCount);
            TextView tvItemDesc = rootView.findViewById(R.id.tvItemDesc);

            tvItemName.setText(item.getItem_name());
            tvItemPrice.setText(item.getItem_price() + " FC");
            tvStockCount.setText(Utils.STR_STOCK_PREFIX  + item.getItem_stock_count());
            tvItemDesc.setText(item.getItem_desc());



        }else{
            Toast.makeText(getActivity(), "Item data missing!", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentViewIteInteraction(uri);
            Log.e(Utils.TAG, "onButtonPressed: " );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentViewItemInteractionListener) {
            mListener = (OnFragmentViewItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentViewItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentViewItemInteractionListener {
        // TODO: Update argument type and name
       // void onFragmentViewIteInteraction(Uri uri);
    }
}
