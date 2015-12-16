package strong;

import java.util.concurrent.Executor;

import common.Action;

public class StrongWorker {

	Executor executor;

	public StrongWorker() {
	}
	
	
	
	public StrongWorker(Executor executor) {
		this.executor = executor;
	}



	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public <S> void work(final Action<S,?> action, final S s, final StrongChain chain) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				chain.nextAction(action.getResult(s));
			}
		});
	}
}
