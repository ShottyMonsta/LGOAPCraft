package Actions;

import java.util.ArrayList;
import java.util.List;

import Agent.*;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import CraftPlanner.CraftPlannerMemoryFact;
import LGOAP.*;
import Orders.BuildOrder;
import Orders.BuildOrderStatus;
import Orders.TrainingOrder;
import Orders.TrainingOrderStatus;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

public class L_BuildOutpost extends LGOAPAction
{
	private List<TrainingOrder> trainingOrders;
	
	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasOutpost", new WorldStateValue(true));
		
		lowerLayerPreconditions = new WorldState ();
		
		trainingOrders = new ArrayList<TrainingOrder>();
		trainingOrders.clear();
		
		cost = 3;
		priority = 1;	
		
		layer = 0;
	}

	public boolean isComplete(Agent agent)
	{
		boolean isFinished = false;
		for (TrainingOrder order : trainingOrders)
		{
			if (order.status == TrainingOrderStatus.Finished)
			{
				continue;
			}
			else
			{
				return false;
			}
		}
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
		WorkingMemory workingMemory = agent.getWorkingMemory();
		WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerBuilding, null);
		
		int totalBarracks = 0;
		for (WorkingMemoryFact playerFact : playerFacts)
		{
			CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
			Unit playerUnit = (Unit) playerCraftFact.getFact();
			if (playerUnit.getType() == UnitType.Terran_Barracks)
			{
				totalBarracks += 1;
			}
		}
		
		if (totalBarracks < 1)
		{
			agent.getWorldState().setProperty("HasBarrack", new WorldStateValue(false));
			return false;
		}
		return true;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
		actualWorldState.setProperty("HasOutpost", new WorldStateValue(true));
	}
	
	
}
