package com.example.project2.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PokemonTierList {

    // Insert a Pokémon into the database
    @Insert
    void insert(com.example.project2.Database.entities.PokemonTierList favoritePokemon);

    // Retrieve all favorite Pokémon
    @Query("SELECT * FROM PokemonTierList")
    List<com.example.project2.Database.entities.PokemonTierList> getAllFavorites();

    // Delete a specific Pokémon by name
    @Query("DELETE FROM PokemonTierList WHERE pokemonName = :name")
    void deleteByName(String name);
}
