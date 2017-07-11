package com.example.abanoub.onlinenotebook.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.abanoub.onlinenotebook.Note;
import com.example.abanoub.onlinenotebook.R;
import com.example.abanoub.onlinenotebook.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * Created by Abanoub on 2017-06-28.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    RemoteViews remoteView;
    private ArrayList<Note> listItems = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;
    FirebaseAuth firebaseAuth;
    String currentUserEmail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (Utilities.isNetworkAvailable(context)) {
            currentUserEmail = Utilities.getCurrentEmail();

            if (currentUserEmail != null) {
                databaseReference = firebaseDatabase.getReference().child("online-notebook/" + currentUserEmail);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        listItems = Utilities.getAllNotes(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_single_item);
        remoteView.setTextViewText(R.id.title, listItems.get(position).title);
        remoteView.setTextViewText(R.id.note, listItems.get(position).note);

        Bundle extras = new Bundle();
        extras.putInt(WidgetProvider.EXTRA_ITEM, position);
        extras.putString("title", listItems.get(position).title);
        extras.putString("note", listItems.get(position).note);
        extras.putString("pushId", listItems.get(position).pushId);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.linear, fillInIntent);

        return remoteView;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
