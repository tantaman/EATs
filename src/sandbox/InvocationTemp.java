package sandbox;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.tantaman.commons.concurrent.NamedThreadFactory;
import com.tantaman.commons.concurrent.throttler.AccumulativeRunnable;
import com.tantaman.commons.concurrent.throttler.InvocationCombiner;

public class InvocationTemp {
	public static void main(String[] args) {
		InvocationCombiner<Integer> throttler = 
			new InvocationCombiner<Integer>(
					new AccumulativeRunnable<Integer>() {
						public void run(LinkedList<Integer> pParams) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(pParams.getLast());
						};
					}, 0, TimeUnit.MILLISECONDS, Executors.newScheduledThreadPool(1, new NamedThreadFactory("Combiner")));
		
		int i = 0;
		while (true) {
			throttler.invoke(++i);
		}
	}
}
