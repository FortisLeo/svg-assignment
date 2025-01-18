package com.example.assignmentsvg.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignmentsvg.R;

public class MainActivity extends AppCompatActivity {
    Button btnGenerateDogs, btnRecentlyGeneratedDogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initViews();
        setListeners();
    }

    private void initViews(){
        btnGenerateDogs = findViewById(R.id.btnGenerateDogs);
        btnRecentlyGeneratedDogs = findViewById(R.id.btnRecentlyGeneratedDogs);
    }

    private void setListeners(){
        btnGenerateDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateDogs.class);
                startActivity(intent);
            }


        });

        btnRecentlyGeneratedDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecentlyGeneratedDogs.class);
                startActivity(intent);

            }
        });
    }

}