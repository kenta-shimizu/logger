package com.shimizukenta.logger.tcpiplogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.shimizukenta.logger.AbstractLogger;

public abstract class AbstractTcpIpLogger extends AbstractLogger implements TcpIpLogger {
	
	private final AbstractTcpIpLoggerConfig config;
	
	public AbstractTcpIpLogger(AbstractTcpIpLoggerConfig config) {
		super();
		this.config = config;
	}
	
	public void open() throws IOException {
		
		super.open();
		
		this.executorLoop(this::connectingLoop);
		this.executorService().execute(this::receivingLines);
		this.executorLoop(this::systemEchoLoop);
	}
	
	@Override
	public void close() throws IOException {
		
		synchronized ( this ) {
			
			if ( super.isClosed() ) {
				return;
			}
			
			super.close();
		}
	}
	
	private void connectingLoop() throws InterruptedException {
		
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
	
	private static final byte LF = (byte)0xA;
	private static final byte CR = (byte)0xD;
	
	private void reading(AsynchronousSocketChannel channel) throws InterruptedException {
		
		try (
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				) {
			
			boolean detectCr = false;
			
			ByteBuffer rr = ByteBuffer.allocate(4096);
			
			for ( ;; ) {
				
				((Buffer)rr).clear();
				
				final Future<Integer> f = channel.read(rr);
				
				try {
					int r = f.get().intValue();
					
					LocalDateTime ts = LocalDateTime.now();
					
					if ( r < 0 ) {
						return;
					}
					
					List<byte[]> bss = new ArrayList<>();
					
					((Buffer)rr).flip();
					
					while ( rr.hasRemaining() ) {
						
						byte b = rr.get();
						
						if ( b == CR ) {
							
							detectCr = true;
							
							bss.add(baos.toByteArray());
							baos.reset();
							
						} else if ( b == LF ) {
							
							if ( detectCr ) {
								
								detectCr = false;
								
							} else {
								
								bss.add(baos.toByteArray());
								baos.reset();
							} 
							
						} else {
							
							if ( detectCr ) {
								detectCr = false;
							}
							
							baos.write(b);
						}
					}
					
					entryLines(bss, ts);
				}
				catch ( InterruptedException e ) {
					f.cancel(true);
					
					byte[] bs = baos.toByteArray();
					if ( bs.length > 0 ) {
						entryLines(Collections.singletonList(bs), LocalDateTime.now());
					}
					
					throw e;
				}
			}
		}
		catch ( IOException nothappened) {
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
	
	private void entryLines(List<byte[]> bss, LocalDateTime ts) throws InterruptedException {
		List<String> lines = bss.stream()
				.map(bs -> new String(bs, config.fileCharset()))
				.collect(Collectors.toList());
		lineQueue.put(new LinesTimestampPair(lines, ts));
	}
	
	private final BlockingQueue<LinesTimestampPair> lineQueue = new LinkedBlockingQueue<>();
	
	abstract protected void writeLines(LinesTimestampPair pair) throws IOException;
	
	private void receivingLines() {
		
		try {
			for ( ;; ) {
				try {
					writeLines(lineQueue.take());
				}
				catch ( IOException e ) {
					systemEcho(e);
				}
			}
		}
		catch ( InterruptedException ignore ) {
		}
		
		for ( ;; ) {
			LinesTimestampPair pair = lineQueue.poll();
			if ( pair == null ) {
				break;
			}
			try {
				writeLines(pair);
			}
			catch ( IOException e ) {
				systemEcho(e);
			}
		}
	}
	
	private final BlockingQueue<Object> echoQueue = new LinkedBlockingQueue<>();
	
	protected void systemEcho(Object o) {
		echoQueue.offer(o);
	}
	
	private void systemEchoLoop() throws InterruptedException {
		Object o = echoQueue.take();
		if ( config.isEcho() ) {
			staticSystemEcho(o);
		}
	}
	
	private static final Object syncStaticSystemEcho = new Object();
	
	protected static void staticSystemEcho(Object o) {
		synchronized ( syncStaticSystemEcho ) {
			if ( o instanceof Throwable ) {
				((Throwable) o).printStackTrace();
				System.out.println();
			} else {
				System.out.println(o);
			}
		}
	}
	
}
