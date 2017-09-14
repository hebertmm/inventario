package com.example.hebert.inventario.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hebert on 22/08/2017.
 */

public class InventarioProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseOpenHelper dbOpenHelper;

    static final int ITEM = 100;
    static final int ITEM_COM_ID = 101;
    static final int ENDERECO = 200;
    static final int SETOR = 300;
    static final int ITEM_POR_ENDERECO = 102;
    static final int ENDERECO_POR_SETOR = 201;
    @Override
    public boolean onCreate() {
        dbOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case ITEM:
            {
                retCursor = dbOpenHelper.getReadableDatabase().query(
                        DatabaseContract.ItemPatrim.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                //Log.i("Query",selection + selectionArgs[0]);
                break;
            }
            // "weather/*"
            case ITEM_COM_ID: {
                retCursor = dbOpenHelper.getReadableDatabase().query(
                        DatabaseContract.ItemPatrim.TABLE_NAME,
                        projection,
                        DatabaseContract.ItemPatrim._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        null
                );
                //Log.i("Query",selection + selectionArgs[0]);
                break;
            }
            // "weather"
            case ENDERECO: {
                retCursor = dbOpenHelper.getReadableDatabase().query(
                        DatabaseContract.EnderecoPatrim.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case SETOR: {
                retCursor = dbOpenHelper.getReadableDatabase().query(
                        DatabaseContract.SetorPatrim.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ENDERECO_POR_SETOR: {
                retCursor = null;
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEM:
                return DatabaseContract.ItemPatrim.CONTENT_TYPE;
            case ENDERECO:
                return DatabaseContract.EnderecoPatrim.CONTENT_TYPE;
            case SETOR:
                return DatabaseContract.SetorPatrim.CONTENT_TYPE;
            case ITEM_COM_ID:
                return DatabaseContract.ItemPatrim.CONTENT_ITEM_TYPE;
        }
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ITEM: {
                long _id = db.insert(DatabaseContract.ItemPatrim.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContentUris.withAppendedId(DatabaseContract.ItemPatrim.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ENDERECO: {
                long _id = db.insert(DatabaseContract.EnderecoPatrim.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContentUris.withAppendedId(DatabaseContract.EnderecoPatrim.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SETOR: {
                long _id = db.insert(DatabaseContract.SetorPatrim.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContentUris.withAppendedId(DatabaseContract.SetorPatrim.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int  affectedRows = 0;

        switch (match) {
            case ITEM: {
                affectedRows = db.update(DatabaseContract.ItemPatrim.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case ITEM_COM_ID: {
                affectedRows = db.update(DatabaseContract.ItemPatrim.TABLE_NAME,
                        values,DatabaseContract.ItemPatrim._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case ENDERECO: {
                long _id = db.insert(DatabaseContract.EnderecoPatrim.TABLE_NAME, null, values);
                if ( _id > 0 )
                    ContentUris.withAppendedId(DatabaseContract.EnderecoPatrim.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SETOR: {
                long _id = db.insert(DatabaseContract.SetorPatrim.TABLE_NAME, null, values);
                if ( _id > 0 )
                    ContentUris.withAppendedId(DatabaseContract.SetorPatrim.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DatabaseContract.PATH_ITEM, ITEM);
        matcher.addURI(authority,DatabaseContract.PATH_ITEM + "/#",ITEM_COM_ID);
        matcher.addURI(authority, DatabaseContract.PATH_ENDERECO, ENDERECO);
        matcher.addURI(authority, DatabaseContract.PATH_SETOR, SETOR);
       return matcher;
    }
}
