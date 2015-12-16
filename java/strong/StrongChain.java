package strong;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import common.Action;
import common.Getter;

public class StrongChain {
	private StrongWorker curWorker;
	private Queue<Relation> actionQueue = new LinkedBlockingQueue<Relation>();
	Object sign = new Object();
	boolean pause = false;

	class Relation {
		Action<?, ?> action;
		StrongWorker worker;

		public Relation(Action<?, ?> action, StrongWorker worker) {
			this.action = action;
			this.worker = worker;
		}
	}

	public void stop() {
		pause();
		synchronized (actionQueue) {
			actionQueue.clear();
		}
		resume();
	}

	public void resume() {
		synchronized (sign) {
			pause = false;
			sign.notify();
		}
	}

	public void pause() {
		synchronized (sign) {
			pause = true;
		}
	}

	<S> void nextAction(S s) {
		synchronized (sign) {
			if (pause) {
				try {
					sign.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized (actionQueue) {
			if (actionQueue.size() > 0) {
				Relation relation = actionQueue.poll();
				@SuppressWarnings("unchecked")
				Action<S, ?> nextAction = (Action<S, ?>) relation.action;
				StrongWorker worker = relation.worker;
				worker.work(nextAction, s, this);
			}
		}
	}

	StrongChain addAction(Action<?, ?> action) {
		actionQueue.add(new Relation(action, curWorker));
		return this;
	}

	StrongChain workOn(StrongWorker worker) {
		curWorker = worker;
		return this;
	}

	<S, R> void work(final Getter<R> getter, S s) {
		addAction(new Action<R, Void>() {
			@Override
			public Void getResult(R r) {
				getter.onGet(r);
				return null;
			}

			@Override
			public void onError(Throwable t) {
			}
		});
		nextAction(s);
	}

	<S> void work(S s) {
		nextAction(s);
	}
}
