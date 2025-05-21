package com.myapp.nutrition.dao;

import java.io.IOException;
import java.util.List;

/**
 * DAO generic pentru tipuri serializabile.
 */
public interface GenericDAO<T> {
    /**
     * Încarcă toate obiectele de tip T din fișier.
     */
    List<T> loadAll() throws IOException, ClassNotFoundException;

    /**
     * Salvează toate obiectele de tip T în fișier (over-write).
     */
    void saveAll(List<T> items) throws IOException;
}
