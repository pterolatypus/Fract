package com.github.pterolatypus.comp1206.coursework.fract.math;

import java.awt.Color;

public abstract class Fractal {

	
	public static Fractal MANDELBROT = new Fractal() {

		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(), point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.square();
				c = c.add(new Complex(point.getReal(), point.getImaginary()));
				if (c.modulusSquared() > 4) {
					float f = (float) Math.log(Math.log(c.modulusSquared())/Math.log(2));
					f = Math.abs(n+1 - f)/MAX_ITERATIONS;
					return new Color(f,f,(float) ((1-f)*0.6));
				}
			}
			return Color.BLACK;
		}

	};

	public static class Julia extends Fractal {

		private Complex init;
		
		public Julia(Complex init) {
			this.init = init;
		}
		
		@Override
		public Color calculate(Complex point) {
			Complex c = init;
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.square();
				c = c.add(new Complex(point.getReal(), point.getImaginary()));
				if (c.modulusSquared() > 4) {
					float f = (float) Math.log(Math.log(c.modulusSquared())/Math.log(2));
					f = Math.abs(n+1 - f)/MAX_ITERATIONS;
					return new Color(f,f,(float) ((1-f)*0.6));
				}
			}
			return Color.BLACK;
		}

	};

	private static final int MAX_ITERATIONS = 100;
	
	public abstract Color calculate(Complex point);
	
}