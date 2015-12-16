package strong;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor implements Executor{

	ExecutorService service = Executors.newFixedThreadPool(5);
	public MyExecutor() {
	}
	@Override
	public void execute(Runnable command) {
		service.execute(command);
	}
	
}
