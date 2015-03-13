package com.github.pterolatypus.comp1206.coursework.fract.math;

/**
 * A class of static mathematical helper methods
 * 
 * @author James
 *
 */
public class MathUtil {

	/**
	 * Rounds the given double value to the specified number of decimal places
	 * 
	 * @param value
	 *            the double value to round
	 * @param dp
	 *            the number of decimal places
	 * @return the rounded result
	 */
	public static double round(double value, int dp) {
		double nv = value * Math.pow(10, dp);
		int n = (int) nv;
		nv = n / (Math.pow(10, dp));
		return nv;
	}

}
