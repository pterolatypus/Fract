package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;

public class CoordinatePanel extends CursorPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	private JTextField txtX;
	private JTextField txtY;
	
	public CoordinatePanel(final GraphPanel parent) {
		super(2);
		
		this.setBackground(new Color(0.3f,0.3f,0.3f,0.6f));
		
		this.setPreferredSize(new Dimension(96,56));
		
		txtX = new JTextField(5);
		txtY = new JTextField(5);
		
		txtX.setEditable(false);
		txtY.setEditable(false);
		
		JLabel lblX = new JLabel("Re:");
		lblX.setForeground(Color.WHITE);
		JLabel lblY = new JLabel("Im:");
		lblY.setForeground(Color.WHITE);
		
		this.add(lblX);
		this.add(txtX);
		this.add(lblY);
		this.add(txtY);
	}
	
	public void setCoordinates(Complex coords) {
		String x = String.valueOf(coords.getReal());
		x = x.substring(0,(int)Math.min(((x.startsWith("-"))?8:7), x.length()));
		String y = String.valueOf(coords.getImaginary());
		y = y.substring(0,(int)Math.min(((y.startsWith("-"))?8:7),y.length()));
		txtX.setText(x);
		txtY.setText(y);
	}
	
	public MouseAdapter getListener(final GraphContainer parent) {
		final CoordinatePanel panel = this;
		MouseAdapter m = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Complex coords = parent.getMathCoords(e.getPoint());
				panel.setCoordinates(coords);
			}
		};
		return m;
		
	}
	

}
