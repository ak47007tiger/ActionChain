package strong;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import common.Action;
import common.Getter;

public class StrongChain {
	private StrongWorker curWorker;
	private Queue<Relation> actionQueue = new LinkedBlockingQueue<Relation>();
	class Relation{
		Action<?, ?> action;
		StrongWorker worker;
		public Relation(Action<?, ?> action, StrongWorker worker) {
			this.action = action;
			this.worker = worker;
		}
	}
	
	<S> void nextAction(S s) {
		if (actionQueue.size() > 0) {
			Relation relation = actionQueue.poll();
			@SuppressWarnings("unchecked")
			Action<S, ?> curAction = (Action<S, ?>) relation.action;
			StrongWorker worker = relation.worker;
			worker.work(curAction, s, this);
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
			public void onError() {
			}
		});
		nextAction(s);
	}
	
	<S> void work(S s){
		nextAction(s);
	}
}
