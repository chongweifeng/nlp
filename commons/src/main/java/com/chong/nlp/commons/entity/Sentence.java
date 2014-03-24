package com.chong.nlp.commons.entity;

import java.util.List;

public class Sentence {
	private List<Word> words;
	private int offset;

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		StringBuilder sentenceBuilder = new StringBuilder();

		for (Word word : getWords()) {
			sentenceBuilder.append(word.getName()).append(" ");
		}
		return sentenceBuilder.toString()+"。";
	}

}
