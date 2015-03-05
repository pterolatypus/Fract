package com.github.pterolatypus.comp1206.coursework.fractal;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FractalEngine extends Thread {

	private boolean bRun = true;
	
	private ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();
	private List<NotifyListener> listeners = new ArrayList<NotifyListener>();
	
	private Fractal f;
	private Double mathBounds = new Rectangle2D.Double(-2D,1.6D,4D,-3.2D);
	
	private BufferedImage graphImage;
	
	public FractalEngine(Fractal f) {
		super();
		this.f = f;
		this.setPriority(this.getPriority() - 1);
	}
	
	@Override
	public void run() {
		while(bRun) {
			try {
				while(taskQueue.isEmpty()) {
					Thread.sleep(10);
				}
				while(taskQueue.size() > 1) {
					taskQueue.poll();
				}
				Runnable r = taskQueue.poll();
				r.run();
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
	
	public BufferedImage getImage() {
		return graphImage;
	}
	
	public void updateMathBounds(Rectangle2D.Double mathBounds) {
		this.mathBounds = mathBounds;
	}
	
	public void updateImage(final Rectangle pixelBounds) {
		taskQueue.add(new Task(1000) {
			public void run() {
				super.run();
				BufferedImage im = new BufferedImage((int)pixelBounds.getWidth(), (int)pixelBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < pixelBounds.getWidth(); x++) {
					for (int y = 0; y < pixelBounds.getHeight(); y++) {
						double mathX = (x/pixelBounds.getWidth())*mathBounds.getWidth()+mathBounds.getX();
						double mathY = (y/pixelBounds.getHeight())*mathBounds.getHeight()+mathBounds.getY();
						Color c = f.calculate(new Complex(mathX,mathY));
						im.setRGB(x,y,c.getRGB());
					}
				}
				graphImage = im;
			}
		});
	}
	
	public void terminate() {
		bRun = false;
	}
	
}
