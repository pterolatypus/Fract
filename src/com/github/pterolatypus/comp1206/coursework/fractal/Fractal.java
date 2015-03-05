package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;

public enum Fractal {

	
	MANDELBROT {

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

	},

	JULIA {

		@Override
		public Color calculate(Complex point) {
			return null;
		}

	};

	private static final int MAX_ITERATIONS = 100;
	
	public abstract Color calculate(Complex point);
	
}
