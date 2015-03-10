package com.github.pterolatypus.comp1206.coursework.fract.math;

import java.io.Serializable;

import com.github.pterolatypus.comp1206.coursework.fract.gui.AppWindow;


public class Complex implements Serializable {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	private double real;
	private double imaginary;
	
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * @return The real part of this complex number
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * @return The imaginary part of this complex number
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Returns this complex number multiplied by itself, without modifying in-place
	 * @return The square of this complex number
	 */
	public Complex square() {
		double real = this.real*this.real;
		real -= this.imaginary*this.imaginary;
		double imaginary = 2*this.imaginary*this.real;
		return new Complex(real, imaginary);
	}
	
	/**
	 * @return The square of the modulus of this complex number
	 */
	public double modulusSquared() {
		double ret = real*real;
		ret += imaginary*imaginary;
		return ret;
	}
	
	/**
	 * Returns the sum of this complex number and the parameter, does not modify either in-place
	 * @param x The complex number to add to this one
	 * @return The sum
	 */
	public Complex add(Complex x) {
		double real = this.real + x.getReal();
		double imaginary = this.imaginary + x.getImaginary();
		return new Complex(real, imaginary);
	}
	
	public Complex multiply(Complex c) {
		double real = getReal();
		double imag = getImaginary();
		real = (real*c.getReal())-(imag*c.getImaginary());
		imag = (real*c.getImaginary())+(imag*c.getReal());
		return new Complex(real, imag);
	}
	
	public Complex pow(int power) {
		Complex c = clone();
		if (power == 1) {
			return c;
		}
		double n = Math.log(power)/Math.log(2);
		int f = (int) Math.floor(n);
		for (int i = f; i > 0 ; i--) {
			c = c.square();
		}
		int r = power - f;
		for (int i = r; i > 0; i--) {
			c = c.pow(r);
		}
		return c;
	}
	
	@Override
	public Complex clone() {
		return new Complex(this.getReal(), this.getImaginary());
	}
	
	public Complex conjugate() {
		return new Complex(this.getReal(), -this.getImaginary());
	}
	
}
