package com.example.hebert.inventario.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.hebert.inventario.R.array.endereco;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener{

    private Button scanBtn;
    private Button findBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private TextView descricaoTxt, patrimTxt, observacaoTxt;
    private Spinner setorSpn, endSpn, statusSpn;
    //private CheckBox chkInventariado, chkMudou;
    private Item item;


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
        setorSpn = (Spinner)findViewById(R.id.spnSetor);
        endSpn = (Spinner)findViewById(R.id.spnEndereco);
        statusSpn = (Spinner)findViewById(R.id.spnEstado);
        observacaoTxt = (TextView)findViewById(R.id.txtObservacao);
        Cursor setor = getContentResolver().query(DatabaseContract.SetorPatrim.CONTENT_URI, null, null, null, null);
        setor.moveToFirst();
        Log.i("Setor",String.valueOf(setor.getCount()));
        String[] from = new String[] {DatabaseContract.SetorPatrim.COLUMN_NAME_NOMESETOR};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_dropdown_item,setor,from,to);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter<CharSequence> adapterSetor = ArrayAdapter.createFromResource(this,R.array.setor, android.R.layout.simple_spinner_item);
        //adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setorSpn.setAdapter(sca);
        setorSpn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterEnd = ArrayAdapter.createFromResource(this, endereco, android.R.layout.simple_spinner_item);
        adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpn.setAdapter(adapterEnd);
        ArrayAdapter<CharSequence> adapterSta = ArrayAdapter.createFromResource(this,R.array.status, android.R.layout.simple_spinner_item);
        adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpn.setAdapter(adapterSta);
        item = new Item();
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
            IntentIntegrator scantIntegrator = new IntentIntegrator(this);
            scantIntegrator.initiateScan();
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
                }//falta zerar o objeto item para preparar para nova leitura
                resetFields();
            }
            else
                Toast.makeText(this,"Favor preencher o campo Patrim e clicar em IR, ou ler o código de barras!", Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.find_button){
            //Toast.makeText(this,"teste",Toast.LENGTH_SHORT).show();
            String[] a = new String[] {patrimTxt.getText().toString()};
            Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
            Cursor c = getContentResolver().query(uri,null,DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",a,null);
            Log.i("Cursor", String.valueOf(c.getCount()));
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
                //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,"BOM");
                /*cv = item.toContentValues();
                uri = ContentUris.withAppendedId(uri, item.get_ID());
                Log.i("uri", uri.toString());
                Log.i("update",String.valueOf(getContentResolver().update(uri,cv,null,null)));*/
            }
            else {
                Toast.makeText(this,"Item não localizado no banco de dados",Toast.LENGTH_LONG).show();
                item.setPatrim(patrimTxt.getText().toString());
                item.setLocalInventario(this.endSpn.getSelectedItemId());
                item.setData_inventario(mFormat.format(cal.getTime()));
                //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,fields[COL_DESC].trim());
            }

            c.close();
        }
        if(v.getId() == R.id.btnCancelar){
            resetFields();
        }
    }

    @Override
    @TargetApi(23)
    public boolean onOptionsItemSelected(MenuItem item) {
        String perm[] = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};

        requestPermissions(perm, 200);
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

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

            // Filter to only show results that can be "opened", such as
            // a file (as opposed to a list of contacts or timezones).
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Create a file with the requested MIME type.
            intent.setType("text/comma-separated-values");
            intent.putExtra(Intent.EXTRA_TITLE, "teste.csv");
            startActivityForResult(intent, EXPORT_REQUEST_CODE);

        }
        if(item.getItemId() == R.id.menu_config){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            try {
                startActivityForResult(intent, JSON_REQUEST_CODE);
            } catch (ActivityNotFoundException e){
                Log.e("FILE", "No app found for import the file.");
            }
        }
        if(item.getItemId() == R.id.app_bar_search){
            Intent intent = new Intent(this, ListViewActivity.class);
            intent.putExtra("cod_end",endSpn.getSelectedItemId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Log.e("onActivityResult:","requestCode: "+ requestCode + " intentIntegrator: " + IntentIntegrator.REQUEST_CODE);
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                String a[] = {""};
                if(scanningResult.getContents().charAt(0) == '0')
                    a[0] = scanningResult.getContents().substring(1,7);
                else
                    a[0] = scanningResult.getContents().substring(2,8);
                //Log.i("PAT", a[0]);
                patrimTxt.setText(a[0]);
                Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
                Cursor c = getContentResolver().query(uri,null,DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",a,null);
                Log.i("Cursor", String.valueOf(c.getCount()));
                ContentValues cv = new ContentValues();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");
                if(c.getCount() > 0){
                    item = Item.fromCursor(c);
                    descricaoTxt.setText(item.getDescricao());
                    observacaoTxt.setText(item.getObservacao());
                    //Log.i("ID", String.valueOf(i.get_ID()));
                    item.setLocalInventario(this.endSpn.getSelectedItemId());
                    item.setData_inventario(mFormat.format(cal.getTime()));
                    item.setObservacao(observacaoTxt.getText().toString());
                    //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,"BOM");
                    /*cv = i.toContentValues();
                    uri = ContentUris.withAppendedId(uri, i.get_ID());
                    Log.i("uri", uri.toString());
                    Log.i("update",String.valueOf(getContentResolver().update(uri,cv,null,null)));//zerar  objeto item para gravar novo item
                    item = new Item();*/
                }
                else {
                    Toast.makeText(this,"Item não localizado no banco de dados",Toast.LENGTH_LONG).show();
                    //Item i = new Item();
                    item.setLocalInventario(endSpn.getSelectedItemId());
                    item.setData_inventario(mFormat.format(cal.getTime()));
                    //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,fields[COL_DESC].trim());
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
            Uri uri = null;
            FileOutputUtility fo = new FileOutputUtility(this);
            if(intent != null){
                uri = intent.getData();
                Log.i("AAAA: ", uri.toString());
                fo.execute(uri);

            }
        }
        if(requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            FileInputUtility fileUtility = new FileInputUtility(this);
            if(intent != null) {
                uri = intent.getData();
                fileUtility.execute(uri);

            }
        }
        if(requestCode == JSON_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String a = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
            Toast.makeText(getBaseContext(),a,Toast.LENGTH_LONG ).show();
            Uri uri = null;
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
        //Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
        String[] args = new String[] {String.valueOf(id)};
        Cursor endereco = getContentResolver().query(DatabaseContract.EnderecoPatrim.CONTENT_URI, null, DatabaseContract.EnderecoPatrim.COLUMN_NAME_COD_SETOR + "= ?", args, null);
        endereco.moveToFirst();
        Log.i("Endereco",String.valueOf(endereco.getCount()));
        String[] from = new String[] {DatabaseContract.EnderecoPatrim.COLUMN_NAME_NOME_ENDERECO};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_dropdown_item,endereco,from,to);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.endSpn.setAdapter(sca);
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
    }
}
