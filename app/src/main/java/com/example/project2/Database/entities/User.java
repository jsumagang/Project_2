package com.example.project2.Database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.project2.Database.DexDatabase;
import com.example.project2.Database.typeConverters.Converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = DexDatabase.USER_TABLE)
@TypeConverters({Converters.class})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;

    private String password;

    private boolean isAdmin;

    private List<String> favPokemonList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
        this.favPokemonList = new ArrayList<>(); // Initialize the list
    }

    public List<String> getFavPokemonList() {
        return favPokemonList;
    }

    public void setFavPokemonList(List<String> favPokemonList) {
        this.favPokemonList = favPokemonList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
