package Sensors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import Agent.FactType;
import Agent.WorkingMemory;
import Agent.WorkingMemoryFact;
import CraftPlanner.CraftPlannerMemoryFact;
import GameModel.Base;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;

public class BaseSensor
{
	private WorkingMemory workingMemory;
	private Game game;
	private List<Base> bases;

	public BaseSensor (WorkingMemory workingMemory, Game game)
	{
		this.workingMemory = workingMemory;
		this.game = game;
		
		BaseLocation playerBase = BWTA.getNearestBaseLocation(game.self().getStartLocation());
		CraftPlannerMemoryFact baseFact = new CraftPlannerMemoryFact(FactType.PlayerBase, new Base(playerBase, game));
		bases = new ArrayList<Base>();
		bases.clear();
		bases.add (new Base(playerBase, game));
		this.workingMemory.addFact(baseFact);
		List<BaseLocation> enemyBases = new ArrayList<BaseLocation>();
		enemyBases.clear();
		enemyBases.addAll(BWTA.getStartLocations());
		for (BaseLocation baseLocation : enemyBases)
		{
			if (!BWTA.isConnected(playerBase.getTilePosition(), baseLocation.getTilePosition()))
			{
				enemyBases.remove(baseLocation);
				continue;
			}
		}
		for (BaseLocation base : enemyBases)
		{
			TilePosition possibleEnemyLocation = base.getTilePosition();
			CraftPlannerMemoryFact enemyLocation = new CraftPlannerMemoryFact(FactType.EnemyLocation, possibleEnemyLocation);
			this.workingMemory.addFact(enemyLocation);
		}
	}
	
	public void onBaseSight (Unit enemyBase)
	{
	
	}
	
	//Updates all the facts pertaining to bases
	public void update ()
	{
//		for (Base playerBase : bases)
//		{
//			playerBase.update();
//		}
	}

}
