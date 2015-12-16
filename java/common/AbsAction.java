package common;

public abstract class AbsAction<S, R> implements Action<S, R>{

	@Override
	public abstract R getResult(S s);

	@Override
	public void onError(Throwable t) {
	}

}
