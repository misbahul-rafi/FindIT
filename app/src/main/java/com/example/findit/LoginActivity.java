package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import fragments.fragmentLogin;
import fragments.fragmentSignin;

public class LoginActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clickLogin(null);
    }
    public void FragmentTransaction (Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flipFragmentLogin, fragment, null)
                .setReorderingAllowed(true)
                .commit();
    }
    public void clickLogin(View view){
        fragmentLogin login = new fragmentLogin();
        FragmentTransaction(login);
    }
    public void clickSignin(View view){
        fragmentSignin signin = new fragmentSignin();
        FragmentTransaction(signin);
    }
}