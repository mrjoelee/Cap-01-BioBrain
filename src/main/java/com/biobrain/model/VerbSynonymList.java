package com.biobrain.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

public class VerbSynonymList {
    private List<VerbSynonym> synonyms;

    public static class VerbSynonym {
        private String word;
        private List<String> synonyms;
    }
    public static String getSynonym(String verb) {

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(VerbSynonymList.class.getClassLoader().getResourceAsStream("jsonFiles/synonyms.json")))) {
            Gson gson = new Gson();
            VerbSynonymList verbSynonymList = gson.fromJson(reader, VerbSynonymList.class);

            for (VerbSynonym verbSynonym : verbSynonymList.synonyms) {
                if (verbSynonym.word.equalsIgnoreCase(verb) || verbSynonym.synonyms.contains(verb.toLowerCase())) {
                    return verbSynonym.word;
                }
            }
            return verb;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return verb;
    }
}
