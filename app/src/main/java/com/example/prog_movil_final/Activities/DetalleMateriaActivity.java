package com.example.prog_movil_final.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.prog_movil_final.Adapter.FileAdapter;
import com.example.prog_movil_final.Adapter.ReviewAdapter;
import com.example.prog_movil_final.Clases.Archivo;
import com.example.prog_movil_final.Clases.Review;
import com.example.prog_movil_final.Dialogs.DialogAgregarArchivo;
import com.example.prog_movil_final.Dialogs.DialogAgregarComentario;
import com.example.prog_movil_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetalleMateriaActivity extends AppCompatActivity implements DialogAgregarComentario.OnInputListener, DialogAgregarArchivo.OnInputListener {

    //Información general
    String emailAlumno;
    String nombreAlumno;
    String nombreMateria;
    FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
    Boolean estaSuscripto;   //Suscripcion al curso

    //Adapters
    ReviewAdapter comAdapter;
    FileAdapter filesAdapter;

    //Lista que contiene los comentarios y archivos de la materia
    ArrayList<Review> reviews = new ArrayList<>();
    ArrayList<Archivo> files = new ArrayList<>();

    //Elementos del Activity
    TextView nombMat;
    TextView horaMat;
    Button suscribMat;
    Button agregarComentario;
    Button agregarArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_materia);

        //Me fijo quién está conectado
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        emailAlumno = prefs.getString("email",null);
        nombreAlumno = prefs.getString("nombre",null);

        //Creo los textView para actualizarlos
        nombMat = (TextView) findViewById(R.id.detalleNombreMateria);
        horaMat = (TextView) findViewById(R.id.detalleHorarios);
        suscribMat = (Button) findViewById(R.id.detalleSuscribirse);

        //Me traigo la materia a la cual se le hizo click para luego buscar y utilizar
        Bundle extrasInt = getIntent().getExtras();
        nombreMateria = extrasInt.getString("nombreMateria");
        Log.e("DEBUG_MATERIA_DETALLE",nombreMateria);

        getSuscripcion();
        //ToDo: Hacer algo cuando las listas están vacias.
        loadDatosMateria();
        loadArchivos();
        loadComentarios();

        //ToDo: Agregar que al hacer click de la lista de archivos, se abra el link

        //Para agregar un nuevo comentario
        agregarComentario = findViewById(R.id.detalleAgregarComentarios);
        agregarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAgregarComentario dialog = new DialogAgregarComentario(nombreMateria);
                dialog.show(getFragmentManager(),"MyTag");
                //Cuando apreto ok, llama a la interfaz

            }
        });

        //Para agregar un nuevo archivo
        agregarArchivo = findViewById(R.id.detalleAgregarArchivos);
        agregarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAgregarArchivo dialog = new DialogAgregarArchivo(nombreMateria);

                dialog.show(getFragmentManager(),"MyTag");
                //Cuando apreto ok, llama a la interfaz pasando la descrip y el link.

            }
        });

        suscribMat = findViewById(R.id.detalleSuscribirse);
        suscribMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si no está suscripto
                if(!estaSuscripto){
                    //Cargo los datos para guardar
                    Map<String, Object> data = new HashMap<>();
                    data.put("faculty", "FICH");
                    data.put("score", 0);
                    data.put("is_rated", false);

                    //Actualizo la base de datos.
                    dbFire.collection("Users")
                            .document(emailAlumno)
                            .collection("MyCourses")
                            .document(nombreMateria)
                            .set(data);
                    estaSuscripto=true;
                    suscribMat.setText("DESUSCRIBIRSE");

                }else{
                    //Lo borro de la base de datos.
                    dbFire.collection("Users")
                            .document(emailAlumno)
                            .collection("MyCourses")
                            .document(nombreMateria)
                            .delete();
                    estaSuscripto=false;
                    suscribMat.setText("SUSCRIBIRSE");

                }

            }
        });


    }

    //Funcion que llama desde el DialogFragment de agregar comentario.
    @Override public void addComentario(String comentario) {

        //Actualizo dinamicamente la lista
        reviews.add(new Review(nombreAlumno,comentario));
        comAdapter.notifyDataSetChanged();

        //Armo el mapa para mandarlo a la base de datos
        Map<String, String> data = new HashMap<>();
        data.put("author", nombreAlumno);
        data.put("body", comentario);

        //Guardo en la base de datos
        dbFire.collection("Courses")
                .document(nombreMateria)
                .collection("Reviews")
                .add(data);

    }

    //Funcion que llama desde el DialogFragment de agregar archivo.
    @Override
    public void addArchivo(String coment, String link) {

        //Actualizo dinamicamente la lista
        files.add(new Archivo(nombreAlumno,coment,link));
        filesAdapter.notifyDataSetChanged();

        //Armo el mapa para mandarlo a la base de datos
        Map<String, String> data = new HashMap<>();
        data.put("author", nombreAlumno);
        data.put("description", coment);
        data.put("link", link);

        //Guardo en la base de datos
        dbFire.collection("Courses")
                .document(nombreMateria)
                .collection("Files")
                .add(data);

    }


    private void getSuscripcion(){
        //Chequear si un usuario está suscripto a la materia o no.
        dbFire.collection("Users")
//                .document("gorounl@gmail.com")
                .document(emailAlumno)
                .collection("MyCourses")
                .document(nombreMateria)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() & task.getResult().exists()){
                            //Si encuentra en MyCourses, es porque está suscripto.
                            suscribMat.setText("DESUSCRIBIRSE");
                            estaSuscripto = true;

                        }else{
                            //Si no lo encuentra en MyCourses, hay que dejarlo suscribirse.
                            suscribMat.setText("SUSCRIBIRSE");
                            estaSuscripto = false;
                        }
                    }
                });
    }

    private void loadDatosMateria(){
        //Traer los datos de la materia.
        dbFire.collection("Courses")
                .document(nombreMateria)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        nombMat.setText(nombreMateria);
                        if(task.getResult().get("hours") != null){
                            horaMat.append(": " + task.getResult().get("hours").toString());
                        }
                    }
                });

    }

    private void loadComentarios(){

        //Traigo la review y las cargo en la lista
        dbFire.collection("Courses")
                .document(nombreMateria)
                .collection("Reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot review : task.getResult()) {
                                Review auxRev = new Review(review.get("author").toString(),review.get("body").toString());

                                reviews.add(auxRev);
                            }
                            //Cargo en la lista lo que me traje de la db
                            ListView commentsList = (ListView) findViewById(R.id.detalleListaComentarios);
                            comAdapter = new ReviewAdapter(DetalleMateriaActivity.this,reviews);
                            commentsList.setAdapter(comAdapter);
                        }


                    }
                });
    }

    private void loadArchivos(){
        //Traigo los documentos y las cargo en la lista
        dbFire.collection("Courses")
                .document(nombreMateria)
                .collection("Files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot file : task.getResult()) {

                                Archivo auxFile = new Archivo(file.get("author").toString(),
                                        file.get("description").toString(),
                                        file.get("link").toString());

                                files.add(auxFile);
                            }
                            //Cargo en la lista lo que me traje de la db
                            ListView filesList = (ListView) findViewById(R.id.detalleListaArchivos);
                            filesAdapter = new FileAdapter(DetalleMateriaActivity.this,files);
                            filesList.setAdapter(filesAdapter);
                        }
                    }
                });
    }
}