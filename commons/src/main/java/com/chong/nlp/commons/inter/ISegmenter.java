package com.chong.nlp.commons.inter;

import java.util.List;

import com.chong.nlp.commons.entity.Word;

/**
 * 分词接口
 * @author 崇伟峰
 *
 */
public interface ISegmenter {

    public List<String> segment2List(String in);

    public String segment2String(String in);

    public List<Word> segment2Word(String in);

}
