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
		
//		Complex c = new Complex(0, 1);
//
//		for(int i = 1; i < 10; i++) {
//			System.out.println(c.pow(i).getReal() +" + "+c.pow(i).getImaginary()+"i");
//		}
		
	}

}
