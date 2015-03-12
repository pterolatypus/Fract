package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring.LogSmoothRepeating;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;
import com.github.pterolatypus.comp1206.coursework.fract.math.MathUtil;

public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	// Object to store the 'user selected point'
	class ComplexCursor {
		private Complex point = null;

		public void setPoint(Complex p) {
			this.point = p;
		}

		public Complex getPoint() {
			return point;
		}

		public String toString() {
			return "Selected point: " + MathUtil.round(getPoint().getReal(), 3)
					+ "+" + MathUtil.round(-getPoint().getImaginary(), 3) + "i";
		}
	}

	// "
	ComplexCursor cursor = new ComplexCursor();

	// Flag for which mode we're in; click-to-select (true) or live update
	// (false)
	private boolean bCursorMode = false;

	// Favourite points, with names.
	private Map<String, Complex> favourites = new HashMap<String, Complex>();

	// Possible configurable fractals
	private static Map<String, Fractal> fractals = new HashMap<String, Fractal>();

	// Statically populate the list of renderable fractals.
	static {
		fractals.put(Fractal.MANDELBROT.toString(), Fractal.MANDELBROT);
		fractals.put(new Fractal.Julia(new Complex(0, 0)).toString(),
				new Fractal.Julia(new Complex(0, 0)));
		fractals.put(Fractal.BURNING_SHIP.toString(), Fractal.BURNING_SHIP);
		fractals.put(Fractal.TRICORN.toString(), Fractal.TRICORN);
		fractals.put(new Fractal.Multibrot(5).toString(),
				new Fractal.Multibrot(5));
	}

	// Map of possible colouring algorithms
	private static Map<String, Class<? extends Coloring>> colorschemes = new HashMap<String, Class<? extends Coloring>>();

	// Statically populate the list of colouring algorithms
	static {
		colorschemes.put("Smooth Coloring", Coloring.LogSmooth.class);
		colorschemes.put("Smooth Double-Ended",
				Coloring.LogSmoothDoubleEnded.class);
		colorschemes.put("Smooth Double-Ended Repeating",
				LogSmoothRepeating.class);
	}

	// The default palette of colours to iterate through when choosing pixel
	// colour
	private static Color[] sPalette = new Color[] { Color.red, Color.orange,
			Color.white };

	// The two actual panels that will appear on screen and abstractly contain
	// the GraphPanels
	private GraphContainer pnlCorner = new GraphContainer();
	private GraphContainer pnlMain = new GraphContainer();

	// Pretty much everything happens here.
	public AppWindow() throws InstantiationException, IllegalAccessException,
			IOException {
		// Boilerplate setup
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		this.setBounds(0, 0, 100, 100);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());

		// Set up file handling and load preexisting favourites
		{
			File favouritesFile = new File("favourites.csv");
			if (!favouritesFile.exists()) {
				try {
					favouritesFile.createNewFile();
					System.out.println("No favourites file found, creating new file.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				BufferedReader favouritesReader = new BufferedReader(
						new FileReader(favouritesFile));
				while (favouritesReader.ready()) {
					String line = favouritesReader.readLine();
					Pattern p = Pattern
							.compile("(.*),(-?\\d+(\\.\\d+))\\+(-?\\d+(\\.\\d+))i");
					Matcher m = p.matcher(line);
					if (m.matches()) {
						String name = m.group(1);
						double real = Double.parseDouble(m.group(2));
						double imaginary = Double.parseDouble(m.group(3));
						favourites.put(name, new Complex(real, imaginary));
						System.out.println("Parsed favourites line:");
						System.out.println(line);
					} else {
						System.err
								.println("An invalid favourites line was detected:");
						System.err.println(line);
					}
				}
				favouritesReader.close();
			}
		}

		// Scoped to stop me accidentally referencing the GraphPanels elsewhere.
		// GraphPanels shouldn't be referenced, they should remain anonymous to
		// make panel-switching possible
		{
			GraphPanel pnlMandelbrot = new GraphPanel(Fractal.MANDELBROT);
			pnlMandelbrot.setColorScheme(colorschemes
					.get("Smooth Double-Ended").newInstance()
					.setProperty("colorPalette", sPalette));
			GraphPanel pnlJulia = new GraphPanel(new Fractal.Julia(new Complex(
					0, 0)));
			pnlJulia.setColorScheme(colorschemes.get("Smooth Double-Ended")
					.newInstance().setProperty("colorPalette", sPalette));

			pnlMain.setPanel(pnlMandelbrot);
			pnlCorner.setPanel(pnlJulia);
		}

		final OverlayPanel pnlOverlay = new OverlayPanel(new Dimension(
				getWidth(), getHeight()));
		this.setGlassPane(pnlOverlay);

		final InfoPanel pnlInfo = new InfoPanel();
		this.add(pnlInfo, BorderLayout.SOUTH);

		this.add(pnlMain, BorderLayout.CENTER);

		pnlCorner.setBounds(15, 15, 256, 256);
		pnlCorner.setPreferredSize(new Dimension(256, 256));
		pnlCorner.setBorderOffset(15);
		pnlCorner.setSnapDistance(30);

		pnlOverlay.add(pnlCorner);

		final ContextMenu m = new ContextMenu() {
			private static final long serialVersionUID = AppWindow.serialVersionUID;

			private JMenuItem itemSwap, itemDrop, itemLift, itemSave, itemLoad,
					itemConfig;

			{
				itemSwap = new JMenuItem("Swap Panels");
				itemSwap.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GraphPanel tmp = pnlMain.setPanel(pnlCorner.getPanel());
						pnlCorner.setPanel(tmp);

						pnlCorner.resetZoom();
						validate();
					}
				});
				addMenuItem(itemSwap, KeyEvent.VK_W);

				itemDrop = new JMenuItem("Drop Cursor");
				itemDrop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dropCursor();
					}
				});
				addMenuItem(itemDrop, KeyEvent.VK_D);

				itemLift = new JMenuItem("Remove Cursor");
				itemLift.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						bCursorMode = false;
						cursor.setPoint(pnlMain.getMathCoords(MouseInfo
								.getPointerInfo().getLocation()));
						pnlCorner.updateFractal(cursor.getPoint());
					}
				});
				addMenuItem(itemLift, KeyEvent.VK_R);

				itemSave = new JMenuItem("Save Favourite");
				itemSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						bCursorMode = true;
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
						dlg.add(new JLabel(
								"Enter a name to save this favourite as."), c);
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
								final String name = txtName.getText()
										+ "("
										+ MathUtil.round(cursor.getPoint()
												.getReal(), 3)
										+ " + "
										+ MathUtil.round(cursor.getPoint()
												.getImaginary(), 3) + "i)";
								if (!favourites.containsKey(name)) {
									favourites.put(name, cursor.getPoint());
									try {
										saveFavourites();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
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
									dlgYN.add(
											new JLabel(
													"That name already exists. Overwrite?"),
											c);
									c.gridwidth = 1;
									c.gridy = 1;
									c.gridx = 0;
									JButton btnYes = new JButton("Yes");
									c.gridx = 1;
									JButton btnNo = new JButton("No");
									btnYes.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent e) {
											favourites.put(name,
													cursor.getPoint());
											try {
												saveFavourites();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											dlgYN.dispatchEvent(new WindowEvent(
													dlgYN,
													WindowEvent.WINDOW_CLOSING));
											dlg.dispatchEvent(new WindowEvent(
													dlg,
													WindowEvent.WINDOW_CLOSING));
										}
									});
									btnNo.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent e) {
											dlgYN.dispatchEvent(new WindowEvent(
													dlgYN,
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
				addMenuItem(itemSave, KeyEvent.VK_S);

				itemLoad = new JMenuItem("Load Favourite");
				itemLoad.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						final JDialog dlg = new JDialog();
						dlg.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();

						JLabel lblLoad = new JLabel(
								"Select a favourite to load");

						c.insets = new Insets(5, 5, 5, 5);
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
								if ((s = (String) cbxFavourites
										.getSelectedItem()) != null) {
									bCursorMode = true;
									cursor.setPoint(favourites.get(s));
									pnlCorner.updateFractal(cursor.getPoint());
									pnlCorner.repaint();
									dlg.dispatchEvent(new WindowEvent(dlg,
											WindowEvent.WINDOW_CLOSING));
								}
							}
						});
						JButton btnCancel = new JButton("Cancel");
						btnCancel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								dlg.dispatchEvent(new WindowEvent(dlg,
										WindowEvent.WINDOW_CLOSING));
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
				addMenuItem(itemLoad, KeyEvent.VK_O);

				itemConfig = new JMenuItem("Configure Fractal");
				itemConfig.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						final JDialog dlg = new JDialog();
						dlg.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();

						JLabel lblChoosePanel = new JLabel(
								"Select a panel to configure");

						c.gridwidth = 2;
						c.gridy = 0;
						dlg.add(lblChoosePanel, c);

						final JRadioButton btnPnlMain = new JRadioButton(
								"Main Panel");
						final JRadioButton btnPnlCorner = new JRadioButton(
								"Corner Panel");
						ButtonGroup grp = new ButtonGroup();
						grp.add(btnPnlMain);
						grp.add(btnPnlCorner);

						c.gridwidth = 1;
						c.gridy++;
						btnPnlMain.setSelected(true);
						dlg.add(btnPnlMain, c);
						dlg.add(btnPnlCorner, c);

						JLabel lblChooseFractal = new JLabel(
								"Choose a fractal to display");

						c.gridwidth = 2;
						c.gridy++;
						dlg.add(lblChooseFractal, c);

						final JComboBox<String> cbxFractals = new JComboBox<String>();
						for (String s : fractals.keySet()) {
							cbxFractals.addItem(s);
						}

						cbxFractals.setSelectedItem(pnlMain.getFractal());

						ItemListener l = new ItemListener() {
							@Override
							public void itemStateChanged(ItemEvent e) {
								if (e.getStateChange() == ItemEvent.SELECTED) {
									if (btnPnlMain.isSelected()) {
										cbxFractals.setSelectedItem(pnlMain
												.getFractal());
									} else {
										cbxFractals.setSelectedItem(pnlCorner
												.getFractal());
									}
								}
							}
						};

						btnPnlMain.addItemListener(l);
						btnPnlCorner.addItemListener(l);

						c.gridy++;
						dlg.add(cbxFractals, c);

						JLabel lblChooseColor = new JLabel(
								"Choose a coloring system to use");

						c.gridwidth = 2;
						c.gridy++;
						dlg.add(lblChooseColor, c);

						final JComboBox<String> cbxColoring = new JComboBox<String>();
						for (String s : colorschemes.keySet()) {
							cbxColoring.addItem(s);
						}

						c.gridy++;
						dlg.add(cbxColoring, c);

						JButton btnOk = new JButton("Ok");
						JButton btnCancel = new JButton("Cancel");

						c.gridwidth = 1;
						c.gridy++;
						JLabel lblNumIterations = new JLabel(
								"Enter maximum iterations:");
						dlg.add(lblNumIterations, c);

						c.fill = GridBagConstraints.HORIZONTAL;
						final JTextField txtIterations = new JTextField();
						txtIterations.setText(String.valueOf(Fractal
								.getMaxIterations()));
						dlg.add(txtIterations, c);

						c.fill = GridBagConstraints.NONE;
						c.gridy++;
						dlg.add(btnOk, c);
						dlg.add(btnCancel, c);

						txtIterations.addKeyListener(new KeyAdapter() {
							String last = txtIterations.getText();

							@Override
							public void keyReleased(KeyEvent e) {
								if (txtIterations.getText() != null
										&& txtIterations.getText().equals("")) {
									return;
								} else {
									try {
										Integer.parseInt(txtIterations
												.getText());
										last = txtIterations.getText();
									} catch (IllegalArgumentException ex) {
										txtIterations.setText(last);
									}
								}
							}
						});

						btnOk.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String sel;
								String col;
								if ((sel = (String) cbxFractals
										.getSelectedItem()) != null
										&& (col = (String) cbxColoring
												.getSelectedItem()) != null) {
									if (btnPnlMain.isSelected()) {
										GraphPanel pnl = new GraphPanel(
												fractals.get(sel));
										pnl.setBounds(0, 0, 512, 512);
										Coloring c = null;
										try {
											c = colorschemes
													.get(col)
													.newInstance()
													.setProperty(
															"colorPalette",
															sPalette);
										} catch (InstantiationException e1) {
											e1.printStackTrace();
										} catch (IllegalAccessException e1) {
											e1.printStackTrace();
										}
										pnl.setColorScheme(c);
										pnlMain.setPanel(pnl);
									} else if (btnPnlCorner.isSelected()) {
										GraphPanel pnl = new GraphPanel(
												fractals.get(sel));
										Coloring c = null;
										try {
											c = colorschemes
													.get(col)
													.newInstance()
													.setProperty(
															"colorPalette",
															sPalette);
										} catch (InstantiationException e1) {
											e1.printStackTrace();
										} catch (IllegalAccessException e1) {
											e1.printStackTrace();
										}
										pnl.setColorScheme(c);
										pnlCorner.setPanel(pnl);
									} else {
										return;
									}
									Fractal.setMaxIterations(Integer
											.parseInt(txtIterations.getText()));
								}
								dlg.dispatchEvent(new WindowEvent(dlg,
										WindowEvent.WINDOW_CLOSING));
							}
						});
						btnCancel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								dlg.dispatchEvent(new WindowEvent(dlg,
										WindowEvent.WINDOW_CLOSING));
							}
						});

						dlg.setLocationRelativeTo(null);
						dlg.setUndecorated(true);
						dlg.pack();
						dlg.setVisible(true);

					}
				});
				addMenuItem(itemConfig, KeyEvent.VK_F);
			}
		};

		pnlOverlay.add(m);

		MouseAdapter mouseTrackListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					m.show(pnlOverlay, e.getX(), e.getY());
				} else if (e.getButton() == MouseEvent.BUTTON1 && bCursorMode) {
					cursor.setPoint(pnlMain.getMathCoords(e.getPoint()));
					pnlInfo.setCoordinates(cursor.toString());
					pnlCorner.updateFractal(cursor.getPoint());
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (!bCursorMode) {
					cursor.setPoint(pnlMain.getMathCoords(e.getPoint()));
					pnlInfo.setCoordinates(cursor.toString());
					pnlCorner.updateFractal(cursor.getPoint());
				}
			}

		};

		pnlOverlay.addMouseListener(mouseTrackListener);
		pnlOverlay.addMouseMotionListener(mouseTrackListener);

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

		pnlOverlay.addMouseMotionListener(zoomListener);
		pnlOverlay.addMouseListener(zoomListener);

		pnlOverlay.setVisible(true);

	}

	public void dropCursor() {
		bCursorMode = true;
		pnlCorner.updateFractal(cursor.getPoint());
	}

	public void saveFavourites() throws IOException {
		PrintStream favouritesWriter = new PrintStream(new FileOutputStream(
				new File("favourites.csv")));
		for (String s : favourites.keySet()) {
			String lineOut = s + "," + favourites.get(s).toString();
			favouritesWriter.println(lineOut);
		}
		favouritesWriter.close();
	}
}
