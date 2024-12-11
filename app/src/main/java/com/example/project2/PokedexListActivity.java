package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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

        //Menu button functionality
        Button showMenuButton = findViewById(R.id.dexMenuButton);
        showMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu, passing in the current view and the menu resource
                PopupMenu popupMenu = new PopupMenu(PokedexListActivity.this, v);

                // Inflate the menu from XML resource
                popupMenu.getMenuInflater().inflate(R.menu.dex_menu, popupMenu.getMenu());

                // Set a listener for item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item click
                        if (item.getItemId() == R.id.option_1) {
                            Toast.makeText(PokedexListActivity.this, "Option 1 Selected", Toast.LENGTH_SHORT).show();
                            fetchPokemonData(PokemonNames.getLegendaryPokemon());
                            return true;
                        } else if (item.getItemId() == R.id.option_2) {
                            Toast.makeText(PokedexListActivity.this, "Option 2 Selected", Toast.LENGTH_SHORT).show();
                            fetchPokemonData(PokemonNames.getPseudoLegendaryPokemon());
                            return true;
                        } else if (item.getItemId() == R.id.option_3) {
                            Toast.makeText(PokedexListActivity.this, "Option 3 Selected", Toast.LENGTH_SHORT).show();
                            fetchPokemonData(PokemonNames.getStarterPokemon());
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                // Show the popup menu
                popupMenu.show();
            }
        });


        fetchPokemonData();
    }

    private void fetchPokemonData() {
        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);

        List<String> pokemonNames = PokemonNames.getStarterPokemon();
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

    private void fetchPokemonData(List<String> pokemonNames) {
        PokeApiService apiService = RetrofitClient.getRetrofitInstance().create(PokeApiService.class);

        List<Pokemon> fetchedPokemonList = new ArrayList<>();

        pokemonAdapter.clearData();

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
