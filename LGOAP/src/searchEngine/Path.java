package searchEngine;

import java.util.ArrayList;
import java.util.List;

public class Path
{
	protected List<Step> steps;
	protected int currentStep;
	
	//Default constructor instantiates the steps list
	public Path ()
	{
		steps = new ArrayList<Step>();
		steps.clear();
		currentStep = 0;		
	}
	
	//Adds a step to the plan
	public void addStep (Step step)
	{
		steps.add(step);
	}
	
	//Returns true if the plan is finished
	public boolean isFinished ()
	{
		if (currentStep < steps.size() - 1)
		{
			return false;
		}
		return true;
	}
	
	//Returns the size of the plan
	public int size ()
	{
		return steps.size();
	}
	
	public List<Step> getSteps ()
	{
		return steps;
	}
	
	public Step getStep (int index)
	{
		return steps.get(index);
	}
	
	public Step getCurrentStep ()
	{
		return steps.get(currentStep);
	}

}
