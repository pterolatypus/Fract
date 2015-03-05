package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class CursorPanel extends JPanel {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private boolean bInit = false;
	
	public CursorPanel() {
		super();
		
		this.setPreferredSize(new Dimension(64,48));
		
		this.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				if (!bInit) {
					getParent().addMouseMotionListener(this);
					bInit = true;
					removeMouseMotionListener(this);
				} else {
					setBounds(e.getX(),e.getY(),getWidth(),getHeight());
				}
			}
		});
	}
	
}
