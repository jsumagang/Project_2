package com.example.project2.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.project2.Database.entities.PokemonTierList;
import com.example.project2.Database.typeConverters.Converters;
import com.example.project2.MainActivity;
import com.example.project2.Database.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, PokemonTierList.class}, version = 9, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DexDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "DexDatabase";

    private static volatile DexDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static DexDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (DexDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DexDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");

            databaseWriteExecutor.execute(() -> {
                // Populate the User table with default values
                UserDAO userDao = INSTANCE.userDAO();
                userDao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                userDao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                userDao.insert(testUser1);

                // Populate the PokemonTierList table with default values
                PokemonTierListDao tierListDao = INSTANCE.pokemonTierListDao();
                tierListDao.deleteAll();

                // Add default Pokémon tiers
                tierListDao.insertPokemonTierList(new PokemonTierList('S', List.of("nidoking", "victini")));
                tierListDao.insertPokemonTierList(new PokemonTierList('A', List.of("charizard", "slowpoke", "gengar")));
                tierListDao.insertPokemonTierList(new PokemonTierList('B', List.of("hitmonchan", "mawile", "blaziken")));
                tierListDao.insertPokemonTierList(new PokemonTierList('C', List.of("geodude", "loudred", "bunnelby")));
                tierListDao.insertPokemonTierList(new PokemonTierList('F', List.of("zubat", "diglett", "gumshoos")));
            });
        }
    };


    public abstract DexDAO dexDAO();

    public abstract PokemonTierListDao pokemonTierListDao();

    public abstract UserDAO userDAO();
}

