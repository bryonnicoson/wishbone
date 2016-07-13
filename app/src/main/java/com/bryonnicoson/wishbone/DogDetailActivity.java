package com.bryonnicoson.wishbone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DogDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView mDogPhoto = (ImageView) findViewById(R.id.dog_photo);
        TextView mDogName = (TextView) findViewById(R.id.dog_name);
        TextView mDogBreed = (TextView) findViewById(R.id.dog_breed);
        TextView mDogAge = (TextView) findViewById(R.id.dog_age);
        TextView mDogSex = (TextView) findViewById(R.id.dog_sex);
        TextView mDogSize = (TextView) findViewById(R.id.dog_size);
        TextView mDogDesc = (TextView) findViewById(R.id.dog_desc);

        Intent mainIntent = getIntent();

        mDogPhoto.setImageResource(mainIntent.getIntExtra("PHOTO", 0));
        mDogName.setText(mainIntent.getStringExtra("NAME"));
        mDogBreed.setText(mainIntent.getStringExtra("BREED"));
        mDogAge.setText(mainIntent.getStringExtra("AGE"));
        mDogSex.setText(mainIntent.getStringExtra("SEX"));
        mDogSize.setText(mainIntent.getStringExtra("SIZE"));
        mDogDesc.setText(mainIntent.getStringExtra("DESC"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
