package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;

public abstract class Coloring {

	public abstract Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS);
	
	public abstract Color getNonDivergentColor();
	
	public abstract Coloring setColorPalette(Color[] palette);
	
	public static class LogSmooth extends Coloring {
		
		protected Color[] palette;
		
		public LogSmooth() {
			this(new Color[]{Color.RED, Color.orange, Color.yellow, Color.GREEN, Color.BLUE});
		}
		
		public LogSmooth(Color[] palette) {
			this.palette = palette;
		}
		
		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod)/Math.log(2))/Math.log(2);
			double d = iterations + 1 - nu;
			d /= MAX_ITERATIONS;
			d *= (palette.length-1);
			Color c1 = palette[(int)d];
			Color c2 = palette[(int)d+1];
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
		public Coloring setColorPalette(Color[] palette) {
			this.palette = palette;
			return this;
		}
	};
	
	public static class LogSmoothDoubleEnded extends LogSmooth {
		
		public LogSmoothDoubleEnded() {
			super();
		}
		
		public LogSmoothDoubleEnded(Color[] palette) {
			super(palette);
		}
		
		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod)/Math.log(2))/Math.log(2);
			double d = iterations + 1 - nu;
			d /= MAX_ITERATIONS;
			int n = palette.length-1;
			d *= 2*n;
			if (d > n) d = (2*n)-d;
			Color c1 = palette[(int)d];
			Color c2 = palette[(int)d+1];
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
		
		public LogSmoothRepeating(int repeats) {
			super();
			this.repetitions = repeats;
		}
		
		public LogSmoothRepeating(int repeats, Color[] palette) {
			super(palette);
			this.repetitions = repeats;
		}
		
		
		@Override
		public Color getColor(int iterations, Complex dPoint, int MAX_ITERATIONS) {
			double mod = Math.sqrt(dPoint.modulusSquared());
			double nu = Math.log(Math.log(mod)/Math.log(2))/Math.log(2);
			double d = iterations + 1 - nu;
			d /= MAX_ITERATIONS;
			int n = palette.length-1;
			d *= 2*repetitions*n;
			d %= 2*n;
			if (d > n) d = (2*n)-d;
			Color c1 = palette[(int)d];
			Color c2 = palette[(int)d+1];
			d %= 1;
			d = Math.abs(d);
			Color c = Coloring.interpolate(c1, c2, d);
			return c;
		}
	}
	
	public static Color interpolate(Color c1, Color c2, double d) {
		double red = ((c2.getRed()-c1.getRed())*d)+c1.getRed();
		double green = ((c2.getGreen()-c1.getGreen())*d)+c1.getGreen();
		double blue = ((c2.getBlue()-c1.getBlue())*d)+c1.getBlue();
		return new Color((int) red,(int) green,(int) blue);
	}
}
