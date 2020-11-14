package com.shimizukenta.logger.tcpiplogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

/**
 * This class is Simple TCP/IP Logger, connect to such TELNET.
 * 
 * @author kenta-shimizu
 *
 */
public class SimpleTcpIpLogger extends AbstractTcpIpLogger {
	
	private final SimpleTcpIpLoggerConfig config;
	private BufferedWriter writer;
	
	public SimpleTcpIpLogger(SimpleTcpIpLoggerConfig config) {
		super(config);
		this.config = config;
		this.writer = null;
	}
	
	@Override
	public void open() throws IOException {
		
		synchronized ( this ) {
			
			if ( isClosed() ) {
				throw new IOException("Already closed");
			}
			
			if ( isOpen() ) {
				throw new IOException("Already opened");
			}
			
			Path path = config.path();
			if ( path == null ) {
				throw new IOException("file-path not setted");
			}
			
			this.writer = Files.newBufferedWriter(
					path,
					config.fileCharset(),
					StandardOpenOption.WRITE,
					StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
			
			super.open();
		}
	}
	
	@Override
	public void close() throws IOException {
		synchronized ( this ) {
			
			if ( this.isClosed() ) {
				return;
			}
			
			IOException ioExcept = null;
			
			try {
				super.close();
			}
			catch ( IOException e ) {
				ioExcept = e;
			}
			
			if ( this.writer != null ) {
				try {
					this.writer.close();
				}
				catch ( IOException e ) {
					ioExcept = e;
				}
			}
			
			if ( ioExcept != null ) {
				throw ioExcept;
			}
		}
	}

	@Override
	protected void writeLines(LinesTimestampPair pair) throws IOException {
		
		final String ts;
		
		if ( config.addTimestampToLine() ) {
			ts = pair.timestamp().format(config.lineTimestampFormatter()) + " ";
		} else {
			ts = "";
		}
		
		for ( String line : pair.lines() ) {
			writer.write(ts);
			writer.write(line);
			writer.newLine();
		}
	}
	
	public static void main(String[] args) {
		
		final SimpleTcpIpLoggerConfig config = new SimpleTcpIpLoggerConfig();
		
		try {
			
			for ( int i = 0, m = args.length; i < m; i += 2 ) {
				
				String key = args[i];
				String v = args[i + 1];
				
				if ( key.equalsIgnoreCase("--path") ) {
					
					config.path(Paths.get(v));
					
				} else if ( key.equalsIgnoreCase("--connect") ) {
					
					config.connect(parseSocketAddress(v));
					
				} else if ( key.equalsIgnoreCase("--add-line-timestamp") ) {
					
					config.addTimestampToLine(Boolean.parseBoolean(v));
					
				} else if ( key.equalsIgnoreCase("--line-timestamp-format") ) {
					
					config.lineTimestampFormatter(DateTimeFormatter.ofPattern(v));
					
				} else if ( key.equalsIgnoreCase("--echo") ) {
					
					config.isEcho(Boolean.parseBoolean(v));
				}
			}
			
			try (
					SimpleTcpIpLogger logger = new SimpleTcpIpLogger(config);
					) {
				
				logger.open();
				
				synchronized ( SimpleTcpIpLogger.class ) {
					SimpleTcpIpLogger.class.wait();
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
