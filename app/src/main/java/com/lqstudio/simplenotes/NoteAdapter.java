package com.lqstudio.simplenotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.judulView.setText(note.judul);
        holder.deskripsiView.setText(note.deskripsi);
        holder.waktuView.setText(note.tanggal);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,DetailNote.class);
            intent.putExtra("judul",note.judul);
            intent.putExtra("deskripsi",note.deskripsi);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView judulView, deskripsiView, waktuView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            judulView = itemView.findViewById(R.id.recycler_judul);
            deskripsiView = itemView.findViewById(R.id.recycler_deskripsi);
            waktuView = itemView.findViewById(R.id.recycler_waktu);

        }
    }
}
