package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.project2.Database.DexDatabase;
import com.example.project2.Database.DexRepository;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivityFavoritesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;

    private DexRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.favHomeButton.setOnClickListener(v -> {
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
        });

        // Access the repository
        repository = DexRepository.getRepository(getApplication());

        if (repository == null) {
            Log.e("FavoritesActivity", "Repository is null!");
            Toast.makeText(this, "An error occurred while accessing the database.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Retrieve the loggedInUserId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), -1);

        // Inside onCreate() method
        DexDatabase.databaseWriteExecutor.execute(() -> {
            // Perform database access on background thread
            User user = repository.getUserByUserIdSync(loggedInUserId);

            if (user != null) {
                List<String> favPokemonList = new ArrayList<>(user.getFavPokemonList());
                runOnUiThread(() -> {
                    // Update the UI with the fetched data
                    fetchPokemonData(favPokemonList);
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(FavoritesActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }


    static Intent favoritesIntentFactory(Context context){
        return new Intent(context, FavoritesActivity.class);
    }

    private void fetchPokemonData(List<String> favPokemonList) {
        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);
        LinearLayout container = findViewById(R.id.favPokemonContainer);

        // Clear the container before adding new views
        container.removeAllViews();

        for (String pokemonName : favPokemonList) {
            Call<Pokemon> call = apiService.getPokemon(pokemonName);

            call.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Pokemon pokemon = response.body();

                        // Check if the sprites object is null
                        if (pokemon.getSprites() != null && pokemon.getSprites().getFrontDefault() != null) {
                            // Dynamically create TextView and ImageView for each Pokémon
                            TextView pokemonNameTextView = new TextView(FavoritesActivity.this);
                            pokemonNameTextView.setText(pokemon.getName());
                            pokemonNameTextView.setTextSize(20);
                            pokemonNameTextView.setPadding(0, 10, 0, 10);
                            pokemonNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                            ImageView pokemonImageView = new ImageView(FavoritesActivity.this);

                            // Resize the image using Glide
                            Glide.with(FavoritesActivity.this)
                                    .load(pokemon.getSprites().getFrontDefault())
                                    .override(300, 300)
                                    .into(pokemonImageView);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            layoutParams.gravity = Gravity.CENTER; // This centers the image

                            pokemonImageView.setLayoutParams(layoutParams);

                            container.addView(pokemonNameTextView);
                            container.addView(pokemonImageView);
                        } else {
                            Toast.makeText(FavoritesActivity.this, "Sprite not available for " + pokemon.getName(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FavoritesActivity.this, "Pokémon not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    Toast.makeText(FavoritesActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}