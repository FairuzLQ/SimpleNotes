package com.lqstudio.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailNote extends AppCompatActivity {

    EditText judulNote, detailNote;
    TextView waktuNote, tglNote;
    ImageButton simpanNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        judulNote = findViewById(R.id.detail_note_title);
        detailNote = findViewById(R.id.detail_note_desc);

        tglNote = findViewById(R.id.detail_note_dates);
        waktuNote = findViewById(R.id.detail_note_time);

        simpanNote = findViewById(R.id.detail_save_button);

        simpanNote.setOnClickListener((v)->simpanNoteDB());
    }

    void simpanNoteDB(){
        String judul, deskripsi, waktu, tanggal;
        judul = judulNote.getText().toString();
        deskripsi = detailNote.getText().toString();



        if(judul == null || judul.isEmpty()){
            judulNote.setError("Harap isi judul catatan");
            return;
        }
    }
}