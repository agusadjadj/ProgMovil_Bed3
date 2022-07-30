package com.example.prog_movil_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prog_movil_final.Clases.Archivo;
import com.example.prog_movil_final.Clases.Review;
import com.example.prog_movil_final.R;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Archivo> archivos;

    public FileAdapter(Context context, ArrayList<Archivo> archivos) {
        this.context = context;
        this.archivos = archivos;
    }

    @Override
    public int getCount() {
        return archivos.size();
    }

    @Override
    public Object getItem(int position) {
        return archivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        //Configuracion de la View
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_files_item,null);

        TextView autor = view.findViewById(R.id.fileAutor);
        TextView coment = view.findViewById(R.id.fileComentario);
        TextView link = view.findViewById(R.id.fileLink);


        Archivo rev = (Archivo) getItem(position);

        autor.setText(rev.getAutor());
        coment.setText(rev.getComentario());
        link.setText(rev.getLink());


        return view;
    }
}
