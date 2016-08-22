package Actions;

import Agent.*;
import CraftPlanner.CraftPlanner;
import LGOAP.*;

public class M_TrainBioSquad extends LGOAPAction
{
	private int initialMarines;
	
	public void setupConditions()
	{
		preconditions = new WorldState ();
		preconditions.setProperty("HasBioProduction", new WorldStateValue(true));
		
		effects = new WorldState();
		effects.setProperty("HasBioSquads", new WorldStateValue(1));
		
		lowerLayerPreconditions = new WorldState ();
		lowerLayerPreconditions.setProperty("HasMarines", new WorldStateValue(9));
		
		cost = 3;
		priority = 1;
		
		layer = 1;
	}

	public void activate(Agent agent, WorldState state)
	{
		
	}

	public boolean validate(Agent agent)
	{
		return true;
	}

	public boolean isComplete(Agent agent)
	{
		return true;
	}

	public boolean interrupt() {
		return false;
	}

	public void activateAction(Agent agent, WorldState stepState)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		initialMarines = ((Integer)craftAgent.getWorldState().getPropertyValue("HasMarines").getValue()).intValue();
	}

	public void deactivateAction(Agent agent)
	{

	}

	public boolean validateAction(Agent agent)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		int currentMarines = ((Integer)craftAgent.getWorldState().getPropertyValue("HasMarines").getValue()).intValue();
		if (currentMarines < initialMarines + 9)
		{
			craftAgent.getWorldState().setProperty("HasMarines", new WorldStateValue(initialMarines));
			return false;
		}
		return true;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
		int totalBioSquads = 0;
		if (actualWorldState.getProperties().containsKey("HasBioSquads"))
		{
			Object oBioSquads = actualWorldState.getProperties().get("HasBioSquads").value.getValue();
			totalBioSquads = ((Integer)oBioSquads).intValue();
		}
		
		totalBioSquads += 1;
		actualWorldState.setProperty("HasBioSquads", new WorldStateValue(totalBioSquads));	
	}
	
	
}
