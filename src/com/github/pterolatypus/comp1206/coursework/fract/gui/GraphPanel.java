package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.github.pterolatypus.comp1206.coursework.fract.engine.FractalEngine;
import com.github.pterolatypus.comp1206.coursework.fract.engine.NotifyListener;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;

public class GraphPanel extends DraggablePanel implements NotifyListener {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	private FractalEngine graphEngine;

	public GraphPanel(Fractal f) {
		this.graphEngine = new FractalEngine(f, this);
		graphEngine.start();

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
		g.drawImage(graphEngine.getImage(), 0, 0, this);
	}
	
	public void updateImage(Rectangle pixelBounds, Fractal f) {
		graphEngine.updateImage(pixelBounds, f);
	}
	
	public void setMathBounds(Rectangle pixelBounds) {
		Point2D.Double tl = getMathCoords(pixelBounds.getLocation());
		Point2D.Double br = getMathCoords(new Point((int)(pixelBounds.getX()+pixelBounds.getWidth()), (int)(pixelBounds.getY()+pixelBounds.getHeight())));
		graphEngine.updateMathBounds(new Rectangle2D.Double(tl.getX(), tl.getY(), br.getX()-tl.getX(), br.getY()-tl.getY()));
	}

	@Override
	public void notifyOf() {
		this.repaint();
	}

	public Point2D.Double getMathCoords(Point pixelCoords) {
		return graphEngine.getMathCoords(pixelCoords, new Rectangle(getWidth(), getHeight()));
	}
	
}
