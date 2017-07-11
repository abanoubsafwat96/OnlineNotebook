package com.example.abanoub.onlinenotebook.localData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Abanoub on 2017-07-11.
 */

public class NotesProvider extends ContentProvider {

    Helper helper;
    static final String AUTHORITY = "com.example.abanoub.onlinenotebook.localData";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_NOTES = Helper.NOTES_TABLE_NAME;
    //    static final String URL = "content://" + AUTHORITY + "/" + PATH_NOTES;
    public static final Uri CONTENT_URL = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

    static final int NOTES = 100;
    static final int NOTES_WITH_ID = 101;

    static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_NOTES, NOTES);
        uriMatcher.addURI(AUTHORITY, PATH_NOTES + "/#", NOTES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        helper = new Helper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case NOTES:
                returnCursor = db.query(Helper.NOTES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Set a notification URI on the Cursor and return that Cursor
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id;
        Uri returnUri;
        Long noteId;

        switch (match) {
            case NOTES:
                id = db.insert(Helper.NOTES_TABLE_NAME, null, values);
                if (id > 0) {
                    noteId = Long.parseLong(values.getAsString(Helper.id));
                    returnUri = ContentUris.withAppendedId(CONTENT_URL, noteId);
                    String finalResult = returnUri.toString() + " " + id;
                    returnUri = Uri.parse(finalResult);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTES_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                count = db.delete(Helper.NOTES_TABLE_NAME, "id=?", new String[]{movieId});
                if (count > 0) {
                } else {
                    throw new android.database.SQLException("Failed to delete row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
