package LGOAP;

import java.util.List;
import Agent.*;

//This is the base class for an LGOAP action
//It needs to be implemented in a concrete action to define the abstract methods
//It contains a world state that represents the effects
//And a world state that represents the preconditions
//The cost is used to guide the graph search
//The priority is used to break ties in the graph search (lower = higher priority)
public abstract class LGOAPAction implements Cloneable
{
	public WorldState effects;
	public WorldState preconditions;
	public WorldState lowerLayerPreconditions;
	public int cost;
	public int priority;
	public int layer;
	
	//Clones the action, for use in planning
	public LGOAPAction clone ()
	{
		LGOAPAction clone = null;
		try
		{
			clone = (LGOAPAction) super.clone();
			clone.preconditions = new WorldState();
			clone.preconditions.copy(preconditions);
			clone.lowerLayerPreconditions = new WorldState();
			clone.lowerLayerPreconditions.copy(lowerLayerPreconditions);
			clone.effects = new WorldState();
			clone.effects.copy(effects);
			clone.cost = cost;
			clone.priority = priority;
			clone.layer = layer;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return clone;
	}
	
	//Creates a new action
	public LGOAPAction ()
	{
		setupConditions();
	}
	
	//Sets up the preconditions and effects of the action
	protected abstract void setupConditions();	
	
	
	//Returns true if the passed goal state satisfies the actions lower layer preconditions
	public boolean isLowerLayerPreconditionsSatisfied (WorldState lowerLayerGoalState)
	{
		
		for (WorldStateProperty prop : lowerLayerPreconditions.getValues())
		{
			if (lowerLayerGoalState.containsKey(prop.key))
			{
				if (lowerLayerGoalState.getPropertyValue(prop.key).equals(prop.value))
				{
					continue;
				}
				return false;
			}
			return false;
		}
		return true;
	}
	
	//Updates the planners goal state preconditions (used in planning)
	public void updateGoalStatePreconditions (WorldState goalState)
	{
		List<WorldStateProperty> precons = preconditions.getValues();
		for (WorldStateProperty precon : precons)
		{
			goalState.setProperty(precon.key, (WorldStateValue) precon.value.clone());
		}
	}	
	
	//Updates the planners current state preconditions (used in planning)
	public void updateCurrentStatePreconditions (WorldState currentState)
	{
		List<WorldStateProperty> effs = effects.getValues();
		for (WorldStateProperty effect : effs)
		{
			currentState.setProperty(effect.key, (WorldStateValue) effect.value.clone());
		}
	}
	
	//Returns the actions information as a String (for debugging)
	public String toString ()
	{
		String retn = this.getClass().getName();		
//		retn += ", Preconditions = ";
//		List<WorldStateProperty> precons = preconditions.getValues();
//		for (WorldStateProperty prop : precons)
//		{
//			retn += prop.key;	
//		}
//		retn += ", Effects = ";
//		List<WorldStateProperty> effs = effects.getValues();
//		for (WorldStateProperty prop : effs)
//		{
//			retn += prop.key;
//		}
		return retn;
	}
	
	public abstract void activateAction(Agent agent, WorldState stepState);
	public abstract void deactivateAction (Agent agent);
	public abstract boolean isComplete(Agent agent);
	public abstract boolean validateAction(Agent agent);
	public abstract void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect);

	
}
