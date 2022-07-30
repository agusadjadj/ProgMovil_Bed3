package com.example.prog_movil_final.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.prog_movil_final.R;
import com.squareup.picasso.Picasso;

public class DialogFragmentNoticia extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_noticia, container, false);

        Bundle args = getArguments();
        TextView title = view.findViewById(R.id.fn_Title);
        TextView volanta = view.findViewById(R.id.fn_volanta);
        TextView body = view.findViewById(R.id.fn_body);
        TextView date = view.findViewById(R.id.fn_dateTime);
        ImageView image = view.findViewById(R.id.fn_image);

        title.setText(args.getString("Titulo"));
        volanta.setText(args.getString("Volanta"));
        body.setText(Html.fromHtml(args.getString("Cuerpo")));
        date.setText(args.getString("Fecha"));
        if(!args.getString("Imagen").isEmpty()){
            Picasso.get().load(args.getString("Imagen")).into(image);
        }

        return view;
    }

}
