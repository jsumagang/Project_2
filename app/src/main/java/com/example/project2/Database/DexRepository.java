package com.example.project2.Database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2.MainActivity;
import com.example.project2.Database.entities.User;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DexRepository {


    private final UserDAO userDAO;

    private static DexRepository repository;

    private DexRepository(Application application){
        DexDatabase db = DexDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static DexRepository getRepository(Application application){
        if(repository!=null){
            return repository;
        }
        Future<DexRepository> future = DexDatabase.databaseWriteExecutor.submit(
                new Callable<DexRepository>() {
                    @Override
                    public DexRepository call() throws Exception {
                        return new DexRepository(application);
                    }
                }
        );
        try{
            return future.get();
        }catch(InterruptedException | ExecutionException e){
            Log.d(MainActivity.TAG, "Problem getting Repository, thread error.");
        }
        return null;
    }


    public void insertUser(User... user){
        DexDatabase.databaseWriteExecutor.execute(()->{
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



