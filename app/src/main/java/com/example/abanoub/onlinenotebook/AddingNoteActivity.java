package com.example.abanoub.onlinenotebook;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import android.widget.EditText;
import android.widget.Toast;
import com.example.abanoub.onlinenotebook.widget.WidgetProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddingNoteActivity extends AppCompatActivity {

    @BindView(R.id.title)
    EditText titleED;
    @BindView(R.id.note)
    EditText noteED;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_note);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

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
                    Intent intent2 = new Intent(this, WidgetProvider.class);
                    intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(intent2);

                    String pushId = databaseReference.push().getKey();

                    Note note = new Note(titleED.getText().toString(), noteED.getText().toString(), pushId);
                    databaseReference.child(pushId).setValue(note);

                    if (Utilities.isNetworkAvailable(this))
                    Toast.makeText(this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, R.string.successfully_added_connect_to_save, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(AddingNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
