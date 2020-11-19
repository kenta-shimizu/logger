package com.shimizukenta.logger.jsoncommunicatorlogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.shimizukenta.jsoncommunicator.JsonCommunicator;
import com.shimizukenta.jsoncommunicator.JsonCommunicators;
import com.shimizukenta.jsonhub.JsonHub;
import com.shimizukenta.jsonhub.JsonHubParseException;
import com.shimizukenta.jsonhub.JsonHubPrettyPrinter;
import com.shimizukenta.logger.AbstractLogger;

public abstract class AbstractJsonCommunicatorLogger extends AbstractLogger implements JsonCommunicatorLogger {
	
	private final AbstractJsonCommunicatorLoggerConfig config;
	private final JsonHubPrettyPrinter printer;
	private final JsonCommunicator<?> comm;
	
	public AbstractJsonCommunicatorLogger(AbstractJsonCommunicatorLoggerConfig config) {
		super();
		this.config = config;
		this.comm = JsonCommunicators.newInstance(config.jsonCommunicatorConfig());
		this.comm.addLogListener(this::systemEcho);
		this.comm.addJsonReceiveListener(this::putJson);
		this.printer = JsonHubPrettyPrinter.newPrinter(config.jsonHubPrettyPrinter());
	}
	
	@Override
	public void open() throws IOException {
		synchronized ( this ) {
			super.open();
			
			this.comm.open();
			
			this.executorLoop(this::systemEchoLoop);
			this.executorLoop(this::jsonQueueLoop);
			this.executorLoop(this::pairQueueLoop);
		}
	}
	
	@Override
	public void close() throws IOException {
		
		synchronized ( this ) {
			
			if ( isClosed() ) {
				return;
			}
			
			IOException ioExcept = null;
			
			try {
				super.close();
			}
			catch ( IOException e ) {
				ioExcept = e;
			}
			
			try {
				this.comm.close();
			}
			catch ( IOException e ) {
				ioExcept = e;
			}
			
			if ( ioExcept != null ) {
				throw ioExcept;
			}
		}
	}
	
	private final BlockingQueue<String> jsonQueue = new LinkedBlockingQueue<>();
	
	private void jsonQueueLoop() throws InterruptedException {
		pairQueue.put(new JsonTimestampPair(jsonQueue.take()));
		Thread.sleep(10L);
	}
	
	private void putJson(String json) {
		jsonQueue.offer(json);
	}
	
	private final BlockingQueue<JsonTimestampPair> pairQueue = new LinkedBlockingQueue<>();
	
	private void pairQueueLoop() throws InterruptedException {
		writeJsonFile(pairQueue.take());
	}
	
	/**
	 * Returns log file path.
	 * 
	 * @param pair
	 * @return log file path
	 */
	abstract protected Path getFilePath(JsonTimestampPair pair);
	
	private void writeJsonFile(JsonTimestampPair pair) {
		
		try {
			JsonHub jh = JsonHub.fromJson(pair.json());
			
			Path filepath = getFilePath(pair);
			
			Path parentpath = filepath.getParent();
			Files.createDirectories(parentpath);
			
			this.printer.print(jh, filepath);
		}
		catch ( IOException | JsonHubParseException e ) {
			systemEcho(e);
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
