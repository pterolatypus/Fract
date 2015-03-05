package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class DraggablePanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	// Defines a pixel border around the inside of the parent container where
	// this panel cannot be dragged
	private int borderOffset = 0;

	// Defines the distance within which the component must be to the edge of
	// the parent container to snap to the edge
	// If snapDistance <= borderOffset no snapping occurs
	private int snapDistance = 0;

	/**
	 * Default and only constructor for intialising a new draggable JPanel.
	 * Attribute manipulation is done in separate methods.
	 */
	public DraggablePanel() {

		super();

		// The listener which will handle the dragging
		MouseAdapter m = new MouseAdapter() {

			// Where the mouse was clicked relative to the component
			private Point relative;

			@Override
			public void mouseDragged(MouseEvent e) {
				int newx = (int) (getX() + e.getX() - relative.getX());
				int newy = (int) (getY() + e.getY() - relative.getY());
				Point target = getInBounds(new Point(newx, newy),
						new Rectangle(getParent().getWidth(), getParent()
								.getHeight()));
				setBounds((int) target.getX(), (int) target.getY(), getWidth(),
						getHeight());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				relative = e.getPoint();
			}
		};
		this.addMouseMotionListener(m);
		this.addMouseListener(m);
	}

	/**
	 * Finds the nearest point to the specified destination which leaves the
	 * whole panel inside the specified bounds, respecting borderOffset and
	 * snapDistance
	 * 
	 * @param destination
	 *            The intended point to be normalised
	 * @param bounds
	 *            The rectangular boundary to normalise the point into. Assumed
	 *            to begin at 0,0
	 * @return A new Point instance of the point where the panel should be
	 *         placed
	 */
	public Point getInBounds(Point destination, Rectangle bounds) {

		// The point we want to move to
		int newX = (int) destination.getX();
		int newY = (int) destination.getY();

		// The maximum x and y the corner can be at within the bounds
		int widthBound = (int) (bounds.getWidth() - getWidth() - borderOffset);
		int heightBound = (int) (bounds.getHeight() - getHeight() - borderOffset);

		newX = (newX < borderOffset + snapDistance) ? borderOffset : newX;
		newX = (newX > widthBound - snapDistance) ? widthBound : newX;

		newY = (newY < borderOffset + snapDistance) ? borderOffset : newY;
		newY = (newY > heightBound - snapDistance) ? heightBound : newY;

		return new Point(newX, newY);
	}

	/**
	 * Sets the border offset for this panel; the minimum distance from the
	 * edges of the parent component that the panel will attempt to maintain at
	 * all times.
	 * 
	 * @param newBorderOffset
	 *            The new border offset value, in pixels.
	 * @return This, for method chaining.
	 */
	public DraggablePanel setBorderOffset(int newBorderOffset) {
		this.borderOffset = newBorderOffset;
		return this;
	}

	/**
	 * Sets the snap distance for this panel; the minimum distance from the
	 * parent's edges beyond which the panel will 'snap' to the edge (respecting
	 * borderOffset)
	 * 
	 * @param newSnapDistance
	 *            The new snap distance value, in pixels.
	 * @return This, for method chaining.
	 */
	public DraggablePanel setSnapDistance(int newSnapDistance) {
		this.snapDistance = newSnapDistance;
		return this;
	}

}
