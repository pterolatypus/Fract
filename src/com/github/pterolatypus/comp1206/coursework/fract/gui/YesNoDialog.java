package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * A simple dialog containing a yes and no button, whose actions can be
 * configured manually.
 * 
 * @author James
 *
 */
public class YesNoDialog extends ForceDialog {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private JButton btnYes, btnNo;

	public YesNoDialog(ForceDialog pr, String message) {
		super(pr);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		this.add(new JLabel(message), c);

		c.gridwidth = 1;
		c.gridy = 1;
		c.gridx = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		
		this.add(btnYes = new JButton("Yes"), c);
		
		c.gridx++;
		
		this.add(btnNo = new JButton("No"), c);
	}

	/**
	 * Adds the specified ActionListener to the 'Yes' button's listener list.
	 * 
	 * @param l
	 *            The listener to add
	 */
	public void addYesListener(ActionListener l) {
		btnYes.addActionListener(l);
	}

	/**
	 * Adds the specified ActionListener to the 'No' button's listener list
	 * 
	 * @param l
	 *            The listener to add
	 */
	public void addNoListener(ActionListener l) {
		btnNo.addActionListener(l);
	}

}
