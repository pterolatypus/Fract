package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Graph {

	private Fractal f;

	private Rectangle2D mathBounds = new Rectangle2D.Double(-2d, 1.6d, 4d, 3.2d);
	BufferedImage im;
	
	public Graph(Fractal f) {
		this.f = f;
	}
	
	public BufferedImage calculate(Rectangle pixelBounds) {
		im = new BufferedImage((int) pixelBounds.getWidth(), (int) pixelBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
		System.out.println("Recalculating fractal!");
		for (int x = 0; x < pixelBounds.getWidth(); x++) {
			for (int y = 0; y < pixelBounds.getWidth(); y++) {
				double mX = (x/pixelBounds.getWidth())*mathBounds.getWidth() + mathBounds.getMinX();
				double mY = mathBounds.getMaxY() - (y/pixelBounds.getHeight())*mathBounds.getHeight();
				im.setRGB(x, y, f.calculate(new Point2D.Double(mX,mY)).getRGB());
			}
		}
		return im;
	}
	
	public BufferedImage getImage() {
		return im;
	}
	
	public void resize(Rectangle2D mathBounds) {
		this.mathBounds = mathBounds;
	}

}
