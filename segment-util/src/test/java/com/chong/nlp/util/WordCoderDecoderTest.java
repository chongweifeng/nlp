package com.chong.nlp.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chong.nlp.util.WordCoderDecoder;

public class WordCoderDecoderTest {
    private WordCoderDecoder coderDecoder;

    public WordCoderDecoderTest() {
        coderDecoder = new WordCoderDecoder();
        coderDecoder.codeWord("a");
        coderDecoder.codeWord("b");
    }

    @Test
    public void testCodeWord() {
        assertEquals(new Integer(1), coderDecoder.codeWord("a"));
        assertEquals(new Integer(2), coderDecoder.codeWord("b"));
        assertEquals(new Integer(3), coderDecoder.codeWord("c"));
    }
    @Test
    public void testDecoderWord(){
        assertEquals("a", coderDecoder.decodeWord(1));
        assertEquals("b", coderDecoder.decodeWord(2));
        assertNull(coderDecoder.decodeWord(3));
    }
}
