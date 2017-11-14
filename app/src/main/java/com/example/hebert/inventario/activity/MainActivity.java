package com.example.hebert.inventario.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hebert.inventario.FileInputUtility;
import com.example.hebert.inventario.FileOutputUtility;
import com.example.hebert.inventario.JsonFileUtility;
import com.example.hebert.inventario.R;
import com.example.hebert.inventario.data.DatabaseContract;
import com.example.hebert.inventario.domain.Item;
import com.example.hebert.inventario.preferences.PreferenceActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

//import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener{

    private Button scanBtn;
    private Button findBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private TextView descricaoTxt, patrimTxt, observacaoTxt;
    private Spinner endSpn, statusSpn;
    private Item item;
    private String lastSetorId;


    final int EXPORT_REQUEST_CODE = 43;
    final int IMPORT_REQUEST_CODE = 44;
    final int JSON_REQUEST_CODE = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button)findViewById(R.id.scan_button);
        findBtn = (Button)findViewById(R.id.find_button);
        saveBtn = (Button)findViewById(R.id.btnSalvar);
        cancelBtn = (Button) findViewById(R.id.btnCancelar);
        descricaoTxt = (TextView)findViewById(R.id.scan_format);
        patrimTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        findBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        endSpn = (Spinner)findViewById(R.id.spnEndereco);
        statusSpn = (Spinner)findViewById(R.id.spnEstado);
        observacaoTxt = (TextView)findViewById(R.id.txtObservacao);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sharedPref.getString("pref_syncConnectionType","1");
        lastSetorId = id;
        String[] args = new String[] {String.valueOf(id)};
        Cursor endereco = getContentResolver().query(DatabaseContract.EnderecoPatrim.CONTENT_URI, null, DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_SETOR + "= ?", args, null);
        String[] from = new String[] {DatabaseContract.EnderecoPatrim.COLUMN_NAME_NOME_ENDERECO};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_dropdown_item,endereco,from,to,0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpn.setAdapter(sca);
        endSpn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSta = ArrayAdapter.createFromResource(this,R.array.status, android.R.layout.simple_spinner_item);
        adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpn.setAdapter(adapterSta);
        item = new Item();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String setor = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_syncConnectionType","1");
        if(!setor.equals(lastSetorId)){
            String[] args = new String[]{String.valueOf(setor)};
            Cursor endereco = getContentResolver().query(DatabaseContract.EnderecoPatrim.CONTENT_URI, null, DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_SETOR + "= ?", args, null);
            SimpleCursorAdapter sca = (SimpleCursorAdapter) endSpn.getAdapter();
            sca.swapCursor(endereco);
            endSpn.setAdapter(sca);
            lastSetorId = setor;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.setOrientationLocked(false);
            scanIntegrator.initiateScan();
        }
        if(v.getId() == R.id.btnSalvar) {
            if (item.getPatrim() != null) {
                Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
                item.setObservacao(observacaoTxt.getText().toString());
                Log.i("ID", String.valueOf(item.get_ID()));
                if (item.get_ID() != 0) {
                    uri = ContentUris.withAppendedId(uri, item.get_ID());
                    int affectedRows = getContentResolver().update(uri, item.toContentValues(), null, null);
                    Log.i("Affected Rows", String.valueOf(affectedRows));
                } else {
                    ContentValues cv = item.toContentValues();
                    cv.putNull(DatabaseContract.ItemPatrim._ID);
                    uri = getContentResolver().insert(uri,cv);
                    Log.i("inserido", cv.toString());
                }
                resetFields();
            }
            else {
                Toast.makeText(this, "Favor preencher o campo Patrim e clicar em IR, ou ler o código de barras!", Toast.LENGTH_LONG).show();
                resetFields();
            }
        }
        if(v.getId() == R.id.find_button){
            String[] a = new String[] {patrimTxt.getText().toString()};
            Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
            Cursor c = getContentResolver().query(uri,null,DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",a,null);
            ContentValues cv = new ContentValues();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");
            if(c.getCount() > 0){
                item = Item.fromCursor(c);
                descricaoTxt.setText(item.getDescricao());
                observacaoTxt.setText(item.getObservacao());
                Log.i("ID", String.valueOf(item.get_ID()));
                item.setLocalInventario(this.endSpn.getSelectedItemId());
                item.setData_inventario(mFormat.format(cal.getTime()));
                findBtn.setEnabled(false);
                scanBtn.setEnabled(false);
            }
            else {
                Toast.makeText(this,"Item não localizado no banco de dados",Toast.LENGTH_LONG).show();
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                Log.i("Vib", String.valueOf(vibrator.hasVibrator()));
                vibrator.vibrate(200);
                item.setPatrim(patrimTxt.getText().toString());
                item.setLocalInventario(this.endSpn.getSelectedItemId());
                item.setData_inventario(mFormat.format(cal.getTime()));
            }

            c.close();
        }
        if(v.getId() == R.id.btnCancelar){
            resetFields();
        }
    }



    @Override
    //@TargetApi(23)
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            String perm[] = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            requestPermissions(perm, 200);
        }
        if(item.getItemId() == R.id.menu_import){
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("text/comma-separated-values");
            try {

                startActivityForResult(fileintent, IMPORT_REQUEST_CODE);

            } catch (ActivityNotFoundException e) {

                Log.e("FILE:","No app found for importing the file.");

            }

        }
        if(item.getItemId() == R.id.menu_export){
            Intent i = new Intent(this, FileGeneratorActivity.class);
            startActivity(i);
            /*Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

            //Filtro para mostrar somente arquivos que podem ser abertos, como por exemplo
            // um arquivo texto (diferente de um contato, por exemplo).
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Cria um arquivo com o  MIME type especificado.
            intent.setType("text/comma-separated-values");
            intent.putExtra(Intent.EXTRA_TITLE, "teste.csv");
            startActivityForResult(intent, EXPORT_REQUEST_CODE);*/

        }
        if(item.getItemId() == R.id.menu_config){
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.menu_import_locais){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            try {
                startActivityForResult(intent, JSON_REQUEST_CODE);
            } catch (ActivityNotFoundException e){
                Log.e("FILE", "Não foi possível importar o arquivo.");
            }
        }
        if(item.getItemId() == R.id.app_bar_search){
            Intent intent = new Intent(this, ListViewActivity.class);
            intent.putExtra("cod_end",endSpn.getSelectedItemId());
            startActivity(intent);
        }
        if(item.getItemId() == R.id.app_bar_find){
            Intent intent = new Intent(this, FindActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                String a[] = {""};
                if(scanningResult.getContents().charAt(0) == '0')
                    a[0] = scanningResult.getContents().substring(1,7);
                else
                    a[0] = scanningResult.getContents().substring(2,8);
                patrimTxt.setText(a[0]);
                Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
                Cursor c = getContentResolver().query(uri,null,DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",a,null);
                ContentValues cv = new ContentValues();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");
                if(c.getCount() > 0){
                    item = Item.fromCursor(c);
                    descricaoTxt.setText(item.getDescricao());
                    observacaoTxt.setText(item.getObservacao());
                    item.setLocalInventario(this.endSpn.getSelectedItemId());
                    item.setData_inventario(mFormat.format(cal.getTime()));
                    item.setObservacao(observacaoTxt.getText().toString());
                    findBtn.setEnabled(false);
                    scanBtn.setEnabled(false);
                }
                else {
                    Toast.makeText(this,"Item não localizado no banco de dados",Toast.LENGTH_LONG).show();
                    item.setLocalInventario(endSpn.getSelectedItemId());
                    item.setData_inventario(mFormat.format(cal.getTime()));
                    item.setCod_endereco(0); //validar
                    item.setPatrim(a[0]);
                    item.setStatus("BOM");

                }

                c.close();

            } else {
                Toast.makeText(this, "Nenhum dado recebido do scanner", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == EXPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri;
            FileOutputUtility fo = new FileOutputUtility(this);
            if(intent != null){
                uri = intent.getData();
                Log.i("AAAA: ", uri.toString());
                fo.execute(uri);

            }
        }
        if(requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri;
            FileInputUtility fileUtility = new FileInputUtility(this);
            if(intent != null) {
                uri = intent.getData();
                fileUtility.execute(uri);

            }
        }
        if(requestCode == JSON_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String a = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
            Toast.makeText(getBaseContext(),a,Toast.LENGTH_LONG ).show();
            Uri uri;
            if(intent != null) {
                uri = intent.getData();
                Log.i("READ", uri.toString());
                JsonFileUtility jfu = new JsonFileUtility(getApplicationContext());
                jfu.execute(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void resetFields(){
        patrimTxt.setText("");
        descricaoTxt.setText("");
        observacaoTxt.setText("");
        patrimTxt.requestFocus();
        item = new Item();
        findBtn.setEnabled(true);
        scanBtn.setEnabled(true);
    }
}
