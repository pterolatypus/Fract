package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;
import java.awt.geom.Point2D;

public enum Fractal {

	MANDELBROT {

		@Override
		public Color calculate(Point2D point) {
			Complex c = new Complex(point.getX(), point.getY());
			Color col = Color.BLACK;
			for (int n = 0; n < 100; n++) {
				c = c.square();
				c = c.add(new Complex(point.getX(), point.getY()));
				if (c.modulusSquared() < 2) continue;
				float f = n/100;
				col = new Color(f, 1-f, 1-f);
			}
			return col;
		}

	},

	JULIA {

		@Override
		public Color calculate(Point2D point) {
			return null;
		}

	};

	public abstract Color calculate(Point2D point);
}
