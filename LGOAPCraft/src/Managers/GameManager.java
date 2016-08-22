package Managers;

import java.util.List;

import Agent.FactType;
import Agent.WorkingMemoryFact;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerMemoryFact;
import GameModel.Base;
import bwapi.*;
import bwta.*;

public class GameManager extends DefaultBWListener
{
	private Mirror mirror;
	private Game game;
	private Player self;
	private CraftPlanner agent;
	
	public GameManager ()
	{
		mirror = new Mirror();
        mirror.getModule().setEventListener(this);
        mirror.startGame();
		game = mirror.getGame();		
	}

	public static void main (String[] args)
	{			
		GameManager gameManager = new GameManager();
	}
	
	public void setAgent (CraftPlanner agent)
	{
		this.agent = agent;
	}
	
	public Game getGame ()
	{
		return game;
	}
	
	@Override
    public void onStart()
	{
        game = mirror.getGame();
        self = game.self();
        game.enableFlag(1);
        //System.out.println("Self = " +self);
        List<Player> players = game.getPlayers();
        //System.out.println("Players = " +players.size());

		for (Player p : players)
		{
			if (!p.equals(self))
			{
				//System.out.println("Working");
				game.setVision(p, true);
			}
			//System.out.println("Working");
		}

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        //System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        //System.out.println("Map data ready");
        
        agent = new CraftPlanner (game);
    }
	
	@Override
	public void onFrame()
	{
		game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
		agent.update();
	}
	
	@Override
	public void onUnitShow(Unit unit)
	{
		if (unit.getPlayer() != self)
		{
			if (!isUnitResource(unit))
			{
				if (!isUnitBuilding(unit))
				{
					agent.enemySensor.onEnemySight(unit);
				}
			}
		}		
	}
	
	@Override
	public void onUnitComplete(Unit unit)
	{
		if (unit.getPlayer() == self)
		{
			agent.unitSensor.onUnitCreate(unit);

			if (unit.getType().isBuilding())
			{				
				agent.buildingActuator.onBuildingCreate(unit);
			}
			else if (unit.getType().isAddon())
			{
				agent.buildingActuator.onBuildingCreate(unit);
			}
			else if (unit.getType().isWorker())
			{
				agent.workerActuator.onWorkerCreate(unit);
			}
			else
			{
				agent.squadActuator.onUnitCreate(unit);
			}
		}		
	}

	@Override
	public void onUnitMorph(Unit unit)
	{
		if (unit.getPlayer() == self)
		{
			agent.unitSensor.onUnitCreate(unit);

			if (unit.getType().isWorker())
			{
				agent.workerActuator.onWorkerCreate(unit);
			}
		}		
	}
	
	@Override
	public void onUnitDestroy(Unit unit)
	{
		if (unit.getPlayer() == self)
		{
			agent.unitSensor.onUnitDestroy(unit);
		}
		else
		{
			if (isUnitBuilding(unit))
			{
				agent.metrics.destroyBuilding();
			}
			else
			{
				agent.metrics.killUnit();
			}
		}
	}

	
	public boolean isUnitBuilding (Unit unitToCheck)
	{
		if (unitToCheck.getType() != UnitType.Buildings)
		{
			return false;
		}
		return true;
	}
	
	//Returns true if the passed unit is a resource, else false
	public boolean isUnitResource (Unit unitToCheck)
	{
		if (unitToCheck.getType() != UnitType.Resource_Mineral_Field)
		{
			if (unitToCheck.getType() != UnitType.Resource_Mineral_Field_Type_2)
			{
				if (unitToCheck.getType() != UnitType.Resource_Mineral_Field_Type_3)
				{
					if (unitToCheck.getType() != UnitType.Resource_Vespene_Geyser)
					{
						return false;
					}
				}
			}
		}	
		return true;
	}
	
	@Override
	public void onEnd(boolean isWinner)
	{
		//agent.metrics.makeOutput();
	}
	
}
