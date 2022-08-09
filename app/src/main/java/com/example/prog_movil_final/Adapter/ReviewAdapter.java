package com.example.prog_movil_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prog_movil_final.Clases.Archivo;
import com.example.prog_movil_final.Clases.Review;
import com.example.prog_movil_final.R;

import java.util.ArrayList;


public class ReviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Review> reviews;
    private boolean home;

    public ReviewAdapter(Context context, ArrayList<Review> reviews, boolean home) {
        this.context = context;
        this.reviews = reviews;
        this.home = home;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        //Configuracion de la View
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_review_item,null);

        TextView autor = view.findViewById(R.id.revAutor);
        TextView coment = view.findViewById(R.id.revComentario);
        ImageView image = view.findViewById(R.id.imViewHora);

        if(home){
            image.setImageResource(R.drawable.ic_materias);
        }else image.setImageResource(R.drawable.ic_baseline_comment);


        Review file = (Review) getItem(position);

        autor.setText(file.getAutor());
        coment.setText(file.getComentario());

        return view;
    }
}
