package com.chong.nlp.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chong.nlp.util.Biple;
import com.chong.nlp.util.BipleCounter;

public class BipleCounterTest {
    private BipleCounter bipleCounter;

    public BipleCounterTest() {
        bipleCounter = new BipleCounter();
    }

    @Test
    public void testCountOneBiple() {
        bipleCounter.countOneBiple(new Biple(1, 2));
        bipleCounter.countOneBiple(new Biple(1, 3));

        assertEquals(new Integer(2), bipleCounter.getWordTagCountMap().get(0).get(0));
        assertEquals(new Integer(2), bipleCounter.getTagWordCountMap().get(0).get(0));
        
        assertEquals(new Integer(2), bipleCounter.getWordTagCountMap().get(1).get(0));
        
        assertEquals(new Integer(1), bipleCounter.getTagWordCountMap().get(2).get(0));
        assertEquals(new Integer(1), bipleCounter.getTagWordCountMap().get(3).get(0));

        assertEquals(new Integer(1), bipleCounter.getWordTagCountMap().get(1).get(2));
        assertEquals(new Integer(1), bipleCounter.getWordTagCountMap().get(1).get(3));
        
        assertEquals(new Integer(1), bipleCounter.getTagWordCountMap().get(2).get(1));
        assertEquals(new Integer(1), bipleCounter.getTagWordCountMap().get(3).get(1));
    }

}
