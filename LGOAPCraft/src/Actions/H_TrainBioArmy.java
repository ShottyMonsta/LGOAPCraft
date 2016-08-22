package Actions;

import Agent.Agent;
import Agent.WorldState;
import Agent.WorldStateValue;
import CraftPlanner.CraftPlanner;
import LGOAP.LGOAPAction;

public class H_TrainBioArmy extends LGOAPAction
{
	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasBioArmy", new WorldStateValue (true));
		
		lowerLayerPreconditions = new WorldState ();
		lowerLayerPreconditions.setProperty("HasBioSquads", new WorldStateValue(5));
		
		cost = 3;
		priority = 1;
		
		layer = 2;
	}

	public void activate(Agent agent, WorldState state)
	{
		
	}

	public boolean isComplete(Agent agent)
	{
		return true;
	}

	public boolean interrupt()
	{
		return false;
	}

	public void activateAction(Agent agent, WorldState stepState)
	{
		
	}

	public void deactivateAction(Agent agent)
	{

	}

	public boolean validateAction(Agent agent)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		int totalSquads = ((Integer)craftAgent.getWorldState().getPropertyValue("HasBioSquads").getValue()).intValue();
		if (totalSquads < 5)
		{
			return false;
		}
		return true;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
		actualWorldState.setProperty("HasBioArmy", new WorldStateValue(true));	
	}	
}
