package Actions;

import java.util.ArrayList;
import java.util.List;
import Agent.*;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import LGOAP.*;
import Orders.TrainingOrder;
import Orders.TrainingOrderStatus;
import bwapi.UnitType;

public class L_TrainMarines extends LGOAPAction
{
	private List<TrainingOrder> trainingOrders;
	
	
	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasMarines", new WorldStateValue(9));
		
		lowerLayerPreconditions = new WorldState ();
		
		trainingOrders = new ArrayList<TrainingOrder>();
		trainingOrders.clear();
		
		cost = 3;
		priority = 1;	
		
		layer = 0;
	}

	public boolean isComplete(Agent agent)
	{
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
		trainingOrders.clear();
		int trainingOrderCount = 3;
		for (int i = 0; i < trainingOrderCount; i++)
		{			
			CraftPlanner craftPlanner = (CraftPlanner) agent;
			trainingOrders.add(new TrainingOrder (3, UnitType.Terran_Marine, craftPlanner.resourceActuator, craftPlanner.supplyActuator));
			((CraftPlannerBlackboard)agent.getBlackboard()).addTrainingQueue(trainingOrders.get(i));
			//System.out.println("Added a new training order to the queue! Status: " +trainingOrders.get(i).status);
		}		
	}

	public void deactivateAction(Agent agent)
	{
		
	}

	public boolean validateAction(Agent agent)
	{
		int totalBarracks = ((Integer)agent.getWorldState().getPropertyValue("HasBarracks").getValue()).intValue();
		if (totalBarracks < 3)
		{
			agent.getWorldState().setProperty("HasBioProduction", new WorldStateValue(false));
			return false;
		}
		return true;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
//		WorkingMemory workingMemory = agent.getWorkingMemory();
//		WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerUnit, null);
//		
//		int totalMarines = 0;
//		for (WorkingMemoryFact playerFact : playerFacts)
//		{
//			CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
//			Unit playerUnit = (Unit) playerCraftFact.getFact();
//			if (playerUnit.getType() == UnitType.Terran_Marine)
//			{
//				totalMarines = playerCraftFact.getCount();
//				break;
//			}
//		}
//		
//		actualWorldState.setProperty("HasMarines", new WorldStateValue(totalMarines + 10));
	}
	
	
}
