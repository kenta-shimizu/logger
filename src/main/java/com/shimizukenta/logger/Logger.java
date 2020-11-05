package com.shimizukenta.logger;

import java.io.Closeable;
import java.io.IOException;

public interface Logger extends Closeable {
	
	/**
	 * Open and start logging.
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException;
	
	/**
	 * Returns true if logging open and not close.
	 * 
	 * @return {@code true} if logging open and not close
	 */
	public boolean isOpen();
	
	/**
	 * Returns true if logging closed.
	 * 
	 * @return {@code true} if logging closed
	 */
	public boolean isClosed();
	
}
