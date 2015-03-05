package com.github.pterolatypus.comp1206.coursework.fractal;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppEngine extends Thread {

	private Queue<Task> tasks = new ConcurrentLinkedQueue<Task>();

	private boolean run = true;
	
	public void run() {
		int cycle = 0;
		AppWindow window = new AppWindow(this);
		window.setVisible(true);
		while (run) {
			while (!tasks.isEmpty()) {
				Task t = tasks.remove();
				t.run();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if ((cycle = (++cycle) % 10) == 0)
				System.out.println("Engine cycled *10");
		}
		System.exit(0);
	}

	public void queueTask(Task t) {
		tasks.add(t);
	}
	
	public void terminate() {
		this.run = false;
	}

}
