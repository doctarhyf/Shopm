package com.example.doctarhyf.shopm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.adapters.AdapterStockHistory;
import com.example.doctarhyf.shopm.api.ShopmApi;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.objects.StockHistory;
import com.example.doctarhyf.shopm.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentStockHistoryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentStockHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStockHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEM_JSON = null;
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mItemJSON = null;
    private Item item = null;
    //private String mParam2;

    private List<StockHistory> stockHistoryList = new ArrayList<>();
    private AdapterStockHistory adapterStockHistory;
    private CallbacksFragmentStockHistory callbacksFragmentStockHistory;

    private OnFragmentStockHistoryInteractionListener mListener;

    public FragmentStockHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStockHistory newInstance(String param1) {
        FragmentStockHistory fragment = new FragmentStockHistory();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_JSON, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemJSON = getArguments().getString(ARG_ITEM_JSON);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stock_history, container, false);

        getActivity().setTitle("Historique de stock");

        //TextView tvSearch = rootView.findViewById(R.id.tvSearch);

        /*tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.startSearching();
            }
        });*/

        setupHomeItems(rootView);
        //setupHomeClothes(rootView);
        //setupHomeTrendz(rootView);

        loadDataFromServer();



        return rootView;
    }

    private void loadDataFromServer() {

        Log.e(Utils.TAG, "dbg : " + mItemJSON);

        ShopmApplication.getInstance().getApi().loadItemStockHistory(new ShopmApi.CallbacksStockHistoryItems() {
            @Override
            public void onItemsLoaded(List<StockHistory> newItems) {

                stockHistoryList.clear();
                stockHistoryList.addAll(newItems);

                adapterStockHistory.notifyDataSetChanged();
                mListener.onFragmentHomeItemsLoadSuccess();

            }

            @Override
            public void onItemsLoadeError(String errorMessage) {
                mListener.onFragmentHomeItemsLoadError(errorMessage);
            }
        }, Item.FromJSON(mItemJSON).getItem_id());

        /*
        stockHistoryList.clear();
        for (int i = 0; i < 10; i ++){

            Bundle data = new Bundle();

            data.putString(StockHistory.KEY_DATE, "date");
            data.putString(StockHistory.KEY_OLD_STOCK,"old");
            data.putString(StockHistory.KEY_NEW_STOCK,"new");


            stockHistoryList.add(new StockHistory(data));

        }

        adapterStockHistory.notifyDataSetChanged();*/




    }

    private void setupHomeItems(View rootView) {
        RecyclerView rv = rootView.findViewById(R.id.rvStockHistory);
        adapterStockHistory = new AdapterStockHistory(getActivity(), stockHistoryList, (AdapterStockHistory.Callbacks) getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapterStockHistory);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentHomeInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentStockHistoryInteractionListener) {
            mListener = (OnFragmentStockHistoryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentStockHistoryInteractionListener");
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
    public interface OnFragmentStockHistoryInteractionListener {
        // TODO: Update argument type and name
       // void onFragmentHomeInteraction(Uri uri);

        /*void onFragmentStockHistoryItemsLoadError(String errorMessage);

        void onFragmentStockHistoryItemsLoadSuccess();*/

        void onFragmentHomeItemsLoadSuccess();

        void onFragmentHomeItemsLoadError(String errorMessage);
    }

    public interface CallbacksFragmentStockHistory {

        void onHomeItemClicked(Item item);
    }
}
