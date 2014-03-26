/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlp.ngram;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author chong
 */
public class SentenceGenerator {

    private NGramCounter ngc;

    public SentenceGenerator(NGramCounter ngc) {
        this.ngc = ngc;
    }

    public String generateSentence(int nI) {
        StringBuilder sb = new StringBuilder();

        if (nI == 1) {
            String s;
            while (!"</s>".equals(s = generateString(ngc.getnGramProb().get(1)))) {
                sb.append(s).append(" ");
            }
            return sb.delete(nI, nI).toString();
        } else {

            String preString = "";
            String nextWordString;
            int startI = 0;
            while (!(nextWordString = nextWord(preString, nI, ngc.getnGramProb().get(nI))).endsWith("</s>")) {
//                System.out.println(nextWordString);
                sb.append(nextWordString);
                startI = sb.indexOf(" ", startI + 1);
                preString = sb.substring(startI + 1);
//                System.out.println("preString is "+ preString);
            }
            sb.append(nextWordString);
        }
        return sb.toString();
    }

    private String generateString(Map<String, Double> probMap) {

        Random random = new Random();
        double randNum = random.nextDouble();
        double cumProbD = 0;
        for (Map.Entry<String, Double> object : probMap.entrySet()) {
            cumProbD += object.getValue();
            if (cumProbD > randNum) {
                return object.getKey();
            }
        }
        return "";
    }

    private String nextWord(String preString, int nI, Map<String, Double> probMap) {

        if (preString.trim().length() == 0) {
            preString = "<s>";
        }
        Map<String, Double> tempMap = new HashMap<String, Double>();
        double cumProb = 0;
        for (Map.Entry<String, Double> entry : probMap.entrySet()) {
            String string = entry.getKey();
            Double double1 = entry.getValue();
            if (string.startsWith(preString+" ")) {
                tempMap.put(string, double1);
                cumProb += double1;
            }
        }
        if (cumProb > 0.9999999 && cumProb != 0) {
            for (Map.Entry<String, Double> entry : tempMap.entrySet()) {
                String string = entry.getKey();
                Double double1 = entry.getValue();
                tempMap.put(string, double1 / cumProb);
            }
        }
//        System.out.println(tempMap);
        String nextNGramString = generateString(tempMap);
        if (!"<s>".equals(preString)) {
            String s = nextNGramString.trim();
//            System.out.println("next n-gram is "+s);
            return s.substring(nextNGramString.lastIndexOf(" "));
        } else {
            return nextNGramString.trim();
        }

    }

    public static void main(String[] args) {
        SentenceGenerator sg = new SentenceGenerator(null);
        Map<String, Double> probMap = new TreeMap<String, Double>();
        probMap.put("a", 0.2);
        probMap.put("b", 0.5);
        probMap.put("c", 0.3);

        int aCountI = 0, bCountI = 0, cCountI = 0;


        for (int i = 0; i < 10000; i++) {
            String string = sg.generateString(probMap);
            if (string == "a") {
                aCountI++;
            }
            if (string == "b") {
                bCountI++;
            }
            if (string == "c") {
                cCountI++;
            }
        }
        System.out.println(aCountI);
        System.out.println(bCountI);
        System.out.println(cCountI);

    }
}
