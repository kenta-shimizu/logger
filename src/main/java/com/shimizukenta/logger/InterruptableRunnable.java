package com.shimizukenta.logger;

public interface InterruptableRunnable {
	public void run() throws InterruptedException;
}
