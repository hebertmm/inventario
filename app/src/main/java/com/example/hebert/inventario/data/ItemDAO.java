package com.example.hebert.inventario.data;

import com.example.hebert.inventario.domain.Item;

import java.util.List;

/**
 * Created by hebert on 04/07/2017.
 */

public interface ItemDAO {
    public void save (Item i);
    public void delete (Item i);
    public List<Item> list();
    public Item find (String patrim);
}
