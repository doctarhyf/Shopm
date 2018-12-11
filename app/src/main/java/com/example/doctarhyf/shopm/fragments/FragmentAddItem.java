package com.example.doctarhyf.shopm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentAddItemInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAddItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddItem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEM_TO_EDIT = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mItemToEdit;
    private String mParam2;

    private OnFragmentAddItemInteractionListener mListener;
    private boolean editing = false;

    public FragmentAddItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemToEdit Parameter 2.
     * @return A new instance of fragment FragmentAddItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddItem newInstance(Item itemToEdit) {
        FragmentAddItem fragment = new FragmentAddItem();
        Bundle args = new Bundle();

        if(itemToEdit != null) {
            args.putString(ARG_ITEM_TO_EDIT, itemToEdit.toJSON());
           // editing = true;
        }
        //args.putString(ARG_PARAM2, itemToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemToEdit = getArguments().getString(ARG_ITEM_TO_EDIT);
            if(mItemToEdit != null){
                editing = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_add_item, container, false);

        Button btnAddItem = rootView.findViewById(R.id.btnAddItem);

       // if(editing)

        //Item item = null;

        if(editing) {
            btnAddItem.setText(getResources().getString(R.string.btnUpdateItem));
            Item item = Item.FromJSON(mItemToEdit);
            Utils.SetEditTextValue(getContext(), (EditText) rootView.findViewById(R.id.etItemName), item.getItem_name());
            Utils.SetEditTextValue(getContext(), (EditText) rootView.findViewById(R.id.etItemPrice), item.getItem_price());
            Utils.SetEditTextValue(getContext(), (EditText) rootView.findViewById(R.id.etItemInitStock), item.getItem_stock_count());
            Utils.SetEditTextValue(getContext(), (EditText) rootView.findViewById(R.id.etItemDesc), item.getItem_desc());
        }


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //adding new
                final String itemName = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemName));
                final String itemPrice = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemPrice));
                final String itemInitStock = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemInitStock));
                final String itemDesc = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemDesc));

                final Bundle itemData = new Bundle();
                itemData.putString(Item.KEY_ITEM_NAME, itemName);
                itemData.putString(Item.KEY_ITEM_PRICE, itemPrice);
                itemData.putString(Item.KEY_ITEM_INIT_STOCK, itemInitStock);
                itemData.putString(Item.KEY_ITEM_DESC, itemDesc);

                //Log.e(Utils.TAG, "onClick: data to add -> " + itemData.toString() );

                Item curItem = null;
                if(editing) {
                    curItem = Item.FromJSON(mItemToEdit);
                    curItem.setItem_name(itemName);
                    curItem.setItem_price(itemPrice);
                    curItem.setItem_stock_count(itemInitStock);
                    curItem.setItem_desc(itemDesc);
                }

                if(itemName.equals("") || itemPrice.equals("") || itemInitStock.equals("") || itemDesc.equals("")){
                    mListener.onItemAddNoEmptyFieldsAllowed();
                }else {

                    if(editing){
                        mListener.updateItem(curItem);
                    }else {
                        mListener.addItemToStock(itemData);
                    }
                }
            }
        });

        View ivItemPicCont = rootView.findViewById(R.id.ivItemPicCont);
        final ImageView ivItemPic = rootView.findViewById(R.id.ivItemPic);

        ivItemPicCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(Utils.TAG, "onClick: " );
                mListener.takeItemPic(ivItemPic);
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAddItemInteractionListener) {
            mListener = (OnFragmentAddItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentAddItemInteractionListener");
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
    public interface OnFragmentAddItemInteractionListener {
        void addItemToStock(Bundle itemData);

        void takeItemPic(ImageView ivItemPic);

        void onItemAddNoEmptyFieldsAllowed();

        void updateItem(Item item);
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
