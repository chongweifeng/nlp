package com.chong.nlp.commons.entity;

import java.util.List;

public class Article {

	private String title;
	
	private String url;

	private int nChar;
	private int nSentence;
	private int nParagraph;
	private String rawContent;
	private List<Paragraph> paragraphs;
	private List<Word> keyWords;
	private String summary;
	private List<Word> namedEntityWords;
	private List<Word> dateEntityWords;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getnChar() {
		return nChar;
	}

	public void setnChar(int nChar) {
		this.nChar = nChar;
	}

	public int getnSentence() {
		return nSentence;
	}

	public void setnSentence(int nSentence) {
		this.nSentence = nSentence;
	}

	public int getnParagraph() {
		return nParagraph;
	}

	public void setnParagraph(int nParagraph) {
		this.nParagraph = nParagraph;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	public List<Word> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<Word> keyWords) {
		this.keyWords = keyWords;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Word> getNamedEntityWords() {
		return namedEntityWords;
	}

	public void setNamedEntityWords(List<Word> namedEntityWords) {
		this.namedEntityWords = namedEntityWords;
	}

	public List<Word> getDateEntityWords() {
		return dateEntityWords;
	}

	public void setDateEntityWords(List<Word> dateEntityWords) {
		this.dateEntityWords = dateEntityWords;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@Override
	public String toString() {
		StringBuilder articleBuilder = new StringBuilder();

		for (Paragraph paragraph : getParagraphs()) {
			articleBuilder.append(paragraph).append("</br>");
		}
		return articleBuilder.toString().trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
