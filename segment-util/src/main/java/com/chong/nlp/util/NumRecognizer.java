package com.chong.nlp.util;


public class NumRecognizer {
	
	private static String commonNumPattern="\\d+";
	private static String numWithPeriodPattern="\\d+.\\d+";
	private static String numWithCommaPattern="\\d{1,3},(\\d{3,3},)*\\d{3,3}";
	private static String numWithCommaPattern2="(\\d+，*)+\\d";

	
	
	public static boolean isNum(String in){
		if(in.matches(commonNumPattern))
			return true;
		if(in.matches(numWithPeriodPattern))
			return true;
		if(in.matches(numWithCommaPattern))
			return true;
		if(in.matches(numWithCommaPattern2))
			return true;
		return false;
	}
	
	
	public static void main(String[] args) {
		String in="12,4，4";
		System.out.println(NumRecognizer.isNum(in));
	}
	
}
