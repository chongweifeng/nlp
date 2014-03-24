package com.chong.nlp.algorithm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HMMTest {

    private HMM hmm;

    public HMMTest() {
        hmm = new HMM(2, 2);
        double[] start = { 0.1, 0.9 };
        double[] end = { 0.8, 0.2 };
        hmm.setStartProbs(start);
        hmm.setEndProbs(end);
        double[][] trans = { start, end };
        double[][] emission = { end, start };
        hmm.setTransition(trans);
        hmm.setEmission(emission);

    }

    @Test
    public void testLikelihood() {
        int[] observation = new int[4];
        observation[0] = 0;
        observation[1] = 0;
        observation[2] = 1;
        observation[3] = 1;
        hmm.randomInit();
        double likelihood = hmm.likelihood(observation);
        double brutal = hmm.brutalforceLikely(observation);
        assertEquals(likelihood, brutal, 0.001);
    }

    @Test
    public void testbrutalforceLikelihood() {
        int[] observation = new int[2];
        observation[0] = 1;
        observation[1] = 0;
        double likelihood = hmm.brutalforceLikely(observation);
        assertEquals(0.4196, likelihood, 0.00001);
    }

    @Test
    public void testViterbi() {
        int[] observation = new int[2];
        observation[0] = 1;
        observation[1] = 0;
        int[] hidden = hmm.viterbi(observation);
        int[] expecteds = { 1, 0 };
        assertArrayEquals(expecteds, hidden);
    }

    @Test
    public void testLikelihood2() {
        int[] observation = new int[2];
        observation[0] = 1;
        observation[1] = 0;
        double likelihood = hmm.likelihood(observation, observation);
        assertEquals(0.72, likelihood, 0.0001);
    }

    @Test
    public void testPrior() {
        int[] hidden = new int[2];
        hidden[0] = 1;
        hidden[1] = 0;
        double prior = hmm.prior(hidden);
        assertEquals(0.72 * 0.8, prior, 0.0001);
    }
}
