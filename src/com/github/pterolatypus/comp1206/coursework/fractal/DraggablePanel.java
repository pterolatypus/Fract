package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class DraggablePanel extends JPanel {

	private static final long serialVersionUID = AppWindow.getSVUID();

	// Defines a pixel border around the inside of the parent container where
	// this panel cannot be dragged
	private int borderOffset = 5;

	// Defines the distance within which the component must be to the edge of
	// the parent container to snap to the edge
	// If snapDistance <= borderOffset no snapping occurs
	private int snapDistance = 15;

	public DraggablePanel(final Container parent) {

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
						new Rectangle(parent.getWidth(), parent.getHeight()));
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

	public Point getInBounds(Point corner, Rectangle bounds) {

		// The point we want to move to
		int newX = (int) corner.getX();
		int newY = (int) corner.getY();

		// The maximum x and y the corner can be at within the bounds
		int widthBound = (int) (bounds.getWidth() - getWidth() - borderOffset);
		int heightBound = (int) (bounds.getHeight() - getHeight() - borderOffset);

		newX = (newX < borderOffset + snapDistance) ? borderOffset : newX;
		newX = (newX > widthBound - snapDistance) ? widthBound : newX;

		newY = (newY < borderOffset + snapDistance) ? borderOffset : newY;
		newY = (newY > heightBound - snapDistance) ? heightBound : newY;

		return new Point(newX, newY);
	}

	public DraggablePanel pSetBorderOffset(int newBorderOffset) {
		this.borderOffset = newBorderOffset;
		return this;
	}

	public DraggablePanel pSetSnapDistance(int newSnapDistance) {
		this.snapDistance = newSnapDistance;
		return this;
	}

	public DraggablePanel pSetBounds(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
		return this;
	}

	public DraggablePanel pSetPreferredSize(Dimension size) {
		setPreferredSize(size);
		return this;
	}

}
