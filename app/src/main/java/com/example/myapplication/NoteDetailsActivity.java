package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Time;
import java.time.LocalDateTime;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleText, contentText; //элементы заметки
    ImageButton saveNoteButton;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleText = findViewById(R.id.note_title);
        contentText = findViewById(R.id.note_content);
        saveNoteButton = findViewById(R.id.save_note_btn);
        saveNoteButton.setOnClickListener(v->saveNote());
///////
        pageTitleTextView = findViewById(R.id.new_note_page_title);
        deleteNoteTextViewBtn  = findViewById(R.id.delete_note_text_view_btn);

        //тут получаю данные
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleText.setText(title);
        contentText.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteButton.setOnClickListener( (v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );


    }

    private void saveNote() {
        String noteTitle = titleText.getText().toString();
        String noteContent = contentText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty()){
            titleText.setError("Title is required");
            return;
        }

        Note newNote = new Note();
        newNote.setTitle(noteTitle);
        newNote.setContent(noteContent);
        newNote.setTimestamp(Timestamp.now());
        saveNoteToFirebase(newNote);

    }

//    private void saveNoteToFirebase(Note newNote) {
//
//        DocumentReference documentReference;
//        documentReference = Utility.getCollectionReferenceForNote().document();
//        documentReference.set(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Utility.showToast(NoteDetailsActivity.this, "the note added successfully");
//                    finish();
//                }else{
//                    Utility.showToast(NoteDetailsActivity.this, task.getException().getLocalizedMessage());
//                }
//            }
//        });
//
//    }


    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //обновить заметку
            documentReference = Utility.getCollectionReferenceForNote().document(docId);
        }else{
            //создать новую
            documentReference = Utility.getCollectionReferenceForNote().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //заметка добавилась
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding note");
                }
            }
        });

    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNote().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //nзаметка удалена
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting note");
                }
            }
        });
    }


}