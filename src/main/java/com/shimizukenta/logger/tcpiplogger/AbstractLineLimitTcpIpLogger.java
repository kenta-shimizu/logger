package com.shimizukenta.logger.tcpiplogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class AbstractLineLimitTcpIpLogger extends AbstractTcpIpLogger {
	
	private final AbstractLineLimitTcpIpLoggerConfig config;
	
	private BufferedWriter writer;
	private int lineCount;
	
	public AbstractLineLimitTcpIpLogger(AbstractLineLimitTcpIpLoggerConfig config) {
		super(config);
		this.config = config;
		this.writer = null;
		this.lineCount = 0;
	}
	
	/**
	 * Returns write file path.
	 * 
	 * @param pair
	 * @return write file path
	 * @throws IOException
	 */
	abstract protected Path getFilePath(LinesTimestampPair pair) throws IOException;
	
	private BufferedWriter getWriter(LinesTimestampPair pair) throws IOException {
		synchronized ( this ) {
			
			if ( this.writer == null ) {
				
				Path filepath = getFilePath(pair);
				
				if ( ! Files.exists(filepath) ) {
					Path parentpath = filepath.getParent();
					Files.createDirectories(parentpath);
				}
				
				this.writer = Files.newBufferedWriter(
						filepath,
						config.fileCharset(),
						StandardOpenOption.WRITE,
						StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
			}
			
			return this.writer;
		}
	}
	
	private void closeWriter() throws IOException {
		synchronized ( this ) {
			if ( this.writer != null ) {
				try {
					this.writer.close();
				}
				finally {
					this.writer = null;
				}
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
		
		synchronized ( this ) {
			
			for ( String line : pair.lines() ) {
				
				BufferedWriter w = getWriter(pair);
				
				w.write(ts);
				w.write(line);
				w.newLine();
				
				int limit = config.lineLimit();
				if ( limit > 0 ) {
					++ this.lineCount;
					if ( this.lineCount >= limit ) {
						this.lineCount = 0;
						closeWriter();
					}
				}
			}
		}
	}
	
	@Override
	public void open() throws IOException {
		super.open();
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
			
			try {
				closeWriter();
			}
			catch ( IOException e ) {
				ioExcept = e;
			}
			
			if ( ioExcept != null ) {
				throw ioExcept;
			}
		}
	}
}
