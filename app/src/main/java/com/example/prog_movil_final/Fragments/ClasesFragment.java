package com.example.prog_movil_final.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prog_movil_final.Adapter.ClaseAdapter;
import com.example.prog_movil_final.Clases.Clase;
import com.example.prog_movil_final.Clases.JSONRequest;
import com.example.prog_movil_final.R;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Clases.VolleyCallBack;

import java.util.ArrayList;


public class ClasesFragment extends Fragment {

    private JSONRequest dataWService = new JSONRequest();
    private ArrayList<Clase> clasesDiaria = new ArrayList<>();

    public static ClasesFragment newInstance() {
        ClasesFragment fragment = new ClasesFragment();
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

        View view = inflater.inflate(R.layout.fragment_clases, container, false);
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        Log.e("DEBUG_LINK",dataWService.prueba());
        if(Utils.isConnected((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){
            //Está conectado a internet. Se puede cargar los datos.

            //Jaja mirá esta magia para que ande en sync

            dataWService.consultarClases(getContext(), new VolleyCallBack<Clase>() {
                @Override
                public void onSuccess(ArrayList<Clase> clases) {
                    //Me guardo el array generado en la llamada JSON para utilizarlo
                    clasesDiaria = clases;
//                    Log.e("DEBUG_CLASE_DIARIA_AREA: ",clasesDiaria.get(1).getArea());
                    // ToDo: Agregar OnListClick para que se abra el aula donde es la clase

                    //Me traigo la listview para cargarle los adapters
                    ListView listViewMain = view.findViewById(R.id.listClasesMain);
                    //Seteo el adapter para poder cargar los datos
                    ClaseAdapter claseAdapter = new ClaseAdapter(getActivity(),clasesDiaria);
                    listViewMain.setAdapter(claseAdapter);

//                    Log.e("LV_ADAPTER",String.valueOf(newsList.size()));
                }
            });

        }else{
            //No está conectado a internet. No se pueden renovar los datos.
            //ToDo: Pantalla de no hay conexión a internet, aparte de la que se usa para la lista.
        }
        return view;
    }

}
