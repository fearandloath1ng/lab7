package com.daniel.server.util;

import com.daniel.common.person.Person;
import com.daniel.server.database.Database;
import com.daniel.server.database.PersonDao;

import java.util.LinkedHashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionHandler {

    private ConcurrentHashMap<Integer, Person> map;
    private final Date initDate;

    public CollectionHandler(ConcurrentHashMap<Integer, Person> map) {
        this.map = map;
        initDate = new Date();
    }

    public CollectionHandler() {
        this.map = PersonDao.getAllPersonFromDB().stream()
                .collect(Collectors.toMap(Person::getId, Function.identity()))
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (v1, v2) -> v1,
                                ConcurrentHashMap::new));
        initDate = new Date();
    }

    public void reloadCollection() {
        this.map = PersonDao.getAllPersonFromDB().stream()
                .collect(Collectors.toMap(Person::getId, Function.identity()))
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (v1, v2) -> v1,
                                ConcurrentHashMap::new));
    }

    public ConcurrentHashMap<Integer, Person> getMap() {
        return map;
    }

    public Person get(Integer key) {
        return map.get(key);
    }

    public int getNextId() {
        if (map.isEmpty()) {
            return 1;
        }
        ArrayList<Person> sorted = new ArrayList<>(map.values());
        sorted.sort(Comparator.comparing(Person::getId));
        return sorted.get(sorted.size() - 1).getId() + 1;
    }

    public Date getInitDate() {
        return initDate;
    }

    public int getSize() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Integer key) {
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    public ArrayList<Person> sortByCoordinates() {
        ArrayList<Person> sorted = new ArrayList<>(map.values());
        sorted.sort(Comparator.comparing(Person::getCoordinates));
        return sorted;
    }

    public ArrayList<Person> sortDescending() {
        ArrayList<Person> sorted = new ArrayList<>(map.values());
        sorted.sort(Collections.reverseOrder());
        return sorted;
    }

    public int removeGreater(Person p) {
        LinkedList<Integer> keys = new LinkedList<>();
        for (Map.Entry<Integer, Person> entry : map.entrySet()) {
            if (entry.getValue().compareTo(p) > 0) {
                keys.add(entry.getKey());
            }
        }
        for (Integer i : keys) {
            map.remove(i);
        }
        return keys.size();
    }

    public int removeGreater(Integer key) {
        LinkedList<Integer> keys = new LinkedList<>();
        for (Map.Entry<Integer, Person> entry : map.entrySet()) {
            if (entry.getKey() > key) {
                keys.add(entry.getKey());
            }
        }
        for (Integer i : keys) {
            map.remove(i);
        }
        return keys.size();
    }

    public void insert(Integer key, Person p) {
        map.put(key, p);
    }

    public void remove(Integer key) {
        map.remove(key);
    }

}
