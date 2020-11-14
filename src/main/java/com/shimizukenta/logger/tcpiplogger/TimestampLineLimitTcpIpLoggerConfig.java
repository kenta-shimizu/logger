package com.shimizukenta.logger.tcpiplogger;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimestampLineLimitTcpIpLoggerConfig extends AbstractLineLimitTcpIpLoggerConfig {
	
	private static final long serialVersionUID = -4821453318521617352L;
	
	private static final String DEFAULT_SIMPLE_PATH_PREFIX = "log_";
	private static final String DEFAULT_SIMPLE_PATH_SUFFIX = ".log";
	private static final DateTimeFormatter DEFAULT_SIMPLE_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");
	
	private DateTimeFormatter fullTimestampFormat;
	private String simplePathPrefix;
	private String simplePathSuffix;
	private DateTimeFormatter simpleTimestampFormat;
	
	public TimestampLineLimitTcpIpLoggerConfig() {
		super();
		
		this.fullTimestampFormat = null;
		this.simplePathPrefix = DEFAULT_SIMPLE_PATH_PREFIX;
		this.simplePathSuffix = DEFAULT_SIMPLE_PATH_SUFFIX;
		this.simpleTimestampFormat = DEFAULT_SIMPLE_TIMESTAMP_FORMAT;
	}
	
	/**
	 * Returns path-string by fullTimestampFormat.
	 * 
	 * @return path-string by fullTimestampFormat
	 */
	public DateTimeFormatter fullTimestampFormat() {
		synchronized ( this ) {
			return this.fullTimestampFormat;
		}
	}
	
	/**
	 * Path-string by fullTimestampFormat setter.
	 * 
	 * @param format
	 */
	public void fullTimestampFormat(DateTimeFormatter format) {
		synchronized ( this ) {
			this.fullTimestampFormat = format;
		}
	}
	
	/**
	 * Returns path-prefix-string.
	 * 
	 * @return path-prefix-string
	 */
	public String simplePathPrefix() {
		synchronized ( this ) {
			return this.simplePathPrefix;
		}
	}
	
	/**
	 * Path-prefix-string setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param prefix
	 */
	public void simplePathPrefix(CharSequence prefix) {
		synchronized ( this ) {
			this.simplePathPrefix = Objects.requireNonNull(prefix).toString();
		}
	}
	
	/**
	 * Returns path-suffix-string.
	 * 
	 * @return path-suffix-string
	 */
	public String simplePathSuffix() {
		synchronized ( this ) {
			return this.simplePathSuffix;
		}
	}
	
	/**
	 * Path-suffix-string setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param suffix
	 */
	public void simplePathSuffix(CharSequence suffix) {
		synchronized ( this ) {
			this.simplePathSuffix = Objects.requireNonNull(suffix).toString();
		}
	}
	
	/**
	 * Returns timestamp-format.
	 * 
	 * @return timestamp-format
	 */
	public DateTimeFormatter simpleTimestampFormat() {
		synchronized ( this ) {
			return this.simpleTimestampFormat;
		}
	}
	
	/**
	 * Timestamp-format setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param format
	 */
	public void simpleTimestampFormat(DateTimeFormatter format) {
		synchronized ( this ) {
			this.simpleTimestampFormat = Objects.requireNonNull(format);
		}
	}
	
}
