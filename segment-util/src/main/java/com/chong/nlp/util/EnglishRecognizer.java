package com.chong.nlp.util;

public class EnglishRecognizer {
	public static boolean isEnglishWord(String in ){
		return in.matches("[a-zA-Z]+");
	}
	
	public static void main(String[] args) {
		String in="word";
		System.out.println(EnglishRecognizer.isEnglishWord(in));
	}
}
