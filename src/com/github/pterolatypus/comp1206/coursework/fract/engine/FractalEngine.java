package com.github.pterolatypus.comp1206.coursework.fract.engine;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
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
	private Rectangle2D.Double mathBounds;

	private BufferedImage graphImage;
	private Coloring colorScheme;

	public FractalEngine(Fractal f, GraphPanel parent) {
		super();
		this.f = f;
		this.parent = parent;
		this.setPriority(this.getPriority() - 1);
	}

	// As long as the thread hasn't been terminated, continually loops checking
	// if any tasks are waiting to be executed.
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

	public synchronized BufferedImage getImage() {
		return graphImage;
	}

	public void updateMathBounds(Rectangle2D.Double mathBounds) {
		this.mathBounds = mathBounds;
	}

	public void setColorScheme(Coloring c) {
		colorScheme = c;
		f.setColoring(c);
	}

	/**
	 * Actually updates the stored {@link BufferedImage} to be a pixel-for-pixel
	 * render of the {@link Fractal} this engine is assigned
	 * 
	 * @param pixelBounds
	 *            The size of the {@link BufferedImage}
	 */
	public synchronized void updateImage(final Rectangle pixelBounds) {
		taskQueue.add(new Runnable() {
			public void run() {
				int width = (int) pixelBounds.getWidth();
				int height = (int) pixelBounds.getHeight();
				// If the pixelbounds are 0,0 (as can sometimes happen after
				// panel-switching) calculate a temporary 1x1 image to avoid errors
				BufferedImage im = new BufferedImage((width > 0) ? width : 1,
						(height > 0) ? height : 1, BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						// If the point being calculated is on the edge of the
						// image, make it black (I couldn't get swing borders to
						// work properly, so this renders a 1px border manually)
						if (x == 0 || y == 0 || x == width - 1
								|| y == height - 1) {
							im.setRGB(x, y, Color.BLACK.getRGB());
						} else {
							Complex mPoint = getMathCoords(new Point(x,y), pixelBounds);
							Color c = f.calculate(mPoint);
							im.setRGB(x, y, c.getRGB());
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

	/**
	 * Converts a pixel coordinate to a {@link Complex} coordinate using the given pixel-bounds
	 * @param pixelCoords The coordinate to convert
	 * @param pixelBounds The size of the pixel-space which generated the coordinate
	 * @return the {@link Complex} number represented by the given point in the given pixel-space
	 */
	public Complex getMathCoords(Point pixelCoords, Rectangle pixelBounds) {
		double mathX = (pixelCoords.getX() / pixelBounds.getWidth())
				* mathBounds.getWidth() + mathBounds.getX();
		double mathY = (pixelCoords.getY() / pixelBounds.getHeight())
				* mathBounds.getHeight() + mathBounds.getY();
		return new Complex(mathX, mathY);
	}

	/**
	 * @return The name of the {@link Fractal} this engine is currently tasked with calculating
	 */
	public String getFractal() {
		return f.toString();
	}
}
