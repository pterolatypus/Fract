package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Graph {

	private Fractal f;

	private Rectangle2D mathBounds = new Rectangle2D.Double(-2d, 1.6d, 4d, 3.2d);
	private Rectangle pixelBounds;
	
	
	public Graph(Fractal f) {
		this.f = f;
	}
	
	public void recalculate() {
		
	}
	
	public void recalculate(Rectangle pixelBounds) {
		this.pixelBounds = pixelBounds;
		recalculate();
	}
	
	public BufferedImage getAsImage() {
		return null;
	}

}
