package com.github.pterolatypus.comp1206.coursework.fract.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 * A kind of dialog box which will, unless configured otherwise, force itself to
 * be the focussed window whenever possible.
 * 
 * @author James
 *
 */
public class ForceDialog extends JDialog {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	private boolean bHasPriority = true;
	private ForceDialog parent;

	/**
	 * Constructs a new ForceDialog with the specified parent. Calling this
	 * constructor causes the specified dialog to relinquish its focus, to allow
	 * the child to take priority. When the child window is closed, it will
	 * return priority to the parent.
	 * 
	 * @param parent
	 *            The parent dialog of this ForceDialog
	 */
	public ForceDialog(ForceDialog parent) {
		super();
		this.parent = parent;
		setParent(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (bHasPriority) {
							ForceDialog.this.toFront();
						}
					}
				});
			}

			@Override
			public void windowClosing(WindowEvent e) {
				setParent(true);
			}
		});
	}

	/**
	 * Sets this ForceDialog's priority status (whether it will force-focus
	 * itself)
	 * 
	 * @param p
	 *            Whether this dialog should force-focus
	 */
	public void setPriority(boolean p) {
		this.bHasPriority = p;
	}

	// Sets the parent's priority; abstracted to avoid code duplication.
	private void setParent(boolean p) {
		if (parent != null) {
			parent.setPriority(p);
		}
	}

}
