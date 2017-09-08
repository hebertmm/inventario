package com.example.hebert.inventario;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hebert.inventario.data.DatabaseContract;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by hebert on 04/09/2017.
 */

public class FileOutputUtility extends AsyncTask<Uri, Integer, Integer> {
    private Context mContext;

    public FileOutputUtility(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        Uri uri = params[0];
        Log.i("AAAA: ", uri.toString());
        try {
            ContentResolver resolver = mContext.getContentResolver();
            Uri itens = DatabaseContract.ItemPatrim.CONTENT_URI;
            Cursor c = resolver.query(itens,null,null,null,null);
            //FileWriter fw = new FileWriter(resolver.openFileDescriptor(uri, "rw").getFileDescriptor());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(resolver.openOutputStream(uri, "rw")));
            //BufferedWriter bw = new BufferedWriter(fw,150000);
            int i = 0;
            while (c.moveToNext()){
                //String line = c.getString(3)+Utility.SEPARATOR+c.getString(4)+Utility.SEPARATOR;
                bw.write(c.getString(3)+Utility.SEPARATOR);
                bw.write(c.getString(1)+Utility.SEPARATOR);
                String desc = c.getString(2);
                if(desc.length() > 20)
                    desc = desc.substring(0,20);
                bw.write(desc+Utility.SEPARATOR);
                bw.write(c.getString(4)+Utility.SEPARATOR);
                bw.write(c.getString(5)+Utility.SEPARATOR);
                //bw.write(c.getString(6));
                bw.newLine();
                i++;
            }
            bw.close();
            c.close();
            /*Intent b = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            b.setData(uri);
            mContext.sendBroadcast(b);*/
            Log.i("File", i + " registros gravados!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
