package com.openclassrooms.entrevoisins.ui.neighbour_detail;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private NeighbourApiService mApiService;
    protected Neighbour neighbour;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getIntent().hasExtra("neighbour"))
        {
            neighbour = (Neighbour) getActivity().getIntent().getSerializableExtra("neighbour");
        }
        mApiService = DI.getNeighbourApiService();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView neighbourNameTitle = view.findViewById(R.id.neigbourhNameTitle);
        neighbourNameTitle.setText(neighbour.getName());

        TextView neighbourName = view.findViewById(R.id.neigbourName);
        neighbourName.setText(neighbour.getName());

        ImageView neighbourPicture = view.findViewById(R.id.pictureNeighbour);
        Glide.with(this)
                .load(neighbour.getAvatarUrl())
                .centerCrop()
                .into(neighbourPicture);

        TextView social = view.findViewById(R.id.tv_socialNetwork);
        social.setText("www.facebook.fr/"+neighbour.getName());

        FloatingActionButton fab_favorite = view.findViewById(R.id.fab_favorite);
        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApiService.setNeighboursFavorite(neighbour);
                if (neighbour.getFavorite() == true){
                    fab_favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                }
                else{
                    fab_favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                }
            }
        });

        return view;
    }

}
