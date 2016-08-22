package LGOAP;

import Agent.Agent;
import searchEngine.Step;


public class PlanManager
{
	//The planner component
	public LGOAPPlanner planner;
		
	//The plan to me managed
	public Plan plan;

	//The agent that owns this component
	private Agent agent;
	
	//Creates a new layered plan manager
	public PlanManager (Agent agent)
	{
		planner = new LGOAPPlanner (agent);
		this.agent = agent;
	}
	
	public LGOAPPlanner getPlanner ()
	{
		return planner;
	}
	
	public boolean validatePlan ()
	{
		if (!plan.getCurrentAction().validateAction(agent))
		{
			return false;
		}
		return true;
	}
	
	public void update ()
	{
		if (plan == null)
		{
			agent.metrics.addToPlan("No active plan (null)! Creating one...");
			plan = planner.getLayeredPlan(planner.getGoalManager().getMostRelevantGoal());
			agent.metrics.addToPlan("Plan found! Plan Size: " +plan.getSteps().size() +"\n");
			agent.metrics.addToPlan("____________\n\n");
			for (Step step : plan.getSteps())
			{
				LGOAPAction actionTaken = ((LGOAPNode) step.node).actionTaken;
				agent.metrics.addToPlan(actionTaken.toString() +"\n");
			}
			
			plan.activate();
		}
		else if (plan.size() == 0)
		{
			agent.metrics.addToPlan("No active plan (size 0)! Creating one...\n\n");
			plan = planner.getLayeredPlan(planner.getGoalManager().getMostRelevantGoal());
			agent.metrics.addToPlan("Plan found! Plan Size: " +plan.getSteps().size() +"\n");
			agent.metrics.addToPlan("____________\n\n");
			for (Step step : plan.getSteps())
			{
				LGOAPAction actionTaken = ((LGOAPNode) step.node).actionTaken;
				agent.metrics.addToPlan(actionTaken.toString() +"\n");
			}
			
			plan.activate();
		}
		else if (!validatePlan())
		{
			agent.metrics.addToPlan("____________\n");
			agent.metrics.addToPlan("____________");
			agent.metrics.addToPlan("\n\nCurrent Agent state:\n\n");
			for (String key : agent.getWorldState().getProperties().keySet())
			{
				agent.metrics.addToPlan(key +": " +agent.getWorldState().getProperties().get(key).value +"\n");
			}
			agent.metrics.addToPlan("\n\nPlan invalidated on " +plan.getCurrentAction() +", replanning!\n\n");
			plan = planner.getLayeredPlan(planner.getGoalManager().getMostRelevantGoal());
			agent.metrics.addToPlan("Plan found! Plan Size: " +plan.getSteps().size() +"\n");
			agent.metrics.addToPlan("____________\n\n");
			for (Step step : plan.getSteps())
			{
				LGOAPAction actionTaken = ((LGOAPNode) step.node).actionTaken;
				agent.metrics.addToPlan(actionTaken.toString() +"\n");
			}
			
			plan.activate();
		}
		else if (!plan.isFinished())
		{
			if (plan.isStepComplete())
			{
				plan.advanceStep();
			}
		}		
		else if (plan.isFinished())
		{
			agent.metrics.addToPlan("Plan finished! Creating new plan...\n\n");
			plan = planner.getLayeredPlan(planner.getGoalManager().getMostRelevantGoal());
			agent.metrics.addToPlan("Plan found! Plan Size: " +plan.getSteps().size() +"\n");
			agent.metrics.addToPlan("____________\n\n");
			for (Step step : plan.getSteps())
			{
				LGOAPAction actionTaken = ((LGOAPNode) step.node).actionTaken;
				agent.metrics.addToPlan(actionTaken.toString() +"\n");
			}
			
			plan.activate();
		}
	}
	
}
