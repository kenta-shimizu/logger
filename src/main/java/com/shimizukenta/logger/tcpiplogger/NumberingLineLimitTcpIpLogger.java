package com.shimizukenta.logger.tcpiplogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberingLineLimitTcpIpLogger extends AbstractLineLimitTcpIpLogger {
	
	private final NumberingLineLimitTcpIpLoggerConfig config;
	
	public NumberingLineLimitTcpIpLogger(NumberingLineLimitTcpIpLoggerConfig config) {
		super(config);
		this.config = config;
	}
	
	private final AtomicInteger autoNumber = new AtomicInteger(0);
	
	@Override
	protected Path getFilePath(LinesTimestampPair pair) throws IOException {

		String full = config.fullNumberingFormat();
		
		if ( full == null ) {
			
			final String prefix = config.simplePathPrefix();
			final String suffix = config.simplePathSuffix();
			final String numFormat = config.simpleNumberingFormat();
			
			for ( ;; ) {
				
				int n = autoNumber.incrementAndGet();
				String s = prefix
						+ String.format(numFormat, n, n, n, n)
						+ suffix;
				
				Path path = Paths.get(s);
				if ( ! Files.exists(path) ) {
					return path;
				}
			}
			
		} else {
			
			for ( ;; ) {
				
				int n = autoNumber.incrementAndGet();
				
				Path path = Paths.get(String.format(full, n, n, n, n));
				if ( ! Files.exists(path) ) {
					return path;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		final NumberingLineLimitTcpIpLoggerConfig config = new NumberingLineLimitTcpIpLoggerConfig();
		
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
					
				} else if ( key.equalsIgnoreCase("--line-limit") ) {
					
					config.lineLimit(Integer.parseInt(v));
					
				} else if ( key.equalsIgnoreCase("--path-full-numbering") ) {
					
					config.fullNumberingFormat(v);
					
				} else if ( key.equalsIgnoreCase("--path-prefix-numbering") ) {
					
					config.simplePathPrefix(v);
					
				} else if ( key.equalsIgnoreCase("--path-suffix-numbering") ) {
					
					config.simplePathSuffix(v);
					
				} else if ( key.equalsIgnoreCase("--path-numbering-format") ) {
					
					config.simpleNumberingFormat(v);
				}
			}
			
			try (
					NumberingLineLimitTcpIpLogger inst = new NumberingLineLimitTcpIpLogger(config);
					) {
				
				inst.open();
				
				synchronized ( NumberingLineLimitTcpIpLogger.class ) {
					NumberingLineLimitTcpIpLogger.class.wait();
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
