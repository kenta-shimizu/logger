package com.shimizukenta.logger.tcpiplogger;

import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTcpIpLoggerConfig implements Serializable {
	
	private static final long serialVersionUID = 3328743966578057539L;
	
	private static final float DEFAULT_RECONNECT = 5.0F;
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	private SocketAddress connect;
	private float reconnectSeconds;
	private boolean isEcho;
	private Charset fileCharset;
	
	public AbstractTcpIpLoggerConfig() {
		this.connect = null;
		this.reconnectSeconds = DEFAULT_RECONNECT;
		this.isEcho = true;
		this.fileCharset = DEFAULT_CHARSET;
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
	
	/**
	 * Logging-file-charset setter
	 * 
	 * @param charset of logging-file
	 */
	public void fileCharset(Charset charset) {
		synchronized ( this ) {
			this.fileCharset = charset;
		}
	}
	
	/**
	 * Returns logging-file-charset.
	 * 
	 * @return logging-file-charset
	 */
	public Charset fileCharset() {
		synchronized ( this ) {
			return this.fileCharset;
		}
	}
}
