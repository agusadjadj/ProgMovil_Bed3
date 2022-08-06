package com.example.prog_movil_final.Dialogs;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prog_movil_final.R;

import java.util.Calendar;
import java.util.TimeZone;

public class DialogAgregarCalendario extends android.app.DialogFragment{

    private EditText mTituloEvento, mDescripEvento, mFechaEvento, mHoraEvento;
    private Button bCancelar, bGuardar, bHora, bFecha;
    private int mDia, mMes, mAnio, mHora, mMinutos;

    public DialogAgregarCalendario() {}

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_event_calendar, container, false);

        //Inicializo los botones y EditText
        mTituloEvento = (EditText) view.findViewById(R.id.eventCalendarTitulo);
        mDescripEvento = (EditText) view.findViewById(R.id.eventCalendarDescripcion);
        mFechaEvento = (EditText) view.findViewById(R.id.eventCalendarFecha);
        mHoraEvento = (EditText) view.findViewById(R.id.eventCalendarHora);

        bCancelar = (Button) view.findViewById(R.id.eventCalendarCancel);
        bGuardar = (Button) view.findViewById(R.id.eventCalendarOK);
        bHora = (Button) view.findViewById(R.id.eventCalendarbuttonHora);
        bFecha = (Button) view.findViewById(R.id.eventCalendarbuttonFecha);


        //Guardar en el calendario el evento, tiene que llamarse al Intent de calendario.
        bGuardar.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View view) {
                //Si hay campos vacios, debe llenarlos
                if(mTituloEvento.getText().toString().isEmpty()
                        || mFechaEvento.getText().toString().isEmpty()
                        || mHoraEvento.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor, ingrese todos los datos",
                            Toast.LENGTH_LONG).show();
                }else{
                    //Guardo el evento en caso que este bien
                    //Seteo la fecha
                    c.set(Calendar.YEAR,mAnio);
                    c.set(Calendar.MONTH,mMes);
                    c.set(Calendar.DAY_OF_MONTH, mDia);

                    //Seteo la hora
                    c.set(Calendar.HOUR_OF_DAY,mHora);
                    c.set(Calendar.MINUTE,mMinutos);

                    //Llamo al calendario
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");

                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,c.getTimeInMillis());

                    intent.putExtra(CalendarContract.Events.TITLE,mTituloEvento.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,mDescripEvento.getText().toString());

                    startActivity(intent);
                    dismiss();

                }

            }
        });

        //Cuando cancela, debe cerrar la ventana
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //Acceder al datePicker
        bFecha.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance(TimeZone.getDefault());

            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mDia = day;
                        mMes = month;
                        mAnio = year;
                        mFechaEvento.setText(mDia+"/"+mMes+"/"+mAnio);

                    }
                },c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH),c.get(Calendar.YEAR));
                datePicker.show();
            }
        });

        //Acceder al timePicker
        bHora.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance(TimeZone.getDefault());
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker= new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        mMinutos = minute;
                        mHora = hour;
                        mHoraEvento.setText(mHora+":"+mMinutos);

                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
                timePicker.show();
            }
        });


        return view;
    }

}
