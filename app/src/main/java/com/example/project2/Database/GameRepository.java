package com.example.project2.Database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

//import com.example.project2.Database.entities.GymLog;
import com.example.project2.MainActivity;
import com.example.project2.Database.entities.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GameRepository {

    //private final GameDAO gymLogDAO;

    private final UserDAO userDAO;

    //private ArrayList<GymLog> allLogs;

    private static GameRepository repository;

    private GameRepository(Application application){
        GameDatabase db = GameDatabase.getDatabase(application);
        //this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDAO();
        //this.allLogs = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
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

//    public ArrayList<GymLog> getAllLogs() {
//        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
//                new Callable<ArrayList<GymLog>>() {
//                    @Override
//                    public ArrayList<GymLog> call() throws Exception {
//                        return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
//                    }
//                }
//        );
//        try{
//            return future.get();
//        } catch(InterruptedException | ExecutionException e){
//            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
//        }
//        return null;
//    }

//    public void insertGymLog(GymLog gymLog){
//        GymLogDatabase.databaseWriteExecutor.execute(()->{
//            gymLogDAO.insert(gymLog);
//        });
//    }

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


//    public ArrayList<GymLog> getAllLogsByUserId(int userId) {
//        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
//                new Callable<ArrayList<GymLog>>() {
//                    @Override
//                    public ArrayList<GymLog> call() throws Exception {
//                        return (ArrayList<GymLog>) gymLogDAO.getRecordsByUserId(userId);
//                    }
//                }
//        );
//        try{
//            return future.get();
//        } catch(InterruptedException | ExecutionException e){
//            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
//        }
//        return null;
//    }
}



