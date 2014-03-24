/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chong.nlp.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author chong
 */
public class HMM {

    private int nStateI;
    private int nObservationI;
    private double[][] transition;
    private double[][] emission;
    private double[] startProbs;
    private double[] endProbs;

    /**
     * 构造特定隐藏和观察状态数量的HMM
     * @param nStateI    有对应观测值的隐藏状态数量
     * @param nObservationI
     */
    public HMM(int nStateI, int nObservationI) {
        this.nStateI = nStateI;
        this.nObservationI = nObservationI;
        transition = new double[nStateI][nStateI];
        emission = new double[nStateI][nObservationI];
        for (int i = 0; i < transition.length; i++) {
            Arrays.fill(transition[i], 0.0);
        }
        for (int i = 0; i < emission.length; i++) {
            Arrays.fill(emission[i], 0.0);
        }
    }

    /**
     * 随机化初始该HMM，保证其满足各种约束条件，概率值从0-1均匀分布中选取然后归一化
     */
    public void randomInit() {
        for (int i = 0; i < nStateI; i++) {
            transition[i] = randomProb(nStateI);
            startProbs = randomProb(nStateI);
            endProbs = randomProb(nStateI);
            emission[i] = randomProb(nObservationI);
        }
    }

    public double[] randomProb(int length) {
        double[] prob = new double[length];
        Random random = new Random();
        double sum = 0.0;
        double tmp = 0.0;
        for (int i = 0; i < length; i++) {
            tmp = random.nextDouble();
            sum += tmp;
            prob[i] = tmp;
        }

        if (sum != 0) {
            for (int i = 0; i < length; i++) {
                prob[i] /= sum;
            }
        }
        return prob;

    }

    /**
     * 计算到当前观察值并且处于某一特定隐藏状态时的似然值
     * @param originStateP  该观察之前所有观察值在前一步隐藏状态取各种可能状态的似然值向量
     * @param stateIndex    特定的隐藏状态
     * @param observationIndex  当前观察值
     * @return
     */
    public double forwardOneStepOneState(double[] originStateP, int stateIndex, int observationIndex) {
        double result = 0.0;
        for (int i = 0; i < originStateP.length; i++) {
            result += originStateP[i] * transition[i][stateIndex]
                      * emission[stateIndex][observationIndex];
        }
        return result;
    }

    /**
     * 计算到当前观察值并且处于各个隐藏状态时的似然值
     * @param originStateP
     * @param observationIndex
     * @return
     */
    public double[] forwardOneStep(double[] originStateP, int observationIndex) {
        double[] result = new double[nStateI];
        for (int i = 0; i < result.length; i++) {
            result[i] = forwardOneStepOneState(originStateP, i, observationIndex);
        }
        return result;
    }

    /**
     * 计算给定观察序列的似然值
     * @param observation   观察序列
     * @return  观察序列的似然值
     */
    public double likelihood(int[] observation) {

        double likelihood = 0.0;
        double[] forwardArray = new double[nStateI];
        /*计算第一个观测值的似然*/
        for (int i = 0; i < nStateI; i++) {
            forwardArray[i] = startProbs[i] * emission[i][observation[0]];
        }
        /*向前推进*/
        for (int i = 1; i < observation.length; i++) {
            forwardArray = forwardOneStep(forwardArray, observation[i]);
        }

        /*计算结束概率*/
        for (int i = 0; i < nStateI; i++) {
            likelihood += forwardArray[i] * endProbs[i];
        }

        return likelihood;
    }

    /**
     * 计算给定隐藏状态和观察序列的似然值，主要用于验证算法的正确性，不要在程序中使用
     * @param hidden
     * @param observation
     * @return
     */
    public double likelihood(int[] hidden, int[] observation) {
        double result = 1.0;
        for (int i = 0; i < hidden.length; i++) {
            result *= emission[hidden[i]][observation[i]];
        }
        return result;
    }

    /**
     * 计算给定隐藏状态序列的似然值
     * @param hidden
     * @return
     */
    public double prior(int[] hidden) {
        double result = startProbs[hidden[0]];
        for (int i = 1; i < hidden.length; i++) {
            result *= transition[hidden[i - 1]][hidden[i]];
        }
        result *= endProbs[hidden[hidden.length - 1]];
        return result;
    }

    public double brutalforceLikely(int[] observation) {
        int obslength = observation.length;
        double result = 0.0;
        int[] hidden = new int[obslength];
        List<int[]> hiddens = new ArrayList<int[]>();

        double numConfig = Math.pow(nStateI, obslength);
        Map<Integer, int[]> codeMap = new HashMap<Integer, int[]>();

        for (int i = 0; i < numConfig; i++) {
            hidden = new int[obslength];
            int tmp = i / nStateI;
            int[] prefix = codeMap.get(tmp);
            if (prefix == null) {
                prefix = new int[obslength];
            }

            for (int j = 0; j < obslength - 1; j++) {
                hidden[j] = prefix[j + 1];
            }
            hidden[obslength - 1] = i % nStateI;
            hiddens.add(hidden.clone());
            codeMap.put(i, hidden.clone());
            result += prior(hidden) * likelihood(hidden, observation);

        }
        return result;
    }

    public int[] viterbi(int[] observation) {
        int[] hidden = new int[observation.length];
        int[][] maxIndexArray = new int[observation.length][nStateI];
        double[] maxScoreArray = new double[nStateI];
        double[] lastMaxScoreArray = new double[nStateI];
        /*计算第一个观测值的似然*/
        for (int i = 0; i < nStateI; i++) {
            lastMaxScoreArray[i] = startProbs[i] * emission[i][observation[0]];
        }

        for (int i = 1; i < observation.length; i++) {
            for (int j = 0; j < nStateI; j++) {
                double maxScore = 0.0;
                double score = 0.0;
                int maxIndex = 0;
                for (int k = 0; k < nStateI; k++) {
                    score = lastMaxScoreArray[k] * transition[k][j];
                    if (score > maxScore) {
                        maxScore = score;
                        maxIndex = k;
                    }
                }
                maxScoreArray[j] = maxScore;
                maxIndexArray[i - 1][j] = maxIndex;
            }
            lastMaxScoreArray = maxScoreArray;
        }
        int lastMaxIndex = 0;

        double maxScore = 0.0;
        double score = 0.0;
        for (int i = 0; i < nStateI; i++) {
            score = maxScoreArray[i] * endProbs[i];
            if (score > maxScore) {
                maxScore = score;
                lastMaxIndex = i;
            }
        }
        hidden[observation.length - 1] = lastMaxIndex;

        /*计算结束概率*/
        for (int i = observation.length - 2; i >= 0; i--) {
            lastMaxIndex = maxIndexArray[i][lastMaxIndex];
            hidden[i] = lastMaxIndex;
        }
        return hidden;
    }

    public static void main(String[] args) {
        HMM hmm = new HMM(3, 2);
        hmm.randomInit();
        int[] obs = { 1, 0, 1 ,1};
        hmm.brutalforceLikely(obs);
    }

    /**
     * @return the transition
     */
    public double[][] getTransition() {
        return transition;
    }

    /**
     * @param transition the transition to set
     */
    public void setTransition(double[][] transition) {
        this.transition = transition;
    }

    public void setTransition(int i, int j, double prob) {
        this.transition[i][j] = prob;
    }

    public double[][] getEmission() {
        return emission;
    }

    public void setEmission(double[][] emission) {
        this.emission = emission;
    }

    public double[] getStartProbs() {
        return startProbs;
    }

    public void setStartProbs(double[] startProbs) {
        this.startProbs = startProbs;
    }

    public double[] getEndProbs() {
        return endProbs;
    }

    public void setEndProbs(double[] endProbs) {
        this.endProbs = endProbs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String lineSplit = System.getProperty("line.separator");

        builder.append("nState:" + nStateI).append(lineSplit);
        builder.append("nObservation:" + nObservationI).append(lineSplit);
        builder.append("start probs").append(lineSplit);
        builder.append(Arrays.toString(startProbs)).append(lineSplit);
        builder.append("trans array").append(lineSplit);
        for (int i = 0; i < nStateI; i++) {
            builder.append(Arrays.toString(transition[i])).append(lineSplit);
        }
        builder.append("end probs").append(lineSplit);
        builder.append(Arrays.toString(endProbs)).append(lineSplit);
        builder.append("emission prob").append(lineSplit);
        for (int i = 0; i < nStateI; i++) {
            builder.append(Arrays.toString(emission[i])).append(lineSplit);
        }

        return builder.toString();
    }

}
