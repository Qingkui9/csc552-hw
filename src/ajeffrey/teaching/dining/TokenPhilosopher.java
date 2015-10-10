package ajeffrey.teaching.dining;

import ajeffrey.teaching.debug.Debug;

/**
 * A philosopher from the dining philosophers problem.
 * A philosopher thinks, picks up their left-hand fork,
 * picks up their right-hand fork, then eats.
 * Unfortunately, putting a collection of philosophers in a circle
 * can produce deadlock, if they all pick up their lh forks before any
 * of them have a chance to pick up their rh forks.
 * This is the second version to fix this problem.
 * @author Alan Jeffrey and Yuancheng Zhang
 * @version 1.0.2
 */
public interface TokenPhilosopher 
{
    /**
     * A factory for building token philosophers.
     */
    public static final PhilosopherFactory factory = new TokenPhilosopherFactoryImpl ();
}

class TokenPhilosopherFactoryImpl implements PhilosopherFactory 
{
	static public TokenPoolImpl pool = new TokenPoolImpl();

	public Philosopher build (final Comparable lhFork, final Comparable rhFork, final String name) 
	{
		pool.addNewToken();
		return new TokenPhilosopherImpl (lhFork, rhFork, name);
	}

}

class TokenPhilosopherImpl implements Runnable, Philosopher 
{
	final protected Object lhFork;
	final protected Object rhFork;
	final protected String name;
	final protected Thread thread;
	private Token t = null;

	protected TokenPhilosopherImpl (final Object lhFork, final Object rhFork, final String name) 
	{
		this.lhFork = lhFork;
		this.rhFork = rhFork;
		this.name = name;
		this.thread = new Thread (this);
		this.t = null;
	}

	public void start () 
	{
		thread.start ();
	}

	public void run () 
	{
		Debug.out.breakPoint (name + " is starting");
		try 
		{
			while (true) 
			{
				
				while (t == null) {
					delay ();
					t = TokenPhilosopherFactoryImpl.pool.getToken ();
				}
				Debug.out.println (name + " got Token " + t.getId());
				Debug.out.println (name + " is thinking");
				delay ();
				Debug.out.println (name + " tries to pick up " + lhFork);
				synchronized (lhFork) 
				{
					Debug.out.println (name + " picked up " + lhFork);
					delay ();
					Debug.out.println (name + " tries to pick up " + rhFork);
					synchronized (rhFork) 
					{
						Debug.out.println (name + " picked up " + rhFork);
						Debug.out.println (name + " starts eating");
						delay ();
						Debug.out.println (name + " finishes eating");
						TokenPhilosopherFactoryImpl.pool.putToken (t);
						Debug.out.println (name + " put Token " + t.getId());
						t = null;
					}
				}
			}
		} 
		catch (final InterruptedException ex) 
		{
			Debug.out.println (name + " is interrupted");
		}
	}

	protected void delay () throws InterruptedException 
	{
		Thread.currentThread().sleep ((long)(1000*Math.random()));
	}

}
