package com.example.hebert.inventario.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hebert on 27/06/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "patrim.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE " + DatabaseContract.ItemPatrim.TABLE_NAME + " (" +
                    DatabaseContract.ItemPatrim._ID + " INTEGER PRIMARY KEY, " +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM  + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_DESC  + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.ItemPatrim.COLUMN_NAME_ALTERACAO_LOCAL + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_SETOR =
            "CREATE TABLE " + DatabaseContract.SetorPatrim.TABLE_NAME + " (" +
                    DatabaseContract.SetorPatrim._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.SetorPatrim.COLUMN_NAME_CODSETOR + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.SetorPatrim.COLUMN_NAME_NOMESETOR + TEXT_TYPE + " )";
    private static final String SQL_CREATE_ENDERECO =
            "CREATE TABLE " + DatabaseContract.EnderecoPatrim.TABLE_NAME + " (" +
                    DatabaseContract.EnderecoPatrim._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_ENDERECO + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.EnderecoPatrim.COLUMN_NAME_NOME_ENDERECO + TEXT_TYPE +
                    DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_SETOR + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ITEM =
            "DROP TABLE IF EXISTS " + DatabaseContract.ItemPatrim.TABLE_NAME;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("CREATE: ",SQL_DELETE_ITEM);
        db.execSQL(SQL_CREATE_SETOR);
        db.execSQL(SQL_CREATE_ITEM);
        db.execSQL(SQL_CREATE_ENDERECO);
        Log.i(this.getClass().getSimpleName(),"chamou open");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ITEM);
        db.execSQL(SQL_CREATE_ITEM);
        Log.i("Upgrade: ",SQL_CREATE_ITEM);
    }
}
