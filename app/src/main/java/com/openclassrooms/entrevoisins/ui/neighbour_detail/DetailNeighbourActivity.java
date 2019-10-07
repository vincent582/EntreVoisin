package com.openclassrooms.entrevoisins.ui.neighbour_detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import java.io.Serializable;

public class DetailNeighbourActivity extends AppCompatActivity {

    private DetailFragment mDetailFragment;
    private Serializable mNeighbour;
    public static String NEIGHBOUR = "NEIGHBOUR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__neighbour);

        if (getIntent().hasExtra(NEIGHBOUR))
        {
            mNeighbour = getIntent().getSerializableExtra(NEIGHBOUR);
        }

        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_activity_framelayout);
        if (mDetailFragment == null){
            Bundle args = new Bundle();
            args.putSerializable(NEIGHBOUR,mNeighbour);

            mDetailFragment = new DetailFragment();
            mDetailFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_activity_framelayout,mDetailFragment)
                    .commit();
        }
    }
}
