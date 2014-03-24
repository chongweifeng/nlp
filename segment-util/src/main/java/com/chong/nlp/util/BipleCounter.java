package com.chong.nlp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BipleCounter {
    
    private Map<Integer, Map<Integer, Integer>> wordTagCountMap = new HashMap<Integer, Map<Integer, Integer>>();
    private Map<Integer, Map<Integer, Integer>> tagWordCountMap = new HashMap<Integer, Map<Integer, Integer>>();

    public BipleCounter() {
        Map<Integer, Integer> wordCount = new HashMap<Integer, Integer>();
        wordCount.put(0, 0);
        wordTagCountMap.put(0, wordCount);
        Map<Integer, Integer> tagCount = new HashMap<Integer, Integer>();
        tagCount.put(0, 0);
        tagWordCountMap.put(0, tagCount);
    }

    public void scan(List<Biple> bipleList) {
        if (bipleList == null) {
            return;
        }
        for (Biple biple : bipleList) {
            countOneBiple(biple);
        }
    }

    public void countOneBiple(Biple biple) {
        Integer word = biple.getWord();
        Integer tag = biple.getTag();
        countOneBiple(word, tag, wordTagCountMap);
        countOneBiple(tag, word, tagWordCountMap);
    }

    private void countOneBiple(Integer word, Integer tag,
                               Map<Integer, Map<Integer, Integer>> CountMap) {
        Map<Integer, Integer> oneWordTagMap = CountMap.get(word);
        if (oneWordTagMap == null) {
            oneWordTagMap = new HashMap<Integer, Integer>();
            oneWordTagMap.put(tag, 1);
            oneWordTagMap.put(0, 1);
            CountMap.put(word, oneWordTagMap);
        } else {
            Integer value = oneWordTagMap.containsKey(tag) ? (oneWordTagMap.get(tag) + 1) : 1;
            oneWordTagMap.put(tag, value);
            CountMap.get(word).put(0, CountMap.get(word).get(0) + 1);
        }

        CountMap.get(0).put(0, CountMap.get(0).get(0) + 1);

    }

    public Map<Integer, Map<Integer, Integer>> getWordTagCountMap() {
        return wordTagCountMap;
    }

    public Map<Integer, Map<Integer, Integer>> getTagWordCountMap() {
        return tagWordCountMap;
    }

}
