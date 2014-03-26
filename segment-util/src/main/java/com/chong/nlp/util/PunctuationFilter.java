/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chong.nlp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author chong
 */
public class PunctuationFilter {

    private static final Pattern pattern = Pattern.compile("^.*?[，。：；“”？！、（）].*?$");
    private static final String PUNCTUATION = "[\\*，。：；“”？！、（）……——]";

    public static boolean containPunct(String in) {
        Matcher matcher = pattern.matcher(in);

        return matcher.find();
    }

    public static String deletePunct(String in) {
        return in.replaceAll(PUNCTUATION, " ");
    }
}
