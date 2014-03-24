package com.chong.nlp.util;

import java.util.ArrayList;
import java.util.List;

public class Biple {

    private Integer word;
    private Integer tag;

    public Biple(Integer word, Integer tag) {
        super();
        this.word = word;
        this.tag = tag;
    }

    public Biple parseBiple(String input, String wordTagSplitReg, WordCoderDecoder coder) {
        String[] split = input.split(wordTagSplitReg);
        if (split.length < 2) {
            return null;
        }
        if (split.length == 2) {
            return new Biple(coder.codeWord(split[0]), coder.codeWord(split[1]));
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < split.length; i++) {
            builder.append(split[i]);
        }
        return new Biple(coder.codeWord(split[0]), coder.codeWord(builder.toString()));
    }

    /**
     * 将字符串转化为一系列二元组
     * @param input 格式为 word+wordTagSplitReg+tag+unitSplit+word+wordTagSplitReg+tag+unitSplit
     * @param unitSplit 单元之间的分隔符，支持使用正则
     * @param wordTagSplitReg   词和标签之间的分隔符，支持使用正则表达式
     * @param coder 将字符串编码为Integer
     * @return
     */
    public List<Biple> string2Biples(String input, String unitSplit, String wordTagSplitReg,
                                     WordCoderDecoder coder) {
        String[] bipleStrings = input.split(unitSplit);
        List<Biple> result = new ArrayList<Biple>();
        for (String element : bipleStrings) {
            result.add(parseBiple(element, wordTagSplitReg, coder));
        }
        return result;
    }

    public Integer getWord() {
        return word;
    }

    public void setWord(Integer word) {
        this.word = word;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

}
