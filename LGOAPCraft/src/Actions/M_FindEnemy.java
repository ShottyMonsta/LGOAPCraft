package Actions;

import Agent.*;
import LGOAP.*;

public class M_FindEnemy extends LGOAPAction
{

	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasFoundEnemy", new WorldStateValue(true));
		
		lowerLayerPreconditions = new WorldState ();
		lowerLayerPreconditions.setProperty("HasScouted", new WorldStateValue(true));
		
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
		actualWorldState.setProperty("HasFoundEnemy", new WorldStateValue(true));	
	}
	
	
}
