package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;
import com.github.pterolatypus.comp1206.coursework.fract.math.MathUtil;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	public static final String ACTION_SPACE_PRESSED = "action_space_pressed";

	private Complex cursor = null;
	private boolean bCursorMode = false;

	private Map<String, Complex> favourites = new HashMap<String, Complex>();
	
	public AppWindow() {
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		this.setBounds(0, 0, 100, 100);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());

		final GraphPanel pnlMandelbrot = new GraphPanel(Fractal.MANDELBROT);
		pnlMandelbrot.setColorScheme(new Coloring.LogSmooth(new Color[] {
				Color.red, Color.orange, Color.white }));
		final GraphPanel pnlJulia = new GraphPanel(new Fractal.Julia(
				new Complex(0, 0)));
		pnlJulia.setColorScheme(new Coloring.LogSmooth());

		final GraphContainer pnlMain = new GraphContainer();
		pnlMain.setPanel(pnlMandelbrot);
		pnlMain.setLayout(new GridLayout(1, 1));

		final GraphContainer pnlCorner = new GraphContainer();
		pnlCorner.setPanel(pnlJulia);
		pnlCorner.setLayout(new GridLayout(1, 1));

		final JPanel pnlOverlay = new JPanel();
		pnlOverlay.setLayout(null);
		pnlOverlay.setSize(getContentPane().getWidth(), getContentPane()
				.getHeight());

		final JPanel pnlInfo = new JPanel();
		pnlInfo.setLayout(new GridBagLayout());
		final JLabel coords = new JLabel();
		pnlInfo.add(coords);
		pnlInfo.setBackground(Color.white);
		add(pnlInfo, BorderLayout.SOUTH);

		this.setGlassPane(pnlOverlay);

		this.add(pnlMain, BorderLayout.CENTER);

		pnlCorner.setBounds(15, 15, 256, 256);
		pnlCorner.setPreferredSize(new Dimension(256, 256));
		pnlCorner.setBorderOffset(15);
		pnlCorner.setSnapDistance(30);

		pnlOverlay.add(pnlCorner);

		MouseAdapter zoomListener = new MouseAdapter() {
			Point p;

			@Override
			public void mouseDragged(MouseEvent e) {
				repaint();
				Graphics g = pnlOverlay.getGraphics();
				int x = (int) Math.min(e.getX(), p.getX());
				int y = (int) Math.min(e.getY(), p.getY());
				int width = (int) Math.abs(e.getX() - p.getX());
				int height = (int) Math.abs(e.getY() - p.getY());
				g.drawRect(x, y, width, height);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getPoint().distance(p) < 4) {
						return;
					}
					int x = (int) Math.min(e.getX(), p.getX());
					int y = (int) Math.min(e.getY(), p.getY());
					int width = (int) Math.abs(e.getX() - p.getX());
					int height = (int) Math.abs(e.getY() - p.getY());
					Rectangle r = new Rectangle(x, y, width, height);
					pnlMain.setMathBounds(r);
					// pnlGraphMain.
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					pnlMain.zoomOut();
				}
			}
		};

		final ContextMenu m = new ContextMenu();

		JMenuItem item = new JMenuItem("Swap Panels");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GraphPanel tmp = pnlMain.setPanel(pnlCorner.getPanel());
				pnlCorner.setPanel(tmp);

				pnlCorner.resetZoom();
				validate();
			}
		});
		m.addMenuItem(item, KeyEvent.VK_W);

		item = new JMenuItem("Drop Cursor");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bCursorMode = true;
				pnlCorner.updateFractal(cursor);
			}
		});
		m.addMenuItem(item, KeyEvent.VK_D);

		item = new JMenuItem("Remove Cursor");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bCursorMode = false;
				cursor = pnlMain.getMathCoords(MouseInfo.getPointerInfo()
						.getLocation());
				pnlCorner.updateFractal(cursor);
			}
		});
		m.addMenuItem(item, KeyEvent.VK_R);

		item = new JMenuItem("Save Favourite");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bCursorMode = true;
				cursor = pnlMain.getMathCoords(MouseInfo.getPointerInfo()
						.getLocation());
				final JDialog dlg = new JDialog();
				dlg.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(5, 5, 5, 5);
				c.gridwidth = 2;
				c.gridy = 0;
				final JButton btnSave = new JButton("Save");
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dlg.dispatchEvent(new WindowEvent(dlg,
								WindowEvent.WINDOW_CLOSING));
					}
				});
				final JTextField txtName = new JTextField(8);
				dlg.add(new JLabel("Enter a name to save this favourite as."),
						c);
				c.gridy = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				dlg.add(txtName, c);
				c.gridwidth = 1;
				c.gridy = 2;
				dlg.add(btnSave, c);
				dlg.add(btnCancel, c);
				txtName.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						btnSave.doClick();
					}
				});
				btnSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Complex x = cursor.clone();
						final String name = txtName.getText() + "(" + MathUtil.round(x.getReal(), 3) + " + " + MathUtil.round(x.getImaginary(), 3) + "i)";
						if (!favourites.containsKey(name)) {
							favourites.put(name, cursor);
							dlg.dispatchEvent(new WindowEvent(dlg,
									WindowEvent.WINDOW_CLOSING));
						} else {
							final JDialog dlgYN = new JDialog();
							dlgYN.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.insets = new Insets(5, 5, 5, 5);
							c.gridwidth = 2;
							c.gridy = 0;
							c.fill = GridBagConstraints.HORIZONTAL;
							dlgYN.add(new JLabel(
									"That name already exists. Overwrite?"), c);
							c.gridwidth = 1;
							c.gridy = 1;
							c.gridx = 0;
							JButton btnYes = new JButton("Yes");
							c.gridx = 1;
							JButton btnNo = new JButton("No");
							btnYes.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									favourites.put(name, cursor);
									dlgYN.dispatchEvent(new WindowEvent(dlgYN,
											WindowEvent.WINDOW_CLOSING));
									dlg.dispatchEvent(new WindowEvent(dlg,
											WindowEvent.WINDOW_CLOSING));
								}
							});
							btnNo.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									dlgYN.dispatchEvent(new WindowEvent(dlgYN,
											WindowEvent.WINDOW_CLOSING));
									dlgYN.pack();
									dlgYN.setVisible(true);
								}
							});
						}
					}
				});
				dlg.setUndecorated(true);
				dlg.pack();
				dlg.setLocationRelativeTo(null);
				dlg.setVisible(true);
			}
		});
		m.addMenuItem(item, KeyEvent.VK_S);

		item = new JMenuItem("Load Favourite");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JDialog dlg = new JDialog();
				dlg.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();

				JLabel lblLoad = new JLabel("Select a favourite to load");

				c.insets = new Insets(5,5,5,5);
				c.gridwidth = 2;
				c.gridy = 0;

				dlg.add(lblLoad, c);

				final JComboBox<String> cbxFavourites = new JComboBox<String>();
				for (String s : favourites.keySet()) {
					cbxFavourites.addItem(s);
				}

				c.gridy = 1;
				c.fill = GridBagConstraints.HORIZONTAL;

				dlg.add(cbxFavourites, c);

				JButton btnLoad = new JButton("Load");
				btnLoad.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String s;
						if ((s = (String) cbxFavourites.getSelectedItem()) != null) {
							bCursorMode = true;
							cursor = favourites.get(s);
							pnlCorner.updateFractal(cursor);
							pnlCorner.repaint();
							dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
						}
					}
				});
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
					}
				});
				
				c.fill = GridBagConstraints.NONE;
				c.gridwidth = 1;
				c.gridy = 2;

				dlg.add(btnLoad, c);
				dlg.add(btnCancel, c);

				dlg.setLocationRelativeTo(null);
				dlg.setUndecorated(true);
				dlg.pack();
				dlg.setVisible(true);
			}
		});
		m.addMenuItem(item, KeyEvent.VK_O);
		
		pnlOverlay.add(m);

		pnlOverlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					m.show(pnlOverlay, e.getX(), e.getY());
				} else if (e.getButton() == MouseEvent.BUTTON1 && bCursorMode) {
					cursor = pnlMain.getMathCoords(e.getPoint());
					pnlCorner.updateFractal(cursor);
				}
			}
		});

		pnlOverlay.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (!bCursorMode && !m.isVisible()) {
					cursor = pnlMain.getMathCoords(e.getPoint());
					coords.setText("Selected point: "
							+ MathUtil.round(cursor.getReal(), 3) + "+"
							+ MathUtil.round(-cursor.getImaginary(), 3) + "i");
					pnlCorner.updateFractal(cursor);
				}
			}
		});

		pnlOverlay.setOpaque(false);
		pnlOverlay.setVisible(true);

		pnlOverlay.addMouseMotionListener(zoomListener);
		pnlOverlay.addMouseListener(zoomListener);

	}
}
