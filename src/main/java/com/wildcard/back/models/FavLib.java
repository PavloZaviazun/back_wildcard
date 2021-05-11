package com.wildcard.back.models;

import java.util.List;

public class FavLib {
    private String name;
    private List<Integer> listOfWords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getListOfWords() {
        return listOfWords;
    }

    public void setListOfWords(List<Integer> listOfWords) {
        this.listOfWords = listOfWords;
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + '\"' +
                ", \"listOfWords\": " + listOfWords +
                '}';
    }
}
