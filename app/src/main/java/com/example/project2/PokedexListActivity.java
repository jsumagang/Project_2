package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.adapters.PokemonAdapter;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexListActivity extends AppCompatActivity {

    private RecyclerView pokedexRecyclerView;
    private PokemonAdapter pokemonAdapter;
    private static final String TAG = "PokedexListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_list);

        pokedexRecyclerView = findViewById(R.id.pokedexRecyclerView);
        pokedexRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pokemonAdapter = new PokemonAdapter(this, new ArrayList<>()); // Empty list initially
        pokedexRecyclerView.setAdapter(pokemonAdapter);

        fetchPokemonData();
    }

    private void fetchPokemonData() {
        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);

        List<String> pokemonNames = PokemonNames.getPokemonNames(); // Use your static list
        List<Pokemon> fetchedPokemonList = new ArrayList<>();

        for (String name : pokemonNames) {
            Call<Pokemon> call = apiService.getPokemon(name);

            call.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        fetchedPokemonList.add(response.body());

                        // Update the RecyclerView when a Pokémon is fetched
                        runOnUiThread(() -> pokemonAdapter.updateData(fetchedPokemonList));
                    } else {
                        Log.e(TAG, "Failed to fetch Pokémon: " + name);
                    }
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    Log.e(TAG, "API call failed", t);
                    Toast.makeText(PokedexListActivity.this, "Error fetching Pokémon data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static Intent pokeDexListIntentFactory(Context context){
        return new Intent(context, PokedexListActivity.class);
    }
}
