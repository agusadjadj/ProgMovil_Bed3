package com.example.prog_movil_final.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prog_movil_final.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {
    //ToDo: Arreglar iconos de Google y Facebook
    private int GOOGLE_SIGN_IN = 100;

    //Base de datos de Firebase.
    FirebaseFirestore dbFire = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Button mButtonEnter = findViewById(R.id.mButtonEnter);
        EditText mLoginUser = findViewById(R.id.mLoginUser);
        EditText mLoginPass = findViewById(R.id.mLoginPass);
        Button mButtonGoogle = findViewById(R.id.mButtonGoogle);

        //----Ingresar con un usuario creado
        mButtonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si el usuario y contraseña no están vacios..
                if(!mLoginUser.getText().toString().isEmpty() && !mLoginPass.getText().toString().isEmpty()){
                    //Se ingresa con los datos ingresados.
                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(mLoginUser.getText().toString(),
                                    mLoginPass.getText().toString()).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){    //Si salio bien



                                        //Paso al Home.
                                        Intent home = new Intent(AuthActivity.this, HomeActivity.class);
                                        home.putExtra("email",mLoginUser.getText().toString());
                                        startActivity(home);
                                        finish();
                                    }else{                      //Si salio bien't
                                        showAlert();
                                    }
                                }
                            });

                }


            }
        });

        //----Comprobar si hay una sesión activa
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email",null);
        // Si existe una sesión, me pago el login, de lo contrario, tengo que mostrarlo.
        if(email != null){
            Intent home = new Intent(AuthActivity.this, HomeActivity.class);
            home.putExtra("email",email);
            startActivity(home);
            finish(); //Mata el activity para que cuando presione el boton atras, no vuelva al login.
        }

        //----Inicio de sesión por Google
        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Configuración

                GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(AuthActivity.this,googleConf);
                googleClient.signOut();
                startActivityForResult(googleClient.getSignInIntent(),GOOGLE_SIGN_IN);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){    //Si salio bien
                                //Chequeo si existe
                                checkUserDatabase(account);

                                Intent home = new Intent(AuthActivity.this, HomeActivity.class);
                                home.putExtra("email",account.getEmail().toString());
                                startActivity(home);
                                finish();
                            }else{                      //Si salio bien't
                                showAlert();
                            }

                        }
                    });
                }
            } catch (ApiException e) {
                e.printStackTrace();
                showAlert();
            }
        }

    }

    protected void onStart(){
        super.onStart();
        this.setVisible(true);
    }

    private void showAlert(){

        Dialog dialogAlert = new Dialog(this);
        dialogAlert.setContentView(R.layout.alert_dialog);
        TextView txt = (TextView) dialogAlert.findViewById(R.id.textDialog);
        txt.setText("No se encuentra el usuario registrado.");
        dialogAlert.setTitle("Error");
        Button dialogButton = (Button) dialogAlert.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.dismiss();
            }
        });
        dialogAlert.show();
    }

    private void checkUserDatabase(GoogleSignInAccount acc) {
        //acc contiene todos los datos de la cuenta para llenar la base de datos de usuarios por primera vez.
        CollectionReference collectionUser = dbFire.collection("Users");

        //Revisamos que exista el usuario.
        DocumentReference userFire = collectionUser.document(acc.getEmail().toString());
        userFire.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e("DEBUG_BUENO", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.e("DEBUG_MALO", "No such document, ahora lo agrego");
                        Map<String,String> data = new HashMap<>();
                        //Campos Firebase Database (email,name).
                        data.put("email",acc.getEmail().toString());
                        data.put("name",acc.getDisplayName().toString());
                        collectionUser.document(acc.getEmail().toString()).set(data);
                    }
                } else {
                    Log.e("DEBUG_MALISIMO", "get failed with ", task.getException());
                }
            }
        });

    }

}


