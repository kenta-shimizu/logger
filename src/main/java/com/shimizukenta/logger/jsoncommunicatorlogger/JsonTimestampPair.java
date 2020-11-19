package com.shimizukenta.logger.jsoncommunicatorlogger;

import java.time.LocalDateTime;

public class JsonTimestampPair {
	
	private final String json;
	private final LocalDateTime timestamp;
	
	public JsonTimestampPair(String json, LocalDateTime timestamp) {
		this.json = json;
		this.timestamp = timestamp;
	}
	
	public JsonTimestampPair(String json) {
		this.json = json;
		this.timestamp = LocalDateTime.now();
	}
	
	public String json() {
		return this.json;
	}
	
	public LocalDateTime timestamp() {
		return this.timestamp;
	}
	
}
