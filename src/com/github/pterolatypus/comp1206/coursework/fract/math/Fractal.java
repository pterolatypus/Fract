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
					float f = (float) Math.log(Math.log(Math.sqrt(c.modulusSquared()))/Math.log(2));
					f = Math.abs(n+1 - f)/MAX_ITERATIONS;
					return new Color(f,f,(float) ((1-f)*0.4));
				}
			}
			return Color.BLACK;
		}

		@Override
		public Fractal updateFractal(Complex point) {
			return this;
		}

	};

	public static class Julia extends Fractal {
		
		private Complex init;
		
		public Julia(Complex init) {
			this.init = init;
		}
		
		@Override
		public Color calculate(Complex point) {
			Complex c = point;
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.square();
				c = c.add(new Complex(init.getReal(), init.getImaginary()));
				if (c.modulusSquared() > 4) {
					float f = (float) Math.log(Math.log(Math.sqrt(c.modulusSquared()))/Math.log(2));
					f = Math.abs(n+1 - f)/MAX_ITERATIONS;
					return new Color(f,f,(float) ((1-f)*0.6));
				}
			}
			return Color.BLACK;
		}

		@Override
		public Fractal updateFractal(Complex point) {
			if (!point.equals(init)) {
				return new Fractal.Julia(point);
			}
			return this;
		}

	};

	private static int MAX_ITERATIONS = 200;
	
	public abstract Color calculate(Complex point);
	
	public abstract Fractal updateFractal(Complex point);
	
}
