package com.example.hebert.inventario.data;

import com.example.hebert.inventario.domain.Item;

import java.io.File;
import java.util.List;

/**
 * Created by hebert on 04/07/2017.
 */

public class ItemDaoCsv implements ItemDAO {

    private File csv_file;

    @Override
    public void save(Item i) {

    }

    @Override
    public void update(Item i) {

    }

    @Override
    public void delete(Item i) {

    }

    @Override
    public List<Item> list() {
        return null;
    }

    @Override
    public Item find(String patrim) {
        return null;
    }
}
