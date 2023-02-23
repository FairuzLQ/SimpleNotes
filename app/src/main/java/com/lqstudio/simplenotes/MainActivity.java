package com.lqstudio.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNote;
    RecyclerView recyclerView;
    ImageButton menuHamburger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        addNote = findViewById(R.id.add_note);
        recyclerView = findViewById(R.id.notes_recycler);
        menuHamburger = findViewById(R.id.my_notes_hamburger);


        addNote.setOnClickListener((v)->startActivity(new Intent(MainActivity.this, DetailNote.class)));
        menuHamburger.setOnClickListener((v)->showMenu());
        setupRecyclerView();
    }

    void showMenu(){
        //TODO DISPLAY MENU
    }

    void setupRecyclerView(){

    }
}