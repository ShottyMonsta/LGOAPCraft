package Actuators;

import java.util.ArrayList;
import java.util.List;
import Agent.Agent;
import Agent.Blackboard;
import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import GameModel.CombatUnit;
import GameModel.Squad;
import GameModel.SquadStatus;
import Orders.MilitaryOrder;
import bwapi.Game;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class SquadActuator
{
	private Agent agent;
	private CraftPlannerBlackboard blackboard;
	private Game game;
	private List<Squad> squads;
	
	public SquadActuator (Agent agent, Blackboard blackboard, Game game)
	{
		this.agent = agent;
		this.blackboard = (CraftPlannerBlackboard) blackboard;
		this.game = game;
		this.squads = new ArrayList<Squad>();
		this.squads.clear();
	}
	
	public void update ()
	{		
		//For each squad
		for (Squad squad : squads)
		{
			squad.update(blackboard);
			switch (squad.status)
			{
				case Dead:
					squads.remove(squad);
					break;
			}
		}
	}
	
	public List<Squad> getSquads ()
	{
		return squads;
	}
	
	private TilePosition getUnitStartLocation ()
	{
		CraftPlanner craftAgent = (CraftPlanner) agent;
		List<BaseLocation> baseLocations = BWTA.getBaseLocations();
		BaseLocation playerBase = BWTA.getStartLocation(craftAgent.game.self());
		int closestIndex = 0;
		double closestDistance = 0;
		for (BaseLocation base : baseLocations)
		{
			if (base.getTilePosition().getDistance(playerBase.getTilePosition()) < closestDistance)
			{
				closestDistance = base.getTilePosition().getDistance(playerBase.getTilePosition());
				closestIndex = baseLocations.indexOf(base);
			}
		}		
		Chokepoint closestChoke = BWTA.getNearestChokepoint(playerBase.getTilePosition());
		return closestChoke.getCenter().toTilePosition();
	}
	
	public void onUnitCreate (Unit newUnit)
	{
		if (squads.size() != 0)
		{
			for (Squad squad : squads)
			{
				if (!squad.isFull())
				{
					if (squad.sameType(newUnit.getType()))
					{
						CombatUnit combatUnit = new CombatUnit(newUnit, getUnitStartLocation());
						squad.addUnit(combatUnit);
						return;
					}
				}
			}
			Squad newSquad = new Squad(newUnit, getUnitStartLocation());
			squads.add(newSquad);
		}
		else
		{
			Squad newSquad = new Squad(newUnit, getUnitStartLocation());
			squads.add(newSquad);
		}		
	}	
}
