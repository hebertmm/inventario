package com.example.hebert.inventario.domain;

/**
 * Created by hebert on 04/07/2017.
 */

public class Item {
    private Integer _ID;
    private String patrim;
    private String descricao;
    //private Integer cod_setor;
    private Integer cod_endereco;
    private String status;
    private String data_inventario;
    private boolean alteracao_local;

    public Item(){};

    public Item(String patrim, String descricao, Integer cod_endereco, String status, String data_inventario, boolean alteracao_local) {
        this.patrim = patrim;
        this.descricao = descricao;
        this.cod_endereco = cod_endereco;
        this.status = status;
        this.data_inventario = data_inventario;
        this.alteracao_local = alteracao_local;
    }

    public Integer get_ID() {
        return _ID;
    }

    public void set_ID() {
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

    public Integer getCod_endereco() {
        return cod_endereco;
    }

    public void setCod_endereco(Integer cod_endereco) {
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

    public boolean isAlteracao_local() {
        return alteracao_local;
    }

    public void setAlteracao_local(boolean alteracao_local) {
        this.alteracao_local = alteracao_local;
    }
}
