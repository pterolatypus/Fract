package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;

/**
 * 
 * A JPanel set up to accept exactly one GraphPanel and allow that panel to be
 * referenced and painted on screen, whilst being interchangeable. Part of the
 * panel-swapping implementation.
 * 
 * Most methods only call the same methods in the child panel.
 * 
 * @author James
 *
 */
public class GraphContainer extends DraggablePanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private GraphPanel panel;

	/**
	 * Instantiates a new empty GraphContainer
	 */
	public GraphContainer() {
		super();
		setLayout(new GridLayout(1, 1));
	}

	/**
	 * See {@link GraphPanel}
	 */
	public Complex getMathCoords(Point p) {
		return panel.getMathCoords(p);
	}

	/**
	 * @return The GraphPanel object stored by this container
	 */
	public GraphPanel getPanel() {
		return this.panel;
	}

	/**
	 * See {@link GraphPanel}
	 */
	public void updateFractal(Complex p) {
		panel.updateFractal(p);
	}

	/**
	 * See {@link GraphPanel}
	 */
	public void zoomOut() {
		panel.zoomOut();
	}

	/**
	 * See {@link GraphPanel}
	 */
	public void resetZoom() {
		panel.resetZoom();
	}

	/**
	 * This method should not be used to place objects in the container. Use
	 * setPanel(GraphPanel) instead.
	 */
	@Deprecated
	@Override
	public Component add(Component p) {
		if (panel == null && p instanceof GraphPanel) {
			this.panel = (GraphPanel) p;
			return super.add(p);
		} else {
			throw new IllegalArgumentException(
					"GraphContainers can only contain a single GraphPanel! You should be using setPanel(GraphPanel)");
		}
	}

	/**
	 * Stores the specified GraphPanel inside this container, removing any
	 * existing panel and returning it.
	 * 
	 * @param pnlNew
	 *            The new panel to store in this container
	 * @return The previous panel (if any) stored by this container
	 */
	public GraphPanel setPanel(GraphPanel pnlNew) {
		GraphPanel pnlOld = this.panel;
		this.panel = null;
		if (pnlOld != null) {
			this.remove(pnlOld);
		}
		this.add(pnlNew);
		return pnlOld;
	}

	/**
	 * See {@link GraphPanel}
	 */
	public void setMathBounds(Rectangle r) {
		panel.setMathBounds(r);
	}

	/**
	 * See {@link GraphPanel}
	 */
	public void setColorScheme(Coloring c) {
		panel.setColorScheme(c);
		repaint();
	}

	/**
	 * See {@link GraphPanel}
	 */
	public String getFractal() {
		return panel.getFractal();
	}

	/**
	 * Overrides the default validate method to ensure the size of the child
	 * panel. Probably not necessary.
	 */
	@Override
	public void validate() {
		panel.setBounds(0, 0, getWidth(), getHeight());
		super.validate();
	}

}
