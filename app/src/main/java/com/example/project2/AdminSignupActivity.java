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

import com.example.project2.Database.DexRepository;
import com.example.project2.databinding.ActivityAdminSignupBinding;

public class AdminSignupActivity extends AppCompatActivity {

    private DexRepository repository;

    private ActivityAdminSignupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = DexRepository.getRepository(getApplication());

        binding.registerAdminButton.setOnClickListener(v -> {
            createAdminAccount();
        });

        binding.backToLoginButton.setOnClickListener(v -> {
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
        });

    }

    private void createAdminAccount() {
        String username = binding.userNameAdminSignupEditText.getText().toString();
        String password = binding.passwordAdminSignupEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username must not be blank");
            return;
        }
        if(password.isEmpty()){
            toastMaker("Password must not be blank");
            return;
        }

        repository.insertAdminUser(username, password);

        toastMaker("Admin Account Created");
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent adminSignupIntentFactory(Context context){
        return new Intent(context, AdminSignupActivity.class);
    }
}