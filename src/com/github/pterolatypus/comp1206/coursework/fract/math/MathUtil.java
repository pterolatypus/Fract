package com.github.pterolatypus.comp1206.coursework.fract.math;

public class MathUtil {

	public static double round(double value, int dp) {
		double nv = value*Math.pow(10, dp);
		int n = (int) nv;
		nv = n/(Math.pow(10, dp));
		return nv;
	}
	
}
