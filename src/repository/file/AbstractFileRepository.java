package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractFileRepository<Long, E extends Entity<Long>> extends InMemoryRepository<Long, E> {

    protected abstract E lineToEntity(String line);

    protected abstract String entityToLine(E entity);

    private final String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }

    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity doesn't exist");
        }
        List<E> entities = loadFromFile();
        entities.add(entity);
        saveToFile(entities);
        loadFromFile();
        return null;
    }


    @Override
    public E delete(Long id) {
        List<E> entities = loadFromFile();
        E e = findOne(id);
        if (e == null) {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }
        entities.remove(e);
        saveToFile(entities);
        loadFromFile();
        return null;
    }


    @Override
    public E findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("this id doesn't exist");
        }
        for (E entity : loadFromFile()) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<E> findAll() {
        return loadFromFile();
    }

    private List<E> loadFromFile() {
        List<E> entities = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    entities.add(lineToEntity(line));
                } catch (RuntimeException ignored) {
                    System.out.println(ignored.getMessage());
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return entities;
    }

    private void saveToFile(List<E> entities) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName, false))) {
            for (E entity : entities) {

                bufferedWriter.write(entityToLine(entity));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
