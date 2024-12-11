package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.project2.Database.PokemonTierListDao;
import com.example.project2.Database.entities.PokemonTierList;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivityPokeDexBinding;

import com.bumptech.glide.Glide;
import com.example.project2.Database.DexRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokeDexActivity extends AppCompatActivity {
    private DexRepository repository;
    private TextView pokemonNameTextView;
    private ImageView pokemonImageView;
    private ActivityPokeDexBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate binding
        binding = ActivityPokeDexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pokemonNameTextView = binding.pokemonName;
        pokemonImageView = binding.pokemonImage;


        // Access repository
        repository = DexRepository.getRepository(getApplication());
        if (repository == null) {
            Log.e("PokeDexActivity", "Repository is null!");
            Toast.makeText(this, "An error occurred while accessing the database.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Retrieve logged-in user in a background thread
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), -1);

        DexDatabase.databaseWriteExecutor.execute(() -> {
            User user = repository.getUserByUserIdSync(loggedInUserId);
            runOnUiThread(() -> {
                if (user == null) {
                    Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    toggleButtonsVisibility(user.isAdmin());
                }
            });
        });

        // Submit Pokémon search
        binding.pokedexSubmitButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();
            if (!pokemonName.isEmpty()) {
                fetchPokemon(pokemonName);
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        // Add Pokémon to favorites
        binding.favoriteItButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();
            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    User user = repository.getUserByUserIdSync(loggedInUserId);
                    if (user != null) {
                        List<String> favPokemonList = new ArrayList<>(user.getFavPokemonList());
                        if (!favPokemonList.contains(pokemonName)) {
                            favPokemonList.add(pokemonName);
                            user.setFavPokemonList(favPokemonList);
                            repository.updateUser(user);
                            runOnUiThread(() -> Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in favorites!", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        binding.STierButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();

                    // Fetch the S Tier
                    List<PokemonTierList> sTierList = tierListDao.getPokemonTierListsByTier('S');  //since getPokemonTierListsByTier is a SELECT * statement,
                    //its gonna have to return a LIST of PokemonTierList objects
                    if (!sTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
                        // Get the first entry for S Tier (I could've renamed these better :/)
                        PokemonTierList sTier = sTierList.get(0);
                        List<String> pokemonList = new ArrayList<>(sTier.getPokemonList());

                        if (!pokemonList.contains(pokemonName)) {
                            // Add Pokémon to the list
                            pokemonList.add(pokemonName);
                            sTier.setPokemonList(pokemonList);

                            // Update the S Tier in the database
                            tierListDao.updatePokemonTierList(sTier);

                            runOnUiThread(() -> Toast.makeText(this, "Added to S Tier!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in S Tier!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "S Tier not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ATierButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();

                    // Fetch the A Tier
                    List<PokemonTierList> aTierList = tierListDao.getPokemonTierListsByTier('A');  //since getPokemonTierListsByTier is a SELECT * statement,
                    //its gonna have to return a LIST of PokemonTierList objects
                    if (!aTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
                        // Get the first entry for A Tier (I could've renamed these better :/)
                        PokemonTierList aTier = aTierList.get(0);
                        List<String> pokemonList = new ArrayList<>(aTier.getPokemonList());

                        if (!pokemonList.contains(pokemonName)) {
                            // Add Pokémon to the list
                            pokemonList.add(pokemonName);
                            aTier.setPokemonList(pokemonList);

                            // Update the S Tier in the database
                            tierListDao.updatePokemonTierList(aTier);

                            runOnUiThread(() -> Toast.makeText(this, "Added to A Tier!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in A Tier!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "A Tier not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        binding.BTierButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();

                    // Fetch the B Tier
                    List<PokemonTierList> bTierList = tierListDao.getPokemonTierListsByTier('B');  //since getPokemonTierListsByTier is a SELECT * statement,
                    //its gonna have to return a LIST of PokemonTierList objects
                    if (!bTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
                        // Get the first entry for B Tier (I could've renamed these better :/)
                        PokemonTierList bTier = bTierList.get(0);
                        List<String> pokemonList = new ArrayList<>(bTier.getPokemonList());

                        if (!pokemonList.contains(pokemonName)) {
                            // Add Pokémon to the list
                            pokemonList.add(pokemonName);
                            bTier.setPokemonList(pokemonList);

                            // Update the B Tier in the database
                            tierListDao.updatePokemonTierList(bTier);

                            runOnUiThread(() -> Toast.makeText(this, "Added to B Tier!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in B Tier!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "B Tier not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        binding.CTierButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();

                    // Fetch the C Tier
                    List<PokemonTierList> cTierList = tierListDao.getPokemonTierListsByTier('C');  //since getPokemonTierListsByTier is a SELECT * statement,
                    //its gonna have to return a LIST of PokemonTierList objects
                    if (!cTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
                        // Get the first entry for C Tier (I could've renamed these better :/)
                        PokemonTierList cTier = cTierList.get(0);
                        List<String> pokemonList = new ArrayList<>(cTier.getPokemonList());

                        if (!pokemonList.contains(pokemonName)) {
                            // Add Pokémon to the list
                            pokemonList.add(pokemonName);
                            cTier.setPokemonList(pokemonList);

                            // Update the C Tier in the database
                            tierListDao.updatePokemonTierList(cTier);

                            runOnUiThread(() -> Toast.makeText(this, "Added to C Tier!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in C Tier!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "C Tier not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });

        // Added this by accident...  But could implement this in the future
//        binding.DTierButton.setOnClickListener(view -> {
//            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();
//
//            if (!pokemonName.isEmpty()) {
//                DexDatabase.databaseWriteExecutor.execute(() -> {
//                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();
//
//                    // Fetch the D Tier
//                    List<PokemonTierList> dTierList = tierListDao.getPokemonTierListsByTier('D');  //since getPokemonTierListsByTier is a SELECT * statement,
//                                                                                                    //its gonna have to return a LIST of PokemonTierList objects
//                    if (!dTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
//                        // Get the first entry for D Tier (I could've renamed these better :/)
//                        PokemonTierList dTier = dTierList.get(0);
//                        List<String> pokemonList = new ArrayList<>(dTier.getPokemonList());
//
//                        if (!pokemonList.contains(pokemonName)) {
//                            // Add Pokémon to the list
//                            pokemonList.add(pokemonName);
//                            dTier.setPokemonList(pokemonList);
//
//                            // Update the D Tier in the database
//                            tierListDao.updatePokemonTierList(dTier);
//
//                            runOnUiThread(() -> Toast.makeText(this, "Added to D Tier!", Toast.LENGTH_SHORT).show());
//                        } else {
//                            runOnUiThread(() -> Toast.makeText(this, "Already in D Tier!", Toast.LENGTH_SHORT).show());
//                        }
//                    } else {
//                        runOnUiThread(() -> Toast.makeText(this, "D Tier not found", Toast.LENGTH_SHORT).show());
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
//            }
//        });

        binding.FTierButton.setOnClickListener(view -> {
            String pokemonName = binding.pokedexUserInput.getText().toString().trim().toLowerCase();

            if (!pokemonName.isEmpty()) {
                DexDatabase.databaseWriteExecutor.execute(() -> {
                    PokemonTierListDao tierListDao = Objects.requireNonNull(DexRepository.getRepository(getApplication())).pokemonTierListDao();

                    // Fetch the F Tier
                    List<PokemonTierList> fTierList = tierListDao.getPokemonTierListsByTier('F');  //since getPokemonTierListsByTier is a SELECT * statement,
                    //its gonna have to return a LIST of PokemonTierList objects
                    if (!fTierList.isEmpty()) {                                                     //instead of a single PokemonTierList object
                        // Get the first entry for S Tier (I could've renamed these better :/)
                        PokemonTierList fTier = fTierList.get(0);
                        List<String> pokemonList = new ArrayList<>(fTier.getPokemonList());

                        if (!pokemonList.contains(pokemonName)) {
                            // Add Pokémon to the list
                            pokemonList.add(pokemonName);
                            fTier.setPokemonList(pokemonList);

                            // Update the F Tier in the database
                            tierListDao.updatePokemonTierList(fTier);

                            runOnUiThread(() -> Toast.makeText(this, "Added to F Tier!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Already in F Tier!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "F Tier not found", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show();
            }
        });


        // Navigate to Home
        binding.homePageButton.setOnClickListener(v -> startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext())));
    }

    private void toggleButtonsVisibility(boolean isAdmin) {
        int visibility = isAdmin ? View.VISIBLE : View.GONE;
        binding.STierButton.setVisibility(visibility);
        binding.ATierButton.setVisibility(visibility);
        binding.BTierButton.setVisibility(visibility);
        binding.CTierButton.setVisibility(visibility);
        //binding.DTierButton.setVisibility(visibility);
        binding.FTierButton.setVisibility(visibility);
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