package com.example.hebert.inventario;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hebert.inventario.data.DatabaseContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hebert on 08/09/2017.
 */

public class JsonFileUtility extends AsyncTask<Uri, Integer, Integer> {

    private Context mContext;

    public JsonFileUtility(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        Uri uri = params[0];
        Uri setor = DatabaseContract.SetorPatrim.CONTENT_URI;
        Uri endereco = DatabaseContract.EnderecoPatrim.CONTENT_URI;
        //Log.i("READ", uri.toString());
        ContentResolver resolver = mContext.getContentResolver();
        try {
            InputStream is = resolver.openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.i("->", line);
                sb.append(line).append('\n');
            }
            JSONArray ja = new JSONArray(sb.toString());
            for(int i = 0;i < ja.length();i++){
                JSONObject object = ja.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseContract.SetorPatrim.COLUMN_NAME_CODSETOR,object.getString("codigo"));
                cv.put(DatabaseContract.SetorPatrim.COLUMN_NAME_NOMESETOR,object.getString("nome"));
                Uri last = resolver.insert(setor,cv);
                JSONArray enderecos = object.getJSONArray("enderecos");
                Log.i("ID",last.getLastPathSegment());
                for(int j = 0;j < enderecos.length();j++){
                    JSONObject o = enderecos.getJSONObject(j);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_ENDERECO, o.getString("codigo"));
                    contentValues.put(DatabaseContract.EnderecoPatrim.COLUMN_NAME_NOME_ENDERECO, o.getString("nome"));
                    contentValues.put(DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_SETOR, ContentUris.parseId(last));
                    Log.i("Insert", resolver.insert(endereco,contentValues).toString());
                }
            }
            //Log.i("JSON");
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
