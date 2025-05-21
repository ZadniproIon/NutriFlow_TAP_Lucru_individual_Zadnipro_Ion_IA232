package com.myapp.nutrition.dao;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementare GenericDAO care folosește serializare Java (ObjectStream).
 */
public class FileGenericDAO<T extends Serializable> implements GenericDAO<T> {
    private final Path filePath;

    /**
     * @param filePath calea către fișierul .dat
     */
    public FileGenericDAO(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<T> loadAll() throws IOException, ClassNotFoundException {
        if (Files.notExists(filePath)) {
            // dacă nu există fișierul, returnăm listă goală
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            @SuppressWarnings("unchecked")
            List<T> items = (List<T>) in.readObject();
            return items;
        }
    }

    @Override
    public void saveAll(List<T> items) throws IOException {
        // creăm directoarele dacă nu există
        Files.createDirectories(filePath.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            out.writeObject(items);
        }
    }
}
