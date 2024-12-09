package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.Database.DexDatabase;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivityPokeDexBinding;

import com.bumptech.glide.Glide;
import com.example.project2.Database.DexRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokeDexActivity extends AppCompatActivity {

    private TextView pokemonNameTextView;
    private ImageView pokemonImageView;

    private ActivityPokeDexBinding binding;

    private DexRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_dex);

        // Access the repository
        repository = DexRepository.getRepository(getApplication());

        // Retrieve the loggedInUserId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), -1);

        binding = ActivityPokeDexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pokemonNameTextView = findViewById(R.id.pokemonName);
        pokemonImageView = findViewById(R.id.pokemonImage);

        EditText pokemonInput = findViewById(R.id.pokedexUserInput);
        Button submitButton = findViewById(R.id.pokedexSubmitButton);

        Button addToFavBtn = findViewById(R.id.favoriteItButton);

        submitButton.setOnClickListener(view -> {
            String pokemonName = pokemonInput.getText().toString().trim().toLowerCase()
                    ;
            if (!pokemonName.isEmpty()) {
                fetchPokemon(pokemonName);
            } else {
                Toast.makeText(PokeDexActivity.this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        addToFavBtn.setOnClickListener(view -> {
            String pokemonName = pokemonInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    // Fetch the current user from the database
                    User user = repository.getUserByUserIdSync(loggedInUserId); // Synchronous call for background thread

                    if (user != null) {
                        // Add the Pokémon to the user's favorite list
                        List<String> favPokemonList = user.getFavPokemonList();
                        if (!favPokemonList.contains(pokemonName)) { // Prevent duplicates
                            favPokemonList.add(pokemonName);
                            user.setFavPokemonList(favPokemonList);

                            // Update the user in the database
                            repository.updateUser(user);

                            runOnUiThread(() -> Toast.makeText(PokeDexActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(PokeDexActivity.this, "Already in favorites!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(PokeDexActivity.this, "User not found", Toast.LENGTH_SHORT).show());
                    }
                });
                Toast.makeText(PokeDexActivity.this, "Success", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(PokeDexActivity.this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }

        });

        binding.homePageButton.setOnClickListener(v -> {
            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
        });

    }


    private void fetchPokemon(String name) {
        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);
        Call<Pokemon> call = apiService.getPokemon(name);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();
                    pokemonNameTextView.setText(pokemon.getName());
                    Glide.with(PokeDexActivity.this)
                            .load(pokemon.getSprites().getFrontDefault())
                            .into(pokemonImageView);
                } else {
                    Toast.makeText(PokeDexActivity.this, "Pokémon not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(PokeDexActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static Intent pokeDexIntentFactory(Context context){
        return new Intent(context, PokeDexActivity.class);
    }
}