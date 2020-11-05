/**
 * module-info.java.
 * 
 * <p>
 * Exclude this file, if using jdk8, or if it interferes on compile.
 * </p>
 * 
 * 
 * @author kenta-shimizu
 *
 */
module com.shimizukenta.secs {
	exports com.shimizukenta.logger;
	exports com.shimizukenta.logger.tcpiplogger;
	exports com.shimizukenta.logger.jsoncommunicatorlogger;
	requires transitive com.shimizukenta.jsonhub;	
	requires transitive com.shimizukenta.jsoncommunicator;
}
