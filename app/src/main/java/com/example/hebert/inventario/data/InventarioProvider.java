package com.example.hebert.inventario.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
    static final int ENDERECO = 101;
    static final int SETOR = 102;
    static final int ITEM_POR_ENDERECO = 103;
    static final int ENDERECO_POR_SETOR = 104;
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
                break;
            }
            // "weather/*"
            case ITEM_POR_ENDERECO: {
                retCursor = null;
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
        }
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        return matcher;
    }
}
