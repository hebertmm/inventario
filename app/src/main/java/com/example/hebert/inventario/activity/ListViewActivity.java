package com.example.hebert.inventario.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.hebert.inventario.data.DatabaseContract;

/**
 * Created by hebert on 29/09/2017.
 */

public class ListViewActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    SimpleCursorAdapter mAdapter;
    private long cod_end;
    static final String[] PROJECTION = new String[] {DatabaseContract.ItemPatrim._ID,
            DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,DatabaseContract.ItemPatrim.COLUMN_NAME_DESC};

    static final String SELECTION = DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO + " = ?";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        cod_end = getIntent().getLongExtra("cod_end",0);
        Log.i("extra",String.valueOf(cod_end));
        String[] fromColumns = {DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,DatabaseContract.ItemPatrim.COLUMN_NAME_DESC};
        int[] toViews = {android.R.id.text1, android.R.id.text2}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DatabaseContract.ItemPatrim.CONTENT_URI,
                PROJECTION, SELECTION, new String[] {String.valueOf(cod_end)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this,"Click!",Toast.LENGTH_SHORT).show();
        super.onListItemClick(l, v, position, id);
    }
}
