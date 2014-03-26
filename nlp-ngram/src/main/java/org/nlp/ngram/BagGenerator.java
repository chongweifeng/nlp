/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlp.ngram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author chong
 */
public class BagGenerator {

    private NGramCounter ngc;
    private double[][] viterbiDses;
    private int[][] backPoints;

    public BagGenerator(NGramCounter ngc) {
        this.ngc = ngc;
    }

    public String reconstruct(List<String> wordList) {
        int nstateI = wordList.size();
        int nObservationI = wordList.size();


        viterbiDses = new double[nObservationI][nstateI + 2];
        backPoints = new int[nObservationI][nstateI + 1];
        List<Integer> choseIntegers = new ArrayList<Integer>();

        for (int i = 0; i < nObservationI; i++) {
            Arrays.fill(backPoints[i], -1);
            Arrays.fill(viterbiDses[i], 0.0);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nstateI; i++) {
            String string = "<s> " + wordList.get(i);
            viterbiDses[0][i] = ngc.getnGramProb().get(2).get(string) == null ? 0 : ngc.getnGramProb().get(2).get(string);
//            viterbiDses[0][i] = ngc.estimateProbNGram(string);
            backPoints[0][0] = 0;
        }

        for (int i = 1; i < nObservationI; i++) {
//            Arrays.fill(viterbiDses[i], 0);
            double sumProb = 0.0;
            for (int j = 0; j < nstateI; j++) {
                for (int k = 0; k < nstateI; k++) {
                    if (k != j) {
                        String string = wordList.get(k) + " " + wordList.get(j);
                        double probD = ngc.getnGramProb().get(2).get(string) == null ? 0 : ngc.getnGramProb().get(2).get(string);
//                        double probD = ngc.estimateProbNGram(string);
                        if (viterbiDses[i - 1][k] * probD > viterbiDses[i][j]) {
                            viterbiDses[i][j] = viterbiDses[i - 1][k] * probD;
                            backPoints[i - 1][j] = k;
                        }
                    }
                }
                sumProb += viterbiDses[i][j];
            }

            if (sumProb == 0) {
                for (int j = 0; j < nstateI; j++) {
                    for (int k = 0; k < nstateI; k++) {
                        if (k != j) {
                            String string = wordList.get(k) + " " + wordList.get(j);
                            double probD = ngc.estimateProbNGram(string);
                            if (viterbiDses[i - 1][k] * probD > viterbiDses[i][j]) {
                                viterbiDses[i][j] = viterbiDses[i - 1][k] * probD;
                                backPoints[i - 1][j] = k;
                            }
                        }
                    }
                }

            }

         
        }
        for (int k = 1; k < nstateI; k++) {
            String string = wordList.get(k) + " </s>";
            double probD = ngc.getnGramProb().get(2).get(string) == null ? 0 : ngc.getnGramProb().get(2).get(string);
            if (viterbiDses[nObservationI - 1][k] * probD > viterbiDses[nObservationI - 1][nstateI - 1]) {
                viterbiDses[nObservationI - 1][nstateI - 1] = viterbiDses[nObservationI - 1][k] * probD;
                backPoints[nObservationI - 1][nstateI] = k;
            }
        }
        for (int i = 0; i < backPoints.length; i++) {
            int[] ds = backPoints[i];
            for (int j = 0; j < ds.length; j++) {
                System.out.print(ds[j] + " ");
            }
            System.out.println("\n");
        }
        choseIntegers.add(backPoints[nObservationI - 1][nstateI]);
        for (int i = 0, j = nObservationI - 1; i < j; i++) {
            choseIntegers.add(backPoints[j - i - 1][choseIntegers.get(i)]);
        }

        for (int i = choseIntegers.size() - 1; i >= 0; i--) {
            sb.append(wordList.get(choseIntegers.get(i))).append(" ");
        }
        return sb.toString();
    }
}
