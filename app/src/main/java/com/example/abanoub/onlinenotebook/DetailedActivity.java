package com.example.abanoub.onlinenotebook;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {

    EditText titleED;
    EditText noteED;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentUserEmail;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        note = (Note) getIntent().getSerializableExtra("note");

        titleED = (EditText) findViewById(R.id.title);
        noteED = (EditText) findViewById(R.id.note);

        titleED.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Roboto-Bold.ttf"));
        noteED.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Roboto-Regular.ttf"));

        titleED.setText(note.title);
        noteED.setText(note.note);

        currentUserEmail = Utilities.getCurrentEmail();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("online-notebook/" + currentUserEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (TextUtils.isEmpty(titleED.getText()) || TextUtils.isEmpty(noteED.getText()))
                    Toast.makeText(this, R.string.write_title_note_first, Toast.LENGTH_SHORT).show();
                else {
                    String key = note.pushId;
                    Note note2 = new Note(titleED.getText().toString(), noteED.getText().toString(), note.pushId);
                    Map<String, Object> postValues = note2.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, postValues);
                    databaseReference.updateChildren(childUpdates);
                    Toast.makeText(DetailedActivity.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return true;

            case R.id.delete:

                databaseReference.child(note.pushId).setValue(null);
                Toast.makeText(this, R.string.successfully_deleted, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
