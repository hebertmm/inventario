package com.example.hebert.inventario.preferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hebert.inventario.R;
import com.example.hebert.inventario.data.DatabaseContract;

public class DeleteDataActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnDeleteItens, btnDeleteLocais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        btnDeleteItens = (Button) findViewById(R.id.btnItens);
        btnDeleteItens.setOnClickListener(this);
        btnDeleteLocais = (Button) findViewById(R.id.btnExclSetor);
        btnDeleteLocais.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnItens){
            int affected = getContentResolver().delete(DatabaseContract.ItemPatrim.CONTENT_URI,null,null);
            Toast.makeText(this,affected + " itens deletados!",Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.btnExclSetor){
            getContentResolver().delete(DatabaseContract.ItemPatrim.CONTENT_URI,null,null);
            int enderecos = getContentResolver().delete(DatabaseContract.EnderecoPatrim.CONTENT_URI, null, null);
            int setores = getContentResolver().delete(DatabaseContract.SetorPatrim.CONTENT_URI,null,null);
            Toast.makeText(this,enderecos + " endereços e " + setores + " setores deletados!",Toast.LENGTH_LONG).show();
        }

    }
}
