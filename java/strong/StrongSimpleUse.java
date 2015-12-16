package strong;

import java.io.File;

import common.Action;
import common.Getter;
import common.Utils;

public class StrongSimpleUse {

	public static void main(String[] args) {
		String path = new File(System.getProperty("user.dir"),
				"files/linux0.11_src_chinese.zip").getPath();
		StrongWorker w0 = new StrongWorker(new SingleTE());
		StrongWorker w1 = new StrongWorker(new SingleTE());
		StrongWorker w2 = new StrongWorker(new SingleTE());
		StrongChain chain = new StrongChain();
		chain.workOn(w0).addAction(new Action<String, File>() {
			@Override
			public File getResult(String s) {
				Utils.ptn("action0");
				return new File(s);
			}

			@Override
			public void onError() {
			}
		}).workOn(w1).addAction(new Action<File, Long>() {
			@Override
			public void onError() {
			}

			@Override
			public Long getResult(File s) {
				Utils.ptn("action1");
				return s.length();
			}
		}).workOn(w2).addAction(new Action<Long, String>() {
			@Override
			public void onError() {
			}

			@Override
			public String getResult(Long s) {
				Utils.ptn("action2");
				return (float) s / 1024f + "kb";
			}
		}).work(new Getter<String>() {
			public void onGet(String t) {
				Utils.ptn("action3");
				System.out.println(t);
			}
		}, path);
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
