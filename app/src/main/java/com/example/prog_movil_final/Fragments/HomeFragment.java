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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prog_movil_final.Activities.AuthActivity;
import com.example.prog_movil_final.Activities.DetalleMateriaActivity;
import com.example.prog_movil_final.Activities.HomeActivity;
import com.example.prog_movil_final.Activities.SplashScreen;
import com.example.prog_movil_final.Adapter.ReviewAdapter;
import com.example.prog_movil_final.Clases.DBHandler;
import com.example.prog_movil_final.Clases.Review;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Clases.VolleyCallBack;
import com.example.prog_movil_final.Dialogs.DialogAgregarCalendario;
import com.example.prog_movil_final.Dialogs.DialogAgregarComentario;
import com.example.prog_movil_final.Dialogs.DialogFragmentNoticia;
import com.example.prog_movil_final.Dialogs.DialogZoomMap;
import com.example.prog_movil_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    //Acceso a base de datos de Firebase
    FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
    ArrayList<Review> materiasSuscripto = new ArrayList<>();
    ListView listMateriasSuscripto;
    String emailAlumno;
    Button ingresarSIU, ingresarEfich, addRecordatorio, mapFICH, mapRectorado;
    ReviewAdapter comAdapter;

    //Datos para el spinner
    DBHandler db;
    Spinner listAulas;
    ArrayList<String> aulas;    //Lista de la consulta de las aulas
    TextView tvPB, tvP1, tvP2, tvP3, tvNave;
    MaterialButton zoomMap;
    String selectedAula;

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
            mapFICH = view.findViewById(R.id.homeMapFICH);  //Boton para ir al intent de map en fich
            mapRectorado = view.findViewById(R.id.homeMapRectorado);    //Boton para ir al intent de map en rectorado
            zoomMap = view.findViewById(R.id.buttonZoomMapa);   //Boton para hacer zoom

            //Tarda mucho cuando abre otra aplicación, hay que crear nuevos threads
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
                    comAdapter = new ReviewAdapter(getContext(),materiasSuscripto,true);
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

        addRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAgregarCalendario dialog = new DialogAgregarCalendario();
                dialog.show(getActivity().getFragmentManager(), "MyTag");
                //Cuando apreto ok, llama a la interfaz

            }
        });

        mapFICH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creo el intent, tengo cargado el link de la posicion exacta
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.FICH_GeoPos)));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        mapRectorado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creo el intent, tengo cargado el link de la posicion exacta
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.Rectorado_GeoPos)));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        //Llenar spinner para mostrar los cursos
        db = new DBHandler(getContext());
        listAulas = view.findViewById(R.id.spinnerAulas);
        tvPB = view.findViewById(R.id.textPisoPB);
        tvP1 = view.findViewById(R.id.textPisoP1);
        tvP2 = view.findViewById(R.id.textPisoP2);
        tvP3 = view.findViewById(R.id.textPisoP3);
        tvNave = view.findViewById(R.id.textPisoNave);

//        db.clearAll();
//        Utils.loadDatabase(getContext());

        //Me traigo las aulas desde la Base de datos.
        aulas = new ArrayList<>();

        aulas = db.getAulas();
        aulas.add(0,"Seleccione un aula...");
        //Seteo en la lista, todas las aultas
        ArrayAdapter<String> adapterAulas = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, aulas);
        listAulas.setAdapter(adapterAulas);

        listAulas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View contextView, int position, long l) {

                if(position!=0){

                    selectedAula = aulas.get(position);
                    String selectedPiso = db.getPiso(selectedAula);
                    Log.e("",selectedPiso);
                    switch(selectedPiso){
                        case "0":
                            tvPB.setAlpha(1.0f);
                            tvP1.setAlpha(0.3f);
                            tvP2.setAlpha(0.3f);
                            tvP3.setAlpha(0.3f);
                            tvNave.setAlpha(0.3f);
                            break;
                        case "1":
                            tvPB.setAlpha(0.3f);
                            tvP1.setAlpha(1.0f);
                            tvP2.setAlpha(0.3f);
                            tvP3.setAlpha(0.3f);
                            tvNave.setAlpha(0.3f);
                            break;
                        case "2":
                            tvPB.setAlpha(0.3f);
                            tvP1.setAlpha(0.3f);
                            tvP2.setAlpha(1.0f);
                            tvP3.setAlpha(0.3f);
                            tvNave.setAlpha(0.3f);
                            break;
                        case "3":
                            tvPB.setAlpha(0.3f);
                            tvP1.setAlpha(0.3f);
                            tvP2.setAlpha(0.3f);
                            tvP3.setAlpha(1.0f);
                            tvNave.setAlpha(0.3f);
                            break;
                        case "5":
                            tvPB.setAlpha(0.3f);
                            tvP1.setAlpha(0.3f);
                            tvP2.setAlpha(0.3f);
                            tvP3.setAlpha(0.3f);
                            tvNave.setAlpha(1.0f);
                            break;
                    }
                }else {
                    tvPB.setAlpha(0.3f);
                    tvP1.setAlpha(0.3f);
                    tvP2.setAlpha(0.3f);
                    tvP3.setAlpha(0.3f);
                    tvNave.setAlpha(0.3f);
                    selectedAula = "-1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        zoomMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedAula != null && selectedAula != "-1") {
                    DialogZoomMap displayMap = new DialogZoomMap();
                    Bundle args = new Bundle();
                    //Paso el path de la imagen
                    args.putString("pathImage", db.getPlano(selectedAula));

                    displayMap.setArguments(args);
                    //Muestro la ventana emergente.
                    displayMap.show(getFragmentManager(), "MyTag");
                }



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
//            Log.e("NOMBREMATERIA",auxNombreMateria);

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
//                                Log.e("DEBUG_MATERIAS_PRE",auxMateria.getAutor()+" -> "+auxMateria.getComentario());
                            }else{
                                Review auxMateria = new Review(auxNombreMateria,"");
                                materiasSuscripto.add(auxMateria);
//                                Log.e("DEBUG_MATERIAS_PRE",auxMateria.getAutor()+" -> "+auxMateria.getComentario());
                            }
                            myCallBack.onSuccess(new ArrayList<>());

                        }
                    });


    }


}