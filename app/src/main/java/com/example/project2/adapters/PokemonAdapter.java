package com.example.project2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.project2.Pokemon;
import com.example.project2.R;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private Context context;
    private List<Pokemon> pokemonList;

    public PokemonAdapter(Context context, List<Pokemon> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        holder.pokemonName.setText(pokemon.getName());
        holder.pokemonId.setText(String.format("#%03d", pokemon.getId()));

        Glide.with(context)
                .load(pokemon.getSprites().getFrontDefault())
                .placeholder(R.drawable.ic_launcher_background) // Replace with your placeholder drawable
                .into(holder.pokemonSprite);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void updateData(List<Pokemon> newPokemonList) {
        pokemonList.clear();
        pokemonList.addAll(newPokemonList);
        notifyDataSetChanged();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName, pokemonId;
        ImageView pokemonSprite;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonId = itemView.findViewById(R.id.pokemonId);
            pokemonSprite = itemView.findViewById(R.id.pokemonSprite);
        }
    }
}
