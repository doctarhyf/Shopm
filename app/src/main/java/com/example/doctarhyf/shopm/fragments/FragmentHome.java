package com.example.doctarhyf.shopm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctarhyf.shopm.adapters.AdapterHomeItems;
import com.example.doctarhyf.shopm.api.ShopmApi;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentHomeInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Item> items = new ArrayList<>();
    private AdapterHomeItems adapterHomeItems;
    private CallbacksFragmentHome callbacksFragmentHome;

    private OnFragmentHomeInteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("Shopm");

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

        getItemsData();



        return rootView;
    }

    private void getItemsData() {

        ShopmApplication.getInstance().getApi().loadAllItems(new ShopmApi.CallbacksItems() {
            @Override
            public void onItemsLoaded(List<Item> newItems) {

                items.clear();
                items.addAll(newItems);

                adapterHomeItems.notifyDataSetChanged();
                mListener.onFragmentHomeItemsLoadSuccess();

            }

            @Override
            public void onItemsLoadeError(String errorMessage) {
                mListener.onFragmentHomeItemsLoadError(errorMessage);
            }
        });

        /*for (int i = 0; i < 10; i ++){
            items.add(new Item("" + i, "Item " + i, (i * 1324) + " $", "Tha desc goes here " + i, "UN",
            2000 + "", "0000-00-00 00:00:00", "0000-00-00 00:00:00") );
            //itemsListClothing.add(new Item(i+"", "Item " + i, "$" + (i*100) + ".00", "http://", Item.ITEM_CATEGORY_CLOTH) );
            //itemsListTrends.add(new Item(i+"", "Item " + i, "$" + (i*100) + ".00", "http://", Item.ITEM_CATEGORY_TREND) );

        }*/



    }

    private void setupHomeItems(View rootView) {
        RecyclerView rv = rootView.findViewById(R.id.rvHome);
        adapterHomeItems = new AdapterHomeItems(getActivity(), items, (AdapterHomeItems.Callbacks) getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapterHomeItems);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentHomeInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentHomeInteractionListener) {
            mListener = (OnFragmentHomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentHomeInteractionListener");
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
    public interface OnFragmentHomeInteractionListener {
        // TODO: Update argument type and name
        void onFragmentHomeInteraction(Uri uri);

        void onFragmentHomeItemsLoadError(String errorMessage);

        void onFragmentHomeItemsLoadSuccess();
    }

    public interface CallbacksFragmentHome{

        void onHomeItemClicked(Item item);
    }
}
