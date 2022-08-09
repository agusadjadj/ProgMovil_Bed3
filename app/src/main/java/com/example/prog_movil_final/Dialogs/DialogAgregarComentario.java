package com.example.prog_movil_final.Dialogs;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
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
import androidx.fragment.app.DialogFragment;

import com.example.prog_movil_final.Activities.DetalleMateriaActivity;
import com.example.prog_movil_final.R;

import org.w3c.dom.Text;

@SuppressLint("ValidFragment")

public class DialogAgregarComentario extends android.app.DialogFragment {

    //Para pasarle datos al Activity
    public interface OnInputListener {
        void addComentario(String input);
    }

    public DialogAgregarComentario(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    private String nombreMateria;
    public OnInputListener mOnInputListener;
    private TextView mNombreMateria;
    private EditText comentarioNuevo;
    private Button mActionOk, mActionCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_comentario, container, false);

        mNombreMateria = view.findViewById(R.id.agregarComentarioMateria);
        mActionCancel = view.findViewById(R.id.agregarComentarioCancelar);
        mActionOk = view.findViewById(R.id.agregarComentarioOK);
        comentarioNuevo = view.findViewById(R.id.agregarComentarioComent);

        mNombreMateria.setText(nombreMateria);

        mActionCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
//                        Log.d("DEBUG_CANCEL", "onClick: closing dialog");
                        getDialog().dismiss();
                    }
                });

        mActionOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        if(comentarioNuevo.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"Ingrese algo",Toast.LENGTH_LONG).show();
                        }else{
                            Log.d("DEBUG_OK", "onClick: capturing input");
                            String input
                                    = comentarioNuevo.getText().toString();
                            mOnInputListener.addComentario(input);
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
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
