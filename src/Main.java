import com.github.pterolatypus.comp1206.coursework.fract.gui.AppWindow;

public class Main {

	public static void main(String[] args) {
		AppWindow window = null;
		try {
			window = new AppWindow();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		window.setVisible(true);
	}

}
