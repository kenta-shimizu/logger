package com.shimizukenta.logger.tcpiplogger;

public class AbstractLineLimitTcpIpLoggerConfig extends AbstractTcpIpLoggerConfig {
	
	private static final long serialVersionUID = 8779757300046616281L;
	
	private int lineLimit;
	
	public AbstractLineLimitTcpIpLoggerConfig() {
		super();
		this.lineLimit = -1;
	}
	
	/**
	 * Line limit of file setter.
	 * 
	 * <p>
	 * To ignore limit, set {@code <=0} value.
	 * </p>
	 * 
	 * @param limit of file lines
	 */
	public void lineLimit(int limit) {
		synchronized ( this ) {
			this.lineLimit = limit;
		}
	}
	
	/**
	 * Returns line limit of file.
	 * 
	 * @return line limit of file
	 */
	public int lineLimit() {
		synchronized ( this ) {
			return this.lineLimit;
		}
	}
	
}
