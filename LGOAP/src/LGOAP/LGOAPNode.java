package LGOAP;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import Agent.*;
import searchEngine.*;

//An LGOAPNode is a node on the search graph that knows:
//which action was taken to arrive at it
//What the current plan state is
//What the goal plan state is
public class LGOAPNode extends Node
{
	public LGOAPAction actionTaken;
	public WorldState currentPlanState;
	public WorldState goalPlanState;
	private List<Node> neighbours;
	private Agent agent;
	private Planner planner;
	
	//Creates a new LGOAP node
	public LGOAPNode (Agent agent, Planner planner)
	{
		currentPlanState = new WorldState();
		goalPlanState = new WorldState();
		neighbours = new ArrayList<Node>();
		this.agent = agent;
		this.planner = planner;
	}
	
	//Gets the GOAPNodes whose actions satisfy some variable in the goal plan state
	public List<Node> getNeighbours()
	{
		//For each world state property in the current plan state
		List<WorldStateProperty> currProps = currentPlanState.getValues();
		for (WorldStateProperty currProp : currProps)
		{
			//For each available action check if the action satisfies a goal plan state variable
			List<LGOAPAction> actions = planner.getActionManager().getActions();
			
			for (LGOAPAction action : actions)
			{
				//Check if the action satisfies a goal plan property
				//We also consider an action which partially satisfies a goal property now!!!!! AWESOME SAU
				boolean goalPartiallySatisfied = false;
				boolean goalCompletelySatisfied = false;
				
				if (action.effects.getPropertyValue(currProp.key).equals(goalPlanState.getPropertyValue(currProp.key)))
				{
					goalCompletelySatisfied = true;
				}
				else if (action.effects.containsKey(currProp.key))
				{
					if (action.effects.getPropertyValue(currProp.key).isSameType(goalPlanState.getPropertyValue(currProp.key).getValue()))
					{
						goalPartiallySatisfied = true;
					}	
				}
								
				if (goalCompletelySatisfied)
				{
					LGOAPNode neighbour = new LGOAPNode (this.agent, this.planner);
					neighbour.currentPlanState.copy(currentPlanState);
					neighbour.goalPlanState.copy(goalPlanState);
					neighbour.actionTaken = action.clone();
					neighbours.add(neighbour);
				}
				else if (goalPartiallySatisfied)
				{
					LGOAPNode neighbour = new LGOAPNode (this.agent, this.planner);
					neighbour.currentPlanState.partialCopy(currentPlanState, action, false);
					neighbour.goalPlanState.partialCopy(goalPlanState, action, true);
					neighbour.actionTaken = action.clone();
					neighbours.add(neighbour);
				}
				else
				{
					continue;
				}						
			}
		}
		
		return neighbours;
	}
	
}
