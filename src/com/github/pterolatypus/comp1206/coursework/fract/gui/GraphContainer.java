package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;


public class GraphContainer extends DraggablePanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private GraphPanel panel;
	
	public GraphContainer() {
		super();
		setLayout(new GridLayout(1,1));
	}
	
	public Complex getMathCoords(Point p) {
		return panel.getMathCoords(p);
	}
	
	public GraphPanel getPanel() {
		return this.panel;
	}
	
	public void updateFractal(Complex p) {
		panel.updateFractal(p);
	}
	
	public void zoomOut() {
		panel.zoomOut();
	}
	
	public void resetZoom() {
		panel.resetZoom();
	}
	
	@Override
	public Component add(Component p) {
		if (panel == null && p instanceof GraphPanel) {
			this.panel = (GraphPanel) p;
			return super.add(p);
		} else {
			throw new IllegalArgumentException("GraphContainers can only contain a single GraphPanel! You should be using setPanel(GraphPanel)");
		}
	}
	
	public GraphPanel setPanel(GraphPanel pnlNew) {
		GraphPanel pnlOld = this.panel;
		this.panel = null;
		if (pnlOld != null) {
			this.remove(pnlOld);
		}
		this.add(pnlNew);
		return pnlOld;
	}
	
	public void setMathBounds(Rectangle r) {
		panel.setMathBounds(r);
	}
	
	public void setColorScheme(Coloring c) {
		panel.setColorScheme(c);
		repaint();
	}
	
	public String getFractal() {
		return panel.getFractal();
	}
	
	@Override
	public void validate() {
		panel.setBounds(0,0,getWidth(),getHeight());
		super.validate();
	}
	
}
