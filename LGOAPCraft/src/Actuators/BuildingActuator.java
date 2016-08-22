package Actuators;

import java.util.ArrayList;
import java.util.List;

import Agent.Agent;
import Agent.Blackboard;
import Agent.FactType;
import Agent.WorkingMemoryFact;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import CraftPlanner.CraftPlannerMemoryFact;
import GameModel.Base;
import GameModel.Building;
import GameModel.BuildingStatus;
import GameModel.Worker;
import GameModel.WorkerStatus;
import Orders.BuildOrder;
import Orders.BuildOrderStatus;
import Orders.ResearchOrder;
import Orders.ResearchOrderStatus;
import Orders.TrainingOrder;
import Orders.TrainingOrderStatus;
import bwapi.Game;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

public class BuildingActuator
{
	private Agent agent;
	private CraftPlannerBlackboard blackboard;
	private ResourceActuator resourceActuator;
	private List<Building> buildings;

	
	public BuildingActuator (Agent agent, Blackboard blackboard, ResourceActuator resourceActuator)
	{
		this.agent = agent;
		this.blackboard = (CraftPlannerBlackboard) blackboard;
		this.resourceActuator = resourceActuator;
		this.buildings = new ArrayList<Building>();
		this.buildings.clear();
	}
	
	private int getRequiredWorkers ()
	{
		int requiredWorkers = 16;
		int totalBases = 0;
		//For each building
		for (Building building : buildings)
		{
			if (building.getBuilding().getType() == UnitType.Terran_Command_Center)
			{
				totalBases += 1;
			}
		}
		return totalBases * requiredWorkers;
	}
	
	public void update ()
	{
		//For each building
		for (Building building : buildings)
		{
			//If the building is still being constructed then ignore it
			if (!building.getBuilding().isCompleted())
			{
				continue;
			}
			
			//If the building is a command center then train SCV's
			if (building.getBuilding().getType() == UnitType.Terran_Command_Center)
			{
				if (!building.getBuilding().isTraining())
				{
					if (((CraftPlanner)agent).workerActuator.getTotalWorkers() < getRequiredWorkers())
					{
						if (resourceActuator.canAfford(UnitType.Terran_SCV))
						{
							building.getBuilding().train(UnitType.Terran_SCV);
						}						
					}
				}
				continue;
			}			
			else
			{
				building.update(blackboard);
			}
		}
	}
	
	public void onBuildingCreate (Unit building)
	{
		if (building.canTrain() || building.canResearch())
		{
			buildings.add(new Building (building));
		}
	}
	
}
