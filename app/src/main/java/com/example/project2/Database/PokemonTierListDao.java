package com.example.project2.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Update;

import com.example.project2.Database.entities.PokemonTierList;

@Dao
public interface PokemonTierListDao {

    @Query("DELETE FROM pokemon_tiers")
    void deleteAll();

    // Insert a new PokemonTierList entry
    @Insert
    void insertPokemonTierList(PokemonTierList pokemonTierList);

    // Update an existing PokemonTierList entry
    @Update
    int updatePokemonTierList(PokemonTierList pokemonTierList);

    // Delete a specific PokemonTierList entry
    @Delete
    void deletePokemonTierList(PokemonTierList pokemonTierList);

    // Get all entries in the pokemon_tiers table
    @Query("SELECT * FROM pokemon_tiers")
    List<PokemonTierList> getAllPokemonTierLists();

    // Get a specific PokemonTierList by its ID
    @Query("SELECT * FROM pokemon_tiers WHERE id = :id")
    PokemonTierList getPokemonTierListById(char id);

    // Get all PokemonTierLists for a specific tier
    @Query("SELECT * FROM pokemon_tiers WHERE tier = :tier")
    List<PokemonTierList> getPokemonTierListsByTier(char tier);

    // Clear all entries in the pokemon_tiers table
    @Query("DELETE FROM pokemon_tiers")
    void clearTable();
}
