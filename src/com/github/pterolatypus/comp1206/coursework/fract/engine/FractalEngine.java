package com.github.pterolatypus.comp1206.coursework.fract.engine;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring;
import com.github.pterolatypus.comp1206.coursework.fract.gui.GraphPanel;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

public class FractalEngine extends Thread {

	private boolean bRun = true;

	private ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();
	private GraphPanel parent;

	private Fractal f;
	private Double mathBounds;

	private BufferedImage graphImage;
	private Coloring colorScheme;
	
	public FractalEngine(Fractal f, GraphPanel parent) {
		super();
		this.f = f;
		this.parent = parent;
		this.setPriority(this.getPriority() - 1);
	}

	@Override
	public void run() {
		while (bRun) {
			try {
				while (taskQueue.isEmpty()) {
					Thread.sleep(10);
				}
				while (taskQueue.size() > 1) {
					taskQueue.poll();
				}
				Runnable r = taskQueue.poll();
				r.run();
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
	
	public synchronized void updateFractal(Complex p) {
		this.f = f.updateFractal(p, colorScheme);
	}

	public BufferedImage getImage() {
			return graphImage;
	}

	public void updateMathBounds(Rectangle2D.Double mathBounds) {
		this.mathBounds = mathBounds;
	}
	
	public void setColorScheme(Coloring c) {
		colorScheme = c;
		f.setColoring(c);
	}

	public void updateImage(final Rectangle pixelBounds) {
		taskQueue.add(new Runnable() {
			public void run() {
				BufferedImage im = new BufferedImage((int)pixelBounds.getWidth(), (int)pixelBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < pixelBounds.getWidth(); x++) {
					for (int y = 0; y < pixelBounds.getHeight(); y++) {
						if (x==0 || y==0 || x==pixelBounds.getWidth()-1 || y==pixelBounds.getWidth()-1) {
							im.setRGB(x,y,Color.BLACK.getRGB());
						} else {
							double mathX = (x/pixelBounds.getWidth())*mathBounds.getWidth()+mathBounds.getX();
							double mathY = (y/pixelBounds.getHeight())*mathBounds.getHeight()+mathBounds.getY();
							Color c;
							synchronized(this){
								c = f.calculate(new Complex(mathX,mathY));
							}
							im.setRGB(x,y,c.getRGB());
						}
					}
				}
				graphImage = im;
				parent.repaint();
			}
		});
	}

	public void updateImage(Rectangle pixelBounds, Fractal f) {
		this.f = f;
		updateImage(pixelBounds);
	}

	public void terminate() {
		bRun = false;
	}

	public Complex getMathCoords(Point pixelCoords, Rectangle pixelBounds) {
		double mathX = (pixelCoords.getX() / pixelBounds.getWidth())
				* mathBounds.getWidth() + mathBounds.getX();
		double mathY = (pixelCoords.getY() / pixelBounds.getHeight())
				* mathBounds.getHeight() + mathBounds.getY();
		return new Complex(mathX, mathY);
	}

	public String getFractal() {
		return f.toString();
	}
}
