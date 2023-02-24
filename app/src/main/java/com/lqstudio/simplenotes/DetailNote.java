package com.lqstudio.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
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
    TextView tglNote, titleDetail;
    TextClock waktuNote;
    ImageButton simpanNote;
    String waktuDatabase, currentTime;

    //VARIABEL UNTUK EDIT CATATAN
    String editJudul, editDetail, docId;

    //HAPUS CATATAN
    TextView hapusNote;

    //CEK APAKAH CATATAN BARU ATAU CATATAN EDIT, JIKA FALSE BERARTI BELUM ADA REFERENCE ID DOCUMENT
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //AMBIL JUDUL DAN ISI CATATAN
        judulNote = findViewById(R.id.detail_note_title);
        detailNote = findViewById(R.id.detail_note_desc);

        //TITLE DI ATAS
        titleDetail = findViewById(R.id.teks_add_note_title);

        //BUTTON UNTUK HAPUS NOTE
        hapusNote = findViewById(R.id.hapus_catatan);

        //AMBIL DATA CATATAN UNTUK DIEDIT
        editJudul = getIntent().getStringExtra("judul");
        editDetail = getIntent().getStringExtra("deskripsi");
        docId = getIntent().getStringExtra("docId");

        //CEK ID DOCUMENT
        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        //TAMPILIN ISI CATATAN YANG AKAN DIEDIT
        judulNote.setText(editJudul);
        detailNote.setText(editDetail);

        //APABILA USER INGIN EDIT CATATAN, GANTI TITLE DI ATAS
        if(isEditMode){
            titleDetail.setText("Edit Catatan Anda");
            hapusNote.setVisibility(View.VISIBLE);
        }

        //AMBIL TANGGAL DIBUATNYA CATATAN
        tglNote = findViewById(R.id.detail_note_dates);

        //SET WAKTU LIVE CATATAN
        waktuNote = findViewById(R.id.detail_note_time);
        waktuNote.setFormat12Hour("kk:mm");
        waktuRealTime();

        //SIMPAN CATATAN
        simpanNote = findViewById(R.id.detail_save_button);
        simpanNote.setOnClickListener((v)->simpanNoteDB());

        hapusNote.setOnClickListener((v)->hapusCatatandariFirebase());
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
        //AMBIL REFERENSI USER DARI FIREBASE
        DocumentReference documentReference;
        //CEK APAKAH USER SEDANG EDIT ATAU ENGGA
        if(isEditMode){
            //JIKA YA, MAKA AKAN DIREFERENSIKAN KE ID CATATAN USER YANG LAGI DIEDIT
            documentReference = Utility.ambilReferensiNote().document(docId);
        }else{
            //JIKA ENGGA, BIKIN CATATAN DENGAN ID CATATAN BARU
            documentReference = Utility.ambilReferensiNote().document();
        }
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

    void hapusCatatandariFirebase(){
        //AMBIL REFERENSI USER DARI FIREBASE
        DocumentReference documentReference;

        documentReference = Utility.ambilReferensiNote().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DetailNote.this, "Berhasil menghapus catatan", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(DetailNote.this, "Gagal menghapus catatan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}