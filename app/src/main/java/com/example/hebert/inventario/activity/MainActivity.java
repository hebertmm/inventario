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
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hebert.inventario.FileInputUtility;
import com.example.hebert.inventario.FileOutputUtility;
import com.example.hebert.inventario.JsonFileUtility;
import com.example.hebert.inventario.R;
import com.example.hebert.inventario.data.DatabaseContract;
import com.example.hebert.inventario.data.ItemDAO;
import com.example.hebert.inventario.domain.Item;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.hebert.inventario.R.array.endereco;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener{

    private Button scanBtn;
    private Button saveBtn;
    private TextView formatTxt, contentTxt;
    private Spinner setorSpn, endSpn, statusSpn;
    private CheckBox chkInventariado, chkMudou;
    private Item item;
    private ItemDAO dao;

    final int EXPORT_REQUEST_CODE = 43;
    final int IMPORT_REQUEST_CODE = 44;
    final int JSON_REQUEST_CODE = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button)findViewById(R.id.scan_button);
        saveBtn = (Button)findViewById(R.id.btnSalvar);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        setorSpn = (Spinner)findViewById(R.id.spnSetor);
        endSpn = (Spinner)findViewById(R.id.spnEndereco);
        statusSpn = (Spinner)findViewById(R.id.spnEstado);
        chkInventariado = (CheckBox)findViewById(R.id.chkVerificado3);
        chkMudou = (CheckBox)findViewById(R.id.chkLocal);
        Cursor setor = getContentResolver().query(DatabaseContract.SetorPatrim.CONTENT_URI, null, null, null, null);
        setor.moveToFirst();
        Log.i("Setor",String.valueOf(setor.getCount()));
        String[] from = new String[] {DatabaseContract.SetorPatrim.COLUMN_NAME_NOMESETOR};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_dropdown_item,setor,from,to);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterSetor = ArrayAdapter.createFromResource(this,R.array.setor, android.R.layout.simple_spinner_item);
        adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setorSpn.setAdapter(sca);
        setorSpn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterEnd = ArrayAdapter.createFromResource(this, endereco, android.R.layout.simple_spinner_item);
        adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpn.setAdapter(adapterEnd);
        ArrayAdapter<CharSequence> adapterSta = ArrayAdapter.createFromResource(this,R.array.status, android.R.layout.simple_spinner_item);
        adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpn.setAdapter(adapterSta);

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
        if(v.getId() == R.id.btnSalvar){
            item = new Item();
            item.setPatrim(String.valueOf(contentTxt.getText()));
            item.setStatus((String)statusSpn.getSelectedItem());
            item.setCod_endereco(endSpn.getSelectedItemPosition());
            item.setAlteracao_local(chkMudou.isSelected());
            //dao.save(item);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.e("onActivityResult:","requestCode: "+ requestCode + " intentIntegrator: " + IntentIntegrator.REQUEST_CODE);
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                String a[] = {""};
                if(scanningResult.getContents().charAt(0) == '0')
                    a[0] = scanningResult.getContents().substring(1,7);
                else
                    a[0] = scanningResult.getContents().substring(2,8);
                Log.i("PAT", a[0]);
                Uri uri = DatabaseContract.ItemPatrim.CONTENT_URI;
                Cursor c = getContentResolver().query(uri,null,DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",a,null);
                Log.i("Cursor", String.valueOf(c.getCount()));
                contentTxt.setText(a[0]);
                if(c.moveToNext()){
                    int i = c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC);
                    formatTxt.setText(c.getString(i));
                    chkInventariado.setChecked(true);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Log.i("Current Date", mFormat.format(cal.getTime()));
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_LOCAL_INVENTARIO,this.endSpn.getSelectedItemId());
                    cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO, mFormat.format(cal.getTime()));
                    //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,fields[COL_DESC].trim());
                    //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO,3); //validar
                    //cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,patrim);
                    cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,"BOM");
                    uri = ContentUris.withAppendedId(uri, c.getLong(0));
                    Log.i("uri", uri.toString());
                    Log.i("update",String.valueOf(getContentResolver().update(uri,cv,null,null)));
                }
                else
                    Log.e("ERRO", "Item não localizado no banco de dados");
                c.close();

            } else {
                Toast.makeText(this, "Nenhum dado recebido do scanner", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == EXPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            FileOutputUtility fo = new FileOutputUtility(this.getApplicationContext());
            if(intent != null){
                uri = intent.getData();
                Log.i("AAAA: ", uri.toString());
                fo.execute(uri);

            }
        }
        if(requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            FileInputUtility fileUtility = new FileInputUtility(getApplicationContext());
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
                /*ContentResolver resolver = getBaseContext().getContentResolver();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i("->", line);
                        sb.append(line).append('\n');
                    }
                    JSONArray ja = new JSONArray(sb.toString());
                    Log.i("JSON",ja.getJSONObject(1).getString("nome"));
                    is.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
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
}
