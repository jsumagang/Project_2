package com.example.project2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokeDexActivity extends AppCompatActivity {

    private TextView pokemonNameTextView;
    private ImageView pokemonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_dex);

        pokemonNameTextView = findViewById(R.id.pokemonName);
        pokemonImageView = findViewById(R.id.pokemonImage);

        fetchPokemon("pikachu"); // Replace with a dynamic name as needed
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
}