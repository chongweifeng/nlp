/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlp.ngram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.chong.nlp.util.PunctuationFilter;
import com.chong.util.SortMapByValue;

/**
 *
 * @author chong
 */
public class NGramCounter implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3354240878091784786L;
    private int nGram = 3; //n元语法
    private int totalItemI = 0;
    private Map<Integer, Map<String, Integer>> nGramCount; //n元语法计数map
    private Map<Integer, Map<String, Double>> nGramProb; //n元预测概率map
    private Map<String, Double> nGramProbEstimate;

    public NGramCounter() {
        this.nGramCount = new HashMap<Integer, Map<String, Integer>>();
        nGramProb = new HashMap<Integer, Map<String, Double>>();
        nGramProbEstimate = new HashMap<String, Double>();
        for (int i = nGram; i > 0; i--) {
            nGramCount.put(i, new HashMap<String, Integer>());
            nGramProb.put(i, new HashMap<String, Double>());
        }
    }

    public NGramCounter(int nGram) {
        this.nGram = nGram;
        this.nGramCount = new HashMap<Integer, Map<String, Integer>>();
        nGramProb = new HashMap<Integer, Map<String, Double>>();
        nGramProbEstimate = new HashMap<String, Double>();
        for (int i = this.nGram; i > 0; i--) {
            nGramCount.put(i, new HashMap<String, Integer>());
            nGramProb.put(i, new HashMap<String, Double>());
        }
    }

    private void countItem() {
        setTotalItemI(0);
        for (int value : nGramCount.get(1).values()) {
            setTotalItemI(getTotalItemI() + value);

        }
        setTotalItemI(getTotalItemI() - nGramCount.get(1).get("<s>"));
    }

    public int countVocabulNum(String inputString) {
        Set<Character> vocabulSet = new HashSet<Character>();
        for (char sC : inputString.toCharArray()) {
            vocabulSet.add(sC);
        }
        return vocabulSet.size();
    }

    public void countNGram(String sentence, int nI, String splitString) {
        StringBuilder sentenceBuilder = new StringBuilder();
        sentenceBuilder.append(sentence.trim());

        sentenceBuilder.insert(0, "<s> ");
        sentenceBuilder.append(" ").append("</s>");
        //        System.out.println(sentenceBuilder.toString());

        String[] splitedStrings = sentenceBuilder.toString().split(splitString);
        for (int i = 0; i < splitedStrings.length - nI + 1; i++) {
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < nI; j++) {
                sb.append(splitedStrings[i + j]).append(" ");
            }
            sb.deleteCharAt(sb.lastIndexOf(" "));
            String string = sb.toString();
            if (string.length() >= nI) {

                if (PunctuationFilter.containPunct(string)) {
                    continue;
                }
                if (nGramCount.get(nI).containsKey(string)) {
                    nGramCount.get(nI).put(string, nGramCount.get(nI).get(string) + 1);
                } else {
                    nGramCount.get(nI).put(string, 1);
                }
            }
        }
    }

    public void countNGram(String input, String splitString) {
        for (int i = 1; i <= getnGram(); i++) {
            countNGram(input, i, splitString);
        }
        countItem();
    }

    public void countNGram(String inPathString, String outPathString, int nI, String splitString) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(inPathString));
            String lineString;
            while ((lineString = br.readLine()) != null) {
                countNGram(lineString, splitString);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NGramCounter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NGramCounter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void probNGram(int nI) {
        if (nI > 1) {
            for (Map.Entry<String, Integer> entry : nGramCount.get(nI).entrySet()) {
                String string = entry.getKey();
                System.out.println("string is: " + string);
                double probD;
                if (string.startsWith("<s>")) {
                    probD = (0.0 + entry.getValue()) / nGramCount.get(1).get("</s>");
                } else {
                    probD = (0.0 + entry.getValue())
                            / nGramCount.get(nI - 1).get(
                                string.substring(0, string.lastIndexOf(" ")));
                }
                System.out.println(string + " prob is: " + probD);
                nGramProb.get(nI).put(string, probD);
            }
        } else {
            for (Map.Entry<String, Integer> entry : nGramCount.get(1).entrySet()) {
                String string = entry.getKey();
                if (!"<s>".equals(string)) {
                    double probD = (0.0 + entry.getValue()) / getTotalItemI();
                    nGramProb.get(nI).put(string, probD);
                }
            }
        }
    }

    public void probNGram() {
        for (int i = getnGram(); i > 0; i--) {
            System.out.println(i);
            probNGram(i);
        }
        estimateProbNGram();
    }

    public void estimateProbNGram() {
        for (String s : nGramProb.get(nGram).keySet()) {
            nGramProbEstimate.put(s, estimateProbNGram(s));
        }
    }

    public double estimateProbNGram(String inputString) {
        String[] splitedSentenceStrings = inputString.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        double[] probDs = new double[nGram];
        for (int k = 0; k < probDs.length; k++) {
            probDs[k] = 0.0;

        }

        /**
         * 对于序列w1w2w3,分别提取P(w3)、P(w3|w2)、P(w3|w1w2)
         * 如果P(w3)为0，则用n元模型中的词数的倒数作为概率估计
         */
        for (int k = nGram; k > 0; k--) {
            sb.insert(0, splitedSentenceStrings[k - 1]);
            double kGramProb = getnGramProb().get(nGram - k + 1).get(sb.toString().trim()) == null ? 0
                : getnGramProb().get(nGram - k + 1).get(sb.toString().trim());
            if (kGramProb == 0) {
                probDs[getnGram() - k] = 1.0 / Math.pow(getTotalItemI(), getnGram() - k + 1);
                sb.insert(0, " ");

                continue;
            }
            probDs[getnGram() - k] = kGramProb;
            sb.insert(0, " ");
        }
        double sumProb = 0;

        /**
         * 对概率插值求
         */
        for (int k = 0; k < getnGram(); k++) {
            //                System.out.println(probDs[k]);
            sumProb += (getnGram() + 0.0) / Math.pow(2, k) * probDs[k];
        }
        return sumProb;
    }

    public Map<Integer, Integer> countFreq(Map<String, Integer> nGraMap, int vTotalNum) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

        int totalCountI = 0;

        for (Map.Entry<String, Integer> entry : nGraMap.entrySet()) {
            Integer integer = entry.getValue();
            totalCountI += integer;
            if (resultMap.get(integer) == null) {
                resultMap.put(integer, 1);
            } else {
                resultMap.put(integer, (resultMap.get(integer) + 1));
            }

        }

        resultMap.put(0, vTotalNum * vTotalNum - totalCountI);

        return resultMap;
    }

    public Map<Integer, Double> GoodTuringSmoothing(Map<Integer, Integer> freqCounterMap) {
        Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Integer> entry : freqCounterMap.entrySet()) {
            Integer integer = entry.getKey();
            Integer integer2 = entry.getValue();
            if (freqCounterMap.get(integer + 1) != null) {
                double smoothedCountD = (integer + 1.0) * freqCounterMap.get(integer + 1)
                                        / integer2;
                resultMap.put(integer, smoothedCountD);
            } else {
                resultMap.put(integer, integer2 + 0.0);
            }
        }

        return resultMap;

    }

    public static void main(String[] args) {
        NGramCounter ngc = new NGramCounter();
        //        System.out.println(NGramCounter.countNGram("崇伟峰是个好孩子好孩子", 1, " "));
        //
        //        System.out.println(NGramCounter.countNGram("崇伟峰是个好孩子好孩子", 2, " "));
        //        System.out.println(NGramCounter.countNGram("崇伟峰是个好孩子好孩子", 3, " "));
        //        String inpString = "这个中国第一产煤大县，近年因推行全民免费医疗而享誉全国。它对于“名分”的渴求，也只是中国上百个县城的一角缩影。据南方周末记者根据公开的官方信息统计，自1997年开始，包括神木，陕西省就有21个县提出撤县设市(区)。如果放之全国，至少有138个县(地区、盟)明确提出改头换面的设想。而近两年，挟城镇化之名义，这场热潮被媒体形容如“不断升温的高压锅”，汹涌而来。只需简单地搜索，就可看到不少地方正在编制规划、成立课题组、上报省级，渲染气氛";
        //        System.out.println(NGramCounter.probNGram(inpString, 2));
        //        System.out.println(SortMapByValue.sortByValue(NGramCounter.countNGram(inpString, 1, " ")).size());
        //
        //        Map<Integer, Integer> freqCounterMap = ngc.countFreq(NGramCounter.countNGram(inpString, 2, " "), 149);
        //        System.out.println(freqCounterMap);
        //        System.out.println(ngc.GoodTuringSmoothing(freqCounterMap));
        //        System.out.println(ngc.countVocabulNum(inpString));
        ngc.countNGram("/opt/data/icwb2-data/training/test.utf8",
            "/opt/data/icwb2-data/training/testOut.utf8", 2, " ");
        System.out.println(SortMapByValue.sortByValue(ngc.getnGramCount().get(1)));
        //            System.out.println(SortMapByValue.sortByValue(NGramCounter.probNGram(2,ngc.countNGram("/opt/data/icwb2-data/training/test.utf8", "/opt/data/icwb2-data/training/testOut.utf8", 1),ngc.countNGram("/opt/data/icwb2-data/training/test.utf8", "/opt/data/icwb2-data/training/testOut.utf8", 2))));

    }

    /**
     * @return the nGramCountMap
     */
    public Map<Integer, Map<String, Integer>> getnGramCount() {
        return nGramCount;
    }

    /**
     * @return the nGramProbMap
     */
    public Map<Integer, Map<String, Double>> getnGramProb() {
        return nGramProb;
    }

    /**
     * @param nGramProbMap the nGramProbMap to set
     */
    public void setnGramProbMap(Map<Integer, Map<String, Double>> nGramProbMap) {
        this.nGramProb = nGramProbMap;
    }

    /**
     * @return the totalItemI
     */
    public int getTotalItemI() {
        return totalItemI;
    }

    /**
     * @param totalItemI the totalItemI to set
     */
    public void setTotalItemI(int totalItemI) {
        this.totalItemI = totalItemI;
    }

    /**
     * @return the nGram
     */
    public int getnGram() {
        return nGram;
    }

    /**
     * @param nGram the nGram to set
     */
    public void setnGram(int nGram) {
        this.nGram = nGram;
    }

    /**
     * @return the nGramProbEstimate
     */
    public Map<String, Double> getnGramProbEstimate() {
        return nGramProbEstimate;
    }

    /**
     * @param nGramProbEstimate the nGramProbEstimate to set
     */
    public void setnGramProbEstimate(Map<String, Double> nGramProbEstimate) {
        this.nGramProbEstimate = nGramProbEstimate;
    }
}
