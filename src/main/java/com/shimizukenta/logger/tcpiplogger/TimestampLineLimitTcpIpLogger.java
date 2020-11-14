package com.shimizukenta.logger.tcpiplogger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * This class is implements of logging timestamp-name-file.
 * 
 * @author kenta-shimizu
 *
 */
public class TimestampLineLimitTcpIpLogger extends AbstractLineLimitTcpIpLogger {
	
	private final TimestampLineLimitTcpIpLoggerConfig config;
	
	public TimestampLineLimitTcpIpLogger(TimestampLineLimitTcpIpLoggerConfig config) {
		super(config);
		this.config = config;
	}
	
	@Override
	protected Path getFilePath(LinesTimestampPair pair) throws IOException {
		
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
		
		final TimestampLineLimitTcpIpLoggerConfig config = new TimestampLineLimitTcpIpLoggerConfig();
		
		try {
			
			for ( int i = 0, m = args.length; i < m; i += 2 ) {
				
				String key = args[i];
				String v = args[i + 1];
				
				if ( key.equalsIgnoreCase("--connect") ) {
					
					config.connect(parseSocketAddress(v));
					
				} else if ( key.equalsIgnoreCase("--add-line-timestamp") ) {
					
					config.addTimestampToLine(Boolean.parseBoolean(v));
					
				} else if ( key.equalsIgnoreCase("--line-timestamp-format") ) {
					
					config.lineTimestampFormatter(DateTimeFormatter.ofPattern(v));
					
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
					TimestampLineLimitTcpIpLogger logger = new TimestampLineLimitTcpIpLogger(config);
					) {
				
				logger.open();
				
				synchronized ( TimestampLineLimitTcpIpLogger.class ) {
					TimestampLineLimitTcpIpLogger.class.wait();
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
