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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring.LogSmoothRepeating;
import com.github.pterolatypus.comp1206.coursework.fract.math.Complex;
import com.github.pterolatypus.comp1206.coursework.fract.math.Fractal;
import com.github.pterolatypus.comp1206.coursework.fract.math.MathUtil;

/**
 * 
 * The frame that contains the program (mostly).
 * 
 * @author James
 *
 */
public class AppWindow extends JFrame {

	public static final long serialVersionUID = 0L;

	/**
	 * 
	 * A data object to store the 'user selected point' Exists mostly to
	 * override the toString method.
	 * 
	 * @author James
	 *
	 */
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

	ComplexCursor cursor = new ComplexCursor();

	// Flag for which mode we're in; click-to-select (true) or live update
	// (false)
	private boolean bCursorMode = false;

	// Favourite points, with names.
	private Map<String, Complex> favourites = new HashMap<String, Complex>();

	// Possible configurable fractals
	private static Map<String, Fractal> fractals = new HashMap<String, Fractal>();

	// Statically populate the list of renderable fractals.
	// A planned feature was to allow the loading of user-defined fractal
	// iterations
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
	// Stored as classes, a planned feature was to allow the loading of
	// user-defined algorithms
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
	// NOTE: CODE FOLDING IS HIGHLY RECOMMENDED!
	// Eclipse: Ctrl+Shift+(Numpad Divide) to fold all.
	public AppWindow() throws InstantiationException, IllegalAccessException,
			IOException {
		// Boilerplate setup
		super("Fract: Interactive Fractal Visualizer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		this.setBounds(0, 0, 100, 100);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());

		// Set up file handling and load pre-existing favourites
		{
			File favouritesFile = new File("favourites.csv");
			if (!favouritesFile.exists()) {
				try {
					favouritesFile.createNewFile();
					System.out
							.println("No favourites file found, creating new file.");
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

		// Set up the custom glass panel onto which the draggable corner panel
		// will be painted
		final OverlayPanel pnlOverlay = new OverlayPanel(new Dimension(
				getWidth(), getHeight()));
		this.setGlassPane(pnlOverlay);
		pnlOverlay.setVisible(true);

		// Add the narrow info panel at the bottom of the screen
		final InfoPanel pnlInfo = new InfoPanel();
		this.add(pnlInfo, BorderLayout.SOUTH);

		// Add the main graph panel to the screen centre
		this.add(pnlMain, BorderLayout.CENTER);

		// Set the parameters of the corner panel; size, starting position, drag
		// properties etc..
		pnlCorner.setBounds(15, 15, 256, 256);
		pnlCorner.setPreferredSize(new Dimension(256, 256));
		pnlCorner.setBorderOffset(15);
		pnlCorner.setSnapDistance(30);

		// Add the corner panel into the glass pane.
		pnlOverlay.add(pnlCorner);

		// A huge block of code to handle all the functionality of the context
		// menu; mostly achieved using an unnecessary quantity of anonymous
		// ActionListeners
		final ContextMenu m = new ContextMenu() {
			private static final long serialVersionUID = AppWindow.serialVersionUID;
			private JMenuItem itemSwap, itemDrop, itemLift, itemSave, itemLoad,
					itemConfig;

			{
				// Choose this item to swap the contents of the two panels.
				itemSwap = new JMenuItem("Swap Panels");
				itemSwap.addActionListener(new ActionListener() {
					@Override
					// Not too complex; just swaps the contents of the two
					// panels over
					public void actionPerformed(ActionEvent arg0) {
						GraphPanel tmp = pnlMain.setPanel(pnlCorner.getPanel());
						pnlCorner.setPanel(tmp);

						pnlCorner.resetZoom();
						validate();
					}
				});
				addMenuItem(itemSwap, KeyEvent.VK_W);

				// Activates cursor mode and places the cursor at the indicated
				// point.
				itemDrop = new JMenuItem("Drop Cursor");
				itemDrop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dropCursor();
					}
				});
				addMenuItem(itemDrop, KeyEvent.VK_D);

				// Disables cursor mode; the virtual cursor will return to
				// following the mouse.
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

				// Most of this is GUI set up; this is the option to save the
				// selected point as a favourite.
				itemSave = new JMenuItem("Save Favourite");
				itemSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// Ensure cursor mode is active so the selected point
						// doesn't change while the user is navigating the GUI
						bCursorMode = true;

						// The dialog through which the user can enter the name
						// of the point to be saved.
						final ForceDialog dlg = new ForceDialog(null);
						dlg.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						c.insets = new Insets(5, 5, 5, 5);
						c.gridwidth = 2;
						c.gridy = 0;
						c.gridx = 0;
						final JButton btnSave = new JButton("Save");

						// The cancel button closes the dialog without doing
						// anything
						JButton btnCancel = new CancelButton(dlg);

						final JTextField txtName = new JTextField(8);
						dlg.add(new JLabel(
								"Enter a name to save this favourite as."), c);
						c.gridy = 1;
						c.fill = GridBagConstraints.HORIZONTAL;
						dlg.add(txtName, c);
						c.gridwidth = 1;
						c.gridy = 2;
						c.fill = GridBagConstraints.NONE;
						c.weightx = 1;
						dlg.add(btnSave, c);
						c.gridx++;
						dlg.add(btnCancel, c);

						// The listeners which handle the actual saving of the
						// point
						txtName.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								btnSave.doClick();
							}
						});
						btnSave.addActionListener(new ActionListener() {
							// This method is long because it can, in turn,
							// produce another dialog.
							@Override
							public void actionPerformed(ActionEvent e) {
								// Fetch the user input name.
								final String name = txtName.getText();

								// If they haven't typed a name, warn them.
								if (name.equals("")
										|| name.equals("Please enter a name!")) {
									txtName.setText("Please enter a name!");
									return;
								}

								// Check that name doesn't already exist; if it
								// doesn't, add it straight off.
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
									// If it does already exist, spawn a
									// confirmation dialog to check whether they
									// want to overwrite the existing value.
									final YesNoDialog dlgYN = new YesNoDialog(
											dlg,
											"That name already exists. Overwrite?");
									// If they do, then we overwrite it (and
									// then close both dialogs).
									dlgYN.addYesListener(new ActionListener() {
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
									// If they don't want to overwrite, we just
									// close the confirmation dialog so they can
									// type a new name.
									dlgYN.addNoListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent e) {
											dlgYN.dispatchEvent(new WindowEvent(
													dlgYN,
													WindowEvent.WINDOW_CLOSING));
										}
									});
									dlgYN.pack();
									dlgYN.setVisible(true);
								}
							}
						});
						dlg.pack();
						dlg.setLocationRelativeTo(null);
						dlg.setVisible(true);
					}
				});
				addMenuItem(itemSave, KeyEvent.VK_S);

				// This will load a pre-saved favourite into the selected point
				// (virtual cursor)
				itemLoad = new JMenuItem("Load Favourite");
				itemLoad.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// Spawn & setup the dialog to allow the user to choose
						// a favourite.
						final ForceDialog dlg = new ForceDialog(null);
						dlg.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();

						JLabel lblLoad = new JLabel(
								"Select a favourite to load");

						// GridBagConstraints
						c.insets = new Insets(5, 5, 5, 5);
						c.gridwidth = 2;
						c.gridy = 0;
						c.gridx = 0;

						dlg.add(lblLoad, c);

						// The list of points to choose from (by name).
						final JComboBox<String> cbxFavourites = new JComboBox<String>();
						for (String s : favourites.keySet()) {
							cbxFavourites.addItem(s);
						}

						c.gridy = 1;
						c.fill = GridBagConstraints.HORIZONTAL;

						dlg.add(cbxFavourites, c);

						// The button to confirm loading the selected point
						JButton btnLoad = new JButton("Load");
						btnLoad.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String s;
								// Check they actually selected an option in the
								// box.
								if ((s = (String) cbxFavourites
										.getSelectedItem()) != null) {
									// Set cursor mode and drop the cursor
									// (otherwise it's pointless as moving the
									// mouse will change the cursor point)
									bCursorMode = true;
									cursor.setPoint(favourites.get(s));
									// Update the corner panel to reflect the
									// new cursor position
									pnlCorner.updateFractal(cursor.getPoint());
									pnlCorner.repaint();
									// Finally, close the dialog
									dlg.dispatchEvent(new WindowEvent(dlg,
											WindowEvent.WINDOW_CLOSING));
								}
							}
						});

						// This will be the cancel button to close the dialog,
						// doing nothing.
						JButton btnCancel = new CancelButton(dlg);

						// GridBagConstraints
						c.fill = GridBagConstraints.NONE;
						c.gridwidth = 1;
						c.gridy = 2;
						c.weightx = 1;

						// Add the buttons
						dlg.add(btnLoad, c);
						c.gridx++;
						dlg.add(btnCancel, c);

						// Position & configure the window
						dlg.setLocationRelativeTo(null);
						dlg.pack();
						dlg.setVisible(true);
					}
				});
				addMenuItem(itemLoad, KeyEvent.VK_O);

				// This item opens the configuration window where properties of
				// the two fractal panels can be adjusted
				itemConfig = new JMenuItem("Configure Fractal");
				itemConfig.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// Spawn and set up the dialog which will contain all
						// the configurable options.
						final ForceDialog dlg = new ForceDialog(null);
						dlg.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();

						// Radio buttons allow the user to select which panel to
						// configure.
						JLabel lblChoosePanel = new JLabel(
								"Select a panel to configure");

						c.insets = new Insets(5, 5, 5, 5);
						c.weightx = 1;
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
						// Initially, the main panel is selected
						btnPnlMain.setSelected(true);
						dlg.add(btnPnlMain, c);
						dlg.add(btnPnlCorner, c);

						// A combo-box of preconfigured fractals to be displayed
						// (NB multibrot is not working yet).
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

						// When the user selects a panel using the radio
						// buttons, set the combo box to display whatever
						// fractal is currently in the panel.
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

						// This option allows the user to select one of several
						// colouring algorithms. Experimental.
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

						// This option allows the user to set the maximum number
						// of iterations for the fractal calculation (this value
						// is static so is the same for both panels).
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

						// The OK and CANCEL buttons at the bottom.
						JButton btnOk = new JButton("Ok");
						JButton btnCancel = new CancelButton(dlg);
						dlg.add(btnOk, c);
						dlg.add(btnCancel, c);

						// This listener ensures that the user cannot enter an
						// invalid number of iterations into the text field; it
						// will automatically revert if they enter an invalid
						// character.
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

						// This listener grabs all the information from the
						// above fields and processes it when the Ok button is
						// pressed.
						btnOk.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String sel;
								String col;
								// Fetch the selections from the fractal and
								// colouring lists. If either is null, do
								// nothing.
								if ((sel = (String) cbxFractals
										.getSelectedItem()) != null
										&& (col = (String) cbxColoring
												.getSelectedItem()) != null) {
									// If the main panel is selected, apply the
									// attributes to that (else apply them to
									// the corner panel).
									if (btnPnlMain.isSelected()) {
										// Create a new GraphPanel to put inside
										// the panel, with the user's selected
										// fractal.
										GraphPanel pnl = new GraphPanel(
												fractals.get(sel));
										pnl.setBounds(0, 0, 512, 512);
										Coloring c = null;
										// Reflection! The list of colour
										// schemes stores them as classes. A
										// planned feature was to have the
										// colour palette also configurable
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
										// Same as above, but for the corner
										// panel (if that was selected)
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
									}
									// Set the maximum iteration count (ideally
									// this would be independent per-panel)
									Fractal.setMaxIterations(Integer
											.parseInt(txtIterations.getText()));
									// Finally, close the dialog
									dlg.dispatchEvent(new WindowEvent(dlg,
											WindowEvent.WINDOW_CLOSING));
								} else {
									// If either fractal or colouring was null,
									// do nothing (don't close the dialog
									// though).
									return;
								}
							}
						});

						// Positioning stuff
						dlg.setLocationRelativeTo(null);
						dlg.pack();
						dlg.setVisible(true);

					}
				});
				addMenuItem(itemConfig, KeyEvent.VK_F);
			}
		};

		// Add the context menu into the glass panel (it has to be in the glass
		// panel to properly detect the user clicks, and can't be in either
		// graph panel because of their one-child restriction).
		pnlOverlay.add(m);

		// Listener responsible for updating the corner panel as the mouse
		// moves/is clicked
		MouseAdapter mouseTrackListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// If the clicked button was right-mouse, display the context
				// menu.
				if (e.getButton() == MouseEvent.BUTTON3) {
					m.show(pnlOverlay, e.getX(), e.getY());
					// If left-mouse was clicked and cursor mode is active, move
					// the cursor to the location of the click and update the
					// corner panel.
				} else if (e.getButton() == MouseEvent.BUTTON1 && bCursorMode) {
					cursor.setPoint(pnlMain.getMathCoords(e.getPoint()));
					pnlInfo.setCoordinates(cursor.toString());
					pnlCorner.updateFractal(cursor.getPoint());
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// If the mouse is moved while not in cursor mode (i.e. while in
				// live update mode), make the cursor follow the mouse
				if (!bCursorMode) {
					cursor.setPoint(pnlMain.getMathCoords(e.getPoint()));
					pnlInfo.setCoordinates(cursor.toString());
					pnlCorner.updateFractal(cursor.getPoint());
				}
			}

		};

		// Add the tracking listener to the glass pane
		pnlOverlay.addMouseListener(mouseTrackListener);
		pnlOverlay.addMouseMotionListener(mouseTrackListener);

		// Listener responsible for handling zooming in and out of the main
		// panel.
		MouseAdapter zoomListener = new MouseAdapter() {
			Point p;

			// Paints the rectangle onto the glass pane; this isn't the best
			// solution as when the mouse stops the rectangle disappears, but is
			// good enough for now.
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

			// When the mouse is initially pressed, remember the 'start point'
			// of the drag
			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
			}

			// When the mouse is released, check that it has actually moved some
			// distance from its starting point (otherwise clicking without
			// dragging would trigger it) and inform the main panel to update
			// its image
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
				}
			}

			// Middle-mouse zooms out to the previous zoom frame
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					pnlMain.zoomOut();
				}
			}
		};

		// Adding the zoom listener
		pnlOverlay.addMouseMotionListener(zoomListener);
		pnlOverlay.addMouseListener(zoomListener);

	}

	// A method to activate cursor mode at the current cursor position,
	// abstracted to avoid code duplication.
	public void dropCursor() {
		bCursorMode = true;
		pnlCorner.updateFractal(cursor.getPoint());
	}

	// A little method to handle the saving of favourites and move some code out
	// of the main constructor.
	public void saveFavourites() throws IOException {
		PrintStream favouritesWriter = new PrintStream(new FileOutputStream(
				new File("favourites.csv")));
		// Prints each favourite as a line in the form
		// "name,-0.000000000+0.00000000i"
		for (String s : favourites.keySet()) {
			String lineOut = s + "," + favourites.get(s).toString();
			favouritesWriter.println(lineOut);
		}
		favouritesWriter.close();
	}
}
