package com.chong.nlp.util;

import java.util.HashMap;
import java.util.Map;

public class WordCoderDecoder {
    private Map<String, Integer> codeMap = new HashMap<String, Integer>();
    private Map<Integer, String> decodeMap = new HashMap<Integer, String>();

    public Integer codeWord(String word) {
        if (codeMap.containsKey(word)) {
            return new Integer(codeMap.get(word));
        }
        int code = codeMap.size() + 1;
        codeMap.put(word, code);
        decodeMap.put(code, word);
        return new Integer(code);
    }

    public String decodeWord(Integer word) {
        if (decodeMap.containsKey(word)) {
            return decodeMap.get(word);
        }
        return null;
    }
}
