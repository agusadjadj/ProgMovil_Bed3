package com.example.prog_movil_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prog_movil_final.Clases.EventoAgenda;
import com.example.prog_movil_final.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<EventoAgenda> newsList;

    public NewsAdapter(Context context, ArrayList<EventoAgenda> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Configuracion de la View
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_agenda_item,null);

        //Me traigo los datos de la View para luego setearlos
        TextView newsTitle = view.findViewById(R.id.newsTitle);
        TextView newsCopete = view.findViewById(R.id.newsCopete);
        TextView newsFecha = view.findViewById(R.id.newsTime);
        ImageView newsImage = view.findViewById(R.id.newsImage);

        EventoAgenda evento = (EventoAgenda) getItem(position);

        newsTitle.setText(evento.getTitle().toString());
        newsCopete.setText(evento.getCopete().toString());
        newsFecha.setText(evento.getTime().toString());
        if(!evento.getPathImage().isEmpty()) {
            Picasso.get().load(evento.getPathImage()).into(newsImage);
        }
//        Log.e("DEBUG_PATH",evento.getPathImage().toString());
        return view;
    }
}
