package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Point;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;


public class GraphContainer extends DraggablePanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private GraphPanel panel;
	
	public GraphContainer(GraphPanel pnl) {
		super();
		this.panel = pnl;
	}
	
	public Complex getMathCoords(Point p) {
		return panel.getMathCoords(p);
	}
	
	public void updateFractal(Complex p) {
		panel.updateFractal(p);
	}
	
}
