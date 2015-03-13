package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;

import com.github.pterolatypus.comp1206.coursework.fract.engine.FractalEngine;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

/**
 * Acts as an interface between FractalEngine and the GUI, handles painting of
 * images, zooming and assists conversion between pixel-space and graph-space
 * 
 * @author James
 *
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	private FractalEngine graphEngine;
	private Stack<Rectangle2D.Double> stackZoomFrames = new Stack<Rectangle2D.Double>();

	/**
	 * Instantiates a new GraphPanel with the specified {@link Fractal} as its initial fractal.
	 * @param f
	 */
	public GraphPanel(Fractal f) {
		super();
		this.graphEngine = new FractalEngine(f, this);
		graphEngine.start();
		//Set the initial mathematical bounds of the graph
		stackZoomFrames.push(new Rectangle2D.Double(-2d, -1.6d, 4d, 3.2d));
		graphEngine.updateMathBounds(stackZoomFrames.peek());

		// Force the entire graph to be recalculated when the window is resized,
		// as the resize will alter the graph resolution
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				graphEngine.interrupt();
				graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
				repaint();
			}
		});
	}

	//Ensures the stored image is up-to-date and then paints it onto the screen.
	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
		BufferedImage i = graphEngine.getImage();
		gr.drawImage(i, 0, 0, this);
	}

	public void updateImage(Rectangle pixelBounds, Fractal f) {
		graphEngine.updateImage(pixelBounds, f);
	}

	//Converts the given pixel-space rectangle into a graph-space rectangle and updates the fractal bounds with that.
	public void setMathBounds(Rectangle pixelBounds) {
		Complex tl = getMathCoords(pixelBounds.getLocation());
		Complex br = getMathCoords(new Point(
				(int) (pixelBounds.getX() + pixelBounds.getWidth()),
				(int) (pixelBounds.getY() + pixelBounds.getHeight())));
		Rectangle2D.Double mathBounds = new Rectangle2D.Double(tl.getReal(),
				tl.getImaginary(), br.getReal() - tl.getReal(),
				br.getImaginary() - tl.getImaginary());
		stackZoomFrames.push(mathBounds);
		graphEngine.updateMathBounds(mathBounds);
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
	}

	//Removes the current zoomframe from the stack and zooms to the next one up.
	public void zoomOut() {
		if (stackZoomFrames.size() > 1) {
			stackZoomFrames.pop();
		}
		graphEngine.updateMathBounds(stackZoomFrames.peek());
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
	}

	//Repeatedly zooms out until we reach the initial zoom level
	public void resetZoom() {
		while (stackZoomFrames.size() > 1) {
			zoomOut();
		}
	}

	/**
	 * Converts the given pixel coordinate point into a {@link Complex} point represented by that pixel on this graph
	 * @param pixelCoords The pixel coordinate point to convert
	 * @return The {@link Complex} point represented by that pixel
	 */
	public Complex getMathCoords(Point pixelCoords) {
		return graphEngine.getMathCoords(pixelCoords, new Rectangle(getWidth(),
				getHeight()));
	}

	/**
	 * Sets the colour scheme of this graph to the specified {@link Coloring} object
	 * @param c The new {@link Coloring} to use
	 */
	public void setColorScheme(Coloring c) {
		graphEngine.setColorScheme(c);
	}

	/**
	 * If this graph is a dependent fractal (i.e. Julia) updates its root point to the given {@Complex} point
	 * @param p the new {@Complex} root to use
	 */
	public void updateFractal(Complex p) {
		graphEngine.updateFractal(p);
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
	}

	/**
	 * @return The name of the {@Fractal} currently represented by this graph
	 */
	public String getFractal() {
		return graphEngine.getFractal();
	}

}
