package Actions;

import Agent.*;
import LGOAP.*;

public class M_BuildBioProduction extends LGOAPAction
{

	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasBioProduction", new WorldStateValue(true));
		
		lowerLayerPreconditions = new WorldState ();
		lowerLayerPreconditions.setProperty("HasBarracks", new WorldStateValue(3));
		
		cost = 3;
		priority = 1;		
		
		layer = 1;
	}

	public void activate(Agent agent, WorldState state) {
		
	}

	public boolean validate(Agent agent) {
		return true;
	}

	public boolean isComplete(Agent agent) {
		return true;
	}

	public boolean interrupt() {
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
		actualWorldState.setProperty("HasBioProduction", new WorldStateValue(true));	
	}
	
	
}
