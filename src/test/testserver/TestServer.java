package testserver;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestServer implements Closeable {
	
	public static void main(String[] args) {
		
		SocketAddress addr = new InetSocketAddress("127.0.0.1", 23000);
		
		try (
				TestServer inst = new TestServer(addr);
				) {
			
			inst.open();
			
			synchronized ( TestServer.class ) {
				TestServer.class.wait();
			}
		}
		catch ( InterruptedException ignore ) {
		}
		catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
	
	private final ExecutorService execServ = Executors.newCachedThreadPool(r -> {
		Thread th = new Thread(r);
		th.setDaemon(true);
		return th;
	});
	
	private final Collection<AsynchronousSocketChannel> channels = new CopyOnWriteArrayList<>();
	private final SocketAddress bindAddr;
	
	private TestServer(SocketAddress addr) {
		this.bindAddr = addr;
	}
	
	public void open() throws IOException {
		
		execServ.execute(() -> {
			
			try (
					AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
					) {
				
				server.bind(bindAddr);
				
				server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
	
					@Override
					public void completed(AsynchronousSocketChannel channel, Void attachment) {
						
						server.accept(null, this);
						
						String channelStr = channel.toString();
						
						try {
							channels.add(channel);
							
							System.out.println("accepted: " + channelStr);
							
							Collection<Callable<Void>> tasks = Arrays.asList(() -> {
								
								try {
									reading(channel);
								}
								catch ( InterruptedException ignore ) {
								}
								
								return null;
							});
							
							execServ.invokeAny(tasks);
						}
						catch ( InterruptedException ignore ) {
						}
						catch ( ExecutionException e ) {
							
							Throwable t = e.getCause();
							
							if ( t instanceof Error ) {
								throw (Error)t;
							}
							
							if ( t instanceof RuntimeException ) {
								throw (RuntimeException)t;
							}
							
							t.printStackTrace();
						}
						finally {
							
							channels.remove(channel);
							
							try {
								channel.shutdownOutput();
							}
							catch ( IOException giveup ) {
							}
							
							try {
								channel.close();
							}
							catch ( IOException e ) {
								e.printStackTrace();
							}
							
							System.out.println("closed: " + channelStr);
						}
					}
	
					@Override
					public void failed(Throwable t, Void attachment) {
						t.printStackTrace();
						synchronized ( server ) {
							server.notifyAll();
						}
					}
				});
				
				synchronized ( server ) {
					server.wait();
				}
			}
			catch ( InterruptedException ignore ) {
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
		});
		
		execServ.execute(() -> {
			try {
				writing();
			}
			catch ( InterruptedException ignore ) {
			}
		});
	}
	
	private void reading(AsynchronousSocketChannel channel) throws InterruptedException, Exception {
		
		final ByteBuffer buffer  = ByteBuffer.allocate(1024);
		
		try {
			
			for ( ;; ) {
				
				((Buffer)buffer).clear();
				
				Future<Integer> f = channel.read(buffer);
				
				try {
					
					int r = f.get().intValue();
					
					if ( r < 0 ) {
						break;
					}
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
			
			throw (Exception)t;
		}
	}
	
	private static final byte LF = (byte)0xA;
	
	private void writing() throws InterruptedException {
		
		int autoNum = 0;
		
		for ( ;; ) {
			
			TimeUnit.SECONDS.sleep(5L);
			
			++ autoNum;
			
			String s = "" + autoNum;
			final byte[] bs = s.getBytes(StandardCharsets.US_ASCII);
			
			Collection<Callable<Void>> tasks = channels.stream()
					.map(channel -> {
						
						return new Callable<Void>() {
							
							@Override
							public Void call() throws Exception {
								
								final ByteBuffer buffer = ByteBuffer.allocate(bs.length + 1);
								buffer.put(bs);
								buffer.put(LF);
								((Buffer)buffer).flip();
								
								while ( buffer.hasRemaining() ) {
									
									final Future<Integer> f = channel.write(buffer);
									
									try {
										int w = f.get().intValue();
										
										if ( w <= 0 ) {
											break;
										}
									}
									catch ( InterruptedException e ) {
										f.cancel(true);
										break;
									}
								}
								
								return null;
							}
						};
					})
					.collect(Collectors.toList());
			
			Collection<Future<Void>> results = execServ.invokeAll(tasks);
			
			for ( Future<Void> f : results ) {
				
				try {
					f.get();
				}
				catch ( InterruptedException e ) {
					f.cancel(true);
					throw e;
				}
				catch ( ExecutionException e ) {
					Throwable t = e.getCause();
					
					if ( t instanceof Error ) {
						throw (Error)t;
					}
					
					if ( t instanceof RuntimeException ) {
						throw (RuntimeException)t;
					}
					
					t.printStackTrace();
				}
			}
			
			System.out.println("wrote \"" + s + "\"");
		}
	}
	
	public void close() throws IOException {
		
		try {
			execServ.shutdown();
			if ( ! execServ.awaitTermination(1L, TimeUnit.MILLISECONDS) ) {
				execServ.shutdownNow();
				if ( ! execServ.awaitTermination(10L, TimeUnit.SECONDS) ) {
					throw new IOException("");
				}
			}
		}
		catch ( InterruptedException ignore ) {
		}
	}
	
}
