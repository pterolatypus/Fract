package com.github.pterolatypus.comp1206.coursework.fract.math;

import java.awt.Color;

import com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring;

public abstract class Fractal {
	
	public static Fractal.Julia MANDELBROT = new Fractal.Julia(null) {

		@Override
		public Color calculate(Complex point) {
			this.init = point;
			return super.calculate(point);
		}
		
		@Override
		public Fractal updateFractal(Complex c, Coloring col) {
			setColoring(col);
			return this;
		}
	};
	
	public static class Multibrot extends Fractal {
		private int power = 2;
		public Multibrot(int power) {
			this.power = power;
		}
		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(),point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.pow(power);
				c = c.add(new Complex(point.getReal(), point.getImaginary()));
				if (c.modulusSquared() > 4) {
					return col.getColor(n, c, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}
	}
	
	public static Fractal BURNING_SHIP = new Fractal() {
		
		@Override
		public Color calculate(Complex point) {
			Complex modP = new Complex(0,0);
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				modP = new Complex(Math.abs(modP.getReal()), Math.abs(modP.getImaginary()));
				modP = modP.square().add(point);
				if (modP.modulusSquared() > 4) {
					return col.getColor(n, modP, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}		
	};

	public static class Julia extends Fractal {
		
		protected Complex init;
		
		public Julia(Complex init) {
			this.init = init;
		}
		
		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(),point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.square();
				c = c.add(new Complex(init.getReal(), init.getImaginary()));
				if (c.modulusSquared() > 4) {
					return col.getColor(n, c, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}

		@Override
		public Fractal updateFractal(Complex point, Coloring col) {
			if (!point.equals(init)) {
				Fractal f = new Fractal.Julia(point);
				f.setColoring(col);
				return f;
			}
			return this;
		}

	};

	private static int MAX_ITERATIONS = 200;
	protected Coloring col;
	
	public abstract Color calculate(Complex point);
	
	public Fractal updateFractal(Complex point, Coloring c) {
		return this;
	}
	
	public void setColoring(Coloring c) {
		this.col = c;
	}
	
}
