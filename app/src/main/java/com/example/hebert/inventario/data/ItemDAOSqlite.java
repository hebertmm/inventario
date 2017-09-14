package com.example.hebert.inventario.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hebert.inventario.domain.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebert on 04/07/2017.
 */

public class ItemDAOSqlite implements ItemDAO {
    private SQLiteDatabase db;
    private Context context;

    public ItemDAOSqlite(Context context) {
        DatabaseOpenHelper helper = new DatabaseOpenHelper(context);
        this.db = helper.getWritableDatabase();
    }



    @Override
    public void save (Item i){
        ContentValues cv = this.objectToCv(i);
        db.insert(DatabaseContract.ItemPatrim.TABLE_NAME, null, cv);
        Log.e("save ", "Fez insert");
    }

    @Override
    public void update(Item i) {
        String[] str = new String[1];
        str[0] = String.valueOf(i.get_ID());
        ContentValues cv = this.objectToCv(i);
        db.update(DatabaseContract.ItemPatrim.TABLE_NAME,cv,DatabaseContract.ItemPatrim._ID + "=?",str);
            Log.e("save ", "Fez update");
    }

    @Override
    public void delete(Item i) {
        String[] str = new String[1];
        str[0] = String.valueOf(i.get_ID());
        if(i.get_ID() != null){
            db.delete(DatabaseContract.ItemPatrim.TABLE_NAME,DatabaseContract.ItemPatrim._ID + " = ?",str);
        }

    }

    @Override
    public List<Item> list() {
        List<Item> list = new ArrayList<>();
        Cursor c = db.query(DatabaseContract.ItemPatrim.TABLE_NAME,null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isLast()){
            //list.add
        }
        c.close();
        return null;
    }

    @Override
    public Item find(String patrim) {
        String[] args = new String[1];
        args[0] = patrim;
        Cursor c = db.query(DatabaseContract.ItemPatrim.TABLE_NAME,null,"patrim = ?",args,null,null,null);
        c.moveToFirst();
        Item i = new Item(c.getInt(c.getColumnIndex(DatabaseContract.ItemPatrim._ID)),
                                c.getString(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM)),
                                c.getString(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC)),
                                c.getInt(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO)),
                                c.getString(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS)),
                                c.getString(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO)),
                                Boolean.valueOf(c.getString(c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_LOCAL_INVENTARIO))));
        Log.e("id=: ",String.valueOf(i.getCod_endereco()));
        c.close();
        return i;
    }

    public void close() {
        this.db.close();
    }

    private ContentValues objectToCv(Item i){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ItemPatrim._ID,i.get_ID());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,i.getPatrim());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,i.getDescricao());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO,i.getCod_endereco());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,i.getStatus());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO, String.valueOf(i.getData_inventario()));
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_LOCAL_INVENTARIO,i.isAlteracao_local());
        return cv;
    }

}
