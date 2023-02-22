package com.lqstudio.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DetailNote extends AppCompatActivity {

    EditText judulNote, detailNote;
    TextView tglNote;
    TextClock waktuNote;
    ImageButton simpanNote;
    String waktuDatabase, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        judulNote = findViewById(R.id.detail_note_title);
        detailNote = findViewById(R.id.detail_note_desc);

        tglNote = findViewById(R.id.detail_note_dates);
        waktuNote = findViewById(R.id.detail_note_time);
        waktuNote.setFormat12Hour("kk:mm");
        waktuRealTime();
        simpanNote = findViewById(R.id.detail_save_button);

        simpanNote.setOnClickListener((v)->simpanNoteDB());
    }

    void simpanNoteDB(){
        String judul, deskripsi, waktu, tanggal;
        judul = judulNote.getText().toString();
        deskripsi = detailNote.getText().toString();
        tanggal = currentTime;



        if(judul == null || judul.isEmpty()){
            judulNote.setError("Harap isi judul catatan");
            return;
        }
        Note note = new Note();
        note.setJudul(judul);
        note.setDeskripsi(deskripsi);
        note.setTanggal(tanggal);
        note.setTimestamp(Timestamp.now());

        simpanKeFirebase(note);

    }

    void simpanKeFirebase(Note note){
        DocumentReference documentReference;
        documentReference = Utility.ambilReferensiNote().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DetailNote.this, "Berhasil menyimpan catatan", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(DetailNote.this, "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void waktuRealTime(){
        String polaTgl = "EEEE, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(polaTgl);
        currentTime = simpleDateFormat.format(new Date());
        tglNote.setText(currentTime);

    }
}