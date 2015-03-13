import java.io.IOException;

import com.github.pterolatypus.comp1206.coursework.fract.gui.AppWindow;

/**
 * Just a launch-wrapper class moved into the default package. No real code is executed here.
 * @author James
 *
 */
public class Main {

	/**
	 * 
	 * Creates a new instance of AppWindow and sets it to be visible. Most code is done in the AppWindow constructor.
	 * 
	 * @param args
	 * @throws IOException if a fatal IOException causes file handling to fail, to avoid possible data loss.
	 */
	public static void main(String[] args) throws IOException {
		AppWindow window = null;
		try {
			window = new AppWindow();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		window.setVisible(true);		
	}
}
