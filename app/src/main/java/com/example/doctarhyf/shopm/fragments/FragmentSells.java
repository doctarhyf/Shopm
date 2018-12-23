package com.example.doctarhyf.shopm.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.doctarhyf.shopm.R;
import com.example.doctarhyf.shopm.adapters.AdapterHomeItems;
import com.example.doctarhyf.shopm.adapters.AdapterSellsItems;
import com.example.doctarhyf.shopm.api.ShopmApi;
import com.example.doctarhyf.shopm.app.ShopmApplication;
import com.example.doctarhyf.shopm.objects.Item;
import com.example.doctarhyf.shopm.objects.SellsItem;
import com.example.doctarhyf.shopm.utils.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSellsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSells#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSells extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = Utils.TAG;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<SellsItem> sellsItemList = new ArrayList<>();
    private AdapterSellsItems adapterSellsItems;


    private OnFragmentSellsInteractionListener mListener;
    private TextView tvTotCash = null;
    private TextView tvDim = null;
    private String mSellDataType = "";
    private String mPeriode = "";

    public FragmentSells() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSells.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSells newInstance(String param1, String param2) {
        FragmentSells fragment = new FragmentSells();
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
        getActivity().setTitle("Details des Ventes");
        final View rootView = inflater.inflate(R.layout.fragment_sells, container, false);



        Button btnToggleSellsHeader = rootView.findViewById(R.id.btnToggleSellsHeader);
        final View viewSellsHeader = rootView.findViewById(R.id.viewSellsHeader);
        tvTotCash = rootView.findViewById(R.id.tvTotCash);
        tvDim = rootView.findViewById(R.id.tvDim);
        //final View llSpinnersSelMonth = rootView.findViewById(R.id.llSpinnersSelMonth);
        //final EditText etDateTime = rootView.findViewById(R.id.etDateTime);
        final View viewDime = rootView.findViewById(R.id.viewDime);
        final Spinner spSellsType = rootView.findViewById(R.id.spSellsType);
        View dateView = getLayoutInflater().inflate(R.layout.layout_date_picker, null);
        final TextView tvDatePickerTitle = dateView.findViewById(R.id.tvDatePickerTitle);
        final View viewSpinnersSelMonth = dateView.findViewById(R.id.viewSpinnersSelMonth);
        final DatePicker datePicker = (DatePicker) dateView.findViewById(R.id.dp);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        final TextView tvSellsDate = rootView.findViewById(R.id.tvSellsDate);



        final Spinner spMonths = dateView.findViewById(R.id.spMonths);
        final Spinner spYears = dateView.findViewById(R.id.spYears);
        final String[] yearMonthSellArray = new String[2];
        //final boolean[] monthlySellsSelected = new boolean[]{false};

        spMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearMonthSellArray[1] = (i+1) + "";
                updateMonthSelection(tvSellsDate, yearMonthSellArray);
                //calculateDime
                //viewDime.setVisibility(View.VISIBLE);
                //monthlySellsSelected[0] = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearMonthSellArray[0] = adapterView.getSelectedItem().toString();
                updateMonthSelection(tvSellsDate, yearMonthSellArray);
                //calculateDime
                //viewDime.setVisibility(View.VISIBLE);
                //monthlySellsSelected[0] = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(dateView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //String date =
                        //viewDime.setVisibility(View.GONE);
                        String sellsDataType = spSellsType.getSelectedItemPosition() == 0 ? Utils.SELL_DATA_TYPE_DAILY : Utils.SELL_DATA_TYPE_MONTHLY;
                        String periode = tvSellsDate.getText().toString();

                        getItemsData(sellsDataType, periode);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.e(TAG, "onDismiss: date picker" );
            }
        });



        spSellsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: " + i );
                if(i == 0) {
                    tvDatePickerTitle.setText("CHOISIR UN JOUR");
                    datePicker.setVisibility(View.VISIBLE);
                    viewSpinnersSelMonth.setVisibility(View.GONE);
                    viewDime.setVisibility(View.GONE);
                }else{
                    tvDatePickerTitle.setText("CHOISIR UN MOIS");
                    datePicker.setVisibility(View.GONE);
                    viewSpinnersSelMonth.setVisibility(View.VISIBLE);
                    viewDime.setVisibility(View.VISIBLE);
                }

                alertDialog.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Log.e(TAG, "onNothingSelected: " );

            }
        });


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month+1) + "/" + dayOfMonth;
                tvSellsDate.setText(date);

            }
        });

        DataPoint[] points = new DataPoint[4];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);

        setupHomeItems(rootView);
        //setupHomeClothes(rootView);
        //setupHomeTrendz(rootView);

        String today = calendar.get(Calendar.YEAR ) + "/" + calendar.get(Calendar.MONTH ) + "/" + calendar.get(Calendar.DAY_OF_MONTH );
        tvSellsDate.setText(today);
        getItemsData(Utils.SELL_DATA_TYPE_DAILY, today );

        tvSellsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alertDialog != null) alertDialog.show();
            }
        });

        btnToggleSellsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewSellsHeader.getVisibility() == View.VISIBLE){
                    viewSellsHeader.setVisibility(View.GONE);
                }else{
                    viewSellsHeader.setVisibility(View.VISIBLE);
                }

            }
        });

        Button btnGenPDFRepport = rootView.findViewById(R.id.btnGenPDFRepport);

        btnGenPDFRepport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(rootView, "Will be emplemented", Snackbar.LENGTH_SHORT).show();

                ShopmApplication.getInstance().getApi().generateReportPDF(new ShopmApi.CallbackAPIActionConfirmation() {
                    @Override
                    public void onActionSuccess(String actionName, String data) {
                        Log.e(TAG, "onActionSuccess: actionName ; " + actionName + ", Data : " + data );
                    }

                    @Override
                    public void onActionFailure(String actionName, String data) {
                        Log.e(TAG, "onActionFailure: actionName : " + actionName + ", data : " + data );
                    }
                }, mSellDataType, mPeriode);

            }
        });

        return rootView;
    }

    private void getItemsData(String sellDataType, String periode) {

        mSellDataType = sellDataType;
        mPeriode = periode;

        ShopmApplication.getInstance().getApi().loadItemSells(new ShopmApi.CallbacksItemSells() {
            @Override
            public void onItemsLoaded(List<SellsItem> newSellsItems, String tot_qty, String tot_price) {

                sellsItemList.clear();
                sellsItemList.addAll(newSellsItems);

                adapterSellsItems.notifyDataSetChanged();
                mListener.onFragmentSellsItemsLoadSuccess();


                if(tvTotCash != null) tvTotCash.setText(tot_price + ".00");
                if(tvDim != null) tvDim.setText((Integer.parseInt(tot_price) * .1) + "0");


            }

            @Override
            public void onItemsLoadeError(String errorMessage) {
                mListener.onFragmentSellsItemsLoadError(errorMessage);
                sellsItemList.clear();
                adapterSellsItems.notifyDataSetChanged();
            }

            @Override
            public void onEmptyList() {
                mListener.onFragmentSellsEmptyList();
                sellsItemList.clear();
                adapterSellsItems.notifyDataSetChanged();
            }
        }, sellDataType, periode);



    }


    private void setupHomeItems(View rootView) {
        RecyclerView rv = rootView.findViewById(R.id.rvSells);
        adapterSellsItems = new AdapterSellsItems(getContext(), sellsItemList, (AdapterSellsItems.Callbacks) getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapterSellsItems);


    }


    private void updateMonthSelection(TextView tv, String[] yearMonthSellArray) {
        Log.e(TAG, "updateMonthSelection: -> " + yearMonthSellArray );

        tv.setText(yearMonthSellArray[0] + "/" + yearMonthSellArray[1]);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentSellsInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSellsInteractionListener) {
            mListener = (OnFragmentSellsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSellsInteractionListener");
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
    public interface OnFragmentSellsInteractionListener {
        // TODO: Update argument type and name
        void onFragmentSellsInteraction(Uri uri);

        void onFragmentSellsItemsLoadSuccess();

        void onFragmentSellsItemsLoadError(String errorMessage);

        void onFragmentSellsEmptyList();
    }
}
