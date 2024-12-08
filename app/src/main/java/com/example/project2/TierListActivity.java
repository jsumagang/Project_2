package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.databinding.ActivityTierListBinding;

public class TierListActivity extends AppCompatActivity {

    private ActivityTierListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tier_list);

        binding = ActivityTierListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tlHomeButton.setOnClickListener(v -> {
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
        });

    }

    static Intent tierListIntentFactory(Context context){
        return new Intent(context, TierListActivity.class);
    }

}