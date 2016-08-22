package LGOAP;

import Agent.*;
import searchEngine.*;

//This is the concrete implementation of the graph search engine
//It provides the functionality for creating an actual plan once the search is complete
public class LGOAPEngine extends Engine
{
	public Agent agent;
	
	//Creates a new LGOAP engine
	public LGOAPEngine (Utils utils, Agent agent)
	{
		super (utils);
		this.agent = agent;
	}

	//Creates an actual plan when the graph search is completed
	public Path createPlan(Node current)
	{
		Node curr = current;
		Plan plan = new Plan();
		plan.agent = agent;
		LGOAPStep step;
		LGOAPAction action;
		while (curr.parent != null)
		{			
			step = new LGOAPStep();
			action = ((LGOAPNode)curr).actionTaken;

			if (action == null)
			{
				return null;
			}
			step.node = curr;
			step.stepState = ((LGOAPNode)curr).goalPlanState;
			plan.addStep(step);
			curr = curr.parent;
		}
		
		plan.goal = ((LGOAPUtils)utils).goal;
		
		return plan;
	}
}
