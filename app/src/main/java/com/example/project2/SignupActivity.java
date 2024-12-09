package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.project2.Database.DexRepository;
import com.example.project2.Database.entities.User;
import com.example.project2.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {


    private DexRepository repository;

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = DexRepository.getRepository(getApplication());

        binding.registerButton.setOnClickListener(v -> {
            createAccount();
        });

        binding.backToLoginButton.setOnClickListener(v -> {
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
        });

    }

    private void createAccount() {
        String username = binding.userNameSignupEditText.getText().toString();
        String password = binding.passwordSignupEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username must not be blank");
            return;
        }
        if(password.isEmpty()){
            toastMaker("Password must not be blank");
            return;
        }

        repository.insertUser(username, password);

        toastMaker("Account Created");

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent signupIntentFactory(Context context){
        return new Intent(context, SignupActivity.class);
    }

}