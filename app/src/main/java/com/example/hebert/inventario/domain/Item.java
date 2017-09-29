package com.example.hebert.inventario.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.hebert.inventario.data.DatabaseContract;

/**
 * Created by hebert on 04/07/2017.
 */

public class Item {
    private long _ID;
    private String patrim;
    private String descricao;
    private long cod_endereco;
    private String status;
    private String data_inventario;
    private long local_inventario;
    private String observacao;

    public Item(){};

    public Item(long _id, String patrim, String descricao, Integer cod_endereco, String status, String data_inventario, Integer alteracao_local) {
        this._ID = _id;
        this.patrim = patrim;
        this.descricao = descricao;
        this.cod_endereco = cod_endereco;
        this.status = status;
        this.data_inventario = data_inventario;
        this.local_inventario = alteracao_local;
    }

    public static Item fromCursor(Cursor c){
        Item item = new Item();
        if(c.moveToNext()){
            int i = c.getColumnIndex(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC);
            item.set_ID(c.getLong(0)); //set _ID
            item.setPatrim(c.getString(1));
            item.setDescricao(c.getString(i));
            item.setCod_endereco(c.getLong(3));//cod endereco
            item.setStatus(c.getString(4));//status
            item.setData_inventario(c.getString(5));
            item.setLocalInventario(c.getLong(6));
            item.setObservacao(c.getString(7));
        }
        c.close();
        return item;
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ItemPatrim._ID,this.get_ID());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_PATRIM,this.getPatrim());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DESC,this.getDescricao());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_COD_ENDERECO,this.getCod_endereco());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_STATUS,this.getStatus());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_DATA_INVENTARIO, this.getData_inventario());
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_LOCAL_INVENTARIO, this.local_inventario);
        cv.put(DatabaseContract.ItemPatrim.COLUMN_NAME_OBSERVACAO, this.observacao);
        return cv;
    }


    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getPatrim() {
        return patrim;
    }

    public void setPatrim(String patrim) {
        this.patrim = patrim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getCod_endereco() {
        return cod_endereco;
    }

    public void setCod_endereco(long cod_endereco) {
        this.cod_endereco = cod_endereco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData_inventario() {
        return data_inventario;
    }

    public void setData_inventario(String data_inventario) {
        this.data_inventario = data_inventario;
    }

   public void setLocalInventario(long alteracao_local) {
        this.local_inventario = alteracao_local;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
