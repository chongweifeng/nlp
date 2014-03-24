package com.chong.nlp.segmenter.maxMatch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chong.nlp.commons.entity.SetDictionary;
import com.chong.nlp.commons.entity.Word;
import com.chong.nlp.commons.inter.ISegmenter;
import com.chong.nlp.segmenter.AbstractSegmenter;
import com.chong.nlp.util.EnglishRecognizer;
import com.chong.nlp.util.NumRecognizer;

public class MaxMatchSegmenter extends AbstractSegmenter {

    private static SetDictionary dictionary = new SetDictionary();
    private static SetDictionary regDictionary = new SetDictionary();
    static {
        try {
            dictionary.loadDict(new FileInputStream(
                "/home/chong/workspace/segment/resources/sports.dict")); //TODO 需要将其改为可配置项
            regDictionary.loadDict(new FileInputStream(
                "/home/chong/workspace/segment/resources/reg.dict")); //TODO 需要将其改为可配置项
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Word> segment2Word(String in) {
        List<Word> result = new ArrayList<Word>();
        String input = in;
        int endIndexI = input.length();
        int startIndexI = 0;
        String curString;
        while (startIndexI < endIndexI) {
            curString = input.substring(startIndexI, endIndexI);

            if (dictionary.containWord(curString)) {
                for (Word word : dictionary.getWord(curString)) {
                    Word copyWord = new Word(word);
                    copyWord.setOffset(startIndexI);
                    result.add(copyWord);
                }

                startIndexI = endIndexI;
                endIndexI = input.length();
            } else if (NumRecognizer.isNum(curString)) {
                Word rawWord = new Word(curString);
                rawWord.setOffset(startIndexI);
                rawWord.setPos("digit");
                result.add(rawWord);
                startIndexI = endIndexI;
                endIndexI = input.length();
            } else if (EnglishRecognizer.isEnglishWord(curString)) {
                Word rawWord = new Word(curString);
                rawWord.setOffset(startIndexI);
                rawWord.setPos("english");
                result.add(rawWord);
                startIndexI = endIndexI;
                endIndexI = input.length();
            } else if (startIndexI == endIndexI - 1) {
                Word rawWord = new Word(curString);
                rawWord.setOffset(startIndexI);
                rawWord.setPos("single");
                result.add(rawWord);
                startIndexI = endIndexI;
                endIndexI = input.length();
            } else {
                Set<Word> regSet = regDictionary.getRegMatchWords(curString);
                if (regSet != null) {
                    for (Word word : regSet) {
                        Word copyWord = new Word(word);
                        copyWord.setOffset(startIndexI);
                        result.add(copyWord);
                    }

                    startIndexI = endIndexI;
                    endIndexI = input.length();
                } else {

                    endIndexI--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ISegmenter segmenter = new MaxMatchSegmenter();
        List<Word> result = segmenter.segment2Word("2013亚冠nba米兰联赛:柏太阳神-贵州人和");
        for (Word word : result) {
            System.out.println(word.getOffset() + "\t" + word.toDictString());
        }

    }

}
