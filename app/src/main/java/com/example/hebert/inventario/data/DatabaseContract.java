package com.example.hebert.inventario.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hebert on 27/06/2017.
 */

public final class DatabaseContract {

    private DatabaseContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.hebert.inventario";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "item";
    public static final String PATH_SETOR = "setor";
    public static final String PATH_ENDERECO = "endereco";

    public static class ItemPatrim implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_PATRIM = "patrim";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String  COLUMN_NAME_COD_ENDERECO = "cod_endereco";
        public static final String  COLUMN_NAME_STATUS = "status";
        public static final String  COLUMN_NAME_DATA_INVENTARIO = "data_inventario";
        public static final String COLUMN_NAME_LOCAL_INVENTARIO = "local_inventario";
    }
    public static class SetorPatrim implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SETOR).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETOR;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETOR;

        public static final String  TABLE_NAME = "setor";
        public static final String  COLUMN_NAME_CODSETOR = "cod_setor";
        public static final String  COLUMN_NAME_NOMESETOR = "nome_setor";
    }
    public static class EnderecoPatrim implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENDERECO).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ENDERECO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ENDERECO;

        public static final String  TABLE_NAME = "endereco";
        public static final String  COLUMN_NAME_COD_ENDERECO = "cod_endereco";
        public static final String  COLUMN_NAME_NOME_ENDERECO = "nome_endereco";
        public static final String  COLUMN_NAME_COD_SETOR = "id_setor";
    }
}
