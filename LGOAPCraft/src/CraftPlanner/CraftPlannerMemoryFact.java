package CraftPlanner;

import Agent.FactType;
import Agent.WorkingMemoryFact;

public class CraftPlannerMemoryFact<T> extends WorkingMemoryFact
{	
	private double confidence;
	private int count;
	
	public CraftPlannerMemoryFact (FactType type, T fact)
	{
		super(type, fact);
	}
	
	public void setConfidence (double confidence)
	{
		this.confidence = confidence;
	}
	
	public double getConfidence ()
	{
		return confidence;
	}
	
	public void increaseCount ()
	{
		count += 1;
	}
	
	public void increaseCount (int toAdd)
	{
		count += toAdd;
	}
	
	public void decreaseCount ()
	{
		count -= 1;
	}
	
	public int getCount ()
	{
		return count;
	}
	
	public void setCount (int count)
	{
		this.count = count;
	}
	
}
