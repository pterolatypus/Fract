package com.github.pterolatypus.comp1206.coursework.fract.engine;

import com.github.pterolatypus.comp1206.coursework.fract.gui.AppWindow;

public class InvalidPropertyException extends RuntimeException {

	private static final long serialVersionUID = AppWindow.serialVersionUID;

	public InvalidPropertyException(String name, Object value, Object thrower) {
		super("Attempted to set an invalid property of object "+thrower.toString()+". Property: "+name+", Value: "+value.toString());
	}
	
}
