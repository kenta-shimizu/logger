package com.shimizukenta.logger.jsoncommunicatorlogger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class SimpleJsonCommunicatorLogger extends AbstractJsonCommunicatorLogger {
	
	private final SimpleJsonCommunicatorLoggerConfig config;
	
	public SimpleJsonCommunicatorLogger(SimpleJsonCommunicatorLoggerConfig config) {
		super(config);
		this.config = config;
	}
	
	@Override
	protected Path getFilePath(JsonTimestampPair pair) {
		
		DateTimeFormatter full = config.fullTimestampFormat();
		
		if ( full == null ) {
			
			String s = config.simplePathPrefix()
					+ pair.timestamp().format(config.simpleTimestampFormat())
					+ config.simplePathSuffix();
			
			return Paths.get(s);
			
		} else {
			
			return Paths.get(pair.timestamp().format(full));
		}
	}
	
	public static void main(String[] args) {
		
		final SimpleJsonCommunicatorLoggerConfig config = new SimpleJsonCommunicatorLoggerConfig();
		
		try {
			
			for ( int i = 0, m = args.length; i < m; i += 2 ) {
				
				String key = args[i];
				String v = args[i + 1];
				
				if ( key.equalsIgnoreCase("--connect") ) {
					
					config.jsonCommunicatorConfig().addConnect(parseSocketAddress(v));
					
				} else if ( key.equalsIgnoreCase("--bind") ) {
					
					config.jsonCommunicatorConfig().addBind(parseSocketAddress(v));
					
				} else if ( key.equalsIgnoreCase("--echo") ) {
					
					config.isEcho(Boolean.parseBoolean(v));
					
				} else if ( key.equalsIgnoreCase("--path-full-timestamp") ) {
					
					config.fullTimestampFormat(DateTimeFormatter.ofPattern(v));
					
				} else if ( key.equalsIgnoreCase("--path-prefix-timestamp") ) {
					
					config.simplePathPrefix(v);
					
				} else if ( key.equalsIgnoreCase("--path-suffix-timestamp") ) {
					
					config.simplePathSuffix(v);
					
				} else if ( key.equalsIgnoreCase("--path-timestamp-format") ) {
					
					config.simpleTimestampFormat(DateTimeFormatter.ofPattern(v));
				}
			}
			
			try (
					SimpleJsonCommunicatorLogger logger = new SimpleJsonCommunicatorLogger(config);
					) {
				
				logger.open();
				
				synchronized ( SimpleJsonCommunicatorLogger.class ) {
					SimpleJsonCommunicatorLogger.class.wait();
				}
			}
			catch ( InterruptedException ignore ) {
			}
		}
		catch ( Throwable t ) {
			staticSystemEcho(t);
		}
	}
	
}
