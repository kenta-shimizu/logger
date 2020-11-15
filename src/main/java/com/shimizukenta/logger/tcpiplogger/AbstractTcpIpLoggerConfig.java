package com.shimizukenta.logger.tcpiplogger;

import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class AbstractTcpIpLoggerConfig implements Serializable {
	
	private static final long serialVersionUID = 3328743966578057539L;
	
	private static final float DEFAULT_RECONNECT = 5.0F;
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
//	private static final DateTimeFormatter DEFAULT_LINE_TIMESTAMP_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final DateTimeFormatter DEFAULT_LINE_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
	
	private SocketAddress connect;
	private float reconnectSeconds;
	private boolean isEcho;
	private Charset fileCharset;
	private boolean addTimestampToLine;
	private DateTimeFormatter lineTimestampFormatter;
	
	public AbstractTcpIpLoggerConfig() {
		this.connect = null;
		this.reconnectSeconds = DEFAULT_RECONNECT;
		this.isEcho = true;
		this.fileCharset = DEFAULT_CHARSET;
		this.addTimestampToLine = false;
		this.lineTimestampFormatter = DEFAULT_LINE_TIMESTAMP_FORMAT;
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
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param socketaddress
	 */
	public void connect(SocketAddress socketaddress) {
		synchronized ( this ) {
			this.connect = Objects.requireNonNull(socketaddress);
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
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param charset of logging-file
	 */
	public void fileCharset(Charset charset) {
		synchronized ( this ) {
			this.fileCharset = Objects.requireNonNull(charset);
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
	
	/**
	 * Returns true if adding timestamp to lines.
	 * 
	 * @return {@code true} if adding timestamp to lines
	 */
	public boolean addTimestampToLine() {
		synchronized ( this ) {
			return this.addTimestampToLine;
		}
	}
	
	/**
	 * Adding timestamp to lines setter.
	 * 
	 * @param f is adding
	 */
	public void addTimestampToLine(boolean isAdd) {
		synchronized ( this ) {
			this.addTimestampToLine = isAdd;
		}
	}
	
	/**
	 * Returns line timestamp formatter.
	 * 
	 * @return line timestamp formatter
	 */
	public DateTimeFormatter lineTimestampFormatter() {
		synchronized ( this ) {
			return this.lineTimestampFormatter;
		}
	}
	
	/**
	 * Line Timestamp format setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param format
	 */
	public void lineTimestampFormatter(DateTimeFormatter format) {
		synchronized ( this ) {
			this.lineTimestampFormatter = Objects.requireNonNull(format);
		}
	}
	
}
