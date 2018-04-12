package com.example.abanoub.onlinenotebook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Abanoub on 2017-06-29.
 */

public class Utilities {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;

        return user;
    }

    public static String getCurrentEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;
        String currentUserEmail = user.getEmail().replace(".", "_");

        return currentUserEmail;
    }

    public static ArrayList<Note> getAllNotes(DataSnapshot dataSnapshot) {

        ArrayList<Note> list = new ArrayList<>();

        //iterate through each Msg, ignoring their UID
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                Note noteObj = new Note();
                noteObj.title = child.getValue(Note.class).title;
                noteObj.note = child.getValue(Note.class).note;
                noteObj.pushId = child.getValue(Note.class).pushId;
                list.add(noteObj);
            }
        }
        return list;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
