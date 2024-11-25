package com.example.project2.Database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2.MainActivity;
import com.example.project2.Database.entities.User;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GameRepository {


    private final UserDAO userDAO;

    private static GameRepository repository;

    private GameRepository(Application application){
        GameDatabase db = GameDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static GameRepository getRepository(Application application){
        if(repository!=null){
            return repository;
        }
        Future<GameRepository> future = GameDatabase.databaseWriteExecutor.submit(
                new Callable<GameRepository>() {
                    @Override
                    public GameRepository call() throws Exception {
                        return new GameRepository(application);
                    }
                }
        );
        try{
            return future.get();
        }catch(InterruptedException | ExecutionException e){
            Log.d(MainActivity.TAG, "Problem getting GymLogRepository, thread error.");
        }
        return null;
    }


    public void insertUser(User... user){
        GameDatabase.databaseWriteExecutor.execute(()->{
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

}



