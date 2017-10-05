package com.example.hebert.inventario.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hebert.inventario.R;
import com.example.hebert.inventario.data.DatabaseContract;

public class FindActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnBuscar;
    private TextView txtPatrim, txtDesc, txtEndCadastro, txtStatus, txtDataInv, txtEnderecoInv, txtObs;
    private EditText txtBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        txtBusca = (EditText) findViewById(R.id.txtBusca);
        txtPatrim = (TextView) findViewById(R.id.txtPatrim);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtEndCadastro = (TextView) findViewById(R.id.txtEndCadastro);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtDataInv = (TextView) findViewById(R.id.txtDataInventario);
        txtEnderecoInv = (TextView) findViewById(R.id.txtLocalInventario);
        txtObs = (TextView) findViewById(R.id.txtObservacao);
        Intent intent = getIntent();
        if(intent.hasExtra("patrim")){
            txtBusca.setText(intent.getStringExtra("patrim"));
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBuscar){
            Uri item = DatabaseContract.ItemPatrim.CONTENT_URI;
            Uri endereco = DatabaseContract.EnderecoPatrim.CONTENT_URI;
            Cursor i = getContentResolver().query(item, null,
                    DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM + " = ?",
                    new String[] {txtBusca.getText().toString()},null);
            while (i.moveToNext()){
                Uri endCadastro = ContentUris.withAppendedId(endereco, i.getLong(3));
                Cursor e = getContentResolver().query(endCadastro,null,null,null,null);
                if(e.moveToNext()){
                    txtEndCadastro.setText(e.getString(2));
                }
                Uri endInventario = ContentUris.withAppendedId(endereco, i.getLong(6));
                e = getContentResolver().query(endInventario,null,null,null,null);
                if(e.moveToNext()){
                    txtEnderecoInv.setText(e.getString(2));
                }
                txtPatrim.setText(i.getString(1));
                txtDesc.setText(i.getString(2));
                txtStatus.setText(i.getString(4));
                txtDataInv.setText(i.getString(5));
                txtObs.setText(i.getString(7));
                e.close();
            }
            i.close();

        }
    }
}
