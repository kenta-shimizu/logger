package com.shimizukenta.logger.tcpiplogger;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class LinesTimestampPair {
	
	private final List<String> lines;
	private final LocalDateTime timestamp;
	
	public LinesTimestampPair(List<String> lines, LocalDateTime timestamp) {
		this.lines = lines;
		this.timestamp = timestamp;
	}
	
	public LinesTimestampPair(List<String> lines) {
		this.lines = lines;
		this.timestamp = LocalDateTime.now();
	}
	
	public List<String> lines() {
		return Collections.unmodifiableList(lines);
	}
	
	public LocalDateTime timestamp() {
		return timestamp;
	}
	
}
