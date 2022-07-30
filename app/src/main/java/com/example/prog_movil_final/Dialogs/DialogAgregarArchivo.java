package com.example.prog_movil_final.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prog_movil_final.R;

@SuppressLint("ValidFragment")
public class DialogAgregarArchivo extends android.app.DialogFragment{

    private String nombreMateria;

    //Para pasarle datos al Activity
    public interface OnInputListener {
        void addArchivo(String coment, String link);
    }

    public DialogAgregarArchivo(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public DialogAgregarArchivo.OnInputListener mOnInputListener;
    private TextView mNombreMateria;
    private EditText comentarioNuevo, linkNuevo;
    private Button mActionOk, mActionCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_archivo, container, false);

        mNombreMateria = view.findViewById(R.id.agregarArchivoMateria);
        mActionCancel = view.findViewById(R.id.agregarArchivoCancelar);
        mActionOk = view.findViewById(R.id.agregarArchivoOK);
        comentarioNuevo = view.findViewById(R.id.agregarArchivoComent);
        linkNuevo = view.findViewById(R.id.agregarArchivoLink);

        mNombreMateria.setText(nombreMateria);

        mActionCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        Log.e("DEBUG_CANCEL", "onClick: closing dialog");
                        getDialog().dismiss();
                    }
                });

        mActionOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {

                        Log.e("DEBUG_OK", "onClick: capturing input");

                        if(comentarioNuevo.getText().toString().isEmpty() || linkNuevo.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"Ingrese algo",Toast.LENGTH_LONG).show();
                        }else{
                            mOnInputListener.addArchivo(comentarioNuevo.getText().toString(), linkNuevo.getText().toString());
                            getDialog().dismiss();
                        }


                    }
                });

        return view;
    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (DialogAgregarArchivo.OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
