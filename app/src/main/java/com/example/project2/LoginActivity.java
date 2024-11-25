package com.example.project2;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2.Database.GameRepository;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivityLoginBinding;
import com.example.project2.databinding.ActivityMainBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private GameRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = GameRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameLogInEditText.getText().toString();


        if(username.isEmpty()){
            toastMaker("Username must not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user != null){
                String password = binding.passwordLogInEditText.getText().toString();
                if(password.equals(user.getPassword())){
//                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
//                    sharedPrefEditor.putInt(MainActivity.SHARED_PREFERENCE_USERID_KEY,user.getId());
//                    sharedPrefEditor.apply();
                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId()));

                }else{
                    toastMaker("Invalid Password!");
                    binding.passwordLogInEditText.setSelection(0);
                }
            }else{
                toastMaker(String.format("%s is not a valid username", username));
                binding.userNameLogInEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class);
    }
}