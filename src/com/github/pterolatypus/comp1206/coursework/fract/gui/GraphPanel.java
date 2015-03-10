package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import javax.swing.JPanel;

import com.github.pterolatypus.comp1206.coursework.fract.engine.FractalEngine;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	private FractalEngine graphEngine;
	private Stack<Rectangle2D.Double> stackZoomFrames = new Stack<Rectangle2D.Double>();


	public GraphPanel(Fractal f) {
		super();
		this.graphEngine = new FractalEngine(f, this);
		graphEngine.start();
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

	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
		gr.drawImage(graphEngine.getImage(), 0, 0, this);
	}

	public void updateImage(Rectangle pixelBounds, Fractal f) {
		graphEngine.updateImage(pixelBounds, f);
	}

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

	public void zoomOut() {
		if (stackZoomFrames.size() > 1) {
			stackZoomFrames.pop();
		}
		graphEngine.updateMathBounds(stackZoomFrames.peek());
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
	}

	public void resetZoom() {
		while (stackZoomFrames.size() > 1) {
			zoomOut();
		}
	}

	public Complex getMathCoords(Point pixelCoords) {
		return graphEngine.getMathCoords(pixelCoords, new Rectangle(getWidth(),
				getHeight()));
	}

	public void setColorScheme(Coloring c) {
		graphEngine.setColorScheme(c);
	}

	public void updateFractal(Complex p) {
		graphEngine.updateFractal(p);
		graphEngine.updateImage(new Rectangle(getWidth(), getHeight()));
	}
	
	public String getFractal() {
		return graphEngine.getFractal();
	}

}
