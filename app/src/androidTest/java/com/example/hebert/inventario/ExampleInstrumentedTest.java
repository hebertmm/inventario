package com.example.hebert.inventario;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.hebert.inventario.data.DatabaseContract;
import com.example.hebert.inventario.data.DatabaseOpenHelper;
import com.example.hebert.inventario.data.ItemDAO;
import com.example.hebert.inventario.data.ItemDAOSqlite;
import com.example.hebert.inventario.domain.Item;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.hebert.inventario", appContext.getPackageName());
    }
    @Test
    public void testDbConnection() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        Item i = new Item();
        assertTrue(db.isOpen());
        db.close();
    }
    @Test
    public void testInsert() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        //SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        //Log.i("path",db.getPath());
        ContentResolver resolver = appContext.getContentResolver();
        Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_ALTERACAO_LOCAL,false);
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO, "2017-07-05");
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,"Teste de Descrição 2222");
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO,3);
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,"565656");
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,"OCIOSO");
        resolver.insert(uri, cv);
        Item a = new Item();
        /*a.set_ID();
        a.setAlteracao_local(false);
        a.setDescricao("Teste de descrição 2");
        a.setCod_endereco(3);
        a.setPatrim("123456");
        a.setData_inventario("2017-07-05");
        a.setStatus("BOM222");
        ItemDAO dao = new ItemDAOSqlite(appContext);
        dao.save(a);
        Item i = new Item();
        i.set_ID();
        i.setAlteracao_local(false);
        i.setDescricao("Teste de descrição");
        i.setCod_endereco(3);
        i.setPatrim("565656");
        i.setData_inventario("2017-07-05");
        i.setStatus("BOM");
        //ItemDAO dao = new ItemDAOSqlite(db);
        dao.save(i);
        Item j = dao.find("565656");*/
        assertTrue(true);
        //db.close();
    }
    @Test
    public void testSelectAll() throws Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        //Log.i("path",db.getPath());
        Cursor c = db.rawQuery("select * from " + DatabaseContract.SetorPatrim.TABLE_NAME,null);
        c.moveToFirst();
        for(int i = 0;i < c.getCount();i++){
            Log.i("Select",c.getColumnName(0)+"|"+c.getColumnName(1)+"|"+c.getColumnName(2)+"|");//+c.getColumnName(3)+"|"+c.getColumnName(4)+"|"+c.getColumnName(5)+"|"+c.getColumnName(6)+"|");
            Log.i("Select",c.getString(0)+"|"+c.getString(1)+"|"+c.getString(2)+"|");//+c.getString(3)+"|"+c.getString(4)+"|"+c.getString(5)+"|"+c.getString(6)+"|");
            c.moveToNext();
        }
        c.close();
        db.close();
    }
    @Test
    public void testUpdate() throws Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        //SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        ItemDAO dao = new ItemDAOSqlite(appContext);
        Item i = dao.find("565656");
        Log.e("item id",String.valueOf(i.get_ID()));
        i.setCod_endereco(5);
        dao.update(i);
        Item j = new Item();
        j = dao.find("565656");
        Log.i("update", String.valueOf(i.getCod_endereco())+" - "+String.valueOf(j.getCod_endereco()));
        assertTrue(i.getCod_endereco()==j.getCod_endereco());
    }
    @Test
    public void testFind() throws  Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        //SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        ItemDAO dao = new ItemDAOSqlite(appContext);
        Item i = dao.find("565656");
        Log.i("Find",String.valueOf(i.get_ID()));
        assertEquals(null,i.getPatrim(),"565656");

    }
}
