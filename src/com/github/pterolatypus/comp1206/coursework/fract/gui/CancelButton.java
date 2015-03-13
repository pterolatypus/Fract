package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

/**
 * A simple extension of JButton which is automatically configured to close its
 * parent window when pressed
 * 
 * @author James
 *
 */
public class CancelButton extends JButton {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	/**
	 * Constructs a new CancelButton instance
	 * 
	 * @param parent
	 *            The parent window which this button will close. If null, the
	 *            button will attempt to close the lowest-level Window it is
	 *            inside.
	 */
	public CancelButton(final Window parent) {
		super("Cancel");
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Null check to avoid NPE
				Container containerParent = (parent == null) ? getParent()
						: parent;
				// Climbs up through the hierarchy and grabs the first Window
				// instance. (Usually the immediate parent, but not always).
				while (!(containerParent instanceof Window))
					containerParent = containerParent.getParent();
				// Close the parent window.
				Window windowParent = (Window) containerParent;
				windowParent.dispatchEvent(new WindowEvent(windowParent,
						WindowEvent.WINDOW_CLOSING));
			}
		});
	}

}
