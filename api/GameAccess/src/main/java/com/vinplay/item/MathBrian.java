package com.vinplay.item;

import java.math.BigDecimal;
/**
 * 解决Double金额问题
 * @author Brian
 *
 */
public class MathBrian {
	private static final int DEF_DIV_SCALE = 10;

	
	/**
	 * 精度
	 * @param n 保留几位
	 * @param d double
	 * @return
	 */
	public static double scale(int n,double d){
		BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
	}
	/**
	 * 提供精确的加法运算�?
	 * 
	 * @param v1
	 *            被加�?
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	public static double adds(double...arg) {
		BigDecimal res = new BigDecimal(0);
		for(int i=0;i<arg.length;i++){
			BigDecimal b = new BigDecimal(Double.toString(arg[i]));
			res = res.add(b);
		}
		return res.doubleValue();
	}
	/**
	 * 提供精确的减法运算�?
	 * 
	 * @param v1
	 *            被减�?
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	public static double sub(double...arg) {
		double f = arg[0];
		for(int i=1;i<arg.length;i++){
			BigDecimal b1 = new BigDecimal(Double.toString(f));
			BigDecimal b2 = new BigDecimal(Double.toString(arg[i]));
			f = b1.subtract(b2).doubleValue();
		}
		return f;
	}
	/**
	 * 提供精确的乘法运算�?
	 * 
	 * @param v1
	 *            被乘�?
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	public static double muls(double...arg){
		if(arg.length==0){
			return 0;
		}
		BigDecimal res = new BigDecimal(1);
		for(int i=0;i<arg.length;i++){
			BigDecimal b = new BigDecimal(Double.toString(arg[i]));
			res = res.multiply(b);
		}
		return res.doubleValue();
	}
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以�?0位，以后的数字四舍五入�?
	 * 
	 * @param v1
	 *            被除�?
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算�?当发生除不尽的情况时，由scale参数�?定精度，以后的数字四舍五入�?
	 * 
	 * @param v1
	 *            被除�?
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示�?��精确到小数点以后几位�?
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理�?
	 * 
	 * @param v
	 *            �?��四舍五入的数�?
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
