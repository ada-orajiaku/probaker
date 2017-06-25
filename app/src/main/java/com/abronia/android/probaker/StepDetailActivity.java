package com.abronia.android.probaker;

import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.fragments.StepDetailsFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity
implements StepDetailsFragment.OnFragmentInteractionListener{

    private Step step;
    private static final String FRAGMENT_STEP_DETAIL =
            "com.abronia.android.probaker.RecipeDetailActivity.STEP_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Bundle bundle = getIntent().getExtras();
        step = bundle.getParcelable(this.getString(R.string.step_package_name));

        Fragment f = StepDetailsFragment.newInstance(step);
        getSupportFragmentManager() //
                .beginTransaction() //
                .add(R.id.step_details_fragment, f, FRAGMENT_STEP_DETAIL) //
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
