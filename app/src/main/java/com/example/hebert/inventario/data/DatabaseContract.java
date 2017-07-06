package com.example.hebert.inventario.data;

import android.provider.BaseColumns;

/**
 * Created by hebert on 27/06/2017.
 */

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class ItemPatrim implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_PATRIM = "patrim";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String COLUMN_NAME_COD_SETOR = "cod_setor";
        public static final String  COLUMN_NAME_COD_ENDERECO = "cod_endereco";
        public static final String  COLUMN_NAME_STATUS = "status";
        public static final String  COLUMN_NAME_DATA_INVENTARIO = "data_inventario";
        public static final String  COLUMN_NAME_ALTERACAO_LOCAL = "alteracao_local";
    }
    public static class SetorPatrim implements BaseColumns {
        public static final String  TABLE_NAME = "setor";
        public static final String  COLUMN_NAME_CODSETOR = "cod_setor";
        public static final String  COLUMN_NAME_NOMESETOR = "nome_setor";
    }
    public static class EnderecoPatrim implements BaseColumns {
        public static final String  TABLE_NAME = "endereco";
        public static final String  COLUMN_NAME_COD_ENDERECO = "cod_endereco";
        public static final String  COLUMN_NAME_NOME_ENDERECO = "nome_endereco";
        public static final String  COLUMN_NAME_COD_SETOR = "cod_setor";
    }
}
