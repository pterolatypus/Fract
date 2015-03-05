package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D.Double;

import javax.swing.JTextField;

public class CoordinatePanel extends CursorPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	public CoordinatePanel(final GraphPanel parent) {
		super(2);
		
		this.setBackground(new Color(0.3f,0.3f,0.3f,0.6f));
		
		this.setPreferredSize(new Dimension(96,32));
		
		final JTextField txtX = new JTextField(3);
		final JTextField txtY = new JTextField(3);
		
		txtX.setEditable(false);
		txtY.setEditable(false);
		
		txtX.setText("x=");
		txtY.setText("y=");
		
		this.add(txtX);
		this.add(txtY);
		
		parent.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Double p = parent.getMathCoords(new Point(e.getX(), e.getY()));
				System.out.println(String.valueOf(p.getX()));
				txtX.setText("x="+String.valueOf(p.getX()));
				System.out.println(String.valueOf(p.getY()));
				txtY.setText("y="+String.valueOf(p.getY()));
			}
		});
	}
}
