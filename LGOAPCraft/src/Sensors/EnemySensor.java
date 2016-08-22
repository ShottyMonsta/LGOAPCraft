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
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class EnemySensor
{
	private WorkingMemory workingMemory;
	private int nearThreshold = 40;
	private Game game;
	
	public EnemySensor (WorkingMemory workingMemory, Game game)
	{
		this.workingMemory = workingMemory;
		this.game = game;
	}
	
	public void onEnemySight (Unit enemyUnit)
	{
		//Get all the known facts about enemy units
		WorkingMemoryFact[] enemyFacts = workingMemory.getFactsByType(FactType.EnemyUnit, null);
		//Assume we've found a new enemy
		boolean newEnemy = true;
		//Go through each existing fact we know about enemy units
		for (WorkingMemoryFact fact : enemyFacts)
		{
			//Convert to a craftFact
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			//Get the unit that fact describes
			Unit factUnit = (Unit) craftFact.getFact();
			UnitType unitType = factUnit.getType();
			
			if (unitType == enemyUnit.getType())
			{
				craftFact.setConfidence(1.0);
				newEnemy = false;
			}			
		}
		if (newEnemy)
		{
			CraftPlannerMemoryFact unitFact = new CraftPlannerMemoryFact(FactType.EnemyUnit, enemyUnit);
			unitFact.setConfidence(1.0);
			unitFact.increaseCount();
			workingMemory.addFact(unitFact);
		}
		else
		{
			WorkingMemoryFact[] unitFacts = workingMemory.getFactsByType(FactType.EnemyUnit, null);
			for (WorkingMemoryFact unitFact : unitFacts)
			{
				Unit unit = (Unit) unitFact.getFact();
				if (unit.getType() == enemyUnit.getType())
				{
					CraftPlannerMemoryFact existingFact = (CraftPlannerMemoryFact) unitFact;
					existingFact.setConfidence(1.0);
					existingFact.increaseCount();
					break;
				}
			}			
		}
		
		
		//HANDLES IF THE ENEMY IS NEAR A BASE
		WorkingMemoryFact[] baseFacts = workingMemory.getFactsByType(FactType.EnemyBase, null);
		boolean nearBase = false;
		for (WorkingMemoryFact fact : baseFacts)
		{
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			Base base = (Base) craftFact.getFact();
			if (enemyUnit.getDistance(base.getPosition().toPosition()) < 400)
			{
				craftFact.setConfidence(1.0);
				nearBase = true;
				break;
			}
		}
		if (!nearBase)
		{
			//IF NOT AT A KNOWN LOCATION THEN CREATE A NEW LOCATION FOR IT
			if (!isNearLocation(enemyUnit))
			{
				CraftPlannerMemoryFact locationFact = new CraftPlannerMemoryFact (FactType.EnemyLocation, enemyUnit.getTilePosition());
				workingMemory.addFact(locationFact);
			}
		}
	}
	
	//Updates all the facts pertaining to enemies
	public void update ()
	{
		List<WorkingMemoryFact> factsToRemove = new ArrayList<WorkingMemoryFact>();
		
		//UPDATE ENEMY UNIT FACTS
		WorkingMemoryFact[] enemyUnitFacts = workingMemory.getFactsByType(FactType.EnemyUnit, null);

		//For each enemy unit fact
		for (WorkingMemoryFact fact : enemyUnitFacts)
		{
			//Convert to a craft fact
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			//Convert to a unit and check if it's still visible
			if (!((Unit) craftFact.getFact()).isVisible())
			{
				//Not visible? Confidence is 0...
				//MAKE THIS BETTER!!!
				craftFact.setConfidence(0.0);
			}
			else
			{
				if (((Unit) craftFact.getFact()).getHitPoints() <= 0)
				{
					//Dead enemy
					factsToRemove.add(fact);
					//Decrease count for that fact
					craftFact.decreaseCount();
				}
				else
				{
					//Handle enemy location 
					Unit unit = (Unit) craftFact.getFact();
					if (!isNearLocation(unit))
					{
						CraftPlannerMemoryFact newLocation = new CraftPlannerMemoryFact (FactType.EnemyLocation, unit.getTilePosition());
						workingMemory.addFact(newLocation);
					}
				}
			}
		}
		//Remove facts from working memory
		for (WorkingMemoryFact toRemove : factsToRemove)
		{
			workingMemory.removeFact(toRemove);
		}		
		factsToRemove.clear();
		
		
		//UPDATE ENEMY LOCATION FACTS
		WorkingMemoryFact[] locationFacts = workingMemory.getFactsByType(FactType.EnemyBase, null);
		for (WorkingMemoryFact fact : locationFacts)
		{
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			TilePosition position = (TilePosition) craftFact.getFact();
			if (game.isVisible(position))
			{
				boolean foundNearEnemy = false;
				for (WorkingMemoryFact enemyFact : enemyUnitFacts)
				{
					CraftPlannerMemoryFact enemyCraftFact = (CraftPlannerMemoryFact) enemyFact;
					Unit enemyUnit = (Unit) enemyCraftFact.getFact();
					if (enemyUnit.getPosition().toTilePosition().getDistance(position) < nearThreshold)
					{
						foundNearEnemy = true;
						break;
					}
				}
				if (!foundNearEnemy)
				{
					factsToRemove.add(craftFact);
				}
			}
		}
		
		
		//UPDATE BASE FACTS
		WorkingMemoryFact[] baseFacts = workingMemory.getFactsByType(FactType.EnemyBase, null);
		for (WorkingMemoryFact fact : baseFacts)
		{
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			Base baseFact = (Base) craftFact.getFact();
			if (game.isVisible(baseFact.getPosition()))
			{
				boolean foundNearEnemy = false;
				for (WorkingMemoryFact enemyFact : enemyUnitFacts)
				{
					CraftPlannerMemoryFact enemyCraftFact = (CraftPlannerMemoryFact) enemyFact;
					Unit enemyUnit = (Unit) enemyCraftFact.getFact();
					if (enemyUnit.getPosition().toTilePosition().getDistance(baseFact.getPosition()) < 300)
					{
						foundNearEnemy = true;
						break;
					}
				}
				if (!foundNearEnemy)
				{
					factsToRemove.add(craftFact);
				}
			}
		}
		//Remove facts from working memory
		for (WorkingMemoryFact toRemove : factsToRemove)
		{
			workingMemory.removeFact(toRemove);
		}		
		factsToRemove.clear();
	}
	
	//Returns true if the passed enemy unit is near the location
	private boolean isNearLocation (Unit enemyUnit)
	{
		WorkingMemoryFact[] facts = workingMemory.getFactsByType(FactType.EnemyUnit, null);
		for (WorkingMemoryFact fact : facts)
		{
			CraftPlannerMemoryFact craftFact = (CraftPlannerMemoryFact) fact;
			Unit unit = (Unit) craftFact.getFact();
			if (enemyUnit.getDistance(unit.getPosition()) < nearThreshold)
			{
				return true;
			}
		}
		return false;
	}
}
