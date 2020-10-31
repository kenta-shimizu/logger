package com.shimizukenta.logger;

import java.io.IOException;

public interface Logger {
	
	/**
	 * Write log to file.
	 * 
	 * @param log
	 * @throws IOException
	 */
	public void write(CharSequence log) throws IOException;
}
