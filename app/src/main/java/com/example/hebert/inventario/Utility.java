package com.example.hebert.inventario;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hebert.inventario.data.DatabaseContract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hebert on 30/08/2017.
 */

public class Utility extends AsyncTask<Uri, Integer, Integer>{
    //constantes para colunas do arquivo csv
    public static final String SEPARATOR = ";";
    public static final int NUM_COL = 5;
    public static final int COL_SETOR = 0;
    public static final int COL_ENDERECO = 1;
    public static final int COL_PATRIM = 2;
    public static final int COL_DESC = 3;
    public static final int COL_SITUACAO_FISICA = 4;
    private Context mContext;


    public Utility(Context mContext) {
        this.mContext = mContext;
    }

    //Converte uma linha, lida do arquivo csv, para um ContentValue para inserir no banco
    public ContentValues lineToCv(String line){
        String[] fields = line.split(SEPARATOR,NUM_COL);
        Uri db = DatabaseContract.ItemPatrim.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_ALTERACAO_LOCAL,false);
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO, "");
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,fields[COL_DESC].trim());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO,3); //validar
        String patrim = fields[COL_PATRIM].substring(0,7).trim();
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,patrim);
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,fields[COL_SITUACAO_FISICA].trim());
        return cv;
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        Log.i("READ", params.toString());
        ContentResolver resolver = mContext.getContentResolver();
        try {
            InputStream is;
            is = resolver.openInputStream(params[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                Log.i("->",line);
                Uri db = DatabaseContract.ItemPatrim.CONTENT_URI;
                ContentValues cv = lineToCv(line);
                Log.i("INSERT", String.valueOf(resolver.insert(db, cv)));

            }

            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
