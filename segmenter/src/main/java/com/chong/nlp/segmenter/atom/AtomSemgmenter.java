package com.chong.nlp.segmenter.atom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chong.nlp.commons.entity.Word;
import com.chong.nlp.segmenter.AbstractSegmenter;
import com.chong.nlp.util.EnglishRecognizer;
import com.chong.nlp.util.NumRecognizer;

public class AtomSemgmenter extends AbstractSegmenter {

    private static Pattern chinesePattern = Pattern.compile("[\u4e00-\u9fa5]");

    @Override
    public List<Word> segment2Word(String in) {
        List<Word> resultList = new ArrayList<Word>();

        int index = 0;
        String oneChar;
        while (index < in.length()) {
            oneChar = in.substring(index, index + 1);
            Word word = null;
            if (oneChar.matches("[\u4e00-\u9fa5]")) {
                word = new Word(oneChar);
                word.setOffset(index);
                word.setPos("Chinese");
                resultList.add(word);
            } else {
                for (int i = findNextChinese(in, index); i > index; i--) {
                    String nonChinese = in.substring(index, i);
                    if (isNonChineseAtomWord(nonChinese)) {
                        word = new Word(nonChinese);
                        word.setOffset(index);
                        word.setPos("EngNum");
                        resultList.add(word);
                        index = i - 1;
                        break;
                    }

                    if (i == index + 1) {
                        word = new Word(nonChinese);
                        word.setOffset(index);
                        word.setPos("EngNum");
                        resultList.add(word);
                    }
                }
            }
            index++;

        }

        return resultList;
    }

    public static int findNextChinese(String in, int start) {
        Matcher matcher = chinesePattern.matcher(in);
        int index = -1;
        if (start < in.length() && matcher.find(start)) {
            index = matcher.start();
        }
        return index;
    }

    @SuppressWarnings("unused")
    private boolean isAtomWord(String in) {
        return in.matches("\u4e00-\u9fa5") || NumRecognizer.isNum(in)
               || EnglishRecognizer.isEnglishWord(in);
    }

    private boolean isNonChineseAtomWord(String in) {
        return NumRecognizer.isNum(in) || EnglishRecognizer.isEnglishWord(in);
    }

    public static void main(String[] args) {
        String in = "ab@c崇af2.6g伟峰>";
        AtomSemgmenter aSemgmenter = new AtomSemgmenter();
        System.out.println("segment to List : " + aSemgmenter.segment2List(in));
        System.out.println("segment to String : " + aSemgmenter.segment2String(in));

        for (Word word : aSemgmenter.segment2Word(in)) {
            System.out.println(word.getName() + ":" + word.getPos() + ":" + word.getOffset());
        }
    }

}
