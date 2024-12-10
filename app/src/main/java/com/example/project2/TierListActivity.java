package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.bumptech.glide.Glide;
import com.example.project2.Database.PokemonTierListDao;
import com.example.project2.Database.DexDatabase;
import com.example.project2.Database.DexRepository;
import com.example.project2.Database.entities.PokemonTierList;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivityTierListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TierListActivity extends AppCompatActivity {

    private ActivityTierListBinding binding;

    private DexRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tier_list);

        binding = ActivityTierListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tlHomeButton.setOnClickListener(v -> {
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
        });

        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);
        LinearLayout container = findViewById(R.id.tierListContainer);

        // Clear the container before adding new views
        container.removeAllViews();

        DexDatabase.databaseWriteExecutor.execute(() -> {
            PokemonTierListDao dao = DexDatabase.getDatabase(getApplicationContext()).pokemonTierListDao();
            List<PokemonTierList> allTiers = dao.getAllPokemonTierLists();

            runOnUiThread(() -> {
                for (PokemonTierList tier : allTiers) {
                    // Create a new vertical LinearLayout for this tier
                    LinearLayout tierLayout = new LinearLayout(TierListActivity.this);
                    tierLayout.setOrientation(LinearLayout.VERTICAL);
                    tierLayout.setPadding(0, 20, 0, 20);

                    // Add a TextView for the tier name
                    TextView tierNameTextView = new TextView(TierListActivity.this);
                    tierNameTextView.setText("Tier: " + tier.getTier());
                    tierNameTextView.setTextSize(24); // Adjust size as needed
                    tierNameTextView.setPadding(0, 10, 0, 10);
                    tierNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    // Set the text to bold and underlined
                    tierNameTextView.setTypeface(null, Typeface.BOLD); // Make text bold
                    tierNameTextView.setPaintFlags(tierNameTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // Underline the text

                    // Add the tier name TextView to the tierLayout
                    tierLayout.addView(tierNameTextView);

                    for (String pokemon : tier.getPokemonList()) {
                        Call<Pokemon> call = apiService.getPokemon(pokemon);

                        call.enqueue(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Pokemon pokemon = response.body();

                                    if (pokemon.getSprites() != null && pokemon.getSprites().getFrontDefault() != null) {
                                        // Dynamically create TextView and ImageView for each Pokémon
                                        TextView pokemonNameTextView = new TextView(TierListActivity.this);
                                        pokemonNameTextView.setText(pokemon.getName());
                                        pokemonNameTextView.setTextSize(20);
                                        pokemonNameTextView.setPadding(0, 10, 0, 10);
                                        pokemonNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                        ImageView pokemonImageView = new ImageView(TierListActivity.this);

                                        // Resize the image using Glide
                                        Glide.with(TierListActivity.this)
                                                .load(pokemon.getSprites().getFrontDefault())
                                                .override(200, 200) // Resize image
                                                .into(pokemonImageView);

                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        layoutParams.gravity = Gravity.CENTER; // This centers the image

                                        pokemonImageView.setLayoutParams(layoutParams);

                                        // Add the Pokémon's TextView and ImageView to the tierLayout
                                        tierLayout.addView(pokemonNameTextView);
                                        tierLayout.addView(pokemonImageView);
                                    } else {
                                        Toast.makeText(TierListActivity.this, "Sprite not available for " + pokemon.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(TierListActivity.this, "Pokémon not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Pokemon> call, Throwable t) {
                                Toast.makeText(TierListActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    container.addView(tierLayout);
                }
            });
        });

    }

    static Intent tierListIntentFactory(Context context){
        return new Intent(context, TierListActivity.class);
    }

}