/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlp.ngram;

import com.chong.nlp.commons.inter.ISegmenter;
import com.chong.nlp.util.PunctuationFilter;

/**
 *
 * @author chong
 */
public class SentencePrePlexity {

    private NGramCounter ngc; //n元语法模型
    private ISegmenter mms; //正向最大匹配分词器

    public SentencePrePlexity(NGramCounter ngc, ISegmenter segmenter) {
        this.mms = segmenter;
        this.ngc = ngc;
    }

    /**
     *计算一句话的preplixity值
     * 计算方法为句子中包含的所有n元语法的概率乘积的倒数再开K次方，K为句子种包含的n元语法的个数
     * 计算时先对概率乘积取对数然后在进行指数运算
     * @param sentence 包含一句话
     * @return preplexity 值
     */
    public double computePP(String sentence) {
        String segmentedSentence = PunctuationFilter.deletePunct(mms.segment2String(sentence)); //去除标点
        String labledSentenceString = "<s> " + segmentedSentence + " </s>";
        String[] splitedSentenceStrings = labledSentenceString.split("\\s+");
        double probD = 0.0; //记录
        int nGramLengthI = 0;
        /**
         * 计算preplexity值
         */
        for (int i = 0, j = splitedSentenceStrings.length - ngc.getnGram() + 1; i < j; i++) {
            StringBuilder sb = new StringBuilder();
            double[] probDs = new double[ngc.getnGram()];
            for (int k = 0; k < probDs.length; k++) {
                probDs[k] = 0.0;

            }

            /**
             * 对于序列w1w2w3,分别提取P(w3)、P(w3|w2)、P(w3|w1w2)
             * 如果P(w3)为0，则用n元模型中的词数的倒数作为概率估计
             */
            for (int k = ngc.getnGram(); k > 0; k--) {
                sb.insert(0, splitedSentenceStrings[i + k - 1]);
                double kGramProb = ngc.getnGramProb().get(ngc.getnGram() - k + 1)
                    .get(sb.toString().trim()) == null ? 0 : ngc.getnGramProb()
                    .get(ngc.getnGram() - k + 1).get(sb.toString().trim());
                if (kGramProb == 0) {
                    probDs[ngc.getnGram() - k] = 1.0 / Math.pow(ngc.getTotalItemI(), ngc.getnGram()
                                                                                     - k + 1);
                    sb.insert(0, " ");

                    continue;
                }
                probDs[ngc.getnGram() - k] = kGramProb;
                sb.insert(0, " ");
            }
            nGramLengthI++;
            double sumProb = 0;

            /**
             * 对概率插值求
             */
            for (int k = 0; k < ngc.getnGram(); k++) {
                System.out.println(probDs[k]);
                sumProb += (ngc.getnGram() + 0.0) / Math.pow(2, k) * probDs[k];
            }

            System.out.println(sb.toString().trim());
            //            probD +=Math.log(ngc.getnGramProb().get(ngc.getnGram()).get(sb.toString().trim()));
            probD += Math.log(sumProb / (2 * ngc.getnGram() * (1 - Math.pow(0.5, ngc.getnGram()))));

            System.out.println(probD);
        }
        System.out.println(nGramLengthI);
        return Math.exp((-1.0 / nGramLengthI) * probD);
    }
}
