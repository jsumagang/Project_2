package com.example.project2.Database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_pokemon")
public class PokemonTierList {

    @PrimaryKey(autoGenerate = true)
    private int id; // Auto-generated ID for each entry

    private String pokemonName; // Name of the favorite Pokémon

    // Constructor
    public PokemonTierList(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }
}

