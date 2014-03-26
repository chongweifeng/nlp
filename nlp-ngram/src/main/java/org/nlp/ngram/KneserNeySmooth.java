package org.nlp.ngram;



/**
 * @author 崇伟峰
 *
 */
public class KneserNeySmooth {
	/**
	 * 二元语法的总个数
	 */
	private int totalNumOfBiGram=0;
	/**
	 * 
	 */
	private  double discount=0.75;
	
	public KneserNeySmooth(int total,double discount){
		this.totalNumOfBiGram=total;
		this.discount=discount;
	}
	
	
	/**
	 * 计算经过KneserNey平滑过的二元语法概率
	 * @param biGramCount	待计算的二元语法的频率
	 * @param preCount	二元语法第一个词出现的频率
	 * @param contextNum	以第二个词为结尾的二元语法的个数
	 * @param prePostWordNum 以第一个词为开始的二元语法的个数
	 * @return
	 */
	public double computeKNProb(int biGramCount,int preCount,int contextNum,int prePostWordNum){
		return Math.max(biGramCount-discount+0.0, 0.0)/preCount+		(discount*prePostWordNum*contextNum+0.0)/((preCount+0.0)*totalNumOfBiGram);
			
	}
	
	public static void main(String[] args) {
		KneserNeySmooth kns=new KneserNeySmooth(1942171, 0.75);
		System.out.println(kns.computeKNProb(5	,799774,	742	,2858));
	}
}
