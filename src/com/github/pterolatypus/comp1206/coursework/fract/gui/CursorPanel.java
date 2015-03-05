package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class CursorPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;
	
	private int offset;
	
	public CursorPanel(int offset) {
		super();
		this.offset = offset;
	}
	
	public CursorPanel() {
		this(0);
	}
	
	public void enable() {
		getParent().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				setBounds(e.getX()+offset,e.getY()+offset,getWidth(),getHeight());
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				setBounds(e.getX()+offset,e.getY()+offset,getWidth(),getHeight());
			}
		});
	}
	
}
