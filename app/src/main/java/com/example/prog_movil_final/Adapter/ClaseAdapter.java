package com.example.prog_movil_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prog_movil_final.Clases.Clase;
import com.example.prog_movil_final.R;

import java.util.ArrayList;

public class ClaseAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Clase> claseList;

    public ClaseAdapter(Context context, ArrayList<Clase> claseList) {
        this.context = context;
        this.claseList = claseList;
    }

    @Override
    public int getCount() { return claseList.size(); }

    @Override
    public Object getItem(int i) { return claseList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        //Configuracion de la View
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_clase_item,null);

        //Me traigo los datos de la View para luego setearlos
        TextView claseArea = view.findViewById(R.id.clase_Area);
        TextView claseAula = view.findViewById(R.id.clase_Aula);
        TextView claseFecha = view.findViewById(R.id.clase_Fecha);
        TextView claseEvento = view.findViewById(R.id.clase_Evento);
        TextView claseHora = view.findViewById(R.id.clase_Hora);

        Clase clase = (Clase) getItem(position);

        //Seteo los datos
        claseArea.setText(clase.getArea().toString());
        claseAula.setText(clase.getAula().toString());
        claseFecha.setText(clase.getFechaInicio().toString());
        claseEvento.setText(clase.getEvento().toString());
        claseHora.setText(clase.getHoraInicio().toString());

//        Log.e("DEBUG_PATH",evento.getPathImage().toString());
        return view;

    }
}
