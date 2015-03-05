package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class DraggablePanel extends JPanel {

	private static final long serialVersionUID = 8100130353703535801L;

	private Point relative;
	
	public DraggablePanel() {
		super();
		setBackground(Color.RED);
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int newx = (int)(e.getX()-0);
				int newy = (int)(e.getY()-0);
				setBounds(newx,newy,getWidth(), getHeight());
			}
		});
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				relative = e.getPoint();
			}
		});
	}
	
}
