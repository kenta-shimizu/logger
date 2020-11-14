package com.shimizukenta.logger.tcpiplogger;

import java.nio.file.Path;
import java.util.Objects;

public class SimpleTcpIpLoggerConfig extends AbstractTcpIpLoggerConfig {
	
	private static final long serialVersionUID = -8144875687675308820L;
	
	private Path path;
	
	public SimpleTcpIpLoggerConfig() {
		super();
		this.path = null;
	}
	
	/**
	 * Returns logging-file-path.
	 * 
	 * @return logging-file-path
	 */
	public Path path() {
		synchronized ( this ) {
			return this.path;
		}
	}
	
	/**
	 * Logging-file-path setter.
	 * 
	 * @param path of logging-file
	 */
	public void path(Path path) {
		synchronized ( this ) {
			this.path = Objects.requireNonNull(path);
		}
	}
	
}
