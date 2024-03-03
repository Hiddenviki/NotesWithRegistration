package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        if (note.getTitle() != null) {

            Log.i("onBindViewHolder","titleTextView "+note.getTitle());
            holder.titleTextView.setText(note.getTitle());
        }

        if (note.getContent() != null) {
            Log.i("onBindViewHolder","contentTextView "+note.getContent());
            holder.contentTextView.setText(note.getContent());
        }

        if (note.getTimestamp() != null) {
            Log.e("onBindViewHolder","время "+Utility.timestampToString(note.getTimestamp()));
            holder.timestampTextView.setText(Utility.timestampToString(note.getTimestamp()));
        }

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.getTitle());
            intent.putExtra("content",note.getContent());
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

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView, timestampTextView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_main);
            contentTextView = itemView.findViewById(R.id.note_content_main);
            timestampTextView = itemView.findViewById(R.id.note_time_main);

        }
    }
}
