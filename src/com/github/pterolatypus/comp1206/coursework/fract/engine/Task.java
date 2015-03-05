package com.github.pterolatypus.comp1206.coursework.fract.engine;

public class Task implements Runnable {

	private int pauseTime;
	private long timestamp;
	
	public Task(int pauseTime) {
		this.pauseTime = pauseTime;
		timestamp = System.currentTimeMillis();
	}
	
	public Task() {
		this(0);
	}
	
	@Override
	public void run() {
		try {
			if (timestamp + pauseTime > System.currentTimeMillis()) {
				Thread.sleep(timestamp+pauseTime-System.currentTimeMillis());
			}
		} catch (InterruptedException e) {
			return;
		}
	}

}
