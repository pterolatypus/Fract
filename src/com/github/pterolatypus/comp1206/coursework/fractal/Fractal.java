package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;
import java.awt.geom.Point2D;

public enum Fractal {

	MANDELBROT {

		@Override
		public Color calculate(Point2D point) {
			return null;
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
