package com.example.prog_movil_final.Dialogs;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.prog_movil_final.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class DialogZoomMap extends DialogFragment {

    TouchImageView mapImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_zoom_map, container, false);

        mapImage = view.findViewById(R.id.planoAula);

        Bundle args = getArguments();
        int path = Integer.parseInt(args.getString("pathImage").toString());

        mapImage.setZoom(1.0f);
//        mapImage.setRotation(90);
        mapImage.setImageResource(getContext().getResources().getIdentifier(String.valueOf(path),"drawable",getContext().getPackageName()));

        return view;
    }

}
