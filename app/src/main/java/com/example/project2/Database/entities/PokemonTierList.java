package com.example.project2.Database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.project2.Database.typeConverters.Converters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "pokemon_tiers")
@TypeConverters({Converters.class})
public class PokemonTierList {

    @PrimaryKey(autoGenerate = true)
    private char id; // Auto-generated ID for each entry

    private List<String> pokemonList;

    private char tier;

    // Constructor
    public PokemonTierList(char tier, List<String> pokemonList) {
        this.tier = tier;
        this.pokemonList = pokemonList;
    }

    // Getters and Setters

    public char getTier() {
        return tier;
    }

    public void setTier(char tier) {
        this.tier = tier;
    }


    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public List<String> getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(List<String> pokemonList) {
        this.pokemonList = pokemonList;
    }
}

