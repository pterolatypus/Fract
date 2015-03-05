package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 3254183707783343297L;

	private AppEngine eng;

	public AppWindow(final AppEngine eng) {
		super("Fractal Visualiser");
		this.eng = eng;
		//GUI Stuff to come
		this.setBounds(0,0,512,512);
		
		InputMap inputMap = ((JComponent) getContentPane()).getInputMap();
		inputMap.put(KeyStroke.getKeyStroke('f'), "action_f_pressed");
		inputMap.put(KeyStroke.getKeyStroke('g'), "action_g_pressed");
		
		ActionMap actionMap = ((JComponent) getContentPane()).getActionMap();
		actionMap.put("action_f_pressed", new AbstractAction() {
			private static final long serialVersionUID = 5315061756923696165L;
			public void actionPerformed(ActionEvent e) {
				queueTask(new Task() {
					@Override
					public void run(Object... args) {
						System.out.println("Clicked!");
					}
				});
			}
		});
		

		JButton btn = new JButton("clickme");
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				queueTask(new Task() {
					@Override
					public void run(Object... args) {
						System.out.println("Clicked!");
					}
				});
			}
		});
		
		this.setLayout(new BorderLayout());
		
		GraphPanel pnlGraph = new GraphPanel(new Graph(Fractal.MANDELBROT));
		
		this.add(pnlGraph, BorderLayout.CENTER);
		this.add(btn, BorderLayout.EAST);
		
		WindowAdapter w = new WindowAdapter(){
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					eng.terminate();
				}
			};
		this.addWindowListener(w);
		
	}

	public void queueTask(Task t) {
		eng.queueTask(t);
	}

	class GraphPanel extends JPanel {

		private static final long serialVersionUID = 3491027712033512993L;

		private Graph graph;
		private BufferedImage im;

		public GraphPanel(Graph g) {
			this.graph = g;
		}

		@Override
		public void print(Graphics g) {
			if (im == null || im.getHeight() != this.getHeight()
					|| im.getWidth() != this.getWidth()) {
				queueTask(new Task() {
					public void run(Object... args) {
						graph.calculate(new Rectangle(getWidth(), getHeight()));
					}
				});
				im = graph.getImage();
			}
			g.drawImage(im, 0, 0, this);
			System.out.println("Panel painted");
		}

	}

}
