package com.shimizukenta.logger;

import java.time.LocalDateTime;

public class LogTimestampPair {
	
	private final CharSequence log;
	private final LocalDateTime timestamp;
	
	public LogTimestampPair(CharSequence log, LocalDateTime timestamp) {
		this.log = log;
		this.timestamp = timestamp;
	}
	
	public LogTimestampPair(CharSequence log) {
		this.log = log;
		this.timestamp = LocalDateTime.now();
	}
	
	public String log() {
		return log.toString();
	}
	
	public LocalDateTime timestamp() {
		return timestamp;
	}
	
}
