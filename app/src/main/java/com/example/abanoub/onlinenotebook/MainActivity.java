package com.example.abanoub.onlinenotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    TextView textView;
    String currentUserEmail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Note> list = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading_note));
        progressDialog.show();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        textView = (TextView) findViewById(R.id.noNotes);

        if (Utilities.isNetworkAvailable(this)) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            currentUserEmail = Utilities.getCurrentEmail();
            Log.e("onCreate: ", "online-notebook/" + currentUserEmail);
            databaseReference = firebaseDatabase.getReference().child("online-notebook/" + currentUserEmail);

            //To read data at a path and listen for changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    list = Utilities.getAllNotes(dataSnapshot);
                    progressDialog.dismiss();
                    fillRecyclerView(list);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        } else {
            progressDialog.dismiss();
            Snackbar.make(coordinatorLayout, R.string.check_internet_connection, Snackbar.LENGTH_LONG).show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddingNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
    }

    private void fillRecyclerView(ArrayList<Note> notesList) {
        if (list.size() == 0)
            textView.setVisibility(View.VISIBLE);
        else {
            textView.setVisibility(View.GONE);

            int columnCount = getResources().getInteger(R.integer.recycler_column_count);
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            Adapter adapter = new Adapter(this, notesList);
            recyclerView.setAdapter(adapter);
        }
    }
}
