
package ajeffrey.teaching.dining;

import ajeffrey.teaching.debug.Debug;

/**
 * A philosopher from the dining philosophers problem.
 * A philosopher thinks, picks up their left-hand fork,
 * picks up their right-hand fork, then eats.
 * Fixed the deadlock problem.
 * @author Alan Jeffrey and Yuancheng Zhang
 * @version 1.0.2
 */
public interface OrderedPhilosopher {

    /**
     * A factory for building deadlocking philosophers.
     */
    public static final PhilosopherFactory factory 
    = new OrderedPhilosopherFactoryImpl ();

}

class OrderedPhilosopherFactoryImpl implements PhilosopherFactory {

	public Philosopher build 
	(final Comparable lhFork, final Comparable rhFork, final String name) 
	{
		return new OrderedPhilosopherImpl (lhFork, rhFork, name);
	}

}

class OrderedPhilosopherImpl implements Runnable, Philosopher {

	final protected Object lhFork;
	final protected Object rhFork;
	final protected String name;
	final protected Thread thread;
	// EDITED by Yuancheng Zhang:
	// I created a "tie breaking" lock in case 
	// the left and right forks have same hash value. 
	private static final Object tieLock = new Object();

	protected OrderedPhilosopherImpl
	(final Object lhFork, final Object rhFork, final String name) 
	{
		this.lhFork = lhFork;
		this.rhFork = rhFork;
		this.name = name;
		this.thread = new Thread (this);
	}

	public void start () {
		thread.start ();
	}

	public void run () {
		Debug.out.breakPoint (name + " is starting");
		try {
			while (true) {
				Debug.out.println (name + " is thinking");
				delay ();
				Debug.out.println (name + " tries to pick up " + lhFork);

				// EDITED by Yuancheng Zhang:
				// I added HashCode to change lhFork Object to Integer.
				int lhForkHash = System.identityHashCode(lhFork);
				int rhForkHash = System.identityHashCode(rhFork);
				
				// EDITED by Yuancheng Zhang:
				// I used Integer to order locks.
				// The small-hash-value fork is picked first.
				if (lhForkHash < rhForkHash) {
					synchronized (lhFork) {
						Debug.out.println (name + " picked up " + lhFork);
						delay ();
						Debug.out.println (name + " tries to pick up " + rhFork);
						synchronized (rhFork) {
							Debug.out.println (name + " picked up " + rhFork);
							Debug.out.println (name + " starts eating");
							delay ();
							Debug.out.println (name + " finishes eating");
						}
					}
				} else if (lhForkHash > rhForkHash) {
					synchronized (rhFork) {
						Debug.out.println (name + " picked up " + rhFork);
						delay ();
						Debug.out.println (name + " tries to pick up " + lhFork);
						synchronized (lhFork) {
							Debug.out.println (name + " picked up " + lhFork);
							Debug.out.println (name + " starts eating");
							delay ();
							Debug.out.println (name + " finishes eating");
						}
					}
				} else {
					// If the hash values are same. 
					// Take the left fork first.
					synchronized (tieLock) {
						Debug.out.println (name + " picked up " + lhFork);
						delay ();
						Debug.out.println (name + " tries to pick up " + rhFork);
						synchronized (rhFork) {
							Debug.out.println (name + " picked up " + rhFork);
							Debug.out.println (name + " starts eating");
							delay ();
							Debug.out.println (name + " finishes eating");
						}
					}
				}
			}
		} catch (final InterruptedException ex) {
			Debug.out.println (name + " is interrupted");
		}
	}

	protected void delay () throws InterruptedException {
		Thread.currentThread().sleep ((long)(1000*Math.random()));
	}

}
