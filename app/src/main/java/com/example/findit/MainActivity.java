package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import fragments.fragmentHome;
import fragments.fragmentAdd;
import fragments.fragmentProfile;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    fragmentHome fragmentHome = new fragmentHome();
    fragmentAdd fragmentAdd = new fragmentAdd();
    fragmentProfile fragmentProfile = new fragmentProfile();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.flipFragment, fragmentHome).commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int idItem = item.getItemId();
        if(idItem == R.id.home){
            getSupportFragmentManager().beginTransaction().replace(R.id.flipFragment, fragmentHome).commit();
            return true;
        } else if (idItem == R.id.add) {

            getSupportFragmentManager().beginTransaction().replace(R.id.flipFragment, fragmentAdd).commit();
            return true;
        } else if (idItem == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flipFragment, fragmentProfile).commit();
            return true;
        }

        return false;
    }

}