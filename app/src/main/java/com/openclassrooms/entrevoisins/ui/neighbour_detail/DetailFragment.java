package com.openclassrooms.entrevoisins.ui.neighbour_detail;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    @BindView(R.id.neigbourhNameTitle)
    TextView neighbourNameTitle;
    @BindView(R.id.neigbourName)
    TextView neighbourName;
    @BindView(R.id.pictureNeighbour)
    ImageView neighbourPicture;
    @BindView(R.id.tv_socialNetwork)
    TextView social;
    @BindView(R.id.fab_favorite)
    FloatingActionButton fab_favorite;


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
        ButterKnife.bind(this,view);

        neighbourNameTitle.setText(neighbour.getName());
        neighbourName.setText(neighbour.getName());
        social.setText("www.facebook.fr/"+neighbour.getName());

        Glide.with(this)
                .load(neighbour.getAvatarUrl())
                .centerCrop()
                .into(neighbourPicture);

        setFabFavorite();

        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mApiService.setNeighboursFavorite(neighbour);
                setFabFavorite();
            }
        });

        return view;
    }

    public void setFabFavorite(){
        List<Neighbour> allNeighbour = mApiService.getNeighbours();
        Neighbour n = allNeighbour.get(allNeighbour.indexOf(neighbour));

        if (n.getFavorite() == true){
            fab_favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
        }
        else{
            fab_favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
        }
    }

}
