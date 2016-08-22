package LGOAP;

import Agent.*;

//GOAP goal describes the world state that the planner wishes to satisfy
//Have to provide a concrete implementation of this for each goal that an agent may have
//Contains:
//A world state the represents the goal state that the planner is trying to reach
//A float that represents the goals relevance, this is updated at run time depending on the state of the world
public class LGOAPGoal
{
	public WorldState goalState;
	public float relevance;
	
	//Creates a new LGOAP goal
	public LGOAPGoal ()
	{
		this.goalState = new WorldState();
	}
	
	public LGOAPGoal (WorldState goalState)
	{
		this.goalState = goalState.clone();
		for (WorldStateProperty prop : goalState.getProperties().values())
		{
			this.goalState.setProperty(prop.key, prop.value);
			this.relevance = 1.0f;
		}		
	}
	
	//Sets the properties of the goal state in the planner
	public void setWorldStateSatisfactionConditions(WorldState worldState)
	{
		for (WorldStateProperty prop : goalState.getValues())
		{
			worldState.setProperty(prop.key, prop.value);
		}
	}
	
	//Returns true if the passed world state matches the goal state (i.e. planning has finished)
	public boolean isSatisfied (WorldState worldStateToCheck)
	{
		for (WorldStateProperty prop : goalState.getValues())
		{
			if (!worldStateToCheck.containsKey(prop.key))
			{
				continue;
			}
			if (!worldStateToCheck.getPropertyValue(prop.key).equals(prop.value))
			{
				return false;
			}
		}
		return true;
	}
	
	public void updateRelevance () 
	{
		
	}
	
}
