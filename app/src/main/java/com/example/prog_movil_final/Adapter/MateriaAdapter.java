package com.example.prog_movil_final.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prog_movil_final.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MateriaAdapter extends BaseAdapter {

    private ArrayList<String> materias;
    private Context context;

    public MateriaAdapter(Context context, ArrayList<String> materias) {

        this.materias = materias;
        this.context = context;
    }

    @Override
    public int getCount() {
        return materias.size();
    }

    @Override
    public Object getItem(int position) {
        return materias.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_materias_item,null);
        TextView nomMateria = view.findViewById(R.id.nombreMateria);

        nomMateria.setText((String) getItem(position));

        return view;
    }
}
