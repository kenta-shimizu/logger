package com.shimizukenta.logger.tcpiplogger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shimizukenta.logger.AbstractLogger;

public abstract class AbstractTcpIpLogger extends AbstractLogger implements TcpIpLogger {
	
	private final AbstractTcpIpLoggerConfig config;
	
	public AbstractTcpIpLogger(AbstractTcpIpLoggerConfig config) {
		super();
		this.config = config;
	}
	
	public void open() throws IOException {
		
		super.open();
		
		executorLoop(() -> {
			
			final SocketAddress addr = config.connect();
			
			if ( addr != null) {
				connect(addr);
			}
			
			{
				float v = config.reconnectSeconds();
				long t = (long)(v * 1000.0F);
				if ( t > 0 ) {
					systemEcho("reconnect sleep " + v + " seconds...");
					TimeUnit.MILLISECONDS.sleep(t);
				}
			}
		});
		
		//TODO
		//writing
		//echo
	}
	
	private void connect(SocketAddress addr) throws InterruptedException {
		
		try (
				AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
				) {
			
			systemEcho("try-connect: " + addr);
			
			channel.connect(addr, null, new CompletionHandler<Void, Void>() {

				@Override
				public void completed(Void result, Void attachment) {
					
					String channelString = channel.toString();
					
					try {
						
						systemEcho("connected: " + channelString);
						
						final Collection<Callable<Void>> tasks = Arrays.asList(() -> {
							
							try {
								reading(channel);
							}
							catch ( InterruptedException ignore ) {
							}
							
							return null;
						});
						
						executorService().invokeAny(tasks);
					}
					catch ( ExecutionException e ) {
						
						Throwable t = e.getCause();
						
						if ( t instanceof Error ) {
							throw (Error)t;
						}
						
						if ( t instanceof RuntimeException ) {
							throw (RuntimeException)t;
						}
						
						systemEcho(t);
					}
					catch ( InterruptedException ignore ) {
					}
					finally {
						
						try {
							channel.shutdownOutput();
						}
						catch ( IOException giveup ) {
						}
						
						systemEcho("disconnected: " + channelString);
						
						synchronized ( channel ) {
							channel.notifyAll();
						}
					}
				}

				@Override
				public void failed(Throwable t, Void attachment) {
					systemEcho(t);
					synchronized ( channel ) {
						channel.notifyAll();
					}
				}
			});
			
			synchronized ( channel ) {
				channel.wait();
			}
		}
		catch ( IOException e ) {
			systemEcho(e);
		}
	}
	
	private void reading(AsynchronousSocketChannel channel) throws InterruptedException {
		
		try {
			
			ByteBuffer rr = ByteBuffer.allocate(4096);
			
			for ( ;; ) {
				
				((Buffer)rr).clear();
				
				final Future<Integer> f = channel.read(rr);
				
				try {
					int r = f.get().intValue();
					
					if ( r < 0 ) {
						return;
					}
					
					((Buffer)rr).flip();
					
					//TODO
					
				}
				catch ( InterruptedException e ) {
					f.cancel(true);
					throw e;
				}
			}
		}
		catch ( ExecutionException e ) {
			
			Throwable t = e.getCause();
			
			if ( t instanceof Error ) {
				throw (Error)t;
			}
			
			if ( t instanceof RuntimeException ) {
				throw (RuntimeException)t;
			}
			
			systemEcho(t);
		}
	}
	
	@Override
	public void write(CharSequence log) throws IOException {
		// TODO Auto-generated method stub

	}
	
	protected void systemEcho(Object o) {
		
		//TODO
	}
}
