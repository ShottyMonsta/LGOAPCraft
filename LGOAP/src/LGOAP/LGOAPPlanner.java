package LGOAP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import Agent.*;
import searchEngine.Step;

//This object is used to bring together the various planning systems into a single planner object
public class LGOAPPlanner
{
	private List<Planner> planners;
	private LGOAPGoalManager goalManager;
	private Agent agent;
	
	//Creates a new GOAP planner
	public LGOAPPlanner (Agent agent)
	{
		this.agent = agent;
		planners = new ArrayList<Planner>();
		planners.clear();
		for (int i = 0; i <= 2; i++)
		{
			planners.add(new Planner(this.agent, i));
		}
		goalManager = new LGOAPGoalManager();
	}
	
	public void addActionToPlanner (LGOAPAction actionToAdd, int layer)
	{
		planners.get(layer).actionManager.addAction(actionToAdd);
	}
	
	public void addGoal (LGOAPGoal goalToAdd)
	{
		goalManager.addGoal(goalToAdd);
	}
	
	//Returns a layered plan given the global goal
	public Plan getLayeredPlan (LGOAPGoal globalGoal)
	{
		long startTime = System.nanoTime();
		long totalTime = 0;
		int L = planners.size() - 1;
		Plan plan = planners.get(L).getPlan(globalGoal);		
		
		for (int l = L-1; l >= 0; l--)
		{			
			for (LGOAPAction action : plan.getActions())
			{
				if (action.layer == l + 1)
				{
					Plan newPlan = planners.get(l).getPlan(new LGOAPGoal (action.lowerLayerPreconditions));  
					newPlan = orderPlan(newPlan);
					plan.addPlan(newPlan, plan, action);
				}
			}
		}
		
		totalTime = System.nanoTime() - startTime;
		System.out.println("Algo time: " +totalTime);
		
		return plan;
	}
	
	public Plan orderPlan (Plan planToOrder)
	{
		boolean isCompound = false;
		List<String> planActionKeySet = new ArrayList<String>();
		for (Step step : planToOrder.getSteps())
		{
			LGOAPNode node = (LGOAPNode) step.node;			
			Set<String> keySet = node.actionTaken.effects.getProperties().keySet();
			for (String key : keySet)
			{
				planActionKeySet.add(key);
				if (Collections.frequency(planActionKeySet, key) > 1)
				{
					isCompound = true;
					break;
				}
			}
		}

		Plan orderedPlan;				
		if (isCompound)
		{
			orderedPlan = new Plan();
			boolean isComplete = false;
			while (!isComplete)
			{
				int indexOfStepToRemove = 0;
				for (Step step : planToOrder.getSteps())
				{
					LGOAPNode node = (LGOAPNode) step.node;
					LGOAPAction action = node.actionTaken;
					boolean isSatisfied = true;
										
					List<String> otherActionKeys = new ArrayList<String>();
					for (Step otherStep : planToOrder.getSteps())
					{
						LGOAPNode otherNode = (LGOAPNode) otherStep.node;
						LGOAPAction otherAction = otherNode.actionTaken;
						otherActionKeys.addAll(otherAction.effects.getProperties().keySet());
					}
					boolean containsKey = false;
					for (String key : otherActionKeys)
					{
						if (action.preconditions.getProperties().containsKey(key))
						{
							containsKey = true;
							break;
						}
					}
					if (!containsKey)
					{
						indexOfStepToRemove = planToOrder.getSteps().lastIndexOf(step);
						isSatisfied = false;
						break;
					}
					if (!isSatisfied)
					{
						break;
					}
				}
				orderedPlan.addStep(planToOrder.getSteps().remove(indexOfStepToRemove));
				if (planToOrder.getSteps().size() == 0)
				{
					isComplete = true;
				}
			}			
		}
		else
		{
			orderedPlan = planToOrder;
		}
		
		for (Step step : orderedPlan.getSteps())
		{
			LGOAPNode node = (LGOAPNode) step.node;
		}
		
		Collections.reverse(orderedPlan.getSteps());
		
		//TODO order the plan...
		return orderedPlan;
	}
	
	private long getAlgoTime ()
	{
		//TODO measure algorithm time
		//Both GOAP and LGOAP
		//Compare for results...
		return 1;
		//TODO ^ obviously return actual result
	}
		
	public LGOAPGoalManager getGoalManager ()
	{
		return goalManager;
	}
}