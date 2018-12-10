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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentAddItemInteractionListener mListener;

    public FragmentAddItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddItem newInstance(String param1, String param2) {
        FragmentAddItem fragment = new FragmentAddItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_item, container, false);

        Button btnAddItem = rootView.findViewById(R.id.btnAddItem);

        String itemName = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemName));
        String itemPrice = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemPrice));
        String itemInitStock = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemInitStock));
        String itemDesc = Utils.GetEditTextValue(getContext(), (EditText)rootView.findViewById(R.id.etItemDesc));

        final Bundle itemData = new Bundle();
        itemData.putString(Item.KEY_ITEM_NAME, itemName);
        itemData.putString(Item.KEY_ITEM_PRICE, itemPrice);
        itemData.putString(Item.KEY_ITEM_INIT_STOCK, itemInitStock);
        itemData.putString(Item.KEY_ITEM_DESC, itemDesc);

        Log.e(Utils.TAG, "addItem: \nItem Name : " + itemName + "\nItem Price : " + itemPrice +
                "\nStock : " + itemInitStock + "\nItem Desc : " + itemDesc);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddItemListener(itemData);
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
        void onAddItemListener(Bundle itemData);
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
