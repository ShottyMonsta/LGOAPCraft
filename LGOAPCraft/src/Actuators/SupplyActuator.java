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

public class SupplyActuator
{
	private Agent agent;
	private CraftPlannerBlackboard blackboard;
	private Game game;
	private int currentGas;
	private int actualSupply;
	private int plannedSupply;
	
	public SupplyActuator (Agent agent, Blackboard blackboard, Game game)
	{
		this.agent = agent;
		this.blackboard = (CraftPlannerBlackboard) blackboard;
		this.game = game;
		this.actualSupply = game.self().supplyTotal() / 2;
		this.plannedSupply = game.self().supplyTotal() / 2;
		this.currentGas = 0;
	}
	
	private boolean isSupplyRequired ()
	{
		if (game.self().supplyTotal() - game.self().supplyUsed() <= 4) 
		{
			return true;
		}
		return false;
	}
	
	public int getUseableSupply ()
	{
		return game.self().supplyTotal() - game.self().supplyUsed();
	}
	
	public void update ()
	{
		if (isSupplyRequired())
		{
			if (plannedSupply - actualSupply <= 0)
			{
				BuildOrder buildOrder = new BuildOrder (UnitType.Terran_Supply_Depot, ((CraftPlanner)agent).resourceActuator);
				blackboard.addBuildQueue(buildOrder);			
				plannedSupply += 8;
			}
		}
		
		if (game.elapsedTime() >= 60 && currentGas == 0)
		{
			BuildOrder buildOrder = new BuildOrder (UnitType.Terran_Refinery, ((CraftPlanner)agent).resourceActuator);
			blackboard.addBuildQueue(buildOrder);	
			currentGas += 1;
		}
		
		actualSupply = game.self().supplyTotal() / 2;
	}	
}
