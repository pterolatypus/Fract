package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;

import com.github.pterolatypus.comp1206.coursework.fract.engine.InvalidPropertyException;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;

public abstract class Coloring {

	public abstract Color getColor(int iterations, Complex dPoint,
			int MAX_ITERATIONS);

	public abstract Color getNonDivergentColor();

	public abstract Coloring setProperty(String propertyName, Object value);

	public static class LogSmooth extends Coloring {

		protected Color[] palette;

		public LogSmooth() {
			setProperty("colorPalette", new Color[] { Color.RED, Color.orange,
					Color.yellow, Color.GREEN, Color.BLUE });
		}

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
			return Color.white;
		}

		@Override
		public Coloring setProperty(String propertyName, Object value) {
			if (propertyName.equals("colorPalette") && value instanceof Color[]) {
				this.palette = (Color[]) value;
				return this;
			}
			throw new InvalidPropertyException(propertyName, value, this);
		}
	};

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

	public static class LogSmoothRepeating extends LogSmooth {

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
			int paletteConstant = palette.length - 1;
			dValue *= 2 * repetitions * paletteConstant;
			dValue %= 2 * paletteConstant;
			if (dValue > paletteConstant)
				dValue = (2 * paletteConstant) - dValue;
			Color c1 = palette[(int) dValue];
			Color c2 = palette[(int) dValue + 1];
			dValue %= 1;
			dValue = Math.abs(dValue);
			Color result = Coloring.interpolate(c1, c2, dValue);
			return result;
		}

		@Override
		public Coloring setProperty(String propertyName, Object value) {
			if (propertyName.equals("repeats") && value instanceof Integer) {
				this.repetitions = (int) value;
			}
			return super.setProperty(propertyName, value);
		}
	}

	public static Color interpolate(Color c1, Color c2, double d) {
		double red = ((c2.getRed() - c1.getRed()) * d) + c1.getRed();
		double green = ((c2.getGreen() - c1.getGreen()) * d) + c1.getGreen();
		double blue = ((c2.getBlue() - c1.getBlue()) * d) + c1.getBlue();
		return new Color((int) red, (int) green, (int) blue);
	}
}
