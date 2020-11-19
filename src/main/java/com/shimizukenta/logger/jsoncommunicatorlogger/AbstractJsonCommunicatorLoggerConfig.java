package com.shimizukenta.logger.jsoncommunicatorlogger;

import java.io.Serializable;

import com.shimizukenta.jsoncommunicator.JsonCommunicatorConfig;
import com.shimizukenta.jsonhub.JsonHubPrettyPrinterConfig;

/**
 * This class is configuration of AbstractJsonCommunicatorLogger.
 * 
 * @author kenta-shimizu
 *
 */
public abstract class AbstractJsonCommunicatorLoggerConfig implements Serializable {
	
	private static final long serialVersionUID = 7342596743017884202L;
	
	private boolean isEcho;
	
	public AbstractJsonCommunicatorLoggerConfig() {
		this.isEcho = true;
	}
	
	/**
	 * Echo setter
	 * 
	 * @param f
	 */
	public void isEcho(boolean f) {
		synchronized ( this ) {
			this.isEcho = f;
		}
	}
	
	/**
	 * Returns {@code true} if is echo.
	 * 
	 * @return {@code true} if is echo
	 */
	public boolean isEcho() {
		synchronized ( this ) {
			return this.isEcho;
		}
	}
	
	private final JsonCommunicatorConfig jsonCommConfig = new JsonCommunicatorConfig();
	
	/**
	 * Returns JsonCommunicatorConfig instance.
	 * 
	 * @return JsonCommunicatorConfig instance
	 */
	public JsonCommunicatorConfig jsonCommunicatorConfig() {
		return this.jsonCommConfig;
	}
	
	private final JsonHubPrettyPrinterConfig printerConfig = JsonHubPrettyPrinterConfig.defaultConfig();
	
	public JsonHubPrettyPrinterConfig jsonHubPrettyPrinter() {
		return this.printerConfig;
	}
	
}
