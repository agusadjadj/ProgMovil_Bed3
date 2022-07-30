package com.example.prog_movil_final.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prog_movil_final.Adapter.MateriaAdapter;
import com.example.prog_movil_final.Activities.DetalleMateriaActivity;
import com.example.prog_movil_final.R;
import com.example.prog_movil_final.Clases.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class MateriasFragment extends Fragment {

    private ArrayList<String> listMaterias;
    private MateriaAdapter matAdapter;
    FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
    ListView listViewMain;

    public MateriasFragment newInstance() {
        MateriasFragment fragment = new MateriasFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_materias, container, false);

        listViewMain = (ListView) view.findViewById(R.id.ListMaterias);
        listMaterias = new ArrayList<>();
//        matAdapter = new MateriaAdapter(getContext(),listMaterias);

        //Si tengo internet..
        if (Utils.isConnected((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))) {

            //Accedo a la base de datos de los cursos.
            CollectionReference collectionCursos = dbFire.collection("Courses");

            dbFire.collection("Courses")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        //Para cada documento, guardo el nombre en la lista.
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    listMaterias.add(document.getId());
                                }
                                //Divido las materias por año

                                Collections.sort(listMaterias,String.CASE_INSENSITIVE_ORDER);
                                //Seteo el adapter para poder cargar los datos
//                                Log.e("PRE_INFLATE",listMaterias.toString());
                                MateriaAdapter materiaAdapter = new MateriaAdapter(getActivity(),listMaterias);
                                listViewMain.setAdapter(materiaAdapter);

                            } else {
                                Log.w("TAG_ERROR_DOCUMENTS", "Error getting documents.", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ERROR",e.getMessage().toString());
                        }
                    });


        } else {
            //No está conectado a internet. No se pueden renovar los datos.
            //ToDo: Pantalla de no hay conexión a internet, aparte de la que se usa para la lista.
        }

        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Log.e("FLACOQUEHACES",listMaterias.get(position).toString());
                Intent i = new Intent(getActivity(), DetalleMateriaActivity.class);
                i.putExtra("nombreMateria",listMaterias.get(position));
                getActivity().startActivity(i);
            }
        });

        return view;
    }




}
