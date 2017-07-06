package com.example.hebert.inventario.data;

import com.example.hebert.inventario.domain.Item;

import java.util.List;

/**
 * Created by hebert on 04/07/2017.
 */

public interface ItemDAO {
    void save (Item i);
    void update(Item i);
    void delete (Item i);
    List<Item> list();
    Item find (String patrim);
}
