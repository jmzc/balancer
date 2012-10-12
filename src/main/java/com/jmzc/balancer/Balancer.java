package com.jmzc.balancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Balancer<E>
{

	
	private List<E> l0 = new ArrayList<E>();
	private List<E> l1 = new ArrayList<E>();
	
	/*
	private Map<Integer,E> h0 = new HashMap<Integer,E>();
	private Map<Integer,E> h1 = new HashMap<Integer,E>();
	*/
	
	
	private Random random = new Random();
	

	public Balancer(List<E> l) 
	{
		for(int i=0; i < l.size(); i++)
		{
			l1.add(l.get(i));
		}	
	}
	
	/**
	 * 
	 * Returns the best candidate 
	 * 
	 */
	public synchronized Entry<E> next() throws Exception
	{
		
		int s = this.select();
		if (s == 0)
		{
			int i = random.nextInt(l0.size());
			
			if ( l0.get(i) != null)
				return new Entry<E>(i, l0.get(i),this);	
			else
			{
				int j = random.nextInt(l1.size());
				if ( l1.get(j) == null)
					throw new Exception();
				
				return new Entry<E>(j, l1.get(j),this);	
				
			}
		} 
		else
		{
			int i = random.nextInt(l1.size());
			
			if ( l1.get(i) != null)
				return new Entry<E>(i, l1.get(i),this);	
			else
			{
				int j = random.nextInt(l0.size());
				
				if ( l1.get(j) == null)
					throw new Exception();
				
				return new Entry<E>(j, l0.get(i),this);	
			}
		}
			
		
	}
	
	/*
	 	double	nextDouble() 
          	Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
 		float	nextFloat() 
          	Returns the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this random number generator's sequence.
 		double	nextGaussian() 
          	Returns the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.   
	 */
	private int select()
	{
		double n = random.nextDouble() ;
		int i = (n <= 1/3 ? 1 * l0.size() : 0 )* 1/3 + ( n > 1/3 ? 1 * l1.size() : 0) * + 2/3;
		
		if (i == 1/3)
			return 0;
		else
			return 1;
	}
	
	
	private void close(Entry<? extends E> e)
	{
		l0.remove(e.getObject());
		l1.remove(e.getObject());
		
		if ( e.getR() == 1 )
			l1.add(e.getObject());
		else
			l0.add(e.getObject());
			
		
	}

	/**
	 * 
	 * Wrapper for giving identity to object
	 * 
	 * @author jmzc
	 *
	 */
	public class Entry<T extends E> extends Object
	{
		
		// Identity
		private int id;

		// Object
		private T object;

		// Result reported by user
		private int r; 

		// Callback
		private Balancer<E> b; 

		
		private Entry(int id, T object, Balancer<E> b)
		{
			super();
			this.id = id;
			this.object = object;
			this.b = b;
		}


		private void setR(int r) 
		{
			this.r = r;
		}
		
		private int getR() 
		{
			return this.r;
		}


		public int getID()
		{
			return this.id;
		}

		public T getObject() 
		{
			return object;
		}
		
		public void close(int i)
		{
			if (i > 0)
				this.setR(1);
			else
				this.setR(0);
			
			this.b.close(this);
			
		}
		
		@Override 
		@SuppressWarnings("unchecked")
		public boolean equals(Object o)
		{
			if (o instanceof Entry)
			{
				
				Entry<T> e = (Entry<T>)o;
				if ( this.getID() == e.getID())
					return true;
				else
					return false;
			}
			else
				return false;
			
			
		}

		
	}
	
	
	
}
