package com.example.assignmentsvg.presentation.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.assignmentsvg.R;
import com.example.assignmentsvg.data.DogImage;
import com.example.assignmentsvg.domain.helpers.ImageCache;
import com.example.assignmentsvg.domain.api.DogAPI;
import com.example.assignmentsvg.presentation.viewmodels.GenerateDogsViewmodel;
import com.google.gson.ToNumberStrategy;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateDogs extends AppCompatActivity {
    private ImageView ivDog;
    private GenerateDogsViewmodel viewModel;

    Button btnGenerate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_dogs);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initViewModel();
        setListeners();

    }

    public void initViews(){
        ivDog = findViewById(R.id.ivDog);
        btnGenerate = findViewById(R.id.btnGenerate);

    }

    public void setListeners(){
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // fetchRandomDogIMage();
                viewModel.fetchRandomDogImage(GenerateDogs.this);
                Toast.makeText(GenerateDogs.this, "i love simple viral games plz hire me....", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(GenerateDogs.this).get(GenerateDogsViewmodel.class);

        viewModel.getDogImage().observe(this, bitmap -> {
            if (bitmap != null) {
                ivDog.setImageBitmap(bitmap);
            }
        });

    }



}