package com.jmzc;

import java.util.ArrayList;
import java.util.List;

import com.jmzc.balancer.Balancer;

public class TestMain {


	public static void main(String[] args) 
	{
		try
		{
			List<String> l = new ArrayList<String>();
			l.add("PRIMARIO");
			l.add("SECUNDARIO");
			Balancer<String> balancer = new Balancer<String>(l);
			
			for (int i=1; i <=100; i++)
			{
				Balancer<String>.Entry e = balancer.next();
				System.out.println("Candidato:" + e.getObject());
				if (i == 15)
				{
					System.out.println("Candidato:" + e.getObject() + " es malo");
					e.close(0);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
