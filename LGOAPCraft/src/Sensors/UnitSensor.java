package Sensors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import Agent.Agent;
import Agent.FactType;
import Agent.WorkingMemory;
import Agent.WorkingMemoryFact;
import CraftPlanner.CraftPlannerMemoryFact;
import GameModel.Base;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class UnitSensor
{
	private WorkingMemory workingMemory;
	private Game game;
	private Agent agent;
	
	private enum raceTypes
	{
		Protoss, Terran, Zerg, Unknown
	}
	private raceTypes myRace;

	
	public UnitSensor (WorkingMemory workingMemory, Game game, Agent agent)
	{
		this.workingMemory = workingMemory;
		this.game = game;		
		this.agent = agent;
	}
	
	//Updates the agents knowledge about what units they actually have when new ones are created
	public void onUnitCreate (Unit unit)
	{
		if (unit.getType().isBuilding())
		{
			//Get everything we know about our buildings
			WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerBuilding, null);
			//Assume we have a new unit type
			boolean newBuilding = true;
			for (WorkingMemoryFact playerFact : playerFacts)
			{
				CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
				Unit playerBuilding = (Unit) playerCraftFact.getFact();
				if (playerBuilding.getType() == unit.getType())
				{
					newBuilding = false;
					playerCraftFact.increaseCount();
					agent.metrics.createBuilding();
					//System.out.println("An existing building type: " +unit.getType() +" has been built again! Current count: " +playerCraftFact.getCount());
					break;
				}
			}
			//If we do indeed have a new unit type then create a new craftFact for it
			if (newBuilding)
			{
				CraftPlannerMemoryFact newBuildingFact = new CraftPlannerMemoryFact  (FactType.PlayerBuilding, unit);
				newBuildingFact.setCount(1);
				newBuildingFact.setConfidence(1.0);
				workingMemory.addFact(newBuildingFact);
				agent.metrics.createBuilding();
				//System.out.println("A new building type was created: " +unit.getType());
			}
		}
		else
		{
			//Get everything we know about our units	
			WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerUnit, null);
			//Assume we have a new unit type
			boolean newUnit = true;
			for (WorkingMemoryFact playerFact : playerFacts)
			{
				CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
				Unit playerUnit = (Unit) playerCraftFact.getFact();
				if (playerUnit.getType() == unit.getType())
				{
					agent.metrics.createUnit();
					newUnit = false;
					playerCraftFact.increaseCount();
					break;
				}
			}
			//If we do indeed have a new unit type then create a new craftFact for it
			if (newUnit)
			{
				agent.metrics.createUnit();
				CraftPlannerMemoryFact newUnitFact = new CraftPlannerMemoryFact  (FactType.PlayerUnit, unit);
				newUnitFact.setCount(1);
				newUnitFact.setConfidence(1.0);
				workingMemory.addFact(newUnitFact);
			}
		}		
	}
	
	//Updates the agents knowledge about what units they actually have when new ones are created
	public void onUnitDestroy (Unit unit)
	{
		if (unit.getType().isBuilding())
		{
			//Get everything we know about our buildings	
			WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerBuilding, null);

			for (WorkingMemoryFact playerFact : playerFacts)
			{
				CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
				Unit playerUnit = (Unit) playerCraftFact.getFact();
				if (playerUnit.getType() == unit.getType())
				{
					agent.metrics.loseBuilding();
					playerCraftFact.decreaseCount();
					//System.out.println("A " +unit.getType() +" was destroyed! Current count: " +playerCraftFact.getCount());
					break;
				}
			}
		}
		else
		{
			//Get everything we know about our units	
			WorkingMemoryFact[] playerFacts = workingMemory.getFactsByType(FactType.PlayerUnit, null);

			for (WorkingMemoryFact playerFact : playerFacts)
			{
				CraftPlannerMemoryFact playerCraftFact = (CraftPlannerMemoryFact) playerFact;
				Unit playerUnit = (Unit) playerCraftFact.getFact();
				if (playerUnit.getType() == unit.getType())
				{
					agent.metrics.loseUnit();
					playerCraftFact.decreaseCount();
					break;
				}
			}
		}
	}
	
	//Updates the agents knowledge about what units they want based off what enemy units have been encountered
	public void update ()
	{
		//Get everything we know about enemies	
		WorkingMemoryFact[] enemyFacts = workingMemory.getFactsByType(FactType.EnemyUnit, null);
		
		//HANDLE COUNTERS IF AGENT IS PLAYING PROTOSS
		if (myRace == raceTypes.Protoss)
		{					
			//Go through each enemy fact and see if we do indeed need to produce a new counter
			for (WorkingMemoryFact enemyFact : enemyFacts)
			{				
				UnitType counterType = UnitType.Unknown;
				UnitType enemyType = UnitType.Unknown;
				int counterCount = 0;
				
				CraftPlannerMemoryFact enemyCraftFact = (CraftPlannerMemoryFact) enemyFact;
				Unit enemyUnit = (Unit) enemyCraftFact.getFact();
				
				enemyType = enemyUnit.getType();
				
				//PROTOSS COUNTERS AGAINST ZERGLING GO HERE!!!
				if (enemyType == UnitType.Zerg_Zergling)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Hydralisk)
				{
					counterType = UnitType.Protoss_Reaver;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Mutalisk)
				{
					counterType = UnitType.Protoss_Dragoon;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Lurker)
				{
					counterType = UnitType.Protoss_Dragoon;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Overlord)
				{
					counterType = UnitType.Protoss_Corsair;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Broodling)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Devourer)
				{
					counterType = UnitType.Protoss_Corsair;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Queen)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = Math.round(enemyCraftFact.getCount()/3);
				}
				else if (enemyType == UnitType.Zerg_Ultralisk)
				{
					counterType = UnitType.Protoss_Dark_Templar;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Guardian)
				{
					counterType = UnitType.Protoss_Corsair;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Scourge)
				{
					counterType = UnitType.Protoss_Corsair;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Drone)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Larva)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				
				
				//PROTOSS COUNTERS AGAINST PROTOSS GO HERE!!!
				else if (enemyType == UnitType.Protoss_Arbiter)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Archon)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Carrier)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Corsair)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Archon)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Templar)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dragoon)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_High_Templar)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Interceptor)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Observer)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Probe)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Reaver)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Scout)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Shuttle)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Zealot)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				
				//PROTOSS COUNTERS AGAINST TERRAN GO HERE!!!
				else if (enemyType == UnitType.Terran_Battlecruiser)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Firebat)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Ghost)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Goliath)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Marine)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Medic)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Science_Vessel)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Tank_Mode)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Siege_Mode)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture_Spider_Mine)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Valkyrie)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Wraith)
				{
					counterType = UnitType.Protoss_Zealot;
					counterCount = enemyCraftFact.getCount();
				}

				
				
				WorkingMemoryFact[] playerWantsFacts = workingMemory.getFactsByType(FactType.PlayerWants, null);
				boolean newType = true;			
				for (WorkingMemoryFact playerWant : playerWantsFacts)
				{
					UnitType wantsType = (UnitType) playerWant.getFact();
					if (counterType == wantsType)
					{
						CraftPlannerMemoryFact craftPlayerWant = (CraftPlannerMemoryFact) playerWant;
						craftPlayerWant.setCount(counterCount);
						newType = false;
						//System.out.println("Player needs " +counterCount +" lots of unit type " +counterType +" to counter existing enemy unit " +enemyType);
						break;
					}
				}
				if (newType)
				{
					CraftPlannerMemoryFact wantsTypeFact = new CraftPlannerMemoryFact (FactType.PlayerWants, counterType);
					wantsTypeFact.setCount(counterCount);
					wantsTypeFact.setConfidence(1.0);
					workingMemory.addFact(wantsTypeFact);
					//System.out.println("Player needs " +counterCount +" lots of new unit type " +counterType +" to counter recently spotted enemy unit " +enemyType);
				}
			}
		}
		//HANDLE COUNTERS IF AGENT IS PLAYING ZERG
		else if (myRace == raceTypes.Zerg)
		{
			//Go through each enemy fact and see if we do indeed need to produce a new counter
			for (WorkingMemoryFact enemyFact : enemyFacts)
			{				
				UnitType counterType = UnitType.Unknown;
				UnitType enemyType = UnitType.Unknown;
				int counterCount = 0;
				
				CraftPlannerMemoryFact enemyCraftFact = (CraftPlannerMemoryFact) enemyFact;
				Unit enemyUnit = (Unit) enemyCraftFact.getFact();
				
				enemyType = enemyUnit.getType();
				
				//ZERG COUNTERS AGAINST ZERGLING GO HERE!!!
				if (enemyType == UnitType.Zerg_Zergling)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Hydralisk)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Mutalisk)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Lurker)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Overlord)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Broodling)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Devourer)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Queen)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/3);
				}
				else if (enemyType == UnitType.Zerg_Ultralisk)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Guardian)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Scourge)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Drone)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Larva)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				
				
				//ZERG COUNTERS AGAINST PROTOSS GO HERE!!!
				else if (enemyType == UnitType.Protoss_Arbiter)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Archon)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Carrier)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Corsair)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Archon)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Templar)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dragoon)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_High_Templar)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Interceptor)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Observer)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Probe)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Reaver)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Scout)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Shuttle)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Zealot)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				
				//ZERG COUNTERS AGAINST TERRAN GO HERE!!!
				else if (enemyType == UnitType.Terran_Battlecruiser)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Firebat)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Ghost)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Goliath)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Marine)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Medic)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Science_Vessel)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Tank_Mode)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Siege_Mode)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture_Spider_Mine)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Valkyrie)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Wraith)
				{
					counterType = UnitType.Zerg_Zergling;
					counterCount = enemyCraftFact.getCount();
				}

				
				
				WorkingMemoryFact[] playerWantsFacts = workingMemory.getFactsByType(FactType.PlayerWants, null);
				boolean newType = true;			
				for (WorkingMemoryFact playerWant : playerWantsFacts)
				{
					UnitType wantsType = (UnitType) playerWant.getFact();
					if (counterType == wantsType)
					{
						CraftPlannerMemoryFact craftPlayerWant = (CraftPlannerMemoryFact) playerWant;
						craftPlayerWant.setCount(counterCount);
						newType = false;
						//System.out.println("Player needs " +counterCount +" lots of unit type " +counterType +" to counter existing enemy unit " +enemyType);
						break;
					}
				}
				if (newType)
				{
					CraftPlannerMemoryFact wantsTypeFact = new CraftPlannerMemoryFact (FactType.PlayerWants, counterType);
					wantsTypeFact.setCount(counterCount);
					wantsTypeFact.setConfidence(1.0);
					workingMemory.addFact(wantsTypeFact);
					//System.out.println("Player needs " +counterCount +" lots of new unit type " +counterType +" to counter recently spotted enemy unit " +enemyType);
				}
			}
		}
		//HANDLE COUNTERS IF AGENT IS PLAYING TERRAN
		else if (myRace == raceTypes.Terran)
		{
			//Go through each enemy fact and see if we do indeed need to produce a new counter
			for (WorkingMemoryFact enemyFact : enemyFacts)
			{				
				UnitType counterType = UnitType.Unknown;
				UnitType enemyType = UnitType.Unknown;
				int counterCount = 0;
				
				CraftPlannerMemoryFact enemyCraftFact = (CraftPlannerMemoryFact) enemyFact;
				Unit enemyUnit = (Unit) enemyCraftFact.getFact();
				
				enemyType = enemyUnit.getType();
				
				//TERRAN COUNTERS AGAINST ZERGLING GO HERE!!!
				if (enemyType == UnitType.Zerg_Zergling)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Hydralisk)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Mutalisk)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Lurker)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Overlord)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Broodling)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Devourer)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Queen)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/3);
				}
				else if (enemyType == UnitType.Zerg_Ultralisk)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Guardian)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				else if (enemyType == UnitType.Zerg_Scourge)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Zerg_Drone)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/4);
				}
				else if (enemyType == UnitType.Zerg_Larva)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = Math.round(enemyCraftFact.getCount()/2);
				}
				
				
				//TERRAN COUNTERS AGAINST PROTOSS GO HERE!!!
				else if (enemyType == UnitType.Protoss_Arbiter)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Archon)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Carrier)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Corsair)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Archon)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dark_Templar)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Dragoon)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_High_Templar)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Interceptor)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Observer)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Probe)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Reaver)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Scout)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Shuttle)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Protoss_Zealot)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				
				//ZERG COUNTERS AGAINST TERRAN GO HERE!!!
				else if (enemyType == UnitType.Terran_Battlecruiser)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Firebat)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Ghost)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Goliath)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Marine)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Medic)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Science_Vessel)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Tank_Mode)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Siege_Tank_Siege_Mode)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture_Spider_Mine)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Valkyrie)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Vulture)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}
				else if (enemyType == UnitType.Terran_Wraith)
				{
					counterType = UnitType.Terran_Marine;
					counterCount = enemyCraftFact.getCount();
				}

				
				
				WorkingMemoryFact[] playerWantsFacts = workingMemory.getFactsByType(FactType.PlayerWants, null);
				boolean newType = true;			
				for (WorkingMemoryFact playerWant : playerWantsFacts)
				{
					UnitType wantsType = (UnitType) playerWant.getFact();
					if (counterType == wantsType)
					{
						CraftPlannerMemoryFact craftPlayerWant = (CraftPlannerMemoryFact) playerWant;
						craftPlayerWant.setCount(counterCount);
						newType = false;
						//System.out.println("Player needs " +counterCount +" lots of unit type " +counterType +" to counter existing enemy unit " +enemyType);
						break;
					}
				}
				if (newType)
				{
					CraftPlannerMemoryFact wantsTypeFact = new CraftPlannerMemoryFact (FactType.PlayerWants, counterType);
					wantsTypeFact.setCount(counterCount);
					wantsTypeFact.setConfidence(1.0);
					workingMemory.addFact(wantsTypeFact);
					//System.out.println("Player needs " +counterCount +" lots of new unit type " +counterType +" to counter recently spotted enemy unit " +enemyType);
				}
			}
		}
	}

}
