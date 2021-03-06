package com.example.doctarhyf.shopm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.doctarhyf.shopm.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragnentErrorMessage.OnFragmentErrorMessageInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragnentErrorMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragnentErrorMessage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ERROR_MESSAGE = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mErrorMessage;
    private String mParam2;

    private OnFragmentErrorMessageInteractionListener mListener;

    public FragnentErrorMessage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragnentErrorMessage.
     */
    // TODO: Rename and change types and number of parameters
    public static FragnentErrorMessage newInstance(String param1, String param2) {
        FragnentErrorMessage fragment = new FragnentErrorMessage();
        Bundle args = new Bundle();
        args.putString(ARG_ERROR_MESSAGE, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mErrorMessage = getArguments().getString(ARG_ERROR_MESSAGE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_error_message, container, false);

        getActivity().setTitle("Shopm");

        //@string/msgNoConnection
        TextView tvErrorMessage = rootView.findViewById(R.id.tvErrorMessage);

        tvErrorMessage.setText("SHOPM CHECKLIST\n" +
                "\n" +
                "    1. Connecter le téléphone et le PC au même réseau WIFI des deux manières suivantes : \n" +
                "       * Soit en connectant le PC au WIFI partage par le téléphone\n" +
                "       * Soit en utilisant un modem WIFI ( Méga non nécessaire )\n" +
                "    2. Démarrer le serveur ( xampp )\n" +
                "    3. Démarrer l’application serveur ( SHOPM )\n" +
                "    4. Lancer l’application mobile sur le téléphone\n" +
                "    5. Connecter l’application mobile au serveur, de deux manières suivantes :\n" +
                "       * Soit par scan QR Code du sur l’application PC SHOPM\n" +
                "       * Soit par paramétrage manuel de l’application mobile, en entrant manuellement l’adresse IP du PC dans les parametres.");


        Button btnRetry = rootView.findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.scanServerQR();
            }
        });


        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentErrorMessageInteractionListener) {
            mListener = (OnFragmentErrorMessageInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentAddItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
    public interface OnFragmentErrorMessageInteractionListener {
        // TODO: Update argument type and name
        void scanServerQR();
    }
}
