package testserver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import com.shimizukenta.jsoncommunicator.JsonCommunicator;
import com.shimizukenta.jsoncommunicator.JsonCommunicators;

public class TestJsonCommunicatorServer {
	
	private static final AtomicInteger autoNum = new AtomicInteger(0);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	
	public final int num;
	public final String timestamp;
	
	public TestJsonCommunicatorServer() {
		this.num = autoNum.incrementAndGet();
		this.timestamp = LocalDateTime.now().format(formatter);
	}

	public static void main(String[] args) {
		
		final SocketAddress socketAddr = new InetSocketAddress("127.0.0.1", 10000);
		
		try (
				JsonCommunicator<?> comm = JsonCommunicators.createServer(socketAddr);
				) {
			
			comm.addLogListener(System.out::println);
			
			comm.open();
			
			for ( ;; ) {
				
				Thread.sleep(5000L);
				
				comm.send(new TestJsonCommunicatorServer());
			}
		}
		catch ( Throwable t ) {
			t.printStackTrace();
		}
	}

}
