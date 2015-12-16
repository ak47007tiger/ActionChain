package strong;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;

public class SingleTE extends Thread implements Executor {

	public SingleTE() {
		start();
	}
	
	Queue<Runnable> runs = new LinkedBlockingDeque<Runnable>(4);
	Object sign = new Object();
	@Override
	public void execute(Runnable command) {
		synchronized (sign) {
			runs.add(command);
			sign.notify();
		}
	}

	@Override
	public void run() {
		while (true) {
			synchronized (sign) {
				if (runs.size() == 0) {
					try {
						sign.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			runs.poll().run();
		}
	}

}
