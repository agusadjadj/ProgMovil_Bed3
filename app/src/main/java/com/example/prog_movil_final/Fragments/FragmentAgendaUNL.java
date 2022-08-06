package com.example.prog_movil_final.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.prog_movil_final.Dialogs.DialogFragmentNoticia;
import com.example.prog_movil_final.Clases.EventoAgenda;
import com.example.prog_movil_final.Clases.JSONRequest;
import com.example.prog_movil_final.R;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Clases.VolleyCallBack;
import com.example.prog_movil_final.Adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragmentAgendaUNL extends Fragment {

    private JSONRequest dataWService = new JSONRequest();
    private ArrayList<EventoAgenda> newsList = new ArrayList<>();

    public static FragmentAgendaUNL newInstance() {
        FragmentAgendaUNL fragment = new FragmentAgendaUNL();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda_unl, container, false);

        if(Utils.isConnected((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){
            //Está conectado a internet. Se puede cargar los datos.

            //Jaja mirá esta magia para que ande en sync
            dataWService.consultarNoticias(getContext(), new VolleyCallBack<EventoAgenda>() {
                @Override
                public void onSuccess(ArrayList<EventoAgenda> events) {
                    //Me guardo el array generado en la llamada JSON para utilizarlo
                    newsList = events;
//                    Log.e("DEBUG_NEWS",String.valueOf(newsList.size()));

                    //Función para ordenar por fecha de más nuevo a más viejo
                    Collections.sort(newsList, new Comparator<EventoAgenda>() {
                        @Override
                        public int compare(EventoAgenda t1, EventoAgenda t2) {
                            //Ordeno de más nuevo a más viejo
                            return t2.getTime().compareTo(t1.getTime());
                        }
                    });

                    //Me traigo la listview para cargarle los adapters
                    ListView listViewMain = view.findViewById(R.id.listNewsMain);
                    //Seteo el adapter para poder cargar los datos
                    NewsAdapter newsAdapter = new NewsAdapter(getActivity(),newsList);
                    listViewMain.setAdapter(newsAdapter);

                    listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            showNoticiaCompleta(position);

                        }
                    });
//                    Log.e("LV_ADAPTER",String.valueOf(newsList.size()));
                }
            });

        }else{
            //No está conectado a internet. No se pueden renovar los datos.
            //ToDo: Pantalla de no hay conexión a internet, aparte de la que se usa para la lista.
        }

        return view;
    }

    public void showNoticiaCompleta(int position){

        DialogFragmentNoticia displayNoticia = new DialogFragmentNoticia();
        Bundle args = new Bundle();
        //Paso los argumentos necesarios para mostrar
        args.putString("Titulo",newsList.get(position).getTitle());
        args.putString("Cuerpo",newsList.get(position).getBody());
        args.putString("Volanta",newsList.get(position).getVolanta());
        args.putString("Imagen",newsList.get(position).getPathImage());
        args.putString("Fecha",newsList.get(position).getTime());

        displayNoticia.setArguments(args);
        //Muestro la ventana emergente.
        displayNoticia.show(getFragmentManager(),"MyTag");

    }
}