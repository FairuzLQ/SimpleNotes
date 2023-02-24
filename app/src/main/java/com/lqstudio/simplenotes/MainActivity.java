package com.lqstudio.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNote;
    RecyclerView recyclerView;
    ImageButton menuHamburger;
    NoteAdapter noteAdapter;

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
        //BUAT MENU POPUP
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuHamburger);
        popupMenu.getMenu().add("Keluar");
        //MUNCULIN MENU
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //USER MENEKAN TOMBOL KELUAR
                if(menuItem.getTitle()=="Keluar"){
                    //LOGOUT DARI APP
                    FirebaseAuth.getInstance().signOut();
                    //PINDAH KE LOGIN
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView(){
        //QUERY UNTUK AMBIL CATATAN NOTE BERDASARKAN URUTAN TANGGAL
        Query query = Utility.ambilReferensiNote().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}