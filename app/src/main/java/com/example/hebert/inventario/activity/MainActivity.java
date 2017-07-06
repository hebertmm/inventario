package com.example.hebert.inventario.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hebert.inventario.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private Spinner setorSpn, endSpn, statusSpn;
    private CheckBox chkInventariado, chkMudou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        setorSpn = (Spinner)findViewById(R.id.spnSetor);
        endSpn = (Spinner)findViewById(R.id.spnEndereco);
        statusSpn = (Spinner)findViewById(R.id.spnEstado);
        chkInventariado = (CheckBox)findViewById(R.id.chkVerificado3);
        chkMudou = (CheckBox)findViewById(R.id.chkLocal);
        ArrayAdapter<CharSequence> adapterSetor = ArrayAdapter.createFromResource(this,R.array.setor, android.R.layout.simple_spinner_item);
        adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setorSpn.setAdapter(adapterSetor);
        ArrayAdapter<CharSequence> adapterEnd = ArrayAdapter.createFromResource(this,R.array.endereco, android.R.layout.simple_spinner_item);
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

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            formatTxt.setText("Microcomputador tipo Desktop marca Lenovo, modelo M93P..."+ scanningResult.getFormatName());
            contentTxt.setText(scanningResult.getContents());
            chkInventariado.setChecked(true);

        }
        else {
            Toast.makeText(this, "Nenhum dado recebido do scanner", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
