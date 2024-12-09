package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.databinding.ActivityLoginBinding;
import com.example.project2.databinding.ActivitySighupBinding;
import com.google.android.material.button.MaterialButton;

public class signup extends AppCompatActivity {


    private ActivitySighupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sighup);

        binding = ActivitySighupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText username = (EditText) findViewById(R.id.userNameSignupEditText);

        @SuppressLint("WrongViewCast") MaterialButton regbtn = (MaterialButton) findViewById(R.id.loginButton);
// admin and admin
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1 = username.getText().toString();
                Toast.makeText(signup.this, "Username is" + username1, Toast.LENGTH_SHORT).show();

            }
        });

        /*
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(signup.this,MainActivity.class));
            finish();
        }
    },1000);
*/
    }










}