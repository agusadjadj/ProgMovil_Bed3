package com.example.prog_movil_final.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.prog_movil_final.Activities.AuthActivity;
import com.example.prog_movil_final.Activities.DetalleMateriaActivity;
import com.example.prog_movil_final.Activities.HomeActivity;
import com.example.prog_movil_final.Activities.SplashScreen;
import com.example.prog_movil_final.Adapter.ReviewAdapter;
import com.example.prog_movil_final.Clases.Review;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Clases.VolleyCallBack;
import com.example.prog_movil_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    //Acceso a base de datos de Firebase
    FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
    ArrayList<Review> materiasSuscripto = new ArrayList<>();
    ListView listMateriasSuscripto;
    String emailAlumno;
    Button ingresarSIU, ingresarEfich, addRecordatorio;
    ReviewAdapter comAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        emailAlumno = GoogleSignIn.getLastSignedInAccount(getContext()).getEmail().toString();
//        Log.e("DEBUG_MAIL",emailAlumno);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Si está conectado a internet, puedo seguir
        if(Utils.isConnected((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){

            listMateriasSuscripto = view.findViewById(R.id.homeMisCursos);  //Me traigo para cargar la lista con los cursos
            ingresarSIU = view.findViewById(R.id.homeSIU);  //Boton para entrar al SIU
            ingresarEfich = view.findViewById(R.id.homeEfich);  //Boton para entrar al e-fich
            addRecordatorio = view.findViewById(R.id.homeRecordatorio); //Boton para agregar recordatorio

            //ToDo: Tarda mucho cuando abre otra aplicación, hay que crear nuevos threads
            //Me cargo todos los cursos a los que está suscrito
            loadCursos(new VolleyCallBack<Review>() {
                @Override
                public void onSuccess(ArrayList<Review> events) {
                    Collections.sort(materiasSuscripto, new Comparator<Review>() {
                        @Override
                        public int compare(Review r1, Review r2) {
                            return r1.getAutor().compareTo(r2.getAutor());
                        }
                    });
                    comAdapter = new ReviewAdapter(getContext(),materiasSuscripto);
//                    Log.e("DDEBUG_MATERIAS_POST",String.valueOf(materiasSuscripto.size()));
                    listMateriasSuscripto.setAdapter(comAdapter);
                }
            });






        }else{
            //No está conectado a internet. No se pueden renovar los datos.
            //ToDo: Pantalla de no hay conexión a internet, aparte de la que se usa para la lista.
        }

        ingresarSIU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://servicios.unl.edu.ar/guarani3/autogestion/acceso"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ingresarEfich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://e-fich.unl.edu.ar/moodle/entrar.php"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        //Cuando se le haga click a un curso, te abra el activity de la materia.
        listMateriasSuscripto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(getActivity(), DetalleMateriaActivity.class);
                i.putExtra("nombreMateria",materiasSuscripto.get(position).getAutor().toString());
                getActivity().startActivity(i);
            }
        });

        return view;
    }

    private void loadCursos(VolleyCallBack<Review> myCallback){


        dbFire.collection("Users")
                .document(emailAlumno)
                .collection("MyCourses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() & !task.getResult().isEmpty()){
                            for(QueryDocumentSnapshot auxDoc : task.getResult()){
                                //Me guardo el nombre de la materia a la que estoy suscripto
                                loadHorarios(myCallback, auxDoc.getId().toString());
                            }
                            myCallback.onSuccess(new ArrayList<>());
                        }else{
                            //Si no lo encuentra en MyCourses, no tiene que cargar nada, o pantalla de error
                        }
                    }

                });


    }

    private void loadHorarios(VolleyCallBack<Review> myCallBack,String auxNombreMateria){

        //Para cada materia, debo buscar los datos que necesito..
            Log.e("NOMBREMATERIA",auxNombreMateria);

            dbFire.collection("Courses")
                    .document(auxNombreMateria)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful() & task.getResult().get("hours") != null){
                                //Me guardo los datos
                                Review auxMateria = new Review(auxNombreMateria,task.getResult().get("hours").toString());
                                materiasSuscripto.add(auxMateria);
                                Log.e("DEBUG_MATERIAS_PRE",auxMateria.getAutor()+" -> "+auxMateria.getComentario());
                            }else{
                                Review auxMateria = new Review(auxNombreMateria,"");
                                materiasSuscripto.add(auxMateria);
                                Log.e("DEBUG_MATERIAS_PRE",auxMateria.getAutor()+" -> "+auxMateria.getComentario());
                            }
                            myCallBack.onSuccess(new ArrayList<>());

                        }
                    });


    }


}