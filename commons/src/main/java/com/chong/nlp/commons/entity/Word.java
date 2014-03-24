package com.chong.nlp.commons.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Word {
    /**
     * 词对应的字符串
     */
    private final String name;
    private int offset;
    private String pos;
    private String chunk;
    private String nerTag;
    private int freq;
    private int length;
    private Concept parentConcept;
    private List<Concept> candidateParentConcept;
    private long createTime;
    private long lastUpdateTime;
    private double score;

    public Word(String wordName) {
        this.name = wordName;
    }

    public Word(Word originWord) {
        this.name = originWord.getName();
        this.offset = originWord.getOffset();
        this.pos = originWord.getPos();
        this.chunk = originWord.getChunk();
        this.nerTag = originWord.getNerTag();
        this.freq = originWord.getFreq();
        this.length = originWord.getLength();
        this.parentConcept = originWord.getParentConcept();
        this.candidateParentConcept = originWord.getCandidateParentConcept();
    }

    public void copyProperty(Word originWord) {
        this.offset = originWord.getOffset();
        this.pos = originWord.getPos();
        this.chunk = originWord.getChunk();
        this.nerTag = originWord.getNerTag();
        this.freq = originWord.getFreq();
        this.length = originWord.getLength();
    }

    public static Word parseDictWord(String in) {
        if (in.trim().length() == 0) {
            return null;
        } else {
            String[] splittedStrings = in.split("\t");
            Word word = new Word(splittedStrings[0].trim());
            if (splittedStrings.length > 1) {
                word.setPos(splittedStrings[1].trim());
            }
            if (splittedStrings.length > 2) {
                word.setNerTag(splittedStrings[2].trim());
            }
            return word;
        }

    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return toDictString();
    }

    public String toDictString() {
        return getName() + "\t" + getPos() + "\t" + getNerTag();
    }

    public String getNerTag() {
        return nerTag;
    }

    public void setNerTag(String nerTag) {
        this.nerTag = nerTag;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Concept getParentConcept() {
        return parentConcept;
    }

    public void setParentConcept(Concept parentConcept) {
        this.parentConcept = parentConcept;
    }

    public List<Concept> getCandidateParentConcept() {
        return candidateParentConcept;
    }

    public void setCandidateParentConcept(List<Concept> candidateParentConcept) {
        this.candidateParentConcept = candidateParentConcept;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (name != null)
            result = result * 31 + name.hashCode();
        if (pos != null)
            result = result * 31 + pos.hashCode();
        if (nerTag != null)
            result = result * 31 + nerTag.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Word)) {
            return false;
        }

        Word otherWord = (Word) obj;
        return (name.equals(otherWord.name) && pos.equals(otherWord.pos) && nerTag
            .equals(otherWord.nerTag));

    }

    public static void main(String[] args) {
        Set<Word> set = new HashSet<Word>();
        Word word = new Word("chong");
        word.setPos("1");
        word.setNerTag("3");
        set.add(word);
        Word word2 = new Word("chong");
        word2.setPos("1");
        word2.setNerTag("2");
        set.add(word2);
        System.out.println(word.equals(word2));
        System.out.println(set);
    }

}
