package common;

public interface Action<S,R> {
	R getResult(S s);
	void onError();
}
