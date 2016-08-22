package Utils;

import java.util.ArrayList;
import java.util.List;

import CraftPlanner.CraftPlannerBlackboard;
import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;

public class UnitUtils
{
	
	public static Unit getUnitOfType (Game game, UnitType type)
	{
		for (Unit unit : game.self().getUnits())
		{
			if (unit.getType().equals(type))
			{
				return unit;
			}
		}
		return null;
	}
	
	public static List<Unit> getAllUnitsOfType (Game game, UnitType type)
	{
		List<Unit> units = new ArrayList<Unit>();
		for (Unit unit : game.self().getUnits())
		{
			if (unit.getType().equals(type))
			{
				units.add(unit);
			}
		}
		return units;
	}
	
	public static Unit getIdleWorker (Game game, CraftPlannerBlackboard blackboard)
	{
		for (Unit unit : UnitUtils.getAllUnitsOfType(game, UnitType.Terran_SCV))
		{
//			if (!blackBoard.isOccupied(unit))
//			{
//				return unit;
//			}
		}
		return null;
	}
	
	public Unit getClosestUnitTo (Game game, Unit unit, UnitType type)
	{
		Unit closestUnit = null;
		double closest = Double.MAX_VALUE;
		
		for (Unit otherUnit : getAllUnitsOfType (game, type))
		{
			double dx = unit.getPosition().getX() - otherUnit.getPosition().getX();
			double dy = unit.getPosition().getY() - otherUnit.getPosition().getY();
			double total = Math.sqrt(Math.pow(dx, dx) +Math.pow(dy, dy));
			
			if (total < closest)
			{
				closestUnit = otherUnit;
				closest = total;
			}
		}
		return closestUnit;
	}
	
	public static int countMilitaryUnits (Game game)
	{
		int count = 0;
		for (Unit unit : game.self().getUnits())
		{
			if (unit.getType().isBuilding())
			{
				continue;
			}
			if (unit.getType().isWorker())
			{
				continue;
			}
			if (unit.getType().isResourceContainer())
			{
				continue;
			}
			if (unit.getHitPoints() <= 0)
			{
				continue;
			}
			count += 1;
		}
		return count;
	}
	
	public static List<Unit> getCompletedUnitsOfType (Game game, UnitType type)
	{
		List<Unit> toCheck = getAllUnitsOfType (game, type);
		List<Unit> completedUnits = new ArrayList<Unit>();
		for (Unit unit : toCheck)
		{
			if (unit.isCompleted())
			{
				completedUnits.add(unit);
			}
		}
		return completedUnits;
	}
	
}
