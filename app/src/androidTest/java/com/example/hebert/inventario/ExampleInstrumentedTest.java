package com.example.hebert.inventario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        Item i = new Item();
        i.set_ID();
        i.setAlteracao_local(false);
        i.setCod_endereco(3);
        i.setPatrim("565656");
        i.setData_inventario("2017-07-05");
        ItemDAO dao = new ItemDAOSqlite(db);
        dao.save(i);
        Item j = dao.find("565656");
        assertTrue(i.getPatrim().equals(j.getPatrim()));
        db.close();
    }
    @Test
    public void testFind() throws  Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteDatabase db = new DatabaseOpenHelper(appContext).getWritableDatabase();
        ItemDAO dao = new ItemDAOSqlite(db);
        Item i = dao.find("123456");
        assertEquals(null,i.getPatrim(),"123456");

    }
}
