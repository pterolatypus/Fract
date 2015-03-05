package com.github.pterolatypus.comp1206.coursework.fractal;

public class Task implements Runnable {

	private int pauseTime;
	private long timestamp;
	
	public Task(int pauseTime) {
		this.pauseTime = pauseTime;
		timestamp = System.currentTimeMillis();
	}
	
	public Task() {
		this(-1);
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(timestamp+pauseTime-System.currentTimeMillis());
		} catch (InterruptedException e) {
			return;
		}
	}

}
