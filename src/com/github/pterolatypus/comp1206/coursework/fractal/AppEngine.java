package com.github.pterolatypus.comp1206.coursework.fractal;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppEngine extends Thread {

	private Queue<Task> tasks = new ConcurrentLinkedQueue<Task>();
	
	public void run() {
		AppWindow window = new AppWindow(this);
		window.setVisible(true);
		while (true) {
			while(!tasks.isEmpty()) {
				Task t = tasks.remove();
				t.run();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void queueTask(Task t) {
		tasks.add(t);
	}
	
}
