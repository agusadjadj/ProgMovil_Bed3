package com.example.prog_movil_final.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prog_movil_final.Activities.HomeActivity;
import com.example.prog_movil_final.Adapter.ClaseAdapter;
import com.example.prog_movil_final.Clases.Clase;
import com.example.prog_movil_final.Clases.DBHandler;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Clases.VolleyCallBack;
import com.example.prog_movil_final.R;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AulasFragment extends Fragment {

    //Creo la base de datos, después la inicializo
    DBHandler db;
    Spinner listAulas;
    ArrayList<String> aulas;    //Lista de la consulta de las aulas
    TextView tvPiso;
    TouchImageView imagePlano;

    public static AulasFragment newInstance() {
        AulasFragment fragment = new AulasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_aulas, container, false);

        db = new DBHandler(getContext());
        listAulas = view.findViewById(R.id.spinnerAulas);
        tvPiso = view.findViewById(R.id.textPisoAulas);
        imagePlano = (TouchImageView) view.findViewById(R.id.planoAula);



//        db.clearAll();
//        Utils.loadDatabase(getContext());

        //Me traigo las aulas desde la Base de datos.
        aulas = db.getAulas();

        //Seteo en la lista, todas las aultas
        ArrayAdapter<String> adapterAulas = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, aulas);
        listAulas.setAdapter(adapterAulas);

        //ToDo: Poner en el Spinner -> Seleccionar aula y que no aparezca nada de una
        listAulas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View contextView, int position, long l) {

                //Seteo en que piso está el aula seleccionada
                tvPiso.setText(db.getPiso(aulas.get(position)));

                //Seteo la configuración de la imagen
                int imageAux = (Integer.parseInt(db.getPlano(aulas.get(position))));
                imagePlano.setZoom(1.0f);
                imagePlano.setImageResource(getContext().getResources().getIdentifier(String.valueOf(imageAux),"drawable",getContext().getPackageName()));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

}
