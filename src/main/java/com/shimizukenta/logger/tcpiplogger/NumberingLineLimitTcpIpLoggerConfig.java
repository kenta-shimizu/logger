package com.shimizukenta.logger.tcpiplogger;

import java.util.Objects;

public class NumberingLineLimitTcpIpLoggerConfig extends AbstractLineLimitTcpIpLoggerConfig {
	
	private static final long serialVersionUID = 7252659130615572472L;
	
	private static final String DEFAULT_SIMPLE_PATH_PREFIX = "log_";
	private static final String DEFAULT_SIMPLE_PATH_SUFFIX = ".log";
	private static final String DEFAULT_SIMPLE_NUMBERING_FORMAT = "%06d";
	
	private String fullNumberingFormatPath;
	private String simplePathPrefix;
	private String simplePathSuffix;
	private String simpleNumberingFormat;
	
	public NumberingLineLimitTcpIpLoggerConfig() {
		super();
		this.fullNumberingFormatPath = null;
		this.simplePathPrefix = DEFAULT_SIMPLE_PATH_PREFIX;
		this.simplePathSuffix = DEFAULT_SIMPLE_PATH_SUFFIX;
		this.simpleNumberingFormat = DEFAULT_SIMPLE_NUMBERING_FORMAT;
	}
	
	/**
	 * Returns full-numbering-format used by {@link String#format(String, Object...)}.
	 * 
	 * @return full-numbering-format
	 */
	public String fullNumberingFormat() {
		synchronized ( this ) {
			return this.fullNumberingFormatPath;
		}
	}
	
	/**
	 * Full-numbering-format used by {@link String#format(String, Object...)} setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param format
	 */
	public void fullNumberingFormat(CharSequence format) {
		synchronized ( this ) {
			this.fullNumberingFormatPath = Objects.requireNonNull(format).toString();
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
	 * Returns Number-Format used by {@link String#format(String, Object...)}.
	 * 
	 * @return Number-Format
	 */
	public String simpleNumberingFormat() {
		synchronized ( this ) {
			return this.simpleNumberingFormat;
		}
	}
	
	/**
	 * Numbering-Format used by {@link String#format(String, Object...)} setter.
	 * 
	 * <p>
	 * Not accept {@code null}.
	 * </p>
	 * 
	 * @param format
	 */
	public void simpleNumberingFormat(CharSequence format) {
		synchronized ( this ) {
			this.simpleNumberingFormat = Objects.requireNonNull(format).toString();
		}
	}
	
}
