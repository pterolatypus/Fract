package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Graph {

	private Fractal f;

	private Rectangle2D mathBounds = new Rectangle2D.Double(-2d, 1.6d, 4d, 3.2d);
	
	
	public Graph(Fractal f) {
		this.f = f;
	}
	
	public void calculate(Rectangle pixelBounds) {
		
	}
	
	public void resize(Rectangle2D mathBounds) {
		this.mathBounds = mathBounds;
	}

}
