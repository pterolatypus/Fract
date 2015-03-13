package com.github.pterolatypus.comp1206.coursework.fract.engine;

import com.github.pterolatypus.comp1206.coursework.fract.gui.AppWindow;

/**
 * An exception thrown when a call to {@link com.github.pterolatypus.comp1206.coursework.fract.gui.Coloring#setProperty(String, Object)} is made with an invalid property name or a value of invalid type for
 * the specified property
 * 
 * @author James
 *
 */
public class InvalidPropertyException extends RuntimeException {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	public InvalidPropertyException(String name, Object value, Object thrower) {
		super("Attempted to set an invalid property of object "
				+ thrower.toString() + ". Property: " + name + ", Value: "
				+ value.toString());
	}

}
