package com.chong.nlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import com.chong.nlp.commons.entity.Word;
import com.chong.nlp.commons.inter.ISegmenter;

public abstract class AbstractSegmenter implements ISegmenter {

    public List<String> segment2List(String in) {
        List<String> wordNameList = new ArrayList<String>();
        for (Word word : segment2Word(in)) {
            wordNameList.add(word.getName());
        }
        return wordNameList;
    }

    public String segment2String(String in) {
        StringBuilder resultBuilder = new StringBuilder();
        for (Word word : segment2Word(in)) {
            resultBuilder.append(word.getName()).append(" ");
        }
        return resultBuilder.toString().trim();
    }

    public abstract List<Word> segment2Word(String in);

}
