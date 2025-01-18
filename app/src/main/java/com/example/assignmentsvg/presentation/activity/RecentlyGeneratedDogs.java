package com.example.assignmentsvg.presentation.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentsvg.R;
import com.example.assignmentsvg.domain.helpers.ImageCache;
import com.example.assignmentsvg.presentation.adapter.DogSliderAdapter;
import com.example.assignmentsvg.presentation.viewmodels.GenerateDogsViewmodel;
import com.example.assignmentsvg.presentation.viewmodels.RecentlyGeneratedDogsViewModel;

import java.util.List;

public class RecentlyGeneratedDogs extends AppCompatActivity {
    private RecyclerView rvDogSlider;
    private Button btnClearCache;
    private DogSliderAdapter adapter;
    private RecentlyGeneratedDogsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recently_generated_dogs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initViews();
        setListeners();
        initViewModel();

        observeDogImages();
        viewModel.fetchCachedImages();



    }

    private void initViews(){
        rvDogSlider = findViewById(R.id.rvDogSlider);
        btnClearCache = findViewById(R.id.btnClearCache);
    }

    private void setListeners(){
        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearCache(RecentlyGeneratedDogs.this);

            }
        });
    }

    private void initViewModel() {
        viewModel =  new RecentlyGeneratedDogsViewModel();



    }

    private void observeDogImages() {
        viewModel.getDogImagesLiveData().observe(this, new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> cachedImages) {
                if (adapter == null) {
                    adapter = new DogSliderAdapter(cachedImages, RecentlyGeneratedDogs.this);
                    rvDogSlider.setLayoutManager(new LinearLayoutManager(RecentlyGeneratedDogs.this, LinearLayoutManager.HORIZONTAL, false));
                    rvDogSlider.setAdapter(adapter);

                } else {
                    adapter.updateData(cachedImages);

                }
            }
        });
    }



}