package com.shimizukenta.logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractLogger implements Logger {
	
	private final ExecutorService execServ = Executors.newCachedThreadPool(r -> {
		Thread th = new Thread(r);
		th.setDaemon(false);
		return th;
	});
	
	protected ExecutorService executorService() {
		return execServ;
	}
	
	protected void executorLoop(InterruptableRunnable r) {
		try {
			for ( ;; ) {
				r.run();
			}
		}
		catch ( InterruptedException ignore ) {
		}
	}
	
	private boolean opened;
	private boolean closed;
	
	public AbstractLogger() {
		this.opened = false;
		this.closed = false;
	}
	
	@Override
	public void open() throws IOException {
		
		synchronized ( this ) {
			
			if ( this.closed ) {
				throw new IOException("Already closed");
			}
			
			if ( this.opened ) {
				throw new IOException("Already opened");
			}
			
			this.opened = true;
		}
	}
	
	@Override
	public void close() throws IOException {
		
		synchronized ( this ) {
			
			if ( this.closed ) {
				return;
			}
			
			this.closed = true;
			
			try {
				execServ.shutdown();
				if ( ! execServ.awaitTermination(1L, TimeUnit.MILLISECONDS) ) {
					execServ.shutdownNow();
					if ( ! execServ.awaitTermination(10L, TimeUnit.SECONDS) ) {
						throw new IOException("ExecutorService#shutdown failed");
					}
				}
			}
			catch ( InterruptedException ignore ) {
			}
		}
	}
	
	@Override
	public boolean isOpen() {
		synchronized ( this ) {
			return this.opened && ! this.closed;
		}
	}
	
	@Override
	public boolean isClosed() {
		synchronized ( this ) {
			return this.closed;
		}
	}
	
	protected static SocketAddress parseSocketAddress(String socketaddress) {
		String[] ss = socketaddress.split(":", 2);
		return new InetSocketAddress(
				ss[0].trim(),
				Integer.parseInt(ss[1]));
	}
	
}
