package com.chong.nlp.commons.entity;

import java.util.List;

public class Paragraph {

    private String rawParagraph;
    private List<Sentence> sentences;
    private int offset;

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        StringBuilder paraBuilder = new StringBuilder();

        for (Sentence sentence : getSentences()) {
            paraBuilder.append(sentence);
        }
        return paraBuilder.toString() + System.getProperty("line.separator");
    }

    public String getRawParagraph() {
        return rawParagraph;
    }

    public void setRawParagraph(String rawParagraph) {
        this.rawParagraph = rawParagraph;
    }

}
