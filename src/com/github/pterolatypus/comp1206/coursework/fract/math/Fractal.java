package com.github.pterolatypus.comp1206.coursework.fract.math;

import java.awt.Color;

import com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring;

/**
 * Describes a mathematical Fractal; an iterative formula which, given a unique
 * set of {@link Complex} roots, either diverges or does not as n -> infinity.
 * 
 * @author James
 *
 */
public abstract class Fractal {

	/**
	 * The Mandelbrot set, implemented as a subclass of its own Julia just for
	 * convenience's sake
	 */
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

		@Override
		public String toString() {
			return "Mandelbrot";
		}
	};

	/**
	 * The tricorn fractal, a three-pointed fractal with many small
	 * mandelbrot-like structures along its edge
	 */
	public static Fractal TRICORN = new Fractal() {

		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(), point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.conjugate().square();
				c = c.add(new Complex(point.getReal(), point.getImaginary()));
				if (c.modulusSquared() > 4) {
					return col.getColor(n, c, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}

		@Override
		public String toString() {
			return "Tricorn";
		}

	};

	/**
	 * An (incomplete) attempt and implementing the Multibrot fractal; a
	 * generalisation of the mandelbrot set which involves varying the exponent
	 * in the formula
	 * 
	 * @author James
	 *
	 */
	public static class Multibrot extends Fractal {
		private int power = 2;

		public Multibrot(int power) {
			this.power = power;
		}

		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(), point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.pow(power);
				c = c.add(new Complex(point.getReal(), point.getImaginary()));
				if (c.modulusSquared() > 4) {
					return col.getColor(n, c, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}

		@Override
		public String toString() {
			return "Multibrot " + power;
		}
	}

	/**
	 * The 'Burning Ship' fractal, a particularly cool-looking one.
	 */
	public static Fractal BURNING_SHIP = new Fractal() {

		@Override
		public Color calculate(Complex point) {
			Complex modP = new Complex(0, 0);
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				modP = new Complex(Math.abs(modP.getReal()), Math.abs(modP
						.getImaginary()));
				modP = modP.square().add(point);
				if (modP.modulusSquared() > 4) {
					return col.getColor(n, modP, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}

		@Override
		public String toString() {
			return "Burning Ship";
		}
	};

	/**
	 * The Mandelbrot-Julia set
	 * 
	 * @author James
	 *
	 */
	public static class Julia extends Fractal {

		protected Complex init;

		public Julia(Complex init) {
			this.init = init;
		}

		@Override
		public Color calculate(Complex point) {
			Complex c = new Complex(point.getReal(), point.getImaginary());
			for (int n = 0; n < MAX_ITERATIONS; n++) {
				c = c.square();
				c = c.add(new Complex(init.getReal(), init.getImaginary()));
				if (c.modulusSquared() > 4) {
					return col.getColor(n, c, MAX_ITERATIONS);
				}
			}
			return col.getNonDivergentColor();
		}

		// Returns a new instance of Julia using the specified complex point as
		// secondary root
		@Override
		public Fractal updateFractal(Complex point, Coloring col) {
			if (!point.equals(init)) {
				Fractal f = new Fractal.Julia(point);
				f.setColoring(col);
				return f;
			}
			return this;
		}

		@Override
		public String toString() {
			return "Julia";
		}

	};

	private static int MAX_ITERATIONS = 200;
	protected Coloring col;

	/**
	 * The method (overridden by each sub-Fractal) which actually performs the
	 * iterations and calculates the parameters used to render each pixel. This
	 * method should make a call to {@link Coloring.getColor} to determine what
	 * {@link Color} to return
	 * 
	 * @param point
	 *            the {@link Complex} point to iterate from
	 * @return the {@link Color} to paint the pixel represented by the specified
	 *         point
	 */
	public abstract Color calculate(Complex point);

	/**
	 * Returns an updated version of this fractal using the new secondary
	 * {@link Complex} root (currently only affects the Julia fractal, but has
	 * to be abstracted for panel-swapping to work)
	 * 
	 * @param point
	 *            the new secondary root to use
	 * @param c
	 *            the {@link Coloring} to pass to the new Fractal
	 * @return the updated Fractal
	 */
	public Fractal updateFractal(Complex point, Coloring c) {
		return this;
	}

	public void setColoring(Coloring c) {
		this.col = c;
	}

	@Override
	public abstract String toString();

	public static void setMaxIterations(int newi) {
		MAX_ITERATIONS = newi;
	}

	public static int getMaxIterations() {
		return MAX_ITERATIONS;
	}

}
