package GameModel;

import java.util.List;

import CraftPlanner.CraftPlanner;
import CraftPlanner.CraftPlannerBlackboard;
import Orders.MilitaryOrder;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitCommand;
import bwapi.UnitCommandType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class CombatUnit
{
	private Unit unit;
	public CombatUnitStatus status;
	private TilePosition currentTarget;
	private TilePosition startingPosition;

	public CombatUnit (Unit unit, TilePosition startingPosition)
	{
		this.unit = unit;
		this.status = CombatUnitStatus.Alive;
		this.currentTarget = unit.getInitialTilePosition();
		this.startingPosition = startingPosition;
		unit.move(startingPosition.toPosition());
	}	
	
	public void update ()
	{
		if (unit.getHitPoints() <= 0)
		{
			status = CombatUnitStatus.Dead;
		}
		switch (status)
		{
			case Alive:
				if (unit.getLastCommand() != null)
				{
					UnitCommand currentCommand = unit.getLastCommand();
					if (currentCommand.getUnitCommandType() != UnitCommandType.Attack_Move)
					{
						unit.attack(currentTarget.toPosition());
					}		
				}						
				break;
		}		
	}
	
	public void setTarget (TilePosition target)
	{
		this.currentTarget = target;
	}
	
	public void cancelTarget ()
	{
		this.currentTarget = startingPosition;
	}
	
	public Unit getUnit ()
	{
		return unit;
	}
}
