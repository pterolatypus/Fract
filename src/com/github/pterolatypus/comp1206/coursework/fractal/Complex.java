package com.github.pterolatypus.comp1206.coursework.fractal;

public class Complex {

	private double real;
	private double imaginary;
	
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public double getReal() {
		return real;
	}
	
	public double getImaginary() {
		return imaginary;
	}
	
	public Complex square() {
		double real = this.real*this.real;
		real -= this.imaginary*this.imaginary;
		double imaginary = 2*this.imaginary*this.real;
		return new Complex(real, imaginary);
	}
	
	public double modulusSquared() {
		double ret = real*real;
		ret += imaginary*imaginary;
		return ret;
	}
	
	public Complex add(Complex x) {
		double real = this.real + x.getReal();
		double imaginary = this.imaginary + x.getImaginary();
		return new Complex(real, imaginary);
	}
	
}
