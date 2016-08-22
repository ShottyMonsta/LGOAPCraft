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
import Orders.MilitaryOrder;
import Orders.MilitaryOrderStatus;
import Orders.TrainingOrder;
import Orders.TrainingOrderStatus;
import bwapi.Color;
import bwapi.Game;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class L_Scout extends LGOAPAction
{
	MilitaryOrder militaryOrder;
	
	public void setupConditions()
	{
		preconditions = new WorldState ();
		
		effects = new WorldState();
		effects.setProperty("HasScouted", new WorldStateValue(true));
		
		lowerLayerPreconditions = new WorldState ();
		
		cost = 3;
		priority = 1;	
		
		layer = 0;
	}

	public boolean isComplete(Agent agent)
	{
		if (militaryOrder.status == MilitaryOrderStatus.Finished)
		{
			return true;
		}
		return false;
	}

	public boolean interrupt() {
		return false;
	}

	public void activateAction(Agent agent, WorldState stepState)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		List<BaseLocation> baseLocations = BWTA.getBaseLocations();
		BaseLocation playerBase = BWTA.getStartLocation(craftAgent.game.self());
		int furthestIndex = 0;
		double furthestDistance = 0;
		for (BaseLocation base : baseLocations)
		{
			if (base.getTilePosition().getDistance(playerBase.getTilePosition()) > furthestDistance)
			{
				furthestDistance = base.getTilePosition().getDistance(playerBase.getTilePosition());
				furthestIndex = baseLocations.indexOf(base);
			}
		}		
		Chokepoint closestChoke = BWTA.getNearestChokepoint(baseLocations.get(furthestIndex).getTilePosition());
		//TilePosition target = closestChoke.getCenter().toTilePosition();
		TilePosition target = baseLocations.get(furthestIndex).getTilePosition();
		militaryOrder = new MilitaryOrder(target);
		((CraftPlannerBlackboard)agent.getBlackboard()).addMilitaryQueue(militaryOrder);	
	}

	public void deactivateAction(Agent agent)
	{
		
	}

	public boolean validateAction(Agent agent)
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		boolean hasBioArmy = (Boolean) craftAgent.getWorldState().getPropertyValue("HasBioArmy").getValue();
		return hasBioArmy;
	}

	public void applyEffect(Agent agent, WorldState actualWorldState, WorldState actionEffect)
	{
		actualWorldState.setProperty("HasScouted", new WorldStateValue(true));
	}
	
	
}
