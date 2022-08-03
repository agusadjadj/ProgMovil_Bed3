package com.example.prog_movil_final.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prog_movil_final.Clases.DBHandler;
import com.example.prog_movil_final.Clases.Utils;
import com.example.prog_movil_final.Fragments.AulasFragment;
import com.example.prog_movil_final.Fragments.ClasesFragment;
import com.example.prog_movil_final.Fragments.FragmentAgendaUNL;
import com.example.prog_movil_final.Fragments.HomeFragment;
import com.example.prog_movil_final.Fragments.MateriasFragment;
import com.example.prog_movil_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    //Informaciòn extra
    Button logOut;
    TextView welcomeInfo;
    DBHandler db = new DBHandler(HomeActivity.this);

    //NavigationBottom para generar el paso entre fragments
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOut = (Button) findViewById(R.id.logoutButton);
        welcomeInfo = (TextView) findViewById(R.id.TextPersonName);

//        Utils.loadDatabase(HomeActivity.this);

        //-----POST INICIO DE SESIÓN-----//
        //Obtener dato de la cuenta de la sesión
        GoogleSignInAccount accData = GoogleSignIn.getLastSignedInAccount(this); //Obtengo la sesión actual

        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit();
        prefs.putString("email",accData.getEmail().toString());    //Guardo el correo
        prefs.putString("nombre",accData.getDisplayName().toString());  //Guardo el nombre completo de la persona
        prefs.apply();

        //Si no es nulo, obtengo los datos que requiera
        if(accData!=null){
//            Log.e("retrieveDatafromUser",accData.getPhotoUrl().toString());
            welcomeInfo.setText("Bienvenido "+accData.getDisplayName().toString());
        }

        //Retrieve data de Google Account
        //Cerrar sesión
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borrar datos
                SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit();
                prefs.clear();
                prefs.apply();
                //Cerrar sesión
                FirebaseAuth.getInstance().signOut();
                //Borro todos los intent que existen y vuelvo a la AuthActivity para volver a iniciar sesión
                Intent logBack = new Intent(HomeActivity.this, AuthActivity.class);
                logBack.addFlags(   Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logBack);
                finish();
            }
        });
        //-----FIN POST INICIO DE SESIÓN-----//

        //-----HOME FRAGMENT - NAVIGATION BAR CONFIGURATION-----//
        navView = findViewById(R.id.navBar);

        navView.setOnItemSelectedListener(selectedItem);

        HomeFragment homeFrag = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContent, homeFrag, "");
        fragmentTransaction.commit();

    }

    private NavigationBarView.OnItemSelectedListener selectedItem = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            FragmentTransaction fragmentTransact = getSupportFragmentManager().beginTransaction();

            switch (menuItem.getItemId()) {

                case R.id.nav_Home:
                    HomeFragment home_Fragm = new HomeFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransact.replace(R.id.FragmentContent, home_Fragm, "");
                    fragmentTransact.commit();
                    Log.e("DEBUG_SELECT_ITEM",menuItem.toString());
                    return true;

                    //ToDo: (2) Noticias de la facultad.
                case R.id.nav_Noticias:
                    FragmentAgendaUNL agenda_Fragm = new FragmentAgendaUNL();
//                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransact.replace(R.id.FragmentContent, agenda_Fragm);
                    fragmentTransact.commit();
                    Log.e("DEBUG_SELECT_ITEM",menuItem.toString());
                    return true;

                //ToDo: (3) WS de Bedelía. Al hacer click, muestra que aula es.
                case R.id.nav_Clases:
                    ClasesFragment fragment2 = new ClasesFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.FragmentContent, fragment2, "");
                    fragmentTransaction2.commit();
                    return true;

                //ToDo: (4) Listado de las materias para suscribirse y ver información
                case R.id.nav_Materias:
                    MateriasFragment listFragment = new MateriasFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.FragmentContent, listFragment, "");
                    fragmentTransaction3.commit();
                    return true;

                //ToDo: (5) Navegación a traves de las aulas y su mapa (Con Zoom)
                case R.id.nav_aulas:
                    AulasFragment fragment4 = new AulasFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.FragmentContent, fragment4, "");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }
    };
}


    //ToDo: Activity Principal con botones. Al pulsar un botón, se inicia un fragment. Los fragments para volver para atras hay que tener en cuenta el onBackPressed
    // Integración de Clima (https://www.geeksforgeeks.org/how-to-build-a-weather-app-in-android/)

    //https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
    //https://stackoverflow.com/questions/63751166/how-can-multiple-buttons-in-fragment-load-different-data

    //ToDo: Botones Principales
    // Servicio de Google Maps con rectorado y otros edif

    //ToDo:
    // Si una clase se suspende, notificación al celu de que se suspendio. La cagada es que puede que te lleguen
    // notific de clases que no te importan.

    //ToDo: (5) Calendar con https://bamideleomonayin.medium.com/a-simple-explanation-of-how-to-use-recyclerview-on-android-e8aec236b67f
    // Calendario personalizado para las materias.