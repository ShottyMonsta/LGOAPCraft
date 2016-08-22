package Actions;

import java.util.ArrayList;
import java.util.List;

import Agent.*;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import LGOAP.*;
import Orders.BuildOrder;
import Orders.BuildOrderStatus;
import bwapi.UnitType;

public class L_BuildBarrack extends LGOAPAction
{
	List<BuildOrder> buildOrders;
	
	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasBarracks", new WorldStateValue(3));
		
		lowerLayerPreconditions = new WorldState ();
		
		cost = 3;
		priority = 1;	
		
		buildOrders = new ArrayList<BuildOrder>();
		buildOrders.clear();
		
		layer = 0;
	}

	public boolean isComplete(Agent agent)
	{ 
		boolean complete = false;
		int completedBuildOrders = 0;
		for (BuildOrder buildOrder : buildOrders)
		{
			if (buildOrder.status == BuildOrderStatus.Finished)
			{
				completedBuildOrders += 1;
			}
		}
		if (completedBuildOrders == buildOrders.size())
		{
			return true;
		}
		return false;		
	}

	public boolean interrupt()
	{
		return false;
	}

	public void activateAction(Agent agent, WorldState stepState)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		for (int i = 0; i < 3; i++)
		{
			BuildOrder newBuildOrder = new BuildOrder (UnitType.Terran_Barracks, craftAgent.resourceActuator);
			buildOrders.add(newBuildOrder);
			((CraftPlannerBlackboard)agent.getBlackboard()).addBuildQueue(newBuildOrder);
		}

		
//		WorkingMemory workingMemory = agent.getWorkingMemory();
//		WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerBuilding, null);
//		
//		int requiredBarracks = 1;
//		for (WorkingMemoryFact playerFact : playerFacts)
//		{
//			CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
//			Unit playerUnit = (Unit) playerCraftFact.getFact();
//			if (playerUnit.getType() == UnitType.Terran_Barracks)
//			{
//				requiredBarracks -= 1;
//			}
//		}
//		if (requiredBarracks > 0)
//		{
//			CraftPlanner craftAgent = (CraftPlanner) agent;
//			for (int i = 0; i < requiredBarracks; i++)
//			{
//				buildOrder = new BuildOrder (UnitType.Terran_Barracks, craftAgent.resourceActuator);
//				((CraftPlannerBlackboard)agent.getBlackboard()).addBuildQueue(buildOrder);
//			}
//		}			
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
		//actualWorldState.setProperty("HasBarracks", new WorldStateValue(1));
	}
	
	
}
