/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chong.nlp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.chong.util.SortMapByValue;

/**
 * 
 * @author chong
 */
public class WordCounter {

	public static Map<String, Integer> countWord(String inString, int minLengthI) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		String[] splitedStrings = inString.split("\\s");
		for (int i = 0; i < splitedStrings.length; i++) {
			String string = splitedStrings[i];
			if (string.length() > minLengthI) {
				int countI = (resultMap.get(string) == null ? 1 : (resultMap
						.get(string) + 1));
				resultMap.put(string, countI);
			}
		}
		return resultMap;
	}

	public static Map<String, Integer> countWord(Collection<String> collection) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for (String word : collection) {
			int countI = (resultMap.get(word) == null ? 1 : (resultMap
					.get(word) + 1));
			resultMap.put(word, countI);
		}

		return resultMap;
	}

	public static void countWord(int minLengthI, String inPathString,
			String outPathString) {
		BufferedReader br = null;
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new FileReader(new File(inPathString)));
			String s;
			while ((s = br.readLine()) != null && s.length() > minLengthI) {
				String[] splitedStrings = s.split("\\s");

				for (int i = 0; i < splitedStrings.length; i++) {

					String string = splitedStrings[i];
					if (string.length() > minLengthI) {
						int countI = (resultMap.get(string) == null ? 1
								: (resultMap.get(string) + 1));
						resultMap.put(string, countI);
					}
				}
			}
			br.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(outPathString);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		Map<String, Integer> sortMap = SortMapByValue.sortByValue(resultMap);
		for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
			String string = entry.getKey();
			Integer integer = entry.getValue();
			pw.println(string + "\t" + integer);
		}
		pw.close();
	}

	public static void main(String[] args) {

		countWord(1, "data/segArticle0731", "data/wordFreq");
	}
}
