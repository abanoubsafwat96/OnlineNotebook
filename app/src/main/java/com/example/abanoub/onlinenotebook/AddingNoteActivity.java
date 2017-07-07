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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddingNoteActivity extends AppCompatActivity {

    EditText titleED;
    EditText noteED;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_note);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleED = (EditText) findViewById(R.id.title);
        noteED = (EditText) findViewById(R.id.note);

        titleED.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Roboto-Bold.ttf"));
        noteED.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Roboto-Regular.ttf"));

        currentUserEmail = Utilities.getCurrentEmail();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("online-notebook/" + currentUserEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adding_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (TextUtils.isEmpty(titleED.getText()) || TextUtils.isEmpty(noteED.getText()))
                    Toast.makeText(this, R.string.write_title_note_first, Toast.LENGTH_SHORT).show();
                else {
                    String pushId = databaseReference.push().getKey();
                    Log.e("onOptionsItemsel-adding", pushId);

                    Note note = new Note(titleED.getText().toString(), noteED.getText().toString(), pushId);
                    databaseReference.child(pushId).setValue(note);

                    Toast.makeText(this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddingNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
