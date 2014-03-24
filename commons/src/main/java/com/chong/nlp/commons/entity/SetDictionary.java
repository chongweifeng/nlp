/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chong.nlp.commons.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chong
 */
public class SetDictionary {

    private ConcurrentHashMap<String, Set<Word>> dictionary = new ConcurrentHashMap<String, Set<Word>>();

    public void addWord(String id, Word word) {
        Set<Word> wordSenses;
        if (!dictionary.containsKey(id)) {
            wordSenses = new HashSet<Word>();
            wordSenses.add(word);
            dictionary.putIfAbsent(id, wordSenses);
        } else {
            wordSenses = dictionary.get(id);
            wordSenses.add(word);
        }
    }

    public boolean containWord(String word) {
        return dictionary.containsKey(word);
    }

    public Set<Word> getRegMatchWords(String word) {
        for (String reg : dictionary.keySet()) {
            if (word.matches(reg)) {
                Set<Word> wordSet = new HashSet<Word>();
                for (Word matchWord : dictionary.get(reg)) {
                    Word tmpWord = new Word(word);
                    tmpWord.copyProperty(matchWord);
                    wordSet.add(tmpWord);
                }
                return wordSet;
            }
        }

        return null;
    }

    public void loadDict(String dictPath) {
        BufferedReader fr;
        try {
            fr = new BufferedReader(new FileReader(dictPath));
            String s;

            while ((s = fr.readLine()) != null) {
                Word word = Word.parseDictWord(s);
                addWord(word.getName(), word);
            }

            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDict(InputStream input) {
        BufferedReader fr;
        try {
            fr = new BufferedReader(new InputStreamReader(input));
            String s;

            while ((s = fr.readLine()) != null) {
                Word word = Word.parseDictWord(s);
                addWord(word.getName(), word);
            }

            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<Word> getWord(String id) {
        return dictionary.get(id);
    }

    public static void main(String[] args) {
        SetDictionary dictionary = new SetDictionary();
        dictionary.loadDict("data/sportsEpg.dict");
        dictionary.getWord("贵州人和");
        for (Word word : dictionary.getWord("贵州人和")) {
            System.out.println(word.hashCode());
            System.out.println(word.toDictString());
        }
    }
}
