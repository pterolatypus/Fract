package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;

import com.github.pterolatypus.comp1206.coursework.fract.engine.InvalidPropertyException;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;

//LOTS OF INNER CLASSES, CODE FOLDING IS RECOMMENDED!
public abstract class Coloring {

	/**
	 * Calculates the {@link Color} of a given complex point based on a number
	 * of parameters
	 * 
	 * @param iterations
	 *            Number of iterations this point took to 'diverge'
	 * @param dPoint
	 *            The value of the number immediately after 'diverging'
	 * @param MAX_ITERATIONS
	 *            The maximum number of iterations which were allowed
	 * @return The {@link Color} of the pixel.
	 */
	public abstract Color getColor(int iterations, Complex dPoint,
			int MAX_ITERATIONS);

	/**
	 * @return Whatever colour will be assigned by this algorithm to values
	 *         which do not diverge within the maximum iterations
	 */
	public abstract Color getNonDivergentColor();

	/**
	 * Allows the setting of particular properties which may differ between
	 * colouring implementations
	 * 
	 * @param propertyName
	 *            The name of the property to set
	 * @param value
	 *            The value to set the property to
	 * @return {@code this}, for method chaining
	 */
	public abstract Coloring setProperty(String propertyName, Object value);

	/**
	 * Logarithmic smooth colouring, sourced from <a href=
	 * "http://en.wikipedia.org/wiki/Mandelbrot_set#Continuous_.28smooth.29_coloring"
	 * >Wikipedia</a>
	 * 
	 * @author James
	 *
	 */
	public static class LogSmooth extends Coloring {

		protected Color[] palette;

		public LogSmooth() {
			setProperty("colorPalette", new Color[] { Color.RED, Color.orange,
					Color.yellow, Color.GREEN, Color.BLUE });
		}

		// The algorithm, sourced from wikipedia with minor modifications.
		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod) / Math.log(2)) / Math.log(2);
			double d = iterations + 1 - nu;
			d /= MAX_ITERATIONS;
			d *= (palette.length - 1);
			Color c1 = palette[(int) d];
			Color c2 = palette[(int) d + 1];
			d %= 1;
			d = Math.abs(d);
			Color c = Coloring.interpolate(c1, c2, d);
			return c;
		}

		@Override
		public Color getNonDivergentColor() {
			return Color.black;
		}

		// This method allows live configuration of the colour palette used by
		// this algorithm
		// A planned feature was to make this user-configurable
		@Override
		public Coloring setProperty(String propertyName, Object value) {
			if (propertyName.equals("colorPalette") && value instanceof Color[]) {
				this.palette = (Color[]) value;
				return this;
			}
			throw new InvalidPropertyException(propertyName, value, this);
		}
	};

	/**
	 * An extension of logarithmic smooth colouring, which gradiates through the
	 * given colour palette once forward and then backward to return to its
	 * starting colour.
	 * 
	 * @author James
	 *
	 */
	public static class LogSmoothDoubleEnded extends LogSmooth {

		public LogSmoothDoubleEnded() {
			super();
		}

		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod) / Math.log(2)) / Math.log(2);
			double d = iterations + 1 - nu;
			d /= MAX_ITERATIONS;
			int n = palette.length - 1;

			// The important code for double-ending the colouring.
			d *= 2 * n;
			if (d > n)
				d = (2 * n) - d;

			Color c1 = palette[(int) d];
			Color c2 = palette[(int) d + 1];
			d %= 1;
			d = Math.abs(d);
			Color c = Coloring.interpolate(c1, c2, d);
			return c;
		}
	}

	/**
	 * An extension of the double-ended smooth colouring algorithm which
	 * iterates over the colour palette a given number of times instead of just
	 * once.
	 * 
	 * @author James
	 *
	 */
	public static class LogSmoothRepeating extends LogSmooth {

		// Default number of times to repeat
		private int repetitions = 12;

		public LogSmoothRepeating() {
			super();
		}

		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod) / Math.log(2)) / Math.log(2);
			double dValue = iterations + 1 - nu;
			dValue /= MAX_ITERATIONS;

			// Expands the value to cover 'repetitions' number of identical
			// colour palettes
			int paletteConstant = palette.length - 1;
			dValue *= 2 * repetitions * paletteConstant;
			// Re-normalises the result to within one palette's range
			dValue %= 2 * paletteConstant;

			// Double-ends the result
			if (dValue > paletteConstant)
				dValue = (2 * paletteConstant) - dValue;

			Color c1 = palette[(int) dValue];
			Color c2 = palette[(int) dValue + 1];
			dValue %= 1;
			dValue = Math.abs(dValue);
			Color result = Coloring.interpolate(c1, c2, dValue);
			return result;
		}

		// This Coloring class allows the setting of its 'repetitions' value, as
		// well as its colour palette
		@Override
		public Coloring setProperty(String propertyName, Object value) {
			if (propertyName.equals("repeats") && value instanceof Integer) {
				this.repetitions = (int) value;
			}
			return super.setProperty(propertyName, value);
		}
	}

	/**
	 * Interpolates between the given {@link Color}s by the specified amount
	 * 
	 * @param c1
	 *            The lower end of the gradient
	 * @param c2
	 *            The upper end of the gradient
	 * @param d
	 *            The amount by which to slide along the gradient (denoted by
	 *            the fractional part {@code d%1})
	 * @return The {@link Color} generated by interpolating
	 */
	public static Color interpolate(Color c1, Color c2, double d) {
		d = d % 1;
		double red = ((c2.getRed() - c1.getRed()) * d) + c1.getRed();
		double green = ((c2.getGreen() - c1.getGreen()) * d) + c1.getGreen();
		double blue = ((c2.getBlue() - c1.getBlue()) * d) + c1.getBlue();
		return new Color((int) red, (int) green, (int) blue);
	}
}
