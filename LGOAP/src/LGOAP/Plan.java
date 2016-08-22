package LGOAP;

import java.util.ArrayList;
import java.util.List;

import Agent.*;
import searchEngine.*;

//LGOAP plan defines functionality for actually using a plan at run time
//It contains methods for activation, deactivation, stepping through and checking if the plan is complete
public class Plan extends Path
{
	protected Agent agent;
	public LGOAPGoal goal;

	//Activates the plan
	public void activate ()
	{
		//As long as plan has more than 0 steps...
		if (steps.size() > 0)
		{
			//Set the current step to 0
			currentStep = 0;			
			
			//Get the current step
			LGOAPStep step = getCurrentLGOAPStep();
			//Get the first steps action
			LGOAPAction action = getCurrentAction();
			
			//If first action is null then return because there is no actions in the plan
			if (action == null)
			{
				return;
			}
			//NEED TO ADD CONTEXTUAL VALIDATION HERE!!!
			agent.metrics.addToExecution("Activating " +action +"...\n");
			action.activateAction (agent, step.stepState);
			if (action.isComplete(agent))
			{
				advanceStep();
			}
		}
	}
	
	public void addPlan (Plan planToAdd)
	{
		for (Step step : planToAdd.steps)
		{
			steps.add(step);
		}
	}
	
	public void addPlan (Plan planToAdd, Plan planToRecieve, LGOAPAction insertionAction)
	{
		Step insertionStep = findStepFromAction(planToRecieve, insertionAction);
		int insertionIndex = planToRecieve.getSteps().lastIndexOf(insertionStep);
		for (Step step : planToAdd.steps)
		{
			steps.add(insertionIndex, step);
		}
	}
	
	private Step findStepFromAction (Plan planToSearch, LGOAPAction actionToSearch)
	{
		for (Step step : planToSearch.getSteps())
		{
			LGOAPNode node = (LGOAPNode) step.node;
			if (node.actionTaken == actionToSearch)
			{
				return step;
			}
		}
		return null;
	}
	
	//Deactivates the plan
	public void deactivate ()
	{
		//Get the current step
		LGOAPStep step = getCurrentLGOAPStep();
		//Get the first steps action
		LGOAPAction action = getCurrentAction();
		
		if (action != null)
		{
			action.deactivateAction(agent);
		}
		//Set the current step to 0
		currentStep = 0;
	}
	
	//Returns true if the current step has completed
	public boolean isStepComplete ()
	{
		//If the entire plan is finished then return true
		if (isFinished())
		{
			return true;
		}
		
		//Get the current step
		LGOAPStep step = getCurrentLGOAPStep();
		//Get the first steps action
		LGOAPAction action = getCurrentAction();
		
		//If action is null then there is a problem
		if (action == null)
		{
			System.out.println("The action is null!");
		}
		//Return true if the action is complete, else false
		return action.isComplete(agent);
	}
	
	//Advances the plan to the next action in the queue
	public boolean advanceStep ()
	{
		while (true)
		{
			//Get the current step
			LGOAPStep thisStep = getCurrentLGOAPStep();

			//If the step exists then carry out the effects
			if (thisStep != null)
			{
				//Get the first steps action
				LGOAPAction action = getCurrentAction();
				
				if (action != null)
				{
					agent.metrics.addToExecution(action +" complete! Applying effect...\n\n");
					action.applyEffect (agent, agent.getWorldState(), thisStep.getWorldState());
					action.deactivateAction(agent);
				}
			}
			//Increase the current plan step
			currentStep += 1;
			//If the plan has finished pass processing back to caller
			if (isFinished ())
			{
				return false;
			}
			
			//Get the next step
			LGOAPStep nextStep = getCurrentLGOAPStep();
			
			//If the next step exists
			if (nextStep != null)
			{
				//Get the first steps action
				LGOAPAction action = getCurrentAction();
				
				//If the action exists
				if (action != null)
				{
					//TODO Validate the actions contextual preconditions!!!
					
					//Activate the action
					agent.metrics.addToExecution("Activating " +action +"...\n");
					action.activateAction(agent, nextStep.stepState);
					//If the action doesn't instantly complete then pass processing back to caller
					if (!action.isComplete(agent))
					{
						return true;
					}
					//Else deactivate the action
					action.deactivateAction(agent);
				}
			}
		}
	}
	
	//Returns true if the current action is still valid
	public boolean isValid (Agent agent)
	{
		return getCurrentAction().validateAction(agent);
	}
	
	public LGOAPAction getCurrentAction ()
	{
		//Get the current step
		LGOAPStep step = (LGOAPStep) getCurrentStep();
		//Get the first steps action
		LGOAPAction action = ((LGOAPNode) getCurrentStep().node).actionTaken;
		return action;
	}
	
	private LGOAPStep getCurrentLGOAPStep ()
	{
		//Get the current step
		LGOAPStep step = (LGOAPStep) getCurrentStep();
		return step;
	}

	public List<LGOAPAction> getActions ()
	{
		List<LGOAPAction> actions = new ArrayList<LGOAPAction>();
		actions.clear();
		for (Step step : steps)
		{
			LGOAPAction action = ((LGOAPNode) step.node).actionTaken;
			actions.add(action);
		}
		return actions;
	}
	
	
}
