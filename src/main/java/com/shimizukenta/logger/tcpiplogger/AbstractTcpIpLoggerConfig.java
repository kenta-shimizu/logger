package com.shimizukenta.logger.tcpiplogger;

import java.net.SocketAddress;
import java.nio.file.Path;

public class AbstractTcpIpLoggerConfig {
	
	private static final float DEFAULT_RECONNECT = 10.0F;
	
	private Path path;
	private SocketAddress connect;
	private float reconnectSeconds;
	private boolean isEcho;
	
	public AbstractTcpIpLoggerConfig() {
		this.path = null;
		this.connect = null;
		this.reconnectSeconds = DEFAULT_RECONNECT;
		this.isEcho = false;
	}
	
	/**
	 * Returns log file path.
	 * 
	 * @return log file path
	 */
	public Path path() {
		synchronized ( this ) {
			return this.path;
		}
	}
	
	/**
	 * Log file path setter.
	 * 
	 * @param path
	 */
	public void path(Path path) {
		synchronized ( this ) {
			this.path = path;
		}
	}
	
	/**
	 * Returns connect SocketAddress.
	 * 
	 * @return connect SocketAddress
	 */
	public SocketAddress connect() {
		synchronized ( this ) {
			return this.connect;
		}
	}
	
	/**
	 * Connect SocketAddress setter.
	 * 
	 * @param socketaddress
	 */
	public void connect(SocketAddress socketaddress) {
		synchronized ( this ) {
			this.connect = socketaddress;
		}
	}
	
	/**
	 * Returns reconnect seconds.
	 * 
	 * @return reconnect seconds
	 */
	public float reconnectSeconds() {
		synchronized ( this ) {
			return this.reconnectSeconds;
		}
	}
	
	/**
	 * Reconnect seconds setter.
	 * 
	 * @param seconds
	 */
	public void reconnectSeconds(float seconds) {
		synchronized ( this ) {
			this.reconnectSeconds = seconds;
		}
	}
	
	/**
	 * Returns {@code true} if echo.
	 * 
	 * @return {@code true} if echo
	 */
	public boolean isEcho() {
		synchronized ( this ) {
			return this.isEcho;
		}
	}
	
	/**
	 * Echo setter.
	 * 
	 * @param f
	 */
	public void isEcho(boolean f ) {
		synchronized ( this ) {
			this.isEcho = f;
		}
	}
}
