package Actions;

import Agent.Agent;
import Agent.WorldState;
import Agent.WorldStateValue;
import LGOAP.LGOAPAction;

public class H_LaunchAttack extends LGOAPAction
{
	public void setupConditions()
	{
		preconditions = new WorldState ();
		preconditions.setProperty("HasBioArmy", new WorldStateValue (true));
		
		effects = new WorldState();
		effects.setProperty("HasLaunchedAttack", new WorldStateValue (true));
		
		lowerLayerPreconditions = new WorldState ();
		lowerLayerPreconditions.setProperty("HasAdvanced", new WorldStateValue(true));
		
		cost = 3;
		priority = 1;
		
		layer = 2;
	}

	public void activate(Agent agent, WorldState state)
	{
		
	}

	public boolean isComplete(Agent agent)
	{
		return false;
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
		return true;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
		actualWorldState.setProperty("HasLaunchedAttack", new WorldStateValue(true));	
	}	
}
